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
	List<String> getTamperedResponse() {
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

				if (line.contains("<img src=")) {
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
}
