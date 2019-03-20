package CLUSTER;

public class point {
	
	private double[] _location;
	
	//empty constructor
	public point() {
		_location = null;
	}
	
	// point constructor taking a double[] 
	public point(double[] location){
		_location = location;
	}
	
	// return location of a point
	public double[] getLocation() {
		return _location;
	}
	
	// equal methods
	public boolean equals(point p) {
		if (this == p) return true;
		if(!(p instanceof point)) return false;
		double[] location = p.getLocation();
		if( _location.length!= p.getDimension()) return false;
		
		for(int i = 0; i<p.getDimension();i++) {
			if(Math.abs(_location[i] - location[i]) > 1.0e-5) return false;
		}
		return true;
	}
	
	// set the location of a point
	public void setLocation(double[] location) {
		_location = location;
	}
	
	//calculate the distance between this point and a centroid
	public double distanceToCentroid(centroid c) throws Exception {
		double distancesqr = 0;
		double[] locationc = c.getLocation();
		if(_location.length != locationc.length) throw new Exception("Dimension does not match!");
		for (int i=0;i<_location.length;i++) {
			distancesqr += (_location[i] - locationc[i]) * (_location[i] - locationc[i]);
		}
		return Math.sqrt(distancesqr);
	}
	
	// get the dimension of this point
	public int getDimension() {
		return _location.length;
	}
	
	
	
}
