package domain.exceptions;

@SuppressWarnings("serial")
public class WarenkorbIstLeerException extends Exception {
	public WarenkorbIstLeerException() {
		super("Warenkorb ist leer.");
	}
}
