package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class StueckzahlEntsprichtNichtPackungException extends Exception {
	
	public StueckzahlEntsprichtNichtPackungException(int stueck, int pack) {
		super("Die Stueckzahl " + stueck + " entspricht nicht der Packungsgroesse von " + pack );
	}
}
