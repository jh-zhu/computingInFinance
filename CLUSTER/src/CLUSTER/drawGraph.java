package CLUSTER;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class drawGraph {
	private ArrayList<int[]> clocations = new ArrayList<int[]>();
	private ArrayList<int[]> plocations = new ArrayList<int[]>();
	private String label = "graph";
	
	public drawGraph() {
		
	} 
	
	public void setLabel(String s) {
		label = s;
	}
	
	public void addPoints(point[] points) {
		if(points.length == 0) return;
		for(int i=0;i<points.length;i++) {
			int[] temp = new int[2];
			double[] location = points[i].getLocation();
			temp[0] = (int)location[0]*7;
			temp[1] = (int)location[1]*7;
			plocations.add(temp);
		}
	}
	
	public void clearPoints() {
		plocations.clear();
	}
	
	public void addClusters(cluster[] clusters) {
		if(clusters.length == 0) return;
		for(int j= 0; j<clusters.length;j++) {
			int[] temp = new int[2];
			double[] location = clusters[j].getCentroid().getLocation();
			temp[0] = (int)location[0]*7;
			temp[1] = (int)location[1]*7;
			clocations.add(temp);
		}
	}
	
	public void clearClusters() {
		clocations.clear();
	}
	
	public void draw() {
		JFrame jFrame = new JFrame(label);
		JPanel jPanel = new JPanel() {

			private static final long serialVersionUID = 1L;

			public void paint(Graphics graphics) {
				super.paint(graphics);
				graphics.setColor(Color.white);
				for(int m = 0; m< plocations.size();m++) {
					graphics.fillRect(plocations.get(m)[0]+15, plocations.get(m)[1]+5, 4, 4);
				}
				graphics.setColor(Color.red);
				for(int n = 0; n<clocations.size();n++) {
					graphics.fillRect(clocations.get(n)[0]+15, clocations.get(n)[1]+5, 4, 4);
				}
			}
		};
		jFrame.add(jPanel);
		jFrame.setSize(750,750);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
}
