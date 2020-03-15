package utilisateur;
/**
 * @author TADJER Badr
 * @author CUSIN Albéric
 */
public class Utilisateur {
	private String login;
	private String password;
	
	/**
	 * Constructor for User
	 * @param l
	 * @param p
	 */
	public Utilisateur(String l, String p) {
		this.login = l;
		this.password = p;
	}
	
	/**
	 * get the login of the current user
	 * @return Login
	 */
	public String getLogin() {
		return this.login;
	}
	
	/**
	 * To verify the identy of an user
	 * @param login
	 * @param password
	 * @return boolean : True if login and password of are correct
	 */
	public boolean controlIdentity(String login, String password) {
		return (this.login.equals(login)) && (this.password.equals(password));
	}
	

}
