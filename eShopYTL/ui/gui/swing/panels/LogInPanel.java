package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.Verwaltung;
import domain.exceptions.LoginBenutzerNichtGefundenException;
import valueObjects.Benutzer;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class LogInPanel extends JPanel {

	public interface LogInListener {
		public void wennEingeloggt(Benutzer benutzer);
	}

	private Verwaltung eShop = null;
	private LogInListener einLoggenListener = null;

	private JButton logInButton;
	private JTextField benutzerNameFeld = null;
	private JTextField passwortFeld = null;
	private JLabel fehlermeldungFeld = null;

	public LogInPanel(Verwaltung verwaltung, LogInListener listener) {
		eShop = verwaltung;
		einLoggenListener = listener;

		erstelleUI();
		erstelleEreignisse();
	}

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

	private void erstelleEreignisse() {
		logInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String benutzerName = benutzerNameFeld.getText();
				String benutzerPasswort = passwortFeld.getText();

				Benutzer benutzerLogin = null;

				if (!benutzerName.isEmpty() && !benutzerPasswort.isEmpty()) {

					try {
						benutzerLogin = eShop.sucheNachBenutzernameLogin(benutzerName, benutzerPasswort);
						System.out.println("Eingeloggter Benutzer: " + benutzerLogin);

						fehlermeldungFeld.setText("");
					} catch (LoginBenutzerNichtGefundenException lng) {
						fehlermeldungFeld.setText("LogIn nicht erfolgreich, Probiere erneut.");
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
