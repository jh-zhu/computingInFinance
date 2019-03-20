package edu.nyu.class3.montecarlo.payout;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import edu.nyu.class3.montecarlo.path.*;

import org.junit.jupiter.api.Test;

class Test_Payout {
	@Test
	public void test_EuroCall() {
		double strike = 120.5;
		DataPoint d1 = new DataPoint();
		DataPoint d2 = new DataPoint();
		DataPoint d3 = new DataPoint();
		d1.date(LocalDate.of(2018, 10, 29));
		d1.price(120.0);
		d2.date(LocalDate.of(2018, 10, 30));
		d2.price(121);
		d3.date(LocalDate.of(2018, 11, 1));
		d3.price(130);
		Path path = new Path();
		path.addDataPoint(d1);
		path.addDataPoint(d2);
		path.addDataPoint(d3);
		EuroCall euro = new EuroCall(strike);
		assertEquals(9.5,euro.payout(path));
	}
	@Test
	public void test_EuroCall2() {
		double strike = 120.5;
		DataPoint d1 = new DataPoint();
		DataPoint d2 = new DataPoint();
		DataPoint d3 = new DataPoint();
		d1.date(LocalDate.of(2018, 10, 29));
		d1.price(120.0);
		d2.date(LocalDate.of(2018, 10, 30));
		d2.price(121);
		d3.date(LocalDate.of(2018, 11, 1));
		d3.price(120);
		Path path = new Path();
		path.addDataPoint(d1);
		path.addDataPoint(d2);
		path.addDataPoint(d3);
		EuroCall euro = new EuroCall(strike);
		assertEquals(0,euro.payout(path));
	}
	
	@Test
	public void test_AsiaCall1() {
		double strike = 120.5;
		DataPoint d1 = new DataPoint();
		DataPoint d2 = new DataPoint();
		DataPoint d3 = new DataPoint();
		d1.date(LocalDate.of(2018, 10, 29));
		d1.price(120);
		d2.date(LocalDate.of(2018, 10, 30));
		d2.price(121);
		d3.date(LocalDate.of(2018, 11, 1));
		d3.price(122);
		Path path = new Path();
		path.addDataPoint(d1);
		path.addDataPoint(d2);
		path.addDataPoint(d3);
		AsiaCall asia = new AsiaCall(strike);
		assertEquals(0.5,asia.payout(path));
	}
	
	@Test
	public void test_AsiaCall2() {
		double strike = 120.5;
		DataPoint d1 = new DataPoint();
		DataPoint d2 = new DataPoint();
		DataPoint d3 = new DataPoint();
		d1.date(LocalDate.of(2018, 10, 29));
		d1.price(120);
		d2.date(LocalDate.of(2018, 10, 30));
		d2.price(121);
		d3.date(LocalDate.of(2018, 11, 1));
		d3.price(119);
		Path path = new Path();
		path.addDataPoint(d1);
		path.addDataPoint(d2);
		path.addDataPoint(d3);
		AsiaCall asia = new AsiaCall(strike);
		assertEquals(0,asia.payout(path));
	}
	
	

}
