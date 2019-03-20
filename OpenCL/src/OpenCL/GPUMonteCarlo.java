package OpenCL;
import edu.nyu.class3.montecarlo.engine.*;
import edu.nyu.class3.montecarlo.random.*;

public class GPUMonteCarlo {
	public static void main (String[] args) {
		// daily drift
		float r = 0.0001f;
		// daily vol
		float sigma = 0.01f;
		//length to simulate (a year here)
		int length = 252;
		//initial stock price
		float initial = 152.35f;
		//strike price
		float strike = 165.0f;
		//length of gaussian and uniform random vectors (1M suggested in HW)
		int N = 1000000;
		//seed
		int seed = 1;
		// stats collector
		statsCollector stats = new statsCollector();
		//precision of uniform random generator (factor of 10)
		int precision =100;
		// confidence level
		double confidence = 0.96;
		// error tolerance
		double error = 0.1;
		// normalCDF calculator
		normalCDF normal = new normalCDF();
				
		//we use GPU to generate 2M gaussian random variables at a time, and transform
		//these variables into final values of Geometric Brownian Motion paths of given length (~7936 paths a batch)
		//keep doing until the estimated error under given confidence level is less than 0.1 
		
		// initialize estimated error, std, vectors and uniform random generator
		double e = 9999;
		double std = 999;
		float[] uniform1 = new float[N];
		float[] uniform2 = new float[N];
		float[] gs1 = new float[N];
		float[] gs2 = new float[N];
		UniformRandomGenerator uniform = new UniformRandomGenerator(seed,precision);

		// execute new batches until estimated error narrows down to 0.1
		while(e>error) {
			// load two uniform random vectors
			for(int i=0;i<N;i++) {
			uniform1[i] = (float)uniform.nextRandom()/precision;
			uniform2[i] = (float)uniform.nextRandom()/precision;
			}
			// initialize a GPU gaussian generator
			GPUGaussianRandomGenerator gs = new GPUGaussianRandomGenerator(uniform1,uniform2);
			gs.generates();
			//get gaussian random vector
			gs1 = gs.getGaussianVector1();
			gs2 = gs.getGaussianVector2();
			// put these two vectors into a path generator
			GPUPathGenerator generator = new GPUPathGenerator(length,r,sigma,initial,gs1,gs2);
			float[] GMValues = generator.generates();
			// calculate european payout and add them into stats collector
			int n = GMValues.length;
			for (int i=0;i<n;i++) {
				double payout = Math.max(0, GMValues[i] - strike);
				stats.add(payout);
			}
			// estimate std
			std = Math.sqrt(stats.getVariance());
			// estimae error under this confidence level 
			double a  = normal.calculate((1-confidence)/2);
			// update estimated error
			e = (a * std )/ Math.sqrt(stats.getN());
		}
		//print out estimaed price
		System.out.println("The price of European call with strike 165 worths: " + stats.getMean());
	}
}
