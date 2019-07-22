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

import domain.exceptions.BenutzerExistiertBereitsException;
import interfaces.EShopInterface;
import valueObjects.Benutzer;

/**
 * Layout und Funktionen zum Mitarbeiter hinzufuegen Bereich
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class FuegeBenutzerHinzuPanel extends JPanel {

	public interface BenutzerHinzufuegenListener {
		public void benutzerHinzugefuegt(Benutzer benutzer);

		public void benutzerPanelaktualisieren();
	}

	private EShopInterface eShopInterface = null;
	private BenutzerHinzufuegenListener hinzufuegenListener = null;
	private BenutzerHinzufuegenListener panelaktualisieren = null;

	private JButton hinzufuegenButton;
	private JTextField benutzernameTextfeld = null;
	private JTextField passwortTextfeld = null;
	private JTextField vornameTextfeld = null;
	private JTextField nachnameTextfeld = null;
	private JLabel fehlermeldungFeld = null;

	public FuegeBenutzerHinzuPanel(EShopInterface verwaltung, BenutzerHinzufuegenListener listener) {
		eShopInterface = verwaltung;
		hinzufuegenListener = listener;
		panelaktualisieren = listener;

		erstelleUI();
		erstelleEreignisse();
	}

	/**
	 * Erstellen des Layouts
	 */
	private void erstelleUI() {
		int anzahlZeilen = 13;
		this.setLayout(new GridLayout(anzahlZeilen, 1));

		this.add(new JLabel("Benutzername: "));
		benutzernameTextfeld = new JTextField();
		this.add(benutzernameTextfeld);
		this.add(new JLabel("Passwort: "));
		passwortTextfeld = new JTextField();
		this.add(passwortTextfeld);
		this.add(new JLabel("Vorname: "));
		vornameTextfeld = new JTextField();
		this.add(vornameTextfeld);
		this.add(new JLabel("Nachname: "));
		nachnameTextfeld = new JTextField();
		this.add(nachnameTextfeld);
		this.add(new JLabel()); // Abstandhalter
		hinzufuegenButton = new JButton("Hinzufuegen");
		this.add(hinzufuegenButton);
		hinzufuegenButton.setEnabled(false);
		this.add(new JLabel()); // Abstandhalter
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
		hinzufuegenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String benutzername = benutzernameTextfeld.getText();
				String passwort = passwortTextfeld.getText();
				String vorname = vornameTextfeld.getText();
				String nachname = nachnameTextfeld.getText();

				if (!benutzername.isEmpty() && !passwort.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty()) {
					try {
						Benutzer benutzer = eShopInterface.fuegeMitarbeiterEin(benutzername, false, passwort, vorname,
								nachname);

						benutzernameTextfeld.setText("");
						passwortTextfeld.setText("");
						vornameTextfeld.setText("");
						nachnameTextfeld.setText("");

						fehlermeldungFeld.setText("");
						hinzufuegenButton.setEnabled(false);
						hinzufuegenListener.benutzerHinzugefuegt(benutzer);
					} catch (BenutzerExistiertBereitsException bebe) {
						fehlermeldungFeld.setText("Benutzername existiert bereits. ");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
			}
		});
		benutzernameTextfeld.addKeyListener(new buttonAktivieren());
		passwortTextfeld.addKeyListener(new buttonAktivieren());
		vornameTextfeld.addKeyListener(new buttonAktivieren());
		nachnameTextfeld.addKeyListener(new buttonAktivieren());

		benutzernameTextfeld.addKeyListener(new NumericKeyAdapter());
		passwortTextfeld.addKeyListener(new NumericKeyAdapter());
		vornameTextfeld.addKeyListener(new NumericKeyAdapter());
		nachnameTextfeld.addKeyListener(new NumericKeyAdapter());
	}

	class buttonAktivieren implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			String benutzername = benutzernameTextfeld.getText();
			String passwort = passwortTextfeld.getText();
			String vorname = vornameTextfeld.getText();
			String nachname = nachnameTextfeld.getText();
			if (!benutzername.isEmpty() && !passwort.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
			} else {
				hinzufuegenButton.setEnabled(false);
			}
			panelaktualisieren.benutzerPanelaktualisieren();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			String benutzername = benutzernameTextfeld.getText();
			String passwort = passwortTextfeld.getText();
			String vorname = vornameTextfeld.getText();
			String nachname = nachnameTextfeld.getText();
			if (!benutzername.isEmpty() && !passwort.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
			} else {
				hinzufuegenButton.setEnabled(false);
			}
			panelaktualisieren.benutzerPanelaktualisieren();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			String benutzername = benutzernameTextfeld.getText();
			String passwort = passwortTextfeld.getText();
			String vorname = vornameTextfeld.getText();
			String nachname = nachnameTextfeld.getText();
			if (!benutzername.isEmpty() && !passwort.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
			} else {
				hinzufuegenButton.setEnabled(false);
			}
			panelaktualisieren.benutzerPanelaktualisieren();
		}
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
			if (((c == KeyEvent.VK_PERIOD) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
				getToolkit().beep();
				e.consume();
			}
		}
	}
}
