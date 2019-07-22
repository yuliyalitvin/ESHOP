package ui.gui.swing.panels;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ui.gui.swing.models.ArtikelTabellenModel;
import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class ArtikelTabellenPanel extends JTable {
	
	private ArtikelTabellenModel tabellenModel;
	
	public ArtikelTabellenPanel(java.util.List<Artikel> artikel) {
		super();

		// TableModel erzeugen ...
		tabellenModel = new ArtikelTabellenModel(artikel);
		super.setAutoCreateRowSorter(true);
		ListSelectionModel rowSelectionModel = this.getSelectionModel(); 
		rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
//		
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
