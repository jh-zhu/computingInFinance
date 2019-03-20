package edu.nyu.class3.montecarlo.payout;

import edu.nyu.class3.montecarlo.path.*;
import java.util.*;

public class EuroCall implements Payout {
	
	private double _strike;
	
	public EuroCall(double strike) {
		_strike = strike;
	}
	
	public double payout(Path p) {
		SortedSet<DataPoint> points = p.getData();
		double lastprice = points.last().price();
		if(lastprice - _strike > 0) return lastprice - _strike;
		return 0;
	}
}
