package simulation;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;

import dbReaderFramework.I_DBReader;
import taqDBReaders.GZFileUtils;

/**
 * This class implements an I_DBReader interface for use in the DBManager class.
 * 
 * <P>
 * This class allows a calling object to step through that data one chunk at
 * a time. A chunk is defined as a piece of data in which all of the records have
 * the same sequence number.
 * </P>
 * 
 * <P>
 * After a chunk is read, this class allows a calling object to retrieve the
 * number of records read for that chunk, and the fields in each record.
 * </P>
 * 
 * @author Lee
 *
 */
public class MergeFormatTradesDBReader implements I_DBReader {

	protected boolean _isFinished;
	protected long    _lastSequenceNumberRead;
	
	protected DataInputStream _inputStream;
	protected LinkedList<LongTradeRec> _records;
	protected LongTradeRec _record; // First record of chunk
	
	@Override
	public boolean isFinished() { return _isFinished; }
	
	public MergeFormatTradesDBReader( String filePathName ) throws IOException {
		_inputStream = GZFileUtils.getGZippedFileInputStream( filePathName );
		_records = new LinkedList<LongTradeRec>();
		_record = LongTradeRec.newRec( _inputStream );
		if( _record == null ) { // Encountered an end of file marker
			_isFinished = true;
			_inputStream.close();
			return;
		} 
		_isFinished = false;
	}
	
	@Override
	public int readChunk( long targetSequenceNum ) {
		
		if( _isFinished )
			return 0;
		
		_records.clear();
		
		while( true ) {
			// We already read one record of the new chunk
			// so add it to the list of records and
			// increment the record counter
			_records.addLast( _record );
			
			// Try to read another record
			try {
				_record = LongTradeRec.newRec( _inputStream );
			} catch (IOException e) {
				// Something wrong with the source.
				// Unfortunately, there is nothing we can do
				// about it.
				e.printStackTrace();
			}
			if( _record == null ) { // Reached end of file
				_isFinished = true;
				try {
					_inputStream.close();
				} catch (IOException e) {
					// If we can't close the data source,
					// there is nothing we can do about
					// it.
					e.printStackTrace();
				}
				break;
			}
			if( _record.getTimestamp() > targetSequenceNum )
				break;
		}
		
		// Save the sequence number that we just tried
		// to read up to. It might not actually be the 
		// sequence number that we were able to read up
		// to because this data source had a sequence
		// number that was higher and lower but not
		// exactly this one.
			
			_lastSequenceNumberRead = targetSequenceNum;
			
		// Save number of records read and return it
			
			return _records.size();
			
	}
	
	/**
	 * @return The internal store of records for the last sequence
	 *         number that was successfully read.
	 */
	public LinkedList<LongTradeRec> getRecords() { return _records; }

	/**
	 * Return the sequence number of the record that this reader is currently
	 * pointing to and will step over when it is asked to read all of the 
	 * records it has for this sequence number.
	 * 
	 * return Current sequence number of this reader
	 */
	@Override
	public long getSequenceNumber() {
		return _record.getTimestamp();
	}

	/**
	 * Stop reading and close all files.
	 */
	@Override
	public void stop() {
		if( ! _isFinished ) {
			_isFinished = true;
			try {
				_inputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); // Nothing we can do here
			}
		}
	}

	/**
	 * Return the last sequence number for which records were successfully
	 * read by this reader.
	 */
	@Override
	public long getLastSequenceNumberRead() {
		return _lastSequenceNumberRead;
	}

}
