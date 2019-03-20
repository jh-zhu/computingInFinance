package orderSpecs;

/** This class represents the client id of a client of the exchange */
public class ClientId {
	
	private String _clientId;

	public ClientId( String clientId ) {
		_clientId = clientId;
	}
	
	public String getValue() { return _clientId; }
	
	@Override
	public String toString() {
		return String.format( "%s(%s)", ClientId.class.getName(), this.getValue() );
	}
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof ClientId ) )
			return false;
		return this.getValue().equals( ((ClientId)o).getValue() );
	}
	
	@Override
	public int hashCode() { return this.getValue().hashCode(); }
	
}
