package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Massengutartikel extends Artikel {

	private int pack;

	public Massengutartikel(String artikelname, int anr, float pr, int anz, boolean verfuegbar, boolean massengut,
			int pack) {
		super(artikelname, anr, pr, anz, verfuegbar, massengut);
		this.pack = pack;
	}

	public Massengutartikel(Massengutartikel artikel) {
		super(artikel);

		pack = artikel.pack;
	}

	public String toString() {
		String verfuegbarkeit = verfuegbar ? "Auf Lager" : "nicht verfügbar";
		String imWarenkorb = imKorb ? "\n/ im Warenkorb: " + stueck : "";
		return ("Nr: " + artikelNummer + " / Artikelname: " + artikelName + "/ Preis: " + preis 
				+ " / Bestand: " + anzahl + " / Packungsinhalt: " + pack + "/" + verfuegbarkeit +
				 imWarenkorb);
	}

	public int getPack() {
		return pack;
	}

	public int getStueck() {
		return stueck;
	}
}
