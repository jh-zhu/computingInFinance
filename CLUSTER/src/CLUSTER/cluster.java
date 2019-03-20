package CLUSTER;
import java.util.*;

public class cluster {
	
	// cluster contains a centroid and a list of points
	private centroid _c;
	private ArrayList<point> _points;
	
	public cluster() {
		_c = null;
		_points = null;
	}
	
	public cluster (centroid c, ArrayList<point> points) {
		_c = c;
		_points = points;
	}
	
	public centroid getCentroid() {
		return _c;
	}
	
	public void setCentroid(centroid c) {
		_c = c;
	}
	public void addPoint(point p ) {
		_points.add(p);
	}
	public void removePoint(point p) {
		_points.remove(p);
	}
	public void clearPoint() {
		_points.clear();
	}
	
	public ArrayList<point> getPoints() {
		return _points;
	}
	public boolean equals(cluster cl) {
		if(this == cl) return true;
		if(! (cl instanceof cluster)) return false;
		ArrayList<point> points = cl.getPoints();
		if(points.size() != _points.size()) return false;
		for (int i=0; i<points.size();i++) {
			if(! _points.contains(points.get(i))) return false;
			if(! points.contains(_points.get(i))) return false;
		}
		return _c.equals(cl.getCentroid());
	}
	public int getNumber() {
		 return _points.size();
	}
	
	// if the centroid of this cluster changed, the function will return false (does not converge yet), else return true
	public boolean computeCentroid() {
		if(_points.size()==0) return true;
		int n = _points.size();
		int d = _points.get(0).getDimension();
		double[] locationNew = new double[d];
		for (int i =0;i<n;i++) {
			point p = _points.get(i);
			for (int j =0; j<d;j++) {
				locationNew[j] += p.getLocation()[j];
			}
		}
		for (int k=0; k<d;k++) {
			locationNew[k] = locationNew[k] / n;
		}
		if (_c.atLocation(locationNew)) return true; // if the location of centroid does not change, i.e. converged
		_c.setLocation(locationNew);
		return false;
	}
	
	// return the distance between the farthest point and the centroid in a cluster
	public double farthestDistance() throws Exception {
		double farthest = 0;
		if(_points.size() == 0) return 0;
		for(int i =0; i<_points.size();i++) {
			double distance = _c.distanceToPoint(_points.get(i));
			if(distance>farthest) farthest = distance;
		}
		return farthest;
	}
	
	// return the farthest point in the cluster
	public point farthestPoint() throws Exception {
		if(_points.size()==0) return null;
		point farthestp = _points.get(0);
		double farthest = _c.distanceToPoint(farthestp);
		for(int i=1;i<_points.size();i++) {
			double distance = _c.distanceToPoint(_points.get(i));
			if(distance > farthest) {
				farthestp = _points.get(i);
				farthest = distance;
			}
		}
		return farthestp;
	}
}
