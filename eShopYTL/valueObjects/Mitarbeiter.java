package valueObjects;

import java.util.List;
import java.util.Vector;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Mitarbeiter extends Benutzer {

	private List<Artikel> warenkorb;

	public Mitarbeiter(String benutzerName, boolean kunde, String passwort, String vorname, String nachname) {

		super(benutzerName, kunde, passwort, vorname, nachname);

		warenkorb = new Vector<Artikel>();

		erlaubteBefehle.add(new Befehl("a", "Artikelliste ausgeben", "ArtikelGruppe"));
		// erlaubteBefehle.add(new Befehl("ad", "Artikel loeschen", "ArtikelGruppe"));
		erlaubteBefehle.add(new Befehl("ae", "Artikel hinzufuegen", "ArtikelGruppe"));
		erlaubteBefehle.add(new Befehl("af", "Artikel suchen", "ArtikelGruppe"));
		erlaubteBefehle.add(new Befehl("ac", "Artikel sortieren", "ArtikelGruppe"));

		erlaubteBefehle.add(new Befehl("ab", "Artikel nach Bezeichnung sortieren", "ArtikelSortierenGruppe"));
		erlaubteBefehle.add(new Befehl("abr", "Artikel nach Bezeichnung sortieren (rueckwaerts)", "ArtikelSortierenGruppe"));
		erlaubteBefehle.add(new Befehl("an", "Artikel nach Nummer sortieren", "ArtikelSortierenGruppe"));
		erlaubteBefehle.add(new Befehl("anr", "Artikel nach Nummer sortieren (rueckwaerts)", "ArtikelSortierenGruppe"));

		erlaubteBefehle.add(new Befehl("b", "Benutzerliste ausgeben", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("be", "Mitarbeiter hinzufuegen", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("bf", "Benutzer suchen", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("h", "Historie anzeigen", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("ha", "Historie eines Artikels ansehen", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("bha", "Bestandshistorie eines Artikel fuer letzten 30 Tage ansehen", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("ah", "Bestand eines Artikels erhoehen", "BenutzerGruppe"));
		erlaubteBefehle.add(new Befehl("l", "LogOut", "SystemGruppe"));
		erlaubteBefehle.add(new Befehl("q", "Programm beenden", "SystemGruppe"));
	}

	public void fuegeZuWarenkorb(Artikel artikel) {
		warenkorb.add((Artikel) artikel);
	}

	public String toString() {
		return ("Mitarbeiternummer: " + getBenutzerIndex() + " / Benutzerame: " + getBenutzerName() + " / Vorname: "
				+ getVorname() + " / Nachname: " + getNachname());
	}

	public void loescheArtikelAusWarenkorb(Artikel artikel) {
		warenkorb.remove((Artikel) artikel);
	}

	public void loescheWarenkorb() {
		warenkorb.clear();
	}

	public List<Artikel> getWarenkorb() {
		return warenkorb;
	}
}