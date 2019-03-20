package OpenCL;

import static org.junit.jupiter.api.Assertions.*;
import edu.nyu.class3.montecarlo.random.*;

import org.junit.jupiter.api.Test;


class testGPUGaussianRandomGenerator {

	@Test
	public void test_GPUGaussianRandomGenerator() throws Exception {
		//each vector has 1 million
		int N = 1000000;
		// precision of uniform random generator
		int precision = 100;
		//set seed to 1
		int seed = 1;
		// initiate uniform random generator 
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
	
		//initiate a GPUGaussianRandomGenerator using two uniform random vectors
		GPUGaussianRandomGenerator gs = new GPUGaussianRandomGenerator(uniform1,uniform2);
		//generates gaussian random vector
		gs.generates();
		// get two gaussian random vectors
		float[] gs1 = gs.getGaussianVector1();
		float[] gs2 = gs.getGaussianVector2();
		// the length of each gaussian random vector should be N
		assertEquals(gs1.length,N);
		assertEquals(gs2.length,N);
		//print out
		for (int j=0;j<N;j++) {
			System.out.println(gs1[j]);
		}
	}

}
