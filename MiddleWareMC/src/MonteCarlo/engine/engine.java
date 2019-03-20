package edu.nyu.class3.montecarlo.engine;

import edu.nyu.class3.montecarlo.payout.*;
import edu.nyu.class3.montecarlo.path.*;

public class engine {
	private statsCollector _stat;
	private Payout _option;
	private PathGenerator _generator;
	private double _tolerance;
	private double _confidence;
	private normalCDF _normal;
	
	public engine(Payout option, PathGenerator generator, double tolerance,double confidence) {
		_stat = new statsCollector();
		_normal = new normalCDF();
		_option = option;
		_generator = generator;
		_tolerance = tolerance;
		_confidence = confidence;
		
	}
	
	public double getOptionPrice() {
		// run 10,000 times to warm up the engine
		for (int i=0;i<10000;i++) {
			Path p = _generator.getPath();
			double payout = _option.payout(p);
			_stat.add(payout);
		}
		// the estimated variance
		double variance = _stat.getVariance();
		double sigma = Math.sqrt(variance);
		
		// calculator the number of times we need to actually run the simultion
		double y = _normal.calculate((1-_confidence)/2);
		double n = (int)((y*sigma)/_tolerance) * (int)((y*sigma)/_tolerance);
		
		if(n < 10000) return _stat.getMean();
		// do the remaining simulation
		for (int j=0;j<(n - 10000);j++) {
			Path p = _generator.getPath();
			double payout = _option.payout(p);
			_stat.add(payout);
		}
		return _stat.getMean();
		
	}
	
	
}
