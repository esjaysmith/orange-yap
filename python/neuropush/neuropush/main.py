#!/usr/bin/env python

import argparse
import multiprocessing

import numpy as np
from joblib import Parallel, delayed
from py4j.java_gateway import JavaGateway
from sklearn.neighbors import BallTree

from neuropush.binarysum import EvolveBinarySumDirect

np.seterr(all='raise')

fitness_cache = {}
behaviors_archive = []
history = []


def evaluate_fitness(_floats):
    _key = str(_floats)
    if _key in fitness_cache:
        return fitness_cache[_key]
    _net = problem.create_network(_floats)
    return problem.train_fitness(_net)


def evaluate_behavior(_floats):
    _net = problem.create_network(_floats)
    return problem.fingerprint(_net)


def do_fitness_generation(num_cpu=multiprocessing.cpu_count()):
    gateway.nextGeneration()
    chromosomes = gateway.listNewChromosomes()
    machines = [gateway.executeProgram(c.getProgram()) for c in chromosomes]
    _floats = [list(m.floats().toList()) for m in machines]
    fitness = Parallel(n_jobs=num_cpu)(delayed(evaluate_fitness)(_f) for _f in _floats)
    for idx in range(len(chromosomes)):
        score = fitness[idx]
        chromosomes[idx].setFitness(score)
        fitness_cache[str(_floats[idx])] = score
        if len(history) < 1 or history[-1][0] < score:
            history.append((score, chromosomes[idx].toCodeString()))
            print(history[-1])
    print("Tested {} new chromosomes.".format(len(chromosomes)))
    behaviors = Parallel(n_jobs=num_cpu)(delayed(evaluate_behavior)(_f) for _f in _floats)
    for b in behaviors:
        if b.tolist() not in behaviors_archive:
            behaviors_archive.append(b.tolist())


def do_novelty_generation(num_cpu=multiprocessing.cpu_count(), min_distance=1.):
    gateway.nextGeneration()
    chromosomes = gateway.listNewChromosomes()
    machines = [gateway.executeProgram(c.getProgram()) for c in chromosomes]
    _floats = [list(m.floats().toList()) for m in machines]
    behaviors = Parallel(n_jobs=num_cpu)(delayed(evaluate_behavior)(_f) for _f in _floats)
    ball = BallTree(behaviors_archive, metric='euclidean')
    _indexes = []
    for i, candidate in enumerate(behaviors):
        dist, _ = ball.query([candidate], k=1)
        if dist[0][0] > min_distance:
            behaviors_archive.append(candidate.tolist())
            chromosomes[i].setFitness(dist[0][0])
            _indexes.append(i)
            score = evaluate_fitness(_floats[i])
            if len(history) < 1 or history[-1][0] < score:
                history.append((score, chromosomes[i].toCodeString()))
                print(history[-1])
    print("Added {} new behaviors.".format(len(_indexes)))


def do_fitness(num=100, num_cpu=multiprocessing.cpu_count()):
    fitness_cache.clear()
    for _ in range(num):
        do_fitness_generation(num_cpu=num_cpu)


def do_novelty(num=100, num_cpu=multiprocessing.cpu_count(), min_distance=1.):
    if len(behaviors_archive) < 1:
        do_fitness(num=1)
    for _ in range(num):
        do_novelty_generation(num_cpu=num_cpu, min_distance=min_distance)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Run a neural evolution session.')
    parser.add_argument('--num_hidden', '-n', type=int, required=False, default=10, help='Number of hidden neurons.')
    args = parser.parse_args()

    problem = EvolveBinarySumDirect(dimension=3, num_hidden=args.num_hidden, num_samples=16)
    gateway = JavaGateway()
    gateway.close()
