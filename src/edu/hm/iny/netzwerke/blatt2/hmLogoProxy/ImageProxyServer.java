/**
 *
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Stäff
 */
public class ImageProxyServer {

	/** Konstante fuer den gewuenschten Port beim TargetHost. */
	private static final int TARGET_PORT = 80;

	/** Konstante fuer den Port des Proxys. */
	private static final int PORT = 8082;

	/**
	 * @param ignored
	 * @throws IOException
	 */
	public static void main(final String... ignored) throws IOException {

		final int port = PORT;

		try (final ServerSocket serverSocket = new ServerSocket(port)) {

			while (true) {

				// Standard-IO/Socket-Voodoo fuer Rolle als Server.
				try (final Socket socket = serverSocket.accept();
						final InputStream input = socket.getInputStream();
						final InputStreamReader inputReader = new InputStreamReader(input);
						final BufferedReader buffReader = new BufferedReader(inputReader);

						final OutputStream output = socket.getOutputStream();
						final OutputStreamWriter outputWriter = new OutputStreamWriter(output);
						final PrintWriter printWriter = new PrintWriter(outputWriter)) {

					final List<String> request = new ArrayList<String>();

					System.err.println("*** Reading HTTP Request... ");

					// Abfangen und Speichern des urspruenglichen HTTP-Requests.
					for (String line = buffReader.readLine(); line != null && line.length() > 0; line = buffReader
							.readLine()) {

						request.add(line);

					}

					request.add("");

					// for (final String line : request) {
					// // Ausgabe des komplett eingelesenen Requests auf der Konsole zur Kontrolle.
					// System.err.println(line);
					// }

					// Parsen des TargetHosts aus dem abgefangenen HTTP Request, um Request weiterleiten zu koennen.
					final RequestParser requestParser = new RequestParser(request);

					final String targetHost = requestParser.getTargetHost();
					final int targetPort = TARGET_PORT;

					// Kontrollausgabe.
					System.err.printf(
							System.lineSeparator() + "*** Sending request to %s on Port %d:" + System.lineSeparator(),
							targetHost, targetPort);

					final InetAddress inetAddress = InetAddress.getByName(targetHost);
					// System.err.println("InetAddress is " + inetAddress.toString());

					// Standard-IO/Socket-Voodoo fuer Rolle als Client.
					try (final Socket targetSocket = new Socket(inetAddress, targetPort);
							final InputStream clientInput = targetSocket.getInputStream();
							final InputStreamReader clientInputReader = new InputStreamReader(clientInput);
							final BufferedReader clientBuffReader = new BufferedReader(clientInputReader);

							final OutputStream clientOutput = targetSocket.getOutputStream();
							final OutputStreamWriter steamWriter = new OutputStreamWriter(clientOutput);
							final PrintWriter writer = new PrintWriter(steamWriter)) {

						final Iterator<String> requestCursor = request.iterator();

						// Senden des Requests an TargetHost.
						while (requestCursor.hasNext()) {

							final String line = requestCursor.next();

							writer.println(line);
							System.err.println(line);
							writer.flush();
						}

						writer.println(System.lineSeparator());
						// Sicher ist sicher!
						writer.flush();

						// //////////////////////////////////////////////////////

						final List<String> responseHeader = new ArrayList<String>();

						// Abfangen des Response-Headers vom Server.
						System.err.println("*** Receiving response from Host... ");
						for (String line = clientBuffReader.readLine(); line != null; line = clientBuffReader
								.readLine()) {
							responseHeader.add(line);
							System.err.println(line);
						}

						// Weiterreichen des Headers zur Anpassung an den Manipulator
						final HTTPResponseManipulator responseManipulator = new HTTPResponseManipulator(responseHeader);
						final List<String> manipulatedResponse = responseManipulator.getManipulatedResponse();

						// Rausschicken des manipulierten Response Headers in der Rolle des Servers an den
						// urspruenglichen Client.
						final Iterator<String> manResponseCursor = manipulatedResponse.iterator();

						System.err.println("*** Sending Response to Client... ");

						while (manResponseCursor.hasNext()) {

							final String line = manResponseCursor.next();

							printWriter.println(line);
							System.err.println(line);
							printWriter.flush();
						}

						printWriter.println(System.lineSeparator());
						printWriter.flush();

					}
				}
			}
		}
	}
}
