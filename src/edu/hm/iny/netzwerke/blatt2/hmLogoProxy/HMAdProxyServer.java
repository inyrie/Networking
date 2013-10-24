/**
 *
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.io.IOException;

/**
 * @author Stäff
 */
public class HMAdProxyServer {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		final String host = args[0];

		final ImageProxyServer proxy = new ImageProxyServer(host);
		proxy.handleConnections();
	}
}
