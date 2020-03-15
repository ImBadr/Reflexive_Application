package servicesProg;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceUpdate implements Runnable {

    private Socket client;
    private String url;

    public ServiceUpdate(Socket socket, String url) {
        client = socket;
        this.url = url;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("What is the name of your class that you want to update ?");
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(url)});

            String classeName = in.readLine();

            Class<?> classe = urlClassLoader.loadClass(classeName);
            ServiceRegistry.addService(classe);

            client.close();
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
