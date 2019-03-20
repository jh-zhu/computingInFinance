package edu.nyu.class4.montecarlo.distributive;

/*this class describes an option
 * The option's r, sigma, strike,s0, type and maturity*/

public class Option {
	private double _r;
	private double _sigma;
	private double _k;
	private double _s;
	private String _type;
	private int _t;
	
	public Option(double r, double sigma, double k, double s,String type,int t) {
		_r = r;
		_sigma = sigma;
		_k = k;
		_s = s;
		_type = type;
		_t = t;
	}
	public double getR() {
		return _r;
	}
	public double getSigma() {
		return _sigma;
	}
	public double getStrike() {
		return _k;
	}
	public double getS0() {
		return _s;
	}
	public String getType() {
		return _type;
	}
	public int getT() {
		return _t;
	}
	@Override
	// In order to compose the text message sent to clients more convenient
	public String toString() {
		String s = _type + "," +_r+","+_sigma+","+_k+","+_s+","+_t;
		return s;
	}
}
