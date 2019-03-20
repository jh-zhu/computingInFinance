package CLUSTER;

import junit.framework.TestCase;
import java.util.*;

public class Test_Cluster extends TestCase {
	public void testConstructor() {
		double[] location1 = {1,1,1};
		double[] location2 = {3,3,3};
		double[] locationc = {2,2,2};
		point point1 = new point(location1);
		point point2 = new point(location2);
		ArrayList<point> points = new ArrayList<point>();
		points.add(point1);
		points.add(point2);
		centroid c = new centroid(locationc);
		cluster cl = new cluster(c,points);
		assertTrue(cl.getCentroid().equals(c));
		assertEquals(cl.getPoints(),points);
		
	}
	
	public void testSetCentroid() {
		double[] location1 = {1,1,1};
		double[] location2 = {3,3,3};
		double[] locationc = {2,2,2};
		double[] locationc2 = {9,9,9};
 		point point1 = new point(location1);
		point point2 = new point(location2);
		ArrayList<point> points = new ArrayList<point>();
		points.add(point1);
		points.add(point2);
		centroid c1 = new centroid(locationc);
		centroid c2 = new centroid(locationc2);
		cluster cl = new cluster(c1,points);
		cl.setCentroid(c2);
		assertTrue(cl.getCentroid().equals(c2));
		assertFalse(cl.getCentroid().equals(c1));
	}
	
	public void testAddPoint() {
		double[] location1 = {1,1,1};
		double[] locationc = {2,2,2};
		double[] location2 = {3,3,3};
		point point1 = new point(location1);
		point point2 = new point(location2);
		ArrayList<point> points = new ArrayList<point>();
		ArrayList<point> check = new ArrayList<point>();
		points.add(point1);
		check.add(point1);
		points.add(point2);
		check.add(point2);
		check.add(point2);
		centroid c = new centroid(locationc);
		cluster cl = new cluster(c,points);
		cl.addPoint(point2);
		assertEquals(check, cl.getPoints());
	}
	
	public void testRemovePoint() {
		double[] location1 = {1,1,1};
		double[] locationc = {2,2,2};
		double[] location2 = {3,3,3};
		point point1 = new point(location1);
		point point2 = new point(location2);
		centroid c = new centroid(locationc);
		ArrayList<point> points = new ArrayList<point>();
		points.add(point1);
		points.add(point2);
		cluster cl = new cluster(c, points);
		cl.removePoint(point2);
		ArrayList<point> check = new ArrayList<point>();
		check.add(point1);
		assertEquals(check, cl.getPoints());
	}
	
	public void testEquals() {
		double[] location1 = {1,1,1};
		double[] location2 = {2,2,2};
		double[] locationc = {3,3,3};
		point p1 = new point(location1);
		point p2 = new point(location2);
		centroid c= new centroid(locationc);
		ArrayList<point> points1 = new ArrayList<point>();
		ArrayList<point> points2 = new ArrayList<point>();
		points1.add(p1);
		points2.add(p2);
		points1.add(p2);
		points2.add(p1);
		cluster c1 = new cluster(c,points1);
		cluster c2 = new cluster(c,points2);
		assertTrue(c1.equals(c2));
	}
	
	public void testComputeCentroid() {
		double[] location1 = {1,1,1};
		double[] location2 = {1,3,5};
		double[] locationc = {1,2,3};
		double[] locationf = {9,9,9};
		point p1 = new point(location1);
		point p2 = new point(location2);
		ArrayList<point> points = new ArrayList<point>();
		points.add(p1);
		points.add(p2);
		centroid c = new centroid(locationf);
		cluster cl = new cluster(c,points);
		
		// new centroid are computed, not converged 
		assertFalse(cl.computeCentroid());
		// now the centroid should be at the right place
		assertTrue(cl.getCentroid().atLocation(locationc));
		// now the method should converge
		assertTrue(cl.computeCentroid());
	}
	public void testClearPoint() {
		double[] location1 = {1,1,1};
		double[] location2 = {2,2,2};
		point p1 = new point(location1);
		point p2 = new point(location2);
		ArrayList<point> points = new ArrayList<point>();
		points.add(p1);
		points.add(p2);
		centroid c =new centroid(location1);
		cluster cl = new cluster(c,points);
		cl.clearPoint();
		assertTrue(cl.getPoints().isEmpty());
	}
	
	public void testFarthestDistance() throws Exception {
		double[] location1 = {1,1,1};
		double[] locationc = {2,2,2};
		double[] location2 = {5,5,5};
		point p1 = new point(location1);
		point p2 = new point(location2);
		centroid c = new centroid(locationc);
		ArrayList<point> points = new ArrayList<point>();
		points.add(p1);
		points.add(p2);
		cluster cl = new cluster(c,points);
		double distance = cl.farthestDistance();
		assertTrue(distance - 3*Math.sqrt(3) < 1e-3);
		}
	public void testFarthestPoint() throws Exception{
		double[] location1 = {1,1,1};
		double[] locationc = {2,2,2};
		double[] location2 = {5,5,5};
		point p1 = new point(location1);
		point p2 = new point(location2);
		centroid c = new centroid(locationc);
		ArrayList<point> points = new ArrayList<point>();
		points.add(p1);
		points.add(p2);
		cluster cl = new cluster(c,points);
		point p = cl.farthestPoint();
		assertTrue(p.equals(p2));
	}
	
	
}
	



