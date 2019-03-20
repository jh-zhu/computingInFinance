package messages;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

/** This class represents a message sent by the
 * exchange to the client via the comms link.
 */
public class Cancelled extends AbstractMessage {

	/** Create a new cancelled message to client.
	 * 
	 * @param clientId Id of the client to whom this message is going
	 * @param clientOrderId Id of the order that was cancelled
	 */
	public Cancelled( ClientId clientId, ClientOrderId clientOrderId ) {
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
	public int hashCode() { return this.getClientOrderId().hashCode(); }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof Cancelled ) )
			return false;
		Cancelled msg = (Cancelled) o;
		return(
			( this.getClientId().equals( msg.getClientId() ) ) &&
			( this.getClientOrderId().equals( msg.getClientOrderId() ) )
		);
	}
	
}
