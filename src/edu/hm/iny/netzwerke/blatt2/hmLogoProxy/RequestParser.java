/**
 *
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.util.Iterator;
import java.util.List;

/**
 * @author Stäff
 */
class RequestParser {

	private final List<String> httpRequest;
	private String targetHost;

	/**
	 * @param request
	 */
	RequestParser(final List<String> request) {

		httpRequest = request;
		parseHost();
	}

	/**
	 * 
	 */
	String getTargetHost() {
		return targetHost;
	}

	/**
	 * @param host
	 */
	private void setTargetHost(final String host) {
		targetHost = host;
	}

	/**
	 * 
	 */
	final private void parseHost() {

		final Iterator<String> cursor = httpRequest.iterator();
		String host = "";

		for (String line = cursor.next(); cursor.hasNext(); line = cursor.next()) {

			if (line.startsWith("Host: ")) {

				final String[] substrings = line.split(" ");
				host = substrings[1];

				// System.err.println("*** Host set to " + host);
			}
		}

		setTargetHost(host);
	}
}
