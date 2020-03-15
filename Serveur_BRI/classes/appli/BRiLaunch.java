package appli;

/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import bri.ServeurBRi;
import utilisateur.Utilisateur;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BRiLaunch {
	private final static int PORT_AMA = 6000;
	private final static int PORT_PROG = 3000;
	private final static String ServerLink = "ftp://localhost:2121/";
	
	public static void main(String[] args) throws MalformedURLException {
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);
		
		// URLClassLoader on ftp

		System.out.println("Welcome in our Server");
		
		Map<Utilisateur, String> programmeurs = new HashMap<>();
		programmeurs.put(new Utilisateur("badr","tadjer"), ServerLink);
		programmeurs.put(new Utilisateur("albe","cusin"), ServerLink);

		Map<Utilisateur, String> amateurs = new HashMap<>();
		amateurs.put(new Utilisateur("brette","jfb"), "1");
		amateurs.put(new Utilisateur("rossit","jr"), "2");
		
		new Thread(new ServeurBRi(PORT_AMA, amateurs)).start();
		new Thread(new ServeurBRi(PORT_PROG, programmeurs)).start();
		
	}
}
