package orderTypes;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;

public class SweepingOrder {
	
	private ClientId _clientId;
	private Quantity _quantity;
	private Price _price;
	private ClientOrderId _clientOrderId;
	private MarketId _marketId;
	private Side _side;

	/** Construct a sweeping order
	 * 
	 * @param clientId Id of the client who placed the order
	 * @param clientOrderId Id that uniquely identifies this order, any resting order it may
	 *                      create, and any fills that may result from a match
	 * @param marketId The id of the market this order is intended to sweep, eg IBM
	 * @param side One of two static variable in class Side, Side.BUY or Side.SELL
	 * @param quantity Quantity of this sweeping order
	 * @param price Price of this sweeping order multiplied by 10000
	 */
	public SweepingOrder(
		ClientId clientId,
		ClientOrderId clientOrderId,
		MarketId marketId,
		Side side, // If side is buy, sweep offers, and vice versa
		Quantity quantity,
		Price price
	) {
		_clientId = clientId;
		_quantity = new Quantity( quantity );
		_price = new Price( price );
		_clientOrderId = clientOrderId;
		_marketId = marketId;
		_side = side;
	}
	
	@Override
	public String toString() {
		return String.format(
			"%s(%s,%s,%s,%s,%s,%s)",
				this.getClass().getName(),
				this.getClientId(),
				this.getClientOrderId(),
				this.getMarketId(),
				this.getSide(),
				this.getQuantity(),
				this.getPrice()
		);
	}
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof SweepingOrder ) )
			return false;
		SweepingOrder order = (SweepingOrder) o;
		return(
			( this.getClientId().equals( order.getClientId() ) ) &&
			( this.getClientOrderId().equals( order.getClientOrderId() ) ) &&
			( this.getMarketId().equals( order.getMarketId() ) ) &&
			( this.getSide().equals( order.getSide() ) ) &&
			( this.getQuantity().equals( order.getQuantity() ) ) &&
			( this.getPrice().equals( order.getPrice() ) )
		);
	}
	
	@Override
	public int hashCode() { return this.getClientOrderId().hashCode(); }
	
	/**
	 * @return Remaining quantity of this sweeping order
	 */
	public Quantity getQuantity() { return _quantity; } 
	
	/**
	 * @return Limit price of this sweeping order
	 */
	public Price getPrice() { return _price; }

	/**
	 * @return Client order id of this order
	 */
	public ClientOrderId getClientOrderId() { return _clientOrderId; }

	/**
	 * @return Market id of the market that this order is supposed
	 *         to sweep
	 */
	public MarketId getMarketId() { return _marketId; }
	
	/** Returns the side of this order
	 * @return Either Side.BUY or Side.SELL
	 */
	public Side getSide() { return _side; }

	/** Return the client id of the client who placed this order
	 * @return Get id of the client who placed this sweeping
	 *         order
	 */
	public ClientId getClientId() { return _clientId; }

	/** Reduces the quantity of this order by another quantity
	 * @param qty Amount to subtract from current quantity
	 * @throws Exception Thrown if the amount to subtract is greater
	 *                   than the amount available
	 */
	public void reduceQtyBy( Quantity qty ) throws Exception {
		if( qty.getValue() > this.getQuantity().getValue() )
			throw new Exception( String.format( "In reduceQtyBy method of SweepingOrder, can't reduce qty %d by %d", this.getQuantity().getValue(), qty.getValue() ) );
		_quantity.reduceBy( qty );
	}

	/** Returns true if this order has an unfilled quantity
	 * @return True if this order has a quantity of zero - ie it has
	 *         been filled
	 */
	public boolean isFilled() { return _quantity.getValue() == 0; }
	
}
