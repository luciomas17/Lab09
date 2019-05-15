package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public Map<Integer, Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		Map<Integer, Country> result = new HashMap<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next())
				result.put(rs.getInt("ccode"), new Country(rs.getString("StateAbb"), rs.getInt("ccode"), rs.getString("StateNme")));
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Country> loadCountriesByYear(int year, Map<Integer, Country> countryIdMap) {
		
		String sql = "SELECT state1no, state2no " + 
				"FROM contiguity " + 
				"WHERE year <= ? " + 
				"GROUP BY state1no, state2no";
		List<Country> result = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country state1 = countryIdMap.get(rs.getInt("state1no"));
				if(!result.contains(state1))
					result.add(state1);
				Country state2 = countryIdMap.get(rs.getInt("state2no"));
				if(!result.contains(state2))
					result.add(state2);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int contType, Map<Integer, Country> countryIdMap) {
		String sql = "SELECT state1no, state2no, conttype " + 
				"FROM contiguity " + 
				"WHERE conttype = ? " + 
				"GROUP BY state1no, state2no";
		List<Border> result = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, contType);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country state1 = countryIdMap.get(rs.getInt("state1no"));
				Country state2 = countryIdMap.get(rs.getInt("state2no"));
				
				result.add(new Border(state1, state2, rs.getInt("conttype")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
