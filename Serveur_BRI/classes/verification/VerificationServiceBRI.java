package verification;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class  VerificationServiceBRI {
	public static void verificationBRI(Class<?> classe) throws Exception {
		if(!(Modifier.toString(classe.getModifiers()).contains("public"))) {
			if (Modifier.toString(classe.getModifiers()).contains("abstract"))
				throw new Exception("La classe n'est pas publique et abstraite -> pas conforme !");
			throw new Exception("La classe n'est pas publique -> pas conforme !");
		}
		boolean BRI = false;	
		if (classe.getInterfaces().length > 0) {
			for(Class<?> c : classe.getInterfaces()) {
				if (c.getName().equals("bri.Service"))
					BRI = true;
			}
		}
		if (!BRI)
			throw new Exception("La classe n'implémente pas l'interface bri.Service -> pas conforme !");
		try {
			if (classe.getConstructor(java.net.Socket.class).getExceptionTypes().length > 0)
				throw new Exception("Le constructeur a des Exceptions -> pas conforme");
		} catch (NoSuchMethodException e) {
			throw new Exception("La classe n'a pas de constructeur public(Socket) -> pas conforme !");
		}
		boolean attrSocketPrivateFinal = false;
		for(Field attribut : classe.getDeclaredFields()) {
			if(attribut.getType() == java.net.Socket.class) {
				if ((Modifier.isPrivate(attribut.getModifiers()) && Modifier.isFinal(attribut.getModifiers())))
					attrSocketPrivateFinal = true;
			}
		}
		if (!attrSocketPrivateFinal)
			throw new Exception("La classe de possède pas d'attribut de type private final Socket -> pas conforme");
		try {
			Method m = classe.getDeclaredMethod("toStringue");
			if (m.getExceptionTypes().length > 0)
				throw new Exception("La méthode toStringue a des Exceptions -> pas conforme");
			if (!(Modifier.toString(m.getModifiers()).contains("public"))) {
				if (!(Modifier.toString(m.getModifiers()).contains("static")))
					throw new Exception("La méthode toStringue n'est pas publique statique -> pas conforme");
				throw new Exception("La méthode toStringue n'est pas publique -> pas conforme");
			}
			if (m.getReturnType() != String.class)
				throw new Exception("Le type de retour de la classe toStringue() n'est pas String -> pas conforme");
				
			} catch (NoSuchMethodException e) {
				throw new Exception("La classe ne possède pas de méthode public static String toStringue()  -> pas conforme !");
		}
			
	}
}