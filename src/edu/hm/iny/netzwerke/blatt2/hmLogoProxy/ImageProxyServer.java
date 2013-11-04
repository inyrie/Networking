/**
 * Munich University for Applied Science,
 * Faculty 07 for Mathematics and Computer Science
 * Netzwerke I, WS2013/14, Praktikumsgruppe IF2B[2]
 * Windows 7 Professional SP1; Java-Version: 1.7.0_21
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Klasse fuer einen Proxyserver auf einem gewuenschten Port (Default ist Port 8020), der die Bilder auf einer
 * aufgerufenen Website durch HM-Logo-Bilder austauscht.
 * @author Stephanie Ehrenberg
 * @author Diana Irmscher
 * @version 2013-11-04
 */
public class ImageProxyServer {

	/** Konstante fuer den Standard-Port beim TargetHost. */
	private static final int STD_TARGET_PORT = 80;

	/** Konstante fuer den Port des Proxys. */
	private static final int PROXY_PORT = 8082;

	private final String targetHost;

	private final int targetPort;

	// ///////////////////////////////// C T O R //////////////////////////////////////////

	/**
	 * Ctor, wenn anderer Port gewuenscht wird als der Standardport.
	 * @param targetHost
	 * @param targetPort
	 */
	ImageProxyServer(final String targetHost, final int targetPort) {

		this.targetHost = targetHost;
		this.targetPort = targetPort;
	}

	/**
	 * Ctor fuer Proxyserver auf Defaultport 8020.
	 * @param targetHost
	 */
	ImageProxyServer(final String targetHost) {
		this(targetHost, STD_TARGET_PORT);
	}

	/**
	 * Baut die einzelnen Verbindungen zum Client bzw. zum Targethost auf.
	 * @throws IOException
	 */
	void handleConnections() throws IOException {

		try (final ServerSocket serverSocket = new ServerSocket(PROXY_PORT)) {

			while (true) {

				System.err.println("*** HM Logo Advertising-Server now running on port " + PROXY_PORT);

				// Standard-IO/Socket-Voodoo fuer Rolle als Server.
				try (final Socket socket = serverSocket.accept();
						final BufferedReader fromClient = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						final PrintWriter toClient = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {

					System.err.println("*** Oh! It seems we have a visitor... ");

					// Standard-IO/Socket-Voodoo fuer Rolle als Client.
					try (final Socket targetSocket = new Socket(InetAddress.getByName(targetHost), targetPort);
							final BufferedReader fromServer = new BufferedReader(new InputStreamReader(
									targetSocket.getInputStream()));
							final PrintWriter toServer = new PrintWriter(new OutputStreamWriter(
									targetSocket.getOutputStream()))) {

						System.err.printf("*** Connection to %s on port %d established" + System.lineSeparator(),
								targetHost, targetPort);

						sendHTTPRequest(fromClient, toServer);
						receiveResponseHeader(fromServer);

						// Einlesen und Manipulation des response bodies.
						final HTTPResponseManipulator responseManipulator = new HTTPResponseManipulator(
								receiveResponseBody(fromServer));

						// Auslieferung der manipulierten Seite an den Client.
						sendTamperedResponse(responseManipulator.getTamperedResponse(), toClient);
					}
				}
			}
		}
	}

	/**
	 * Methode zum Einlesen des Request Headers, damit Request an target host weitergeschickt werden kann.
	 * @param fromClient BuffReader-Objekt, das Datenquelle Client repraesentiert.
	 * @throws IOException
	 * @return Den urspruenglichen HTTP Request Header vom Client.
	 */
	private List<String> readRequestHeader(final BufferedReader fromClient) throws IOException {

		final List<String> requestFromClient = new ArrayList<String>();

		System.err.printf(System.lineSeparator() + "*** Sending request to %s on Port %d:" + System.lineSeparator(),
				targetHost, targetPort);

		for (String line = fromClient.readLine(); line != null && line.length() > 0; line = fromClient.readLine()) {
			requestFromClient.add(line);
			System.err.println(line);
		}

		return requestFromClient;
	}

	/**
	 * Methode zum Abfangen und Ausgeben des Response-Headers vom Server. Der Header wird nicht weiter beachtet.
	 * @param fromServer Ein BuffReader-Objekt , das den Server als Datenquelle repraesentiert.
	 * @throws IOException
	 */
	private void receiveResponseHeader(final BufferedReader fromServer) throws IOException {

		System.err.println("*** Receiving response from Host... ");

		for (String line = fromServer.readLine(); line.length() > 0; line = fromServer.readLine()) {
			System.err.println(line);
		}
	}

	/**
	 * Methode zum Empfangen/Einlesen des HTTP Response bodies.
	 * @param fromServer Ein BuffReader-Objekt , das den Server als Datenquelle repraesentiert.
	 * @return Den Body (also die HTML-Seite) der HTTP Response.
	 * @throws IOException
	 */
	private List<String> receiveResponseBody(final BufferedReader fromServer) throws IOException {

		final List<String> responseBody = new ArrayList<String>();

		for (String line = fromServer.readLine(); line != null; line = fromServer.readLine()) {

			responseBody.add(line);
			// System.err.println(line);
		}

		return responseBody;
	}

	/**
	 * Sendet einen HTTP Request an einen global definierten Server.
	 * @param toServer Ein PrintWriter Ojekt fuer den Output zum Server.
	 * @throws IOException
	 */
	private void sendHTTPRequest(final BufferedReader fromClient, final PrintWriter toServer) throws IOException {

		final RequestParser requestParser = new RequestParser(readRequestHeader(fromClient));
		final String request = "GET " + requestParser.getRelativeAddress() + " HTTP/1.1" + System.lineSeparator()
				+ "Host: " + targetHost + System.lineSeparator() + System.lineSeparator();

		toServer.println(request);
		toServer.flush();
	}

	/**
	 * Methode zum Rausschicken des manipulierten Response an den Client.
	 * @param manipulatedResponse Die manipulierte HTTP Response mit ausgetauschten URL fuer die Bilder.
	 * @param toClient Ein PrintWriter Objekt fuer den Output zum Client.
	 */
	private void sendTamperedResponse(final List<String> manipulatedResponse, final PrintWriter toClient) {

		// Rausschicken des manipulierten Response Headers in der Rolle des Servers an den
		// urspruenglichen Client.
		final Iterator<String> manResponseCursor = manipulatedResponse.iterator();

		System.err.println("*** Sending tampered HTTP response to Client... ");

		while (manResponseCursor.hasNext()) {

			final String line = manResponseCursor.next();

			toClient.println(line);
			System.err.println(line);
			toClient.flush();
		}

		toClient.println(System.lineSeparator());
		toClient.flush();
	}
}
