package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import domain.Verwaltung;
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
import valueObjects.Kunde;
import valueObjects.Mitarbeiter;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

class ClientEShopRequestProcessor implements Runnable {

	private Verwaltung eShop;

	// Datenstrukturen fuer die Kommunikation
	private Socket clientSocket;
	private BufferedReader in;
	private PrintStream out;

	public ClientEShopRequestProcessor(Socket socket, Verwaltung eShopDaten) {

		// Verbindungsdaten uebernehmen
		clientSocket = socket;

		// Referenz auf Server-EShop merken
		this.eShop = eShopDaten;

		// I/O-Streams initialisieren:
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (IOException e2) {
			}
			System.err.println("Ausnahme bei Bereitstellung des Streams: " + e);
			return;
		}

		System.out.println("Verbunden mit Client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
	}

	@Override
	public void run() {
		try {
			verarbeiteAnfragen();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void verarbeiteAnfragen() throws IOException {

		String input = "";

		out.println("Server bereit");

		do {
			try {
				input = in.readLine();
			} catch (Exception e) {
				System.out.println("--->Fehler beim Lesen vom Client (Aktion): ");
				System.out.println(e.getMessage());
				continue;
			}

			// Eingabe bearbeiten:
			if (input == null) {
				input = "quit";
			} else if (input.equals("sucheLogin")) {
				sucheLogin();
				// Artikel
			} else if (input.equals("sucheArtikel")) {
				sucheArtikelNachNummer();
			} else if (input.equals("schreibeArtikel")) {
				schreibeArtikel();
			} else if (input.equals("gibAlleArtikel")) {
				gibAlleArtikel();
			} else if (input.equals("bestandAbziehen")) {
				bestandAbziehen();
			} else if (input.equals("bestandErhoehen")) {
				bestandErhoehen();
			} else if (input.equals("sucheNachArtikelname")) {
				sucheNachArtikelname();
			} else if (input.equals("fuegeArtikelEin")) {
				fuegeArtikelEin();

				// Benutzer
			} else if (input.equals("fuegeKundeEin")) {
				fuegeKundeEin();
			} else if (input.equals("schreibeKunde")) {
				schreibeKunde();
			} else if (input.equals("schreibeMitarbeiter")) {
				schreibeMitarbeiter();
			} else if (input.equals("getBenutzerListe")) {
				getBenutzerListe();
			} else if (input.equals("sucheBenutzerNachBenutzername")) {
				sucheBenutzerNachBenutzername();
			} else if (input.equals("fuegeMitarbeiterEin")) {
				fuegeMitarbeiterEin();
			} else if (input.equals("gibAlleMitarbeiter")) {
				gibAlleMitarbeiter();
			} else if (input.equals("gibAlleKunden")) {
				gibAlleKunden();

				// Warenkorb
			} else if (input.equals("istImKorb")) {
				istImKorb();
			} else if (input.equals("anzahlImKorbAendern")) {
				anzahlImKorbAendern();
			} else if (input.equals("fuegeZuWarenkorb")) {
				fuegeZuWarenkorb();
			} else if (input.equals("loescheArtikelAusWarenkorb")) {
				loescheArtikelAusWarenkorb();
			} else if (input.equals("gibWarenkorb")) {
				gibWarenkorb();

				// Historie
			} else if (input.equals("schreibeHistorie")) {
				schreibeHistorie();
			} else if (input.equals("addHistorieEreignis")) {
				addHistorieEreignis();
			} else if (input.equals("getAnzeigeEreignisListe")) {
				getAnzeigeEreignisListe();
			} else if (input.equals("getArtikelBestandsHistorieVector")) {
				getArtikelBestandsHistorieVector();
			} else if (input.equals("gibAlleArtikel")) {
				gibAlleArtikel();
			} else if (input.equals("quit")) {
			}
		} while (!(input.equals("quit")));

		System.out.println("Verbindung zu " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
				+ " durch Client abgebrochen");

		// Verbindung beenden
		try {
			clientSocket.close();
		} catch (IOException e2) {
		}
	}

//LogIn

	/**
	 * Methode zum Empfangen der Einlog-Daten
	 *
	 * @catch LoginBenutzerNichtGefundenException fangen und Weiterleiten der
	 *        Exception an die Fassade
	 */
	private void sucheLogin() {
		String benutzernameLogin = "";
		String passwortLogin;

		try {
			benutzernameLogin = in.readLine();
			passwortLogin = in.readLine();
			try {
				Benutzer benutzerLogin = eShop.sucheNachBenutzernameLogin(benutzernameLogin, passwortLogin);
				if (benutzerLogin != null) {
					if (benutzerLogin.isKunde()) {
						Kunde kundeLogin = (Kunde) benutzerLogin;

						out.println(kundeLogin.isKunde());
						out.println(kundeLogin.getBenutzerName());
						out.println(kundeLogin.getBenutzerIndex());
						out.println(kundeLogin.getPasswort());
						out.println(kundeLogin.getVorname());
						out.println(kundeLogin.getNachname());
						out.println(kundeLogin.getStrasse());
						out.println(kundeLogin.getPostleitzahl());
						out.println(kundeLogin.getOrt());
						out.println(kundeLogin.getLand());
					} else {
						Mitarbeiter mitarbeiterLogin = (Mitarbeiter) benutzerLogin;

						out.println(mitarbeiterLogin.isKunde());
						out.println(mitarbeiterLogin.getBenutzerName());
						out.println(mitarbeiterLogin.getBenutzerIndex());
						out.println(mitarbeiterLogin.getPasswort());
						out.println(mitarbeiterLogin.getVorname());
						out.println(mitarbeiterLogin.getNachname());
					}
				}
			} catch (LoginBenutzerNichtGefundenException e) {
				out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Name): ");
			System.out.println(e.getMessage());
		}

	}

	// Artikel

	/**
	 * Methode zum suchen eines artikels
	 */
	private void sucheNachArtikelname() {
		String artikelName;

		try {
			artikelName = in.readLine();

			List<Artikel> artikelListe = eShop.sucheNachArtikelname(artikelName);
			for (Artikel artikel : artikelListe) {
				outArtikel(artikel);
			}

			out.println("Ende");
		} catch (ArtikelNichtGefundenException e) {
			out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum einfuegen eines artikels
	 */
	private void fuegeArtikelEin() {
		int artikelNummer;
		String artikelName;
		float preis;
		int anzahl;
		boolean massengut;
		int pack;

		try {
			artikelNummer = Integer.parseInt(in.readLine());
			artikelName = in.readLine();
			preis = Float.parseFloat(in.readLine());
			anzahl = Integer.parseInt(in.readLine());
			massengut = Boolean.parseBoolean(in.readLine());
			pack = Integer.parseInt(in.readLine());

			Artikel artikel = eShop.fuegeArtikelEin(artikelName, artikelNummer, preis, anzahl, massengut, pack);

			outArtikel(artikel);

		} catch (ArtikelExistiertBereitsException e) {
			out.println("Artikel mit dieser Nummer existiert bereits.");
		} catch (StueckzahlEntsprichtNichtPackungException e) {
			out.println(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum ausgeben von Artikelparameter
	 */
	private void outArtikel(Artikel artikel) {
		out.println("NeuerArtikel");
		out.println(artikel.getArtikelName());
		out.println(artikel.getArtikelNummer());
		out.println(artikel.getAnzahl());
		out.println(artikel.getPreis());
		out.println(artikel.getStueck());
		out.println(artikel.isVerfuegbar());
		out.println(artikel.isMassengut());
		out.println(artikel.getPack());
	}

	/**
	 * Methode zum suchen einer artikelnummer
	 */
	private void sucheArtikelNachNummer() {
		int artikelNummer;

		try {
			artikelNummer = Integer.parseInt(in.readLine());
			try {
				Artikel artikel = eShop.sucheArtikel(artikelNummer);
				if (artikel != null) {

					outArtikel(artikel);
				} else {
					out.println("Fehler");
				}
			} catch (ArtikelNichtGefundenException e) {
				out.println(e.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum ausgeben aller Artikel
	 */
	private void gibAlleArtikel() {
		List<Artikel> artikelListe = eShop.gibAlleArtikel();
		for (Artikel artikel : artikelListe) {
			outArtikel(artikel);
		}

		out.println("Ende");
	}

	/**
	 * Methode zum speichern des Artikel
	 */
	private void schreibeArtikel() {
		try {
			eShop.schreibeArtikel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum abziehen der Anzahl vom Artikel
	 */
	private void bestandAbziehen() {

		try {
			int artikelNummer = Integer.parseInt(in.readLine());
			int anzahlImWarenkorb = Integer.parseInt(in.readLine());

			Artikel artikel;
			try {
				artikel = eShop.sucheArtikel(artikelNummer);

				eShop.bestandAbziehen(artikel, anzahlImWarenkorb);
			} catch (ArtikelNichtGefundenException e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum erhoehen der Anzahl vom Artikel
	 */
	private void bestandErhoehen() {

		try {
			int artikelNummer = Integer.parseInt(in.readLine());
			int plusanzahl = Integer.parseInt(in.readLine());

			Artikel artikel;
			try {
				artikel = eShop.sucheArtikel(artikelNummer);

				eShop.bestandErhoehen(plusanzahl, artikel);
				out.println("Bestand erhoeht.");
			} catch (StueckzahlEntsprichtNichtPackungException e) {
				out.println(e.getMessage());
			} catch (ArtikelNichtGefundenException e) {
				out.println(e.getMessage());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Warenkorb

	/**
	 * Methode gibt an welcher Artikel im warenkorb ist
	 */
	private void istImKorb() {

		try {
			int artikelNummer = Integer.parseInt(in.readLine());
			int benutzerIndex = Integer.parseInt(in.readLine());

			boolean istImKorb = eShop.istImKorb(artikelNummer, benutzerIndex);
			out.println(istImKorb);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum aendern der stueckzahl im warenkorb
	 */
	private void anzahlImKorbAendern() {

		try {
			int artikelNummer = Integer.parseInt(in.readLine());
			int benutzerIndex = Integer.parseInt(in.readLine());
			int stueck = Integer.parseInt(in.readLine());

			eShop.anzahlImKorbAendern(artikelNummer, benutzerIndex, stueck);
			out.println("Anzahl im Korb veraendert.");
		} catch (StueckzahlEntsprichtNichtPackungException e) {
			out.println(e.getMessage());
		} catch (StueckzahlException e) {
			out.println(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum entfernen des Artikel aus den Warenkorb
	 */
	private void loescheArtikelAusWarenkorb() {

		try {
			int artikelNummer = Integer.parseInt(in.readLine());
			int benutzerIndex = Integer.parseInt(in.readLine());

			ArtikelImWarenkorb artikel;
			try {
				artikel = eShop.sucheArtikelImWarenkorb(artikelNummer, benutzerIndex);

				eShop.loescheArtikelAusWarenkorb(artikel, benutzerIndex);
				out.println("Artikel aus Warenkorb entfernt.");
			} catch (StueckzahlException e) {
				out.println(e.getMessage());
			} catch (StueckzahlEntsprichtNichtPackungException e) {
				out.println(e.getMessage());
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum einfuegen des Artikel in den Warenkorb
	 */
	private void fuegeZuWarenkorb() {

		try {
			int artikelNummer = Integer.parseInt(in.readLine());
			int benutzerIndex = Integer.parseInt(in.readLine());
			int stueck = Integer.parseInt(in.readLine());

			try {
				Artikel artikel = eShop.sucheArtikel(artikelNummer);

				eShop.fuegeZuWarenkorb(artikel, benutzerIndex, stueck);
				out.println("Artikel Hinzugefuegt");
			} catch (StueckzahlEntsprichtNichtPackungException e) {
				out.println(e.getMessage());
			} catch (StueckzahlException e) {
				out.println(e.getMessage());
			} catch (ArtikelNichtGefundenException e) {
				out.println(e.getMessage());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum ausgeben aller Artikel im Warenkorb
	 */
	private void gibWarenkorb() {
		int benutzerIndex = -1;

		try {
			benutzerIndex = Integer.parseInt(in.readLine());

			List<ArtikelImWarenkorb> warenkorbListe = eShop.gibWarenkorb(benutzerIndex);
			for (ArtikelImWarenkorb artikelImKorb : warenkorbListe) {
				outArtikel(artikelImKorb.artikel);
				out.println(artikelImKorb.getAnzahlImWarenkorb());
			}

			out.println("Ende");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Benutzer

	/**
	 * Methode zum Empfangen aller Kundendaten
	 */
	private void gibAlleKunden() {
		List<Kunde> kundenListe = eShop.gibAlleKunden();
		for (Kunde kunde : kundenListe) {
			out.println("NeuerKunde");
			out.println(kunde.getBenutzerName());
			out.println(kunde.getVorname());
			out.println(kunde.getNachname());
			out.println(kunde.getStrasse());
			out.println(kunde.getPostleitzahl());
			out.println(kunde.getOrt());
			out.println(kunde.getLand());
			out.println(kunde.isKunde());
			out.println(kunde.getPasswort());
			out.println(kunde.getBenutzerIndex());
		}
		out.println("Ende");
	}

	/**
	 * Methode zum Empfangen aller Mitarbeiterdaten
	 */
	private void gibAlleMitarbeiter() {
		List<Mitarbeiter> mitarbeiterListe = eShop.gibAlleMitarbeiter();
		for (Mitarbeiter mitarbeiter : mitarbeiterListe) {
			out.println("NeuerMitarbeiter");
			out.println(mitarbeiter.getBenutzerName());
			out.println(mitarbeiter.getVorname());
			out.println(mitarbeiter.getNachname());
			out.println(mitarbeiter.isKunde());
			out.println(mitarbeiter.getPasswort());
			out.println(mitarbeiter.getBenutzerIndex());
		}
		out.println("Ende");
	}

	/**
	 * Methode zum Registrieren / Hinzufuegen eines Kunden im System
	 */
	private void fuegeKundeEin() {
		String benutzerName;
		boolean isKunde;
		String passwort;
		String vorname;
		String nachname;
		String strasse;
		int plz;
		String ort;
		String land;

		try {
			benutzerName = in.readLine();
			isKunde = Boolean.parseBoolean(in.readLine());
			passwort = in.readLine();
			vorname = in.readLine();
			nachname = in.readLine();

			strasse = in.readLine();
			plz = Integer.parseInt(in.readLine());
			ort = in.readLine();
			land = in.readLine();

			eShop.fuegeKundeEin(benutzerName, isKunde, passwort, vorname, nachname, strasse, plz, ort, land);
			out.println("Kunde hinzugefuegt.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (BenutzerExistiertBereitsException e) {
			out.println(e.getMessage());
		}

	}

	/**
	 * Methode zum hinzufuegen eines Mitarbeiters im System
	 */
	private void fuegeMitarbeiterEin() {
		String benutzerName;
		boolean isKunde;
		String passwort;
		String vorname;
		String nachname;

		try {
			benutzerName = in.readLine();
			isKunde = Boolean.parseBoolean(in.readLine());
			passwort = in.readLine();
			vorname = in.readLine();
			nachname = in.readLine();

			Benutzer neuerMitarbeiter = eShop.fuegeMitarbeiterEin(benutzerName, isKunde, passwort, vorname, nachname);
			out.println(neuerMitarbeiter.isKunde());
			out.println(neuerMitarbeiter.getBenutzerName());
			out.println(neuerMitarbeiter.getVorname());
			out.println(neuerMitarbeiter.getNachname());
			out.println(neuerMitarbeiter.getPasswort());
			out.println(neuerMitarbeiter.getBenutzerIndex());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (BenutzerExistiertBereitsException e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * Methode zum speichern der Kunden in die Textdatei
	 */
	private void schreibeKunde() throws IOException {
		eShop.schreibeKunde();
	}

	/**
	 * Methode zum speichern der Mitarbeiter in die Textdatei
	 */
	private void schreibeMitarbeiter() throws IOException {
		eShop.schreibeMitarbeiter();
	}

	/**
	 * Methode zum Empfangen der Benutzerlisten Daten
	 */
	private void getBenutzerListe() {
		out.println(eShop.getBenutzerListe());
	}

	/**
	 * Methode zum suchen und Empfangen aller Benutzerdaten
	 */
	private void sucheBenutzerNachBenutzername() {
		String benutzerName;
		try {
			benutzerName = in.readLine();

			List<Benutzer> gesuchteBenutzer = eShop.sucheBenutzerNachBenutzername(benutzerName);

			if (gesuchteBenutzer != null) {
				for (Benutzer benutzer : gesuchteBenutzer) {

					out.println("NeuerBenutzer");
					out.println(benutzer.isKunde());
					out.println(benutzer.getBenutzerName());
					out.println(benutzer.getVorname());
					out.println(benutzer.getNachname());
					out.println(benutzer.getPasswort());
					out.println(benutzer.getBenutzerIndex());
					if (benutzer instanceof Kunde) {
						out.println(((Kunde) benutzer).getStrasse());
						out.println(((Kunde) benutzer).getPostleitzahl());
						out.println(((Kunde) benutzer).getOrt());
						out.println(((Kunde) benutzer).getLand());
					}
				}
				out.println("Ende");
			}
		} catch (BenutzerNichtGefundenException e) {
			out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Name): ");
			System.out.println(e.getMessage());
		}
	}

	// Historie

	private void getAnzeigeEreignisListe() {
		List<AnzeigeEreignis> ereignisListe = eShop.getAnzeigeEreignisListe();
		for (AnzeigeEreignis ereignis : ereignisListe) {
			out.println("NeuesEreignis");
			out.println(ereignis.getDatum());
			out.println(ereignis.getArtikelNummer());
			out.println(ereignis.getArtikelName());
			out.println(ereignis.getAnzahl());
			out.println(ereignis.getEreignisArt());
			out.println(ereignis.getPersonIndex());
			out.println(ereignis.getBenutzerName());
		}

		out.println("Ende");
	}

	private void getArtikelBestandsHistorieVector() {
		int artikelNummer = 0;
		try {
			artikelNummer = Integer.parseInt(in.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Artikel artikel = null;
		try {
			artikel = eShop.sucheArtikel(artikelNummer);
		} catch (ArtikelNichtGefundenException e) {

			e.printStackTrace();
		}

		int[] bestandsVektor = eShop.getHistorie().artikelBestandsHistorieVector(artikel);
		for (int bestand : bestandsVektor) {
			out.println(bestand);
		}

	}

	// Historie

	private void schreibeHistorie() {
		try {
			eShop.schreibeHistorie();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addHistorieEreignis() {

		int artikelNummer;
		try {
			artikelNummer = Integer.parseInt(in.readLine());
			int anzahl = Integer.parseInt(in.readLine());
			int ereignisArt = Integer.parseInt(in.readLine());
			int benutzerIndex = Integer.parseInt(in.readLine());

			eShop.addHistorieEreignis(artikelNummer, anzahl, ereignisArt, benutzerIndex);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
