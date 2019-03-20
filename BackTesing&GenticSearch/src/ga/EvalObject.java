package ga;

import java.util.ArrayList;

/**
 * @author Lee
 * 030515.1429
 */
public abstract class EvalObject {
	
	// Instance variables
		
		protected int		_popSize;
		protected long		_numGens;
		protected float		_mutationRate;
		protected float		_selfSimilarityTarget;
		protected long []	_dims;
	
	public EvalObject ( int popSize, int numGens, float mutationRate, float selfSimTarget, long[] dims ) {
		_popSize				= popSize;
		_numGens				= numGens;
		_mutationRate			= mutationRate;
		_selfSimilarityTarget	= selfSimTarget;
		_dims                   = dims;
	}
	
	// Getters - Used by Population
	
		public long []	getDims			() { return _dims;			}
		public double	getMutationRate	() { return _mutationRate;	}
		public int		getPopSize		() { return _popSize;		}
	
	
	/** Determines if convergence or completion has been achieved.
	 * 
	 * <P>
	 * 
	 * Compares the Population's statistics to the specs in this EvalObject to determine if the search is finished.
	 * 
	 * </P>
	 * 
	 * @param pop The GA Population for which the test is being performed
	 * @return Returns true if the self-similarity is high enough to stop, or if the allowed number of generations have been used up.
	 */
	public boolean isFinished ( Population pop ) {
		return 
			( pop.genNum () == _numGens ) 
			|| ( pop.selfSim () >= _selfSimilarityTarget );
	}

	/** Evaluates a proposed set of solutions.
	 * 
	 * @param pop Population
	 * @param values A list of proposed solutions
	 * @return A linked list of double fitness values
	 */
	public abstract ArrayList<Double> evaluate ( Population pop, ArrayList<ArrayList<Long>> proposedSolutions );

}
