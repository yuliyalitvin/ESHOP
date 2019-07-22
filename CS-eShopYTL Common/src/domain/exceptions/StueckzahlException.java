package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class StueckzahlException extends Exception{
	
	public StueckzahlException() {
		super("Die angegebene Stückzahl ist höher als der verfügbare Bestand.");
	}
}
