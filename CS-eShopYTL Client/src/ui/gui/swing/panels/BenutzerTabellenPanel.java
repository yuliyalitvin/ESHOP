package ui.gui.swing.panels;

import javax.swing.JTable;
import ui.gui.swing.models.BenutzerTabellenModel;
import valueObjects.Benutzer;

/**
 * Methoden für die Benutzertabelle
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class BenutzerTabellenPanel extends JTable {
	public BenutzerTabellenPanel(java.util.List<Benutzer> benutzer) {
		super();

		// TabellenModel erzeugen ...
		BenutzerTabellenModel tabellenModel = new BenutzerTabellenModel(benutzer);
		// ... bei JTable "anmelden" und ...
		setModel(tabellenModel);
		// ... Daten an Model Uebergeben
		aktualisiereBenutzerListe(benutzer);
	}

	public void aktualisiereBenutzerListe(java.util.List<Benutzer> benutzer) {
		// TableModel von JTable holen und ...
		BenutzerTabellenModel tabellenModel = (BenutzerTabellenModel) getModel();
		// ... Inhalt aktualisieren
		tabellenModel.setBenutzer(benutzer);
	}
}
