package ui.gui.swing.panels;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

import domain.Verwaltung;
import valueObjects.Artikel;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class BestandsHistoriePanel extends JPanel {

	public interface Funktion {
		public abstract int f(int x);

		default public boolean isDefinedForValue(int x) {
			try {
				int y = f(x);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}

	private Canvas canvas = null;
	private Funktion funktion = null;

	public BestandsHistoriePanel() {
		setLayout(new FlowLayout());
		canvas = new GraphOberflaeche();
		canvas.setPreferredSize(new Dimension(600, 600));
		add(canvas);

		setVisible(true);
	}

	public void setFunktion(Funktion f) {
		this.funktion = f;
		this.canvas.repaint();
	}

	public void aktualisiereBestandsHistorieGraph(Artikel artikel, Verwaltung eShop) {
		int[] bestandsVektor = eShop.getHistorie().artikelBestandsHistorieVector(artikel);

		setFunktion(new Funktion() {
			@Override
			public int f(int x) {
				return bestandsVektor[x];
			}
		});
	}

	class GraphOberflaeche extends Canvas {

		private final static int X_AXIS_OFFSET = 40;
		private final static int Y_AXIS_OFFSET = 40;

		@Override
		public void paint(Graphics graphFenster) {
			super.paint(graphFenster);

			Dimension groesse = getSize();

			graphFenster.setColor(Color.white);
			graphFenster.fillRect(0, 0, groesse.width, groesse.height);

			plot(graphFenster);
		}

		private void plot(Graphics graph) {
			graph.setColor(Color.black);

			Dimension groesse = getSize();

			graph.setFont(new Font("Arial", Font.PLAIN, 15));
			FontMetrics fm = graph.getFontMetrics();
			String tag = "Tag";
			int stringWidth = fm.stringWidth(tag);
			graph.drawString(tag, groesse.width - (X_AXIS_OFFSET / 2) - stringWidth,
					groesse.height - Y_AXIS_OFFSET + 30);

			String bestand = "Bestand";
			stringWidth = fm.stringWidth(bestand);
			graph.drawString(bestand, X_AXIS_OFFSET - stringWidth / 2, 0 + Y_AXIS_OFFSET - 25);

			// X-Achse
			graph.drawLine(0 + (X_AXIS_OFFSET / 2), groesse.height - Y_AXIS_OFFSET, groesse.width - (X_AXIS_OFFSET / 2),
					groesse.height - Y_AXIS_OFFSET);
			int einheitX = (groesse.width - X_AXIS_OFFSET) / 30;
			int strichX = 0;
			int strichY1 = 5;
			int strichY2 = -5;
			graph.setFont(new Font("Arial", Font.PLAIN, 10));
			fm = graph.getFontMetrics();

			for (int i = 0; i < 30; i++) {
				strichX += einheitX;

				graph.drawLine(strichX + X_AXIS_OFFSET, groesse.height - Y_AXIS_OFFSET - strichY1,
						strichX + X_AXIS_OFFSET, groesse.height - Y_AXIS_OFFSET - strichY2);
				String tagNummer = String.valueOf(i + 1);

				stringWidth = fm.stringWidth(tagNummer);

				graph.drawString(tagNummer, strichX + X_AXIS_OFFSET - stringWidth / 2,
						groesse.height - Y_AXIS_OFFSET - strichY2 + 10);
			}

			int x = 0;
			int y = 0;
			int maxBestand = 0;

			if (funktion != null) {
				for (int i = 0; i < 30; i++) {
					x = i;
					y = funktion.f(x);

					if (y > maxBestand) {
						maxBestand = y;
					}
				}
			}

			if (maxBestand > 0) {
				// Y-Achse
				graph.drawLine(0 + X_AXIS_OFFSET, groesse.height - (Y_AXIS_OFFSET / 2), 0 + X_AXIS_OFFSET,
						0 + (Y_AXIS_OFFSET / 2));
				int einheitY = (groesse.height - Y_AXIS_OFFSET * 2) / maxBestand;
				int strichX1 = 5;
				int strichX2 = -5;
				int strichY = 0;
				for (int j = 0; j < maxBestand; j++) {
					strichY += einheitY;

					graph.drawLine(strichX1 + X_AXIS_OFFSET, groesse.height - Y_AXIS_OFFSET - strichY,
							strichX2 + X_AXIS_OFFSET, groesse.height - Y_AXIS_OFFSET - strichY);

					bestand = String.valueOf(j + 1);

					int bestandStringWidth = fm.stringWidth(bestand);

					graph.drawString(bestand, strichX2 + X_AXIS_OFFSET - bestandStringWidth,
							groesse.height - Y_AXIS_OFFSET - strichY + 10 / 2);

				}

				// BestandsGraph
				if (funktion != null) {
					Point punkt1 = null;
					Point punkt2 = null;

					for (int index = 0; index < 30; index++) {
						x = index;
						y = funktion.f(x);

						punkt2 = new Point(einheitX * (x + 1) + X_AXIS_OFFSET,
								groesse.height - Y_AXIS_OFFSET - einheitY * y);

						if (punkt1 != null) {
							graph.drawLine(punkt1.x, punkt1.y, punkt2.x, punkt2.y);
						}

						punkt1 = punkt2;
					}
				}
			}
		}
	}
}
