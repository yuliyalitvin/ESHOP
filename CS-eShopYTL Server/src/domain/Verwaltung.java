package domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import domain.exceptions.ArtikelExistiertBereitsException;
import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.BenutzerExistiertBereitsException;
import domain.exceptions.BenutzerNichtGefundenException;
import domain.exceptions.LoginBenutzerNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import valueObjects.AnzeigeEreignis;
import valueObjects.Artikel;
import valueObjects.ArtikelImWarenkorb;
import valueObjects.Benutzer;
import valueObjects.Ereignis;
import valueObjects.Kunde;
import valueObjects.Mitarbeiter;
import valueObjects.Massengutartikel;

/**
 * Klasse zum weiterleiten der Aufgaben an die jeweilige Verwaltung
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Verwaltung {

	private String datei = "";

	BufferedReader reader = null;

	BufferedWriter writer = null;

	private ArtikelVerwaltung meineArtikel;
	private BenutzerVerwaltung meineBenutzer;
	private Historie historie;

	public Verwaltung(String datei) throws IOException {
		this.datei = datei;

		// Artikelbestand aus Datei einlesen
		meineArtikel = new ArtikelVerwaltung(datei);
		meineArtikel.liesDaten(datei + "_A.txt");

		meineBenutzer = new BenutzerVerwaltung();
		meineBenutzer.liesDatenKunde(datei + "_K.txt");
		meineBenutzer.liesDatenMitarbeiter(datei + "_M.txt");

		historie = new Historie("ESHOP_H.txt");
	}

	// Artikel
	public List<Artikel> gibAlleArtikel() {
		return meineArtikel.getArtikelBestand();
	}

	public List<Artikel> sucheNachArtikelname(String artikelName) throws ArtikelNichtGefundenException {
		return meineArtikel.sucheArtikel(artikelName);
	}

	public Artikel sucheArtikel(int artikelNummer) throws ArtikelNichtGefundenException {

		Artikel gesuchterArtikel = meineArtikel.sucheArtikel(artikelNummer);
		if (gesuchterArtikel == null) {
			throw new ArtikelNichtGefundenException();
		}
		return gesuchterArtikel;
	}

	public Artikel sucheArtikelNachName(String artikelName) throws ArtikelNichtGefundenException {

		Artikel gesuchterArtikel = meineArtikel.sucheArtikelNachName(artikelName);
		if (gesuchterArtikel == null) {
			throw new ArtikelNichtGefundenException();
		}
		return gesuchterArtikel;
	}

	public Artikel fuegeArtikelEin(String artikelName, int artikelNummer, float preis, int anzahl, boolean massengut,
			int pack) throws ArtikelExistiertBereitsException, StueckzahlEntsprichtNichtPackungException {
		Artikel neuerArtikel = null;

		Artikel sucheArtikel = meineArtikel.sucheArtikel(artikelNummer);
		if (sucheArtikel == null) {
			if (massengut) {
				if (anzahl % pack == 0) {
					neuerArtikel = new Massengutartikel(artikelName, artikelNummer, preis, anzahl, true, massengut,
							pack);
					meineArtikel.einfuegen(neuerArtikel);
				} else {
					throw new StueckzahlEntsprichtNichtPackungException();
				}
			} else {
				neuerArtikel = new Artikel(artikelName, artikelNummer, preis, anzahl, true, massengut);
				meineArtikel.einfuegen(neuerArtikel);
			}
		} else {
			throw new ArtikelExistiertBereitsException();
		}
		return neuerArtikel;
	}

	public void bestandErhoehen(int erhoehung, Artikel artikel) throws StueckzahlEntsprichtNichtPackungException {
		meineArtikel.bestandErhoehen(erhoehung, artikel);
	}

	public void bestandAbziehen(Artikel artikel, int anzahlImWarenkorb) {
		meineArtikel.bestandAbziehen(artikel, anzahlImWarenkorb);
	}

	public void schreibeArtikel() throws IOException {
		meineArtikel.schreibeDaten(datei + "_A.txt");
	}

	public List<Artikel> sortiereArtikelnachNummer(boolean bReverse) {
		List<Artikel> Artikels = meineArtikel.getArtikelBestand();

		if (bReverse) {
			Artikels.sort(Comparator.comparing(Artikel::getArtikelNummer).reversed());
		} else {
			Artikels.sort(Comparator.comparing(Artikel::getArtikelNummer));
		}

		return Artikels;
	}

	public List<Artikel> sortiereArtikelName(boolean bReverse) {
		List<Artikel> Artikels = meineArtikel.getArtikelBestand();

		if (bReverse) {
			Artikels.sort(Comparator.comparing(Artikel::getArtikelName).reversed());
		} else {
			Artikels.sort(Comparator.comparing(Artikel::getArtikelName));
		}

		return Artikels;
	}

	public ArtikelVerwaltung getArtikelListe() {
		return meineArtikel;
	}

	// Benutzer
	public Benutzer sucheNachBenutzernameLogin(String benutzerName, String benutzerPasswort)
			throws LoginBenutzerNichtGefundenException {
		return meineBenutzer.sucheLogin(benutzerName, benutzerPasswort);
	}

	public List<Benutzer> sucheBenutzerNachBenutzername(String benutzerName) throws BenutzerNichtGefundenException {
		return meineBenutzer.sucheBenutzer(benutzerName);
	}

	public Mitarbeiter getMitarbeiter(int benutzerIndex) {
		return meineBenutzer.getMitarbeiter(benutzerIndex);
	}

	public List<Benutzer> gibAlleBenutzer() {
		List<Mitarbeiter> mitarbeiter = meineBenutzer.getVorhandeneMitarbeiter();
		List<Kunde> kunden = meineBenutzer.getVorhandeneKunden();

		List<Benutzer> benutzer = new Vector<Benutzer>();
		benutzer.addAll(mitarbeiter);
		benutzer.addAll(kunden);

		return benutzer;
	}

	public BenutzerVerwaltung getBenutzerListe() {
		return meineBenutzer;
	}

	// Kunde
	public List<Kunde> gibAlleKunden() {
		return meineBenutzer.getVorhandeneKunden();
	}

	public void fuegeKundeEin(String benutzerName, boolean kunde, String passwort, String vorname, String nachname,
			String strasse, int postleitzahl, String ort, String land) throws BenutzerExistiertBereitsException {

		Kunde neuerKunde = null;

		List<Benutzer> sucheBenutzer = meineBenutzer.sucheBenutzerEinfuegen(benutzerName);
		if (sucheBenutzer.isEmpty()) {
			neuerKunde = new Kunde(benutzerName, kunde, passwort, vorname, nachname, strasse, postleitzahl, ort, land);
			meineBenutzer.einfuegenKunde(neuerKunde);
		} else {
			throw new BenutzerExistiertBereitsException();
		}
	}

	public void schreibeKunde() throws IOException {
		meineBenutzer.schreibeDatenKunde(datei + "_K.txt");
	}

	// Mitarbeiter
	public List<Mitarbeiter> gibAlleMitarbeiter() {
		return meineBenutzer.getVorhandeneMitarbeiter();
	}

	public void schreibeMitarbeiter() throws IOException {
		meineBenutzer.schreibeDatenMitarbeiter(datei + "_M.txt");
	}

	public Benutzer fuegeMitarbeiterEin(String benutzerName, boolean kunde, String passwort, String vorname,
			String nachname) throws BenutzerExistiertBereitsException {
		Mitarbeiter neuerMitarbeiter = null;

		List<Benutzer> sucheBenutzer = meineBenutzer.sucheBenutzerEinfuegen(benutzerName);
		if (sucheBenutzer.isEmpty()) {
			neuerMitarbeiter = new Mitarbeiter(benutzerName, kunde, passwort, vorname, nachname);
			meineBenutzer.einfuegenMitarbeiter(neuerMitarbeiter);
		} else {
			throw new BenutzerExistiertBereitsException();
		}
		return neuerMitarbeiter;
	}

	// Warenkorb

	public boolean istImKorb(int artikelNummer, int benutzerIndex) {
		return meineBenutzer.istImKorb(artikelNummer, benutzerIndex);
	}

	public void anzahlImKorbAendern(int artikelNummer, int benutzerIndex, int stueck)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		meineBenutzer.anzahlImKorbAendern(artikelNummer, benutzerIndex, stueck);
	}

	public ArtikelImWarenkorb sucheArtikelImWarenkorb(int artikelNummer, int benutzerIndex) {
		return meineBenutzer.sucheArtikelImWarenkorb(artikelNummer, benutzerIndex);
	}

	public void loescheArtikelAusWarenkorb(ArtikelImWarenkorb artikel, int benutzerIndex)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		meineBenutzer.loescheArtikelAusWarenkorb(artikel, benutzerIndex);
	}

	public void fuegeZuWarenkorb(Artikel artikel, int benutzerIndex, int stueck)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		meineBenutzer.fuegeZuWarenkorb(artikel, benutzerIndex, stueck);
	}

	public List<ArtikelImWarenkorb> gibWarenkorb(int benutzerIndex) {
		return meineBenutzer.getWarenkorb(benutzerIndex);
	}

	public String zeigeWarenkorb(Vector<Artikel> warenkorb) {
		return meineBenutzer.zeigeWarenkorb(warenkorb);
	}

	// Historie

	public void schreibeHistorie() throws IOException {
		historie.schreibeHistorie();
	}

	public void addHistorieEreignis(int artikelNummer, int anzahl, int ereignisArt, int benutzerIndex) {
		historie.addEreignis(artikelNummer, anzahl, ereignisArt, benutzerIndex);
	}

	public String historieAusgeben(BenutzerVerwaltung benutzerListe, ArtikelVerwaltung artikelListe) {
		return historie.historieAusgeben(benutzerListe, artikelListe);
	}

	public String artikelHistorieAusgabe(Artikel gewuenschterArtikel, BenutzerVerwaltung benutzerListe) {
		return historie.artikelHistorieAusgabe(gewuenschterArtikel, benutzerListe);
	}

	public String artikelBestandsHistorieAusgabe(Artikel gewuenschterArtikel) {
		return historie.artikelBestandsHistorieAusgabe(gewuenschterArtikel);
	}

	public Historie getHistorie() {
		return historie;
	}

	public List<AnzeigeEreignis> getAnzeigeEreignisListe() {

		List<AnzeigeEreignis> anzeigeEreignisListe = new Vector<AnzeigeEreignis>();

		BenutzerVerwaltung benutzerListe = getBenutzerListe();
		ArtikelVerwaltung artikelListe = getArtikelListe();

		for (Ereignis ereignis : historie.getEreignisListe()) {
			AnzeigeEreignis anzeigeEreignis = new AnzeigeEreignis(ereignis);

			boolean istKunde = false;

			switch (ereignis.getEreignisArt()) {
			case 1:
				istKunde = false;
				break;
			case 2:
				istKunde = false;
				break;
			case 3:
				istKunde = true;
				break;
			}

			int benutzerIndex = ereignis.getPersonIndex();
			Benutzer benutzer = benutzerListe.getBenutzer(benutzerIndex, istKunde);
			anzeigeEreignis.setBenutzerName(benutzer.getBenutzerName());

			Artikel artikel = artikelListe.sucheArtikel(ereignis.getArtikelNummer());

			anzeigeEreignis.setArtikelName(artikel.getArtikelName());

			anzeigeEreignisListe.add(anzeigeEreignis);
		}

		return anzeigeEreignisListe;
	}

}
