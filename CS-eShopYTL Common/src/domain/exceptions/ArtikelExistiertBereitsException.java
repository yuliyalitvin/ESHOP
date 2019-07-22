package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class ArtikelExistiertBereitsException extends Exception {

	public ArtikelExistiertBereitsException() {
		super("Artikel mit dieser Nummer existiert bereits.");
	}
}
