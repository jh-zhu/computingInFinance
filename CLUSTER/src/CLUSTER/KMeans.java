package CLUSTER;
import java.io.PrintWriter;
import java.util.*;


/* use KMmeans to play with the toy problem. 
The implementations of point, centroid and cluster are more general, that can be 
used in any dimensional problem*/

public class KMeans {
	public static void main(String[] args) throws Exception {
		
		drawGraph graph = new drawGraph();
		graph.setLabel("Before KMeans");
		drawGraph graph2 = new drawGraph();
		graph2.setLabel("After KMeans");
		
		//initialize 100000 locations
		double[][] locations = new double[10000][2];
		int n =0;
		for(int i=0;i<100;i++) {
			for(int j=0;j<100;j++) {
				double[] temp = {i,j};
				locations[n] = temp;
				n++;
			}
		}
		
		// use the 10000 locations to generate 10000 points
		point[] points = new point[10000];
		for(int k =0; k<10000;k++) {
			points[k] = new point(locations[k]);
		}
		graph.addPoints(points);
		graph2.addPoints(points);
		System.out.println(points.length + " points generated!");
		
		// generate 500 random cluster with a distinct centroid each
		int k = 0;
		Random rdm = new Random();
		ArrayList<double[]> clocations = new ArrayList<double[]>();
		while(k<500) {
			int x = rdm.nextInt(100);
			int y = rdm.nextInt(100);
			double[] clocation = {x,y};
			if(!clocations.contains(clocation)) {
				clocations.add(clocation);
				k++;
			}
		}
		cluster[] clusters = new cluster[500];
		for (int i = 0; i<clocations.size();i++) {
			centroid c =new centroid(clocations.get(i));
			ArrayList<point> points_ = new ArrayList<point>();
			clusters[i] = new cluster(c,points_);
		}
		System.out.println(clocations.size() + " clusters generated!");
		
		//graph points before fit
		graph.addClusters(clusters);
		graph.draw();
		
		// calculate SDP before fit
		fitness kmeansBefore = new fitness();
		kmeansBefore.getClusters(clusters);
		double kmeansBeforeSDP = kmeansBefore.computeSDP();
		System.out.println("SDP before kmeans is: "+ kmeansBeforeSDP);
		
		// KMeans--points find nearest centroid
		boolean done = false;
		while(!done) {
			done = true;
			for(int i = 0; i< 10000;i++) {
				int cl = 0;
				double minDistance = 1.0e11;
				for(int j=0;j<500;j++) {
					double distance = points[i].distanceToCentroid(clusters[j].getCentroid());
					if(distance < minDistance) {
						minDistance= distance;
						cl = j;
					}
				}
				clusters[cl].addPoint(points[i]);
			}
			for(int p=0;p<500;p++) {
				boolean converge = clusters[p].computeCentroid();
				if(!converge) done = false;
				clusters[p].clearPoint();
			}
		}
		
		graph2.addClusters(clusters);
		graph2.draw();
		// Output result into kmeans.txt
		PrintWriter outputStream = new PrintWriter("KMeans.txt");
		System.out.println("alogirthm converged. Results are in KMeans.txt");
		for(int m = 0; m< clusters.length;m++) {
			outputStream.println(clusters[m].getCentroid().getLocation()[0] +"," + clusters[m].getCentroid().getLocation()[1]);
		}
		outputStream.close();
		
		// calculate SDP after fit
		fitness kmeansAfter = new fitness();
		kmeansAfter.getClusters(clusters);
		double kmeansAfterSDP = kmeansAfter.computeSDP();
		System.out.println("SDP after kmeans is: " + kmeansAfterSDP);
		
		//output fitness report
		PrintWriter outputStream2 = new PrintWriter("fitnessReport_kmeans.txt");
		outputStream2.println("The SDP before kmeans is: " + kmeansBeforeSDP);
		outputStream2.println("The SDP after kmeans is: " + kmeansAfterSDP);
		outputStream2.close();
	}
}
