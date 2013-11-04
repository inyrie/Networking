/**
 * Munich University for Applied Science,
 * Faculty 07 for Mathematics and Computer Science
 * Netzwerke I, WS2013/14, Praktikumsgruppe IF2B[2]
 * Windows 7 Professional SP1; Java-Version: 1.7.0_21
 */
package edu.hm.iny.netzwerke.blatt2.hmLogoProxy;

import java.io.IOException;

/**
 * Proxy-Server, der auf einer gewuenschten Seite alle Bilder durch HM-Logo-Bilder austauscht.
 * @author Stephanie Ehrenberg
 * @author Diana Irmscher
 * @version 2013-11-04
 */
public class HMAdProxyServer {

	/**
	 * Proxy-Server, der auf einer gewuenschten Seite alle Bilder durch HM-Logo-Bilder austauscht.
	 * @param args Der gewuenschte Target-Host.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		final String host = args[0];

		final ImageProxyServer proxy = new ImageProxyServer(host);
		proxy.handleConnections();
	}
}
