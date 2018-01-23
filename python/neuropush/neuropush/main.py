import multiprocessing

import numpy as np
from joblib import Parallel, delayed

from data import binary_sum_train_test
from network import create_rnn, step

np.seterr(all='raise')

binary_dim = 8
n_samples = 200

input_dim = 2
hidden_dim = 500
output_dim = 1


def binary2int(d):
    out = 0
    for index, x in enumerate(reversed(d)):
        out += x * pow(2, index)
    return out


class EvolveBinarySumDirect(object):
    def __init__(self, dimension=binary_dim, num_samples=n_samples):
        self.dimension = dimension
        self.x_train, self.y_train, self.x_test, self.y_test = binary_sum_train_test(binary_dim=dimension, num_samples=num_samples)

    def train_fitness(self, floats, echo=False):
        return self._fitness(self.x_train, self.y_train, floats, echo=echo)

    def test_fitness(self, floats, echo=False):
        return self._fitness(self.x_test, self.y_test, floats, echo=echo)

    @staticmethod
    def _fitness(x_data, y_label, floats, echo=False):
        if len(floats) < 1:
            return 0.
        _net = create_rnn(input_dim, hidden_dim, output_dim, floats=floats)
        _correct = 0
        _err = 0
        try:
            for i in range(len(x_data)):
                a, b = x_data[i]
                c = y_label[i]
                _net.reset()
                _bin_out = np.zeros_like(c)
                _local_err = 0
                for position in range(binary_dim):
                    _x = np.array([[a[binary_dim - position - 1], b[binary_dim - position - 1]]])
                    _y = np.array([[c[binary_dim - position - 1]]]).T
                    _out = step(_net.forward(_x))
                    _local_err += abs(_y[0][0] - _out[0][0])
                    _bin_out[position] = _out[0][0]
                _bin_out = [_bin_out[-1 * i] for i in range(1, len(_bin_out) + 1)]
                if echo:
                    int_a = binary2int(a)
                    int_b = binary2int(b)
                    print("{} + {} = {} ({}) le:{}".format(int_a, int_b, c, _bin_out, _local_err))
                if _local_err == 0:
                    _correct += 1
                _err += _local_err
            return (_correct * 1. / len(x_data)) + 1. / (_err + len(x_data))
        except ArithmeticError:
            return 0.


problem = EvolveBinarySumDirect()

history = {}


def evaluate_fitness(_floats):
    _key = str(_floats)
    if _key in history:
        return history[_key]
    return problem.train_fitness(_floats)


def do_generation(gateway, num_cpu=multiprocessing.cpu_count()):
    chromosomes = gateway.listNewChromosomes()
    machines = [gateway.executeProgram(c.getProgram()) for c in chromosomes]
    _floats = [list(m.floats().toList()) for m in machines]
    fitness = Parallel(n_jobs=num_cpu)(delayed(evaluate_fitness)(_floats[idx]) for idx in range(len(chromosomes)))
    for idx in range(len(chromosomes)):
        chromosomes[idx].setFitness(fitness[idx])
        history[str(_floats[idx])] = fitness[idx]
    print(gateway.getFittest())
    print("Tested {} new chromosomes.".format(len(chromosomes)))
    gateway.nextGeneration()


def do_generations(gateway, num=100, num_cpu=multiprocessing.cpu_count()):
    history.clear()
    for _ in range(num):
        do_generation(gateway, num_cpu=num_cpu)
