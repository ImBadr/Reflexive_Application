package bri;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import utilisateur.Utilisateur;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;


public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private Map<Utilisateur, String> users;
	
	/**
	 * Constructor : Create TCP Server - object of  ServerSocket class
	 * @param port
	 * @param users
	 */
	public ServeurBRi(int port, Map<Utilisateur, String> users) {
		try {
			this.listen_socket = new ServerSocket(port);
			this.users = users;
		} catch (IOException e) { throw new RuntimeException(e); }
	}

	/* Server listen and accept connections
	 * for each connection, it create a ServiceInversion
	 * which will trait it
	 */
	public void run() {
		try {
			while(true) {
				switch (listen_socket.getLocalPort()) {
				case 3000:
					new ServiceBRiProg(listen_socket.accept(), users).start();
					break;
				case 6000:
					new ServiceBRiAmateur(listen_socket.accept(), users).start();
					break;
				}
			}
		}
		catch (IOException e) { 
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	protected void finalize() throws Throwable {
		try { 
			this.listen_socket.close(); 
		} catch (IOException e1) {
				
		}
	}

	public void lancer() { 
		new Thread(this).start(); 
	}
}
