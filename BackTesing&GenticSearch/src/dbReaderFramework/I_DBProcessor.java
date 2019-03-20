package dbReaderFramework;

import java.io.IOException;
import java.util.LinkedList;


/**
 * Interface used by DBManager to process readers that have just read data
 * for a given sequence number. Readers with new data are passed to a processor
 * via its {@link I_DBProcessor#processReaders(long, int, LinkedList) process readers method}.
 * 
 * @author Lee
 *
 */
public interface I_DBProcessor {

	/**
	 * Method used by DBManager to pass readers with data to a processor.
	 * 
	 * @param sequenceNumber         Sequence number that DBManager just asked all readers to read
	 * @param numReadersWithNewData  Number of readers in the readers list that have new data. Note that it may be zero.
	 * @param readers                List of readers, the first n of which have new data, where n = numReadersWithNewData
	 * 
	 * @return True if I_DBProcessor is not finished and false if it is.
	 */
	public boolean processReaders( 
		long sequenceNumber,				// Sequence number of the data that was just read
		int numReadersWithNewData, 			// Number of readers with new data
		LinkedList<I_DBReader> readers 		// List of readers - the first n of which have new data - where n = numReadersWithNewData
	);
	
	/**
	 * Method used by external objects to stop this processor, which
	 * might close files and set an isFinished flag, if appropriate.
	 * @throws IOException 
	 */
	public void stop() throws Exception;
	
}
