package CLUSTER;

import junit.framework.TestCase;

public class Test_Point extends TestCase {
	public void testConstructor() {
		double[] location = {1,2,3,4,5};
		point p = new point(location);
		assertEquals(location, p.getLocation());
	}
	
	public void testEquals() {
		double[] location = {3,3,3,1,};
		double[] location2 = {1,1,1,3};
		point p1 = new point(location) ;
		point p2 = new point(location);
		point p3 = new point(location2);
		assertTrue(p1.equals(p2));
		assertFalse(p1.equals(p3));
	}
	public void testSetLocation() {
		double[] location = {1,1,2,3};
		double[] location2 = {9,9,9,9};
		point p = new point(location);
		p.setLocation(location2);
		assertEquals(p.getLocation(),location2);
	}
	public void testDistanceToCentroid() throws Exception {
		double[] location1 = {1,1,1};
		double[] location2 = {2,2,2};
		point p = new point(location1);
		centroid c = new centroid(location2);
		double distance = p.distanceToCentroid(c);
		double accuracy = 1.00e-10;
		assertTrue(Math.abs(distance - Math.sqrt(3)) < accuracy);
	}
	
	// this test method is meant to catch an "dimension does not match" exception
	public void testDistanceToCentroid2() throws Exception{
		double[] location1 = {1,1,1};
		double[] location2 = {2,2,2,2};
		point p = new point(location1);
		centroid c = new centroid(location2);
		try {
			p.distanceToCentroid(c);
		} catch(Exception e){
			System.out.println(e);
			System.out.println("Dimession doesn't match exception caught!");
		}
	}
	
}
