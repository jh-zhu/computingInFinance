package edu.nyu.class3.montecarlo.payout;

import edu.nyu.class3.montecarlo.path.*;
import java.util.*;

public class AsiaCall implements Payout{
	private double _strike;
	
	public AsiaCall(double strike) {
		_strike = strike;
	}
	
	public double payout(Path p) {
		SortedSet<DataPoint> path = p.getData();
		Iterator<DataPoint> iter = path.iterator();
		double priceSum = 0.0;
		int size = path.size();
		while(iter.hasNext()) {
			DataPoint point = iter.next();
			priceSum += point.price();
		}
		double averagePrice = priceSum / size;
		if(averagePrice - _strike >0) return averagePrice - _strike;
		return 0;
	}
}
