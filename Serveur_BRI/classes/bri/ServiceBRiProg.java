package bri;

/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import servicesProg.*;
import utilisateur.Utilisateur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

class ServiceBRiProg implements Runnable {

	private Socket client;
	private Map<Utilisateur, String> users;

	private static final int ADD = 1;
	private static final int UPDATE = 2;
	private static final int CHANGE_FTP = 3;
	private static final int STOP_AND_GO = 4;
	private static final int UNINSTALL = 5;

	/**
	 * Constructor
	 * 
	 * @param socket
	 * @param users
	 */
	ServiceBRiProg(Socket socket, Map<Utilisateur, String> users) {
		client = socket;
		this.users = users;
	}

	/**
	 * Verify if login and password are correct
	 * 
	 * @param login
	 * @param password
	 * @return link : FTP URL of the developer
	 */
	private String verifyIdentity(String login, String password) {
		String url = "";
		for (Map.Entry<Utilisateur, String> entry : users.entrySet()) {
			if (url.isEmpty())
				if (entry.getKey().controlIdentity(login, password))
					url = entry.getValue();
		}
		return url;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			out.println("Enter your Login : ");
			String login = in.readLine();
			out.println("Enter your Password : ");
			String password = in.readLine();
			String url = verifyIdentity(login, password);
			if (!url.isEmpty()) {
				String message = "Welcome on the Programming Server !#n";
				message += "#1 Add a new Service #n";
				message += "#2 Update an existing Service #n";
				message += "#3 Change FTP adress #n";
				message += "#4 STOP or START a service #n";
				message += "#5 Uninstall a service #n";
				out.println(message);

				boolean badFormat = true;
				boolean again = true;
				String againAnswer;

				while (again) {
					while (badFormat) {
						try {
							switch (Integer.parseInt(in.readLine())) {
								case ADD:
									new ServiceAjout(client, url, login).run();
									badFormat = false;
									break;
								case UPDATE:
									new ServiceUpdate(client, url).run();
									badFormat = false;
									break;
								case CHANGE_FTP:
									new ServiceChangeFTP(client, url, users).run();
									badFormat = false;
									break;
								case STOP_AND_GO:
									new ServiceStopAndGo(client, login).run();
									badFormat = false;
									break;
								case UNINSTALL:
									new ServiceUninstall(client, login).run();
									badFormat = false;
									break;
								default:
									badFormat = true;
									out.println("Look at the choices and choose one of them !#n");
									break;
							}

						} catch (Exception e) {
							badFormat = true;
							out.println("We are asking for a INTEGER, please respect the rules !#n");
						}
					}

					out.println("Would you like to choose an other service ? (N/Y)");
					againAnswer = in.readLine();

					while (!(againAnswer.equalsIgnoreCase("N") || againAnswer.equalsIgnoreCase("Y"))) {
						out.println("Would you like to choose an other service ? (Y/N)");
						againAnswer = in.readLine();
					}

					if (againAnswer.equalsIgnoreCase("Y")) {
						again = true;
						badFormat = true;
						out.println("Please re-choose a service !");
					} else again = false;

				}

				out.println("See you soon !#END");
			} else
				out.println("You are nobody, get out server please !#END");
			client.close();
		} catch (IOException e) {
			// End of service
		}
	}

	protected void finalize() throws Throwable {
		client.close();
	}

	/*
	 * Starting of service
	 */
	public void start() {
		new Thread(this).start();
	}

}
