package exchangeStructures;

import java.util.*;
import orderSpecs.*;
import orderTypes.*;
import messages.*;
import fills.*;

public class Exchange {
	private HashMap<MarketId,Market> _markets;
	private HashMap<ClientOrderId,RestingOrder> _restings;
	private Comms _comms;
	
	public Exchange() {
		_markets = new HashMap<MarketId,Market>();
		_restings = new HashMap<ClientOrderId,RestingOrder>();
		_comms = new Comms();
		
	}
	
	public void addMarket(Market market) {
		MarketId id = market.getMarketId();
		_markets.put(id, market);
	}
	public Market getMarket(MarketId marketid) {
		return _markets.get(marketid);
	}
	
	public RestingOrder getOrder(ClientOrderId id) {
		return _restings.get(id);
	}
	
	public void addOrder(ClientOrderId id, RestingOrder rest) {
		_restings.put(id, rest);
	}
	
	public void removeOrder(ClientOrderId id) {
		_restings.remove(id);
	}
	
	public Comms getComms() {
		return _comms;
	}
	
	public void sendRestingOrderConfirmation(RestingOrderConfirmation confirmation) {
		_comms.sendRestingOrderConfirmation(confirmation);
	}
	
	public void sendFill(Fill f) {
		_comms.sendFill(f);
	}
	
	public void cancel(Cancel c) throws Exception {
		ClientId cid = c.getClientId();
		ClientOrderId coid = c.getClientOrderId();
		
		// failed to cancel
		if(!_restings.containsKey(coid)) {
			CancelRejected reject = new CancelRejected(cid,coid);
			_comms.sendCancelRejected(reject);
			return;
		}
		
		//cancel
		RestingOrder r =  _restings.get(coid);
		r.cancel();
		_restings.remove(coid);
		Cancelled cancelled = new Cancelled(cid,coid);
		_comms.cancelled(cancelled);
	}
	
	
	// call sweep method in the right market
	public void sweep(SweepingOrder sweepingOrder) throws Exception {
		MarketId marketId = sweepingOrder.getMarketId();
		if(!_markets.containsKey(marketId)) throw new Exception("The market is not exists");
		else _markets.get(marketId).sweep(sweepingOrder);
	}
	
	
}
