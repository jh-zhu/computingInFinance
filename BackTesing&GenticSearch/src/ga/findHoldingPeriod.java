package ga;

import simulation.*;
import java.util.ArrayList;

/**
 * This is the main class to deploy genetic algorithm to find the optimal holding period
 * The solution is accurate up to 1 day */

public class findHoldingPeriod {
	public static void main(String args[]) {
		int popSize = 5; // Number of candidate solutions per generation
		int numGens = 100; // Maximum number of generations to run
		float mutationRate = 0.001F; // Smaller means faster convergence
		float selfSimTarget = 0.99F; // Stopping criterion
		long[] dims = { 60 }; // Everywhere between 0 to 60 days is a candidate
		EvalObject evalObject = new EvalObject(
			popSize,
			numGens,
			mutationRate,
			selfSimTarget,
			dims
		) {
			@Override
			public ArrayList<Double> evaluate( Population pop, ArrayList<ArrayList<Long>> proposedSolutions ) {
				//initialize fitness array
				ArrayList<Double> fitness = new ArrayList<Double>(proposedSolutions.size());
				//initialize simulation launcher
				SimulationLauncher launcher = new SimulationLauncher();		
				//initialize soluions array
				int[] solutions = new int[proposedSolutions.size()];
				//fill in solutions array
				int n=0;
				for(ArrayList<Long> proposedSolution:proposedSolutions) {
					solutions[n] = proposedSolution.get(0).intValue();
					n++;
				}
				// get fitness
				try {
					fitness = launcher.run(solutions);
				} catch (Exception e) {
				}
				return fitness;		
			}
		};		
		Population pop = new Population( evalObject );
		pop.turnPrintingOn();
		pop.run();
	}
}
