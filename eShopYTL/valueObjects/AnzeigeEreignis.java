package valueObjects;

public class AnzeigeEreignis extends Ereignis {
		
		private String benutzerName; 
		private String artikelName; 
		
		public AnzeigeEreignis (Ereignis ereignis) {
			
			setDatum(ereignis.getDatum());
			setArtikelNummer(ereignis.getArtikelNummer());
			setAnzahl(ereignis.getAnzahl()); 
			setEreignisart(ereignis.getEreignisArt()); 
			setPersonIndex(ereignis.getPersonIndex()); 
		}

		public void setBenutzerName(String benutzerNameInput) {
			benutzerName = benutzerNameInput; 
		}
		
		public String getBenutzerName() {
			return benutzerName; 
		}

		public void setArtikelName(String artikelNameInput) {
			artikelName = artikelNameInput; 
		}
		
		public String getArtikelName() {
			return artikelName; 
		}
	
}
