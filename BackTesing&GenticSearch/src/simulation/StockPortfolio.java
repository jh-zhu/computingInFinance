package simulation;


import simulation.SignalFileReader.Signal;

public class StockPortfolio {
	
	protected StockPosition[] _positions;
	
	// how many positions do we have in this portfolio
	protected int _count;
	
	//portfolio constructor
	public StockPortfolio() {
		_positions = new StockPosition[ 1004 ];
		for( int i = 0; i < 1004; i++ ) {
			_positions[ i ] = new StockPosition();
		}
		_count = 0;
	}
	
	//update price for each stock, and produce MTM if we have open positions
	public double updatePrice( LongTradeRec tradeRec ) {
		StockPosition position = _positions[ tradeRec.getId() ];
		return position.markToMarket(tradeRec.getPrice());
	}
	
	// process signal, to decide close positions or open new positions
	public void updateSignal( Signal signal ) {
		StockPosition position = _positions[ signal.getId() ];
		//previously no postion
		if(position.getDirection() == 0) {
			//open new position
			position.setDirection(signal.getSignal());
			_count++;
		}
		//previously long
		if(position.getDirection() == 1) {
			//keep long
			if(signal.getSignal()==1) {
				position.resetDays();
			}
			// close long
			else {
				position.setDirection(0);
				_count --;
			}
		}
		// previously short
		if(position.getDirection()==-1) {
			// close short
			if(signal.getSignal()==1) {
				position.setDirection(0);
				_count--;
			}
			//keep short
			else {
				position.resetDays();
			}
		}		
	}
	
	//this function build positions for stocks indicated by signals
	public void build(double value) {
		//loop over all stocks
		for(int k=0;k<1004;k++) {
			_positions[k].build(value);
		}
	}
	
	//return the number of positions in this portfolio
	public int getCount() {
		return _count;
	}
	
	//increment holding days for each stock
	//close positions if they are expired due to holding period
	public void incrementDay(int daystoHold) {
		for(int i = 0;i<1004;i++) {
			_positions[i].incrementDay(daystoHold);
		}
	}
	
	
}
