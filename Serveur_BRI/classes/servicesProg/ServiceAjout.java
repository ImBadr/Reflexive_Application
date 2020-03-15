package servicesProg;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceAjout implements Runnable {

    private Socket client;
    private String url;
    private String login;

    public ServiceAjout(Socket socket, String url, String login) {
        client = socket;
        this.url = url;
        this.login = login;
    }

    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            out.println("Please enter the name of the class that you want to load :");
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(url)});
            String classeName = in.readLine();
            Class<?> classe = urlClassLoader.loadClass(login + "." + classeName);

            ServiceRegistry.addService(classe);
            out.println("Your service has been added ! #MSG");
            in.readLine();

        } catch (Exception e) {
            if (out != null) {
                out.println(e + "#MSG");
                try {
                    in.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

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
