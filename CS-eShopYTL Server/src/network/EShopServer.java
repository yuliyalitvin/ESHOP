package network;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import domain.Verwaltung;

public class EShopServer {

	public final static int DEFAULT_PORT = 6789;

	private int port;
	private ServerSocket serverSocket;

	private Verwaltung eShop;

	public EShopServer(int optPort) {

		this.port = (optPort == 0) ? DEFAULT_PORT : optPort;

		try {
			serverSocket = new ServerSocket(port);

			InetAddress ia = InetAddress.getLocalHost();
			System.out.println("Host: " + ia.getHostName());
			System.out.println("Server *" + ia.getHostAddress() + "* lauscht auf Port " + port);
		} catch (IOException e) {
			System.err.println("Eine Ausnahme trat beim Anlegen des Server-Sockets auf: " + e);
			System.exit(1);
		}

		try {
			eShop = new Verwaltung("ESHOP");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void acceptClientConnectRequests() {

		try {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				ClientEShopRequestProcessor c = new ClientEShopRequestProcessor(clientSocket, eShop);
				Thread thread = new Thread(c);
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Fehler waehrend des Wartens auf Verbindungen: " + e);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		int port = 0;
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				port = 0;
			}
		}
		EShopServer server = new EShopServer(port);
		server.acceptClientConnectRequests();
	}
}