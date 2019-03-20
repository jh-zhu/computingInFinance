package exchangeStructures;

import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Side;
import orderTypes.RestingOrder;
import orderTypes.SweepingOrder;

public class Market {

	private Exchange _exchange;
	private MarketId _marketId;
	private Book _bidBook;
	private Book _offerBook;
	
	public Market( Exchange exchange, MarketId marketId ) throws Exception {
		_exchange = exchange;
		_marketId = marketId;
		_bidBook = new Book( this, Side.BUY );
		_offerBook = new Book( this, Side.SELL );
		_bidBook.setOtherSide( _offerBook );
		_offerBook.setOtherSide( _bidBook );
	}
	
	public Exchange getExchange() { return _exchange; }
	public MarketId getMarketId() { return _marketId; }
	public Book getBidBook() { return _bidBook; }
	public Book getOfferBook() { return _offerBook; }
	
	public void sweep( SweepingOrder sweepingOrder ) throws Exception {
		if( sweepingOrder.getSide() == Side.BUY )
			_offerBook.sweep( sweepingOrder );
		else
			_bidBook.sweep( sweepingOrder );
	}

	/**
	 * @return Number of price levels in this market's offer book
	 */
	public int getNumOfferBookPriceLevels() {
		return this.getOfferBook().getPriceLevels().size();
	}

	/**
	 * @return Number of price levels in this market's bid book
	 */
	public int getNumBidBookPriceLevels() {
		return this.getBidBook().getPriceLevels().size();
	}

	/**
	 * Returns a resting order identified by its price level and position in the queue
	 * @param price The price of the price level where we will look for the resting order
	 * @param orderIndex The index of the order at that price level
	 * @return The resting order at the specified price level and position in the queue
	 */
	public RestingOrder getRestingBid( Price price, int orderIndex) {
		Book bidBook = this.getBidBook();
		PriceLevel priceLevel = bidBook.getPriceLevels().get( price );
		return priceLevel.getOrders().get( orderIndex );
	}
	
}
