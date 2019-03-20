package orderSpecs;

/** Class that represents a price in dollars multiplied by 10000.
 * This is a common way to avoid round-off. This class will throw
 * an exception if the price is less than or equal to zero.
 */
public class Price implements Comparable<Price>{
	
	private long _dollarsTimesTenThousand;
	
	/** Create a price from a long value of dollars scaled up by
	 * 10000.
	 * @param dollarsTimesTenThousand Price in dollars multiplied by 10000
	 * @throws Exception Thrown if price is less than 0
	 */
	public Price( long dollarsTimesTenThousand ) throws Exception {
		if( dollarsTimesTenThousand <= 0 ) {
			throw new Exception( "In constructor of Price, argument dollarsTimesTenThousand cannot be less than zero" );
		}
		_dollarsTimesTenThousand = dollarsTimesTenThousand;
	}

	/** Copy constructor of price */
	public Price( Price otherPrice ) {
		_dollarsTimesTenThousand = otherPrice.getValue();
	}

	/** @return Dollars multiplied by 10000 expressed as a long value
	 */
	public long getValue() { return _dollarsTimesTenThousand; }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof Price ) )
			return false;
		Price price = (Price) o;
		return this.compareTo( price ) == 0;
	}
	
	@Override
	public int compareTo( Price price ) {
		long v1 = this.getValue();
		long v2 = price.getValue();
		if( v1 > v2 )
			return 1;
		if( v2 > v1 )
			return -1;
		return 0;
	}
	
	@Override
	public String toString() { return String.format( "%s(%d)", this.getClass().getName(), this.getValue() ); }
	
}
