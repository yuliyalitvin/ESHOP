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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import domain.exceptions.StueckzahlException;
import domain.exceptions.WarenkorbIstLeerException;
import interfaces.EShopInterface;
import ui.gui.swing.fenster.RechnungFensterDialog;
import valueObjects.Artikel;
import valueObjects.ArtikelImWarenkorb;
import valueObjects.Benutzer;
import valueObjects.Kunde;

/**
 * Bereich um im Warenkorb die Artikel zu bearbeiten
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class WarenkorbAktionenPanel extends JPanel {

	public interface WarenkorbListener {

		public void warenkorbPanelaktualisieren();

		public void stueckzahlGeaendert();

		public void leereWarenkorb();

		public void loescheArtikelAusWarenkorb();

		public void kaufen();
	}

	private EShopInterface eShopInterface = null;
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

	public WarenkorbAktionenPanel(EShopInterface verwaltung, Benutzer benutzer, WarenkorbListener listener) {
		eShopInterface = verwaltung;
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

		this.add(new JLabel()); // Abstandhalter

		kaufenButton = new JButton("Artikel kaufen");
		kaufenButton.setPreferredSize(new Dimension(100, 50));
		kaufenButton.setFont(new Font("Arial", Font.BOLD, 15));

		this.add(kaufenButton);
		List<ArtikelImWarenkorb> warenkorb = eShopInterface.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
		if (warenkorb.isEmpty()) {
			kaufenButton.setEnabled(false);
			leerenButton.setEnabled(false);
		} else {
			kaufenButton.setEnabled(true);
			leerenButton.setEnabled(true);
		}

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
						List<ArtikelImWarenkorb> warenkorb = eShopInterface
								.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
						if (warenkorb.isEmpty()) {
							throw new WarenkorbIstLeerException();
						} else {
							int nr = Integer.parseInt(nummer);
							int stueck = Integer.parseInt(stueckzahl);
							Artikel artikel = eShopInterface.sucheArtikel(nr);

							boolean imKorb = eShopInterface.istImKorb(artikel, aktuellerBenutzer.getBenutzerIndex());
							if (imKorb) {
								eShopInterface.anzahlImKorbAendern(artikel, aktuellerBenutzer.getBenutzerIndex(),
										stueck);
								if (stueck == 0) {
									eShopInterface.loescheArtikelAusWarenkorb(artikel,
											aktuellerBenutzer.getBenutzerIndex());
								}
							} else {
								throw new ArtikelNichtGefundenException();
							}
							nummernTextfeld.setText("");
							stueckZahlTextfeld.setText("");
							aendernButton.setEnabled(false);

							warenkorbListener.stueckzahlGeaendert();
						}
						fehlermeldungFeld.setText("");
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungFeld.setText("Artikel nicht vorhanden.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (NumberFormatException n) {
						fehlermeldungFeld.setText("Bitte Zahlen eingeben.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlException s) {
						fehlermeldungFeld.setText("Stueckzahl hoeher als Bestand.");
						fehlermeldungFeld.setForeground(new Color(255, 0, 0));
						warenkorbListener.stueckzahlGeaendert();
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
				List<ArtikelImWarenkorb> warenkorb = eShopInterface.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());

				for (ArtikelImWarenkorb artikelImKorb : warenkorb) {
					try {
						eShopInterface.anzahlImKorbAendern(artikelImKorb.artikel, aktuellerBenutzer.getBenutzerIndex(),
								0);

						eShopInterface.loescheArtikelAusWarenkorb(artikelImKorb.artikel,
								aktuellerBenutzer.getBenutzerIndex());

					} catch (StueckzahlException e) {
						System.out.println(e.getMessage());
					} catch (StueckzahlEntsprichtNichtPackungException e) {
						System.out.println(e.getMessage());
					}
				}

				kaufenButton.setEnabled(false);

				leerenButton.setEnabled(false);

				warenkorbListener.leereWarenkorb();
			}
		});

		loeschenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummer = loeschenNummernTextfeld.getText();
				if (!nummer.isEmpty()) {
					try {
						int nr = Integer.parseInt(nummer);
						Artikel artikel = eShopInterface.sucheArtikel(nr);
						boolean imKorb = eShopInterface.istImKorb(artikel, aktuellerBenutzer.getBenutzerIndex());
						if (imKorb) {
							eShopInterface.anzahlImKorbAendern(artikel, aktuellerBenutzer.getBenutzerIndex(), 0);
							eShopInterface.loescheArtikelAusWarenkorb(artikel, aktuellerBenutzer.getBenutzerIndex());

							List<ArtikelImWarenkorb> warenkorb = eShopInterface
									.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
							if (warenkorb.isEmpty()) {
								kaufenButton.setEnabled(false);
								leerenButton.setEnabled(false);
							} else {
								kaufenButton.setEnabled(true);
								leerenButton.setEnabled(true);
							}
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
				warenkorbListener.loescheArtikelAusWarenkorb();
			}
		});

		kaufenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				List<ArtikelImWarenkorb> warenkorbListe = eShopInterface
						.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());

				for (ArtikelImWarenkorb artikelImKorb : warenkorbListe) {
					int anzahl = artikelImKorb.getAnzahlImWarenkorb();
					if (artikelImKorb.artikel.isMassengut()) {
						anzahl = anzahl * artikelImKorb.artikel.getPack();
					}
					eShopInterface.addHistorieEreignis(artikelImKorb.artikel.getArtikelNummer(), anzahl, 3,
							aktuellerBenutzer.getBenutzerIndex());
					eShopInterface.bestandAbziehen(artikelImKorb);
				}

				warenkorbListener.kaufen();

				if (!warenkorbListe.isEmpty()) {
					rechnungFenster = new RechnungFensterDialog(null, "", Dialog.ModalityType.DOCUMENT_MODAL,
							(Kunde) aktuellerBenutzer, warenkorbListe);
					try {
						for (ArtikelImWarenkorb artikelImWarenkorb : warenkorbListe) {
							eShopInterface.loescheArtikelAusWarenkorb(artikelImWarenkorb.artikel,
									aktuellerBenutzer.getBenutzerIndex());
						}
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

	/**
	 * 
	 * klasse um die Buttons zu aktivieren
	 * 
	 */
	class buttonAktivieren implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			List<ArtikelImWarenkorb> warenkorb = eShopInterface.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
			String nummerloeschen = loeschenNummernTextfeld.getText();
			if (!warenkorb.isEmpty()) {
				if (!nummerloeschen.isEmpty()) {
					loeschenButton.setEnabled(true);
				} else {
					loeschenButton.setEnabled(false);
				}
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					aendernButton.setEnabled(true);
				} else {
					aendernButton.setEnabled(false);
				}
			} else {
				loeschenButton.setEnabled(false);
				aendernButton.setEnabled(false);
			}
			warenkorbListener.warenkorbPanelaktualisieren();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			List<ArtikelImWarenkorb> warenkorb = eShopInterface.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
			String nummerloeschen = loeschenNummernTextfeld.getText();
			if (!warenkorb.isEmpty()) {
				if (!nummerloeschen.isEmpty()) {
					loeschenButton.setEnabled(true);
				} else {
					loeschenButton.setEnabled(false);
				}
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					aendernButton.setEnabled(true);
				} else {
					aendernButton.setEnabled(false);
				}
			} else {
				loeschenButton.setEnabled(false);
				aendernButton.setEnabled(false);
			}
			warenkorbListener.warenkorbPanelaktualisieren();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			List<ArtikelImWarenkorb> warenkorb = eShopInterface.gibWarenkorb(aktuellerBenutzer.getBenutzerIndex());
			String nummerloeschen = loeschenNummernTextfeld.getText();
			if (!warenkorb.isEmpty()) {
				if (!nummerloeschen.isEmpty()) {
					loeschenButton.setEnabled(true);
				} else {
					loeschenButton.setEnabled(false);
				}
				String nummer = nummernTextfeld.getText();
				String stueckzahl = stueckZahlTextfeld.getText();

				if (!nummer.isEmpty() && !stueckzahl.isEmpty()) {
					aendernButton.setEnabled(true);
				} else {
					aendernButton.setEnabled(false);
				}
			} else {
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