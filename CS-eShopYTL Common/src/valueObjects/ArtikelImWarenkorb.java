package valueObjects;

import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class ArtikelImWarenkorb {

	/**
	 * Konstruktoren
	 */
	public ArtikelImWarenkorb(Artikel inputArtikel) {
		artikel = inputArtikel;
	}

	public Artikel artikel;
	private int anzahlImWarenkorb;

	/**
	 * Methode zum getten der Anzahl im Warenkorb
	 */
	public int getAnzahlImWarenkorb() {
		return anzahlImWarenkorb;
	}

	/**
	 * Methode zum setten der Anzahl im warenkorb
	 */
	public synchronized void setAnzahlImWarenkorb(int stueckNeu)
			throws StueckzahlException, StueckzahlEntsprichtNichtPackungException {

		if (artikel.isMassengut()) {
			if (artikel.anzahl - artikel.stueck >= stueckNeu) {
				int pack = artikel.getPack();
				if ((stueckNeu % pack) == 0) {
					artikel.stueck -= anzahlImWarenkorb;
					anzahlImWarenkorb = stueckNeu / pack;
					artikel.stueck += anzahlImWarenkorb;
				} else {
					throw new StueckzahlEntsprichtNichtPackungException();
				}
			} else {
				throw new StueckzahlException();
			}
		} else {
			if (artikel.anzahl - artikel.stueck >= stueckNeu) {
				artikel.stueck -= anzahlImWarenkorb;
				anzahlImWarenkorb = stueckNeu;
				artikel.stueck += anzahlImWarenkorb;
			} else {
				throw new StueckzahlException();
			}
		}

	}

	/**
	 * Methode zum getten der Artikelnummer
	 */
	public int getArtikelNummer() {
		return artikel.getArtikelNummer();
	}

	/**
	 * Methode zum setten der Anzahl
	 */
	public void setAnzahl(int anzahl) {
		anzahlImWarenkorb = anzahl;
	}

}
