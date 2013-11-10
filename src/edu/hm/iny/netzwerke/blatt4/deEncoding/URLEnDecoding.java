package edu.hm.iny.netzwerke.blatt4.deEncoding;
import java.io.UnsupportedEncodingException;

/**
 *
 */

/**
 * @author St�ff
 */
public class URLEnDecoding {

	public static void main(final String... ignored) throws UnsupportedEncodingException {

		final String testURL = "http://www.bla.de/h�ll�.php?test=bl�bb&fu=bar";

		final URLEnDecoder enDecoder = new URLEnDecoder(testURL);
		System.out.println("URl-codierte URL: " + enDecoder.encodeURL());
		System.out.println("decodierte URL: " + enDecoder.decodeURL());
	}
}
