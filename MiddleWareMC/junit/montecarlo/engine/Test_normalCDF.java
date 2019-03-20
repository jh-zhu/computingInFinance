package edu.nyu.class3.montecarlo.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_normalCDF {

	@Test
	public void test_normalCDF() {
		normalCDF n = new normalCDF();
		double out = n.calculate(0.0026);
		double x = 2.80;
		assertTrue(Math.abs(out - x) <0.01);
	}
	
	@Test
	public void test_normalCDF2() {
		normalCDF n = new normalCDF();
		double out = n.calculate(0.3632);
		double x = 0.35;
		assertTrue(Math.abs(out - x) <0.01);
	}
	
	@Test
	public void test_normalCDF3() {
		normalCDF n = new normalCDF();
		double out = n.calculate(0.59483);
		double x = -0.24;
		assertTrue(Math.abs(out - x) <0.01);
	}
	@Test
	public void test_normalCDF4() {
		normalCDF n = new normalCDF();
		double out = n.calculate(0.02);
		double x = 2.06;
		assertTrue(Math.abs(out - x) <0.01);
	}

}
