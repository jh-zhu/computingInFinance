package edu.nyu.class4.montecarlo.distributive;

import edu.nyu.class3.montecarlo.payout.*;
import edu.nyu.class3.montecarlo.engine.*;
import edu.nyu.class3.montecarlo.random.*;
import edu.nyu.class3.montecarlo.path.*;

import org.apache.activemq.*;
import javax.jms.*;
import javax.jms.Message;

public class MonteCarloClient {
	private static String brokerURL = "tcp://localhost:61616";
	private static ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	
	public MonteCarloClient() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(null);
	}
	
	public static void main(String[] args) throws JMSException {
		MonteCarloClient client = new MonteCarloClient();
		client.run();
	}
	
	public void run() throws JMSException {
		// configure destinations for the client
		Destination topicRequest = session.createTopic("SimulationRequests");
		Destination topicResult = session.createTopic("SimulationResults");
		MessageConsumer consumer = session.createConsumer(topicRequest);
		GaussianRandomGenerator gs = new GaussianRandomGenerator(1);
		// set up the listener
		MessageListener listener =  new MessageListener() {
			public void onMessage(Message msg) {
				if(msg instanceof TextMessage) {
					try {
						// the received msg is a string, we split it by ","
						// access the option information for the received string
						TextMessage txtMsg = (TextMessage) msg;
						String[] elements = txtMsg.getText().split(",");
						String type = elements[0];
						double r = Double.parseDouble(elements[1]);
						double sigma = Double.parseDouble(elements[2]);
						double strike = Double.parseDouble(elements[3]);
						double s0 = Double.parseDouble(elements[4]);
						int t = Integer.parseInt(elements[5]);
						int id = Integer.parseInt(elements[6]);
						// generate a stock path
						PathGenerator pathG = new PathGenerator(t,r,sigma,s0,gs);
						Path p  =pathG.getPath();
						// create payout based on option type
						Payout payout;
						if(type.equals("EuropeanCall")) {
							 payout = new EuroCall(strike);
						}
						// we assume only two options style are meant to passed in here
						// EuropeanCall and AsianCall, so else means AsianCall case here
						else {
							 payout = new AsiaCall(strike);
						}
						// return price and send back to the server
						double price = payout.payout(p);
						// construct the msg sent back to server
						TextMessage tMsg = session.createTextMessage(price+","+id);
						producer.send(topicResult,tMsg);
						System.out.println("Resopnd: Payout:" +price);
					}
					catch(Exception e){
					}
				}
			}
		};
		consumer.setMessageListener(listener);
	}
	
	
}
