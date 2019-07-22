package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Ereignis {

	private int datum;
	private int artikelNummer;
	private int anzahl;
	private int ereignisArt; // 1 - neue Artikel, 2 - Einlagerung, 3 - Auslagerung (verkauft)
	private int personIndex; // abhaenging von der Ereignisart ist es ein Mitarbeiter oder Kunde

	public Ereignis() {

	}

	public void setDatum(int parseInt) {
		datum = parseInt;
	}

	public void setArtikelNummer(int parseInt) {
		artikelNummer = parseInt;
	}

	public void setEreignisart(int parseInt) {
		ereignisArt = parseInt;
	}

	public void setAnzahl(int parseInt) {
		anzahl = parseInt;
	}

	public void setPersonIndex(int parseInt) {
		personIndex = parseInt;
	}

	public int getDatum() {
		return datum;
	}

	public int getArtikelNummer() {
		return artikelNummer;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public int getEreignisArt() {
		return ereignisArt;
	}

	public int getPersonIndex() {
		return personIndex;
	}
}
