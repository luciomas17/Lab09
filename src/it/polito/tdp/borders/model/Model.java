package it.polito.tdp.borders.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	public class ComparatorOfCountryByName implements Comparator<Country> {
		@Override
		public int compare(Country c1, Country c2) {
			return c1.getStateName().compareTo(c2.getStateName());
		}
	}

	private BordersDAO dao;
	private Map<Integer, Country> countryIdMap;
	private Graph<Country, DefaultEdge> graph;

	public Model() {
		this.dao = new BordersDAO();
		this.countryIdMap = dao.loadAllCountries();
	}

	public void createGraph(int year) {
		this.graph = new SimpleGraph<>(DefaultEdge.class);
		
		List<Country> countries = dao.loadCountriesByYear(year, countryIdMap);
		Collections.sort(countries, new Model.ComparatorOfCountryByName());
		Graphs.addAllVertices(this.graph, countries);
		
		List<Border> borders = dao.getCountryPairs(1, countryIdMap);
		for(Border b : borders) {
			Country state1 = b.getState1();
			Country state2 = b.getState2();
			
			if(this.graph.vertexSet().contains(state1) && this.graph.vertexSet().contains(state2))
				this.graph.addEdge(state1, state2);
		}
		
		System.out.println(String.format("Grafo creato! %d vertici e %d archi.", this.graph.vertexSet().size(), this.graph.edgeSet().size()));
	}
	
	public int findDegree(Country vertex) {
		return this.graph.degreeOf(vertex);
	}
	
	public int getNumberOfConnectedComponents() {
		ConnectivityInspector<Country, DefaultEdge> inspector = new ConnectivityInspector<Country, DefaultEdge>(this.graph);
		return inspector.connectedSets().size();
	}

	public Set<Country> getVertexSet() {
		return this.graph.vertexSet();
	}
	
}
