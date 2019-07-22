package ui.gui.swing.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import valueObjects.Benutzer;
import valueObjects.Rechnung;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class RechnungsFensterPanel extends JPanel {

	private Rechnung rechnung;

	public RechnungsFensterPanel(Rechnung rechnung, Benutzer benutzer) {
		this.rechnung = rechnung;

		erstelleUI();
	}

	private void erstelleUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.gridx = 0;
		constraints.gridy = 0;

		constraints.weightx = 0.2; // 20% der gesamten Breite
		constraints.anchor = GridBagConstraints.EAST;

		constraints.gridx = 1; // Spalte 1
		constraints.weightx = 0.6; // 60% der gesamten Breite

		constraints.gridx = 2; // Spalte 2
		constraints.weightx = 0.2; // 20% der Gesammtbreite

		this.add(new JLabel());

		rechnungAusgeben();
	}

	public void rechnungAusgeben() {
		rechnung.ausgebenToString();
	}
}
