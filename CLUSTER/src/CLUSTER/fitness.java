package CLUSTER;
import java.util.*;

public class fitness {
	
	/* No matter applying kmeans or kmeans2, at the very end, every point should belong to a cluster.
	 * Therefore, studying the distribution the 500 centroids should be sufficient to give us clues on how good the fit is
	 * 
	 * Ideally, the distance between any two nearby centroids should be no less than 100/sqrt(500) ~ 4.5
	 * To say nearby centroids, two centroids are considered nearby if their distance is less than sqrt(2)* 4.5 ~ 6
	 * 
	 * Therefore in general, ideally any distance between tow points that are less than 7 should be greater than 4
	 * 
	 * Define shortDistancePercent(SDP) as the percentage that (number of distances less than 4.5) / (number of distance less than 7)
	 * Ideally, SDP should be 0 for a perfect fit, which means the lower SDP, the better the fit*/
	
	private ArrayList<centroid> centroids = new ArrayList<centroid>();
	private double SDP;
	private final double limit = 4.5;
	private  final double nearby = 6;
	
	public fitness() {
		
	}
	public void getClusters(cluster[] cls) {
		for(int i= 0;i<cls.length;i++) {
			centroids.add(cls[i].getCentroid());
		}
	}
	public void clearClusters() {
		centroids.clear();
	}
	
	public double computeSDP() throws Exception {
		int n = centroids.size();
		int nearbyPoints = 0;
		int shortDistance = 0;
		for (int i=0;i<n;i++) {
			for(int j=i+1;j<n;j++) {
				double distance = centroids.get(i).distanceToCentroid(centroids.get(j));
				if(distance < nearby) nearbyPoints +=1;
				if(distance < limit) shortDistance +=1;
			}
		}
		SDP = (double)shortDistance/nearbyPoints;
		return SDP;
	}
	public double getSDP() {
		return SDP;
	}
	
}
