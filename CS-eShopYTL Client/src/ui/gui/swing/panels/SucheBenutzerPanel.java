package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.exceptions.BenutzerNichtGefundenException;
import interfaces.EShopInterface;
import valueObjects.Benutzer;

/**
 * Layout und Funktionen des Suchen Bereiches
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class SucheBenutzerPanel extends JPanel {

	public interface SucheBenutzerPanelListener {
		public void beiSuchErgebnisBenutzer(List<Benutzer> benutzerListe);
	}

	private EShopInterface eShopInterface = null;
	private SucheBenutzerPanelListener sucheListener = null;

	private JTextField sucheTextFeld;
	private JButton suchButton = null;

	private JLabel fehlermeldungFeld = null;

	public SucheBenutzerPanel(EShopInterface verwaltung, SucheBenutzerPanelListener listener) {
		eShopInterface = verwaltung;
		sucheListener = listener;

		erstelleUI();
		erstelleEreignisse();
	}

	/**
	 * Erstellen des Layouts
	 */
	private void erstelleUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 0; // Zeile 0

		JLabel sucheLabel = new JLabel("Benutzername:");
		constraints.gridx = 0; // Spalte 0
		constraints.weightx = 0.2; // 20% der gesamten Breite
		constraints.anchor = GridBagConstraints.EAST;
		gridBagLayout.setConstraints(sucheLabel, constraints);
		this.add(sucheLabel);

		sucheTextFeld = new JTextField();
		sucheTextFeld.setToolTipText("Suchbegriff eingeben.");
		constraints.gridx = 1; // Spalte 1
		constraints.weightx = 0.6; // 60% der gesamten Breite
		gridBagLayout.setConstraints(sucheTextFeld, constraints);
		this.add(sucheTextFeld);

		suchButton = new JButton("Suchen!");
		constraints.gridx = 2; // Spalte 2
		constraints.weightx = 0.2; // 20% der Gesammtbreite
		gridBagLayout.setConstraints(suchButton, constraints);
		this.add(suchButton);

		fehlermeldungFeld = new JLabel("");
		constraints.gridy = 1; // Zeile 1
		constraints.gridx = 1; // Spalte 1
		constraints.weightx = 0.2; // 20% der Gesammtbreite
		gridBagLayout.setConstraints(fehlermeldungFeld, constraints);
		this.add(fehlermeldungFeld);
	}

	/**
	 * Hinzufuegen der Funktionen
	 */
	private void erstelleEreignisse() {
		suchButton.addActionListener(new SuchListener());
		sucheTextFeld.addKeyListener(new TextFeldListener());
	}

	class SuchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getSource().equals(suchButton)) {
				String gesuchtes = sucheTextFeld.getText();
				java.util.List<Benutzer> suchErgebnis = null;
				if (gesuchtes.isEmpty()) {
					suchErgebnis = eShopInterface.gibAlleBenutzer();
				} else {
					try {
						suchErgebnis = eShopInterface.sucheBenutzerNachBenutzername(gesuchtes);
					} catch (BenutzerNichtGefundenException e) {
						fehlermeldungFeld.setText("Benutzer nicht gefunden. Gib Benutzernamen erneut ein.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
				if (suchErgebnis != null) {
					sucheListener.beiSuchErgebnisBenutzer(suchErgebnis);
				}
			}
		}
	}

	class TextFeldListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			fehlermeldungFeld.setText("");
		}
	}
}
