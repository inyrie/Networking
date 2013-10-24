/**
 *
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.util.List;

/**
 * @author Stäff
 */
class RequestParser {

	static final String STD_RELATIVE_ADDRESS = "/";

	private final List<String> httpRequest;
	private String relativeAddress;
	private int targetPort;

	/**
	 * @param request
	 */
	RequestParser(final List<String> request) {

		httpRequest = request;
		parsePort();
		parseRelativeAddress();
	}

	/**
	 * @return
	 */
	String getRelativeAddress() {
		return relativeAddress;
	}

	/**
	 * @return
	 */
	int getTargetPort() {
		return targetPort;
	}

	/**
	 * @param port
	 */
	private void setTargetPort(final int port) {
		targetPort = port;
	}

	/**
	 * @param relAdd
	 */
	void setRelativeAddress(final String relAdd) {

		if (relAdd.length() == 0) {
			relativeAddress = STD_RELATIVE_ADDRESS;
		}

		else {
			relativeAddress = relAdd;
		}
	}

	/**
	 * 
	 */
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
		System.err.printf("*** Target Port is set to %d" + System.lineSeparator(), targetPort);
	}

	/**
	 * 
	 */
	final private void parseRelativeAddress() {

		setRelativeAddress(httpRequest.get(0).split(" ")[1]);
		System.err.printf("*** Request for Ressource %s sent to Host...", relativeAddress);
	}
}
