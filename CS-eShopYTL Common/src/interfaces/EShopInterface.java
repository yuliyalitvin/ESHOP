package interfaces;

import java.util.List;

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
 * Interface des eShops
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public interface EShopInterface {

	Benutzer sucheNachBenutzernameLogin(String benutzernameLogin, String passwortLogin) throws LoginBenutzerNichtGefundenException;

	void fuegeKundeEin(String benutzerName, boolean b, String passwort, String vorname, String nachname, String strasse,
			int plz, String ort, String land) throws BenutzerExistiertBereitsException;

	List<Artikel> gibAlleArtikel();

	Artikel fuegeArtikelEin(String artikelName, int anr, float pr, int anz, boolean massengut, int pack) throws ArtikelExistiertBereitsException, StueckzahlEntsprichtNichtPackungException; 

	void addHistorieEreignis(int anr, int anz, int i, int benutzerIndex);

	List<Artikel> sucheNachArtikelname(String artikelName) throws ArtikelNichtGefundenException;

	Artikel sucheArtikel(int anr) throws ArtikelNichtGefundenException;

	void bestandErhoehen(int plusAnzahl, Artikel gesuchterArtikel) throws ArtikelNichtGefundenException, StueckzahlEntsprichtNichtPackungException;

	List<Kunde> gibAlleKunden();

	List<Mitarbeiter> gibAlleMitarbeiter();

	Benutzer fuegeMitarbeiterEin(String benutzerName, boolean b, String passwort, String vorname, String nachname) throws BenutzerExistiertBereitsException;

	List<Benutzer> sucheBenutzerNachBenutzername(String benutzerName) throws BenutzerNichtGefundenException;

	List<ArtikelImWarenkorb> gibWarenkorb(int benutzerIndex);

	void fuegeZuWarenkorb(Artikel artikel, int benutzerIndex, int stueck) throws ArtikelNichtGefundenException, StueckzahlException, StueckzahlEntsprichtNichtPackungException;

	void loescheArtikelAusWarenkorb(Artikel artikel, int benutzerIndex) throws StueckzahlException, StueckzahlEntsprichtNichtPackungException;

	void bestandAbziehen(ArtikelImWarenkorb artikel);

	Object getBenutzerListe();

	void schreibeArtikel();

	void schreibeMitarbeiter();

	void schreibeKunde();

	void schreibeHistorie();

	List<Benutzer> gibAlleBenutzer();

	List<AnzeigeEreignis> getAnzeigeEreignisListe();

	int[] getArtikelBestandsHistorieVector(Artikel artikel);

	boolean istImKorb(Artikel artikel, int benutzerIndex);

	void anzahlImKorbAendern(Artikel artikel, int benutzerIndex, int stueck) throws StueckzahlException, StueckzahlEntsprichtNichtPackungException;
	
	
}
