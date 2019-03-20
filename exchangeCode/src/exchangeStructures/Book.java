package exchangeStructures;

import orderSpecs.*;
import java.util.*;
import orderTypes.*;
import messages.*;
import fills.*;

public class Book {
	private Exchange _exchange;
	private Market _market;
	private Book _otherside;
	private Side _side;
	private TreeMap<Price,PriceLevel> _pricelevels;
	
	public Book(Market market, Side side) {
		_exchange = market.getExchange();
		_market = market;
		_side = side;
		_pricelevels = new TreeMap<Price,PriceLevel>();
	}
	
	public Market getMarket() {
		return _market;
	}
	
	public void setOtherSide(Book otherSideBook) {
		_otherside = otherSideBook;
	}
	
	public TreeMap<Price,PriceLevel> getPriceLevels(){
		return _pricelevels;
	}
	
	// clean up a price level
	public void clean(Price p) {
		if(p == null) return;
		if(!_pricelevels.containsKey(p)) return;
		LinkedList<RestingOrder> restingOrders = _pricelevels.get(p).getOrders();
		for(Iterator<RestingOrder> iterator = restingOrders.iterator();iterator.hasNext();){
			RestingOrder r = iterator.next();
			if(r.isFilled()) {
				_exchange.removeOrder(r.getClientOrderId());
				iterator.remove();
			}
		}
		if(restingOrders.isEmpty()) {_pricelevels.remove(p);}
	}
	
	public void sweep(SweepingOrder sweepingOrder) throws Exception {
		
		SweepingOrder sorder = sweepingOrder;
		
		// get the right comparator (bid or ask) for comparing 
		Comparator<Price> comparator = _side.getComparator();
		
		Price p = sweepingOrder.getPrice();
		Price bookp = null;
		
		/* if it is a offer book, we begin with the lowest value
		 if it is a bid book, we begin with the highest value */
		if(_pricelevels.size()>0) {
			if(_side == Side.SELL) {bookp = _pricelevels.firstKey();}
			else {bookp = _pricelevels.lastKey();}
		}
		
		/*begin to fill order*/
		while(bookp != null && comparator.compare(p, bookp)>= 0 && !sorder.isFilled()) {
			LinkedList<RestingOrder> restingorders = _pricelevels.get(bookp).getOrders();
			// inside a price level
			for(int i=0; i< restingorders.size();i++) {
				
				// if the sweep order is filled, break the for loop
				if(sorder.isFilled()) break;
				
				//if the order is not canceled (like not filled)
				//if the order is cancelled, we just bypass this order and the order will be cleaned up at the end
				RestingOrder rorder = restingorders.get(i);
				if(!rorder.isFilled()) {
					Quantity bookq = rorder.getQuantity();
					Quantity q = sorder.getQuantity();
					Quantity reduceq = bookq.compareTo(q)>=0?new Quantity(q):new Quantity(bookq);
				
					//fill the resting order
					rorder.reduceQtyBy(reduceq);
					Fill f1 = new Fill(rorder.getClientId(),sorder.getClientId(),rorder.getClientOrderId(),reduceq);
					_exchange.sendFill(f1);
				
					//fill the sweep order
					sorder.reduceQtyBy(reduceq);
					Fill f2 = new Fill(sorder.getClientId(),rorder.getClientId(),sorder.getClientOrderId(),reduceq);
					_exchange.sendFill(f2);
					}
			}
			clean(bookp);
			if(_side == Side.SELL)bookp = _pricelevels.higherKey(bookp);	
			else bookp = _pricelevels.lowerKey(bookp);
		}
		/*if sweep order is filled, return*/
		if(sorder.isFilled()) return;
		
		/*rest the filled orders*/
		RestingOrder rest = new RestingOrder(sorder);
		if(_otherside._pricelevels.containsKey(p)) {
			_otherside._pricelevels.get(p).addRestingOrder(rest);
		}
		else {
			PriceLevel plevel = new PriceLevel();
			plevel.addRestingOrder(rest);
			_otherside._pricelevels.put(p, plevel);
			
		}
		_exchange.addOrder(rest.getClientOrderId(), rest);
		RestingOrderConfirmation confirmation = new RestingOrderConfirmation(rest);
		_exchange.sendRestingOrderConfirmation(confirmation);
	}
	

}
