package edu.nyu.class4.montecarlo.distributive;

import edu.nyu.class3.montecarlo.engine.*;
import java.util.*;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.io.*;

public class MonteCarloServer {
	private static String brokerURL = "tcp://localhost:61616";
	private static ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private MessageProducer producer;

	public MonteCarloServer() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection  = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(null);
	}
	public void close() throws JMSException {
		if (connection != null) connection.close();
	}

	public static void main(String[] args) throws JMSException, FileNotFoundException, InterruptedException {
		//print writer to write report
		File file = new File("report.txt");
		PrintWriter writer = new PrintWriter(file);
		//create new server
		MonteCarloServer server = new MonteCarloServer();

		// configure the first option that we want to price
		Option option1 = new Option(0.0001,0.01,165,152.35,"EuropeanCall",252);
		// to price this option
		double p1 = server.price(option1);
		// output this price
		System.out.println("The " + option1.getType()+" of strike "+option1.getStrike()+" worths " +p1);
		writer.println("Under the assumption that stock price with move under Geometric Brownian Motion with:");
		writer.println("daily drift: "+option1.getR() + " daily volatility: "+option1.getSigma());
		writer.println("The " + option1.getType()+" of strike "+option1.getStrike()+" worths " +p1);

		//configure the second option that we want to price
		Option option2 = new Option(0.0001,0.01,164,152.35,"AsianCall",252);
		// to price this option
		double p2 = server.price(option2);
		server.close();
		// output the price
		System.out.println("The "+option2.getType()+" of strike "+option2.getStrike()+" worths "+p2);
		writer.println("The "+option2.getType()+" of strike "+option2.getStrike()+" worths "+p2);
		writer.close();
	}

	public double price(Option p) throws JMSException, InterruptedException {
		//configure destinations for server
		Destination topicRequest = session.createTopic("SimulationRequests");
		Destination topicResult = session.createTopic("SimulationResults");
		MessageConsumer consumer = session.createConsumer(topicResult);

		//flag that measures wether the current option pricing is completed
		boolean done =false;
		// maintan variance and mean of the simulation for this price
		statsCollector stat = new statsCollector();
		// in order to generate unique request ids, update every 100 requests are sent out
		int idprefix = 0;
		// A  hashset contains all outsanding Requests
		HashSet<Integer> outstandingRequests = new HashSet<Integer>();

		// set up listener
		MessageListener listener =  new MessageListener() {
			public void onMessage(Message msg) {
				if(msg instanceof TextMessage) {
					try {
						// pass the recieved msg from clients
						TextMessage txtMesg = (TextMessage) msg;
						String s = txtMesg.getText();
						int id = Integer.parseInt(s.split(",")[1]);
						double price = Double.parseDouble(s.split(",")[0]);
						// check if the id is consistent with the id we sent out
						if(outstandingRequests.contains(id)) {
							stat.add(price);
							outstandingRequests.remove(id);
						}
					}
					catch(Exception e) {}
				}
			}
		};
		//hear back from clients
		consumer.setMessageListener(listener);

		while(!done) {
			// make sure that outstanding requests are less than 10
			// don't want requests cumulate over time unprocessed
			while(outstandingRequests.size()<10) {
			// send a batch of 100 request
			for (int i=0;i<100;i++) {
				int id = idprefix*100 +i;
				outstandingRequests.add(id);
				TextMessage msg = session.createTextMessage(p.toString()+","+id);
				producer.send(topicRequest,msg);
			}
			//make sure each request has unique id
			idprefix +=1;
			}
			// under 95% confidence, estimate error dynamically with updated simulation times and variance,
			// when estimated error is less than 0.1, we stop the simulation for this option
			double var = stat.getVariance();
			double n = stat.getN();
			double error = (2.06*Math.sqrt(var))/Math.sqrt(n);
			System.out.println("Received:"+(int)n+" payouts and estimated error is "+error);
			// n>10000 here to make sure the simulation has at least run 10000 times
			if(n>10000 && error < 0.1) done = true;
			}
		// return the price
		return stat.getMean();
	}

}
