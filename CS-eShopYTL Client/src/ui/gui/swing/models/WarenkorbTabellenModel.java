package ui.gui.swing.models;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import valueObjects.ArtikelImWarenkorb;

/**
 * Klasse zum erstellen und fuellen der Warenkorbtabelle
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class WarenkorbTabellenModel extends AbstractTableModel {
	private List<ArtikelImWarenkorb> warenkorb;
	private String[] spaltenNamen = { "Artikelnummer", "Artikelname", "Preis", "Packungsinhalt", "Bestand",
			"Verfuegbarkeit", "Im Warenkorb" };

	public WarenkorbTabellenModel(List<ArtikelImWarenkorb> aktuellerWarenkorb) {
		super();
		warenkorb = new Vector<ArtikelImWarenkorb>();
		warenkorb.addAll(aktuellerWarenkorb);
	}

	public void setWarenkorb(List<ArtikelImWarenkorb> aktuellerWarenkorb) {
		warenkorb.clear();
		warenkorb.addAll(aktuellerWarenkorb);
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return warenkorb.size();
	}

	@Override
	public int getColumnCount() {
		return spaltenNamen.length;
	}

	@Override
	public String getColumnName(int col) {
		return spaltenNamen[col];
	}

	/**
	 * Methode, zum einfuegen der Artikel im Warenkorb in die Tabelle
	 */
	@Override
	public Object getValueAt(int row, int col) {

		ArtikelImWarenkorb gewaehlterArtikel = warenkorb.get(row);
		switch (col) {
		case 0:
			return gewaehlterArtikel.artikel.getArtikelNummer();
		case 1:
			return gewaehlterArtikel.artikel.getArtikelName();
		case 2:
			return gewaehlterArtikel.artikel.getPreis();
		case 3:
			if (gewaehlterArtikel.artikel.getPack() == 0) {
				return ("Keine Packung");
			}
			return gewaehlterArtikel.artikel.getPack();
		case 4:
			int stueck = gewaehlterArtikel.artikel.getStueck();
			if (gewaehlterArtikel.artikel.isMassengut()) {
				stueck *= gewaehlterArtikel.artikel.getPack();
			}
			return gewaehlterArtikel.artikel.getAnzahl() - stueck;

		case 5:
			if (gewaehlterArtikel.artikel.isVerfuegbar()) {
				return ("Auf Lager");
			} else {
				return ("nicht verfuegbar");
			}
		case 6:
			return gewaehlterArtikel.getAnzahlImWarenkorb();
		default:
			return null;
		}
	}
}
