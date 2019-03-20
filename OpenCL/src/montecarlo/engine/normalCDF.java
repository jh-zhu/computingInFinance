package edu.nyu.class3.montecarlo.engine;

public class normalCDF {
	private final double c0 = 2.515517;
	private final double c1 = 0.802853;
	private final double c2 = 0.010328;
	private final double d1 = 1.432788;
	private final double d2 = 0.189269;
	private final double d3 = 0.001308;
	
	
	// the input p is the area of the tail of 1 side
	public double calculate(double p) {
		double t = Math.sqrt(Math.log( (1/(p*p))));
		return(t - (c0+c1*t+c2*t*t)/(1+d1*t+d2*t*t+d3*t*t*t));
	}
}
