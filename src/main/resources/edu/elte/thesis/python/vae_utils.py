from __future__ import absolute_import, division, print_function

import tensorflow as tf
import json
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import imageio
import glob
import PIL
import datetime
import time
import os

from IPython import display
from cvae import CVAE

tfe = tf.contrib.eager


def calculate_binary_maze_size(dimension):
    binary_dimension = 1 + 2 * dimension
    
    if binary_dimension % 4 == 3:
        binary_dimension += 1
    elif binary_dimension % 4 == 2:
        binary_dimension += 2
    elif binary_dimension % 4 == 1:
        binary_dimension += 3
        
    return binary_dimension
    
    
def save_model_weights(model, filename): 
    # serialize weights to HDF5
    model.save_weights(filename)
    print("saved model to " + filename)


def load_model_weights(latent_dim, dimension, filename):
    model = CVAE(latent_dim, dimension)
    model.load_weights(filename)
    return model

    
def get_data_from_json(filename):
    np_array = []
    with open(filename) as f:
        for line in f:
            maze = json.loads(line)['rows']
            np_maze = []
            for row in maze:
                np_maze.append(np.array([int(r) for r in row]))
            np_array.append(np.array(np_maze))

    np_array = np.array(np_array)
    return np_array


def write_data_to_json(array, filename):
    with open(filename, 'a', 1) as f:
        d = dict()
        d['rows'] = array
        f.write("{}\n".format(json.dumps(d, separators=(",", ":"))))
        
        
def log_normal_pdf(sample, mean, logvar, raxis=1):
    log2pi = tf.log(2. * np.pi)
    return tf.reduce_sum(
        -.5 * ((sample - mean) ** 2. * tf.exp(-logvar) + logvar + log2pi),
        axis=raxis)


def compute_loss(model, x):
    mean, logvar = model.encode(x)
    z = model.reparameterize(mean, logvar)
    x_logit = model.decode(z)
    
    cross_ent = tf.nn.sigmoid_cross_entropy_with_logits(logits=x_logit, labels=x)
    logpx_z = -tf.reduce_sum(cross_ent, axis=[1, 2, 3])
    logpz = log_normal_pdf(z, 0., 0.)
    logqz_x = log_normal_pdf(z, mean, logvar)
    return -tf.reduce_mean(logpx_z + logpz - logqz_x)


def compute_gradients(model, x):
    with tf.GradientTape() as tape:
        loss = compute_loss(model, x)
    return tape.gradient(loss, model.trainable_variables), loss

    
def apply_gradients(optimizer, gradients, variables, global_step=None):
    optimizer.apply_gradients(zip(gradients, variables), global_step=global_step)

    
def save_prediction_matrix_to_file(prediction, vae_location, vae_generated_filename):
    binarized = np.where(prediction >= .5, 1, 0)
    flattened = []
    for sublist in binarized:
        val = ''.join(str(x) for x in sublist.tolist())
        flattened.append(val)
    
    if not os.path.exists(vae_location):
        os.makedirs(vae_location)

    write_data_to_json(flattened, os.path.join(vae_location, vae_generated_filename))


def generate_and_save_images(model, epoch, test_input, vae_location, vae_generated_filename, maze_size):
    predictions = model.sample(test_input)
    fig = plt.figure(figsize=(1,1))

    for i in range(predictions.shape[0]):
        plt.subplot(1, 1, i+1)
        plt.imshow(predictions[i, :, :, 0], cmap='gray')
        plt.axis('off')
        save_prediction_matrix_to_file(predictions[i, :, :, 0], vae_location, vae_generated_filename)

    # tight_layout minimizes the overlap between 2 sub-plots
    plt.savefig(os.path.join(vae_location, '{:03d}_image_at_epoch_{:04d}.png'.format(maze_size, epoch)))


def display_image(epoch_no, vae_location, maze_size):
    return PIL.Image.open(os.path.join(vae_location, '{:03d}_image_at_epoch_{:04d}.png'.format(maze_size, epoch_no)))


def create_epochs_gif(path_to_images, maze_size):
    gif_file = os.path.join(path_to_images, '{:03d}_cvae.gif'.format(maze_size))
    with imageio.get_writer(gif_file, mode='I') as writer:
        filenames = glob.glob(os.path.join(path_to_images, '{:03d}_image*.png'.format(maze_size)))
        filenames = sorted(filenames)
        last = -1
        for i,filename in enumerate(filenames):
            frame = 2*(i**0.5)
            if round(frame) > round(last):
                last = frame
            else:
                continue
            image = imageio.imread(filename)
            writer.append_data(image)
        image = imageio.imread(filename)
        writer.append_data(image)
    
    
def train_model(epochs, train_dataset, test_dataset, model, optimizer, random_vector_for_generation,
                statistics_file, vae_location, vae_generated_filename, maze_size, vae_model_file):
    generate_and_save_images(model, 
                             0, 
                             random_vector_for_generation, 
                             vae_location,
                             vae_generated_filename,
                             maze_size)

    with open(statistics_file, "a", 1) as f:
        f.write(datetime.datetime.today().strftime('%Y-%m-%d %H:%M:%S') + '\n')
        for epoch in range(1, epochs + 1):
            start_time = time.time()
            for train_x in train_dataset:
                gradients, loss = compute_gradients(model, train_x)
                apply_gradients(optimizer, gradients, model.trainable_variables)
            end_time = time.time()
            
            if epoch % 1 == 0:
                loss = tfe.metrics.Mean()

                for test_x in test_dataset:
                    loss(compute_loss(model, test_x))
                elbo = -loss.result()
                display.clear_output(wait=False)
                print('Epoch: {}, ELBO: {}, elapsed time for current epoch {}'.format(epoch,
                                                                elbo,
                                                                end_time - start_time))
                f.write("Epoch: {}, Test set ELBO: {}, elapsed time for current epoch {}\n".format(epoch,
                                                                                                  elbo,
                                                                                                  end_time - start_time))
                generate_and_save_images(model, epoch, random_vector_for_generation, vae_location, vae_generated_filename, maze_size)
            if epoch % 10 == 0:
                save_model_weights(model, vae_model_file)

