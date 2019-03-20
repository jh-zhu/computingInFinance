package exchangeStructures;

import java.util.LinkedList;

import fills.Fill;
import messages.Cancel;
import messages.CancelRejected;
import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;
import orderTypes.RestingOrder;
import orderTypes.SweepingOrder;

public class Test_Exchange extends junit.framework.TestCase {

	/**
	 * One giant test of all exchange functions 
	 */
	public void test1() throws Exception {
		
		// Create an exchange and add one market
		Exchange exchange = new Exchange();
		MarketId marketId0 = new MarketId( "IBM" );
		Market market = new Market( exchange, marketId0 );
		exchange.addMarket( market );
		
		// Before we start, there should be no price levels
		// in either the bid book or the offer book of the
		// above market
		assertTrue( exchange.getMarket( marketId0 ).getOfferBook().getPriceLevels().size() == 0 );
		assertTrue( exchange.getMarket( marketId0 ).getBidBook().getPriceLevels().size() == 0 );
		
		// Create a client order
		ClientId clientId0 = new ClientId( "Lee" );
		ClientOrderId clientOrderId0 = new ClientOrderId( "ABC" );
		Side side0 = Side.BUY;
		Quantity quantity0 = new Quantity( 1000L );
		Price price0 = new Price( 1280000 );
		SweepingOrder sweepingOrder = new SweepingOrder(
			clientId0,
			clientOrderId0,
			marketId0,
			side0,
			quantity0,
			price0
		);

		// Sweep the exchange with this order
		exchange.sweep( sweepingOrder );
		
		// No part of the first order to buy 1000 shares at $128
		// was executed because the offer book was empty. So 
		// the entire order became a resting order in the bid 
		// book at a price level of $128.
		
		// There should be one price level in the bid book
		assertTrue( exchange.getMarket( marketId0 ).getBidBook().getPriceLevels().size() == 1 );
		// There should be no price levels in the offer book
		assertTrue( exchange.getMarket( marketId0 ).getOfferBook().getPriceLevels().size() == 0 );
		// The exchange should know about this order
		assertTrue( exchange.getOrder( sweepingOrder.getClientOrderId() ).equals( new RestingOrder( sweepingOrder ) ) );

		// Make sure that the above sweeping order generated a resting order
		// Specify the price level we will examine
		Price priceOfPriceLevel = sweepingOrder.getPrice();
		// We want the first order at that price level
		int orderIndex = 0; 
		assertTrue( 
			exchange
				.getMarket( marketId0 )
				.getBidBook()
				.getPriceLevels()
				.get( priceOfPriceLevel )
				.getOrders()
				.get( orderIndex )
				.equals( new RestingOrder( sweepingOrder ) ) 
		);
		
		// Make sure that the market sent an alert to the client
		// about the new resting order via the fake comms link
		RestingOrder restingOrder = exchange.getComms().getRestingOrderConfirmations().getLast().getRestingOrder();
		assertTrue( restingOrder.equals( new RestingOrder( sweepingOrder ) ) );
		assertTrue( restingOrder.getQuantity().equals( new Quantity( 1000 ) ) );
		// The message is sitting in comms just so we can check
		// it. Now that we've checked it, we can remove it so it
		// doesn't interfere with the rest of our tests.
		exchange.getComms().getRestingOrderConfirmations().removeLast();
		
		// We're moving on to our second order, a sell of 500
		// shares. It will match partially with our 1st order
		// which is now a resting order in the bid book.
		ClientId clientId1 = new ClientId( "Bob" );
		ClientOrderId clientOrderId1 = new ClientOrderId( "VZFZF" );
		MarketId marketId1 = new MarketId( "IBM" );
		Side side1 = Side.SELL;
		Quantity quantity1 = new Quantity( 500L ); // Half of the 1000 that's already in the book
		Price price1 = new Price( 1200000L );
		SweepingOrder sweepingOrder1 = new SweepingOrder(
			clientId1,
			clientOrderId1,
			marketId1,
			side1,
			quantity1,
			price1
		);
		// Sweep exchange with this order
		exchange.sweep( sweepingOrder1 );
		
		// This order should have generated two fills of 500 each.
		// One goes to Bob, whose sweeping order caused the fill.
		// And the other goes to Lee, whose resting order was
		// filled by the sweeping order. Lee is Bob's counter party.
		Fill fill = exchange.getComms().getFills().get(0);
		assertTrue( fill.getClientId().equals( new ClientId( "Lee" ) ) );
		assertTrue( fill.getCounterpartyId().equals( new ClientId( "Bob" ) ) );
		assertTrue( fill.getQuantity().equals( new Quantity( 500 ) ) );
		// The clientOrderId of the first fill should be the client
		// order id of the resting order that was filled by the 
		// sweeping order
		assertTrue( fill.getClientOrderId().equals( sweepingOrder.getClientOrderId() ) );
		// Here is the second fill - the fill of the sweeping order
		fill = exchange.getComms().getFills().get(1);
		assertTrue( fill.getClientId().equals( new ClientId( "Bob" ) ) );
		assertTrue( fill.getCounterpartyId().equals( new ClientId( "Lee" ) ) );
		assertTrue( fill.getQuantity().equals( new Quantity( 500 ) ) );
		// The clientOrderId of the second fill should be the clientOrderId
		// of the sweeping order that produced the match
		assertTrue( fill.getClientOrderId().equals( sweepingOrder1.getClientOrderId() ) );
		
		// Remove the two fills so we are working with a clean record
		// for the next test
		exchange.getComms().getFills().removeLast();
		exchange.getComms().getFills().removeLast();


		// There should be one price level in the bid book
		assertTrue( exchange.getMarket(marketId0).getBidBook().getPriceLevels().size() == 1 );
		// There should be no price levels in the offer book
		assertTrue( exchange.getMarket(marketId0).getOfferBook().getPriceLevels().size() == 0 );
		// The single resting order in the bid book should be sitting
		// at price level $128. We want the first order at that price level
		orderIndex = 0; 
		// Retrieve that order and check its contents
		restingOrder = exchange.getMarket( marketId0 ).getBidBook().getPriceLevels().get( priceOfPriceLevel ).getOrders().get( orderIndex );
		// It should have an unfilled quantity of 500 shares
		assertTrue( restingOrder.getQuantity().equals( new Quantity( 500L ) ) );
		// It should have the clientOrderId of the original sweeping
		// order that created it
		assertTrue( restingOrder.getClientId().equals( sweepingOrder.getClientId() ) );
		
		// There are 500 shares sitting in the bid book at $128
		// We will now add three orders to the bid book then sweep
		// with a sell order that takes out the first three
		// orders in the bid book but, because of its limit price
		// doesn't match with the fourth. The remainder of the
		// sweeping order will then be added to the offer book
		// as a resting order.
		
		ClientId clientId2 = new ClientId( "Steve" );
		ClientOrderId clientOrderId2 = new ClientOrderId( "UnP17az" );
		MarketId marketId2 = new MarketId( "IBM" );
		Side side2 = Side.BUY;
		Quantity quantity2 = new Quantity( 300 );
		Price price2 = new Price( 1270000 );
		SweepingOrder sweepingOrder2 = new SweepingOrder(
			clientId2,
			clientOrderId2,
			marketId2,
			side2,
			quantity2,
			price2
		);
		
		// Sweep exchange with this order
		exchange.sweep( sweepingOrder2 );
		
		// Make sure there are now two price levels for the bid book of
		// market "IBM"
		assertTrue(
			exchange
				.getMarket( marketId2 )
				.getBidBook()
				.getPriceLevels()
				.size()
			== 2
		);
		
		// Make sure that this last sweeping order didn't match - there 
		// was nothing to match with - and became a resting order
		orderIndex = 0;
		assert(
			exchange
				.getMarket(marketId2)
				.getBidBook()
				.getPriceLevels()
				.get( price2 )
				.getOrders()
				.get( orderIndex )
				.equals( 
					new RestingOrder( sweepingOrder2 ) 
				)
		);
		
		// There should now be two orders in the bid book, one
		// for 500 shares at $128 and one for 300 shares at $127
		// We will add another order at $127 to makes sure that
		// price level correctly manages more than one order
		
		ClientId clientId3 = clientId2; // Same client
		ClientOrderId clientOrderId3 = new ClientOrderId( "llLWE" );
		MarketId marketId3 = new MarketId( "IBM" );
		Side side3 = Side.BUY;
		Quantity quantity3 = new Quantity( 300 );
		Price price3 = new Price( 1270000 );
		SweepingOrder sweepingOrder3 = new SweepingOrder(
			clientId3,
			clientOrderId3, // Same client, different order
			marketId3,
			side3,
			quantity3,
			price3
		);
		
		// Sweep exchange with this order
		exchange.sweep( sweepingOrder3 );
		
		// Make sure exchange knows about this order
		assertTrue( exchange.getOrder( clientOrderId3 ) != null );

		// There should be two price levels in the bid book
		assertTrue(
			exchange
				.getMarket( marketId3 )
				.getBidBook()
				.getPriceLevels()
				.size()
			== 2
		);

		// There should now be three orders in the bid book, one
		// for 500 shares at $128, one for 300 shares at $127,
		// and another one for 300 shares at $127. We will add
		// a fourth order at $125.
		
		ClientId clientId4 = clientId3;
		ClientOrderId clientOrderId4 = new ClientOrderId( "ZuPER" );
		MarketId marketId4 = new MarketId( "IBM" );
		Side side4 = Side.BUY;
		Quantity quantity4 = new Quantity( 200 );
		Price price4 = new Price( 1250000 );
		SweepingOrder sweepingOrder4 = new SweepingOrder(
				clientId4,
				clientOrderId4, // Same client, different order
				marketId4,
				side4,
				quantity4,
				price4
		);
		exchange.sweep( sweepingOrder4 );
		
		// There should now be four orders in the bid book, one
		// for 500 shares at $128, one for 300 shares at $127,
		// another one for 300 shares at $127, and an order for
		// 200 shares at $125. 
		
		// There should be three price levels in the bid book
		assertTrue(
			exchange
				.getMarket( marketId0 )
				.getBidBook()
				.getPriceLevels()
				.size()
			== 3
		);
		// There should be no price levels in the offer book
		assertTrue(
			exchange
				.getMarket( marketId0 )
				.getOfferBook()
				.getPriceLevels()
				.size()
			== 0
		);

		// We will now sweep with a sell order for 1500 shares 
		// at $126. It should generate 3 fills for the clients
		// and 3 for the counterparties. The 4th order in the 
		// book is below the sweeping order's sell price of 
		// $126. The sweeping order will then become a resting 
		// order to sell 400 shares at $126.
		
		ClientId clientId5 = new ClientId( "Eric");
		ClientOrderId clientOrderId5 = new ClientOrderId( "BqUCC5" );
		MarketId marketId5 = new MarketId( "IBM" );
		Side side5 = Side.SELL;
		Quantity quantity5 = new Quantity( 1500 );
		Price price5 = new Price( 1260000 );
		SweepingOrder sweepingOrder5 = new SweepingOrder(
			clientId5,
			clientOrderId5,
			marketId5,
			side5,
			quantity5,
			price5
		);
		exchange.sweep( sweepingOrder5 );
		
		// There should now be two orders in the book, an
		// order to sell 400 shares at $126 in the offer book,
		// and an order to buy 200 shares at $125 in the bid
		// book. Confirm that there is one order on each side.
		assertTrue(
			exchange
				.getMarket( new MarketId( "IBM" ) )
				.getOfferBook()
				.getPriceLevels()
				.get( new Price( 1260000L ) )
				.getOrders()
				.size()
			== 1
		);
		assertTrue(
			exchange
				.getMarket( new MarketId( "IBM" ) )
				.getBidBook()
				.getPriceLevels()
				.get( new Price( 1250000L ) )
				.getOrders()
				.size()
			== 1
		);
		
		// Confirm that we got 6 fills - 3 fills for the clients
		// and 3 for the counterparties
		LinkedList<Fill> fills = exchange.getComms().getFills();
		assertTrue( fills.size() == 6 );
		
		// Make sure old orders were unregistered
		assertTrue( exchange.getOrder( clientOrderId0 ) == null );
		assertTrue( exchange.getOrder( clientOrderId1 ) == null );
		assertTrue( exchange.getOrder( clientOrderId2 ) == null );
		assertTrue( exchange.getOrder( clientOrderId3 ) == null );
		
		// Make sure new orders are still registered
		assertTrue( exchange.getOrder( clientOrderId4 ) != null );
		assertTrue( exchange.getOrder( clientOrderId5 ) != null );
		
		// Confirm the quantity of our resting orders
		RestingOrder ro4 = exchange.getOrder( clientOrderId4 );
		assertTrue( ro4.getQuantity().equals( new Quantity( 200 ) ) );
		RestingOrder ro5 = exchange.getOrder( clientOrderId5 );
		assertTrue( ro5.getQuantity().equals( new Quantity( 400 ) ) );
		
		// We will now test canceling an order
		exchange.cancel( new Cancel( clientId4, clientOrderId4 ) );
		assertTrue( exchange.getOrder( clientOrderId4 ) == null );
		assertTrue( 
			exchange
				.getComms()
				.getCancelationConfirmations()
				.getLast()
				.getClientId()
				.equals( clientId4 ) 
		);
		
		// We will now test canceling the above order again.
		// We should see a cancel rejected message.
		exchange.cancel( new Cancel( clientId4, clientOrderId4 ) );
		assertTrue( exchange.getComms().getCancelRejections().getLast().equals( new CancelRejected(clientId4, clientOrderId4) ) );
		
		// We will now test sweeping against the cancelled order.
		// There should be no matches. 
		assertTrue( exchange.getMarket( marketId5 ).getBidBook().getPriceLevels().size() == 1 );
		SweepingOrder sweepingOrder6 = new SweepingOrder(
			new ClientId( "Tom" ),
			new ClientOrderId( "12345" ),
			new MarketId( "IBM" ),
			Side.SELL,
			new Quantity( 100 ),
			new Price( 1200000 )
		);
		exchange.sweep( sweepingOrder6 );
		// Make sure there was no fill
		assertFalse( exchange.getComms().getFills().getLast().getClientOrderId().equals( new ClientOrderId( "12345" ) ) );
		// Make sure sweeping against the bid book wound up turning into
		// a resting order in the offer book
		assertTrue( exchange.getOrder( new ClientOrderId( "12345" ) ) != null );
		// At this point, the cancelled order should no longer be
		// in the book, and there should be no additional orders
		// at the same price level. In my implementation, the
		// price level no longer exists in the book.
		// Make sure the cancelled order is no longer in the book, price level is empty
		assertTrue(exchange.getMarket( marketId5 ).getBidBook().getPriceLevels().size() == 0 );

	}
	
}
