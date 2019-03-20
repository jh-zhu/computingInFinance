package messages;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

/**
 * This class represents a cancel message sent by the client
 * to the exchange via its cancel method.
 */
public class Cancel extends AbstractMessage {

	/** Create a new Cancel message */
	public Cancel( ClientId clientId, ClientOrderId clientOrderId ) {
		super( clientId, clientOrderId );
	}

	@Override
	public String getDescription() { return this.toString(); }
	
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
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof Cancel ) )
			return false;
		Cancel cancel = (Cancel) o;
		return( 
			this.getClientId().equals( cancel.getClientId() ) && 
			this.getClientOrderId().equals( cancel.getClientOrderId() )
		);
	}
	
	@Override
	public int hashCode() { return this.getClientOrderId().hashCode(); }
	
}