package dbReaderFramework;

/**
 * This class is used to tell the db manager which sequence number is next.
 * This is the sequence number that each of the db readers is asked to read
 * up to.
 * 
 * @author Lee
 *
 */
public interface I_DBClock {
	
	/** Return a sequence number that will be used to determine how
	 * much data each db reader will next read
	 * 
	 * @return Sequence number (which usually represents some kind of time stamp)
	 */
	public long getNextSequenceNumber();
	
	/** Return true if this clock is finished - If there are no more sequence
	 * numbers to report
	 * @return True if finished, or false otherwise
	 */
	public boolean isFinished();
}
