package edu.nyu.class3.montecarlo.engine;

import static org.junit.jupiter.api.Assertions.*;
import edu.nyu.class3.montecarlo.random.*;
import edu.nyu.class3.montecarlo.path.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class Test_pathGenerator {

	@Test
	// if there is no noise i.e. gaussian random generator always return 0
	public void test_pathGenerator() {
		GaussianRandomGenerator gsr = mock(GaussianRandomGenerator.class);
		when(gsr.nextRandom()).thenReturn(0.0);
		PathGenerator generator = new PathGenerator(252,0.0001,0.01,100,gsr);
		Path p = generator.getPath();
		assertTrue(Math.abs(p.getData().last().price()-101.268) < 10e-2);
	}
	@Test
	// if gaussian random generator always return 0.1 (cdf 0.53983)
	public void test_pathGenerator2() {
		GaussianRandomGenerator gsr = mock(GaussianRandomGenerator.class);
		when(gsr.nextRandom()).thenReturn(0.1);
		PathGenerator generator = new PathGenerator(252,0.0001,0.01,100,gsr);
		Path p = generator.getPath();
		assertTrue(Math.abs(p.getData().last().price()-130.291) < 10e-2);
	}
		
}
