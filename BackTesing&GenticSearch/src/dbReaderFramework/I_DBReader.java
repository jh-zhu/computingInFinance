package dbReaderFramework;

/**
 * Interface used by DBManager to manage readers of data. The DBManager calls
 * {@link I_DBReader#readChunk(long) a method } to read a chunk of data -
 *  a number of records that all have the same sequence number.
 * 
 * @author Lee
 *
 */
public interface I_DBReader {

	/**
	 * Method to read all records with sequence numbers less than or equal to the
	 * specified sequence number
	 * 
	 * @param sequenceNum Sequence number specified for the above task
	 * @return Number of records that were read
	 */
	public int readChunk( long sequenceNum );

	/**
	 * Get sequence number of next record. Internally, this reader is looking at some
	 * record of a larger data set. That record has a sequence number, as might some
	 * number of records that follow it. This sequence number represents a new chunk
	 *  - a collection of records that has not yet been processed.
	 * 
	 * @return Sequence number of next record
	 */
	public long getSequenceNumber();
	
	/**
	 * Method used to tell the reader that it should close all data sources and otherwise
	 * prepare for an end to the reading process.
	 */
	public void stop();
	
	/**
	 * Method used to determine whether this reader has any more data to read.
	 * @return True if this reader has no more data to read. Return false otherwise.
	 */
	public boolean isFinished();
	
	/**
	 * Method used to obtain the last sequence number that was successfully read by this method.
	 * @return Last sequence number successfully read
	 */
	public long getLastSequenceNumberRead();
	
}
