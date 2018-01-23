import numpy as np


def binary_sum_train_test(binary_dim=8, num_samples=int(1e2), train_split=.5):
    int2binary = {}
    largest_number = pow(2, binary_dim)
    binary = np.unpackbits(np.array([range(largest_number)], dtype=np.uint8).T, axis=1)
    for i in range(largest_number):
        int2binary[i] = binary[i]
    samples = set()
    while len(samples) < num_samples:
        samples.add((np.random.randint(largest_number / 2), np.random.randint(largest_number / 2)))
    samples = list(samples)
    cut = int(num_samples * train_split)
    x_train = [(int2binary[p[0]], int2binary[p[1]]) for p in samples[:cut]]
    x_test = [(int2binary[p[0]], int2binary[p[1]]) for p in samples[cut:]]
    y_train = [int2binary[p[0] + p[1]] for p in samples[:cut]]
    y_test = [int2binary[p[0] + p[1]] for p in samples[cut:]]
    return x_train, y_train, x_test, y_test
