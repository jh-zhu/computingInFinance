package simulation;

import java.util.LinkedList;

import dbReaderFramework.I_DBProcessor;
import dbReaderFramework.I_DBReader;
import simulation.SignalFileReader.Signal;


public class Simulation implements I_DBProcessor {
	
	protected MergeFormatTradesDBReader _tradesReader;
	protected SignalFileReader          _signalFileReader;
	protected int                       _daysToHold;
	protected Double                    _cash;
	protected StockPortfolio			_portfolio; //the portfolio to hold for each simulation
	protected double					_PL;        //cumulated P&L within a day
	protected statsCollector			_stats;
	
	public Simulation( MergeFormatTradesDBReader tradesReader, SignalFileReader signalFileReader, int daysToHold ) {
		_tradesReader = tradesReader;
		_signalFileReader = signalFileReader;
		_daysToHold = daysToHold;
		_cash = 10000D;
		_portfolio = new StockPortfolio();
		_PL = 0D;
		_stats = new statsCollector();
	}

	@Override
	public boolean processReaders( long sequenceNumber, int numReadersWithNewData, LinkedList<I_DBReader> readers) {
		// as new price comes in, we first MTM our existing positions
		if(sequenceNumber == _tradesReader.getLastSequenceNumberRead()) {
			LinkedList<LongTradeRec> records = _tradesReader.getRecords();
			// process all records
			for (int i=0;i<records.size();i++) {
				double change = 0;
				//P&L in each position
				change = _portfolio.updatePrice(records.get(i));
				// cumulated P&L and cash remaining in our account are affected by these MTM
				_cash += change;
				_PL += change;

			}
		}
		
		// as new signal comes in, we update our positions
		if(sequenceNumber == _signalFileReader.getLastSequenceNumberRead()) {
			LinkedList<Signal> signals = _signalFileReader.getRecords();
			// process all signals
			for(int j=0;j<signals.size();j++) {
				_portfolio.updateSignal(signals.get(j));
			}
		}
		
		//now we can build new positions
		//how many positions to open
		int count = _portfolio.getCount();
		// value for each position
		double value = _cash/count;
		//open this positions
		_portfolio.build(value);
		
		//at the end of each day (24*60*60*1000 milli second as a day)
		// firt we check if it is the end of day
		final long day = 24*60*60*1000;
		final long start = 1182346200000L;
		if((sequenceNumber-start)%day == 0 && sequenceNumber!=start) {
			//reprot daily return
			long n = (sequenceNumber-start)/day;
			System.out.println("Hold "+_daysToHold+" days. Return for day "+n+" is "+_PL );
			// add daily cumulated P&L		
			_stats.add(_PL);
			// reset P&L to 0
			_PL = 0;
			// increment holding days
			_portfolio.incrementDay(_daysToHold);
		}
		
		return true;
	}

	@Override
	public void stop() throws Exception {
	}

	public Double getFitness() {
		double r = _cash - 10000;
		double std = _stats.getVariance();
		std = Math.sqrt(std);
		double sharp = r/std;
		//report sharp for this holding period
		System.out.println("sharp ratio for hold " + _daysToHold+" days is " + sharp);
		// add 999 to sharp ratio allowing ga to maximize it
		return 9999D + sharp;
		
	}

}
