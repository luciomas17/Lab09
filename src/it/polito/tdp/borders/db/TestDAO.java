package it.polito.tdp.borders.db;

import java.util.Map;

import it.polito.tdp.borders.model.Country;

public class TestDAO {

	public static void main(String[] args) {

		BordersDAO dao = new BordersDAO();

		System.out.println("Lista di tutte le nazioni:");
		Map<Integer, Country> countries = dao.loadAllCountries();
		System.out.println(countries);
	}
}
