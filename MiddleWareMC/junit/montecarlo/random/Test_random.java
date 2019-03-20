package edu.nyu.class3.montecarlo.random;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_random {
	
	// test that if set by the same seed, the Gaussian generator would generate the same random number
	@Test
	public void test_normal() {
		GaussianRandomGenerator normal = new GaussianRandomGenerator(1);
		GaussianRandomGenerator normal2 = new GaussianRandomGenerator(1);
		assertTrue(normal.nextRandom() - normal2.nextRandom() <10e-5);
		assertTrue(normal.nextRandom() - normal2.nextRandom() <10e-5);
	}
}
