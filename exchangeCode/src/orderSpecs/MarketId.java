package orderSpecs;

/** This class represents the market id of a market such as IBM
 * that has its own bid and offer book and is traded on an 
 * exchange.
 */
public class MarketId {

	private String _marketId;
	public MarketId( String marketId ) {
		_marketId = marketId;
	}
	
	public String getValue() { return _marketId; }
	
	@Override
	public int hashCode() { return _marketId.hashCode(); }
	
	@Override
	public String toString() { return String.format("%s(%s)", MarketId.class.getName(), _marketId ); }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof MarketId ) )
			return false;
		return this.getValue().equals( ((MarketId)o).getValue());
	}

}
