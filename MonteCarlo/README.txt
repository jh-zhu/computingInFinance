1. The project is called SimpleMonteCarloSim. There are 4 packages under the project:
  a. edu.nyu.class3.montecarlo.engine
  b. edu.nyu.class3.montecarlo.path
  c. edu.nyu.class3.montecarlo.random
  d. edu.nyu.class3.montecarl.payout
each package has both source files and Junit tests.

2. In order to run Junit tests:
  a. please install Junit5
  b. please install mockito 

3. For results, please run the main class under the engine package. The result will be reported in a file called
"report.txt" generated under the project root folder. Also the result will be printed in the console.

4. After warming up the simulation with the first 10,000 runs, my design is to keep these 10,000 runs and do the remaining.
For example, if the simulation requires 60,000 runs to be accurate as desired, the engine will only run 50,000 after the
initial 10,000 runs, adding up to be 60,000

5. The stock paths are generated based on geometric brownian motion

6. The normalCDF class is designed to replace the normal table. For the calculate method under this class,
the input p is the area of the tail of 1 side and the output is a number corresponding to standard normal CDF(1-p)

7. The simulation is designed to be general, so you can configure the simulation to price options in other circumstance.
detail please see the main class, which is highly documented. 
