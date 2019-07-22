package ui.gui.swing.panels;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.exceptions.ArtikelNichtGefundenException;
import interfaces.EShopInterface;
import valueObjects.Artikel;

/**
 * Suchleiste fuer die Artikelliste
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class SucheArtikelPanel extends JPanel {

	public interface SucheArtikelPanelListener {
		public void beiSuchErgebnisArtikel(List<Artikel> artikelListe);
	}

	private EShopInterface eShopInterface = null;
	private SucheArtikelPanelListener sucheListener = null;

	private JTextField sucheTextFeld;
	private JButton suchButton = null;

	private JLabel fehlermeldungFeld = null;

	public SucheArtikelPanel(EShopInterface verwaltung, SucheArtikelPanelListener listener) {
		eShopInterface = verwaltung;
		sucheListener = listener;

		erstelleUI();
		erstelleEreignisse();
	}

	private void erstelleUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 0; // Zeile 0

		JLabel sucheLabel = new JLabel("Artikelname:");
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

	private void erstelleEreignisse() {
		suchButton.addActionListener(new SuchListener());
		sucheTextFeld.addKeyListener(new TextFeldListener());
	}

	class SuchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getSource().equals(suchButton)) {
				String gesuchtes = sucheTextFeld.getText();
				java.util.List<Artikel> suchArtikelListe = null;
				if (gesuchtes.isEmpty()) {
					suchArtikelListe = eShopInterface.gibAlleArtikel();
				} else {
					suchArtikelListe = new Vector<Artikel>();
					try {
						String artikelName = gesuchtes;
						suchArtikelListe = eShopInterface.sucheNachArtikelname(artikelName);
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungFeld.setText("Artikel nicht gefunden. Gib Artikelnamen erneut ein.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
				if (suchArtikelListe != null) {
					sucheListener.beiSuchErgebnisArtikel(suchArtikelListe);
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
