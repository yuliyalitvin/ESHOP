package domain;

import java.io.IOException;
import java.util.Vector;

import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;

import java.util.Iterator;
import java.util.List;

import persistence.FilePersistenceManager;
import persistence.PersistenceManager;
import valueObjects.Artikel;
import valueObjects.Massengutartikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class ArtikelVerwaltung {

	public ArtikelVerwaltung(String datei) {
	}

	// Verwaltung des Artikelbestands in einer verketteten Liste
	private static List<Artikel> artikelBestand = new Vector<Artikel>();

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	public void liesDaten(String datei) throws IOException {
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);

		Artikel einArtikel;

		do {
			einArtikel = pm.ladeArtikel();
			if (einArtikel != null) {
				einfuegen(einArtikel);
			}
		} while (einArtikel != null);

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	public void schreibeDaten(String datei) throws IOException {
		// PersistenzManager fuer schreibvorg�nge �ffnen
		pm.openForWriting(datei);

		if (!artikelBestand.isEmpty()) {
			Iterator<Artikel> iter = artikelBestand.iterator();
			while (iter.hasNext()) {
				Artikel a = iter.next();
				pm.speichereArtikel(a);
			}
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	public void einfuegen(Artikel einArtikel) {
		artikelBestand.add(einArtikel);
	}

	public void einfuegen(Massengutartikel einMassengutrtikel) {
		artikelBestand.add(einMassengutrtikel);
	}

//	public void loeschen(Artikel einArtikel) {
//		artikelBestand.remove(einArtikel);
//	}

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

	public List<Artikel> getArtikelBestand() {
		return new Vector<Artikel>(artikelBestand);
	}

	public void stueckImWarenkorb(int stueckNeu, Artikel artikel)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		int anzahl = artikel.getAnzahl();
		int stueck = artikel.getStueck();
		if (artikel.isMassengut()) {
			if (anzahl >= stueckNeu) {
				int pack = artikel.getPack();
				if ((stueckNeu % pack) == 0) {
					stueck = stueckNeu / pack;
					artikel.setStueck(stueck);
					artikel.setImKorb(true);
				} else {
					throw new StueckzahlEntsprichtNichtPackungException(stueckNeu, pack);
				}
			} else {
				throw new StueckzahlException(anzahl, stueckNeu);
			}
		} else {
			if (anzahl >= stueckNeu) {
				artikel.setStueck(stueckNeu);
				artikel.setImKorb(true);
			} else {
				throw new StueckzahlException(anzahl, stueckNeu);
			}
			if (stueckNeu == 0) {
				artikel.setImKorb(false);
			}
		}

	}

	public void bestandErhoehen(int erhoehung, Artikel artikel) throws StueckzahlEntsprichtNichtPackungException {
		int anzahl = artikel.getAnzahl();
		boolean verfuegbar = artikel.isVerfuegbar();

		if (artikel.isMassengut()) {
			int pack = artikel.getPack();
			if ((erhoehung % pack) == 0) {
				anzahl += erhoehung;
				artikel.setAnzahl(anzahl);
			} else {
				throw new StueckzahlEntsprichtNichtPackungException(erhoehung, pack);
			}
		} else {
			anzahl += erhoehung;
			artikel.setAnzahl(anzahl);
		}
		if (!verfuegbar) {
			artikel.setVerfuegbar(true);
		}
	}

	public void bestandAbziehen(Artikel artikel) {
		int anzahl = artikel.getAnzahl();
		int stueck = artikel.getStueck();

		if (artikel.isMassengut()) {
			int pack = artikel.getPack();
			anzahl -= (stueck * pack);
			artikel.setAnzahl(anzahl);

		} else {
			anzahl -= stueck;
			artikel.setAnzahl(anzahl);
		}

		if (anzahl == 0) {
			artikel.setVerfuegbar(false);
		}
		if (stueck == 0) {
			artikel.setImKorb(false);
		}
	}
}
