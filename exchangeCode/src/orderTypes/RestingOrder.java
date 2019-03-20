package orderTypes;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.Price;
import orderSpecs.Quantity;

/** This class represents a resting order that is sitting in either
 * the bid book or the offer book waiting for a sweeping order
 * to provide it with a match. There are two instance variables
 * in RestingOrder, the _sweepingOrder, which is the SweepingOrder
 * that failed to fill in its sweep and became this RestingOrder,
 * and _quantity, which is initially set to the unfilled quantity
 * of the SweepingOrder but then may differ as the RestingOrder
 * is partially (and eventually) fully filled.
 */
public class RestingOrder {

	private SweepingOrder _sweepingOrder;
	private Quantity      _quantity;

	/** Create a new resting order
	 * 
	 * @param sweepingOrder The sweeping order that became this resting
	 *                      order when it failed to (fully) fill
	 */
	public RestingOrder( SweepingOrder sweepingOrder ) {
		_sweepingOrder = sweepingOrder;
		_quantity = new Quantity( sweepingOrder.getQuantity() );
	}
	
	/**
	 * @return The sweeping order that was made into this RestingOrder
	 */
	public ClientOrderId getClientOrderId() { return _sweepingOrder.getClientOrderId(); }
	
	/**
	 * @return The limit price of the sweeping order that was made into
	 *         this resting order. Obviously, the limit price doesn't
	 *         change.
	 */
	public Price getPrice() { return _sweepingOrder.getPrice(); }
	
	/**
	 * Set the quantity of this order to zero
	 */
	public void cancel() throws Exception { _quantity = new Quantity( 0L ); }
	
	/**
	 * @return The quantity of this order
	 */
	public Quantity getQuantity() { return _quantity; }
	
	/**
	 * @return The client id of the sweeping order that was made into
	 *         this resting order
	 */
	public ClientId getClientId() { return _sweepingOrder.getClientId(); }

	/** Reduce the quantity of this resting order by another quantity
	 * 
	 * @param matchQty Quantity by which to reduce quantity of this order
	 */
	public void reduceQtyBy( Quantity matchQty ) throws Exception {
		_quantity.reduceBy( matchQty );
	}

	public SweepingOrder getSweepingOrder() { return _sweepingOrder; }

	public boolean isFilled() { return _quantity.getValue() == 0; }
	
	@Override
	public int hashCode() { return this.getClientOrderId().hashCode(); }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof RestingOrder ) )
			return false;
		RestingOrder order = (RestingOrder)o;
		return(
			( this.getQuantity().equals( order.getQuantity() ) ) &&
			( this.getSweepingOrder().equals( order.getSweepingOrder() ) )
		);
	}
	
	@Override
	public String toString() {
		return String.format(
			"%s(%s,%s)",
				this.getClass().getName(),
				this.getQuantity().toString(),
				this.getSweepingOrder().toString()
		);
	}
	
}
