package application;

import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
	ArrayList<double[]> nodes = new ArrayList<>();
	ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
	
	void addNode(double x, double y) {
		//Dodajemy koordynaty wierzchołka do tablicy wierzchołków
		double[] tab = {x, y};
		nodes.add(tab);
		
		//Dodajemy listę krawędzi do macierzy krawędzi dla nowego wierzchołka
		ArrayList<Integer> myEdges = new ArrayList<>();
		for(int i=0; i<nodes.size()-1; i++) myEdges.add(0);
		edges.add(myEdges);
		
		//Dodajemy wierzchołek do listy krawędzi pozostałych wierzchołków
		for (ArrayList<Integer> innerList : edges) {
            innerList.add(0);
        }

	}
	void removeNode(int index) {
		//Usuwamy wierzchołek z tablicy wierzchołków
		nodes.remove(index);
		
		//Usuwamy listę krawędzi z macierzy krawędzi dla usuwanego wierzchołka
		edges.remove(index);
		
		//Usuwamy wierzchołek z listy krawędzi pozostałych wierzchołków
		for (ArrayList<Integer> innerList : edges) {
            innerList.remove(index);
        }
	}
	void addEdge(int node1, int node2, int value) {
		//Ustawiamy odległość w macierzy krawędzi
		ArrayList<Integer> firstInnerList = edges.get(node1);
		firstInnerList.set(node2, value);
		
		ArrayList<Integer> secondInnerList = edges.get(node2);
		secondInnerList.set(node1, value);
	}
	void removeEdge(int node1, int node2) {
		//Ustawiamy odległość w macierzy krawędzi na zero
		ArrayList<Integer> firstInnerList = edges.get(node1);
		firstInnerList.set(node2, 0);
		
		ArrayList<Integer> secondInnerList = edges.get(node2);
		secondInnerList.set(node1, 0);
	}
	ArrayList<Integer> getNeighbors(int index) {
		ArrayList<Integer> neighbors = new ArrayList<>();
		ArrayList<Integer> innerList = edges.get(index);
		for (int i=0; i<innerList.size(); i++) {
			if(innerList.get(i)!=0) {
				neighbors.add(i);
			}
		}
		return neighbors;
	}
	
	int findIndex(double x, double y) {
		int foundIndex=-1;
		double[] searchArray = {x,y};
		for (int i = 0; i < nodes.size(); i++) {
			double[] currentArray = nodes.get(i);
            if (Arrays.equals(currentArray, searchArray)) {
                foundIndex = i;
                break;
            }
        }
		return foundIndex;
	}
	public void Dijkstra(int sourceNode) {
	    int numNodes = nodes.size();
	    boolean[] visited = new boolean[numNodes];
	    int[] distances = new int[numNodes];
	    int[] previous = new int[numNodes];

	    
	    for (int i = 0; i < numNodes; i++) {
	        distances[i] = Integer.MAX_VALUE;
	        previous[i] = -1;
	    }

	    distances[sourceNode] = 0;

	    for (int i = 0; i < numNodes - 1; i++) {
	        // Znajdź najbliższy nieodwiedzony wierzchołek
	        int minDistanceNode = -1;
	        int minDistance = Integer.MAX_VALUE;
	        for (int j = 0; j < numNodes; j++) {
	            if (!visited[j] && distances[j] < minDistance) {
	                minDistance = distances[j];
	                minDistanceNode = j;
	            }
	        }

	        // Zaznacz odwiedzone wierzchołki
	        visited[minDistanceNode] = true;

	        // Aktualizuj dystans sąsiednich wierzchołków
	        ArrayList<Integer> neighbors = getNeighbors(minDistanceNode);
	        for (int neighbor : neighbors) {
	            int distance = edges.get(minDistanceNode).get(neighbor);
	            if (!visited[neighbor] && distance != 0 && distances[minDistanceNode] != Integer.MAX_VALUE
	                    && distances[minDistanceNode] + distance < distances[neighbor]) {
	                distances[neighbor] = distances[minDistanceNode] + distance;
	                previous[neighbor] = minDistanceNode;
	            }
	        }
	    }

	    // Wypisz najkrótsze ścieżki z wybranego wierzchołka do wszystkich innych
	    for (int i = 0; i < numNodes; i++) {
	        if (i != sourceNode) {
	            System.out.print("Shortest path from node " + sourceNode + " to node " + i + ": ");
	            printShortestPath(sourceNode, i, previous);
	            System.out.println("Distance: " + distances[i]);
	        }
	    }
	}

	private void printShortestPath(int sourceNode, int targetNode, int[] previous) {
	    if (targetNode == sourceNode) {
	        System.out.print(sourceNode + " ");
	    } else if (previous[targetNode] == -1) {
	        System.out.println("No path from node " + sourceNode + " to node " + targetNode);
	    } else {
	        printShortestPath(sourceNode, previous[targetNode], previous);
	        System.out.print(targetNode + " ");
	    }
	}

	void showNodes() {
		for (double[] value : nodes) {
			System.out.println(value[0]+" "+value[1]);
		}
	}
	void showEdges() {
		for (ArrayList<Integer> innerList : edges) {
            for (int value : innerList) {
                System.out.print(value);
            }
            System.out.println("\n");
        }
	}
}
