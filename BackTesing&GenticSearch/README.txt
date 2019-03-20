SIM Assignment
Jiahao Zhu N14368952 jz3511

********* How to run the program ***********
1. Please configure files paths in the Files class under package simulation,
   so that the program can read correctly from trading data and signal data.

2. Please run findHoldingPeriod class under package GA for results.

********* Trading Simulation Logic ********************
We start with 10000 dollars

1. P&L is realized every time new trading data comes in.
   Therefore, it is accurate up to 1 second

2. Portfolio is adjusted every time new signal data comes in.
   Therefore, our portfolio adjustment is up to 1 second. All positions
   are equal in dollar value.

3. For each day, a cumulated daily return is calculated and added to a stats collector
   After reading through all trading data (~92days), sharp ratio is reported for these
   daily returns. Please note that the actual fitness used in genetic algorithm is
   the sharp ratio plus 9999, so that GA is able to maximize sharp ratios.

****************** GA Logic *********************************
1. for each generation, 5 solutions are posted simultaneously (can be adjusted by modifying popSize).
   All the 5 solutions (holding periods) are backtested at the same time reading through
   the trading data under DBReader framework.

   (Please note that, it takes 15 minutes even for the 5 simulations to just
   read through all data.)

2. After reading through the trading data (~92 days). 5 simulations report their
   fitness. The GA algorithm then selected them to mate and evolve. It converges
   when self-similarity reaches 99%

***************** Results Reporting **************************
Please refer to the console tracking the process.

1. for each generation, 5 simulations read through the trading data (~92 days)
   and report their daily returns.

2. At the end of each generation, 5 simulations report their sharp ratio. GA
   reports its generation, self similarity, best fitness and best values.

3. The best value reported in the last generation is the holding period we want.
