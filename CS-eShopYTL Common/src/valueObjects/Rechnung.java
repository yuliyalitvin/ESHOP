package valueObjects;

import java.util.Calendar;
import java.util.Vector;

/**
 * Rechnungzusammenstellung
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Rechnung {

	Calendar datum;
	Kunde kunde;
	Vector<Artikel> gekaufteArtikel;
	float gesamtPreis;

	public Rechnung(Kunde benutzer, Vector<Artikel> warenkorb) {
		datum = Calendar.getInstance();
		kunde = benutzer;
		gekaufteArtikel = new Vector<Artikel>();
		for (Artikel artikel : warenkorb) {
			Artikel gekaufterArtikel = null;
			if (artikel.isMassengut() == false) {
				gekaufterArtikel = new Artikel(artikel);
			} else {
				gekaufterArtikel = new Massengutartikel((Massengutartikel) artikel);
			}
			gekaufteArtikel.add(gekaufterArtikel);
		}
		setGesamtPreis();
	}

	private void setGesamtPreis() {
		for (Artikel artikel : gekaufteArtikel) {
			gesamtPreis += artikel.getPreis() * artikel.getStueck();
		}
	}

	private String datumAusgeben() {
		return ("Kaufdatum: " + datum.get(Calendar.DAY_OF_MONTH) + "." + (datum.get(Calendar.MONTH) + 1) + "."
				+ datum.get(Calendar.YEAR) + "\nUhrzeit: " + datum.get(Calendar.HOUR_OF_DAY) + ":"
				+ datum.get(Calendar.MINUTE) + ":" + datum.get(Calendar.SECOND));
	}

	public String artikelAusgebenToString() {
		String gekaufteArtikelImWarenkorb = "";

		for (Artikel artikel : gekaufteArtikel) {
			gekaufteArtikelImWarenkorb += "Nummer: " + artikel.getArtikelNummer() + " Name: " + artikel.getArtikelName()
					+ " Preis: " + artikel.getPreis() + " Anzahl: " + artikel.getStueck() + "\n";
		}
		return gekaufteArtikelImWarenkorb;
	}

	public String ausgebenToString() {
		return "RECHNUNG: \n" + datumAusgeben() + "\nKunde: " + kunde.getVorname() + " " + kunde.getNachname()
				+ "\nAddresse: " + kunde.getStrasse() + "\n" + kunde.getPostleitzahl() + " " + kunde.getOrt() + ", "
				+ kunde.getLand() + "\nGekaufte Artikel: \n" + artikelAusgebenToString() + "\nGesamtpreis: "
				+ gesamtPreis + "\n";
	}
}
