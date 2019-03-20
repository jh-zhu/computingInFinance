package messages;

import orderTypes.RestingOrder;

/** This class represents a message to the client telling the
 * client that the client's sweeping order was not completely
 * filled and was made into a resting order that is now part
 * of the appropriate book.
 */
public class RestingOrderConfirmation extends AbstractMessage {
	
	/** The resting order to which this message refers */ 
	private RestingOrder _restingOrder;
	
	/** Create a new RestingOrderConfirmation and save a
	 * reference to the resting order
	 * @param restingOrder The resting order to which this message
	 *                     makes reference
	 */
	public RestingOrderConfirmation( RestingOrder restingOrder ) {
		super( restingOrder.getClientId(), restingOrder.getClientOrderId() );
		_restingOrder = restingOrder;
	}
	/**
	 * @return Resting order to which this message makes reference
	 */
	public RestingOrder getRestingOrder() { return _restingOrder; }

	@Override
	public String getDescription() { return this.toString(); }
	
	@Override
	public String toString() {
		return String.format(
			"%s(%s)",
				this.getClass().getName(),
				this.getRestingOrder().toString()
		);
	}
	
	@Override
	public int hashCode() { return _restingOrder.hashCode(); }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof RestingOrderConfirmation ) )
			return false;
		RestingOrderConfirmation roc = (RestingOrderConfirmation) o;
		return(
			( this.getRestingOrder().equals( roc ) ) &&
			( this.getClientId().equals( roc.getClientId() ) ) &&
			( this.getClientOrderId().equals( roc.getClientOrderId() ) )
		);
	}
	
}
