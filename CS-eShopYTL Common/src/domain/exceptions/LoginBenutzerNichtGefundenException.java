package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class LoginBenutzerNichtGefundenException extends Exception {
	public LoginBenutzerNichtGefundenException() {
		super("Benutzername und Passwort stimmen nicht ueberein.");
	}
}
