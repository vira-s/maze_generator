from __future__ import absolute_import, division, print_function

# Import TensorFlow >= 1.9 and enable eager execution
import tensorflow as tf

import argparse
import os
import vae_utils as utils

tfe = tf.contrib.eager
tf.enable_eager_execution()

parser = argparse.ArgumentParser()
optional = parser._action_groups.pop()
required = parser.add_argument_group('required arguments')

required.add_argument('--dimension', type=int, required=True, help='The size of the symmetrical maze. E.g. If a maze is 12x12, the DIMENSION should be 12')
required.add_argument('--count', type=int, required=True, help='The number of mazes to generate with the CVAE model.')
required.add_argument('--model_file', type=str, required=True, help='The file either containing a CVAE model or where the model should be saved. NOTE: The provided dimension must match the dimension of the mazes and the dimension of the model\'s layers.')

parser._action_groups.append(optional)
arguments = parser.parse_args()

base_dir = os.path.join(os.path.expanduser('~'), 'maze_generator')
vae_location = os.path.join(base_dir, 'cvae')
vae_model_file = os.path.join(vae_location, arguments.model_file)

dimension = utils.calculate_binary_maze_size(arguments.dimension)
vae_generated_filename = "cvae_mazes_" + str(arguments.dimension) + "x" + str(arguments.dimension) + ".txt"

latent_dim = 50
num_examples_to_generate = 1
model = utils.load_model_weights(latent_dim, dimension, vae_model_file)

for i in range(0, arguments.count):
    if i % 10 == 0:
        print(i)
    random_vector_for_generation = tf.random_normal(shape=[num_examples_to_generate, latent_dim])
    utils.generate_and_save_images(model,
                                   0,
                                   random_vector_for_generation,
                                   vae_location,
                                   vae_generated_filename,
                                   arguments.dimension)
