import numpy as np
import matplotlib.pyplot as plt
import argparse
import os

parser = argparse.ArgumentParser()
optional = parser._action_groups.pop()
required = parser.add_argument_group('required arguments')
required.add_argument('--filename', type=str, required=True, help='The file containing the measurements')
parser._action_groups.append(optional)
arguments = parser.parse_args()

length_of_mazes = []
with open(arguments.filename) as file:
    for line in file:
        values = line.split(",")
        for value in values:
            length_of_mazes.append(int(value))

data = np.array(length_of_mazes)

# the histogram of the data
n, bins, patches = plt.hist(data, bins=35, align='left')

plt.ylabel('Number of mazes')
if "walks" in arguments.filename:
    plt.xlabel('Walk\'s length')
    plt.title('Histogram of the longest walks')
elif "disconnected" in arguments.filename:
    plt.xlabel('Disconnected part count')
    plt.title('Histogram of the number of disconnected parts in a maze')
plt.grid(True)
plt.show()
plt.close('all')
