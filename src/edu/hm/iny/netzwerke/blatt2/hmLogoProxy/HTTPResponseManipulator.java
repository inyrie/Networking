/**
 * Munich University for Applied Science,
 * Faculty 07 for Mathematics and Computer Science
 * Netzwerke I, WS2013/14, Praktikumsgruppe IF2B[2]
 * Windows 7 Professional SP1; Java-Version: 1.7.0_21
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Klasse, die die Manipulation von Bild-URL in IMG-Tags uebernimmt (wichtig: funktioniert nicht fuer Bilder, die durch
 * Style-Information als Hintergrund eines Elements definiert wurden!), so dass auf einer gewuenschten Seite am Ende
 * alle Bilder durch HM-Logo-Bilder ausgetauscht sind.
 * @author Stephanie Ehrenberg
 * @author Diana Irmscher
 * @version 2013-11-04
 */
class HTTPResponseManipulator {

	private final String IMAGE_HOST = "http://fi.cs.hm.edu";
	private final String IMAGE_RESSOURCE = "/fi/hm-logo.png";
	private final String IMAGE_PATH = IMAGE_HOST + IMAGE_RESSOURCE;

	private final List<String> httpResponseBody;
	private final List<String> manipulatedResponse = new ArrayList<String>();

	/**
	 * Ctor.
	 * @param responseBody HTTP Response von einem Target Host gesendet. Soll abgeaendert werden.
	 */
	HTTPResponseManipulator(final List<String> responseHeader, final List<String> responseBody) {

		httpResponseBody = responseBody;
		// Einfuegen des vom Server geschickten HTTP Headers.
		insertOriginalHeader(responseHeader);
		// Analysieren und ggf. Manipulieren des vom Server geschickten HTTP Bodies.
		analyzeResponse();
	}

	/**
	 * Getter.
	 * @return Die abgeaenderte HTTP Antwort.
	 */
	List<String> getTamperedResponse() {
		return manipulatedResponse;
	}

	/**
	 * Methode zum Einfuegen des urspruenglichen HTTP Response Headers in die manipulierte HTTP Response, damit diese
	 * komplett an den Client ausgeliefert werden kann.
	 * @param responseHeader Der vom Server gesendete Header der HTTP Response.
	 */
	final private void insertOriginalHeader(final List<String> responseHeader) {

		final Iterator<String> headerCursor = responseHeader.iterator();

		for (String line = headerCursor.next(); headerCursor.hasNext(); line = headerCursor.next()) {
			manipulatedResponse.add(line);
		}

		manipulatedResponse.add(System.lineSeparator());
	}

	/**
	 * Methode, die die HTTP Response nach Image-Tags durchsucht und dann die Manipulation der Bilder anstoesst.
	 */
	final private void analyzeResponse() {

		final Iterator<String> responseIterator = httpResponseBody.iterator();

		if (httpResponseBody.size() > 0) {

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
	 * Methode zum Austauschen der Image-URL im Quelltext der Seite.
	 * @param line Eine gelesene Zeile des HTTP Response Bodies.
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
