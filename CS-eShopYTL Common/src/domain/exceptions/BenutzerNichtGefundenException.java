package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class BenutzerNichtGefundenException extends Exception {
	public BenutzerNichtGefundenException() {
		super("Benutzer nicht gefunden.");
	}
}
