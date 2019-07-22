package domain;

import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.util.Vector;
import java.util.Calendar;
import java.util.Collections;

import persistence.FilePersistenceManager;
import persistence.PersistenceManager;
import valueObjects.Artikel;
import valueObjects.Benutzer;
import valueObjects.Ereignis;

/**
 * Verwaltung der Ereignisliste
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Historie {

	private String historieDateiName;

	private List<Ereignis> ereignisListe = new Vector<Ereignis>();

	private class TagesBestand {
		private int datum;
		private int bestandsaenderung;
		private int bestand;

		public TagesBestand(int tagesDatum) {
			datum = tagesDatum;
			bestandsaenderung = 0;
			bestand = 0;
		}

		public int getDatum() {
			return datum;
		}

		public int getBestand() {
			return bestand;
		}

		public void veraendereBestandsaenderung(int anzahl) {
			bestandsaenderung += anzahl;
		}

		public void setBestand(int neuerBestand) {
			bestand = neuerBestand;
		}

		public int getBestandsaenderung() {
			return bestandsaenderung;
		}
	};

	public Historie(String datei) throws IOException {

		historieDateiName = datei;

		liesDaten(historieDateiName);
	}

	private PersistenceManager pm = new FilePersistenceManager();

	public void liesDaten(String datei) throws IOException {
		pm.openForReading(datei);

		Ereignis ereignis;
		do {
			ereignis = pm.ladeEreignis();
			if (ereignis != null) {
				getEreignisListe().add(ereignis);
			}
		} while (ereignis != null);

		pm.close();
	}

	/**
	 * Ereignisliste in eine TXT-Datei wegschreiben
	 */
	public void schreibeHistorie() throws IOException {

		pm.openForWriting(historieDateiName);
		if (!getEreignisListe().isEmpty()) {
			Iterator<Ereignis> iter = getEreignisListe().iterator();
			while (iter.hasNext()) {
				Ereignis ereignis = iter.next();
				pm.speichereEreignis(ereignis);
			}
		}
		pm.close();
	}

	/**
	 * Neues Ereignis hinzufuegen
	 * 
	 * @param artikelNummer, eindeutige Nummer des Artikels
	 * @param anzahl,        Veraenderung der Stueckzahl hinsichtlich des
	 *                       Ereignistyps
	 * @param ereignisArt,   Art des Ereignisses: 1. Neuen Artikel hinzugefuegt, 2.
	 *                       Bestand eines Artikels erhoeht, 3. Artikel verkauft
	 * @param benutzerIndex, Ident-Nummer des Benutzers
	 */
	public void addEreignis(int artikelNummer, int anzahl, int ereignisArt, int benutzerIndex) {
		Ereignis neuesEreignis = new Ereignis();

		Calendar datum = Calendar.getInstance();

		int tagDesJahres = datum.get(Calendar.DAY_OF_YEAR);

		neuesEreignis.setDatum(tagDesJahres);
		neuesEreignis.setArtikelNummer(artikelNummer);
		neuesEreignis.setAnzahl(anzahl);
		neuesEreignis.setEreignisart(ereignisArt);
		neuesEreignis.setPersonIndex(benutzerIndex);

		getEreignisListe().add(neuesEreignis);
	}

	public List<Ereignis> getEreignisListe() {
		return ereignisListe;
	}

	public void setEreignisListe(List<Ereignis> ereignisListe) {
		this.ereignisListe = ereignisListe;
	}

	/**
	 * Historieausgabe fuer die CUI vorbereitet
	 * 
	 * @param benutzerListe, Liste der Benutzer
	 * @param artikelListe,  Liste der Artikel
	 * @return ausgabe, Historieausgabe in einem String
	 */
	public String historieAusgeben(BenutzerVerwaltung benutzerListe, ArtikelVerwaltung artikelListe) {
		String ausgabe = "";

		for (Ereignis ereignis : getEreignisListe()) {

			int tagDesJahres = ereignis.getDatum();
			Calendar kalender = Calendar.getInstance();
			kalender.set(Calendar.DAY_OF_YEAR, tagDesJahres);
			ausgabe += "Datum: " + kalender.get(Calendar.DAY_OF_MONTH) + "." + (kalender.get(Calendar.MONTH) + 1) + "."
					+ kalender.get(Calendar.YEAR) + " ";

			boolean istKunde = false;

			switch (ereignis.getEreignisArt()) {
			case 1:
				ausgabe += "Neuer Artikel hinzugefuegt. ";
				istKunde = false;
				break;
			case 2:
				ausgabe += "Artikelbestand erhoeht (Einlagerung) ";
				istKunde = false;
				break;
			case 3:
				ausgabe += "Artikel verkauft (Auslagerung) ";
				istKunde = true;
				break;
			}

			ausgabe += " Anzahl: " + ereignis.getAnzahl() + " ";

			if (ereignis.getEreignisArt() == 1 || ereignis.getEreignisArt() == 2) {
				ausgabe += "Mitarbeiter: ";
			} else {
				// Kunde kann nur kaufen, deswegen kann er nur case 3
				ausgabe += "Kunde: ";
			}

			int benutzerIndex = ereignis.getPersonIndex();
			Benutzer benutzer = benutzerListe.getBenutzer(benutzerIndex, istKunde);
			String personName = benutzer.getBenutzerName();
			ausgabe += personName + "\n";

			Artikel artikel = artikelListe.sucheArtikel(ereignis.getArtikelNummer());
			ausgabe += "Artikel :  ";
			ausgabe += "Nummer: " + artikel.getArtikelNummer() + " Name: " + artikel.getArtikelName() + " Preis: "
					+ artikel.getPreis() + "\n\n";
		}
		return ausgabe;
	}

	/**
	 * Historieausgabe eines Artikels fuer die CUI vorbereitet
	 * 
	 * @param gewuenschterArtikel, Artikelobjekt
	 * @param benutzerListe,       Liste der Benutzer
	 * @return ausgabe, Historieausgabe in einem String
	 */
	public String artikelHistorieAusgabe(Artikel gewuenschterArtikel, BenutzerVerwaltung benutzerListe) {
		String ausgabe = "";

		for (Ereignis ereignis : getEreignisListe()) {
			if (ereignis.getArtikelNummer() == gewuenschterArtikel.getArtikelNummer()) {
				int tagDesJahres = ereignis.getDatum();
				Calendar kalender = Calendar.getInstance();
				kalender.set(Calendar.DAY_OF_YEAR, tagDesJahres);
				ausgabe += "Datum: " + kalender.get(Calendar.DAY_OF_MONTH) + "." + (kalender.get(Calendar.MONTH) + 1)
						+ "." + kalender.get(Calendar.YEAR) + " ";

				boolean istKunde = false;

				switch (ereignis.getEreignisArt()) {
				case 1:
					ausgabe += "Neuer Artikel hinzugefuegt. ";
					istKunde = false;
					break;
				case 2:
					ausgabe += "Artikelbestand erhoeht (Einlagerung) ";
					istKunde = false;
					break;
				case 3:
					ausgabe += "Artikel verkauft (Auslagerung) ";
					istKunde = true;
					break;
				}

				ausgabe += " Anzahl: " + ereignis.getAnzahl() + " ";

				if (ereignis.getEreignisArt() == 1 || ereignis.getEreignisArt() == 2) {
					ausgabe += "Mitarbeiter: ";
				} else {
					// Kunde kann nur kaufen, deswegen kann er nur case 3
					ausgabe += "Kunde: ";
				}

				int benutzerIndex = ereignis.getPersonIndex();
				Benutzer benutzer = benutzerListe.getBenutzer(benutzerIndex, istKunde);
				String personName = benutzer.getBenutzerName();
				ausgabe += personName + "\n";
			}
		}
		return ausgabe;
	}

	/**
	 * Bestandsausgabe eines Artikels fuer die CUI vorbereitet
	 * 
	 * @param gewuenschterArtikel, Artikelobjekt
	 * @return ausgabe, Bestandsausgabe in einem String
	 */
	public String artikelBestandsHistorieAusgabe(Artikel gewuenschterArtikel) {
		String ausgabe = "";

		Calendar datum = Calendar.getInstance();

		int aktuellerTagDesJahres = datum.get(Calendar.DAY_OF_YEAR);

		int anfangsDatum = aktuellerTagDesJahres - 30;

		List<TagesBestand> hilfsliste = new Vector<TagesBestand>();

		int letztesDatum = 0;

		TagesBestand tagesBestand = null;

		for (Ereignis ereignis : getEreignisListe()) {
			if (ereignis.getArtikelNummer() == gewuenschterArtikel.getArtikelNummer()
					&& ereignis.getDatum() >= anfangsDatum) {

				int aenderung = ereignis.getAnzahl();
				if (ereignis.getEreignisArt() == 3) {
					aenderung *= -1;
				}

				if (ereignis.getDatum() != letztesDatum) {
					tagesBestand = new TagesBestand(ereignis.getDatum());

					tagesBestand.veraendereBestandsaenderung(aenderung);

					hilfsliste.add(tagesBestand);
					letztesDatum = ereignis.getDatum();
				} else {
					tagesBestand.veraendereBestandsaenderung(aenderung);
				}
			}
		}

		Collections.reverse(hilfsliste);

		int vorherigerBestand = 0;

		int aktuellerBestand = gewuenschterArtikel.getAnzahl();

		int bestandaenderung = aktuellerBestand * (-1);

		for (TagesBestand tBestand : hilfsliste) {

			int neuerBestand = vorherigerBestand + bestandaenderung * (-1);

			tBestand.setBestand(neuerBestand);

			vorherigerBestand = neuerBestand;

			bestandaenderung = tBestand.getBestandsaenderung();

		}

		Collections.reverse(hilfsliste);

		for (TagesBestand tagBestand : hilfsliste) {

			int tagDesJahres = tagBestand.getDatum();
			Calendar kalender = Calendar.getInstance();
			kalender.set(Calendar.DAY_OF_YEAR, tagDesJahres);
			ausgabe += "Datum: " + kalender.get(Calendar.DAY_OF_MONTH) + "." + (kalender.get(Calendar.MONTH) + 1) + "."
					+ kalender.get(Calendar.YEAR) + " ";

			ausgabe += " Bestand: " + tagBestand.getBestand() + "\n";
		}
		return ausgabe;
	}

	// GUI

	/**
	 * Bestandshistorie fuer die letzten 30 Tage fuer das Zeichnen des
	 * Bestandsgraphen
	 * 
	 * Die Bestandsveraenderungen eines Artikels an einem Tag werden
	 * zusammengerechnet. Dann werden abwaerts aus dem aktuellen Tagesbestand die
	 * einzelnen Tagesbestaende anhand der Tagesbestandsveraenderungen berechnet.
	 * 
	 * @param gewuenschterArtikel, Artikelobjekt
	 * @return bestandsVektor, Tagesbestaende der letzten 30 Tage
	 */
	public int[] artikelBestandsHistorieVector(Artikel gewuenschterArtikel) {
		int[] bestandsVektor = new int[30];

		if (gewuenschterArtikel != null) {
			Calendar datum = Calendar.getInstance();

			int aktuellerTagDesJahres = datum.get(Calendar.DAY_OF_YEAR);

			int anfangsDatum = aktuellerTagDesJahres - 30;

			List<TagesBestand> hilfsliste = new Vector<TagesBestand>();

			int letztesDatum = 0;

			TagesBestand tagesBestand = null;

			for (Ereignis ereignis : getEreignisListe()) {
				if (ereignis.getArtikelNummer() == gewuenschterArtikel.getArtikelNummer()) {

					int aenderung = ereignis.getAnzahl();
					if (ereignis.getEreignisArt() == 3) {
						aenderung *= -1;
					}

					if (ereignis.getDatum() != letztesDatum) {
						tagesBestand = new TagesBestand(ereignis.getDatum());

						tagesBestand.veraendereBestandsaenderung(aenderung);

						hilfsliste.add(tagesBestand);
						letztesDatum = ereignis.getDatum();
					} else {
						tagesBestand.veraendereBestandsaenderung(aenderung);
					}
				}
			}

			Collections.reverse(hilfsliste);

			int vorherigerBestand = 0;

			int aktuellerBestand = gewuenschterArtikel.getAnzahl();

			int bestandaenderung = aktuellerBestand * (-1);
			int bestandsaenderungsTagNummer = 0;
			List<TagesBestand> hilfslisteAufbereitet = new Vector<TagesBestand>();

			for (TagesBestand tBestand : hilfsliste) {

				int neuerBestand = vorherigerBestand + bestandaenderung * (-1);

				tBestand.setBestand(neuerBestand);

				vorherigerBestand = neuerBestand;

				bestandaenderung = tBestand.getBestandsaenderung();

				int tagDesJahres = tBestand.getDatum();

				hilfslisteAufbereitet.add(tBestand);

				bestandsaenderungsTagNummer = tagDesJahres - anfangsDatum;
				if (bestandsaenderungsTagNummer <= 0) {
					break;
				}
			}

			Collections.reverse(hilfslisteAufbereitet);

			int letzteTagNummer = 0;
			int letzterBestand = -1;

			for (TagesBestand tagBestand : hilfslisteAufbereitet) {

				int tagDesJahres = tagBestand.getDatum();

				bestandsaenderungsTagNummer = tagDesJahres - anfangsDatum;

				if (letzterBestand > -1) {
					for (int index = letzteTagNummer - 1; index < bestandsaenderungsTagNummer - 1; index++) {
						bestandsVektor[index] = letzterBestand;
					}
				}

				letzterBestand = tagBestand.getBestand();
				letzteTagNummer = bestandsaenderungsTagNummer;
				if (letzteTagNummer <= 0) {
					letzteTagNummer = 1;
				}
			}

			for (int i = letzteTagNummer - 1; i < 30; i++) {
				bestandsVektor[i] = letzterBestand;
			}
		}
		return bestandsVektor;
	}
}
