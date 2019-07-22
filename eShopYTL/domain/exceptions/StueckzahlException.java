package domain.exceptions;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class StueckzahlException extends Exception{
	
	public StueckzahlException(int anzahl, int stueck) {
		super("Die angegebene Stückzahl "+stueck+" ist höher als der verfügbare Bestand von "+ anzahl + ".\n");
	}
}
