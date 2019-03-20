package exchangeStructures;
import java.util.LinkedList;

import fills.Fill;
import messages.CancelRejected;
import messages.Cancelled;
import messages.RestingOrderConfirmation;

/**
 * This class simulates a communications link between the
 * clients and the exchange. Messages that it can send
 * to the client include Fill, Cancelled, CancelRejected, 
 * and RestingOrderConfirmation. Nothing as actually sent 
 * to the client. All messages are just appended to lists 
 * that are available for verification of functionality
 * for testing purposes.
 *
 */
public class Comms {
	
	private LinkedList<Fill>                     _fills;
	private LinkedList<RestingOrderConfirmation> _restingOrders;
	private LinkedList<CancelRejected>           _rejectedCancels;
	private LinkedList<Cancelled>                _canceledMsgs;
	
	public Comms() {
		_fills = new LinkedList<Fill>();
		_restingOrders   = new LinkedList<RestingOrderConfirmation>();
		_rejectedCancels = new LinkedList<CancelRejected>();
		_canceledMsgs    = new LinkedList<Cancelled>();
	}

	/** Simulated telling client that client's resting or 
	 * sweeping order has been filled
	 * @param fill The fill message stating the above
	 */
	public void sendFill( Fill fill ) {
		_fills.addLast( fill );
	}
	
	/**
	 * Simulates telling client that client's sweeping order has 
	 * become a resting order
	 * 
	 * @param restingOrderConfirmation Message stating the above
	 */
	public void sendRestingOrderConfirmation( RestingOrderConfirmation restingOrderConfirmation ) {
		_restingOrders.addLast( restingOrderConfirmation );
	}
	
	/**
	 * Simulates sending a message to the client telling the 
	 * client that the client's cancel message was rejected
	 * @param rejectMsg Cancel reject message
	 */
	public void sendCancelRejected( CancelRejected rejectMsg ) {
		_rejectedCancels.addLast( rejectMsg );
	}

	/**
	 * Simulates sending a cancelled message to the client. The
	 * cancelled message tells the client that the order
	 * the client wanted to cancel was cancelled.
	 * @param cancelled Message that tells client that the client's
	 *                  order was cancelled
	 */
	public void cancelled(Cancelled cancelled) { _canceledMsgs.addLast( cancelled ); }
	
	// Getters
	public LinkedList<Fill>                       getFills()                     { return _fills; }
	public LinkedList<CancelRejected>             getCancelRejections()          { return _rejectedCancels; }
	public LinkedList<Cancelled>                  getCancelationConfirmations()  { return _canceledMsgs; }
	public LinkedList<RestingOrderConfirmation>   getRestingOrderConfirmations() { return _restingOrders; }

}
