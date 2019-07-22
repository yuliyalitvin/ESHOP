package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import domain.exceptions.ArtikelExistiertBereitsException;
import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.BenutzerExistiertBereitsException;
import domain.exceptions.BenutzerNichtGefundenException;
import domain.exceptions.LoginBenutzerNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import interfaces.EShopInterface;
import valueObjects.AnzeigeEreignis;
import valueObjects.Artikel;
import valueObjects.ArtikelImWarenkorb;
import valueObjects.Benutzer;
import valueObjects.Kunde;
import valueObjects.Massengutartikel;
import valueObjects.Mitarbeiter;

/**
 * Schnittstelle zum Server
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class EShopFassade implements EShopInterface {

	private Socket socket = null;
	private BufferedReader in;
	private PrintStream out;

	class Message {
		public String exceptionTyp;
	}

	public EShopFassade(String host, int port) {
		try {
			socket = new Socket(host, port);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Fehler beim Oeffnen des Sockets/Streams: " + e);
			if (socket != null) {
				try {
					socket.close();
					System.err.println("Socket geschlossen");
				} catch (IOException ioe) {
				}
			}
			System.exit(1);
		}

		System.err.println("Verbunden mit Server " + socket.getInetAddress() + ":" + socket.getPort());

		try {
			String message = in.readLine();
			System.out.println(message);
		} catch (IOException e) {
		}
	}

	// LogIn

	/**
	 * Methode zum Einloggen
	 *
	 * @param benutzerName     der eingegebeneBenutzername
	 * @param benutzerPasswort eingegebenes Passwort
	 * @boolean kunde Boolean ob es ein Kunde oder Mitarbeiter ist
	 * @throws LoginBenutzerNichtGefundenException
	 * @return benutzer
	 */
	public Benutzer sucheNachBenutzernameLogin(String benutzerName, String benutzerPasswort)
			throws LoginBenutzerNichtGefundenException {

		Message message = new Message();
		message.exceptionTyp = "";

		out.println("sucheLogin");
		out.println(benutzerName);
		out.println(benutzerPasswort);

		Benutzer benutzer = null;

		try {
			String isKunde = in.readLine();
			if (!isKunde.equals("Benutzername und Passwort stimmen nicht ueberein.")) {

				boolean kunde = Boolean.parseBoolean(isKunde);
				benutzerName = in.readLine();
				int benutzerIndex = Integer.parseInt(in.readLine());
				String passwort = in.readLine();
				String vorname = in.readLine();
				String nachname = in.readLine();
				if (kunde) {
					String strasse = in.readLine();
					int plz = Integer.parseInt(in.readLine());
					String ort = in.readLine();
					String land = in.readLine();

					benutzer = new Kunde(benutzerName, kunde, passwort, vorname, nachname, strasse, plz, ort, land);
				} else {
					benutzer = new Mitarbeiter(benutzerName, kunde, passwort, vorname, nachname);
				}
				benutzer.setBenutzerIndex(benutzerIndex);

			} else {
				message.exceptionTyp = "Benutzername und Passwort stimmen nicht ueberein.";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (message.exceptionTyp.equals("Benutzername und Passwort stimmen nicht ueberein.")) {
			throw new LoginBenutzerNichtGefundenException();
		}

		return benutzer;
	}

	// Artikel
	/**
	 * Methode zum ausgeben aller Artikel
	 */
	private Artikel inArtikel(Message message) {
		Artikel artikel = null;
		message.exceptionTyp = "";

		try {
			String eingelesenes = in.readLine();
			if (eingelesenes.equals("NeuerArtikel")) {

				String artikelName = in.readLine();
				int artikelNummer = Integer.parseInt(in.readLine());
				int anzahl = Integer.parseInt(in.readLine());
				float preis = Float.parseFloat(in.readLine());
				int stueck = Integer.parseInt(in.readLine());
				boolean verfuegbar = Boolean.parseBoolean(in.readLine());
				boolean massengut = Boolean.parseBoolean(in.readLine());
				int pack = Integer.parseInt(in.readLine());

				if (massengut) {
					artikel = new Massengutartikel(artikelName, artikelNummer, preis, anzahl, true, massengut, pack);
				} else {
					artikel = new Artikel(artikelName, artikelNummer, preis, anzahl, true, massengut);
				}
				artikel.setStueck(stueck);
				artikel.setVerfuegbar(verfuegbar);

			} else if (eingelesenes.equals("Artikel nicht gefunden.")) {
				message.exceptionTyp = "Artikel nicht gefunden.";
			} else if (eingelesenes.equals("Artikel mit dieser Nummer existiert bereits.")) {
				message.exceptionTyp = "Artikel mit dieser Nummer existiert bereits.";
			} else if (eingelesenes.equals("Die Stueckzahl entspricht nicht der Packungsgroesse.")) {
				message.exceptionTyp = "Die Stueckzahl entspricht nicht der Packungsgroesse.";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return artikel;
	}

	/**
	 * Methode zum ausgeben aller Artikel
	 */
	@Override
	public List<Artikel> gibAlleArtikel() {
		List<Artikel> artikelListe = new Vector<Artikel>();

		out.println("gibAlleArtikel");

		Artikel artikel = null;
		Message message = new Message();

		while ((artikel = inArtikel(message)) != null) {
			artikelListe.add(artikel);
		}

		return artikelListe;
	}

	/**
	 * Methode zum suchen eines artikels
	 */
	@Override
	public Artikel fuegeArtikelEin(String artikelName, int artikelNummer, float preis, int anzahl, boolean massengut,
			int pack) throws ArtikelExistiertBereitsException, StueckzahlEntsprichtNichtPackungException {

		Message message = new Message();
		message.exceptionTyp = "";

		out.println("fuegeArtikelEin");
		out.println(artikelNummer);
		out.println(artikelName);
		out.println(preis);
		out.println(anzahl);
		out.println(massengut);
		out.println(pack);

		Artikel artikel = inArtikel(message);

		if (message.exceptionTyp.equals("Artikel mit dieser Nummer existiert bereits.")) {
			throw new ArtikelExistiertBereitsException();
		} else if (message.exceptionTyp.equals("Die Stueckzahl entspricht nicht der Packungsgroesse.")) {
			throw new StueckzahlEntsprichtNichtPackungException();
		}

		return artikel;
	}

	/**
	 * Methode zum suchen einer Artikelnummer
	 */
	@Override
	public Artikel sucheArtikel(int artikelNummer) throws ArtikelNichtGefundenException {
		out.println("sucheArtikel");
		out.println(artikelNummer);

		Message message = new Message();

		Artikel artikel = inArtikel(message);

		if (message.exceptionTyp.equals("Artikel nicht gefunden.")) {
			throw new ArtikelNichtGefundenException();
		}

		return artikel;
	}

	/**
	 * Methode zum suchen einer Artikelliste
	 */
	@Override
	public List<Artikel> sucheNachArtikelname(String artikelName) throws ArtikelNichtGefundenException {

		List<Artikel> artikelListe = new Vector<Artikel>();

		out.println("sucheNachArtikelname");
		out.println(artikelName);

		Artikel artikel = null;
		Message message = new Message();

		while ((artikel = inArtikel(message)) != null) {
			artikelListe.add(artikel);
		}

		if (message.exceptionTyp.equals("Artikel nicht gefunden.")) {
			throw new ArtikelNichtGefundenException();
		}

		return artikelListe;
	}

	/**
	 * Methode zum speichern des Artikel
	 */
	@Override
	public void schreibeArtikel() {
		out.println("schreibeArtikel");
	}

	// Warenkorb

	/**
	 * Methode gibt an welcher Artikel im warenkorb ist
	 * 
	 * @return istImKorb true wenn Artikel im warenkorb befindet
	 * @return istImKorb false wenn Artikel nicht im warenkorb befindet
	 */
	@Override
	public boolean istImKorb(Artikel artikel, int benutzerIndex) {

		boolean istImKorb = false;
		out.println("istImKorb");
		out.println(artikel.getArtikelNummer());
		out.println(benutzerIndex);

		try {
			istImKorb = Boolean.parseBoolean(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return istImKorb;
	}

	/**
	 * Methode zum ausgeben aller Artikel im Warenkorb
	 */
	@Override
	public List<ArtikelImWarenkorb> gibWarenkorb(int benutzerIndex) {

		List<ArtikelImWarenkorb> warenkorbListe = new Vector<ArtikelImWarenkorb>();

		out.println("gibWarenkorb");
		out.println(benutzerIndex);

		ArtikelImWarenkorb artikelImKorb = null;
		Message message = new Message();
		Artikel inArtikel = null;
		while ((inArtikel = inArtikel(message)) != null) {
			try {
				artikelImKorb = new ArtikelImWarenkorb(inArtikel);
				if (artikelImKorb != null) {
					artikelImKorb.setAnzahl(Integer.parseInt(in.readLine()));
					warenkorbListe.add(artikelImKorb);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return warenkorbListe;
	}

	/**
	 * Methode zum aendern der stueckzahl im warenkorb
	 */
	@Override
	public void anzahlImKorbAendern(Artikel artikel, int benutzerIndex, int stueck)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {

		out.println("anzahlImKorbAendern");
		out.println(artikel.getArtikelNummer());
		out.println(benutzerIndex);
		out.println(stueck);

		String eingelesenes;
		try {
			eingelesenes = in.readLine();

			if (eingelesenes.equals("Die angegebene St�ckzahl ist h�her als der verf�gbare Bestand.")) {
				throw new StueckzahlException();
			} else if (eingelesenes.equals("Die Stueckzahl entspricht nicht der Packungsgroesse.")) {
				throw new StueckzahlEntsprichtNichtPackungException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum erhoehen der Anzahl vom Artikel
	 */
	@Override
	public void bestandErhoehen(int plusAnzahl, Artikel artikel)
			throws ArtikelNichtGefundenException, StueckzahlEntsprichtNichtPackungException {

		out.println("bestandErhoehen");
		out.println(artikel.getArtikelNummer());
		out.println(plusAnzahl);

		String eingelesenes;
		try {
			eingelesenes = in.readLine();
			if (eingelesenes.equals("Artikel nicht gefunden.")) {
				throw new ArtikelNichtGefundenException();
			} else if (eingelesenes.equals("Die Stueckzahl entspricht nicht der Packungsgroesse.")) {
				throw new StueckzahlEntsprichtNichtPackungException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum einfuegen des Artikel in den Warenkorb
	 */
	@Override
	public void fuegeZuWarenkorb(Artikel artikel, int benutzerIndex, int stueck)
			throws ArtikelNichtGefundenException, StueckzahlException, StueckzahlEntsprichtNichtPackungException {

		out.println("fuegeZuWarenkorb");
		out.println(artikel.getArtikelNummer());
		out.println(benutzerIndex);
		out.println(stueck);

		try {
			String eingelesenes = in.readLine();
			if (eingelesenes.equals("Artikel nicht gefunden.")) {
				throw new ArtikelNichtGefundenException();
			} else if (eingelesenes.equals("Die Stueckzahl entspricht nicht der Packungsgroesse.")) {
				throw new StueckzahlEntsprichtNichtPackungException();
			} else if (eingelesenes.equals("Die angegebene St�ckzahl ist h�her als der verf�gbare Bestand.")) {
				throw new StueckzahlException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum entfernen des Artikel aus den Warenkorb
	 */
	@Override
	public void loescheArtikelAusWarenkorb(Artikel artikel, int benutzerIndex)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {

		out.println("loescheArtikelAusWarenkorb");
		out.println(artikel.getArtikelNummer());
		out.println(benutzerIndex);

		String eingelesenes;
		try {
			eingelesenes = in.readLine();
			if (eingelesenes.equals("Die angegebene St�ckzahl ist h�her als der verf�gbare Bestand.")) {
				throw new StueckzahlException();
			} else if (eingelesenes.equals("Die Stueckzahl entspricht nicht der Packungsgroesse.")) {
				throw new StueckzahlEntsprichtNichtPackungException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum abziehen der Anzahl vom Artikel
	 */
	@Override
	public void bestandAbziehen(ArtikelImWarenkorb artikelImKorb) {

		out.println("bestandAbziehen");
		out.println(artikelImKorb.artikel.getArtikelNummer());
		out.println(artikelImKorb.getAnzahlImWarenkorb());

	}

	// Benutzer

	/**
	 * Methode zum Empfangen der Benutzerliste
	 */
	@Override
	public Object getBenutzerListe() {
		out.println("getBenutzerListe");
		return getBenutzerListe();
	}

	/**
	 * Methode zum Ausgeben aller Mitarbeiter
	 */
	@Override
	public List<Mitarbeiter> gibAlleMitarbeiter() {
		out.println("gibAlleMitarbeiter");

		List<Mitarbeiter> mitarbeiterListe = new Vector<Mitarbeiter>();

		String eingelesenes;
		try {
			while (!(eingelesenes = in.readLine()).equals("Ende")) {
				if (eingelesenes.equals("NeuerMitarbeiter")) {
					String benutzerName = in.readLine();
					String vorname = in.readLine();
					String nachname = in.readLine();
					boolean isKunde = Boolean.parseBoolean(in.readLine());
					String passwort = in.readLine();
					int benutzerIndex = Integer.parseInt(in.readLine());
					Mitarbeiter mitarbeiter = new Mitarbeiter(benutzerName, isKunde, passwort, vorname, nachname);

					mitarbeiterListe.add(mitarbeiter);
					mitarbeiter.setBenutzerIndex(benutzerIndex);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mitarbeiterListe;
	}

	/**
	 * Methode zum Ausgeben aller Kunden
	 */
	@Override
	public List<Kunde> gibAlleKunden() {
		out.println("gibAlleKunden");

		List<Kunde> kundenListe = new Vector<Kunde>();

		String eingelesenes;
		try {
			while (!(eingelesenes = in.readLine()).equals("Ende")) {
				if (eingelesenes.equals("NeuerKunde")) {
					String benutzerName = in.readLine();
					String vorname = in.readLine();
					String nachname = in.readLine();
					String strasse = in.readLine();
					int plz = Integer.parseInt(in.readLine());
					String ort = in.readLine();
					String land = in.readLine();
					boolean isKunde = Boolean.parseBoolean(in.readLine());
					String passwort = in.readLine();
					int benutzerIndex = Integer.parseInt(in.readLine());

					Kunde kunde = new Kunde(benutzerName, isKunde, passwort, vorname, nachname, strasse, plz, ort,
							land);

					kunde.setBenutzerIndex(benutzerIndex);
					kundenListe.add(kunde);

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return kundenListe;
	}

	/**
	 * Methode zum suchen aller Benutzer
	 * 
	 * @return gesuchteBenutzer Liste aller gefundenen Benutzer
	 */
	@Override
	public List<Benutzer> sucheBenutzerNachBenutzername(String benutzerName) throws BenutzerNichtGefundenException {
		out.println("sucheBenutzerNachBenutzername");
		out.println(benutzerName);

		Message message = new Message();
		message.exceptionTyp = "";

		List<Benutzer> gesuchteBenutzer = new Vector<Benutzer>();

		String eingelesenes;
		try {
			while (!(eingelesenes = in.readLine()).equals("Ende") && !eingelesenes.equals("Benutzer nicht gefunden.")) {
				if (eingelesenes.equals("NeuerBenutzer")) {
					boolean isKunde = Boolean.parseBoolean(in.readLine());
					String benutzername = in.readLine();
					String vorname = in.readLine();
					String nachname = in.readLine();
					String passwort = in.readLine();
					int benutzerIndex = Integer.parseInt(in.readLine());
					if (isKunde == true) {
						String strasse = in.readLine();
						int plz = Integer.parseInt(in.readLine());
						String ort = in.readLine();
						String land = in.readLine();

						Benutzer kunde = new Kunde(benutzername, isKunde, passwort, vorname, nachname, strasse, plz,
								ort, land);

						kunde.setBenutzerIndex(benutzerIndex);
						gesuchteBenutzer.add(kunde);
					} else {
						Benutzer mitarbeiter = new Benutzer(benutzername, isKunde, passwort, vorname, nachname);

						mitarbeiter.setBenutzerIndex(benutzerIndex);
						gesuchteBenutzer.add(mitarbeiter);
					}
				}
			}
			if (eingelesenes.equals("Benutzer nicht gefunden.")) {
				message.exceptionTyp = "Benutzer nicht gefunden.";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (message.exceptionTyp.equals("Benutzer nicht gefunden.")) {
			throw new BenutzerNichtGefundenException();
		}

		return gesuchteBenutzer;

	}

	/**
	 * Methode zum Registrieren / Hinzufuegen eines Kunden
	 * 
	 * @throws BenutzerExistiertBereitsException wird geworfen, falls Benutzername
	 *                                           bereits belegt
	 */
	@Override
	public void fuegeKundeEin(String benutzerName, boolean kunde, String passwort, String vorname, String nachname,
			String strasse, int plz, String ort, String land) throws BenutzerExistiertBereitsException {

		Message message = new Message();
		message.exceptionTyp = "";

		out.println("fuegeKundeEin");
		out.println(benutzerName);
		out.println(kunde);
		out.println(passwort);
		out.println(vorname);
		out.println(nachname);
		out.println(strasse);
		out.println(plz);
		out.println(ort);
		out.println(land);

		String eingelesenes;
		try {
			eingelesenes = in.readLine();
			if (eingelesenes.equals("Benutzer mit Namen existiert bereits.")) {
				throw new BenutzerExistiertBereitsException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode zum hinzufuegen eines Mitarbeiter durch einen anderen Mitarbeiter
	 * 
	 * @throws BenutzerExistiertBereitsException wird geworfen, falls Benutzername
	 *                                           bereits belegt
	 * @return neuerMitarbeiter Der neu hinzugefuegte Mitarbeiter
	 */
	@Override
	public Benutzer fuegeMitarbeiterEin(String benutzerName, boolean kunde, String passwort, String vorname,
			String nachname) throws BenutzerExistiertBereitsException {

		Benutzer neuerMitarbeiter = null;

		out.println("fuegeMitarbeiterEin");
		out.println(benutzerName);
		out.println(kunde);
		out.println(passwort);
		out.println(vorname);
		out.println(nachname);

		String eingelesenes;
		try {
			if (!(eingelesenes = in.readLine()).equals("Benutzer mit Namen existiert bereits.")) {
				boolean isKunde = Boolean.parseBoolean(eingelesenes);
				benutzerName = in.readLine();
				vorname = in.readLine();
				nachname = in.readLine();
				passwort = in.readLine();
				int benutzerIndex = Integer.parseInt(in.readLine());

				neuerMitarbeiter = new Benutzer(benutzerName, isKunde, passwort, vorname, nachname);

				neuerMitarbeiter.setBenutzerIndex(benutzerIndex);
			} else if (eingelesenes.equals("Benutzer mit Namen existiert bereits.")) {
				throw new BenutzerExistiertBereitsException();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return neuerMitarbeiter;
	}

	/**
	 * Methode zum speichern der Mitarbeiter in die Textdatei
	 */
	@Override
	public void schreibeMitarbeiter() {
		out.println("schreibeMitarbeiter");
	}

	/**
	 * Methode zum speichern der Kunde in die Textdatei
	 */
	@Override
	public void schreibeKunde() {
		out.println("schreibeKunde");
	}

	/**
	 * Methode zum Ausgeben aller Benutzer
	 * 
	 * @return benutzerListe alle vorhandenen Benutzer
	 */
	@Override
	public List<Benutzer> gibAlleBenutzer() {

		List<Mitarbeiter> mitarbeiter = gibAlleMitarbeiter();
		List<Kunde> kunden = gibAlleKunden();

		List<Benutzer> benutzerListe = new Vector<Benutzer>();
		benutzerListe.addAll(mitarbeiter);
		benutzerListe.addAll(kunden);

		return benutzerListe;
	}

	// Historie

	/**
	 * Neues Ereignis in die Ereignisliste einfuegen
	 * 
	 * @param artikelNummer, eindeutige Nummer des Artikels
	 * @param anzahl,        Veraenderung der Stueckzahl hinsichtlich des
	 *                       Ereignistyps
	 * @param ereignisArt,   Art des Ereignisses: 1. Neuen Artikel hinzugefuegt, 2.
	 *                       Bestand eines Artikels erhoeht, 3. Artikel verkauft
	 * @param benutzerIndex, Ident-Nummer des Benutzers
	 */
	@Override
	public void addHistorieEreignis(int artikelNummer, int anzahl, int ereignisArt, int benutzerIndex) {

		out.println("addHistorieEreignis");
		out.println(artikelNummer);
		out.println(anzahl);
		out.println(ereignisArt);
		out.println(benutzerIndex);

	}

	/**
	 * Ereignisliste in eine TXT-Datei wegschreiben
	 */
	@Override
	public void schreibeHistorie() {
		out.println("schreibeHistorie");
	}

	/**
	 * Ereignisliste fuer die Anzeige formatieren
	 * 
	 * @return ereignisListe, Liste der Ereignisse fuer das Anzeigen
	 */
	@Override
	public List<AnzeigeEreignis> getAnzeigeEreignisListe() {
		List<AnzeigeEreignis> ereignisListe = new Vector<AnzeigeEreignis>();

		out.println("getAnzeigeEreignisListe");

		try {
			String eingelesenes;
			while (!(eingelesenes = in.readLine()).equals("Ende")) {
				if (eingelesenes.equals("NeuesEreignis")) {
					int datum = Integer.parseInt(in.readLine());
					int artikelNummer = Integer.parseInt(in.readLine());
					String artikelName = in.readLine();
					int anzahl = Integer.parseInt(in.readLine());
					int ereignisArt = Integer.parseInt(in.readLine());
					int personIndex = Integer.parseInt(in.readLine());
					String benutzerName = in.readLine();

					AnzeigeEreignis ereignis = new AnzeigeEreignis(datum, artikelNummer, artikelName, anzahl,
							ereignisArt, personIndex, benutzerName);
					ereignisListe.add(ereignis);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ereignisListe;
	}

	/**
	 * BestandshistorieVector zusammenstellen
	 * 
	 * @param artikel, Artikelobjekt
	 * @return bestandsVektor, Liste der Tagesbestaende fuer die letzten 30 Tage
	 */
	public int[] getArtikelBestandsHistorieVector(Artikel artikel) {
		int[] bestandsVektor = new int[30];
		if (artikel != null) {
			out.println("getArtikelBestandsHistorieVector");

			String artikelNummer = Integer.toString(artikel.getArtikelNummer());

			out.println(artikelNummer);

			try {
				for (int index = 0; index < 30; index++) {
					bestandsVektor[index] = Integer.parseInt(in.readLine());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bestandsVektor;
	}

	private void quit() {
		out.println("quit");

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String host = "localhost";
		int port = 6789;
		if (args.length == 2) {
			host = args[0];
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				port = 0;
			}
		}
		// Client starten und mit Server verbinden:
		EShopFassade client = new EShopFassade(host, port);
		client.quit();
	}
}
