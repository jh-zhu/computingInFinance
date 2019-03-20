package messages;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

/** This class represents a message sent by the exchange
 * to a client telling the client that the client's attempt
 * to cancel a resting order was rejected. This could
 * happen if the order was filled or already cancelled.
 */
public class CancelRejected extends AbstractMessage {

	/** Create a new CancelRejected message */
	public CancelRejected( ClientId clientId, ClientOrderId clientOrderId ) {
		super( clientId, clientOrderId );
	}
	
	@Override
	public String toString() {
		return String.format(
			"%s(%s,%s)",
				this.getClass().getName(),
				this.getClientId().toString(),
				this.getClientOrderId().toString()
		);
	}

	@Override
	public String getDescription() { return this.toString(); }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof CancelRejected ) )
			return false;
		CancelRejected msg = (CancelRejected) o;
		return (
			this.getClientId().equals( msg.getClientId() ) &&
			this.getClientOrderId().equals( msg.getClientOrderId() )
		);
	}

}
