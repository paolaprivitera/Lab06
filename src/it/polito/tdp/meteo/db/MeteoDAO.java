package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		Calendar cal = Calendar.getInstance();
		
		List<Rilevamento> all = this.getAllRilevamenti();
		List<Rilevamento> rilevamentiDelMese = new LinkedList<Rilevamento>();
		
		for(Rilevamento r : all) {
			cal.setTime(r.getData());
			
			if(r.getLocalita().compareTo(localita)==0 && (cal.get(Calendar.MONTH)+1)==mese) {
				rilevamentiDelMese.add(r);
			}
		}
		
		return rilevamentiDelMese;
	}

	
	public List<Rilevamento> getAllRilevamentiMese(int mese) {

		Calendar cal = Calendar.getInstance();
		
		List<Rilevamento> all = this.getAllRilevamenti();
		List<Rilevamento> rilevamentiDelMese = new LinkedList<Rilevamento>();
		
		for(Rilevamento r : all) {
			cal.setTime(r.getData());
			if((cal.get(Calendar.MONTH)+1)==mese && cal.get(Calendar.DAY_OF_MONTH)>=1 && cal.get(Calendar.DAY_OF_MONTH)<=15) {
				rilevamentiDelMese.add(r);
			}
		}
		
		return rilevamentiDelMese;
	}
	
	public List<String> getAllLocalita() {
		List<String> allLocalita = new LinkedList<String>();
		List<Rilevamento> allRilevamenti = this.getAllRilevamenti();
		
		for(Rilevamento r : allRilevamenti) {
			if(!allLocalita.contains(r.getLocalita())) {
				allLocalita.add(r.getLocalita());
			}
		}
		
		return allLocalita;
	}
	
	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		double umiditaMedia = 0.0;
		int umidita = 0;
		int contatore = 0;
		
		List<Rilevamento> allRilevamenti = this.getAllRilevamentiLocalitaMese(mese, localita);
		
		for(Rilevamento r : allRilevamenti) {
			
			umidita += r.getUmidita();
			contatore++;	
			
		}
		
		umiditaMedia = (double)umidita/(double)contatore;
		
		return (double) (Math.floor(umiditaMedia*100)/100);
	}
	
}
