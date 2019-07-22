package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import domain.Verwaltung;
import domain.exceptions.ArtikelNichtGefundenException;
import valueObjects.AnzeigeEreignis;
import valueObjects.Artikel;
import valueObjects.Ereignis;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class SucheHistorieEinesArtikelsPanel extends JPanel {

	private Verwaltung eShop = null;
	private JTextField sucheTextFeld;
	private JButton suchButton = null;
	private HistorieTabellenPanel historiePanel;
	private BestandsHistoriePanel bestandsHistoriePanel;

	private JLabel fehlermeldungFeld = null;

	public SucheHistorieEinesArtikelsPanel(Verwaltung verwaltung, HistorieTabellenPanel historiepanel,
			BestandsHistoriePanel bestandshistoriepanel) {
		eShop = verwaltung;
		historiePanel = historiepanel;
		bestandsHistoriePanel = bestandshistoriepanel;

		erstelleUI();
		erstelleEreignisse();
	}

	private void erstelleUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 0; // Zeile 0

		JLabel sucheLabel = new JLabel("Artikelnummer:");
		constraints.gridx = 0; // Spalte 0
		constraints.weightx = 0.2; // 20% der gesamten Breite
		constraints.anchor = GridBagConstraints.EAST;
		gridBagLayout.setConstraints(sucheLabel, constraints);
		this.add(sucheLabel);

		sucheTextFeld = new JTextField();
		sucheTextFeld.setToolTipText("Wenn Sie die Historie eines einzelnen Artikels ansehen wollen,"
				+ " geben Sie hier bitte die Artikelnummer Ihres gewuenschten Artikels ein.");
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
				Artikel gewuenschterArtikel = null;
				String gesuchtes = sucheTextFeld.getText();
				java.util.List<AnzeigeEreignis> suchEreignisListe = null;
				if (gesuchtes.isEmpty()) {
					suchEreignisListe = eShop.getAnzeigeEreignisListe();
				} else {
					suchEreignisListe = new Vector<AnzeigeEreignis>();
					try {
						int artikelNummer = Integer.parseInt(gesuchtes);

						gewuenschterArtikel = eShop.sucheArtikel(artikelNummer);
						for (AnzeigeEreignis ereignis : eShop.getAnzeigeEreignisListe()) {
							if (ereignis.getArtikelNummer() == artikelNummer) {
								suchEreignisListe.add(ereignis);
							}
						}
					} catch (NumberFormatException e) {
						fehlermeldungFeld.setText("Fehlerhafte Eingabe! Geben Sie bitte Zahlen ein.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungFeld.setText("Artikel nicht gefunden.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
				historiePanel.aktualisiereEreignisListe(suchEreignisListe);

				bestandsHistoriePanel.aktualisiereBestandsHistorieGraph(gewuenschterArtikel, eShop);
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
