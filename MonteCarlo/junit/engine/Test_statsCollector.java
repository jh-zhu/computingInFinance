package edu.nyu.class3.montecarlo.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_statsCollector {

	@Test
	public void test_statsCollector() {
		statsCollector stat = new statsCollector();
		stat.add(1);
		assertTrue(Math.abs(stat.getMean()-1)<10e-3);
		assertTrue(Math.abs(stat.getVariance() - 0 )< 10e-3);
	}
	
	@Test
	public void test_statsCollector2() {
		statsCollector stat = new statsCollector();
		stat.add(1);
		stat.add(2);
		stat.add(3);
		assertTrue(Math.abs(stat.getMean() - 2) < 10e-3);
		assertTrue(Math.abs(stat.getVariance() - 0.666666667 )<10e-3);
	}
	@Test
	public void test_statsCollector3() {
		statsCollector stat = new statsCollector();
		stat.add(2);
		stat.add(2);
		stat.add(2);
		assertTrue(Math.abs(stat.getMean() - 2) < 10e-3);
		assertTrue(Math.abs(stat.getVariance() - 0 )<10e-3);
	}
	

}
