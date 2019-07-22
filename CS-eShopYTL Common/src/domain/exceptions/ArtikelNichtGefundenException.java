package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class ArtikelNichtGefundenException extends Exception {
	public ArtikelNichtGefundenException() {
		super("Artikel nicht gefunden.");
	}
}
