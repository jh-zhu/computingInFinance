package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import dbReaderFramework.I_DBReader;
public class SignalFileReader implements I_DBReader {
	
	public class Signal {
		protected int _id;
		protected long _timestamp;
		protected int _signal;
		
		private Signal() {}
		private Signal( long timestamp, int id, int signal ) {
			_timestamp = timestamp;
			_id = id;
			_signal = signal;
		}
		
		public long getTimestamp() { return _timestamp; }
		public int getId() { return _id; }
		public int getSignal() { return _signal; }
		
	}

	// This is the big signals file, all signals. It's
	// small enough to suck entirely into memory, so
	// that's what we will do. We still have to pretend
	// we're reading it one record at a time.
	protected LinkedList<Signal> _signals; 
	
	// Set to true when we run out of records to return. 
	protected boolean _isFinished;
	
	// These are the records that were "read" in the last
	// call to readChunk.
	protected LinkedList<Signal> _recordsJustRead;
	
	// This is the record that is used to determine what
	// is the next sequence number, the one we still
	// haven't read.
	protected Signal _nextSignal;
	
	// This is the sequence number that was read in the
	// last readChunk.
	protected long _lastSequenceNumberRead;
	
	public SignalFileReader( String signalFilePathName ) throws IOException {
		_isFinished = false;
		_signals = new LinkedList<Signal>();
		BufferedReader br = new BufferedReader( new FileReader( new File( signalFilePathName ) ) );
		String line = null;
		while( ( line = br.readLine() ) != null ) {
			String[] fields = line.split( "," );
			_signals.add(
				new Signal(
					Long.parseLong( fields[ 0 ] ),    // timestamp
					Integer.parseInt( fields[ 1 ] ),  // id
					Integer.parseInt( fields[ 2 ] )   // signal -1 or 1
				)
			);
		}
		br.close();
		_nextSignal = _signals.removeFirst();
		_recordsJustRead = new LinkedList<Signal>();
		_lastSequenceNumberRead = 0L; // We haven't read any records.
	}

	@Override
	public int readChunk( long sequenceNum ) {
		_recordsJustRead.clear();
		if( _isFinished )
			return 0;
		_recordsJustRead.push( _nextSignal );
		_lastSequenceNumberRead = _nextSignal.getTimestamp();
		while( _signals.size() > 0 ) {
			_nextSignal = _signals.removeFirst();
			if( _nextSignal.getTimestamp() > sequenceNum )
				return _recordsJustRead.size();
			_recordsJustRead.addLast( _nextSignal );
		}
		if( _signals.size() == 0 )
			_isFinished = true;
		return _recordsJustRead.size();
	}

	@Override
	public long getSequenceNumber() {
		return _nextSignal.getTimestamp();
	}

	@Override
	public void stop() {
		_isFinished = true;
	}

	@Override
	public boolean isFinished() {
		return _isFinished;
	}

	@Override
	public long getLastSequenceNumberRead() {
		return _lastSequenceNumberRead;
	}

	public LinkedList<Signal> getRecords() {
		return _recordsJustRead;
	}
	
}
