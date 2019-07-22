package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Artikel {

	protected String artikelName;
	protected int artikelNummer;
	protected int anzahl; // Lagerbestand
	protected float preis;
	protected int stueck = 0; // Summe aus Anzahl im Warenkorb von allen Benutzern
	protected boolean verfuegbar;
	protected boolean massengut;
	protected int pack;

	/**
	 * Methode, um ein neuen Artikel zuerstellen Bzw. konstruktor
	 * 
	 */
	public Artikel(String artikelname, int anr, float pr, int anz, boolean verfuegbar, boolean massengut) {
		artikelNummer = anr;
		this.artikelName = artikelname;
		preis = pr;
		anzahl = anz;
		this.verfuegbar = verfuegbar;
		this.massengut = massengut;
	}

	public Artikel(Artikel artikel) {
		artikelNummer = artikel.getArtikelNummer();
		artikelName = artikel.getArtikelName();
		preis = artikel.getPreis();
		anzahl = artikel.getAnzahl();
		verfuegbar = artikel.verfuegbar;
		stueck = artikel.stueck;
		massengut = artikel.massengut;
	}

	/**
	 * Methode, zur ueberprüfung eines Objekts mit Artikel uebereinstimmt
	 *
	 * @param anderesObjekt ueberpruefendes Objekt
	 * 
	 * @return true wenn artikel gleich sind
	 * @return false wenn objekt nicht teil der Artikel ist
	 */
	public boolean equals(Object anderesObjekt) {
		if (anderesObjekt instanceof Artikel) {
			Artikel anderenArtikel = (Artikel) anderesObjekt;
			return ((this.artikelNummer == anderenArtikel.artikelNummer)
					&& (this.artikelName.equals(anderenArtikel.artikelName)));
		}
		return false;
	}

	/**
	 * Methode, um neue stueckzahl anzugeben
	 *
	 * @param stueckNeu der neue Wert
	 */
	public void setStueck(int stueckNeu) {
		stueck = stueckNeu;
	}

	/**
	 * Methode, um neue Anzahl/Bestand des Artikel anzugeben
	 *
	 * @param anzahlNeu der neue Wert
	 */
	public void setAnzahl(int anzahlNeu) {
		anzahl = anzahlNeu;
	}

	/**
	 * Methode, um neue verfuegbarkeit des Artikels anzugeben
	 *
	 * @param verfuegbarNeu der neue Wert
	 */
	public void setVerfuegbar(boolean verfuegbarNeu) {
		verfuegbar = verfuegbarNeu;
	}

	/**
	 * Methode, gib die Artikelnummer wieder
	 *
	 * @return artikelNummer Nummer des Artikel
	 */
	public int getArtikelNummer() {
		return artikelNummer;
	}

	/**
	 * Methode, gib den Artikelname wieder
	 *
	 * @return artikelName Name des Artikel
	 */
	public String getArtikelName() {
		return artikelName;
	}

	/**
	 * Methode, gib die verfuegbarkeit des Artikels wieder
	 *
	 * @return verfuegbar Verfuegbarkeit des Artikel
	 */
	public boolean isVerfuegbar() {
		return verfuegbar;
	}

	/**
	 * Methode, gib die Anzahl des Artikels wieder
	 *
	 * @return anzahl Wie viel Bestand auf Lager ist
	 */
	public int getAnzahl() {
		return anzahl;
	}

	/**
	 * Methode, gib den Preis wieder
	 *
	 * @return preis Preis des Artikels
	 */
	public float getPreis() {
		return preis;
	}

	/**
	 * Methode, gib die stueckzahl
	 *
	 * @return stueck stueckzahl des Artikel
	 */
	public int getStueck() {
		return stueck;
	}

	/**
	 * Methode, gib wieder ob dieser Artikel ein Massengutartikel ist
	 *
	 * @return massengut boolean ob es ein Massengutartikel ist
	 */
	public boolean isMassengut() {
		return massengut;
	}

	/**
	 * Methode, gib die Packung wieder
	 *
	 * @return pack wie viele sich in einer Packung befindet
	 */
	public int getPack() {
		return pack;
	}
}