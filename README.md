# orange-yap
A configurable  implementation of Push v3 in Java. Push is a programming language designed for evolutionary computation.

# Requirements
Java 8 SDK

# Quickstart
In a terminal cd to the installation directory and issue the following command to build the project:  
./gradlew

The demo program runs an evolutionary algorithm to solve the boolean parity problem. For help try:  
./net.orange.yap.main/build/scripts/boolean_parity_example -h

Omit '-h' to perform a run.

# Boolean Parity 
A run is repeatable with a choice for seed.<br/>
Example run:<br/>
$ net.orange.yap.main/build/scripts/boolean_parity_example<br/> 
Settings for this run:<br/>
dry-run:		    false<br/>
seed:			    1511535016195L<br/>
parity:			    12 (2^12=4096)<br/>
max-points:		    50<br/>
max-instructions:	50<br/>
population-size:	200<br/>
tournament-arity:	5<br/>
elitism-rate:		0.5<br/>
crossover-rate:		0.5<br/>
mutation-rate:		0.5<br/>
max-generation:		100<br/>
max-fitness:		0.99995<br/>
Starting run (please wait for output or press ^C to exit) ...<br/>
<br/>
Jan 20, 2018 10:13:44 PM net.orange.yap.main.CommonsGeneticSearch$1 notify<br/>
INFO: Generation num=0 produced [integer.stack-depth, [name.randboundname, integer.stack-depth, code.noop, boolean.=, boolean.nan... fitness=0.5.<br/>
Jan 20, 2018 10:13:46 PM net.orange.yap.main.CommonsGeneticSearch$1 notify<br/>
INFO: Generation num=10 produced [[boolean.xor, code.do*, code.cons, code.list, exec.y, boolean.=, float.sigmoid, code.rot, exec.d... fitness=0.9996600151062012.<br/>
Jan 20, 2018 10:13:59 PM net.orange.yap.main.CommonsGeneticSearch$1 notify<br/>
INFO: Generation num=20 produced [[boolean.xor, code.do*, code.flush], true, exec.y, boolean.=, float.yankdup, -0.8796974, boolean... fitness=0.9998499751091003.<br/>
Jan 20, 2018 10:14:12 PM net.orange.yap.main.CommonsGeneticSearch$1 notify<br/>
INFO: Generation num=30 produced [exec.y, boolean.=, boolean.=] fitness=0.9999499917030334.<br/>
Jan 20, 2018 10:14:15 PM net.orange.yap.main.CommonsGeneticSearch$1 notify<br/>
INFO: Attained maximum fitness, no further generations will be processed.<br/>
Jan 20, 2018 10:14:15 PM net.orange.yap.main.CommonsGeneticSearch execute<br/>
INFO: Evolution history contains 16 successive improvements.<br/>
The overall best program found in this run is [exec.y, boolean.=] with fitness=0.9999600052833557.<br/>
<br/>
# Todo
Write a proper readme.