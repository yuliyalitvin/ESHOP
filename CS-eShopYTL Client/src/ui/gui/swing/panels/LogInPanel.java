package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.exceptions.LoginBenutzerNichtGefundenException;
import interfaces.EShopInterface;
import valueObjects.Benutzer;

/**
 * Layout und Funktionen der LogIn Seite
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class LogInPanel extends JPanel {

	public interface LogInListener {
		public void wennEingeloggt(Benutzer benutzer);
	}

	private EShopInterface eShopInterface = null;
	private LogInListener einLoggenListener = null;

	private JButton logInButton;
	private JTextField benutzerNameFeld = null;
	private JTextField passwortFeld = null;
	private JLabel fehlermeldungFeld = null;

	public LogInPanel(EShopInterface verwaltung, LogInListener listener) {
		eShopInterface = verwaltung;
		einLoggenListener = listener;

		erstelleUI();
		erstelleEreignisse();
	}

	/**
	 * Erstellen des Layouts
	 */
	private void erstelleUI() {
		int anzahlZeilen = 13;
		this.setLayout(new GridLayout(anzahlZeilen, 1));

		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel("    Benutzername: "));
		benutzerNameFeld = new JTextField();
		this.add(benutzerNameFeld);
		this.add(new JLabel("    Passwort: "));
		passwortFeld = new JTextField();
		this.add(passwortFeld);
		this.add(new JLabel()); // Abstandhalter
		logInButton = new JButton("LogIn");
		this.add(logInButton);
		fehlermeldungFeld = new JLabel("");
		this.add(fehlermeldungFeld);

		for (int i = 13; i < anzahlZeilen; i++) {
			this.add(new JLabel());
		}
	}

	/**
	 * Hinzufuegen der Funktionen
	 */
	private void erstelleEreignisse() {
		logInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String benutzerName = benutzerNameFeld.getText();
				String benutzerPasswort = passwortFeld.getText();

				Benutzer benutzerLogin = null;

				if (!benutzerName.isEmpty() && !benutzerPasswort.isEmpty()) {

					try {
						benutzerLogin = eShopInterface.sucheNachBenutzernameLogin(benutzerName, benutzerPasswort);
						fehlermeldungFeld.setText("");
					} catch (LoginBenutzerNichtGefundenException lng) {
						fehlermeldungFeld
								.setText(lng.getMessage() + " -- LogIn nicht erfolgreich, probieren Sie es erneut.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
						benutzerLogin = null;
					}
					benutzerNameFeld.setText("");
					passwortFeld.setText("");

					if (benutzerLogin != null) {
						einLoggenListener.wennEingeloggt(benutzerLogin);
					}
				}
			}
		});
	}
}
