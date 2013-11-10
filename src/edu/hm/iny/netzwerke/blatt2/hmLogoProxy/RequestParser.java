/**
 * Munich University for Applied Science,
 * Faculty 07 for Mathematics and Computer Science
 * Netzwerke I, WS2013/14, Praktikumsgruppe IF2B[2]
 * Windows 7 Professional SP1; Java-Version: 1.7.0_21
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.util.List;

/**
 * Klasse zum Auswerten eines HTTP Requests, um die relative Adresse und den target port bestimmen zu koennen.
 * @author Stephanie Ehrenberg
 * @author Diana Irmscher
 * @version 2013-11-04
 */
class RequestParser {

	// Konstante fuer den Default fuer die relative Adresse. Greift dann auf die index-Seite zu.
	static final String STD_RELATIVE_ADDRESS = "/";

	private final List<String> httpRequest;
	private String relativeAddress;
	private int targetPort;

	/**
	 * Ctor.
	 * @param request
	 */
	RequestParser(final List<String> request) {

		httpRequest = request;
		parsePort();
		parseRelativeAddress();
	}

	/**
	 * Getter.
	 * @return Die relative Adresse.
	 */
	String getRelativeAddress() {
		return relativeAddress;
	}

	/**
	 * Getter.
	 * @return Den target port.
	 */
	int getTargetPort() {
		return targetPort;
	}

	/**
	 * Setter.
	 * @param port Bla.
	 */
	private void setTargetPort(final int port) {
		targetPort = port;
	}

	/**
	 * Setter.
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
	 * Parst den gewuenschten target port aus dem abgefangenen HTTP Request vom Client.
	 */
	final private void parsePort() {

		int targetPort = 80;
		try {
			final String hostURL = httpRequest.get(1).split(" ")[1];
			final String hostPort = hostURL.split(":")[1];

			if (!hostPort.isEmpty()) {
				targetPort = Integer.parseInt(hostPort);
			}
		} catch (final Exception ex) {
			// falls irgendwas schief laeuft beim parsen des Ports -> nichts machen,
			// es wird dann der Standardport 80 verwendet.
		}

		setTargetPort(targetPort);

		// Ausgabe des gesetzten Ports zur Kontrolle.
		System.err.printf("*** Target Port is set to %d" + System.lineSeparator(), targetPort);
	}

	/**
	 * Parst die gewuenschte relative Adresse aus dem abgefangenen HTTP Request vom Client.
	 */
	final private void parseRelativeAddress() {

		try {
			relativeAddress = httpRequest.get(0).split(" ")[1];
		} catch (final Exception ex) {
			// falls beim Parsen etwas schief geht, liefere die Indexseite aus!
			relativeAddress = STD_RELATIVE_ADDRESS;
		}

		System.err.printf("*** Request for Ressource %s sent to Host...", relativeAddress);
	}
}
