package ui.gui.swing.panels;

import javax.swing.JTable;

import ui.gui.swing.models.WarenkorbTabellenModel;
import valueObjects.ArtikelImWarenkorb;

/**
 * 
 * Feld in der die Warenkorbtabelle angezeigt wird und in der aktualisiert wird
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class WarenkorbTabellenPanel extends JTable {

	public WarenkorbTabellenPanel(java.util.List<ArtikelImWarenkorb> warenkorb) {
		super();

		// TabellenModel erzeugen ...
		WarenkorbTabellenModel tabellenModel = new WarenkorbTabellenModel(warenkorb);
		// ... bei JTable "anmelden" und ...
		setModel(tabellenModel);
		// ... Daten an Model Uebergeben
		aktualisiereWarenkorb(warenkorb);
	}

	public void aktualisiereWarenkorb(java.util.List<ArtikelImWarenkorb> warenkorb) {

		// TableModel von JTable holen und ...
		WarenkorbTabellenModel tabellenModel = (WarenkorbTabellenModel) getModel();
		// ... Inhalt aktualisieren
		tabellenModel.setWarenkorb(warenkorb);
	}
}
