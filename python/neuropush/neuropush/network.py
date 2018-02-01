import copy

import numpy as np


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def relu(x):
    return np.maximum(x, 0, x)


def step_x(x):
    return 0 if sigmoid(x) < .5 else 1


step = np.vectorize(step_x)


def create_local_random_from_float(f):
    return np.random.RandomState(min(2 ** 32 - 1, int(abs(f) * 1e6)))


def create_rnn(num_input, num_hidden, num_output, variance=1, floats=None):
    # Set small (abs) weights to zero. The amount is determined by a percentage interpreted
    # from the ints stack.
    floats = [.22] if floats is None else floats
    l1 = num_input * num_hidden
    l2 = num_hidden * num_hidden
    l3 = num_hidden * num_output
    b2 = num_hidden
    b3 = num_output
    num_weights = l1 + l2 + l3
    weights = np.zeros(num_weights)
    for f in floats:
        local_state = create_local_random_from_float(f)
        weights += local_state.normal(0, variance, num_weights)
    # for i, f in enumerate(floats):
    #     weights[i % num_weights] += f
    # Take the first float as threshold to zero-out weights.
    # _rnd = create_local_random_from_float(floats[0])
    # zero_prob = abs(floats[0])
    # _rnd_array = _rnd.random_sample(num_weights)
    # weights[_rnd_array > zero_prob] = 0

    w_in = np.resize(weights[:l1], (num_input, num_hidden))
    w_h = np.resize(weights[l1:l1 + l2], (num_hidden, num_hidden))
    w_out = np.resize(weights[l1 + l2:l1 + l2 + l3], (num_hidden, num_output))
    b_h = np.resize(weights[l1 + l2 + l3:l1 + l2 + l3 + b2], (1, num_hidden))
    b_out = np.resize(weights[l1 + l2 + l3 + b2:l1 + l2 + l3 + b2 + b3], (1, num_output))
    return RNN(w_in, w_h, w_out, b_h, b_out)


class RNN(object):
    def __init__(self, weight_in, weight_h, weight_out, bias_h, bias_out):
        self.num_hidden = weight_h.shape[0]
        self.weight_in = weight_in
        self.weight_h = weight_h
        self.weight_out = weight_out
        self.bias_h = bias_h
        self.bias_out = bias_out
        self.activation_h = None
        self.parameter_count = self.weight_in.shape[0] * self.weight_in.shape[1] + \
                               self.num_hidden ** 2 + self.weight_out.shape[0] * self.weight_out.shape[1] + \
                               len(self.bias_h) + len(self.bias_out)
        self.reset()

    def count_parameters(self):
        return self.parameter_count

    def reset(self):
        self.activation_h = np.zeros(self.num_hidden)

    def forward(self, data):
        layer_1 = relu(np.dot(data, self.weight_in) + np.dot(self.activation_h, self.weight_h)) - self.bias_h
        self.activation_h = copy.deepcopy(layer_1)
        out = np.dot(layer_1, self.weight_out) - self.bias_out
        if np.isnan(np.min(out)):
            raise AssertionError("Nan encountered in output.")
        return out
