package ui.gui.swing.models;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class WarenkorbTabellenModel extends AbstractTableModel {
	private List<Artikel> warenkorb;
	private String[] spaltenNamen = { "Artikelnummer", "Artikelname", "Preis", "Packungsinhalt", "Bestand",
			"Verfuegbarkeit", "Im Warenkorb" };

	public WarenkorbTabellenModel(List<Artikel> aktuellerWarenkorb) {
		super();
		warenkorb = new Vector<Artikel>();
		warenkorb.addAll(aktuellerWarenkorb);
	}

	public void setWarenkorb(List<Artikel> aktuellerWarenkorb) {
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

	@Override
	public Object getValueAt(int row, int col) {

		Artikel gewaehlterArtikel = warenkorb.get(row);
		switch (col) {
		case 0:
			return gewaehlterArtikel.getArtikelNummer();
		case 1:
			return gewaehlterArtikel.getArtikelName();
		case 2:
			return gewaehlterArtikel.getPreis();
		case 3:
			if (gewaehlterArtikel.getPack() == 0) {
				return ("Keine Packung");
			}
			return gewaehlterArtikel.getPack();
		case 4:
			return gewaehlterArtikel.getAnzahl();

		case 5:
			if (gewaehlterArtikel.isVerfuegbar()) {
				return ("Auf Lager");
			} else {
				return ("nicht verfügbar");
			}
		case 6:
			return gewaehlterArtikel.getStueck();
		default:
			return null;
		}
	}
}
