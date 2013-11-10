package edu.hm.iny.netzwerke.blatt4.deEncoding;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 */

/**
 * @author Stäff
 */
public class URLEnDecoder {

	private final String inputURL;
	private String encodedURL;
	private String outputURL;

	URLEnDecoder(final String url) {
		inputURL = url;
	}

	/**
	 * @return the inputURL
	 */
	String getInputURL() {
		return inputURL;
	}

	/**
	 * @return the encodedURL
	 */
	String getEncodedURL() {
		return encodedURL;
	}

	/**
	 * @return the outputURL
	 */
	String getOutputURL() {
		return outputURL;
	}

	String encodeURL() throws UnsupportedEncodingException {

		encodedURL = URLEncoder.encode(inputURL, "UTF-8");
		return encodedURL;
	}

	String decodeURL() throws UnsupportedEncodingException {

		outputURL = URLDecoder.decode(encodedURL, "UTF-8");
		return outputURL;
	}
}
