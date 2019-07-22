package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class ArtikelBereitsImWarenkorbException extends Exception {

	public ArtikelBereitsImWarenkorbException() {
		super("Artikel breits im Warenkorb.");
	}
}
