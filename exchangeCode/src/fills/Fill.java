package fills;

import messages.AbstractMessage;
import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.Quantity;

public class Fill extends AbstractMessage {
	
	private ClientId _counterpartyId;
	private Quantity _quantity;
	
	public Fill( ClientId clientId, ClientId counterpartyId, ClientOrderId clientOrderId, Quantity fillQuantity ) {
		super( clientId, clientOrderId );
		_counterpartyId = counterpartyId;
		_quantity = fillQuantity;
	}
	
	@Override
	public String toString() { 
		return String.format( 
			"%s(%s,%s,%s,%s)", 
				Fill.class.getName(), 
				this.getClientId().toString(), 
				this.getCounterpartyId().toString(), 
				this.getClientOrderId().toString(), 
				this.getQuantity().toString() 
		);
	}
	
	public ClientId getCounterpartyId() { return _counterpartyId; }
	public Quantity getQuantity() { return _quantity; }
	
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		if( !( o instanceof Fill ) )
			return false;
		Fill fill = (Fill)o;
		return(
			( this.getClientId().equals( fill.getClientId() ) ) &&
			( this.getCounterpartyId().equals( fill.getCounterpartyId() ) ) &&
			( this.getClientOrderId().equals( fill.getClientOrderId() ) ) &&
			( this.getQuantity().equals( fill.getQuantity() ) )
		);
	}

	
	@Override
	public String getDescription() { return this.toString(); }
}
