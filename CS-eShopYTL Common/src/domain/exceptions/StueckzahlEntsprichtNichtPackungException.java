package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class StueckzahlEntsprichtNichtPackungException extends Exception {
	
	public StueckzahlEntsprichtNichtPackungException() {
		super("Die Stueckzahl entspricht nicht der Packungsgroesse.");
	}
}
