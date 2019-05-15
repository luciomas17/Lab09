package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	public class ComparatorOfCountryByName implements Comparator<Country> {
		@Override
		public int compare(Country c1, Country c2) {
			return c1.getStateName().compareTo(c2.getStateName());
		}
	}
	
	private class EdgeTraversalListener implements TraversalListener<Country, DefaultEdge> {

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {			
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
			Country sourceVertex = graph.getEdgeSource(e.getEdge());
			Country targetVertex = graph.getEdgeTarget(e.getEdge());
			
			if(!visit.containsKey(targetVertex) && visit.containsKey(sourceVertex))
				visit.put(targetVertex, sourceVertex);
			else if(!visit.containsKey(sourceVertex) && visit.containsKey(targetVertex)) 
				visit.put(sourceVertex, targetVertex);
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Country> arg0) {			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Country> arg0) {			
		}
	}

	private BordersDAO dao;
	private Graph<Country, DefaultEdge> graph;
	private Map<Integer, Country> countryIdMap;
	private Map<Country, Country> visit;

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
	
	public List<Country> getVisit(Country vertex) {
		List<Country> result = new ArrayList<>();
		this.visit = new HashMap<>();
		
		GraphIterator<Country, DefaultEdge> it = new BreadthFirstIterator<>(this.graph, vertex);
		it.addTraversalListener(new Model.EdgeTraversalListener());
		
		visit.put(vertex, null);		
		while(it.hasNext()) {
			result.add(it.next());
		}
		
		return result;
	}

	public Set<Country> getVertexSet() {
		if(this.graph == null) {
			return null;
		}
		else
			return this.graph.vertexSet();
	}
	
	public Map<Integer, Country> getCountryIdMap() {
		return this.countryIdMap;
	}
}
