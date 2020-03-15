package servicesProg;

import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceUninstall implements Runnable {
    private Socket client;
    private String login;

    public ServiceUninstall(Socket socket, String login) {
        client = socket;
        this.login = login;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("#nPlease, select a service to uninstall !" + ServiceRegistry.toStringue(login));

            boolean badFormat = true;

            if (ServiceRegistry.getNumberOfService(login) != 0) {
                while (badFormat) {
                    try {
                        int numService = Integer.parseInt(in.readLine());
                        while (numService > ServiceRegistry.getNumberOfService(login) || numService < 1) {
                            out.println("#n Please, enter a right number associated to a service #n");
                            numService = Integer.parseInt(in.readLine());
                        }
                        badFormat = false;
                        ServiceRegistry.removeClassLoader(numService);
                    } catch (Exception e) {
                        badFormat = true;
                    }
                }
            } else {
                while (!(in.readLine().equalsIgnoreCase("Q"))) {
                    out.println("Please press (Q) to quit !");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    protected void finalize() throws Throwable {
        client.close();
    }

    /*
     * lancement du service
     */
    public void start() {
        new Thread(this).start();
    }
}
