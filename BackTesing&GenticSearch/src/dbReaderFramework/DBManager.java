package dbReaderFramework;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * This class manages a list of I_DBReader objects and I_DBProcessor objects.
 * 
 * The process is heavily commented below. Briefly, an object of this class
 * obtains the lowest next sequence number from its list of I_DBReader objects.
 * It then asks all readers that have this next sequence number to read all of
 * the data for this sequence number - which may include multiple records with
 * the same sequence number for each reader. As the DBManager object iterates
 * over its I_DBReader objects, when it encounters the first sequence number
 * higher than the sequence number than the lowest sequence number previously
 * found, it stops and starts iterating over its list of I_DBProcessor objects.
 * As DBManager iterates over the list of I_DBProcessor objects, it hands off
 * the list of readers to each processor for processing. (In a simulation
 * framework, each simulation would be a processor and there would be many
 * processors in the DBManager's list of I_DBProcessor objects.)
 *   
 * @author Lee
 *
 */
public class DBManager {

	/**
	 * List of object that will perform the actual reading of data sources
	 */
	LinkedList<I_DBReader> _readers;
	
	/**
	 * List of objects that will process the above readers at certain intervals
	 */
	LinkedList<I_DBProcessor> _processors;
	
	/**
	 * Counter to keep track of how many readers have read data for a given sequence number
	 */
	protected int _numReadersWithNewData;
	
	/**
	 * An object that tells DBManager which target sequence number all db readers should
	 * read up to next. If we want every record to be read, we could set the next time stamp
	 * to be whichever time stamp is the lowest next time stamp across all db readers.
	 */
	protected I_DBClock _dbClock;
	
	/**
	 * The readers must be sorted in ascending order by the next sequence number so we can
	 * determine which sequence number is next for reading (if we want every sequence number
	 * to be read)
	 */
	protected Comparator<I_DBReader> _dbReaderComparator;
	
	/**
	 * Returns the number of processors remaining in list. When a processor's
	 * processReaders method returns false, the processor is removed from the
	 * list of processors, which is its way of telling the db manager that it
	 * is finished.
	 * 
	 * @return Number of active processors
	 */
	protected int getNumActiveProcessors() { return _processors.size(); } 
	
	/**
	 * Returns the number of readers remaining in list. After all readers have
	 * finished reading and all processors have had a chance to process all
	 * readers, the method getRidOfDeadReaders removes readers that are
	 * finished from the list of active readers.
	 * 
	 * @return Number of active readers
	 */
	protected int getNumActiveReaders() { return _readers.size(); }
	
	/**
	 * Constructs a DBManager object. Saves list of readers. Saves list of processors.
	 * Saves clock. Instantiates comparator for sorting readers by sequence number.
	 * 
	 * @param readers List of db readers
	 * @param processors List of db processors
	 * @param clock Clock used to determine next sequence number to read up to
	 */
	public DBManager( 
		LinkedList<I_DBReader>    readers,      // List of db readers, e.g. taq readers 
		LinkedList<I_DBProcessor> processors,   // List of db processors, e.g. simulation objects
		I_DBClock                 clock         // A db clock that defines in what increments we'll step through the data 
	) {
		
		// Save arguments
		
			_readers    = readers;
			_processors = processors;
			_dbClock    = clock;
		
		// Readers have to be sorted in order of their sequence numbers.
		// Create the comparator that we will use to do that:
		
			_dbReaderComparator = new Comparator<I_DBReader>() {
				@Override
				public int compare(I_DBReader o1, I_DBReader o2) {
					long sn1 = o1.getSequenceNumber();
					long sn2 = o2.getSequenceNumber();
					if( sn1 > sn2 )
						return 1;
					else
						if( sn2 > sn1 )
							return -1;
					return 0;
				}
			};
			
	}
	/**
	 * For a given sequenceNumber, tell all readers to read all records until the
	 * new sequence number in all readers is greater than the specified sequence
	 * number.
	 * 
	 * @param sequenceNumber The sequence number specifying how far to read.
	 */
	public void readChunk( long sequenceNumber ) {
		
		// Iterate through all readers that have a sequence number
		// less than or equal to the above. Ask them to read all
		// data until this is no longer true.
			
			Iterator<I_DBReader> dbReaderIterator = _readers.iterator();
			do {
				
				// Get a reader from our list of readers and see if it
				// has records that have sequence numbers that are less
				// than or equal to the target sequence number.
				
					I_DBReader dbReader = dbReaderIterator.next();
					long dbSequenceNum = dbReader.getSequenceNumber(); 
					if( dbSequenceNum > sequenceNumber )
						
						// We have encountered a reader with a sequence number
						// that is greater than the target sequence number for
						// this chunk. The readers are in sorted order so we
						// know the next reader will also have a sequence number
						// above the target sequence number. This means that
						// we are finished reading this chunk. Exit loop.
							
							break;
					
				// Tell this reader to read until it has no more data or
				// it encounters a sequence number greater than the target
				// sequence number.
					
					int numRecsRead = dbReader.readChunk( sequenceNumber );
				
				// If this reader found 1 or more records, increment the
				// count for the number of readers with new records so we
				// can later tell the processors how many readers they
				// should process.
					
					if( numRecsRead > 0 )
						_numReadersWithNewData++;
					
			} while( dbReaderIterator.hasNext() );
		
	}
	
	/**
	 * Iterate over all processors, asking each to process all of the records read
	 * by all of the readers in the last readChunk call.
	 * 
	 * @param sequenceNumber Sequence number that was just read by all readers
	 */
	public void processChunk( long sequenceNumber ) {
		
		Iterator<I_DBProcessor> processorIterator = _processors.iterator();
		while( processorIterator.hasNext() ) {
			I_DBProcessor processor = processorIterator.next();
			if( ! processor.processReaders( sequenceNumber, _numReadersWithNewData, _readers ) )
				processorIterator.remove();
		}
		
	}
	
	/**
	 * Drop inactive readers from readers list
	 * @param sequenceNumber Sequence number specifying the last read operation for all readers
	 */
	public void getRidOfDeadReaders() {
		Iterator<I_DBReader> readerIterator = _readers.iterator();
		while( readerIterator.hasNext() ) {
			I_DBReader reader = readerIterator.next();
			if( reader.isFinished() )
				readerIterator.remove();
		}
	}
	
	/**
	 * Called by launch to stop all readers
	 */
	private void stopAllReaders() {
		for( I_DBReader reader : _readers )
			reader.stop();
	}
	
	/**
	 * Called by launch to stop all processors
	 */
	private void stopAllProcessors() throws Exception {
		for( I_DBProcessor processor : _processors )
			processor.stop();
	}
	
	/** Main processing loop for DBManager.
	 * 
	 * 1) Loops while clock is not finished. Clock may be finished when there are no more readers,
	 * or no more processors, or a certain end time has been reached. See instantiation of clock.
	 * 
	 * 2) Inside the above loop, sort readers by lowest next sequence number.
	 * 
	 * 3) Obtain the next sequence number from the clock. This could be a pre-set interval, like
	 * 930, 932, 934, etc, or it could be the sequence number of the next reader after the above
	 * sort.
	 * 
	 * 4) Tell all readers to read until they run out of data or reach a sequence number greater
	 * than the above sequence number.
	 * 
	 * 5) Tell all processors to process all data read by the readers.
	 * @throws Exception 
	 */
	public void launch() throws Exception {

		while( ! _dbClock.isFinished() ) {
			
			// Reset counter of the number of readers that have records for
			// the current sequence number.
			
				_numReadersWithNewData = 0;

			// Line up the DBReaders so they are in sorted order

				Collections.sort( _readers, _dbReaderComparator );
			
			// Obtain the next sequence number from the I_DBClock
			// object. This tells us the target sequence number for
			// all readers. Readers will be asked to read until
			// they run out of data or until they encounter a
			// sequence number higher than this.
				
				long sequenceNumber = _dbClock.getNextSequenceNumber();
			
			// Read data
			
				readChunk( sequenceNumber );
				
			// Process data
				
				processChunk( sequenceNumber ); // A 'chunk' is a piece of data with the same sequence number
				
			// Get rid of dead readers. Dead processors are discarded automatically
			// in processChunk, but dead readers must be discarded after the call
			// to processChunk, so they are discarded in the following.
				
				getRidOfDeadReaders();
				
		}
		
		stopAllReaders();
		stopAllProcessors();
		
	} // End of 'launch' method

}
