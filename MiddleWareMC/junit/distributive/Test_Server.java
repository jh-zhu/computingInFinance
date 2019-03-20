package edu.nyu.class4.montecarlo.distributive;

import static org.junit.jupiter.api.Assertions.*;

import javax.jms.JMSException;

import org.junit.jupiter.api.Test;

class Test_Server {
	@Test
	public void test1() throws JMSException, InterruptedException {
		MonteCarloServer server = new MonteCarloServer();
		Option option1 = new Option(0.0001,0.001,165,152.35,"EuropeanCall",252);
		double p = server.price(option1);
	}

}
