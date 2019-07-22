package ui.gui.swing.panels;

import javax.swing.JTable;

import ui.gui.swing.models.WarenkorbTabellenModel;
import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class WarenkorbTabellenPanel extends JTable {
	
	public WarenkorbTabellenPanel(java.util.List<Artikel> warenkorb) {
		super();

		// TableModel erzeugen ...
		WarenkorbTabellenModel tabellenModel = new WarenkorbTabellenModel(warenkorb);
		// ... bei JTable "anmelden" und ...
		setModel(tabellenModel);
		// ... Daten an Model übergeben
		aktualisiereWarenkorb(warenkorb);	
	}
	
	public void aktualisiereWarenkorb(java.util.List<Artikel> warenkorb) {

		// TableModel von JTable holen und ...
		WarenkorbTabellenModel tabellenModel = (WarenkorbTabellenModel) getModel();
		// ... Inhalt aktualisieren
		tabellenModel.setWarenkorb(warenkorb);
	}
}
