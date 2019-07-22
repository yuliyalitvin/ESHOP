package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class BenutzerExistiertBereitsException extends Exception {
	public BenutzerExistiertBereitsException() {
		super("Benutzer mit Namen existiert bereits.");
	}
}
