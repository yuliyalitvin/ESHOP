package domain;

import java.io.IOException;
import java.util.Vector;

import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;

import java.util.Iterator;
import java.util.List;

import persistence.FilePersistenceManager;
import persistence.PersistenceManager;
import valueObjects.Artikel;
import valueObjects.Massengutartikel;

/**
 * 
 * Verwaltung die alle Methoden fuer Artikel verwaltet
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class ArtikelVerwaltung {

	private static List<Artikel> artikelBestand = new Vector<Artikel>();
	private PersistenceManager pm = new FilePersistenceManager();

	public ArtikelVerwaltung(String datei) {

	}

	/**
	 * Methode, lies Daten aus der Textdatei
	 *
	 * @param datei Datenquelle wo alle Werte gespeichert sind
	 */
	public synchronized void liesDaten(String datei) throws IOException {
		pm.openForReading(datei);

		Artikel einArtikel;

		do {
			einArtikel = pm.ladeArtikel();
			if (einArtikel != null) {
				einfuegen(einArtikel);
			}
		} while (einArtikel != null);

		pm.close();
	}

	/**
	 * Methode, speichert Daten in der Textdatei
	 *
	 * @param datei Datenquelle wo alle Werte gespeichert sind
	 */
	public synchronized void schreibeDaten(String datei) throws IOException {
		pm.openForWriting(datei);

		if (!artikelBestand.isEmpty()) {
			Iterator<Artikel> iter = artikelBestand.iterator();
			while (iter.hasNext()) {
				Artikel a = iter.next();
				pm.speichereArtikel(a);
			}
		}
		pm.close();
	}

	/**
	 * Methode, fuegt neuen Artikel in den Bestand
	 *
	 * @param einArtikel neuer Artikel
	 */
	public void einfuegen(Artikel einArtikel) {
		artikelBestand.add(einArtikel);
	}

	/**
	 * Methode, fuegt neuen Massengutartikel in den Bestand
	 *
	 * @param einMassengutrtikel neuer Massengutrtikel
	 */
	public void einfuegen(Massengutartikel einMassengutrtikel) {
		artikelBestand.add(einMassengutrtikel);
	}

	/**
	 * Methode, sucht artikelbestand nach bestimmten suchbegriff
	 *
	 * @param artikelName suchbegriff
	 * @return ergebnis Liste der gefundenen Artikel
	 * @throws ArtikelNichtGefundenException wenn Artikel nicht gefunden wird
	 */
	public List<Artikel> sucheArtikel(String artikelName) throws ArtikelNichtGefundenException {
		List<Artikel> ergebnis = new Vector<Artikel>();
		Iterator<Artikel> it = artikelBestand.iterator();

		while (it.hasNext()) {
			Artikel artikel = it.next();
			if (artikel.getArtikelName().equalsIgnoreCase(artikelName)) {
				ergebnis.add(artikel);
			}
		}
		if (ergebnis.isEmpty()) {
			throw new ArtikelNichtGefundenException();
		}
		return ergebnis;
	}

	/**
	 * Methode, sucht artikelbestand nach bestimmten suchbegriff
	 *
	 * @param artikelNummer suchbegriff
	 * @return ergebnis was aus der Suche gefunden wurde
	 */
	public Artikel sucheArtikel(int artikelNummer) {
		Artikel ergebnis = null;
		Iterator<Artikel> it = artikelBestand.iterator();

		while (it.hasNext()) {
			Artikel artikel = it.next();
			if (artikel.getArtikelNummer() == artikelNummer) {
				ergebnis = artikel;
				break;
			}
		}
		return ergebnis;
	}

	/**
	 * Methode, sucht artikelbestand nach bestimmten suchbegriff
	 *
	 * @param artikelName suchbegriff
	 * @return ergebnis was aus der Suche gefunden wurde
	 */
	public Artikel sucheArtikelNachName(String artikelName) {
		Artikel ergebnis = null;
		Iterator<Artikel> it = artikelBestand.iterator();

		while (it.hasNext()) {
			Artikel artikel = it.next();
			if (artikel.getArtikelName() == artikelName) {
				ergebnis = artikel;
				break;
			}
		}
		return ergebnis;
	}

	/**
	 * Methode, gibt ganzen Artikelliste wieder
	 *
	 * @return artikelBestand gesammte Artikelliste
	 */
	public List<Artikel> getArtikelBestand() {
		return new Vector<Artikel>(artikelBestand);
	}

	/**
	 * Methode, erhoeht die Anzahl des Artikels
	 *
	 * @param erhoehung wert zum erhohen des Artikel
	 * @param artikel   der zu erhoende Artikel
	 * @throws StueckzahlEntsprichtNichtPackungException wenn Stueckzahl nicht so gross wie eine Packung ist
	 **/
	public synchronized void bestandErhoehen(int erhoehung, Artikel artikel)
			throws StueckzahlEntsprichtNichtPackungException {
		int anzahl = artikel.getAnzahl();
		boolean verfuegbar = artikel.isVerfuegbar();

		if (artikel.isMassengut()) {
			int pack = artikel.getPack();
			if ((erhoehung % pack) == 0) {
				anzahl += erhoehung;
				artikel.setAnzahl(anzahl);
			} else {
				throw new StueckzahlEntsprichtNichtPackungException();
			}
		} else {
			anzahl += erhoehung;
			artikel.setAnzahl(anzahl);
		}
		if (!verfuegbar) {
			artikel.setVerfuegbar(true);
		}
	}

	/**
	 * Methode, zieht die Anzahl des Artikel ab
	 *
	 * @param artikel           der abzuziehende Artikel
	 * @param anzahlImWarenkorb wert zum abziehen des Artikel
	 */
	public synchronized void bestandAbziehen(Artikel artikel, int anzahlImWarenkorb) {
		int anzahl = artikel.getAnzahl();
		int stueck = artikel.getStueck();

		if (artikel.isMassengut()) {
			int pack = artikel.getPack();
			anzahl -= (anzahlImWarenkorb * pack);
			artikel.setAnzahl(anzahl);

			stueck -= anzahlImWarenkorb;
			artikel.setStueck(stueck);
		} else {
			anzahl -= anzahlImWarenkorb;
			artikel.setAnzahl(anzahl);

			stueck -= anzahlImWarenkorb;
			artikel.setStueck(stueck);
		}

		if (anzahl == 0) {
			artikel.setVerfuegbar(false);
		}
	}

}
