package ga;

import java.util.ArrayList;

/**
 * @author Lee
 * 
 */

public class Population {

	static long powersOfTwo [] = {
		1,
		2,
		4,
		8,
		16,
		32,
		64,
		128,
		256,
		512,
		1024,
		2048,
		4096,
		8192,
		16384,
		32768,
		65536,
		65536 * 2,
		65536 * 4,
		65536 * 8,
		65536 * 16,
		65536 * 32,
		65536 * 64,
		65536 * 128,
		65536 * 256,
		65536 * 512,
		65536 * 1024
	};

	ArrayList<Long>	_bestValues;
	double		    _averageSelfSim;
	Chromosome	    _crs[];
	Chromosome	    _bestCr;
	long		    _dims[];
	int			    _dimsAsBits[];
	double		    _bestFitness;
	long		    _genNum;
	int			    _popSize;
	int			    _numBits;
	double		    _mutationRate;
	EvalObject	    _evalObject;

	boolean _printDebug = false;

	public double getAverageSelfSim () { return _averageSelfSim; }
	public long getGenNum () { return _genNum; }

	public Population ( EvalObject evalObject ) {
		
		// Save dims, bits for each dim, and total bits
			_dims = evalObject.getDims ();
			_dimsAsBits = dimsToBits ( _dims );
			_numBits = 0;
			for ( int i = 0; i < _dimsAsBits.length; i++ ) {
				_numBits += _dimsAsBits [i];			
			}
			
		// Save pop size, mutation rate, and eval object
			_popSize = evalObject.getPopSize ();
			_mutationRate = evalObject.getMutationRate ();
			_evalObject = evalObject;
	
		// Initialize generations counter
			_genNum = 0;
		
		// Randomly populate	
			populateRandom ();
	}
	
	/**
	 * Create initial random population
	 */
	private void populateRandom () {
		_crs = new Chromosome [ _popSize ];
		for ( int i = 0; i < _popSize; i ++ ) {
			_crs [ i ] = new Chromosome ( _numBits );  			
		}
	}
	
	/**
	 * Decode population into a list of proposed solutions, each one between 0
	 * and the dimension size specified in the eval object
	 * @return List of proposed solutions
	 */
	private ArrayList<ArrayList<Long>> decode () {
		ArrayList<ArrayList<Long>> proposedSolutions = new ArrayList<ArrayList<Long>>( _popSize );
		for( Chromosome cr : _crs ) {
			ArrayList<Long> proposedSolution = new ArrayList<Long>( _dims.length );
			int stringOffset = 0;
			for ( int i = 0; i < _dimsAsBits.length; i++ ) {
				long value = 0;
				for ( int j = 0; j < _dimsAsBits [i]; j++ ) {
					int bitVal = ( ( cr._string.charAt ( j + stringOffset ) ) == '0' ) ? 0 : 1;
					value += bitVal * powersOfTwo [ j ]; 
				}
				value = (long) (
					(
						( double ) value 
						/ ( ( double ) powersOfTwo [ _dimsAsBits [ i ] ] - 1.0 )
					) * _dims [ i ]
				);
				proposedSolution.add( value );
				stringOffset += _dimsAsBits [ i ];
			}
			proposedSolutions.add( proposedSolution );
		}
		return proposedSolutions;
	}
	
	
	private void evaluate () {
		_genNum = _genNum + 1;
		ArrayList<ArrayList<Long>> proposedSolutions = decode();
		ArrayList<Double> fitness = _evalObject.evaluate( this, proposedSolutions );
		for ( int i = 0; i < _popSize; i ++ ) {
			_crs [i].setFitness ( fitness.get( i ) );
			if ( _crs [i]._fitness > _bestFitness ) {
				_bestCr = _crs [i];
				_bestFitness = _crs [i]._fitness;
				_bestValues = proposedSolutions.get( i );
			}
		}
		if ( _printDebug ) {
			System.out.print ( "Population genNum = " );
			System.out.print ( _genNum );
			System.out.print ( ", selfSim = " );
			System.out.print ( _averageSelfSim );
			System.out.print ( ", bestFitness = " );
			System.out.print ( _bestFitness );
			System.out.print ( ", value(s) = {" );
			System.out.print ( _bestValues.toString() );
			System.out.println ( "}" );
		}
	}
	
	private static int [] dimsToBits ( long dims [] ) {
		int dimsAsBits [] = new int [ dims.length ];
		for (int i = 0; i < dims.length; i ++ ) {
				dimsAsBits [ i ] = ( int ) ( ( Math.log ( dims [ i ] ) / Math.log (2) ) + 0.5 ); 
		}
		return dimsAsBits;	
	}
	
	public long		genNum	() { return _genNum;			}
	public double	selfSim	() { return _averageSelfSim;	}
	
	public void turnPrintingOn	() { _printDebug = true;	}
	public void turnPrintingOff	() { _printDebug = false;	}
	
	public void run () {
			while ( true ) {
				evaluate ();
				if ( _evalObject.isFinished ( this ) ) {
					return;	
				}
				repopulate ();
			} 
	}
	
	private void repopulate () {
		
			// Initialize self-similarity counters for each column to zero
				int selfSim [] = new int [ _crs [0].getString().length () ];
				for ( int i = 0; i < _crs [0].getString().length (); i++ ) {
					selfSim [ i ] = 0; 
				}
				
			// Make space for new population and force best solution
				Chromosome newPop [] = new Chromosome [ _crs.length ];
				newPop [ 0 ] = _bestCr;
			
			// Create tournament selector and sample from it to repopulate
				TournamentSelector r = new TournamentSelector ( _crs );
				for ( int i = 1; i < _crs.length; i++ ) {
					Chromosome cr1 = r.sample ();
					Chromosome cr2 = r.sample ();
					Chromosome cr3 = cr1.randomSpliceWith( cr2 );
					// Mutate and update self-similarity counts
						cr3.mutateAndReportSelfSimilarity ( _mutationRate, selfSim );
					newPop [i] = cr3;
				}
			// Update average self-similarity calculation
				_averageSelfSim = 0.0;
				for ( int i = 0; i < _numBits; i++ ) {
					double selfSimRatio = selfSim [i] / (double) _crs.length;
					if ( selfSimRatio < 0.5 ) {
						_averageSelfSim += 1 - selfSimRatio;			
					} else {
						_averageSelfSim += selfSimRatio;
					}
				}
				_averageSelfSim /= _numBits;
				
			_crs = newPop; 
			
	}
	
}