package ga;

public class Chromosome {

	String _string;
	double _fitness;
	
	public String getString() { return _string;  }
	public double fitness()   { return _fitness; }
	
	public void setFitness( double fitness ) { _fitness = fitness; }
	
	public Chromosome ( int numBits ) {
		char chars [] = new char [ numBits ];
		for ( int i = 0; i < numBits; i++ ) {
			if ( Math.random () < 0.5 ) {
				chars [ i ] = '0';
			} else {
				chars [ i ] = '1';
			}
		}
		_string = new String ( chars );
	}

	public Chromosome ( String str ) {
			_string = str;
	}
	
	public int at ( int iByte ) {
		return _string.charAt ( iByte ) == '0' ? 0 : 1;
	}
	
	public Chromosome randomSpliceWith ( Chromosome cr ) {
		int splicePoint = ( int ) ( Math.random() * _string.length() );
		String str1 = _string.substring ( 0, splicePoint );
		int str2Length = cr.getString ().length ();
		String str2 = cr.getString ().substring ( 
			splicePoint, 
			str2Length
		);
		String str3 = str1.concat ( str2 ); 
		Chromosome newCr = new Chromosome ( str3 );
		return newCr;
	}

	/** Implements mutation - random flipping - of some bits in population and reports self similarity.
	 * @param mutationRate Rate of mutation as fraction of 1.0
	 * @param selfSim As long as we're go
	 */
	public void mutateAndReportSelfSimilarity( double mutationRate, int selfSim [] ) {
		char chars [] = new char [ _string.length () ];
		for ( int i = 0; i < _string.length(); i++ ) {
			chars [i] = _string.charAt (i);
			if ( Math.random() < mutationRate ) {
				if ( chars [i] == '1' ) {
					chars [i] = '0'; 
				} else {
					chars [i] = '1';
				}
			}
			if ( chars [i] == '1' ) {
				selfSim [i] ++;
			}
		}
		_string = new String ( chars );
	}

}