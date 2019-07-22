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

import domain.exceptions.ArtikelBereitsImWarenkorbException;
import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import interfaces.EShopInterface;
import valueObjects.Artikel;
import valueObjects.Benutzer;

/**
 * 
 * Feld fuer Kunden um Artikel in den Warenkorb hinzu zufuegen
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class FuegeArtikelInWarenkorbPanel extends JPanel {

	public interface FelderVollListener {
		public void aktikelPanelaktualisieren();
	}

	private EShopInterface eShopInterface = null;
	private Benutzer aktuellerBenutzer = null;

	private FelderVollListener felderVollListener = null;

	private JButton hinzufuegenButton;
	private JTextField nummernTextfeld = null;
	private JTextField stueckZahlTextfeld = null;
	private JLabel fehlermeldungFeld = null;

	public FuegeArtikelInWarenkorbPanel(EShopInterface verwaltung, Benutzer benutzer, FelderVollListener listener) {
		eShopInterface = verwaltung;
		aktuellerBenutzer = benutzer;
		felderVollListener = listener;

		erstelleUI();
		erstelleEreignisse();
	}

	private void erstelleUI() {
		int anzahlZeilen = 13;
		this.setLayout(new GridLayout(anzahlZeilen, 1));

		this.add(new JLabel("Nummer"));
		nummernTextfeld = new JTextField();
		this.add(nummernTextfeld);
		this.add(new JLabel("Stueckzahl"));
		stueckZahlTextfeld = new JTextField();
		this.add(stueckZahlTextfeld);

		this.add(new JLabel()); // Abstandhalter

		hinzufuegenButton = new JButton("Hinzufuegen");
		this.add(hinzufuegenButton);
		hinzufuegenButton.setEnabled(false);
		fehlermeldungFeld = new JLabel("");
		this.add(fehlermeldungFeld);

		for (int i = 13; i < anzahlZeilen; i++) {
			this.add(new JLabel());
		}
	}

	private void erstelleEreignisse() {
		hinzufuegenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					String reserviert = "";
					try {
						int nr = Integer.parseInt(nummer);
						int stueck = Integer.parseInt(stueckzahl);
						Artikel artikel = eShopInterface.sucheArtikel(nr);

						boolean imKorb = eShopInterface.istImKorb(artikel, aktuellerBenutzer.getBenutzerIndex());
						if (!imKorb) {
							if (artikel.getStueck() > 0) {
								reserviert = artikel.getStueck() + " Stueck sind bereits reserviert.";
							}
							eShopInterface.fuegeZuWarenkorb(artikel, aktuellerBenutzer.getBenutzerIndex(), stueck);

						} else {
							throw new ArtikelBereitsImWarenkorbException();
						}
						nummernTextfeld.setText("");
						stueckZahlTextfeld.setText("");
						fehlermeldungFeld.setText("");

					} catch (NumberFormatException n) {
						fehlermeldungFeld.setText("Bitte geben Sie Zahlen ein.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlException s) {
						fehlermeldungFeld.setText("Bestand nicht ausreichend. " + reserviert);
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlEntsprichtNichtPackungException p) {
						fehlermeldungFeld.setText("Die Stueckzahl entspricht nicht der Packung.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungFeld.setText("Artikel nicht vorhanden.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (ArtikelBereitsImWarenkorbException e) {
						fehlermeldungFeld.setText("Artikel bereits im Warenkorb.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					}
				}
			}
		});

		nummernTextfeld.addKeyListener(new buttonAktivieren());
		stueckZahlTextfeld.addKeyListener(new buttonAktivieren());
		nummernTextfeld.addKeyListener(new NumericKeyAdapter());
		stueckZahlTextfeld.addKeyListener(new NumericKeyAdapter());
	}

	class buttonAktivieren implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			String nummer = nummernTextfeld.getText();
			String stueckzahl = stueckZahlTextfeld.getText();
			if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
			} else {
				hinzufuegenButton.setEnabled(false);
			}
			felderVollListener.aktikelPanelaktualisieren();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			String nummer = nummernTextfeld.getText();
			String stueckzahl = stueckZahlTextfeld.getText();
			if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
			} else {
				hinzufuegenButton.setEnabled(false);
			}
			felderVollListener.aktikelPanelaktualisieren();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			String nummer = nummernTextfeld.getText();
			String stueckzahl = stueckZahlTextfeld.getText();
			if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
			} else {
				hinzufuegenButton.setEnabled(false);
			}
			felderVollListener.aktikelPanelaktualisieren();
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
			if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
				getToolkit().beep();
				e.consume();
			}
		}
	}
}
