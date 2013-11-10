/**
 * Munich University for Applied Science,
 * Faculty 07 for Mathematics and Computer Science
 * Netzwerke I, WS2013/14, Praktikumsgruppe IF2B[2]
 * Windows 7 Professional SP1; Java-Version: 1.7.0_21
 */

package edu.hm.iny.netzwerke.blatt1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Programm zum Einlesen von STDIN und Ausgabe des Inputs über STDOUT.
 * @author Stephanie Ehrenberg
 * @author Diana Irmscher
 * @version 2013-11-05
 */
public class NetworkFileWriter {

	public static void main(final String... args) throws IOException {

		// Konstante fuer den Pfad unter Linux (!!!), unter dem die Datei fuer den Output angelegt wird.
		final String FILE_PATH = "/home/network/Dokumente/aufgabe1.txt";

		final File file = new File(FILE_PATH);

		if (!file.exists()) {

			try {
				file.createNewFile();
			}

			catch (final IOException ioEx) {
				ioEx.printStackTrace();
			}
		}

		// ARM - Definition der Streamressourcen.
		try (final FileOutputStream output = new FileOutputStream(file); final InputStream input = System.in;) {

			for (int code = input.read(); code >= 0; code = input.read()) {

				output.write(code);
				output.flush();
			}

			// Sicher ist sicher!
			output.flush();
		}
	}
}
