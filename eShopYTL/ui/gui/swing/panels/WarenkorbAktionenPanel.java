package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
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
import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import domain.exceptions.WarenkorbIstLeerException;
import ui.gui.swing.fenster.RechnungFensterDialog;
import valueObjects.Artikel;
import valueObjects.Benutzer;
import valueObjects.Kunde;
//import valueObjects.Rechnung;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class WarenkorbAktionenPanel extends JPanel {

	public interface WarenkorbListener {
		
		public void warenkorbPanelaktualisieren();
		
		public void stueckzahlGeaendert();

		public void leereWarenkorb();

		public void loescheArtikelAusWarenkorb();

		public void kaufen();
	}

	private Verwaltung eShop = null;
	private Benutzer aktuellerBenutzer = null;
	private WarenkorbListener warenkorbListener = null;

	private JButton aendernButton;
	private JButton leerenButton;
	private JButton loeschenButton;
	private JButton kaufenButton;
	private JTextField nummernTextfeld = null;
	private JTextField loeschenNummernTextfeld = null;
	private JTextField stueckZahlTextfeld = null;

	private JLabel fehlermeldungFeld = null;
	private JLabel fehlermeldungLoeschenFeld = null;

	private RechnungFensterDialog rechnungFenster = null;

	public WarenkorbAktionenPanel(Verwaltung verwaltung, Benutzer benutzer, WarenkorbListener listener) {
		eShop = verwaltung;
		warenkorbListener = listener;
		aktuellerBenutzer = benutzer;

		erstelleUI();
		erstelleEreignisse();
	}

	private void erstelleUI() {
		int anzahlZeilen = 15;
		this.setLayout(new GridLayout(anzahlZeilen, 1));

		this.add(new JLabel("Nummer"));
		nummernTextfeld = new JTextField();
		this.add(nummernTextfeld);
		this.add(new JLabel("Stueckzahl"));
		stueckZahlTextfeld = new JTextField();
		this.add(stueckZahlTextfeld);
		aendernButton = new JButton("Stueckzahl aendern");
		this.add(aendernButton);
		aendernButton.setEnabled(false);
		fehlermeldungFeld = new JLabel("");
		this.add(fehlermeldungFeld);

		this.add(new JLabel("Artikel loeschen:"));

		this.add(new JLabel("Nummer"));
		loeschenNummernTextfeld = new JTextField();
		this.add(loeschenNummernTextfeld);
		loeschenButton = new JButton("Artikel entfernen");
		this.add(loeschenButton);
		loeschenButton.setEnabled(false);
		fehlermeldungLoeschenFeld = new JLabel("");
		this.add(fehlermeldungLoeschenFeld);
		leerenButton = new JButton("Warenkorb leeren");
		this.add(leerenButton);
//		leerenButton.setEnabled(false);

		this.add(new JLabel()); // Abstandhalter
		kaufenButton = new JButton("Artikel kaufen");
		kaufenButton.setPreferredSize(new Dimension(100, 50));
		kaufenButton.setFont(new Font("Arial", Font.BOLD, 15));

		this.add(kaufenButton);
//		kaufenButton.setEnabled(false);
		
		for (int i = 15; i < anzahlZeilen; i++) {
			this.add(new JLabel());
		}
	}

	private void erstelleEreignisse() {
		aendernButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					try {
						Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
						if (warenkorb.isEmpty()) {
							throw new WarenkorbIstLeerException();
						} else {
							int nr = Integer.parseInt(nummer);
							int stueck = Integer.parseInt(stueckzahl);
							Artikel artikel = eShop.sucheArtikel(nr);

							boolean imKorb = artikel.isImKorb();
							imKorb = artikel.isImKorb();

							if (imKorb) {
								eShop.stueckImWarenkorb(stueck, artikel);
								if (stueck == 0) {
									eShop.loescheArtikelAusWarenkorb(artikel, aktuellerBenutzer.getBenutzerIndex());
								}
							} else {
								throw new ArtikelNichtGefundenException();
							}
							nummernTextfeld.setText("");
							stueckZahlTextfeld.setText("");
							aendernButton.setEnabled(false);
							
							warenkorbListener.stueckzahlGeaendert();
//							warenkorbListener.warenkorbPanelaktualisieren();
						}
						fehlermeldungFeld.setText("");
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungFeld.setText("Artikel nicht vorhanden.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
						System.out.println(e.getMessage());
					} catch (NumberFormatException n) {
						fehlermeldungFeld.setText("Bitte Zahlen eingeben.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlException s) {
						fehlermeldungFeld.setText("Stueckzahl hoeher als Bestand.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlEntsprichtNichtPackungException p) {
						fehlermeldungFeld.setText("Stueckzahl entspricht nicht Packungsgroesse.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (WarenkorbIstLeerException e) {
						fehlermeldungFeld.setText("Warenkorb ist leer.");
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
					} catch (StueckzahlException e) {
						System.out.println(e.getMessage());
					} catch (StueckzahlEntsprichtNichtPackungException e) {
						System.out.println(e.getMessage());
					}
				}
				leerenButton.setEnabled(false);
				eShop.loescheWarenkorb(aktuellerBenutzer.getBenutzerIndex());
				warenkorbListener.leereWarenkorb();
//				warenkorbListener.warenkorbPanelaktualisieren();
			}
		});

		loeschenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummer = loeschenNummernTextfeld.getText();
				if (!nummer.isEmpty()) {
					try {
						int nr = Integer.parseInt(nummer);
						Artikel artikel = eShop.sucheArtikel(nr);
						boolean imKorb = artikel.isImKorb();
						if (imKorb) {
							eShop.loescheArtikelAusWarenkorb(artikel, aktuellerBenutzer.getBenutzerIndex());
						} else {
							throw new ArtikelNichtGefundenException();
						}
						loeschenButton.setEnabled(false);
						fehlermeldungLoeschenFeld.setText("");
					} catch (StueckzahlException e) {
						System.out.println(e.getMessage());
					} catch (StueckzahlEntsprichtNichtPackungException e) {
						System.out.println(e.getMessage());
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungLoeschenFeld.setText("Artikel nicht vorhanden.");
						fehlermeldungLoeschenFeld.setForeground(new Color(255, 0, 0));
					}
				}
//				warenkorbListener.warenkorbPanelaktualisieren();
				warenkorbListener.loescheArtikelAusWarenkorb();
			}
		});

		kaufenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());

				for (Artikel artikel : warenkorb) {
					int anzahl = artikel.getStueck(); 
					if (artikel.isMassengut()) {
						anzahl = anzahl * artikel.getPack(); 
					}
					eShop.addHistorieEreignis(artikel.getArtikelNummer(), anzahl, 3,
							aktuellerBenutzer.getBenutzerIndex());
					eShop.bestandAbziehen(artikel);
				}

				warenkorbListener.kaufen();

				if (!warenkorb.isEmpty()) {
					rechnungFenster = new RechnungFensterDialog(null, "", Dialog.ModalityType.DOCUMENT_MODAL,
							(Kunde) aktuellerBenutzer, warenkorb);
					try {
						for (Artikel artikel : warenkorb) {
							eShop.stueckImWarenkorb(0, artikel);
						}
						eShop.loescheWarenkorb(aktuellerBenutzer.getBenutzerIndex());
					} catch (StueckzahlException e1) {
						System.out.println(e1.getMessage());
					} catch (StueckzahlEntsprichtNichtPackungException e1) {
						System.out.println(e1.getMessage());
					}
					rechnungFenster.setBackground(Color.WHITE);
					rechnungFenster.setVisible(true);
				}
			}
		});

		nummernTextfeld.addKeyListener(new buttonAktivieren());
		stueckZahlTextfeld.addKeyListener(new buttonAktivieren());
		loeschenNummernTextfeld.addKeyListener(new buttonAktivieren());
		
		nummernTextfeld.addKeyListener(new NumericKeyAdapter());
		stueckZahlTextfeld.addKeyListener(new NumericKeyAdapter());
		loeschenNummernTextfeld.addKeyListener(new NumericKeyAdapter());
	}
	class buttonAktivieren implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
			String nummerloeschen = loeschenNummernTextfeld.getText();
			if (!warenkorb.isEmpty()) {
//				kaufenButton.setEnabled(true);
//				leerenButton.setEnabled(true);
				if (!nummerloeschen.isEmpty()) {
					loeschenButton.setEnabled(true);
				}else {
					loeschenButton.setEnabled(false);
				}
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					aendernButton.setEnabled(true);
				}else {
					aendernButton.setEnabled(false);
				}
			}else {
//				kaufenButton.setEnabled(false);
//				leerenButton.setEnabled(false);
				loeschenButton.setEnabled(false);
				aendernButton.setEnabled(false);
			}
			warenkorbListener.warenkorbPanelaktualisieren();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
			String nummerloeschen = loeschenNummernTextfeld.getText();
			if (!warenkorb.isEmpty()) {
//				kaufenButton.setEnabled(true);
//				leerenButton.setEnabled(true);
				if (!nummerloeschen.isEmpty()) {
					loeschenButton.setEnabled(true);
				}else {
					loeschenButton.setEnabled(false);
				}
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					aendernButton.setEnabled(true);
				}else {
					aendernButton.setEnabled(false);
				}
			}else {
//				kaufenButton.setEnabled(false);
//				leerenButton.setEnabled(false);
				loeschenButton.setEnabled(false);
				aendernButton.setEnabled(false);
			}
			warenkorbListener.warenkorbPanelaktualisieren();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			Vector<Artikel> warenkorb = eShop.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
			String nummerloeschen = loeschenNummernTextfeld.getText();
			if (!warenkorb.isEmpty()) {
//				kaufenButton.setEnabled(true);
//				leerenButton.setEnabled(true);
				if (!nummerloeschen.isEmpty()) {
					loeschenButton.setEnabled(true);
				}else {
					loeschenButton.setEnabled(false);
				}
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					aendernButton.setEnabled(true);
				}else {
					aendernButton.setEnabled(false);
				}
			}else {
//				kaufenButton.setEnabled(false);
//				leerenButton.setEnabled(false);
				loeschenButton.setEnabled(false);
				aendernButton.setEnabled(false);
			}
			warenkorbListener.warenkorbPanelaktualisieren();
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