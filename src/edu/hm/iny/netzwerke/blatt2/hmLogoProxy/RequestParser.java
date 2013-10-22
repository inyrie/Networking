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
	private int targetPort;

	/**
	 * @param request
	 */
	RequestParser(final List<String> request) {

		httpRequest = request;
		parseHost();
		parsePort();
	}

	/**
	 * 
	 */
	String getTargetHost() {
		return targetHost;
	}

	int getTargetPort() {
		return targetPort;
	}

	/**
	 * @param host
	 */
	private void setTargetHost(final String host) {
		targetHost = host;
	}

	/**
	 * @param port
	 */
	private void setTargetPort(final int port) {
		targetPort = port;
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

	final private void parsePort() {

		int targetPort = 80;
		final String hostURL = httpRequest.get(1).split(" ")[1];

		try {
			targetPort = Integer.parseInt(hostURL.split(":")[1]);
		}

		catch (final ArrayIndexOutOfBoundsException iobEx) {
			// targetPort wird nicht neu belegt, sondern bleibt bei Standardport 80.
		}

		setTargetPort(targetPort);

		// Ausgabe des gesetzten Ports zur Kontrolle
		System.err.printf("Target Port is seet to %d", targetPort);

	}
}
