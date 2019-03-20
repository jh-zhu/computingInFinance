package edu.nyu.class3.montecarlo.random;


public class GaussianRandomGenerator implements RandomNumberGenerator {
	private int _seed;
	private UniformRandomGenerator _uniform;
	
	// determine the number of decimal points to use for the uniform random number we used
	// for example _prcise = 100 means to have 0.01 precise
	// by default the _precise is set to 100, but this can be changed by setPrecise() method
	private final int _precise = 100;
	
	public GaussianRandomGenerator(int seed) {
		_seed = seed;
		_uniform  = new UniformRandomGenerator(_seed,_precise);
	}
	
	public double nextRandom() {
		double U1 = (double)_uniform.nextRandom()/(double)_precise;
		double U2 = (double)_uniform.nextRandom()/(double)_precise;
		
		// if one of U1 or U2 is 0, the R becomes +inf
		while(U1 == 0 || U2==0) {
		U1 = (double)_uniform.nextRandom()/(double)_precise;
		U2 = (double)_uniform.nextRandom()/(double)_precise;
		}
		
		double theta = (2 * Math.PI * U1);
		double R = Math.sqrt(-2 * Math.log(U2));
		
		double N = R * Math.sin(theta);
		return N;
	
	}
}
