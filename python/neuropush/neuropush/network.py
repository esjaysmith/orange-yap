import numpy as np


def relu(x):
    return np.maximum(x, 0, x)


def create_rnn(num_input, num_hidden, num_output, weights=None, seed=42):
    l1 = num_input * num_hidden
    l2 = num_hidden * num_hidden
    l3 = num_hidden * num_output
    num_weights = l1 + l2 + l3
    if weights is None or len(weights) == 0:
        local_state = np.random.RandomState(seed)
        weights = 2 * local_state.uniform(0, 1, num_weights) - 1
    elif len(weights) < num_weights:
        page_size = int(num_weights * 1. / len(weights))
        new_weights = []
        for w in weights:
            local_state = np.random.RandomState(min(2 ** 32 - 1, abs(int(w * 1e3))))
            new_weights.extend([local_state.uniform() * 2 - 1 for _ in range(page_size)])
        weights = new_weights
    elif len(weights) > num_weights:
        for i in range(len(weights) - num_weights):
            weights[i % num_weights] += weights[num_weights + i]

    w_in = np.resize(weights[:l1], (num_input, num_hidden))
    w_h = np.resize(weights[l1:l1 + l2], (num_hidden, num_hidden))
    w_out = np.resize(weights[l1 + l2:l1 + l2 + l3], (num_hidden, num_output))
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
