package it.polito.tdp.meteo;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private List<Citta> citta;
	private List<String> cittaS;
	
	MeteoDAO dao;
	
	private List<Citta> best;
	private double costo_best;
	
	public Model() {
		dao = new MeteoDAO();
		this.citta = new LinkedList<Citta>();
		this.cittaS = dao.getAllLocalita();
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

		Citta ctemp;
		List<Citta> parziale = new LinkedList<Citta>();
		this.best = new LinkedList<Citta>();
		
		for(String s : cittaS) {
			ctemp = new Citta(s);
			ctemp.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, ctemp.getNome()));
			citta.add(ctemp);
		}	
		
		this.cerca(parziale, 0);
		
		String s="";
		int contatore = 0;
		for(Citta c : best) {
			contatore ++;
			s += "Giorno "+contatore+" "+mese+": "+c.getNome()+"\n";
		}
		
		s += "Costo complessivo: "+costo_best;
		
		return s;
	}

	private void cerca(List<Citta> parziale, int L) {

		if(L==NUMERO_GIORNI_TOTALI) {
			
			double costo_parziale = punteggioSoluzione(parziale);
			
			if(vincoloTreCitta(parziale) && costo_parziale < costo_best) {
				best = new LinkedList<Citta>(parziale);
				costo_best = costo_parziale;
				return;
			}
			else {
				return;
			}
			
		}
		
		// Caso intermedio
		for(Citta prova : citta) {
			if(aggiuntaValida(prova, parziale)) {
				parziale.add(prova);
				cerca(parziale, L+1);
				parziale.remove(parziale.size()-1);
				
			}
		}
		
		
	}

	private double punteggioSoluzione(List<Citta> parziale) {
		
		double costo = 0.0;
		
		for(int giorno = 1; giorno <= NUMERO_GIORNI_TOTALI; giorno++) {
			
			Citta c = parziale.get(giorno - 1);
			double umidita = c.getRilevamenti().get(giorno - 1).getUmidita();
			costo += umidita;
		}
		
		
		for(int giorno = 2; giorno <= NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2)))
				costo += COST;
		}
		
		return costo;
	}

	/*private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}*/
	
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		// Verifica massimo 6 giorni per citta'
		
		int contatore = 0;
		
		for(Citta ctemp : parziale) {
			if(prova.equals(ctemp))
				contatore++;
		}
		
		if(contatore >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		// Verifica minimo 3 giorni consecutivi
		
		if(parziale.size() == 0) // primo giorno
			return true;
		if(parziale.size() == 1 || parziale.size() == 2) // secondo o terzo giorno non posso cambiare citta
			return parziale.get(parziale.size() - 1).equals(prova);
		
		if(parziale.get(parziale.size() - 1).equals(prova)) // nei giorni successivi, posso rimanere
			return true;
		
		// sto cambiando citta (dopo almeno 3 giorni consecutivi)
		if(parziale.get(parziale.size() - 1).equals(parziale.get(parziale.size() - 2))
				&& parziale.get(parziale.size() - 2).equals(parziale.get(parziale.size() - 3)))
			return true;
	
		// In tutti gli altri casi
		return false;
		
	}
	
	private boolean vincoloTreCitta(List<Citta> parziale) {
		
		int contatore = 0;
		
		for(String s : cittaS) {
			for(Citta c : parziale) {
				if(s.compareTo(c.getNome())==0)
					contatore++;
			}
		}
		
		if(contatore == cittaS.size())
			return true;
		else
			return false;
	}
	

}


