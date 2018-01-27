import numpy as np

from network import create_rnn, step


def binary2int(d):
    out = 0
    for index, x in enumerate(reversed(d)):
        out += x * pow(2, index)
    return out


def int2binary(value, dimension):
    if pow(2, dimension) <= value:
        raise AssertionError("The binary representation does not fit the dimension specified.")
    return [int(c) for c in np.binary_repr(value).zfill(dimension)]


def binary_sum_train_test(dimension=8, num_samples=200, train_split=.5):
    map_int2binary = {}
    largest_number = pow(2, dimension)
    for i in range(largest_number):
        map_int2binary[i] = int2binary(i, dimension)
    samples = set()
    max_tries = num_samples ** 2
    tries = 0
    while len(samples) < num_samples:
        if tries > max_tries:
            raise AssertionError("Cannot fill the samples under current conditions. Samples={} max-tries={}".format(num_samples, max_tries))
        tries += 1
        samples.add((np.random.randint(largest_number / 2), np.random.randint(largest_number / 2)))
    samples = list(samples)
    cut = int(num_samples * train_split)
    x_train = [(map_int2binary[p[0]], map_int2binary[p[1]]) for p in samples[:cut]]
    x_test = [(map_int2binary[p[0]], map_int2binary[p[1]]) for p in samples[cut:]]
    y_train = [map_int2binary[p[0] + p[1]] for p in samples[:cut]]
    y_test = [map_int2binary[p[0] + p[1]] for p in samples[cut:]]
    return x_train, y_train, x_test, y_test


class EvolveBinarySumDirect(object):
    def __init__(self, dimension=8, num_hidden=100, num_samples=200):
        self.dimension = dimension
        self.num_hidden = num_hidden
        self.x_train, self.y_train, self.x_test, self.y_test = binary_sum_train_test(dimension=dimension, num_samples=num_samples)

    def train_fitness(self, floats, echo=False):
        return self._fitness(self.x_train, self.y_train, floats, echo=echo)

    def test_fitness(self, floats, echo=False):
        return self._fitness(self.x_test, self.y_test, floats, echo=echo)

    def _fitness(self, x_data, y_label, floats, echo=False):
        if len(floats) < 1:
            return 0.
        _net = create_rnn(self.dimension, self.num_hidden, self.dimension, floats=floats)
        _err = 0
        try:
            for i in range(len(x_data)):
                a, b = x_data[i]
                c = y_label[i]
                _net.reset()
                _net.forward(a)
                _out = step(_net.forward(b))[0]
                _err += sum(abs(c - _out))
                if echo:
                    int_a = binary2int(a)
                    int_b = binary2int(b)
                    print("{} + {} = {} ({}) err:{}".format(int_a, int_b, c, _out, _err))
            return 1. / _err
        except ArithmeticError:
            return 0.
