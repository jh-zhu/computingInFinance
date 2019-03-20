package exchangeStructures;

import java.util.*;
import orderTypes.*;


public class PriceLevel {
	private LinkedList<RestingOrder> _restingOrders;
	
	public PriceLevel() {
		_restingOrders = new LinkedList<RestingOrder>();
	}
	
	public void addRestingOrder(RestingOrder r) {
		_restingOrders.add(r);
	}
	
	public LinkedList<RestingOrder> getOrders(){
		return _restingOrders;
	}
}
