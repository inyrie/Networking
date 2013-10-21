/**
 *
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Stäff
 */
class HTTPResponseManipulator {

	private final String IMAGE_HOST = "http://fi.cs.hm.edu";
	private final String IMAGE_RESSOURCE = "/fi/hm-logo.png";
	private final String IMAGE_PATH = IMAGE_HOST + IMAGE_RESSOURCE;

	private final List<String> httpResponse;
	private final List<String> manipulatedResponse = new ArrayList<String>();

	/**
	 * Ctor.
	 * @param response HTTP response sent by target host. Is to be manipulated.
	 */
	HTTPResponseManipulator(final List<String> response) {

		httpResponse = response;
		analyzeResponse();
	}

	/**
	 * @return
	 */
	List<String> getManipulatedResponse() {
		return manipulatedResponse;
	}

	/**
	 * 
	 */
	final private void analyzeResponse() {

		final Iterator<String> responseIterator = httpResponse.iterator();

		// System.out.println("httpResponse size is " + httpResponse.size());

		if (httpResponse.size() > 0) {

			for (String line = responseIterator.next(); responseIterator.hasNext(); line = responseIterator.next()) {

				if (line.contains("Accept: image/")) {
					manipulateResponse();
				}

				else if (line.contains("<img src=")) {
					manipulateImageURL(line);
				}

				else {
					manipulatedResponse.add(line);
				}
			}
		}
	}

	/**
	 * @param line
	 */
	private void manipulateImageURL(final String line) {

		String newLine = "";

		final String[] substrings = line.split(" ");

		for (int index = 0; index < substrings.length; index++) {

			if (substrings[index].startsWith("src")) {
				substrings[index] = "src=\"" + IMAGE_PATH + "\"";
			}
		}

		for (final String substring : substrings) {
			newLine += substring + " ";
		}

		manipulatedResponse.add(newLine);
	}

	/**
	 * 
	 */
	private void manipulateResponse() {

		final Iterator<String> responseIterator = httpResponse.iterator();

		for (String line = responseIterator.next(); responseIterator.hasNext(); line = responseIterator.next()) {
			manipulateImage(line);
		}

	}

	/**
	 * @param readLine
	 */
	private void manipulateImage(final String readLine) {

		if (readLine.startsWith("GET")) {

			manipulatedResponse.add("GET " + IMAGE_RESSOURCE + "HTTP/1.1/r/n");
			manipulatedResponse.add(IMAGE_HOST);
		}

		manipulatedResponse.add(readLine);
	}
}
