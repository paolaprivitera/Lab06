package it.polito.tdp.meteo;

import java.util.List;

import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	public Model() {

	}

	public String getUmiditaMedia(int mese) {
		MeteoDAO dao = new MeteoDAO();
		List<String> localita = dao.getAllLocalita();
		String umiditaMedia = "";
		
		for(String s : localita) {
			double umidita = dao.getAvgRilevamentiLocalitaMese(mese, s);
			umiditaMedia += s+": "+umidita+"\n";
		}
		
		return umiditaMedia;
	}

	public String trovaSequenza(int mese) {

		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
