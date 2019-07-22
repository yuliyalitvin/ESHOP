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
import valueObjects.Benutzer;
import valueObjects.Kunde;
import valueObjects.Mitarbeiter;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class BenutzerVerwaltung {

	private List<Kunde> vorhandeneKunden = new Vector<Kunde>();
	private List<Mitarbeiter> vorhandeneMitarbeiter = new Vector<Mitarbeiter>();

	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	// Kunde
	public void liesDatenKunde(String datei) throws IOException {
		// PersistenzManager fuer Lesevorgaenge oeffnen
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
					// Kann hier eigentlich nicht auftreten, daher auch keine Fehlerbehandlung...
				}
			}
		} while (einKunde != null);
		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}

	public void schreibeDatenKunde(String datei) throws IOException {
		// PersistenzManager fuer Schreibvorgaenge oeffnen
		pm.openForWriting(datei);

		if (!vorhandeneKunden.isEmpty()) {
			Iterator<Kunde> iter = vorhandeneKunden.iterator();
			while (iter.hasNext()) {
				Kunde k = iter.next();
				pm.speichereKunde(k);
			}
		}
		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}

	public void einfuegenKunde(Kunde einKunde) throws BenutzerExistiertBereitsException {
		// das übernimmt die BenutzerListe:
		vorhandeneKunden.add(einKunde);
		int benutzerIndex = vorhandeneKunden.size();
		einKunde.setBenutzerIndex(benutzerIndex);
	}

	public List<Kunde> getVorhandeneKunden() {
		return new Vector<Kunde>(vorhandeneKunden);
	}

	// Mitarbeiter
	public void liesDatenMitarbeiter(String datei) throws IOException {
		// PersistenzManager fuer Lesevorgaenge oeffnen
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
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				}
			}
		} while (einMitarbeiter != null);

		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}

	public void schreibeDatenMitarbeiter(String datei) throws IOException {
		// PersistenzManager fuer Schreibvorgaenge oeffnen
		pm.openForWriting(datei);

		if (!vorhandeneMitarbeiter.isEmpty()) {
			Iterator<Mitarbeiter> iter = vorhandeneMitarbeiter.iterator();
			while (iter.hasNext()) {
				Mitarbeiter m = iter.next();
				pm.speichereMitarbeiter(m);
			}
		}

		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}

	public void einfuegenMitarbeiter(Mitarbeiter einMitarbeiter) throws BenutzerExistiertBereitsException {
		// das uebernimmt die BenutzerListe:
		vorhandeneMitarbeiter.add(einMitarbeiter);
		int benutzerIndex = vorhandeneMitarbeiter.size();
		einMitarbeiter.setBenutzerIndex(benutzerIndex);
	}

	// Benutzer suchen
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

	public List<Mitarbeiter> getVorhandeneMitarbeiter() {
		return new Vector<Mitarbeiter>(vorhandeneMitarbeiter);
	}

	// Warenkorb
	public void fuegeZuWarenkorb(Artikel artikel, int benutzerIndex) {
		Benutzer einBenutzer = getKunde(benutzerIndex);
		einBenutzer.fuegeZuWarenkorb(artikel);
	}

	public void loescheArtikelAusWarenkorb(Artikel artikel, int benutzerIndex)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		Benutzer einBenutzer = getKunde(benutzerIndex);
		einBenutzer.loescheArtikelAusWarenkorb(artikel);
	}

	public void loescheWarenkorb(int benutzerIndex) {
		Benutzer einBenutzer = getKunde(benutzerIndex);
		einBenutzer.loescheWarenkorb();
	}

	public Kunde getKunde(int benutzerIndex) {
		return vorhandeneKunden.get(benutzerIndex - 1);
	}

	public Mitarbeiter getMitarbeiter(int benutzerIndex) {
		return vorhandeneMitarbeiter.get(benutzerIndex - 1);
	}

	public Vector<Artikel> getWarenkorb(int benutzerIndex) {
		return (Vector<Artikel>) vorhandeneKunden.get(benutzerIndex - 1).getWarenkorb();
	}

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
