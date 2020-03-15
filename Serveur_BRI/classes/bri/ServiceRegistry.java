package bri;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import verification.VerificationServiceBRI;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

	static {
		servicesClasses = new ConcurrentHashMap<Class<?>, Boolean>();
	}
	private static Map<Class<?>, Boolean> servicesClasses;
	
	/**
	 * Add a class which follow the ServiceBRI verification
	 * 
	 * @param classeName class enter by user
	 * @throws Exception NoClassFoundException
	 */
	public static void addService(Class<?> classeName) throws Exception {
		if(!servicesClasses.containsKey(classeName)) {
			VerificationServiceBRI.verificationBRI(classeName);
			servicesClasses.put(classeName, true);
		} else {
			servicesClasses.remove(classeName);
			addService(classeName);
		}
	}

	/**
	 * renvoie la classe de service (numService -1)
	 * 
	 * @param numService
	 * @return Class<?>
	 */
	public static Class<?> getServiceClass(int numService) {
		return (Class<?>) servicesClasses.keySet().toArray()[numService - 1];
	}

	/*
	 * Met a false le service (numService - 1) à true
	 * Met a true le service (numService - 1) à false
	 */
	public static void setClassState(int numService) {
		Class<?> classe = (Class<?>) servicesClasses.keySet().toArray()[numService - 1];
		if (servicesClasses.get(classe))
			servicesClasses.replace(classe, false);
		else
			servicesClasses.replace(classe, true);
	}

	/**
	 * @return String
	 */
	public static String toStringue(String login) {
		sortMapByProgName(login);
		String result = "Activities :#n";
		int cpt = 1;
		for (Entry<Class<?>,Boolean> entry : servicesClasses.entrySet()) {
			if((entry.getKey().getName().replace(".",",").split(",")[0].equals(login))){
				result += "#" + cpt++ + " " + entry.getKey().getName();
				if (entry.getValue()) {
					result += " \t -- STARTED";
				} else
					result += " \t -- STOPPED";
				result += "#n";
			}
		}

		if (cpt == 1)
			return "Any activities ! Please press (Q) to quit !";
		return result;
	}
	
	/*
	 * Sort la ConcurrentHashMap par nom d'utilisateur (login)
	 */
	
	private static void sortMapByProgName(String login) {
		for(Entry<Class<?>, Boolean> entry : servicesClasses.entrySet()) {
			if(!(entry.getKey().getName().replace(".",",").split(",")[0].equals(login))) {
				Class<?> classe = entry.getKey();
				boolean b = entry.getValue();
				servicesClasses.remove(classe);
				servicesClasses.put(classe, b);
			}
		}
	}
	
	public static int getNumberOfService(String login) {
		int cpt = 0;
		for(Entry<Class<?>, Boolean> entry : servicesClasses.entrySet()) {
			if (entry.getKey().getPackage().toString().contains(login))
				cpt++;
		}
		return cpt;
	}
	
	/*
	 * Return number of started Services
	 */
	public static int getNumberOfServiceAma() {
		int cpt = 0;
		for(Entry<Class<?>, Boolean> entry : servicesClasses.entrySet()) {
			if (entry.getValue())
				cpt++;
		}
		return cpt;
	}
	
	/*
	 * Sort ConcurrentHashMap according started services
	 */
	private static void sortMapByStarted() {
		for(Entry<Class<?>, Boolean> entry : servicesClasses.entrySet()) {
			if (!entry.getValue()) {
				Class<?> classe = entry.getKey();
				servicesClasses.remove(classe);
				servicesClasses.put(classe, false);
			}
		}
	}
	
	/*
	 * Return la liste des activités disponibles pour un amateur 
	 */
	public static String toStringueAma() {
		sortMapByStarted();
		String result = "Activities :#n";
		int cpt = 1;
		for (Entry<Class<?>,Boolean> entry : servicesClasses.entrySet()) {
			result += "#" + cpt++ + " ";
			if (entry.getValue())
				result += entry.getKey().getName() + " #n";
		}
		if(cpt == 1)
			return "There are any activities for the moment !";
		return result;
	}

	public static void removeClassLoader(int numClassLoader){
		Class<?> classe = (Class<?>) servicesClasses.keySet().toArray()[numClassLoader - 1];
		servicesClasses.remove(classe);
		classe = null;
	}

}
