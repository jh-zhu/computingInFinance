package CLUSTER;

import junit.framework.TestCase;

public class Test_Centroid extends TestCase {
	
	public void testConstructor() {
		double[] location = {1,1,1,1};
		centroid c = new centroid(location);
		assertEquals(c.getLocation(),location);
	}
	
	public void testSetLocation() {
		double[] location = {1,1,1,1};
		double[] newLocation = {2,2,2,2};
		centroid c= new centroid(location);
		c.setLocation(newLocation);
		assertEquals(c.getLocation(), newLocation);
	}
	
	public void testEquals() {
		double[] location  = {1,1,1,2,3,};
		centroid c1 = new centroid(location);
		centroid c2 = new centroid();
		c2.setLocation(location);
		assertTrue(c1.equals(c2));
	}
	
	public void testEquals2() {
		double[] location = {1,1,1};
		double[] location2 = {1,1,2};
		centroid c1 = new centroid(location);
		centroid c2 = new centroid(location2);
		assertFalse(c1.equals(c2));
	}
	
	public void testDistanceToPoint() throws Exception {
		double[] locationp = {1,1,1,1};
		double[] locationc = {2,2,2,2};
		centroid c  = new centroid(locationc);
		point p = new point(locationp);
		double distance = c.distanceToPoint(p);
		double accuracy = 1.00e-5;
		assertTrue(Math.abs(distance - Math.sqrt(4)) < accuracy);	
	}
	
	//this test is meant to catch an "dimension does not match!" exception
	public void testDistanceToPoint2() {
		double[] locationp = {1,1,1,1};
		double[] locationc = {2,2,2};
		centroid c = new centroid(locationc);
		point p = new point(locationp);
		try {
			 c.distanceToPoint(p);
		}catch(Exception e) {
			System.out.println("Dimesion does not match exception caught");
		}
	}
	public void testAtLocation() {
		double[] location1 = {1,1,1};
		double[] location2 = {2,2,2};
		centroid c = new centroid(location1);
		assertTrue(c.atLocation(location1));
		assertFalse(c.atLocation(location2));
	}
	
	
}
