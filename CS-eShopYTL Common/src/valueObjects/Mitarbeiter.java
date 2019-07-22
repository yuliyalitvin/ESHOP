package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Mitarbeiter extends Benutzer {

	/** 
	 * Konstruktor der Klasse Mitarbeiter
	 */
	public Mitarbeiter(String benutzerName, boolean kunde, String passwort, String vorname, String nachname) {

		super(benutzerName, kunde, passwort, vorname, nachname);

	}
}