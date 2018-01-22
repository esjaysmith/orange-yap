import numpy as np


def relu(x):
    return np.maximum(x, 0, x)


def create_rnn(num_input, num_hidden, num_output, w=None):
    l1 = num_input * num_hidden
    l2 = num_hidden * num_hidden
    l3 = num_hidden * num_output
    num_weights = l1 + l2 + l3
    if w is None:
        w = 2 * np.random.random(num_weights) - 1
    elif len(w) < num_weights:
        raise AssertionError("Cannot read {} from {} values.".format(num_weights, len(w)))
    w_in = np.resize(w[:l1], (num_input, num_hidden))
    w_h = np.resize(w[l1:l1 + l2], (num_hidden, num_hidden))
    w_out = np.resize(w[l1 + l2:l1 + l2 + l3], (num_hidden, num_output))
    return RNN(w_in, w_h, w_out)


class RNN(object):
    def __init__(self, weight_in, weight_h, weight_out):
        self.num_hidden = weight_h.shape[0]
        self.weight_in = weight_in
        self.weight_h = weight_h
        self.weight_out = weight_out
        self.activation_h = None
        self.reset()

    def reset(self):
        self.activation_h = np.zeros(self.num_hidden)

    def forward(self, data):
        layer_1 = relu(np.dot(data, self.weight_in) + np.dot(self.activation_h, self.weight_h))
        self.activation_h = layer_1
        out = np.dot(layer_1, self.weight_out)
        if np.isnan(np.min(out)):
            raise AssertionError("Nan encountered in output.")
        return out
