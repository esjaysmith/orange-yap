import multiprocessing

import numpy as np
from joblib import Parallel, delayed

from data import binary_sum_train_test
from network import create_rnn


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


class EvolveBinarySumDirect(object):
    def __init__(self, dimension=8):
        self.dimension = dimension
        self.x_train, self.y_train, self.x_test, self.y_test = binary_sum_train_test(binary_dim=dimension)

    def train_fitness(self, floats):
        return self._fitness(self.x_train, self.y_train, floats)

    def test_fitness(self, floats):
        return self._fitness(self.x_test, self.y_test, floats)

    def _fitness(self, x_data, y_label, floats):
        _net = create_rnn(self.dimension, self.dimension * 2, self.dimension, weights=floats)
        _err = 1
        for i in range(len(x_data)):
            a, b = x_data[i]
            _net.reset()
            _net.forward(a)
            _out = sigmoid(_net.forward(b))
            _err += sum(abs(np.array(y_label[i]) - np.array(_out))) * 1. / self.dimension
        return 1. / _err


problem = EvolveBinarySumDirect()


def evaluate_fitness(_floats):
    return problem.train_fitness(_floats)


def do_generation(gateway, num_cpu=multiprocessing.cpu_count()):
    chromosomes = gateway.listNewChromosomes()
    [gateway.execute(c.getMachine()) for c in chromosomes]
    machines = [c.getMachine() for c in chromosomes]
    _floats = [list(m.floats().toList()) for m in machines]
    fitness = Parallel(n_jobs=num_cpu)(delayed(evaluate_fitness)(_floats[idx]) for idx in range(len(chromosomes)))
    for idx in range(len(chromosomes)):
        chromosomes[idx].setFitness(fitness[idx])
    print(gateway.getFittestChromosome())
    gateway.nextGeneration()
