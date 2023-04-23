import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph();
        int[] arr = { 1, 2, 3, 4, 5 };
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 5);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.printAdjacencyMatrix();
    }
}

class Vertex {
    int key;

    public Vertex(int key) {
        this.key = key;
    }
}

class Graph {
    private Map<Vertex, List<Vertex>> map;

    public Graph() {
        map = new HashMap<>();
    }

    public void addVertex(int val) {
        map.putIfAbsent(new Vertex(val), new ArrayList<>());
    }

    public void addEdge(int first, int second) {
        Vertex v1 = map.keySet().stream().filter(v -> v.key == first).findFirst().orElse(null);
        Vertex v2 = map.keySet().stream().filter(v -> v.key == second).findFirst().orElse(null);
        if (v1 != null && v2 != null) {
            map.get(v1).add(v2);
            map.get(v2).add(v1);
        }
    }

    public void printAdjacentVertices() {
        for (Map.Entry<Vertex, List<Vertex>> entry : map.entrySet()) {
            Vertex vertex = entry.getKey();
            List<Vertex> adjacentVertices = entry.getValue();
            System.out.print("Vertex " + vertex.key + " is adjacent to: ");
            for (Vertex adjacentVertex : adjacentVertices) {
                System.out.print(adjacentVertex.key + " ");
            }
            System.out.println();
        }
    }

    public void printAdjacencyMatrix() {
        int n = map.keySet().size();
        int m[][] = new int[n][n];
        for (Map.Entry<Vertex, List<Vertex>> entry : map.entrySet()) {
            Vertex vertex = entry.getKey();
            int i = vertex.key - 1;
            List<Vertex> adjacentVertices = entry.getValue();
            for (Vertex adjacentVertex : adjacentVertices) {
                int j = adjacentVertex.key - 1;
                m[i][j] = m[j][i] = 1;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }
    

}