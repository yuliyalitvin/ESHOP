package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridLayout;
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
import domain.exceptions.ArtikelBereitsImWarenkorbException;
import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import valueObjects.Artikel;
import valueObjects.Benutzer;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class FuegeArtikelInWarenkorbPanel extends JPanel {

	public interface FelderVollListener {
		public void aktikelPanelaktualisieren();
	}
	
	
	private Verwaltung eShop = null;
	private Benutzer aktuellerBenutzer = null;
	
	private FelderVollListener felderVollListener = null;

	private JButton hinzufuegenButton;
	private JButton leerenButton;
	private JTextField nummernTextfeld = null;
	private JTextField stueckZahlTextfeld = null;
	private JLabel fehlermeldungFeld = null;

	public FuegeArtikelInWarenkorbPanel(Verwaltung verwaltung, Benutzer benutzer, FelderVollListener listener) {
		eShop = verwaltung;
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
		leerenButton = new JButton("Warenkorb leeren");
		this.add(leerenButton);
		leerenButton.setEnabled(false);
		
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
					try {
						int nr = Integer.parseInt(nummer);
						int stueck = Integer.parseInt(stueckzahl);
						Artikel artikel = eShop.sucheArtikel(nr);

						boolean imKorb = artikel.isImKorb();
						if (!imKorb) {
							eShop.stueckImWarenkorb(stueck, artikel);
							eShop.fuegeZuWarenkorb(artikel, aktuellerBenutzer.getBenutzerIndex());

							Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
							System.out.println(warenkorb);

							System.out.print(artikel.getArtikelName() + " wurde in Warenkorb eingefügt.");
						} else {
							throw new ArtikelBereitsImWarenkorbException();
						}
						nummernTextfeld.setText("");
						stueckZahlTextfeld.setText("");
						fehlermeldungFeld.setText("");
						
						leerenButton.setEnabled(true);
					} catch (NumberFormatException n) {
						fehlermeldungFeld.setText("Bitte geben Sie Zahlen ein.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlException s) {
						fehlermeldungFeld.setText("Die Stückzahl ist höher als der verfügbare Bestand.");
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

		leerenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());

				for (Artikel artikel : warenkorb) {
					try {
						eShop.stueckImWarenkorb(0, artikel);
						leerenButton.setEnabled(false);
					} catch (StueckzahlException e) {
						System.out.println(e.getMessage());
					} catch (StueckzahlEntsprichtNichtPackungException e) {
						System.out.println(e.getMessage());
					}
				}
				eShop.loescheWarenkorb(aktuellerBenutzer.getBenutzerIndex());
				System.out.println(warenkorb);
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
			}else {
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
			}else {
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
			}else {
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
