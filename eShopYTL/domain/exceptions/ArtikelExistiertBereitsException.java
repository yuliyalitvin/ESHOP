package domain.exceptions;

import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class ArtikelExistiertBereitsException extends Exception {

	public ArtikelExistiertBereitsException(Artikel artikel) {
		super("Artikel mit Nummer " + artikel.getArtikelNummer() +  " existiert bereits.");
	}
}
