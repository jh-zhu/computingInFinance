package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TickerFileReader {
	
	protected ArrayList<String> _tickerIds;
	
	public TickerFileReader( String filePathName ) throws IOException {
		 File file = new File( filePathName );
		 FileReader fileReader = new FileReader( file );
		 BufferedReader br = new BufferedReader( fileReader );
		 _tickerIds = new ArrayList<String>();
		 _tickerIds.add( "123456" );
		 String line;
		 while( ( line = br.readLine() ) != null ) {
			 String[] fields = line.split( " " );
			 _tickerIds.add( fields[ fields.length - 1 ] );
		 }
		 br.close();
	}
	
	public String getTicker( short id ) {
		return _tickerIds.get( id );
	}

}
