package bri;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import utilisateur.Utilisateur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Map;

class ServiceBRiAmateur implements Runnable {

    private Socket client;
    // INUTILE
    private Map<Utilisateur, String> users;

    /**
     * Constructor : Service to redirect the novice developer
     *
     * @param socket
     * @param users
     */
    ServiceBRiAmateur(Socket socket, Map<Utilisateur, String> users) {
        this.client = socket;
        this.users = users;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            boolean again = true;
            while (again) {
                boolean badFormatInteger = true;
                int choix = -1;
                while (badFormatInteger) {
                    out.println(ServiceRegistry.toStringueAma() + "#nEnter the service number that you want to use :");
                    try {
                        choix = Integer.parseInt(in.readLine());
                        badFormatInteger = (choix < 1 || choix > ServiceRegistry.getNumberOfServiceAma());
                    } catch (NumberFormatException e) {
                        badFormatInteger = true;
                    }


                }
                Class<?> classe = ServiceRegistry.getServiceClass(choix);
                try {
                   Constructor<?> c = classe.getConstructor(Socket.class);
                   ((Service) c.newInstance(client)).run();
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                	e.printStackTrace();
                }
                boolean badFormatString = true;
                String ans = "";
                while (badFormatString) {
                    out.println("Do you want to continue ? (Y/N)");
                    ans = in.readLine();
                    badFormatString = !(ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("n"));
                }
                again = ans.equalsIgnoreCase("y");
                if (!again)
                    out.println("See you soon on the server !#END");
            }

            /*
             * instancier le service numéro "choix" en lui passant la socket "client"
             * invoquer run() pour cette instance ou la lancer dans un thread à part
             */

        } catch (IOException e) {
            /*
             * Fin du service
             */
        }

        try {
            client.close();
        } catch (IOException e2) {
            e2.printStackTrace();
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
