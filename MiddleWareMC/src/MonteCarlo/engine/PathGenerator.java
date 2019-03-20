package edu.nyu.class3.montecarlo.engine;
import edu.nyu.class3.montecarlo.random.*;
import edu.nyu.class3.montecarlo.path.*;
import java.util.*;

import java.time.LocalDate;

public class PathGenerator {
	private int _length;
	private double _r;
	private double _sigma;
	private double _initial;
	private RandomNumberGenerator _random;
	
	public PathGenerator(int length,double r, double sigma, double initial, RandomNumberGenerator random) {
		_length = length;
		_r = r;
		_sigma = sigma;
		_initial = initial;
		_random = random;
	}
	
	public void setDrift(double r) {
		_r = r;
	}
	public void setSigma(double sigma) {
		_sigma = sigma;
	}
	public void setInitial(double initial) {
		_initial = initial;
	}
	public void setRandom(RandomNumberGenerator random) {
		_random = random;
	}
	public void setLength(int length) {
		_length = length;
	}
	public double getDrift() {
		return _r;
	}
	public double getSigma() {
		return _sigma;
	}
	public double getInitial() {
		return _initial;
	}
	public int getLength() {
		return _length;
	}
	
	public Path getPath() {
		Path p = new Path();
		LocalDate d = LocalDate.now();
		// used to store historical data 
		HashMap<Integer,Double> prices = new HashMap<Integer,Double>();
		double lastprice = 0;
		
		for (int i=0;i<_length;i++) {
			d = d.plusDays(1);
			DataPoint point = new DataPoint();
			
			if(i == 0) lastprice = _initial;
			else lastprice = prices.get(i-1);
			double price = lastprice * Math.exp((_r - (_sigma * _sigma)/2) +  _sigma * _random.nextRandom());
			prices.put(i, price);
			
			point.date(d);
			point.price(price);
			p.addDataPoint(point);
		}
		return p;
	}
	
}
