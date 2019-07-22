package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Warenkorb {
	private String warenName;
	private int warenNummer;
	private int menge;
	private int summe;
	private float preis;

	public Warenkorb(String artikelname, int anr, float pr) {
		warenNummer = anr;
		this.warenName = artikelname;
		preis = pr;
	}

	public String getWarenName() {
		return warenName;
	}

	public int getWarenNummer() {
		return warenNummer;
	}

	public int getMenge() {
		return menge;
	}

	public float summe() {
		return summe;
	}

	public float getPreis() {
		return preis;
	}
}
