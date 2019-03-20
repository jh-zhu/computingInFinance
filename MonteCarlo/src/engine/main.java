package edu.nyu.class3.montecarlo.engine;
import edu.nyu.class3.montecarlo.random.*;
import edu.nyu.class3.montecarlo.payout.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class main {
	public static void main(String[] args) throws IOException {
		
		//the expected drift of the stock price
		double drift = 0.0001;
		//the expected daily volatility of the stock price
		double sigma = 0.01;
		//the initial stock price;
		double s0 = 152.35;
		//the simulation length (a day as a unit)
		int length = 252;
		//the simulation tolerance
		double tolerance = 0.1;
		//the simulation confidence
		double confidence = 0.96;
		
		//first we make a gaussianRandomGenerator and set seed
		RandomNumberGenerator gr = new GaussianRandomGenerator(1);
		//then we configure the option we want to price
		//in our hw, they are a euroCall with strike 165 and a asiaCall with strike 164 
		Payout euro = new EuroCall(165);
		Payout asia = new AsiaCall(164);
		// configure the path generator for both options
		PathGenerator Path = new PathGenerator(length,drift,sigma,s0,gr);
		// pass them into egines
		engine engine1 = new engine(euro,Path,tolerance,confidence);
		engine engine2 = new engine(asia,Path,tolerance,confidence);
		//pricing
		double euroCall = engine1.getOptionPrice();
		double asiaCall = engine2.getOptionPrice();
		System.out.println("The EuroPean Call with strike 165 and expriation 1 year worth: " + euroCall);
		System.out.println("The Asia Call with strike 164 and expriation 1 year worth: " + asiaCall);
		
		// output results to report.tetx
		File file = new File("report.txt");
		PrintWriter writer = new PrintWriter(file);
		writer.println("The simulation is performed under "+ confidence*100 + "% confidence and " +tolerance+" dollar tolerance");
		writer.println("Under the assumption that the future stock price is a geometric brownian motion with:");
		writer.println("daily drift: " + drift +" daily volatility: " +sigma);
		writer.println("The EuroPean Call with strike 165 and expiration 1 year worth: " + euroCall);
		writer.println("The Asia Call with strike 164 and expiration 1 year worth: " + asiaCall);
		writer.close();
	}
}
