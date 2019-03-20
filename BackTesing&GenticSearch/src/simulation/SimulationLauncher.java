package simulation;


import java.util.ArrayList;
import java.util.LinkedList;

import dbReaderFramework.DBManager;
import dbReaderFramework.I_DBProcessor;
import dbReaderFramework.I_DBReader;
import dbReaderFramework.MergeClock;

public class SimulationLauncher {
	
	public ArrayList<Double> run( int[] decodedParameterSet ) throws Exception {		
		
		// First, we list our readers.
		MergeFormatTradesDBReader tradesReader = new MergeFormatTradesDBReader( Files.tradesFilePathName );
		SignalFileReader signalReader = new SignalFileReader( Files.signalsFilePathName );
		LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
		readers.addLast( tradesReader );
		readers.addLast( signalReader );
		
		// Then, we list our processors.
		LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
		for( int daysToHold : decodedParameterSet ) {
			processors.addLast(
				new Simulation(
					tradesReader,
					signalReader,
					daysToHold
				)
			);
		}
		System.out.println(processors.size()+" solutions proposed.");
		// Create a clock that will use the sequence numbers
		// in the trades file as a guide.
		MergeClock clock = new MergeClock(
			readers,
			processors
		);
		
		DBManager dbm = new DBManager(
			readers, 
			processors,
			clock
		);
		dbm.launch();
		
		// What is the fitness of each Simulation?
		ArrayList<Double> fitness = new ArrayList<Double>(processors.size());
		for( I_DBProcessor processor : processors ) {
			fitness.add( ((Simulation)processor).getFitness() );
		}
		
		return fitness;
		
	}

}
