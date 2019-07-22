package persistence;

import java.io.IOException;

import valueObjects.Artikel;
import valueObjects.Ereignis;
import valueObjects.Kunde;
import valueObjects.Mitarbeiter;
import valueObjects.Warenkorb;

/**
 * @author Lars Obist, Yuliya Litvin, Thao Phoung Nguyen
 */

public interface PersistenceManager {

	public void openForReading(String datenquelle) throws IOException;

	public void openForWriting(String datenquelle) throws IOException;

	public boolean close();

	public Artikel ladeArtikel() throws IOException;

	public boolean speichereArtikel(Artikel a) throws IOException;

	public Kunde ladeKunde() throws IOException;

	public boolean speichereKunde(Kunde k) throws IOException;

	public Mitarbeiter ladeMitarbeiter() throws IOException;

	public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException;

	public Warenkorb ladeWarenkorb() throws IOException;

	public void speichereWarenkorb(Warenkorb w) throws IOException;

	public Ereignis ladeEreignis() throws IOException;

	public void speichereEreignis(Ereignis e) throws IOException;
}