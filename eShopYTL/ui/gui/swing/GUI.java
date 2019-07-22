package ui.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import domain.Verwaltung;
import ui.gui.swing.panels.ArtikelTabellenPanel;
import ui.gui.swing.panels.BenutzerTabellenPanel;
import ui.gui.swing.panels.BestandsHistoriePanel;
import ui.gui.swing.panels.FuegeArtikelHinzuPanel;
import ui.gui.swing.panels.FuegeArtikelInWarenkorbPanel;
import ui.gui.swing.panels.FuegeArtikelInWarenkorbPanel.FelderVollListener;
import ui.gui.swing.panels.FuegeBenutzerHinzuPanel;
import ui.gui.swing.panels.HistorieTabellenPanel;
import ui.gui.swing.panels.LogInPanel;
import ui.gui.swing.panels.RegistrierenPanel;
import ui.gui.swing.panels.SucheArtikelPanel;
import ui.gui.swing.panels.SucheBenutzerPanel;
import ui.gui.swing.panels.SucheHistorieEinesArtikelsPanel;
import ui.gui.swing.panels.WarenkorbAktionenPanel;
import ui.gui.swing.panels.WarenkorbTabellenPanel;
import ui.gui.swing.panels.FuegeArtikelHinzuPanel.ArtikelHinzufuegenListener;
import ui.gui.swing.panels.FuegeBenutzerHinzuPanel.BenutzerHinzufuegenListener;
import ui.gui.swing.panels.LogInPanel.LogInListener;
import ui.gui.swing.panels.RegistrierenPanel.RegistrierenListener;
import ui.gui.swing.panels.SucheArtikelPanel.SucheArtikelPanelListener;
import ui.gui.swing.panels.SucheBenutzerPanel.SucheBenutzerPanelListener;
import ui.gui.swing.panels.WarenkorbAktionenPanel.WarenkorbListener;
import valueObjects.Artikel;
import valueObjects.Benutzer;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class GUI extends JFrame
		implements SucheArtikelPanelListener, SucheBenutzerPanelListener, ArtikelHinzufuegenListener, WarenkorbListener,
		BenutzerHinzufuegenListener, LogInListener, RegistrierenListener, FelderVollListener {

	private Verwaltung eShop;

	private SucheArtikelPanel suchPanelArtikel;
	private FuegeArtikelHinzuPanel hinzufuegenPanelArtikel;
	private FuegeArtikelInWarenkorbPanel hinzufuegenPanelWarenkorb;
	private ArtikelTabellenPanel artikelPanel;
	private JScrollPane scrollPaneArtikel;

	private LogInPanel logInPanel;
	private RegistrierenPanel registrierenPanel;

	private SucheBenutzerPanel suchPanelBenutzer;
	private FuegeBenutzerHinzuPanel hinzufuegenPanelBenutzer;
	private BenutzerTabellenPanel benutzerPanel;
	private JScrollPane scrollPaneBenutzer;

	private WarenkorbAktionenPanel warenkorbAendernPanel;
	private WarenkorbTabellenPanel warenkorbPanel;
	private JScrollPane scrollPaneWarenkorb;

	private JScrollPane scrollPaneHistorie;
	private HistorieTabellenPanel historiePanel;
	private SucheHistorieEinesArtikelsPanel suchPanelHistorie;
	private BestandsHistoriePanel bestandsHistoriePanel;

	private Benutzer benutzer;

	public GUI(String titel) {
		super(titel);

		try {
			eShop = new Verwaltung("ESHOP");

			// Code f√ºr Umschaltung des Look-and-Feels:
			try {
//				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");  //Gl‰nzendes
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Plattes
//				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"); //Altes
				SwingUtilities.updateComponentTreeUI(this);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			initializeLayoutArtikel();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GUI gui = new GUI("ESHOP YTL");
			}
		});
	}

	private void setUpMenu() {
		JMenuBar menueLeiste = new JMenuBar();

		JMenu home = new Home();
		menueLeiste.add(home);

		JMenu warenkorb = new Warenkorb();
		menueLeiste.add(warenkorb);

		JMenu anmelden = new Anmeldung();
		menueLeiste.add(anmelden);

		JMenu beenden = new Beenden();
		menueLeiste.add(beenden);

		this.setJMenuBar(menueLeiste);
	}

	class Home extends JMenu implements ActionListener {

		public Home() {
			super("Seiten");

			JMenuItem neuesMenueItem = new JMenuItem("Artikel");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);

			neuesMenueItem = new JMenuItem("Benutzer");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
			neuesMenueItem.setEnabled(false);
			if (benutzer != null) {
				if (benutzer.isKunde()) {
					neuesMenueItem.setEnabled(false);
				} else {
					neuesMenueItem.setEnabled(true);
				}
			}

			neuesMenueItem = new JMenuItem("Historie");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
			neuesMenueItem.setEnabled(false);
			if (benutzer != null) {
				if (benutzer.isKunde()) {
					neuesMenueItem.setEnabled(false);
				} else {
					neuesMenueItem.setEnabled(true);
				}
			}
		}

		public void actionPerformed(ActionEvent actionEvent) {

			String befehl = actionEvent.getActionCommand();

			if (befehl.equals("Artikel")) {
				allesAusblenden();
				initializeLayoutArtikel();
			} else if (befehl.equals("Benutzer")) {
				allesAusblenden();
				initializeLayoutBenutzer();
			} else if (befehl.equals("Historie")) {
				allesAusblenden();
				initializeLayoutHistorie();
			}
		}
	}

	class Warenkorb extends JMenu implements ActionListener {
		public Warenkorb() {
			super("Warenkorb");

			JMenuItem neuesMenueItem = new JMenuItem("Warenkorb anzeigen");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
			neuesMenueItem.setEnabled(false);
			if (benutzer != null) {
				if (benutzer.isKunde()) {
					neuesMenueItem.setEnabled(true);
				} else {
					neuesMenueItem.setEnabled(false);
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String befehl = actionEvent.getActionCommand();

			if (befehl.equals("Warenkorb anzeigen")) {
				allesAusblenden();
				initializeLayoutWarenkorb();
			}
		}
	}

	class Anmeldung extends JMenu implements ActionListener {

		public Anmeldung() {
			super("Anmeldung");

			JMenuItem neuesMenueItem = new JMenuItem("LogIn");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
			if (benutzer != null) {
				neuesMenueItem.setEnabled(false);
			}

			this.addSeparator();

			neuesMenueItem = new JMenuItem("Registrieren");
			if (benutzer != null) {
				neuesMenueItem.setEnabled(false);
			}
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

			String befehl = actionEvent.getActionCommand();

			if (befehl.equals("LogIn")) {
				allesAusblenden();
				initializeLayoutLogIn();
			} else if (befehl.equals("Registrieren")) {
				allesAusblenden();
				initializeLayoutRegistrieren();
			}
		}
	}

	class Beenden extends JMenu implements ActionListener {
		public Beenden() {
			super("Beenden");

			JMenuItem neuesMenueItem = new JMenuItem("Abmelden");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
			neuesMenueItem.setEnabled(false);
			if (benutzer != null) {
				neuesMenueItem.setEnabled(true);
			}

			neuesMenueItem = new JMenuItem("Speichern");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
			neuesMenueItem.setEnabled(false);
			if (benutzer != null) {
				neuesMenueItem.setEnabled(true);
			}

			this.addSeparator();

			neuesMenueItem = new JMenuItem("Beenden");
			neuesMenueItem.addActionListener(this);
			this.add(neuesMenueItem);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String befehl = actionEvent.getActionCommand();

			if (befehl.equals("Abmelden")) {
				benutzer = null;
				allesAusblenden();
				initializeLayoutArtikel();
			} else if (befehl.equals("Speichern")) {
				try {
					datenSpeichern();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (befehl.equals("Beenden")) {
				try {
					datenSpeichern();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GUI.this.setVisible(false);
				dispose();
				System.exit(0);
			}
		}
	}

	private void initializeLayoutArtikel() {

		setUpMenu();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());

		// NORTH
		suchPanelArtikel = new SucheArtikelPanel(eShop, this);
		suchPanelArtikel.setBorder(BorderFactory.createTitledBorder("Suche"));

		// EAST
		// Artikel Hinzufuegen
		hinzufuegenPanelArtikel = new FuegeArtikelHinzuPanel(eShop, benutzer, this);
		hinzufuegenPanelArtikel.setBorder(BorderFactory.createTitledBorder("Artikel Hinzufuegen"));

		// Warenkorb hinzufuegen
		hinzufuegenPanelWarenkorb = new FuegeArtikelInWarenkorbPanel(eShop, benutzer, this);
		hinzufuegenPanelWarenkorb.setBorder(BorderFactory.createTitledBorder("Artikel in Warenkorb"));

		// CENTER
		java.util.List<Artikel> artikelListe = eShop.gibAlleArtikel();
		artikelPanel = new ArtikelTabellenPanel(artikelListe);
		scrollPaneArtikel = new JScrollPane(artikelPanel);
		scrollPaneArtikel.setBorder(BorderFactory.createTitledBorder("Artikel"));

		this.add(suchPanelArtikel, BorderLayout.NORTH);
		if (benutzer != null) {
			if (benutzer.isKunde()) {
				this.add(hinzufuegenPanelWarenkorb, BorderLayout.EAST);
			} else {
				this.add(hinzufuegenPanelArtikel, BorderLayout.EAST);
			}
		}
		this.add(scrollPaneArtikel, BorderLayout.CENTER); // SCROLLPANE

		this.setSize(1100, 700);
		this.setVisible(true);
	}

	private void initializeLayoutBenutzer() {
		this.setLayout(new BorderLayout());

		// NORTH
		suchPanelBenutzer = new SucheBenutzerPanel(eShop, this);
		suchPanelBenutzer.setBorder(BorderFactory.createTitledBorder("Suche"));

		// EAST
		hinzufuegenPanelBenutzer = new FuegeBenutzerHinzuPanel(eShop, this);
		hinzufuegenPanelBenutzer.setBorder(BorderFactory.createTitledBorder("Benutzer Hinzufuegen"));

		// CENTER
		List<valueObjects.Benutzer> benutzerListe = eShop.gibAlleBenutzer();
		benutzerPanel = new BenutzerTabellenPanel(benutzerListe);
		scrollPaneBenutzer = new JScrollPane(benutzerPanel);
		scrollPaneBenutzer.setBorder(BorderFactory.createTitledBorder("Benutzer"));

		this.add(suchPanelBenutzer, BorderLayout.NORTH);
		this.add(hinzufuegenPanelBenutzer, BorderLayout.EAST);
		this.add(scrollPaneBenutzer, BorderLayout.CENTER); // SCROLLPANE

		this.setSize(1100, 700);
		this.setVisible(true);
	}

	private void initializeLayoutWarenkorb() {
		this.setLayout(new BorderLayout());

		// EAST
		warenkorbAendernPanel = new WarenkorbAktionenPanel(eShop, benutzer, this);
		this.add(warenkorbAendernPanel, BorderLayout.EAST);

		// CENTER
		List<valueObjects.Artikel> warenkorb = eShop.gibWarenkorb(benutzer.getBenutzerIndex());
		warenkorbPanel = new WarenkorbTabellenPanel(warenkorb);
		scrollPaneWarenkorb = new JScrollPane(warenkorbPanel);
		scrollPaneWarenkorb.setBorder(BorderFactory.createTitledBorder("Warenkorb"));
		this.add(scrollPaneWarenkorb, BorderLayout.CENTER); // SCROLLPANE

		this.setSize(1100, 700);
		this.setVisible(true);
	}

	private void initializeLayoutHistorie() {
		this.setLayout(new BorderLayout());

		// Historietabelle
		List<valueObjects.AnzeigeEreignis> ereignisListe = eShop.getAnzeigeEreignisListe();
//		domain.BenutzerVerwaltung benutzerListe = eShop.getBenutzerListe();
//		domain.ArtikelVerwaltung artikelListe = eShop.getArtikelListe();
//		historiePanel = new HistorieTabellenPanel(ereignisListe, benutzerListe, artikelListe);
		historiePanel = new HistorieTabellenPanel(ereignisListe);
		scrollPaneHistorie = new JScrollPane(historiePanel);
		scrollPaneHistorie.setBorder(BorderFactory.createTitledBorder("Historie"));

		// BestandsHistorieGraph
		bestandsHistoriePanel = new BestandsHistoriePanel();

		// SuchFeld fuer einen Artikel
		suchPanelHistorie = new SucheHistorieEinesArtikelsPanel(eShop, historiePanel, bestandsHistoriePanel);
		suchPanelHistorie.setBorder(BorderFactory.createTitledBorder("Geben Sie entweder eine Artikelnummer ein "
				+ "oder lassen Sie das Feld leer, um die komplette Historie mit \"Suchen!\" anzuzeigen."));

		this.add(suchPanelHistorie, BorderLayout.NORTH);
		this.add(scrollPaneHistorie, BorderLayout.CENTER);
		this.add(bestandsHistoriePanel, BorderLayout.EAST);

		this.setSize(1100, 700);
		pack();
		this.setVisible(true);
	}

	private void initializeLayoutLogIn() {
		this.setLayout(new BorderLayout());
		// CENTER
		logInPanel = new LogInPanel(eShop, this);
		this.add(logInPanel, BorderLayout.CENTER);

		this.setVisible(true);
	}

	private void initializeLayoutRegistrieren() {
		this.setLayout(new BorderLayout());
		// CENTER
		registrierenPanel = new RegistrierenPanel(eShop, this);
		this.add(registrierenPanel, BorderLayout.CENTER);

		this.setVisible(true);
	}

	public void artikelAusblenden() {
		if (suchPanelArtikel != null) {
			suchPanelArtikel.setVisible(false);
		}
		if (hinzufuegenPanelArtikel != null) {
			hinzufuegenPanelArtikel.setVisible(false);
		}
		if (hinzufuegenPanelWarenkorb != null) {
			hinzufuegenPanelWarenkorb.setVisible(false);
		}
		if (scrollPaneArtikel != null) {
			scrollPaneArtikel.setVisible(false);
		}
	}

	public void benutzerAusblenden() {
		if (suchPanelBenutzer != null) {
			suchPanelBenutzer.setVisible(false);
		}
		if (hinzufuegenPanelBenutzer != null) {
			hinzufuegenPanelBenutzer.setVisible(false);
		}
		if (scrollPaneBenutzer != null) {
			scrollPaneBenutzer.setVisible(false);
		}
	}

	public void historieAusblenden() {
		if (suchPanelHistorie != null) {
			suchPanelHistorie.setVisible(false);
		}
		if (scrollPaneHistorie != null) {
			scrollPaneHistorie.setVisible(false);
		}
		if (scrollPaneBenutzer != null) {
			scrollPaneBenutzer.setVisible(false);
		}
		if (bestandsHistoriePanel != null) {
			bestandsHistoriePanel.setVisible(false);
		}
	}

	public void warenkorbAusblenden() {
		if (warenkorbAendernPanel != null) {
			warenkorbAendernPanel.setVisible(false);
		}
		if (scrollPaneWarenkorb != null) {
			scrollPaneWarenkorb.setVisible(false);
		}
	}

	public void loginAusblenden() {
		if (logInPanel != null) {
			logInPanel.setVisible(false);
		}
	}

	public void registrierenAusblenden() {
		if (registrierenPanel != null) {
			registrierenPanel.setVisible(false);
		}
	}

	public void allesAusblenden() {
		artikelAusblenden();
		benutzerAusblenden();
		historieAusblenden();
		warenkorbAusblenden();
		loginAusblenden();
		registrierenAusblenden();
	}

	// Artikel Suche
	@Override
	public void beiSuchErgebnisArtikel(java.util.List<Artikel> artikelListe) {
		artikelPanel.aktualisiereArtikelListe(artikelListe);
	}

	// Artikel Hinzugefuegt
	@Override
	public void artikelHinzugefuegt(Artikel artikel) {
		java.util.List<Artikel> a = eShop.gibAlleArtikel();
		artikelPanel.aktualisiereArtikelListe(a);
	}
	
	//artikelpanel aktualisieren
	@Override
	public void aktikelPanelaktualisieren() {
		java.util.List<Artikel> a = eShop.gibAlleArtikel();
		artikelPanel.aktualisiereArtikelListe(a);
	}

	// Stueckzahl ge‰ndert
	@Override
	public void stueckzahlGeaendert() {
		java.util.List<Artikel> a = eShop.gibWarenkorb(benutzer.getBenutzerIndex());
		warenkorbPanel.aktualisiereWarenkorb(a);
	}

	// Artikelanzahl erhoeht
	@Override
	public void artikelGeaedert(Artikel artikel) {
		java.util.List<Artikel> a = eShop.gibAlleArtikel();
		artikelPanel.aktualisiereArtikelListe(a);
	}

	// Artikel aus Warenkorb geloescht
	@Override
	public void loescheArtikelAusWarenkorb() {
		java.util.List<Artikel> a = eShop.gibWarenkorb(benutzer.getBenutzerIndex());
		warenkorbPanel.aktualisiereWarenkorb(a);
	}
	// Warenkorbpanel aktualiesieren
		@Override
		public void warenkorbPanelaktualisieren() {
			java.util.List<Artikel> a = eShop.gibWarenkorb(benutzer.getBenutzerIndex());
			warenkorbPanel.aktualisiereWarenkorb(a);
		}

	// Warenkorb geleert
	@Override
	public void leereWarenkorb() {
		java.util.List<Artikel> a = eShop.gibWarenkorb(benutzer.getBenutzerIndex());
		warenkorbPanel.aktualisiereWarenkorb(a);
	}

	// Warenkorb gekauft
	@Override
	public void kaufen() {
		allesAusblenden();
		initializeLayoutArtikel();
	}

	// Nach LogIn
	@Override
	public void wennEingeloggt(valueObjects.Benutzer benutzer) {
		this.benutzer = benutzer;
		allesAusblenden();
		initializeLayoutArtikel();
	}

	// Nach registrieren
	@Override
	public void wennRegistriert() {
		allesAusblenden();
		initializeLayoutArtikel();
	}

	// Benutzer Suche
	@Override
	public void beiSuchErgebnisBenutzer(List<valueObjects.Benutzer> benutzerListe) {
		benutzerPanel.aktualisiereBenutzerListe(benutzerListe);
	}

	// Benutzer Hinzufuegen
	@Override
	public void benutzerHinzugefuegt(valueObjects.Benutzer benutzer) {
		List<valueObjects.Benutzer> b = eShop.gibAlleBenutzer();
		benutzerPanel.aktualisiereBenutzerListe(b);
	}
	// Benutzerpanel aktualisieren
		@Override
		public void benutzerPanelaktualisieren() {
			List<valueObjects.Benutzer> b = eShop.gibAlleBenutzer();
			benutzerPanel.aktualisiereBenutzerListe(b);
		}

	private void datenSpeichern() throws IOException {
		eShop.schreibeArtikel();
		eShop.schreibeMitarbeiter();
		eShop.schreibeKunde();
		eShop.schreibeHistorie();
		System.out.println("Alles gespeichert.");
	}
	
}
