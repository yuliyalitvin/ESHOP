package ui.gui.swing.panels;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ui.gui.swing.models.ArtikelTabellenModel;
import valueObjects.Artikel;

/**
 * 
 * Feld in der die Warenkorbtabelle angezeigt wird und in der aktualisiert wird
 * 
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

@SuppressWarnings("serial")
public class ArtikelTabellenPanel extends JTable {

	private ArtikelTabellenModel tabellenModel;

	public ArtikelTabellenPanel(java.util.List<Artikel> artikel) {
		super();

		// TabellenModel erzeugen ...
		tabellenModel = new ArtikelTabellenModel(artikel);
		super.setAutoCreateRowSorter(true);
		ListSelectionModel rowSelectionModel = this.getSelectionModel();
		rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// ... bei JTable "anmelden" und ...
		setModel(tabellenModel);
		// ... Daten an Model uebergeben
		aktualisiereArtikelListe(artikel);
	}

	public void aktualisiereArtikelListe(java.util.List<Artikel> artikel) {

		// TableModel von JTable holen und ...
		tabellenModel = (ArtikelTabellenModel) getModel();
//		// ... Inhalt aktualisieren
		tabellenModel.setArtikel(artikel);
	}
}
