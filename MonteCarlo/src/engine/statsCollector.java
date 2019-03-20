package edu.nyu.class3.montecarlo.engine;

public class statsCollector {
	private double _N;
	private double _sumSquare;
	private double _mu;
	
	public statsCollector() {
		_N = 0;
		_sumSquare = 0;
		_mu = 0;
	}
	
	public void add(double d) {
		_N +=1;
		_sumSquare += d*d;
		_mu = ((_N-1)/_N)*_mu + (1/_N)* d;
	}
	
	public double getMean() {
		return _mu;
	}
	public double getVariance() {
		return (_sumSquare/_N) - (_mu*_mu);
	}
	
	public void clear() {
		_N = 0;
		_sumSquare = 0;
		_mu = 0;
	}
	
	
}
