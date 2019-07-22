package valueObjects;

import java.util.List;
import java.util.Vector;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Kunde extends Benutzer {

	private String strasse;
	private int postleitzahl;
	private String ort;
	private String land;
	private List<Artikel> warenkorb;

	public Kunde(String benutzerName, boolean kunde, String passwort, String vorname, String nachname, String strasse,
			int plz, String ort, String land) {
		super(benutzerName, kunde, passwort, vorname, nachname);

		this.strasse = strasse;
		postleitzahl = plz;
		this.ort = ort;
		this.land = land;
		warenkorb = new Vector<Artikel>();

		erlaubteBefehle.add(new Befehl("a", "Artikelliste ausgeben", "ArtikelGruppe"));
		erlaubteBefehle.add(new Befehl("af", "Artikel suchen", "ArtikelGruppe"));
		erlaubteBefehle.add(new Befehl("ac", "Artikel sortieren", "ArtikelGruppe"));

		erlaubteBefehle.add(new Befehl("ab", "Artikel nach Bezeichnung sortieren", "ArtikelSortierenGruppe"));
		erlaubteBefehle.add(new Befehl("abr", "Artikel nach Bezeichnung sortieren (rueckwaerts)", "ArtikelSortierenGruppe"));
		erlaubteBefehle.add(new Befehl("an", "Artikel nach Nummer sortieren", "ArtikelSortierenGruppe"));
		erlaubteBefehle.add(new Befehl("anr", "Artikel nach Nummer sortieren (rueckwaerts)", "ArtikelSortierenGruppe"));

		erlaubteBefehle.add(new Befehl("w", "Warenkorb anzeigen", "WarenkorbGruppe"));
		erlaubteBefehle.add(new Befehl("aw", "Artikel in den Warenkorb hinzufuegen", "WarenkorbGruppe"));
		erlaubteBefehle.add(new Befehl("sw", "Stueckzahl eines Artikels in den Warenkorb aendern", "WarenkorbGruppe"));
		erlaubteBefehle.add(new Befehl("dw", "Artikel aus dem Warenkorb entfernen", "WarenkorbGruppe"));
		erlaubteBefehle.add(new Befehl("cw", "Warenkorb leeren", "WarenkorbGruppe"));
		erlaubteBefehle.add(new Befehl("k", "Zur Kasse gehen", "WarenkorbGruppe"));
		erlaubteBefehle.add(new Befehl("l", "LogOut", "SystemGruppe"));
		erlaubteBefehle.add(new Befehl("q", "Programm beenden", "SystemGruppe"));
	}

	public void fuegeZuWarenkorb(Artikel artikel) {
		warenkorb.add((Artikel) artikel);
	}

	public String toString() {
		return ("Kundennummer: " + getBenutzerIndex() + " / Benutzerame: " + getBenutzerName() + " / Vorname: "
				+ getVorname() + " / Nachname: " + getNachname() + " / Strasse: " + strasse + " / Plz: " + postleitzahl
				+ " / Wohnort: " + ort + " / Land: " + land);
	}

	public void loescheArtikelAusWarenkorb(Artikel artikel) {
		warenkorb.remove((Artikel) artikel);
	}

	public void loescheWarenkorb() {
		warenkorb.clear();
	}

	public String getStrasse() {
		return strasse;
	}

	public int getPostleitzahl() {
		return postleitzahl;
	}

	public String getOrt() {
		return ort;
	}

	public String getLand() {
		return land;
	}

	public List<Artikel> getWarenkorb() {
		return warenkorb;
	}
}