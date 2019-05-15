package it.polito.tdp.borders.model;

import java.util.Set;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();
		
		System.out.println("Creo il grafo relativo al 2000");
		model.createGraph(2000);
		System.out.println("");
		
		System.out.println("Stampo stati e numero di confinanti");
		Set<Country> vertexSet = model.getVertexSet();		
		for(Country c : vertexSet)
			System.out.println(String.format("Stato: %s, Confinanti: %d", c.getStateName(), model.findDegree(c)));
		System.out.println("");
		
		System.out.println("Stampo numero di componenti connesse al grafo");
		System.out.println(model.getNumberOfConnectedComponents());
		
//		List<Country> countries = model.getCountries();
//		System.out.format("Trovate %d nazioni\n", countries.size());

//		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
//		Map<Country, Integer> stats = model.getCountryCounts();
//		for (Country country : stats.keySet())
//			System.out.format("%s %d\n", country, stats.get(country));		
		
	}

}
