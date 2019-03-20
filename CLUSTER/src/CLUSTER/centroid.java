package CLUSTER;

// Centroid are special points that can move around
public class centroid extends point{
	
	//empty constructor
	public centroid () {
		setLocation(null);
	}
	
	// using point constructor
	public centroid (double[] location) {
		super(location);
	}
	
	//equal method
	public boolean equals(centroid c) {
		if(this == c) return true;
		if(! (c instanceof centroid)) return false;
		double[] locationc = c.getLocation();
		double[] location = getLocation();
		if(location.length != locationc.length) return false;
		for (int i=0; i<locationc.length;i++) {
			if(Math.abs(location[i] - locationc[i]) > 1.0e-5) return false;
		}
		return true;
	}
	
	//calculate the distance between a centroid and a point
	public double distanceToPoint(point p) throws Exception{
		double distancesqr = 0;
		double[] locationc = getLocation();
		double[] locationp = p.getLocation();
		if(locationc.length != locationp.length) throw new Exception("Dimension does not match!");
		for (int i=0;i<locationc.length;i++) {
			distancesqr += (locationc[i]-locationp[i]) * (locationc[i] - locationp[i]);
		}
		return Math.sqrt(distancesqr);
	}
	
	// return true or false if a centroid is at a certain location
	public boolean atLocation(double[] location) {
		double[] locationc = getLocation();
		if(locationc.length != location.length) return false;
		for (int i = 0; i< locationc.length;i++) {
			if(Math.abs(locationc[i]-location[i]) > 1.0e-3) return false;
		}
		return true;
	}
	
}



