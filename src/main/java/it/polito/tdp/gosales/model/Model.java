package it.polito.tdp.gosales.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	
	private GOsalesDAO dao;
	private Map<Integer, Retailers> retailersMap;
	private Graph<Retailers, DefaultWeightedEdge> grafo;
	private List<Arco> archi;
	
	
	public Model() {
		this.dao = new GOsalesDAO();
	}

	public List<String> getNazioni() {
		// TODO Auto-generated method stub
		return this.dao.getNazioni();
	}

	public String creaGrafo(String nazione, Integer anno, int nMin) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.retailersMap = new HashMap<Integer, Retailers>();
		
		this.retailersMap.putAll(this.dao.getVertici(nazione));
		System.out.println(this.retailersMap.values().size());
		
		Graphs.addAllVertices(this.grafo, this.retailersMap.values());
		
		this.archi = new ArrayList<Arco>();
		
		this.archi = this.dao.getArchi(nazione, anno, nMin);
		
		for(Arco a : this.archi) {
			Graphs.addEdge(this.grafo, this.retailersMap.get(a.getV1()), this.retailersMap.get(a.getV2()), a.getPeso());
		}
		
		return "Grafo creato!\n # Vertici: "+ this.grafo.vertexSet().size()+"\n # Archi: "+ this.grafo.edgeSet().size();
	}

	public Map<Integer,Retailers> getVertici() {
		// TODO Auto-generated method stub
		return this.retailersMap;
	}

	public List<Arco> getArchi() {
		// TODO Auto-generated method stub
		return this.archi;
	}

	public String doAnalizzaComponente(Retailers r) {
		// TODO Auto-generated method stub
		ConnectivityInspector<Retailers, DefaultWeightedEdge> inspector =
				new ConnectivityInspector<Retailers, DefaultWeightedEdge>(this.grafo);
		
		Set<Retailers> connessi = inspector.connectedSetOf(r);
		int peso = 0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(connessi.contains(this.grafo.getEdgeSource(e)) && connessi.contains(this.grafo.getEdgeTarget(e))) {
				peso+=this.grafo.getEdgeWeight(e);
			}
		}
		
		return "\nLa componente connessa di "+r.getName()+" ha dimensione "+connessi.size()+"\n Il peso totale degli archi della componente connessa è "+peso;
	}
	
	
	
}
