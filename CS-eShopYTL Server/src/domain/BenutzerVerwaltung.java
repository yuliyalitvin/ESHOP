package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import domain.exceptions.BenutzerExistiertBereitsException;
import domain.exceptions.BenutzerNichtGefundenException;
import domain.exceptions.LoginBenutzerNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import persistence.FilePersistenceManager;
import persistence.PersistenceManager;
import valueObjects.Artikel;
import valueObjects.ArtikelImWarenkorb;
import valueObjects.Benutzer;
import valueObjects.Kunde;
import valueObjects.Mitarbeiter;

/**
 * Verwaltung aller Funktionen eines Benutzers
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */
public class BenutzerVerwaltung {

	private List<Kunde> vorhandeneKunden = new Vector<Kunde>();
	private List<Mitarbeiter> vorhandeneMitarbeiter = new Vector<Mitarbeiter>();

	private PersistenceManager pm = new FilePersistenceManager();

	// Kunde

	/**
	 * Methode zum einlesen aller Kundendaten
	 */
	public synchronized void liesDatenKunde(String datei) throws IOException {
		pm.openForReading(datei);

		Kunde einKunde;
		do {
			// Benutzer-Objekt einlesen
			einKunde = pm.ladeKunde();
			if (einKunde != null) {
				// Benutzer in Liste einfuegen
				try {
					einfuegenKunde(einKunde);
				} catch (BenutzerExistiertBereitsException e1) {
				}
			}
		} while (einKunde != null);
		pm.close();
	}

	/**
	 * Methode zum speichern aller Kunden
	 */
	public synchronized void schreibeDatenKunde(String datei) throws IOException {
		pm.openForWriting(datei);

		if (!vorhandeneKunden.isEmpty()) {
			Iterator<Kunde> iter = vorhandeneKunden.iterator();
			while (iter.hasNext()) {
				Kunde k = iter.next();
				pm.speichereKunde(k);
			}
		}
		pm.close();
	}

	/**
	 * Methode zum einfuegen eines Kunden
	 * 
	 * @throws BenutzerExistiertBereitsException falls Name bereits belegt
	 */
	public synchronized void einfuegenKunde(Kunde einKunde) throws BenutzerExistiertBereitsException {
		vorhandeneKunden.add(einKunde);
		int benutzerIndex = vorhandeneKunden.size();
		einKunde.setBenutzerIndex(benutzerIndex);
	}

	public List<Kunde> getVorhandeneKunden() {
		return new Vector<Kunde>(vorhandeneKunden);
	}

	// Mitarbeiter

	/**
	 * Methode zum laden aller Mitarbeiter
	 */
	public synchronized void liesDatenMitarbeiter(String datei) throws IOException {
		pm.openForReading(datei);

		Mitarbeiter einMitarbeiter;
		do {
			// Benutzer-Objekt einlesen
			einMitarbeiter = pm.ladeMitarbeiter();
			if (einMitarbeiter != null) {
				// Benutzer in Liste einfuegen
				try {
					einfuegenMitarbeiter(einMitarbeiter);
				} catch (BenutzerExistiertBereitsException e1) {
				}
			}
		} while (einMitarbeiter != null);

		pm.close();
	}

	/**
	 * Methode zum speichern eines Mitarbeiters
	 */
	public synchronized void schreibeDatenMitarbeiter(String datei) throws IOException {
		pm.openForWriting(datei);

		if (!vorhandeneMitarbeiter.isEmpty()) {
			Iterator<Mitarbeiter> iter = vorhandeneMitarbeiter.iterator();
			while (iter.hasNext()) {
				Mitarbeiter m = iter.next();
				pm.speichereMitarbeiter(m);
			}
		}

		pm.close();
	}

	/**
	 * Methode zum einfuegen eines Mitarbeiters
	 * 
	 * @throws BenutzerExistiertBereitsException falls Name bereits belegt
	 */
	public synchronized void einfuegenMitarbeiter(Mitarbeiter einMitarbeiter) throws BenutzerExistiertBereitsException {
		// das uebernimmt die BenutzerListe:
		vorhandeneMitarbeiter.add(einMitarbeiter);
		int benutzerIndex = vorhandeneMitarbeiter.size();
		einMitarbeiter.setBenutzerIndex(benutzerIndex);
	}

	/**
	 * Methode zum suchen des einloggenden Benutzers
	 * 
	 * @return gefundenen Mitarbeiter zurueckgeben
	 * @throws LoginBenutzerNichtGefundenException falls nichts gefunden
	 */
	public Benutzer sucheLogin(String benutzerName, String benutzerPasswort)
			throws LoginBenutzerNichtGefundenException {

		Benutzer gesuchterBenutzer = null;
		Iterator<Kunde> it = vorhandeneKunden.iterator();

		while (it.hasNext()) {
			Kunde k = it.next();
			if (k.getBenutzerName().equals(benutzerName)) {
				if (k.getPasswort().equals(benutzerPasswort)) {
					gesuchterBenutzer = k;
				}
			}
		}

		if (gesuchterBenutzer == null) {
			gesuchterBenutzer = sucheMitarbeiterLogin(benutzerName, benutzerPasswort);
		}
		return gesuchterBenutzer;
	}

	/**
	 * Methode zum suchen eines Benutzers in der Mitarbeiterliste fuer Login
	 * 
	 * @return gefundenen Mitarbeiter zurueckgeben
	 * @throws LoginBenutzerNichtGefundenException falls nichts gefunden
	 */
	public Benutzer sucheMitarbeiterLogin(String benutzerName, String benutzerPasswort)
			throws LoginBenutzerNichtGefundenException {

		Benutzer gesuchterMitarbeiter = null;
		Iterator<Mitarbeiter> it = vorhandeneMitarbeiter.iterator();

		while (it.hasNext()) {
			Mitarbeiter m = it.next();
			if (m.getBenutzerName().equals(benutzerName)) {
				if (m.getPasswort().equals(benutzerPasswort)) {
					gesuchterMitarbeiter = m;
				}
			}
		}

		if (gesuchterMitarbeiter == null) {
			throw new LoginBenutzerNichtGefundenException();
		}
		return gesuchterMitarbeiter;
	}

	/**
	 * Methode zum suchen eines Benutzers
	 * 
	 * @return ergebnis gefundenen Kunden zurueckgeben
	 * @throws BenutzerNichtGefundenException falls nichts gefunden
	 */
	public List<Benutzer> sucheBenutzer(String benutzerName) throws BenutzerNichtGefundenException {
		List<Benutzer> ergebnis = new Vector<Benutzer>();
		Iterator<Kunde> it = vorhandeneKunden.iterator();

		while (it.hasNext()) {
			Kunde k = it.next();
			if (k.getBenutzerName().equalsIgnoreCase(benutzerName)) {
				ergebnis.add(k);
			}
		}
		if (ergebnis.isEmpty()) {
			ergebnis = sucheBenutzerMitarbeiter(benutzerName);
		}

		return ergebnis;
	}

	/**
	 * Methode zum suchen eines Benutzers, falls in der Kundenliste nichts gefunden
	 * 
	 * @return ergebnis gefundenen Mitarbeiter zurueckgeben
	 * @throws BenutzerNichtGefundenException falls nichts gefunden
	 */
	public List<Benutzer> sucheBenutzerMitarbeiter(String benutzerName) throws BenutzerNichtGefundenException {
		List<Benutzer> ergebnis = new Vector<Benutzer>();
		Iterator<Mitarbeiter> it = vorhandeneMitarbeiter.iterator();

		while (it.hasNext()) {
			Mitarbeiter m = it.next();
			if (m.getBenutzerName().equalsIgnoreCase(benutzerName)) {
				ergebnis.add(m);
			}
		}
		if (ergebnis.isEmpty()) {
			throw new BenutzerNichtGefundenException();
		}

		return ergebnis;
	}

	/**
	 * Methode zum pruefen ob ein Benutzer selben namen hat eines anderen Benutzers
	 * 
	 * @return ergebnis - eingefuegter Benutzer / gefundener Benutzer
	 */
	public List<Benutzer> sucheBenutzerEinfuegen(String benutzerName) {
		List<Benutzer> ergebnis = new Vector<Benutzer>();
		Iterator<Kunde> it = vorhandeneKunden.iterator();

		while (it.hasNext()) {
			Kunde k = it.next();
			if (k.getBenutzerName().equals(benutzerName)) {
				ergebnis.add(k);
			}
		}
		if (ergebnis.isEmpty()) {
			ergebnis = sucheBenutzerMitarbeiterEinfuegen(benutzerName);
		}

		return ergebnis;
	}

	/**
	 * Methode zum pruefen ob ein Benutzer selben namen hat eines anderen Benutzers,
	 * falls bei Kunden nichts gefunden
	 * 
	 * @return ergebnis - eingefuegter Benutzer / gefundener Benutzer
	 */
	public List<Benutzer> sucheBenutzerMitarbeiterEinfuegen(String benutzerName) {
		List<Benutzer> ergebnis = new Vector<Benutzer>();
		Iterator<Mitarbeiter> it = vorhandeneMitarbeiter.iterator();

		while (it.hasNext()) {
			Mitarbeiter m = it.next();
			if (m.getBenutzerName().equals(benutzerName)) {
				ergebnis.add(m);
			}
		}
		return ergebnis;
	}

	/**
	 * Methode zum zeigen des Warenkorbs
	 * 
	 * @return warenliste - Inhalte im Warenkorb
	 */
	public String zeigeWarenkorb(Vector<Artikel> warenkorb) {
		String warenliste = "\n";
		if (warenkorb.isEmpty()) {
			warenliste = "Der Warenkorb ist leer.";
		} else {
			for (Artikel artikel : warenkorb) {
				warenliste += artikel.toString() + "\n";
			}
		}
		return warenliste;
	}

	/**
	 * Methode zum bekommen aller Mitarbeiter
	 * 
	 * @return alle mitarbeiter
	 */
	public List<Mitarbeiter> getVorhandeneMitarbeiter() {
		return new Vector<Mitarbeiter>(vorhandeneMitarbeiter);
	}

	/**
	 * Methode zum pruefen ob ein Artikel im Warenkorb ist
	 * 
	 * @return istImKorb - Boolean ob es wahr oder falsch ist
	 */
	public boolean istImKorb(int artikelNummer, int benutzerIndex) {

		boolean istImKorb = false;
		Kunde kunde = getKunde(benutzerIndex);
		istImKorb = kunde.istImKorb(artikelNummer);
		return istImKorb;
	}

	/**
	 * Methode zum aendern der Stückzahl der Artikel im Warenkorb
	 */
	public synchronized void anzahlImKorbAendern(int artikelNummer, int benutzerIndex, int stueck)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		Kunde kunde = getKunde(benutzerIndex);
		kunde.anzahlImKorbAendern(artikelNummer, stueck);
	}

	/**
	 * Methode zum suchen von Artikeln im Warenkorb
	 * 
	 * @return gesuchter Artikel
	 */
	public ArtikelImWarenkorb sucheArtikelImWarenkorb(int artikelNummer, int benutzerIndex) {
		Kunde kunde = getKunde(benutzerIndex);
		return kunde.sucheArtikelImWarenkorb(artikelNummer);
	}

	/**
	 * Methode zum einfuegen eines Artikels in den Warenkorb
	 */
	public synchronized void fuegeZuWarenkorb(Artikel artikel, int benutzerIndex, int stueck)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		Kunde einBenutzer = getKunde(benutzerIndex);
		einBenutzer.fuegeZuWarenkorb(artikel, stueck);
	}

	/**
	 * Methode zum loeschen eines Artikels aus dem Warenkorb
	 */
	public synchronized void loescheArtikelAusWarenkorb(ArtikelImWarenkorb artikel, int benutzerIndex)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		Kunde einBenutzer = getKunde(benutzerIndex);
		einBenutzer.loescheArtikelAusWarenkorb(artikel);
	}

	/**
	 * Methode zum bekommen des Kunden (& dessen Index)
	 */
	public Kunde getKunde(int benutzerIndex) {
		return vorhandeneKunden.get(benutzerIndex - 1);
	}

	/**
	 * Methode zum bekommen des Mitarbeiter (& dessen Index)
	 */
	public Mitarbeiter getMitarbeiter(int benutzerIndex) {
		return vorhandeneMitarbeiter.get(benutzerIndex - 1);
	}

	/**
	 * Methode zum bekommen des Warenkorbes der Person
	 */
	public Vector<ArtikelImWarenkorb> getWarenkorb(int benutzerIndex) {
		return (Vector<ArtikelImWarenkorb>) vorhandeneKunden.get(benutzerIndex - 1).getWarenkorb();
	}

	/**
	 * Methode zum bekommen des Benutzers
	 */
	public Benutzer getBenutzer(int benutzerIndex, boolean istKunde) {
		Benutzer benutzer = null;
		if (istKunde == false) {
			benutzer = vorhandeneMitarbeiter.get(benutzerIndex - 1);
		} else {
			benutzer = vorhandeneKunden.get(benutzerIndex - 1);
		}
		return benutzer;
	}

}
