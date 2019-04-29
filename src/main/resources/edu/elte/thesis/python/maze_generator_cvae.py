from __future__ import absolute_import, division, print_function

# Import TensorFlow >= 1.9 and enable eager execution
import tensorflow as tf

import argparse
import sys
import os
import time
import glob
import datetime
import vae_utils as utils
from cvae import CVAE

tfe = tf.contrib.eager
tf.enable_eager_execution()

parser = argparse.ArgumentParser()
optional = parser._action_groups.pop()
required = parser.add_argument_group('required arguments')

required.add_argument('--dimension', type=int, required=True, help='The size of the symmetrical maze. E.g. If a maze is 12x12, the DIMENSION should be 12')
required.add_argument('--model_file', type=str, required=True, help='The file either containing a CVAE model or where the model should be saved. NOTE: The provided dimension must match the dimension of the mazes and the dimension of the model\'s layers.')

optional.add_argument('--load_model', action='store_true', help="Indicator if the model should be loaded from the provided file or a new model should be created and trained. (default: %(default)s)")
optional.add_argument('--generate_only', action='store_true', help="Indicator if the model should only generate mazes or it should be trained further. NOTE: If this is selected, the epochs will be ignored. (default: %(default)s)")
optional.add_argument('--training_data', type=str, help='The file containing the training data. NOTE: The provided dimension must match The dimension of the training mazes in this file.')
optional.add_argument('--epochs', type=int, default=20, help="The number of training turns. (default: %(default)d)")

parser._action_groups.append(optional)
arguments = parser.parse_args()

base_dir = os.path.join(os.path.expanduser('~'), 'maze_generator')
statistics_location = os.path.join(base_dir, 'statistics')
training_data_location = os.path.join(base_dir, 'training_data')
vae_location = os.path.join(base_dir, 'cvae')

dimension = utils.calculate_binary_maze_size(arguments.dimension)
vae_generated_filename = "generated_cvae_mazes_" + str(arguments.dimension) + "x" + str(arguments.dimension) + ".txt"

latent_dim = 50
num_examples_to_generate = 1

vae_model_file = os.path.join(vae_location, arguments.model_file)
if arguments.load_model:
    model = utils.load_model_weights(latent_dim, dimension, vae_model_file)
else:
    model = CVAE(latent_dim, dimension)


# keeping the random vector constant for generation (prediction) so
# it will be easier to see the improvement.
random_vector_for_generation = tf.random_normal(shape=[num_examples_to_generate, latent_dim])

if arguments.generate_only:
    utils.generate_and_save_images(model, 
                                   0,
                                   random_vector_for_generation,
                                   vae_location,
                                   vae_generated_filename,
                                   arguments.dimension)
else:
    data_location = os.path.join(training_data_location, arguments.training_data)
    train_input = utils.get_data_from_json(data_location)
    test_input = train_input

    train_input = train_input.reshape(train_input.shape[0], dimension, dimension, 1).astype('float32')
    test_input = test_input.reshape(test_input.shape[0], dimension, dimension, 1).astype('float32')

    TRAIN_BUF = 4 * (train_input.shape[0] / 5)
    BATCH_SIZE = 100

    TEST_BUF = train_input.shape[0] / 5

    train_dataset = tf.data.Dataset.from_tensor_slices(train_input).shuffle(TRAIN_BUF).batch(BATCH_SIZE)
    test_dataset = tf.data.Dataset.from_tensor_slices(test_input).shuffle(TEST_BUF).batch(BATCH_SIZE)

    optimizer = tf.train.AdamOptimizer(learning_rate=1e-4)

    vae_statistics_file = os.path.join(statistics_location, "cvae_statistics.txt")
    utils.train_model(arguments.epochs, 
                      train_dataset, 
                      test_dataset, 
                      model, 
                      optimizer, 
                      random_vector_for_generation, 
                      vae_statistics_file, 
                      vae_location,
                      vae_generated_filename,
                      arguments.dimension,
                      vae_model_file)
    utils.create_epochs_gif(vae_location, arguments.dimension)

    if not arguments.generate_only:
        utils.save_model_weights(model, vae_model_file)
    
