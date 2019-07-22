package valueObjects;

import java.util.List;
import java.util.Vector;

import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Benutzer {

	public class Befehl {

		public Befehl(String commandInput, String beschreibungInput, String commandGroupInput) {
			command = commandInput;
			beschreibung = beschreibungInput;
			commandGroup = commandGroupInput;
		}

		private String command;
		private String beschreibung;
		private String commandGroup;

		public String getBeschreibung() {
			return beschreibung;
		}

		public String getCommand() {
			return command;
		}

		public String getCommandGroup() {
			return commandGroup;
		}
	};

	public List<Befehl> erlaubteBefehle;

	private String benutzerName;
	private int benutzerIndex;
	boolean kunde;
	private String passwort;
	private String vorname;
	private String nachname;
	private List<Artikel> warenkorb;

	public Benutzer(String benutzerName, boolean kunde, String passwort, String vorname, String nachname) {

		this.benutzerName = benutzerName;
		this.kunde = kunde;
		this.passwort = passwort;
		this.vorname = vorname;
		this.nachname = nachname;
		
		warenkorb = new Vector<Artikel>();
		erlaubteBefehle = new Vector<Befehl>();
	}

	public void fuegeZuWarenkorb(Artikel artikel) {
		warenkorb.add((Artikel) artikel);
	}

	public void loescheArtikelAusWarenkorb(Artikel artikel)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {
		warenkorb.remove((Artikel) artikel);
	}

	public void loescheWarenkorb() {
		warenkorb.clear();
	}

	public String getBenutzerName() {
		return benutzerName;
	}

	public boolean isKunde() {
		return kunde;
	}

	public String getPasswort() {
		return passwort;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}
	
	public int getBenutzerIndex() {
		return benutzerIndex;
	}

	public void setBenutzerIndex(int benutzerIndex) {
		this.benutzerIndex = benutzerIndex;
	}

	public List<Artikel> getWarenkorb() {
		return warenkorb;
	}

	public boolean istBefehlErlaubt(String inputBefehl) {
		boolean istErlaubt = false;

		for (Befehl befehl : erlaubteBefehle) {
			if (befehl.getCommand().equals(inputBefehl)) {
				istErlaubt = true;
				break;
			}
		}
		return istErlaubt;
	}
}