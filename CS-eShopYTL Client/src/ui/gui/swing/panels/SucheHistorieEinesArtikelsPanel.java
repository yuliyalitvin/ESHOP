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
import domain.exceptions.ArtikelNichtGefundenException;
import interfaces.EShopInterface;
import valueObjects.AnzeigeEreignis;
import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class SucheHistorieEinesArtikelsPanel extends JPanel {

	private EShopInterface eShopInterface = null;
	private JTextField sucheTextFeld;
	private JButton suchButton = null;
	private HistorieTabellenPanel historiePanel;
	private BestandsHistoriePanel bestandsHistoriePanel;

	private JLabel fehlermeldungFeld = null;

	public SucheHistorieEinesArtikelsPanel(EShopInterface verwaltung, HistorieTabellenPanel historiepanel,
			BestandsHistoriePanel bestandshistoriepanel) {
		eShopInterface = verwaltung;
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
		sucheTextFeld.addKeyListener(new NumericKeyAdapter());
	}

	class SuchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getSource().equals(suchButton)) {
				Artikel gewuenschterArtikel = null;
				String gesuchtes = sucheTextFeld.getText();
				java.util.List<AnzeigeEreignis> suchEreignisListe = null;
				if (gesuchtes.isEmpty()) {
					suchEreignisListe = eShopInterface.getAnzeigeEreignisListe();
				} else {
					suchEreignisListe = new Vector<AnzeigeEreignis>();
					try {
						int artikelNummer = Integer.parseInt(gesuchtes);

						gewuenschterArtikel = eShopInterface.sucheArtikel(artikelNummer);
						for (AnzeigeEreignis ereignis : eShopInterface.getAnzeigeEreignisListe()) {
							if (ereignis.getArtikelNummer() == artikelNummer) {
								suchEreignisListe.add(ereignis);
							}
						}
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungFeld.setText("Artikel nicht gefunden.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
				historiePanel.aktualisiereEreignisListe(suchEreignisListe);

				bestandsHistoriePanel.aktualisiereBestandsHistorieGraph(gewuenschterArtikel, eShopInterface);
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
			if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_PERIOD) || (c == KeyEvent.VK_BACK_SPACE)
					|| (c == KeyEvent.VK_DELETE))) {
				getToolkit().beep();
				e.consume();
			}
		}
	}
}
