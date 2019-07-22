package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Artikel {

	protected String artikelName;
	protected int artikelNummer;
	protected int anzahl;
	protected float preis;
	protected int stueck = 0;
	protected boolean verfuegbar;
	protected boolean massengut;
	protected boolean imKorb = false;
	protected int pack;

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

	public String toString() {
		String verfuegbarkeit = verfuegbar ? "Auf Lager" : "nicht verfügbar";
		String imWarenkorb = imKorb ? "\n/ im Warenkorb: " +stueck : "";
		return ("Nr: " + artikelNummer + " / Artikelname: " + artikelName + "/ Preis: " + preis 
				+ " / Bestand: " + anzahl + "/" + verfuegbarkeit
				+ imWarenkorb);
	}

	public boolean equals(Object anderesObjekt) {
		if (anderesObjekt instanceof Artikel) {
			Artikel anderenArtikel = (Artikel) anderesObjekt;
			return ((this.artikelNummer == anderenArtikel.artikelNummer)
					&& (this.artikelName.equals(anderenArtikel.artikelName)));
		}
		return false;
	}
	
	public void setStueck(int stueckNeu) {
		stueck = stueckNeu;
	}
	
	public void setAnzahl(int anzahlNeu) {
		anzahl = anzahlNeu;
	}
	
	public void setImKorb(boolean imKorbNeu) {
		imKorb = imKorbNeu;
	}
	
	public void setVerfuegbar(boolean verfuegbarNeu) {
		verfuegbar = verfuegbarNeu;
	}

	public int getArtikelNummer() {
		return artikelNummer;
	}

	public String getArtikelName() {
		return artikelName;
	}

	public boolean isVerfuegbar() {

		return verfuegbar;
	}

	public boolean isImKorb() {
		return imKorb;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public float getPreis() {
		return preis;
	}

	public int getStueck() {
		return stueck;
	}
	
	public boolean isMassengut() {
		return massengut;
	}
	public int getPack() {
		return pack;
	}
}