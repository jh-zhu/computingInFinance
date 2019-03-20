The program is built on last homework. New stuff is the in package:
edu.nyu.class4.montecarlo.distributive

*** HOW TO RUN THE PROGRAM ***
1. Start Activemq on your local machine

2. Run the MonteCarloClient class, so that client is set ready to take requests
    (Please do this before you run the MonteCarloServer,
    otherwise the initial requests send by the server will not be received!)

3. Configure the option you want to price in the main method of MonteCarloServer
    (the default configuration is for the two options required in our homework )

4. Run MonteCarloServer for results

****RESULTS*****
after properly running the program:

1. The console for MonteCarloServer class will print out how many payouts are received,
    and under 96% confidence level and the number of payouts received, what is the estimated error.

    When the error narrows down to $0.1, the server give a price and move down to the next option
    (~70K for EuropeanCall and ~14K for AsianCall in this hw)

2. The console for MonteCarloClient class will print out the response it sent back to the server

3. A "report.txt" will be generated under the root directory, see that just for results.

*****STOP THE PROGRAM*****

1. After all pricing are done, the server will close itself. However, the client will keep alive
to listen to future requests. If there is no more task, shut down client.

2. Stop activemq on your machine

*****OTHER REQUIREMENT*******
Like last homework, in order for the program to be error free, please install:
1. Junit5
2. Mokito
3. AssertJ
