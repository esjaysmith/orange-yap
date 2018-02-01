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

    def create_network(self, floats):
        return create_rnn(self.dimension, self.num_hidden, self.dimension, floats=floats)

    def train_fitness(self, network, echo=False):
        return self._net_fitness(self.x_train, self.y_train, network, echo=echo)

    def test_fitness(self, network, echo=False):
        return self._net_fitness(self.x_test, self.y_test, network, echo=echo)

    def fingerprint(self, network):
        _values = np.zeros(len(self.x_train) * self.dimension)
        try:
            for i in range(len(self.x_train)):
                a, b = self.x_train[i]
                network.reset()
                network.forward(a)
                _values[i * self.dimension: (i * self.dimension) + self.dimension] = step(network.forward(b))[0]
        except ArithmeticError:
            pass
        finally:
            return _values

    def _net_fitness(self, x_data, y_label, _net, echo=False):
        _max_score = 1. * len(x_data) * self.dimension
        _score = _max_score
        try:
            for i in range(len(x_data)):
                a, b = x_data[i]
                c = y_label[i]
                _net.reset()
                _net.forward(a)
                _out = step(_net.forward(b))[0]
                _score -= sum((c - _out) ** 2)
                if echo:
                    int_a = binary2int(a)
                    int_b = binary2int(b)
                    print("{} + {} = {} ({}) err:{}".format(int_a, int_b, c, _out, _score))
            return _score / _max_score
        except ArithmeticError:
            return 0.
