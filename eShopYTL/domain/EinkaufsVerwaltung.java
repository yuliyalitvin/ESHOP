package domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import domain.exceptions.ArtikelExistiertBereitsException;
import persistence.FilePersistenceManager;
import persistence.PersistenceManager;
import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class EinkaufsVerwaltung {

	// Verwaltung des Artikelbestands in einer verketteten Liste
	private List<Artikel> artikelBestand = new Vector<Artikel>();

	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	public void liesDaten(String datei) throws IOException {
		// PersistenzManager fuer Lesevorgaenge oeffnen
		pm.openForReading(datei);

		Artikel einArtikel;
		do {
			// Artikel-Objekt einlesen
			einArtikel = pm.ladeArtikel();
			if (einArtikel != null) {
				// Artikel in Liste einfuegen
				try {
					einfuegen(einArtikel);
				} catch (ArtikelExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				}
			}
		} while (einArtikel != null);
		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}

	public void schreibeDaten(String datei) throws IOException {
		// PersistenzManager fuer Schreibvorgaenge oeffnen
		pm.openForWriting(datei);

		if (!artikelBestand.isEmpty()) {
			Iterator<Artikel> iter = artikelBestand.iterator();
			while (iter.hasNext()) {
				Artikel b = iter.next();
				pm.speichereArtikel(b);
			}
		}
		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}

	public void einfuegen(Artikel einArtikel) throws ArtikelExistiertBereitsException {
		if (artikelBestand.contains(einArtikel)) {
			throw new ArtikelExistiertBereitsException(einArtikel);
		}
		// das übernimmt die ArtikelListe:
		artikelBestand.add(einArtikel);
	}

	public void loeschen(Artikel einArtikel) {
		artikelBestand.remove(einArtikel);
	}

	public Artikel sucheArtikel(String artikelName, String artikelNummer, float preis) {
		Artikel ergebnis = null;
		Iterator<Artikel> it = artikelBestand.iterator();

		while (it.hasNext()) {
			Artikel artikel = it.next();
			if (artikel.getArtikelName().equals(artikelName)) {
				artikel.getArtikelNummer();
				artikel.getPreis();
				ergebnis = artikel;
			}
		}
		return ergebnis;
	}

	public List<Artikel> sucheArtikel(String artikelName) {
		List<Artikel> ergebnis = new Vector<Artikel>();
		Iterator<Artikel> it = artikelBestand.iterator();

		while (it.hasNext()) {
			Artikel artikel = it.next();
			if (artikel.getArtikelName().equals(artikelName)) {
				ergebnis.add(artikel);
			}
		}

		return ergebnis;
	}

	public List<Artikel> getVorhandeneArtikel() {
		return new Vector<Artikel>(artikelBestand);
	}
}
