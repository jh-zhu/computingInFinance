package simulation;

public class StockPosition {
	
	protected int    _qty;
	protected double _price;
	protected int    _direction; // 0 means no direction, 1 long,-1 short
	protected int    _holdingDays;
	
	public StockPosition() {
		_qty = 0;
		_holdingDays = 0;
		_direction=0;
	}
	
	//update price for a stock, and produce P&L if there are open positions
	public double markToMarket( double newPrice ) {
		double mtm = 0D;
		if( _qty != 0 ) {
			mtm = (double)_qty * ( newPrice - _price );
		}
		_price = newPrice;
		return mtm;
	}

	public double fill( int qty, double newPrice ) {
		double mtm = this.markToMarket( newPrice );
		_qty = _qty + qty;
		return mtm;
	}
	
	// build positions if indicated by signals
	public void build(double value) {
		_qty = 0;
		if(_direction != 0) { 
			_qty = (int)(value/_price) * _direction;
		}

	}
	// return the direction on this stock
	public int getDirection() {
		return _direction;
	}
	// set the direction on this stock
	public void setDirection(int direction) {
		_direction = direction;
	}
	
	// return how many days we have held this stock
	public int getDays() {
		return _holdingDays;
	}
	
	//reset holding days
	public void resetDays() {
		_holdingDays=0;
	}
	//get the quantity we have on this stock
	public int getQTY() {
		return _qty;
	}
	
	// increment holding day for this stock, and close the position if it expires
	public void incrementDay(int daystoHold) {
		if(_direction ==0) {
			_holdingDays = 0;
		}
		else {
			_holdingDays ++;
			// exit position if we hold the position long enough
			if(_holdingDays>=daystoHold) {
				_direction = 0;
				_qty = 0;
			}
		}
		
	}	
}
