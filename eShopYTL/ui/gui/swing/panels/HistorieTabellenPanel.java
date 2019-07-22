package ui.gui.swing.panels;

import javax.swing.JTable;

import ui.gui.swing.models.HistorieTabellenModel;
import valueObjects.AnzeigeEreignis;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class HistorieTabellenPanel extends JTable {
	public HistorieTabellenPanel(java.util.List<AnzeigeEreignis> ereignisListe) {
		super();

		// TableModel erzeugen ...
		HistorieTabellenModel tabellenModel = new HistorieTabellenModel();
		// ... bei JTable "anmelden" und ...
		setModel(tabellenModel);
		// ... Daten an Model übergeben
		aktualisiereEreignisListe(ereignisListe);	
	}
	
	public void aktualisiereEreignisListe(java.util.List<AnzeigeEreignis> ereignisListe) {

		// TableModel von JTable holen und ...
		HistorieTabellenModel tabellenModel = (HistorieTabellenModel) getModel();
		// ... Inhalt aktualisieren
		tabellenModel.setEreignisListe(ereignisListe);
	}
}
