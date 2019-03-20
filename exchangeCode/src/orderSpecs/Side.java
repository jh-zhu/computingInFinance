package orderSpecs;

import java.util.Comparator;

/** This class represents a side, either buy or sell.
 * 
 * There are only two possible choices, a buy or a sell, so
 * we will make it impossible to instantiate other Side objects
 * by making the constructor of this class private. We will
 * create two Side object, a Side.BUY and Side.SELL and make
 * those static and public so they can be accessed by other
 * classes. The side object also knows how to compare prices
 * via two possible comparators, a BidComparator and an
 * OfferComparator. (When comparing prices in the bid book,
 * we start with the highest first, and in the offer book,
 * with the lowest first.)
 *
 */
public class Side {
	
	private static Comparator<Price> BidComparator = new Comparator<Price>() {
		@Override
		public int compare(Price o1, Price o2) {
			return o2.compareTo( o1 );
		}
	};
	
	private static Comparator<Price> OfferComparator = new Comparator<Price>() {
		@Override
		public int compare(Price o1, Price o2) {
			return o1.compareTo( o2 );
		}
	};
	
	public static Side BUY = new Side( "BUY", BidComparator );
	public static Side SELL = new Side( "SELL", OfferComparator );

	private String            _description;
	private int               _hashCode;
	private Comparator<Price> _comparator;
	
	/** Instantiate side object. Save description string - eg "BUY" - and 
	 * comparator, and save a permanent hashCode value (not necessary
	 * to compue it on the fly)
	 * 
	 * @param description "BUY" or "SELL"
	 * @param comparator Comparator for comparing prices in bid or 
	 *                   offer book
	 */
	private Side( String description, Comparator<Price> comparator ) {
		_description = description;
		_hashCode = _description.hashCode();
		_comparator = comparator;
	}
	
	@Override
	public String toString() {
		return String.format(
			"%s(%s)",
				this.getClass().getName(),
				_description
		);
	}
	
	@Override
	public int hashCode() { return _hashCode; }
	
	@Override
	public boolean equals( Object o ) {
		// There are only two Side objects, so there is no
		// need for comparisons beyond identity comparisons
		return( this == o );
	}
	
	/**
	 * @return Give access to side-specific comparator of prices
	 */
	public Comparator<Price> getComparator() { return _comparator; }
	
}
