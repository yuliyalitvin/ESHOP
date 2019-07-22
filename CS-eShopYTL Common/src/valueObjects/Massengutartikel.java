package valueObjects;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public class Massengutartikel extends Artikel {

	private int pack;
	private int stueck;

	/**
	 * Methode, um ein neuen Massengutartikel zuerstellen Bzw. konstruktor
	 * 
	 */
	public Massengutartikel(String artikelname, int anr, float pr, int anz, boolean verfuegbar, boolean massengut,
			int pack) {
		super(artikelname, anr, pr, anz, verfuegbar, massengut);
		this.pack = pack;
	}

	public Massengutartikel(Massengutartikel artikel) {
		super(artikel);

		pack = artikel.pack;
	}

	public Massengutartikel(Artikel artikel) {
		super(artikel);
	}

	/**
	 * Methode, gib die Packung wieder
	 *
	 * @return pack wie viele sich in einer Packung befindet
	 */
	public int getPack() {
		return pack;
	}

	/**
	 * Methode, gib wieder ob dieser Artikel ein Massengutartikel ist
	 *
	 * @return massengut boolean ob es ein Massengutartikel ist
	 */
	public int getStueck() {
		return stueck;
	}
}
