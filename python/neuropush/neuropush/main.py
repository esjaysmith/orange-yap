import multiprocessing

import numpy as np
from joblib import Parallel, delayed

from neuropush.binarysum import EvolveBinarySumDirect

np.seterr(all='raise')

problem = EvolveBinarySumDirect(dimension=3, num_hidden=100, num_samples=16)

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
