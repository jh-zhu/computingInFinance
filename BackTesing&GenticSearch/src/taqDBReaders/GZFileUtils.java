package taqDBReaders;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This is a utility class that has static methods for obtaining a data 
 * input stream for reading and writing to a gzip file. When writing
 * to a gzip file, remember to call the flush method on DataOutputStream
 * before calling its close method. Otherwise, not everything you've
 * written using this class may wind up in the file.
 * 
 * @author Lee
 */
public class GZFileUtils {

	/**
	 * This is how we get a data input stream from a gzipped file.
	 * 
	 * @param filePathName Full path name of the file
	 * @return A data input stream for the above file. This stream can be asked to read basic data types
	 *         such as int and float
	 * @throws IOException
	 */
	public static DataInputStream getGZippedFileInputStream( String filePathName ) throws IOException {
		InputStream in = new GZIPInputStream( new FileInputStream( filePathName ) );
		BufferedInputStream bis = new BufferedInputStream( in );
		DataInputStream dataInputStream = new DataInputStream( bis );
		return dataInputStream;
	}
	
	/**
	 * This is how we get a data output stream to write a new gzipped file.
	 * 
	 * @param filePathName Full path name of the file to write
	 * @return Data output stream for the above file. It can be used to write basic data types
	 *         such as int and float
	 * @throws IOException
	 */
	public static DataOutputStream getGZippedFileOutputStream( String filePathName ) throws IOException {
		FileOutputStream fos = new FileOutputStream( filePathName );
		OutputStream out = new GZIPOutputStream( fos );
		BufferedOutputStream bos = new BufferedOutputStream( out );
		DataOutputStream dataOutputStream = new DataOutputStream( bos );
		return dataOutputStream;
	}
}
