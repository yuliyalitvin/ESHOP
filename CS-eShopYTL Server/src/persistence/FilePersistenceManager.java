package persistence;

import valueObjects.Artikel;
import valueObjects.Ereignis;
import valueObjects.Kunde;
import valueObjects.Mitarbeiter;
import valueObjects.Warenkorb;
import valueObjects.Massengutartikel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Klasse zur Persistenten Speicherung und Laden aus/in Textdateien
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class FilePersistenceManager implements PersistenceManager {

	private BufferedReader reader = null;
	private PrintWriter writer = null;

	public void openForReading(String datei) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(datei));
	}

	public void openForWriting(String datei) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}

	public boolean close() {
		if (writer != null)
			writer.close();

		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();

				return false;
			}
		}

		return true;
	}

	public Artikel ladeArtikel() throws IOException {
		// Titel einlesen
		String artikelname = liesZeile();
		if (artikelname == null) {
			return null;
		}
		// Nummer einlesen ...
		String nummerString = liesZeile();
		int nummer = Integer.parseInt(nummerString);

		// Preis einlesen ...
		String preisString = liesZeile();
		float preis = Float.parseFloat(preisString);

		// Anzahl einlesen ...
		String anzahlString = liesZeile();
		int anzahl = Integer.parseInt(anzahlString);

		// Artikel auf Lager
		String verfuegbarCode = liesZeile();
		boolean verfuegbar = verfuegbarCode.equals("t") ? true : false;

		// Artikel als Massengut
		String massengutartikelCode = liesZeile();
		boolean massengutartikel = massengutartikelCode.equals("t") ? true : false;

		String packungString = liesZeile();
		// Anzahl einlesen ...
		Artikel artikel = null;

		if (massengutartikel == false) {
			artikel = new Artikel(artikelname, nummer, preis, anzahl, verfuegbar, massengutartikel);
		} else {

			int pack = Integer.parseInt(packungString);

			artikel = new Massengutartikel(artikelname, nummer, preis, anzahl, verfuegbar, massengutartikel, pack);
		}
		return artikel;
	}

	public boolean speichereArtikel(Artikel artikel) {
		schreibeZeile(artikel.getArtikelName());
		schreibeZeile(artikel.getArtikelNummer() + "");
		schreibeZeile(artikel.getPreis() + "");
		schreibeZeile(artikel.getAnzahl() + "");
		if (artikel.isVerfuegbar())
			schreibeZeile("t");
		else
			schreibeZeile("f");
		if (artikel.isMassengut()) {
			schreibeZeile("t");
			int packungsgroesse = ((Massengutartikel) artikel).getPack();
			schreibeZeile(packungsgroesse + "");
		} else {
			schreibeZeile("f");
			schreibeZeile("0");
		}
		return true;
	}

	// Kunden

	/**
	 * Methode zum laden aller Kunden
	 * 
	 * @return Rückgabe des gelandenen Kunden
	 */
	public Kunde ladeKunde() throws IOException {
		// Benutzername einlesen
		String benutzerName = liesZeile();
		if (benutzerName == null) {
			return null;
		}
		String passwort = liesZeile();
		String vorname = liesZeile();
		String nachname = liesZeile();
		String strasse = liesZeile();
		// Nummer einlesen ...
		String nummerString1 = liesZeile();
		int postleitzahl = Integer.parseInt(nummerString1);
		String ort = liesZeile();
		String land = liesZeile();

		String kundeCode = liesZeile();
		boolean kunde = kundeCode.equals("Kunde") ? true : false;

		return new Kunde(benutzerName, kunde, passwort, vorname, nachname, strasse, postleitzahl, ort, land);
	}

	/**
	 * Methode zum speichern aller Kunden
	 */
	public boolean speichereKunde(Kunde k) throws IOException {
		schreibeZeile(k.getBenutzerName());
		schreibeZeile(k.getPasswort());
		schreibeZeile(k.getVorname());
		schreibeZeile(k.getNachname());
		schreibeZeile(k.getStrasse());
		schreibeZeile(k.getPostleitzahl() + "");
		schreibeZeile(k.getOrt());
		schreibeZeile(k.getLand());
		if (k.isKunde())
			schreibeZeile("Kunde");
		else
			schreibeZeile("Mitarbeiter");
		return true;
	}

	// Mitarbeiter
	/**
	 * Methode zum laden aller Mitarbeiter
	 * 
	 * @return Rückgabe des gelandenen Mitarbeiter
	 */
	public Mitarbeiter ladeMitarbeiter() throws IOException {
		// Benutzername einlesen
		String benutzerName = liesZeile();
		if (benutzerName == null) {
			return null;
		}
		String passwort = liesZeile();
		String vorname = liesZeile();
		String nachname = liesZeile();

		String kundeCode = liesZeile();
		boolean kunde = kundeCode.equals("Kunde") ? true : false;

		return new Mitarbeiter(benutzerName, kunde, passwort, vorname, nachname);
	}

	/**
	 * Methode zum speichern aller Mitarbeiter
	 */
	public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException {
		schreibeZeile(m.getBenutzerName());
		schreibeZeile(m.getPasswort());
		schreibeZeile(m.getVorname());
		schreibeZeile(m.getNachname());
		if (m.isKunde())
			schreibeZeile("Kunde");
		else
			schreibeZeile("Mitarbeiter");
		return true;
	}

	// Warenkorb
	public Warenkorb ladeWarenkorb() throws IOException {
		// Titel einlesen
		String warenName = liesZeile();
		if (warenName == null) {
			return null;
		}
		// Nummer einlesen ...
		String nummerString = liesZeile();
		int warenNummer = Integer.parseInt(nummerString);

		// Preis einlesen ...
		String preisString = liesZeile();
		float preis = Float.parseFloat(preisString);

		return new Warenkorb(warenName, warenNummer, preis);
	}

	public void speichereWarenkorb(Warenkorb w) throws IOException {
		schreibeZeile(w.getWarenName());
		schreibeZeile(w.getWarenNummer() + "");
		schreibeZeile(w.getPreis() + "");
	}

	public Ereignis ladeEreignis() throws IOException {

		Ereignis ereignis = new Ereignis();

		// Datum einlesen
		String datumString = liesZeile();
		if (datumString != null) {
			ereignis.setDatum(Integer.parseInt(datumString));

			// Artikelnummer einlesen ...
			String artikelNummerString = liesZeile();
			ereignis.setArtikelNummer(Integer.parseInt(artikelNummerString));

			// Anzahl einlesen ...
			String anzahlString = liesZeile();
			ereignis.setAnzahl(Integer.parseInt(anzahlString));

			// Ereignisart einlesen ...
			String ereignisArtString = liesZeile();
			ereignis.setEreignisart(Integer.parseInt(ereignisArtString));

			// Personindex einlesen ...
			String personIndexString = liesZeile();
			ereignis.setPersonIndex(Integer.parseInt(personIndexString));
		} else {
			ereignis = null;
		}
		return ereignis;
	}

	public void speichereEreignis(Ereignis ereignis) throws IOException {
		schreibeZeile(ereignis.getDatum() + "");
		schreibeZeile(ereignis.getArtikelNummer() + "");
		schreibeZeile(ereignis.getAnzahl() + "");
		schreibeZeile(ereignis.getEreignisArt() + "");
		schreibeZeile(ereignis.getPersonIndex() + "");
	}

	private String liesZeile() throws IOException {
		if (reader != null)
			return reader.readLine();
		else
			return "";
	}

	private void schreibeZeile(String daten) {
		if (writer != null)
			writer.println(daten);
	}
}
