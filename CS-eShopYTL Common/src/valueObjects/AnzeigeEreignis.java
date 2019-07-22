package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class AnzeigeEreignis extends Ereignis {

	private String benutzerName;
	private String artikelName;

	/**
	 * Konstruktor
	 */
	public AnzeigeEreignis(Ereignis ereignis) {
		setDatum(ereignis.getDatum());
		setArtikelNummer(ereignis.getArtikelNummer());
		setAnzahl(ereignis.getAnzahl());
		setEreignisart(ereignis.getEreignisArt());
		setPersonIndex(ereignis.getPersonIndex());
	}

	/**
	 * Konstruktor
	 */
	public AnzeigeEreignis(int datum, int artikelNummer, String artikelName, int anzahl, int ereignisArt,
			int personIndex, String benutzerName) {
		setDatum(datum);
		setArtikelNummer(artikelNummer);
		setArtikelName(artikelName);
		setAnzahl(anzahl);
		setEreignisart(ereignisArt);
		setPersonIndex(personIndex);
		setBenutzerName(benutzerName);
	}

	/**
	 * Methode zum setten des Benutzernamens
	 */
	public void setBenutzerName(String benutzerNameInput) {
		benutzerName = benutzerNameInput;
	}

	/**
	 * Methode zum getten des Benutzernamens
	 */
	public String getBenutzerName() {
		return benutzerName;
	}

	/**
	 * Methode zum setten des Artikelnamens
	 */
	public void setArtikelName(String artikelNameInput) {
		artikelName = artikelNameInput;
	}

	/**
	 * Methode zum getten des Artikelnamens
	 */
	public String getArtikelName() {
		return artikelName;
	}

}
