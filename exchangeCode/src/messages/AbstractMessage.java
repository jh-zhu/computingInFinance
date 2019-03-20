package messages;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

public abstract class AbstractMessage {
	
	private ClientOrderId _clientOrderId;
	private ClientId _clientId;
	
	protected AbstractMessage( ClientId clientId, ClientOrderId clientOrderId ) {
		_clientId = clientId;
		_clientOrderId = clientOrderId;
	}
	
	public abstract String getDescription();
	
	public ClientId getClientId() { return _clientId; }
	public ClientOrderId getClientOrderId() { return _clientOrderId; }
	
}
