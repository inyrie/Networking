/**
 *
 */
package edu.hm.iny.netzwerke.blatt1;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Stäff
 * @version 2013-10-15
 */
public class NetworkFileWriter {

	public static void main(final String... args) throws IOException {

		final String filename = "outputFile.txt";
		final String byeMsg = "kthxbye";
		final String startMsg = "Hi there!";

		try (final InputStream input = System.in; final PrintStream output = System.out/**
		 * ; FileWriter writer = new
		 * FileWriter(filename, true)
		 */
		) {

			output.println(startMsg);

			int code = input.read();

			while (code >= 0) {
				output.write(code);
				code = input.read();
				output.flush();
			}

			System.out.println(byeMsg);
			output.flush();
		}
	}
}
