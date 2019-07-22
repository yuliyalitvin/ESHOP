package valueObjects;

import java.util.List;
import java.util.Vector;

import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Kunde extends Benutzer {

	private String strasse;
	private int postleitzahl;
	private String ort;
	private String land;
	private List<ArtikelImWarenkorb> warenkorb;

	/** 
	 * Konstruktor des Kunden
	 */
	public Kunde(String benutzerName, boolean kunde, String passwort, String vorname, String nachname, String strasse,
			int plz, String ort, String land) {
		super(benutzerName, kunde, passwort, vorname, nachname);

		this.strasse = strasse;
		postleitzahl = plz;
		this.ort = ort;
		this.land = land;
		warenkorb = new Vector<ArtikelImWarenkorb>();

	}

	/** 
	 * Methode zum setzen des Booleans dass der Artikel im Warenkorb ist
	 */
	public boolean istImKorb(int artikelNummer) {
		
		boolean istImKorb = false;
		for (ArtikelImWarenkorb artikelImKorb : warenkorb) {
			if (artikelImKorb.getArtikelNummer() == artikelNummer) {
				istImKorb = true;
				break; 
			} 
		}
		
		return istImKorb;
	}
	
	/** 
	 * Methode zum veraendern der Anzahl eines Artikels in den Warenkorb
	 */
	public void anzahlImKorbAendern(int artikelNummer, int stueck) throws StueckzahlException, StueckzahlEntsprichtNichtPackungException{
		
		for (ArtikelImWarenkorb artikelImKorb : warenkorb) {
			if (artikelImKorb.getArtikelNummer() == artikelNummer) {
				artikelImKorb.setAnzahlImWarenkorb(stueck);
				break; 
			} 
		}
	}
	
	/** 
	 * Methode zum suchen eines Artikels im Warenkorb
	 */
	public ArtikelImWarenkorb sucheArtikelImWarenkorb(int artikelNummer) {
		
		ArtikelImWarenkorb artikelAusWarenkorb = null; 
		for (ArtikelImWarenkorb artikelImKorb : warenkorb) {
			if (artikelImKorb.getArtikelNummer() == artikelNummer) {
				artikelAusWarenkorb = artikelImKorb; 
				break; 
			} 
		}
		return artikelAusWarenkorb;
	}
	
	/** 
	 * Methode zum hinzufuegen eines Artikels in den Warenkorb
	 */
	public void fuegeZuWarenkorb(Artikel artikel, int stueck) throws StueckzahlException, StueckzahlEntsprichtNichtPackungException{
		ArtikelImWarenkorb artikelImKorb = new ArtikelImWarenkorb(artikel);
		artikelImKorb.setAnzahlImWarenkorb(stueck);
		warenkorb.add(artikelImKorb);
	}

	/** 
	 * Methode zum loeschen eines Artikels aus dem Warenkorb
	 */
	public void loescheArtikelAusWarenkorb(ArtikelImWarenkorb artikel) {
		warenkorb.remove(artikel);
	}

	/** 
	 * Methode zum bekommen der Strasse
	 */
	public String getStrasse() {
		return strasse;
	}

	/** 
	 * Methode zum bekommen der Postleitzahl
	 */
	public int getPostleitzahl() {
		return postleitzahl;
	}

	/** 
	 * Methode zum bekommen des Ortes
	 */
	public String getOrt() {
		return ort;
	}

	/** 
	 * Methode zum bekommen des Landes
	 */
	public String getLand() {
		return land;
	}

	/** 
	 * Methode zum bekommen des Warenkorbes
	 */
	public List<ArtikelImWarenkorb> getWarenkorb() {
		return warenkorb;
	}

}