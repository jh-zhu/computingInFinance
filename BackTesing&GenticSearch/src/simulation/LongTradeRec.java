package simulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LongTradeRec {
	
	public static long EOF_MARKER = ( Long.MAX_VALUE / 2 ) - 1;
	
	public long  _timestamp;
	public int   _id;
	public int   _size;
	public float _price;

	public LongTradeRec(
		long timestamp,
		int id,
		int size,
		float price
	) {
		_timestamp = timestamp;
		_id = id;
		_size = size;
		_price = price;
	}
	
	public long getTimestamp() { return _timestamp; }
	public int getId()         { return _id;        }
	public int getSize()       { return _size;      }
	public float getPrice()    { return _price;     }
	
	public static LongTradeRec newRec( DataInputStream dataInputStream ) throws IOException {
		long timestamp = dataInputStream.readLong();
		int id = dataInputStream.readUnsignedShort();
		if( id == 0 ) // This is an EOF marker
			return null;
		int size = dataInputStream.readInt();
		float price = dataInputStream.readFloat();
		return new LongTradeRec( timestamp, id, size, price );
	}
	
	/**
	 * Create an end-of-file marker
	 */
	public LongTradeRec() {
		_id = 0;
		_price = 0F;
		_size = 0;
		_timestamp = 0L;
	}
	
	public static void writeEOFMarker( DataOutputStream dataOutputStream ) throws IOException {
		LongTradeRec ltr = new LongTradeRec();
		ltr.writeRec( dataOutputStream );
	}
	
	public void writeRec( DataOutputStream dataOutputStream ) throws IOException {
		dataOutputStream.writeLong( _timestamp );
		dataOutputStream.writeShort( _id );
		dataOutputStream.writeInt( _size );
		dataOutputStream.writeFloat( _price );		
	}
	
	public static void writeRecords( DataOutputStream dataOutputStream, List<LongTradeRec> records ) throws IOException {
		Iterator<LongTradeRec> iterator = records.iterator();
		while( iterator.hasNext() )
			( iterator.next() ).writeRec( dataOutputStream );
	}
	
}
