package servicesProg;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import utilisateur.Utilisateur;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ServiceChangeFTP implements Runnable {

    private Socket client;
    private Map<Utilisateur, String> users;
    private String oldURL;

    public ServiceChangeFTP(Socket socket, String oldURL, Map<Utilisateur, String> users) {
        client = socket;
        this.oldURL = oldURL;
        this.users = users;
    }

    public void changeURL(String newURL) {
        for (Map.Entry<Utilisateur, String> entry : users.entrySet()) {
            if (entry.getValue().equals(oldURL))
                entry.setValue(newURL);
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("Please enter your new FTP Address ?");
            String newURL = in.readLine();
            changeURL(newURL);

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
