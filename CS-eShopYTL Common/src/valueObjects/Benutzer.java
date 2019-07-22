package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Benutzer {

	private String benutzerName;
	private int benutzerIndex;
	boolean kunde;
	private String passwort;
	private String vorname;
	private String nachname;

	/**
	 * Konstruktor der Benutzerklasse
	 */
	public Benutzer(String benutzerName, boolean kunde, String passwort, String vorname, String nachname) {

		this.benutzerName = benutzerName;
		this.kunde = kunde;
		this.passwort = passwort;
		this.vorname = vorname;
		this.nachname = nachname;
	}

	/**
	 * Methode zum bekommen des Benutzernamens
	 */
	public String getBenutzerName() {
		return benutzerName;
	}

	/**
	 * Methode zum bekommen des Booleans ob es ein Kunde oder Mitarbeiter ist
	 */

	public boolean isKunde() {
		return kunde;
	}

	/**
	 * Methode zum bekommen des Passwortes
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * Methode zum bekommen des Vornamens
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Methode zum bekommen des Nachnamens
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * Methode zum bekommen des Benutzerindex
	 */
	public int getBenutzerIndex() {
		return benutzerIndex;
	}

	/**
	 * Methode zum setten des Benutzerindex
	 */
	public void setBenutzerIndex(int benutzerIndex) {
		this.benutzerIndex = benutzerIndex;
	}

}