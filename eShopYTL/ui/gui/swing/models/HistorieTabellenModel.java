package ui.gui.swing.models;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import domain.ArtikelVerwaltung;
import domain.BenutzerVerwaltung;
import valueObjects.AnzeigeEreignis;
import valueObjects.Artikel;
import valueObjects.Benutzer;
import valueObjects.Ereignis;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class HistorieTabellenModel extends AbstractTableModel {
	private List<AnzeigeEreignis> ereignisListe;

	private String[] spaltenNamen = { "Datum", "Ereignisart", "Änderungsanzahl", "Benutzer", "Artikelnummer",
			"Artikelname"};

	public HistorieTabellenModel() {
		super();
		ereignisListe = new Vector<AnzeigeEreignis>();
	}

	public void setEreignisListe(List<AnzeigeEreignis> ereignisliste) {
		ereignisListe.clear();
		ereignisListe.addAll(ereignisliste);
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return ereignisListe.size();
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

		AnzeigeEreignis ereignis = ereignisListe.get(row);

		boolean istKunde = false;

		String ereignisArt = "";
		switch (ereignis.getEreignisArt()) {
		case 1:
			ereignisArt = "Neuer Artikel hinzugefuegt. ";
			istKunde = false;
			break;
		case 2:
			ereignisArt = "Artikelbestand erhoeht (Einlagerung) ";
			istKunde = false;
			break;
		case 3:
			ereignisArt = "Artikel verkauft (Auslagerung) ";
			istKunde = true;
			break;
		}
		
		String benutzerName = ereignis.getBenutzerName(); 
		String artikelName = ereignis.getArtikelName(); 

		switch (col) {
		case 0:
			int tagDesJahres = ereignis.getDatum();
			Calendar kalender = Calendar.getInstance();
			kalender.set(Calendar.DAY_OF_YEAR, tagDesJahres);
			String datum = kalender.get(Calendar.DAY_OF_MONTH) + "." + (kalender.get(Calendar.MONTH) + 1) + "."
					+ kalender.get(Calendar.YEAR);
			return datum;
		case 1:
			return ereignisArt;
		case 2:
			return ereignis.getAnzahl();
		case 3:
			String benutzerTyp = "Mitarbeiter: ";
			if (istKunde) {
				benutzerTyp = "Kunde: ";
			}
			return benutzerTyp + benutzerName;
		case 4:
			return ereignis.getArtikelNummer();
		case 5:
			return artikelName;
		default:
			return null;
		}
	}
}
