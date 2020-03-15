package client;
/**
  @author TADJER Badr
 * @author CUSIN Albéric
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientAma {
	private final static int PORT_AMA = 6000;
	private final static String HOST = "localhost";

	public static void main(String[] args) {
		Socket s = null;
		try {
			s = new Socket(HOST, PORT_AMA);

			BufferedReader sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter sout = new PrintWriter(s.getOutputStream(), true);
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Connecté au serveur " + s.getInetAddress() + ":" + s.getPort());

			String line;

			do {
				line = sin.readLine().replaceAll("#n", "\n");
				if (line.contains("#SERVICE")){
					System.out.println(line.replace("#SERVICE", ""));
					sout.println("ROGER THAT");
				}
				else{
					System.out.println(line.replace("#END", ""));
					if (!line.contains("#END"))
						sout.println(clavier.readLine());
				}
			} while (!line.contains("#END"));

		} catch (IOException e) {
			System.err.println("Fin de la connexion");
		}

		/*
		 * Close in all cases the socket
		 */
		try {
			if (s != null)
				s.close();
		} catch (IOException e2) {

		}
	}
}
