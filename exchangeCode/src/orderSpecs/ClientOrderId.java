package orderSpecs;

/** This message represents the order id of a sweeping order
 * created by a client. If a sweeping order is converted
 * to a resting order, this could become the id of that
 * resting order.
 */
public class ClientOrderId {

	private String _clientOrderId;
	
	public ClientOrderId( String clientOrderId ) {
		_clientOrderId = clientOrderId;
	}
	
	public String getValue() { return _clientOrderId; }
	
	@Override
	public String toString() { return String.format( "%s(%s)", ClientOrderId.class.getName(), this.getValue() ); }

	@Override
	public int hashCode() { return this.getValue().hashCode(); }
	
	@Override
	public boolean equals( Object o ) {
		if( o == this )
			return true;
		if( !( o instanceof ClientOrderId ) )
			return false;
		return this.getValue().equals( ( (ClientOrderId)o).getValue() );
	}
	
}
