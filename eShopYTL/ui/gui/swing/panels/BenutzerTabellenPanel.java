package ui.gui.swing.panels;

import javax.swing.JTable;
import ui.gui.swing.models.BenutzerTabellenModel;
import valueObjects.Benutzer;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class BenutzerTabellenPanel extends JTable {
	public BenutzerTabellenPanel(java.util.List<Benutzer> benutzer) {
		super();

		// TableModel erzeugen ...
		BenutzerTabellenModel tabellenModel = new BenutzerTabellenModel(benutzer);
		super.setAutoCreateRowSorter(true);
		// ... bei JTable "anmelden" und ...
		setModel(tabellenModel);
		// ... Daten an Model übergeben
		aktualisiereBenutzerListe(benutzer);	
	}
	
	public void aktualisiereBenutzerListe(java.util.List<Benutzer> benutzer) {
		// TableModel von JTable holen und ...
		BenutzerTabellenModel tabellenModel = (BenutzerTabellenModel) getModel();
		// ... Inhalt aktualisieren
		tabellenModel.setBenutzer(benutzer);
	}
}
