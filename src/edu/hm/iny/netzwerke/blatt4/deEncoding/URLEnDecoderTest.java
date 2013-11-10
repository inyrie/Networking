package edu.hm.iny.netzwerke.blatt4.deEncoding;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

/**
 *
 */

/**
 * @author Stäff
 */
public class URLEnDecoderTest {

	private final String testURL;
	final URLEnDecoder enDecoder;

	public URLEnDecoderTest() {
		super();
		testURL = "http://www.bla.de/hällö.php";
		enDecoder = new URLEnDecoder(testURL);
	}

	@Test public void encodingTest() throws UnsupportedEncodingException {
		enDecoder.encodeURL();
		enDecoder.decodeURL();
		assertEquals(enDecoder.getInputURL(), enDecoder.getOutputURL());
	}
}
