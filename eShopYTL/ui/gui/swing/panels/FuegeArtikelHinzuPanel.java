package ui.gui.swing.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.Verwaltung;
import domain.exceptions.ArtikelExistiertBereitsException;
import domain.exceptions.ArtikelNichtGefundenException;
import domain.exceptions.StueckzahlEntsprichtNichtPackungException;
import valueObjects.Artikel;
import valueObjects.Benutzer;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class FuegeArtikelHinzuPanel extends JPanel {

	public interface ArtikelHinzufuegenListener {
		
		public void aktikelPanelaktualisieren();
		
		public void artikelHinzugefuegt(Artikel artikel);

		public void artikelGeaedert(Artikel artikel);
	}

	private Verwaltung eShop = null;
	private ArtikelHinzufuegenListener panelaktualisieren = null;
	private ArtikelHinzufuegenListener hinzufuegenListener = null;
	private ArtikelHinzufuegenListener bestandListener = null;
	private Benutzer aktuellerBenutzer = null;

	private JButton hinzufuegenButton;
	private JTextField nummernTextfeld = null;
	private JTextField namenTextfeld = null;
	private JTextField preisTextfeld = null;
	private JTextField anzahlTextfeld = null;
	private JCheckBox massengutBox;
	private JTextField packTextfeld = null;
	private JLabel fehlermeldungHinzufuegenFeld = null;
	private JLabel fehlermeldungBestandFeld = null;

	private JButton aedernBestandButton;
	private JTextField nummernBestandTextfeld = null;
	private JTextField anzahlBestandTextfeld = null;

	public FuegeArtikelHinzuPanel(Verwaltung verwaltung, Benutzer benutzer, ArtikelHinzufuegenListener listener) {
		eShop = verwaltung;
		panelaktualisieren = listener;
		hinzufuegenListener = listener;
		bestandListener = listener;
		aktuellerBenutzer = benutzer;

		erstelleUI();
		erstelleEreignisse();
	}

	private void erstelleUI() {
		int anzahlZeilen = 13;
		this.setLayout(new GridLayout(anzahlZeilen, 1));

		this.add(new JLabel("Nummer: "));
		nummernTextfeld = new JTextField();
		this.add(nummernTextfeld);
		this.add(new JLabel("Name: "));
		namenTextfeld = new JTextField();
		this.add(namenTextfeld);
		this.add(new JLabel("Preis: "));
		preisTextfeld = new JTextField();
		this.add(preisTextfeld);
		this.add(new JLabel("Anzahl: "));
		anzahlTextfeld = new JTextField();
		this.add(anzahlTextfeld);
		this.add(new JLabel("Massengutartikel: "));
		massengutBox = new JCheckBox();
		this.add(massengutBox);
		this.add(new JLabel("Packung: "));
		packTextfeld = new JTextField("");
		this.add(packTextfeld);
		packTextfeld.setEnabled(false);
		

		this.add(new JLabel()); // Abstandhalter
		hinzufuegenButton = new JButton("Hinzufuegen");
		this.add(hinzufuegenButton);
		hinzufuegenButton.setEnabled(false);

		this.add(new JLabel()); // Abstandhalter

		fehlermeldungHinzufuegenFeld = new JLabel("");
		this.add(fehlermeldungHinzufuegenFeld);

		this.add(new JLabel("Bestand eines Artikels erhoehen:"));

		this.add(new JLabel());

		this.add(new JLabel("Nummer: "));
		nummernBestandTextfeld = new JTextField();
		this.add(nummernBestandTextfeld);
		this.add(new JLabel("Anzahl hinzufügen: "));
		anzahlBestandTextfeld = new JTextField();
		this.add(anzahlBestandTextfeld);

		this.add(new JLabel()); // Abstandhalter
		aedernBestandButton = new JButton("Bestand erhoehen");
		this.add(aedernBestandButton);
		aedernBestandButton.setEnabled(false);
		this.add(new JLabel()); // Abstandhalter

		fehlermeldungBestandFeld = new JLabel("");
		this.add(fehlermeldungBestandFeld);

		for (int i = 13; i < anzahlZeilen; i++) {
			this.add(new JLabel());
		}
	}

	private void erstelleEreignisse() {
		
		massengutBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummer = nummernTextfeld.getText();
				String name = namenTextfeld.getText();
				String preis = preisTextfeld.getText();
				String anzahl = anzahlTextfeld.getText();
				if(!massengutBox.isSelected()) {
					packTextfeld.setEnabled(false);
					if (!nummer.isEmpty() && !name.isEmpty() && !preis.isEmpty() && !anzahl.isEmpty()) {
						hinzufuegenButton.setEnabled(true);
					}
				}else {
					packTextfeld.setEnabled(true);
					hinzufuegenButton.setEnabled(false);
				}
				
				panelaktualisieren.aktikelPanelaktualisieren();
			}
		});

		hinzufuegenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummer = nummernTextfeld.getText();
				String name = namenTextfeld.getText();
				String preis = preisTextfeld.getText();
				String anzahl = anzahlTextfeld.getText();
				boolean massengutartikel = massengutBox.isSelected();
				String packung = packTextfeld.getText();
				int pack = 0;

				if (!nummer.isEmpty() && !name.isEmpty() && !preis.isEmpty() && !anzahl.isEmpty()) {
					try {
						int nr = Integer.parseInt(nummer);
						float pr = Float.parseFloat(preis);
						int anz = Integer.parseInt(anzahl);

						if (massengutartikel) {
							pack = Integer.parseInt(packung);
						}
						Artikel artikel = null;

						artikel = eShop.fuegeArtikelEin(name, nr, pr, anz, massengutartikel, pack);
						eShop.addHistorieEreignis(nr, anz, 1, aktuellerBenutzer.getBenutzerIndex());

						nummernTextfeld.setText("");
						namenTextfeld.setText("");
						preisTextfeld.setText("");
						anzahlTextfeld.setText("");
						massengutBox.setText("");
						packTextfeld.setText("");
						hinzufuegenButton.setEnabled(false);
						hinzufuegenListener.artikelHinzugefuegt(artikel);
					} catch (NumberFormatException nfe) {
						fehlermeldungHinzufuegenFeld.setText("Geben Sie bitte Zahlen ein.");
						fehlermeldungHinzufuegenFeld.setForeground(new Color(255, 0, 0));
					} catch (ArtikelExistiertBereitsException aebe) {
						fehlermeldungHinzufuegenFeld.setText("Artikelnummer bereits vorhanden.");
						fehlermeldungHinzufuegenFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlEntsprichtNichtPackungException e) {
						fehlermeldungHinzufuegenFeld.setText("Die Stueckzahl entspricht nicht der Packung.");
						fehlermeldungHinzufuegenFeld.setForeground(new Color(255, 0, 0));
					}
				}
			}
		});

		aedernBestandButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String nummerBestand = nummernBestandTextfeld.getText();
				String anzahlBestand = anzahlBestandTextfeld.getText();

				if (!nummerBestand.isEmpty() && !anzahlBestand.isEmpty()) {
					try {
						int nr = Integer.parseInt(nummerBestand);
						int plus = Integer.parseInt(anzahlBestand);
						Artikel artikel = eShop.sucheArtikel(nr);
						eShop.bestandErhoehen(plus, artikel);
						eShop.addHistorieEreignis(nr, plus, 2, aktuellerBenutzer.getBenutzerIndex());

						nummernBestandTextfeld.setText("");
						anzahlBestandTextfeld.setText("");
						
						aedernBestandButton.setEnabled(false);
						panelaktualisieren.aktikelPanelaktualisieren();

						fehlermeldungBestandFeld.setText("");
						fehlermeldungHinzufuegenFeld.setText("");

						bestandListener.artikelGeaedert(artikel);
					} catch (ArtikelNichtGefundenException e) {
						fehlermeldungBestandFeld.setText("Artikel nicht gefunden.");
						fehlermeldungBestandFeld.setForeground(new Color(255, 0, 0));
					} catch (StueckzahlEntsprichtNichtPackungException e) {
						fehlermeldungBestandFeld.setText("Die Stueckzahl entspricht nicht der Packung.");
						fehlermeldungBestandFeld.setForeground(new Color(255, 0, 0));
					} catch (NumberFormatException nfe) {
						fehlermeldungBestandFeld.setText("Geben Sie bitte Zahlen ein.");
						fehlermeldungBestandFeld.setForeground(new Color(255, 0, 0));
					}
				}

			}
		});
		
		nummernTextfeld.addKeyListener(new buttonAktivieren());
		preisTextfeld.addKeyListener(new buttonAktivieren());
		anzahlTextfeld.addKeyListener(new buttonAktivieren());
		packTextfeld.addKeyListener(new buttonAktivieren());
		nummernBestandTextfeld.addKeyListener(new buttonAktivieren());
		anzahlBestandTextfeld.addKeyListener(new buttonAktivieren());

		nummernTextfeld.addKeyListener(new NumericKeyAdapter());
		preisTextfeld.addKeyListener(new NumericKeyAdapter());
		anzahlTextfeld.addKeyListener(new NumericKeyAdapter());
		packTextfeld.addKeyListener(new NumericKeyAdapter());
		nummernBestandTextfeld.addKeyListener(new NumericKeyAdapter());
		anzahlBestandTextfeld.addKeyListener(new NumericKeyAdapter());
	}
	class buttonAktivieren implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			String nummerBestand = nummernBestandTextfeld.getText();
			String anzahlBestand = anzahlBestandTextfeld.getText();
			if (!nummerBestand.isEmpty() && !anzahlBestand.isEmpty()) {
				aedernBestandButton.setEnabled(true);
			}else {
				aedernBestandButton.setEnabled(false);
			}
			String nummer = nummernTextfeld.getText();
			String name = namenTextfeld.getText();
			String preis = preisTextfeld.getText();
			String anzahl = anzahlTextfeld.getText();
			String packung = packTextfeld.getText();

			if (!nummer.isEmpty() && !name.isEmpty() && !preis.isEmpty() && !anzahl.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
				if(massengutBox.isSelected()){
					hinzufuegenButton.setEnabled(false);
					if(!packung.isEmpty()) {
						hinzufuegenButton.setEnabled(true);	
					}else {
						hinzufuegenButton.setEnabled(false);
					}
				}
			}else {
				hinzufuegenButton.setEnabled(false);	
			}
			panelaktualisieren.aktikelPanelaktualisieren();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			String nummerBestand = nummernBestandTextfeld.getText();
			String anzahlBestand = anzahlBestandTextfeld.getText();
			if (!nummerBestand.isEmpty() && !anzahlBestand.isEmpty()) {
				aedernBestandButton.setEnabled(true);
			}else {
				aedernBestandButton.setEnabled(false);
			}
			String nummer = nummernTextfeld.getText();
			String name = namenTextfeld.getText();
			String preis = preisTextfeld.getText();
			String anzahl = anzahlTextfeld.getText();
			String packung = packTextfeld.getText();

			if (!nummer.isEmpty() && !name.isEmpty() && !preis.isEmpty() && !anzahl.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
				if(massengutBox.isSelected()){
					hinzufuegenButton.setEnabled(false);
					if(!packung.isEmpty()) {
						hinzufuegenButton.setEnabled(true);	
					}else {
						hinzufuegenButton.setEnabled(false);
					}
				}
			}else {
				hinzufuegenButton.setEnabled(false);	
			}
			panelaktualisieren.aktikelPanelaktualisieren();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			String nummerBestand = nummernBestandTextfeld.getText();
			String anzahlBestand = anzahlBestandTextfeld.getText();
			if (!nummerBestand.isEmpty() && !anzahlBestand.isEmpty()) {
				aedernBestandButton.setEnabled(true);
			}else {
				aedernBestandButton.setEnabled(false);
			}
			String nummer = nummernTextfeld.getText();
			String name = namenTextfeld.getText();
			String preis = preisTextfeld.getText();
			String anzahl = anzahlTextfeld.getText();
			String packung = packTextfeld.getText();

			if (!nummer.isEmpty() && !name.isEmpty() && !preis.isEmpty() && !anzahl.isEmpty()) {
				hinzufuegenButton.setEnabled(true);
				if(massengutBox.isSelected()){
					hinzufuegenButton.setEnabled(false);
					if(!packung.isEmpty()) {
						hinzufuegenButton.setEnabled(true);	
					}else {
						hinzufuegenButton.setEnabled(false);
					}
				}
			}else {
				hinzufuegenButton.setEnabled(false);	
			}
			panelaktualisieren.aktikelPanelaktualisieren();
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
			if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_PERIOD) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
				getToolkit().beep();
				e.consume();
			}
		}
	}
}
