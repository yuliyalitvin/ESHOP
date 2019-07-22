package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.Verwaltung;
import domain.exceptions.BenutzerExistiertBereitsException;
import ui.gui.swing.GUI;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class RegistrierenPanel extends JPanel {

	public interface RegistrierenListener {
		public void wennRegistriert();
	}

	private Verwaltung eShop = null;
	private GUI gui;
	private RegistrierenListener registrierenListener = null;

	private JButton registrierenButton;
	private JTextField benutzerNameFeld = null;
	private JTextField passwortFeld = null;
	private JTextField vornameFeld = null;
	private JTextField nachnameFeld = null;
	private JTextField StrasseFeld = null;
	private JTextField postleitzahlFeld = null;
	private JTextField ortFeld = null;
	private JTextField landFeld = null;
	private JLabel fehlermeldungFeld = null;

	public RegistrierenPanel(Verwaltung verwaltung, RegistrierenListener listener) {
		eShop = verwaltung;
		registrierenListener = listener;

		erstelleUI();
		erstelleEreignisse();
	}

	private void erstelleUI() {
		int anzahlZeilen = 13;
		this.setLayout(new GridLayout(anzahlZeilen, 1));

		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel()); // Abstandhalter
		this.add(new JLabel("    Benutzername: "));
		benutzerNameFeld = new JTextField();
		this.add(benutzerNameFeld);
		this.add(new JLabel("    Passwort: "));
		passwortFeld = new JTextField();
		this.add(passwortFeld);
		this.add(new JLabel("    Vorname: "));
		vornameFeld = new JTextField();
		this.add(vornameFeld);
		this.add(new JLabel("    Nachname: "));
		nachnameFeld = new JTextField();
		this.add(nachnameFeld);
		this.add(new JLabel("    Strasse: "));
		StrasseFeld = new JTextField();
		this.add(StrasseFeld);
		this.add(new JLabel("    Postleitzahl: "));
		postleitzahlFeld = new JTextField();
		this.add(postleitzahlFeld);
		this.add(new JLabel("    Ort: "));
		ortFeld = new JTextField();
		this.add(ortFeld);
		this.add(new JLabel("    Land: "));
		landFeld = new JTextField();
		this.add(landFeld);
		this.add(new JLabel()); // Abstandhalter
		registrierenButton = new JButton("Registrieren");
		this.add(registrierenButton);
		this.add(new JLabel()); // Abstandhalter
		fehlermeldungFeld = new JLabel("");
		this.add(fehlermeldungFeld);

		for (int i = 13; i < anzahlZeilen; i++) {
			this.add(new JLabel());
		}
	}

	private void erstelleEreignisse() {
		registrierenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String benutzerName = benutzerNameFeld.getText();
				String passwort = passwortFeld.getText();
				String vorname = vornameFeld.getText();
				String nachname = nachnameFeld.getText();
				String strasse = StrasseFeld.getText();
				String postleitzahl = postleitzahlFeld.getText();
				String ort = ortFeld.getText();
				String land = landFeld.getText();

				if (!benutzerName.isEmpty() && !passwort.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty()
						&& !strasse.isEmpty() && !postleitzahl.isEmpty() && !ort.isEmpty() && !land.isEmpty()) {

					try {
						int plz = Integer.parseInt(postleitzahl);
						eShop.fuegeKundeEin(benutzerName, true, passwort, vorname, nachname, strasse, plz, ort, land);
						System.out.println("Sie sind als Kunde registriert.");

						benutzerNameFeld.setText("");
						passwortFeld.setText("");
						vornameFeld.setText("");
						nachnameFeld.setText("");
						StrasseFeld.setText("");
						postleitzahlFeld.setText("");
						ortFeld.setText("");
						landFeld.setText("");

						fehlermeldungFeld.setText("");

						registrierenListener.wennRegistriert();
					} catch (BenutzerExistiertBereitsException bebe) {
						fehlermeldungFeld.setText("Benutzername bereits vorhanden. Probiere erneut.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (NumberFormatException n) {
						fehlermeldungFeld.setText("Geben Sie bei der Postleitzahl Zahlen ein.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
			}
		});
		postleitzahlFeld.addKeyListener(new NumericKeyAdapter());
	}

	class NumericKeyAdapter implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
			char c = e.getKeyChar();
			if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
				getToolkit().beep();
				e.consume();
			}
		}
	}
}
