package ui.gui.swing.models;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import valueObjects.Benutzer;
import valueObjects.Kunde;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class BenutzerTabellenModel extends AbstractTableModel {

	private List<Benutzer> benutzer;
	private String[] spaltenNamen = { "Benutzer", "Benutzernummer", "Benutzername", "Vorname", "Nachname", "Strasse",
			"Postleitzahl", "Ort", "Land" };

	public BenutzerTabellenModel(List<Benutzer> aktuellerBenutzer) {
		super();
		benutzer = new Vector<Benutzer>();
		benutzer.addAll(aktuellerBenutzer);
	}

	public void setBenutzer(List<Benutzer> aktuellerBenutzer) {
		benutzer.clear();
		benutzer.addAll(aktuellerBenutzer);
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return benutzer.size();
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

		Benutzer gewaehlterBenutzer = benutzer.get(row);
		switch (col) {
		case 0:
			boolean istKunde = gewaehlterBenutzer.isKunde();
			String benutzerTyp = "Mitarbeiter";
			if (istKunde) {
				benutzerTyp = "Kunde";
			}
			return benutzerTyp;
		case 1:
			return gewaehlterBenutzer.getBenutzerIndex();
		case 2:
			return gewaehlterBenutzer.getBenutzerName();
		case 3:
			return gewaehlterBenutzer.getVorname();
		case 4:
			return gewaehlterBenutzer.getNachname();
		case 5:
			if (gewaehlterBenutzer instanceof Kunde) {
				return ((Kunde) gewaehlterBenutzer).getStrasse();
			}
		case 6:
			if (gewaehlterBenutzer instanceof Kunde) {
				return ((Kunde) gewaehlterBenutzer).getPostleitzahl();
			}
		case 7:
			if (gewaehlterBenutzer instanceof Kunde) {
				return ((Kunde) gewaehlterBenutzer).getOrt();
			}
		case 8:
			if (gewaehlterBenutzer instanceof Kunde) {
				return ((Kunde) gewaehlterBenutzer).getLand();
			}
		default:
			return null;
		}
	}
}
