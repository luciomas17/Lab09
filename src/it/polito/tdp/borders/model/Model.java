package it.polito.tdp.borders.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private BordersDAO dao;
	private Map<Integer, Country> countryIdMap;
	private Graph<Country, DefaultEdge> graph;

	public Model() {
		this.dao = new BordersDAO();
		this.countryIdMap = dao.loadAllCountries();
	}

	public void createGraph(int year) {
		this.graph = new SimpleGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.graph, dao.loadCountriesByYear(year, countryIdMap));
		
		List<Border> borders = dao.getCountryPairs(1, countryIdMap);
		for(Border b : borders) {
			Country state1 = b.getState1();
			Country state2 = b.getState2();
			
			if(this.graph.vertexSet().contains(state1) && this.graph.vertexSet().contains(state2))
				this.graph.addEdge(state1, state2);
		}
		
		System.out.println(String.format("Grafo creato! %d vertici e %d archi.", this.graph.vertexSet().size(), this.graph.edgeSet().size()));
	}
	
}
