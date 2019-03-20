package CLUSTER;

import java.io.*;
import java.util.*;

public class KMeans2 {
	public static void main(String[] args) throws Exception {
		
		drawGraph graph = new drawGraph();
		graph.setLabel("Before KMeans2");
		drawGraph graph2 = new drawGraph();
		graph2.setLabel("After KMeans2");
		
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
		
		// graph points before fit
		graph.addClusters(clusters);
		graph.draw();
		
		//calculate SDP before fit 
		fitness kmeans2Before = new fitness();
		kmeans2Before.getClusters(clusters);
		double kmeans2BeforeSDP = kmeans2Before.computeSDP();
		System.out.println("The SDP before kmeans2 is: " + kmeans2BeforeSDP);
		
		//kmeans2 -- centroid seek the nearest 20 points 
		boolean done = false;
		while(!done) {
			done = true;
			LinkedList<point> pointsLeft = new LinkedList<point>(Arrays.asList(points));
			
			// loop over clusters
			for(int i=0;i<clusters.length;i++) {
				// the first 20
				for(int j =0;j<20;j++) {
					clusters[i].addPoint(pointsLeft.remove());
				}
				// iterate over the left points
				for(int p=0;p<pointsLeft.size();p++) {
					point newp = pointsLeft.remove();
					double farthestDistance = clusters[i].farthestDistance();
					double distance = clusters[i].getCentroid().distanceToPoint(newp);
					if(distance < farthestDistance) {
						point farp = clusters[i].farthestPoint();
						clusters[i].addPoint(newp);
						clusters[i].removePoint(farp);
						pointsLeft.add(farp);
					}
					else {
						pointsLeft.add(newp);
					}
				}
			}
			for(int c=0;c<clusters.length;c++) {
				boolean converged = clusters[c].computeCentroid();
				if(!converged) done = false;
				clusters[c].clearPoint();
			}
		}
		graph2.addClusters(clusters);
		graph2.draw();
		PrintWriter outputStream = new PrintWriter("KMeans2.txt");
		System.out.println("alogirthm converged. Results are in KMeans2.txt");
		for(int m = 0; m<clusters.length;m++) {
			outputStream.println(clusters[m].getCentroid().getLocation()[0] + "," + clusters[m].getCentroid().getLocation()[1]);
		}
		outputStream.close();
		
		// calculate SDP after fit
		fitness kmeans2After = new fitness();
		kmeans2After.getClusters(clusters);
		double kmeans2AfterSDP = kmeans2After.computeSDP();
		System.out.println("The SDP after kmeans2 is: " + kmeans2AfterSDP);
		
		//output fitness report
		PrintWriter outputStream2 = new PrintWriter("fitnessReport_kmeans2.txt");
		outputStream2.println("The SDP before kmeans2 is: " + kmeans2BeforeSDP);
		outputStream2.println("The SDP after kmeans2 is: " + kmeans2AfterSDP);
		outputStream2.close();
	}
}
