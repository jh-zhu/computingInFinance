package OpenCL;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.nyu.class3.montecarlo.random.*;

class testGPUPathGenerator {

	@Test
	public void test_GPUPathGenerator() {
		// length of gaussian random vector
		int N = 1000000;
		// seed
		int seed = 1;
		//initial price
		float initial = 152.35f;
		// daily drify
		float r = 0.0001f;
		// daily vol
		float sigma = 0.01f;
		//length
		int length = 252;
		// precision of uniform random generator
		int precision = 100;
		
		UniformRandomGenerator uniform = new UniformRandomGenerator(seed,precision);
		// load two uniform random vector of length N 
		float[] uniform1 = new float[N];
		float[] uniform2 = new float[N];
		for (int i=0;i<N;i++) {
			int U1 = uniform.nextRandom();
			int U2 = uniform.nextRandom();
			// take out 0, otherwise R in box-Muller can be inifity
			while(U1==0 || U2==0) {
				U1 = uniform.nextRandom();
				U2 = uniform.nextRandom();
			}
			uniform1[i] = (float)U1/precision;
			uniform2[i] = (float)U2/precision;
		}
		
		
	
		// initiate a gaussian random generator using two uniform random vectors
		GPUGaussianRandomGenerator gs = new GPUGaussianRandomGenerator(uniform1,uniform2);
		gs.generates();
		// get two gaussian random vectors
		float[] gs1 = gs.getGaussianVector1();
		float[] gs2 = gs.getGaussianVector2();
		//initiate a path generator
		GPUPathGenerator generator = new GPUPathGenerator(length,r,sigma,initial,gs1,gs2);
		float[] prices = generator.generates();
		
		// the prices should have a size that is the maxium possible paths can be generated from 2 vectors
		assertEquals(prices.length,(gs1.length+gs2.length)/length);
		//print out
		for (int i=0;i<prices.length;i++) {
			System.out.println(prices[i]);
		}
	}

}
