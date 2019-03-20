package orderSpecs;

/** Class that represents a quantity of stock. Cannot be
 * less than zero.
 */
public class Quantity implements Comparable<Quantity> {
	
	private long _value;
	
	/** Copy constructor of quantity */
	public Quantity( final Quantity quantity ) {
		_value = quantity.getValue();
	}
	
	/** Create a new quantity from a long value */
	public Quantity( long value ) throws Exception {
		if( value < 0 )
			throw new Exception( String.format( "In constructor of Quantity, value cannot be less than zero. value = %d", value ) );
		_value = value;
	}
	
	/** Return long value of this quantity */
	public long getValue() { return _value; }
	
	@Override
	public int hashCode() { return Long.hashCode( _value ); }
	
	@Override
	public boolean equals( Object object ) {
		if( this == object )
			return true;
		if( !( object instanceof Quantity ) )
			return false;
		return ((Quantity) object).getValue() == this.getValue();
	}
	
	@Override
	public int compareTo( Quantity quantity ) {
		long v1 = this.getValue();
		long v2 = quantity.getValue();
		if( v1 > v2 )
			return 1;
		if( v2 > v1 )
			return -1;
		return 0;
	}

	/** Reduce this quantity by another quantity
	 * 
	 * @param quantity Quantity by which to reduce this quantity
	 * @throws Exception Thrown if the results are less than zero
	 */
	public void reduceBy(Quantity quantity) throws Exception {
		if( this.compareTo( quantity ) < 0 )
			throw new Exception( 
				String.format( 
					"Can't reduce quantity %d by %d in method aQuantity.reduceBy", 
					_value, 
					quantity.getValue()
				) 
			);
		_value -= quantity.getValue();
	}
	
	@Override
	public String toString() { return String.format("%s(%d)", this.getClass().getName(), this.getValue() ); }

}
