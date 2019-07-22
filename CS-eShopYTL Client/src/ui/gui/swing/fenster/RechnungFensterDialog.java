package ui.gui.swing.fenster;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Window;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;

import valueObjects.ArtikelImWarenkorb;
import valueObjects.Kunde;
import valueObjects.Massengutartikel;

/**
 * Graphische Darstellung der Rechnung
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class RechnungFensterDialog extends JDialog {

	Calendar datum;
	Kunde kunde;
	List<ArtikelImWarenkorb> gekaufteArtikelListe;
	float gesamtPreis;

	private Canvas canvas = null;

	public RechnungFensterDialog(Window owner, String string, ModalityType documentModal, Kunde benutzer,
			List<ArtikelImWarenkorb> warenkorb) {
		super(owner, string, documentModal);

		Dimension dimension = new Dimension(600, 600);
		this.setSize(dimension);

		setLayout(new FlowLayout());
		canvas = new GraphOberflaeche();
		canvas.setPreferredSize(dimension);
		add(canvas);

		datum = Calendar.getInstance();
		kunde = benutzer;
		gekaufteArtikelListe = new Vector<ArtikelImWarenkorb>();
		for (ArtikelImWarenkorb artikelImKorb : warenkorb) {
			ArtikelImWarenkorb gekaufterArtikel = null;
			if (artikelImKorb.artikel.isMassengut() == false) {
				gekaufterArtikel = new ArtikelImWarenkorb(artikelImKorb.artikel);
				gekaufterArtikel.setAnzahl(artikelImKorb.getAnzahlImWarenkorb());
			} else {
				gekaufterArtikel = new ArtikelImWarenkorb((Massengutartikel) artikelImKorb.artikel);
				gekaufterArtikel.setAnzahl(artikelImKorb.getAnzahlImWarenkorb());
			}
			gekaufteArtikelListe.add(gekaufterArtikel);
		}
		setGesamtPreis();
	}

	/**
	 * Gesamtpreis berechnen
	 */
	private void setGesamtPreis() {
		for (ArtikelImWarenkorb artikelImKorb : gekaufteArtikelListe) {
			gesamtPreis += artikelImKorb.artikel.getPreis() * artikelImKorb.getAnzahlImWarenkorb();
		}
	}

	/**
	 * Datum formatiert ausgeben
	 */
	private String datumAusgeben() {
		return ("Kaufdatum: " + datum.get(Calendar.DAY_OF_MONTH) + "." + (datum.get(Calendar.MONTH) + 1) + "."
				+ datum.get(Calendar.YEAR));
	}

	/**
	 * Uhrzeit formatiert ausgeben
	 */
	private String uhrzeitAusgeben() {
		return ("Uhrzeit: " + datum.get(Calendar.HOUR_OF_DAY) + ":" + datum.get(Calendar.MINUTE));
	}

	/**
	 * Graphische Oberflaeche fuer das Rechnungsfenster erstellen
	 */
	class GraphOberflaeche extends Canvas {

		private final static int X_AXIS_OFFSET = 40;
		private final static int Y_AXIS_OFFSET = 40;

		/**
		 * Rechnungsfenster zeichnen
		 */
		@Override
		public void paint(Graphics graphFenster) {
			super.paint(graphFenster);

			Dimension groesse = getSize();

			graphFenster.setColor(Color.white);
			graphFenster.fillRect(0, 0, groesse.width, groesse.height);

			plot(graphFenster);
		}

		/**
		 * Graph zeichnen
		 */
		private void plot(Graphics graph) {
			graph.setColor(Color.black);

			Dimension groesse = getSize();

			graph.setFont(new Font("Arial", Font.PLAIN, 15));
			FontMetrics fm = graph.getFontMetrics();

			int x = X_AXIS_OFFSET / 2;
			int y = 0 + Y_AXIS_OFFSET;
			int fontGroesse = 20;
			graph.setFont(new Font("Arial", Font.BOLD, fontGroesse));
			graph.drawString("RECHNUNG", x, y);

			y += fontGroesse + 5;

			fontGroesse = 15;
			graph.setFont(new Font("Arial", Font.PLAIN, fontGroesse));
			fm = graph.getFontMetrics();

			String kaufDatum = datumAusgeben();
			int stringWidth = fm.stringWidth(kaufDatum);
			x = groesse.width - X_AXIS_OFFSET / 2 - stringWidth;
			graph.drawString(kaufDatum, x, y);

			String uhrZeit = uhrzeitAusgeben();
			y += fontGroesse + 5;
			graph.drawString(uhrZeit, x, y);

			y += fontGroesse + 10;
			String kundeAusgabe = kunde.getVorname() + " " + kunde.getNachname();
			x = X_AXIS_OFFSET / 2;
			graph.drawString(kundeAusgabe, x, y);

			y += fontGroesse + 5;
			String adresseAusgabe = kunde.getStrasse();
			x = X_AXIS_OFFSET / 2;
			graph.drawString(adresseAusgabe, x, y);

			y += fontGroesse + 5;
			String postleizahlAusgabe = kunde.getPostleitzahl() + " " + kunde.getOrt();
			x = X_AXIS_OFFSET / 2;
			graph.drawString(postleizahlAusgabe, x, y);

			y += fontGroesse + 5;
			String landAusgabe = kunde.getLand();
			x = X_AXIS_OFFSET / 2;
			graph.drawString(landAusgabe, x, y);

			y += fontGroesse + 20;
			graph.setFont(new Font("Arial", Font.BOLD, fontGroesse));
			graph.drawString("Artikel", x, y);

			fontGroesse = 13;
			graph.setFont(new Font("Arial", Font.PLAIN, fontGroesse));
			fm = graph.getFontMetrics();

			y += 5;
			graph.drawLine(0 + (X_AXIS_OFFSET / 2), y, groesse.width - (X_AXIS_OFFSET / 2), y);

			int tabellenBreite = groesse.width - X_AXIS_OFFSET;

			int xNummer = X_AXIS_OFFSET / 2;
			int xName = xNummer + tabellenBreite / 10;
			int xAnzahl = xName + tabellenBreite / 2;
			int xPreis = xAnzahl + tabellenBreite / 10;
			int xTabellenEnde = groesse.width - (X_AXIS_OFFSET / 2);

			int yZeile1 = y;
			int yZeile2 = yZeile1 + fontGroesse + 5;

			graph.drawLine(xNummer, yZeile1, xNummer, yZeile2);
			graph.drawLine(xName, yZeile1, xName, yZeile2);
			graph.drawLine(xAnzahl, yZeile1, xAnzahl, yZeile2);
			graph.drawLine(xPreis, yZeile1, xPreis, yZeile2);
			graph.drawLine(xTabellenEnde, yZeile1, xTabellenEnde, yZeile2);

			y = yZeile2 - 2;

			graph.drawString("Nummer", xNummer + 3, y);
			graph.drawString("Name", xName + 3, y);
			graph.drawString("Anzahl", xAnzahl + 3, y);
			graph.drawString("Preis", xPreis + 3, y);

			y = yZeile2;

			graph.drawLine(0 + (X_AXIS_OFFSET / 2), y, groesse.width - (X_AXIS_OFFSET / 2), y);

			for (ArtikelImWarenkorb artikelImKorb : gekaufteArtikelListe) {
				yZeile1 = y;
				yZeile2 = yZeile1 + fontGroesse + 5;

				graph.drawLine(xNummer, yZeile1, xNummer, yZeile2);
				graph.drawLine(xName, yZeile1, xName, yZeile2);
				graph.drawLine(xAnzahl, yZeile1, xAnzahl, yZeile2);
				graph.drawLine(xPreis, yZeile1, xPreis, yZeile2);
				graph.drawLine(xTabellenEnde, yZeile1, xTabellenEnde, yZeile2);

				y = yZeile2 - 2;

				graph.drawString("" + artikelImKorb.artikel.getArtikelNummer(), xNummer + 3, y);
				graph.drawString(artikelImKorb.artikel.getArtikelName(), xName + 3, y);
				String anzahlImWarenkorb = "" + artikelImKorb.getAnzahlImWarenkorb();
				stringWidth = fm.stringWidth(anzahlImWarenkorb);
				graph.drawString(anzahlImWarenkorb, xPreis - 3 - stringWidth, y);

				String preis = "" + artikelImKorb.artikel.getPreis();
				stringWidth = fm.stringWidth(preis);
				graph.drawString(preis, xTabellenEnde - 3 - stringWidth, y);

				y = yZeile2;

				graph.drawLine(0 + (X_AXIS_OFFSET / 2), y, groesse.width - (X_AXIS_OFFSET / 2), y);
			}

			y += fontGroesse + 10;
			String gesamtpreisAusgabe = "Gesamtpreis: " + gesamtPreis;
			stringWidth = fm.stringWidth(gesamtpreisAusgabe);
			graph.drawString(gesamtpreisAusgabe, xTabellenEnde - 3 - stringWidth, y);
		}
	}
}