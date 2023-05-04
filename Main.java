import java.security.Key;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Graph bg = new Graph();
        bg.addVertex(1);
        bg.addVertex(2);
        bg.addVertex(3);
        bg.addVertex(4);
        bg.addVertex(5);
        bg.addVertex(6);
        bg.addVertex(7);
        bg.addVertex(8);
        bg.addVertex(9);
        bg.addVertex(10);
        bg.addEdge(1, 2);
        bg.addEdge(1, 10);
        bg.addEdge(2, 3);
        bg.addEdge(2, 7);
        bg.addEdge(3, 4);
        bg.addEdge(4, 6);
        bg.addEdge(5, 2);
        bg.addEdge(5, 7);
        bg.addEdge(6, 9);
        bg.addEdge(7, 6);
        bg.addEdge(7, 8);
        bg.addEdge(7, 9);
        bg.addEdge(8, 4);
        bg.addEdge(8, 6);
        bg.addEdge(8, 9);
        bg.addEdge(10, 6);
        bg.addEdge(10, 5);
        // bg.getGraphMatrix();
        bg.getAdjacencyMatrix();
        bg.kahnSort();
        bg.printSorted();
        // bg.printList();
    }
}

class Vertex {
    int key;
    ArrayList<Vertex> in, out;

    public Vertex(int key) {
        this.key = key;
        this.in = new ArrayList<>();
        this.out = new ArrayList<>();
    }
}

class Graph {
    ArrayList<Vertex> vList;
    public int adjacencyMatrix[][];
    public int graphMatrix[][];

    public Graph() {
        vList = new ArrayList<>();
    }

    public void addVertex(int key) {
        vList.add(new Vertex(key));
    }

    public void addEdge(int from, int to) {
        Optional<Vertex> v1 = vList.stream().filter(v -> v.key == from).findFirst();
        Optional<Vertex> v2 = vList.stream().filter(v -> v.key == to).findFirst();
        if (v1.isPresent() && v2.isPresent()) {
            v1.get().out.add(v2.get());
            v2.get().in.add(v1.get());
        } else {
            System.out.println("Can't create edge. One of vertices doesn't exist");
        }
    }

    public void printList() {
        for (Vertex v : vList) {
            System.out.print("Vertex: " + v.key + " In: ");
            if (v.in.isEmpty()) {
                System.out.print("Empty ");
            }
            for (Vertex vIn : v.in) {
                System.out.print(vIn.key + " ");
            }
            System.out.print("Out: ");
            if (v.out.isEmpty()) {
                System.out.print("Empty ");
            }
            for (Vertex vOut : v.out) {
                System.out.print(vOut.key + " ");
            }
            System.out.println();
        }
    }

    public void getAdjacencyMatrix() {
        adjacencyMatrix = new int[vList.size()][vList.size()];
        for (int i = 0; i < vList.size(); i++) {
            for (int j = 0; j < vList.size(); j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
        for (Vertex v : vList) {
            if (!v.in.isEmpty())
                for (Vertex vIn : v.in) {
                    adjacencyMatrix[vList.indexOf(v)][vList.indexOf(vIn)] = -1;
                }
            if (!v.out.isEmpty())
                for (Vertex vOut : v.out) {
                    adjacencyMatrix[vList.indexOf(v)][vList.indexOf(vOut)] = 1;
                }
        }
        // for (int i = 0; i < adjacencyMatrix.length; i++) {
        // for (int j = 0; j < adjacencyMatrix.length; j++) {
        // System.out.print(adjacencyMatrix[i][j] + " ");
        // }
        // System.out.println();
        // }
    }

    public void getGraphMatrix() {
        vList.sort(Comparator.comparing(v -> v.key));
        for (var vertex : vList) {
            vertex.in.sort(Comparator.comparing(v -> v.key));
            vertex.out.sort(Comparator.comparing(v -> v.key));
        }
        graphMatrix = new int[vList.size()][vList.size() + 3];
        for (int i = 0; i < vList.size(); i++) {
            if (!vList.get(i).in.isEmpty() && !vList.get(i).out.isEmpty()) {
                graphMatrix[i][vList.size()] = vList.get(i).out.stream().min(Comparator.comparing(v -> v.key))
                        .get().key;
                graphMatrix[i][vList.size() + 1] = vList.get(i).in.stream().min(Comparator.comparing(v -> v.key))
                        .get().key;
                int index = i;
                graphMatrix[i][vList.size() + 2] = vList.stream()
                        .filter(v -> !v.in.contains(vList.get(index)) && !v.out.contains(vList.get(index))).findFirst()
                        .orElse(null).key;
            }
            for (int j = 0; j < vList.size(); j++) {
                if (vList.get(i).out.contains(vList.get(j))) {
                    graphMatrix[i][j] = vList.get(i).out.get(vList.get(i).out.size() - 1).key;
                } else if (vList.get(i).in.contains(vList.get(j))) {
                    graphMatrix[i][j] = vList.get(i).in.get(vList.get(i).in.size() - 1).key + vList.size();
                } else {
                    int last = 0;
                    for (var v : vList) {
                        if (!v.in.contains(vList.get(i)) && !v.out.contains(vList.get(i)))
                            last = v.key;
                    }
                    int f = last;
                    graphMatrix[i][j] = -vList.stream().filter(v -> v.key == f).findFirst().orElse(null).key;
                }
            }
        }
        for (int i = 0; i < vList.size(); i++) {
            for (int j = 0; j < vList.size() + 3; j++) {
                System.out.print(graphMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void removeFromAdjMatrix(int key) {
        Vertex vertex = vList.stream().filter(v -> v.key == key).findFirst().orElse(null);
        int element = vList.indexOf(vertex);
        for (int i = 0; i < vList.size(); i++) {
            if (adjacencyMatrix[i][element] != 0) {
                adjacencyMatrix[i][element] = 0;
            }
            if (adjacencyMatrix[element][i] != 0) {
                adjacencyMatrix[element][i] = 0;
            }
        }
        for (var v : vList) {
            if (v.in.contains(vertex)) {
                v.in.remove(vertex);
            }
            if (v.out.contains(vertex)) {
                v.out.remove(vertex);
            }
        }
        vList.remove(vertex);
        for (int i = element; i < vList.size(); i++) {
            for (int j = 0; j <= vList.size(); j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
                adjacencyMatrix[j][i] = adjacencyMatrix[j][i + 1];
            }
        }
    }

    Stack<Integer> keyList = new Stack<>();

    public void printSorted() {
        while (!keyList.isEmpty()) {
            System.out.print(keyList.pop() + " ");
        }
    }

    boolean cycle = false;

    public void tajranSort() {
        // 0 = white, 1 = gray, 2 = black
        int i = 0;
        int color[] = new int[adjacencyMatrix.length];
        do {
            if (color[i] == 0) {
                next(color, i);
            } else {
                i++;
            }
        } while(!cycle && keyList.size() < vList.size());
    }

    private void next(int color[], int index) {
        if (color[index] != 2) {
            color[index] = 1;
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                if (adjacencyMatrix[index][i] == 1 && color[i] == 0) {
                    next(color, i);
                }
            }
            color[index] = 2;
            keyList.push(vList.get(index).key);
        } else {
            cycle = true;
        }
    }

    public void kahnSort(){
        int in_deg[] = new int[adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] == -1) {
                    in_deg[i]++;
                }
            }
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < in_deg.length; i++) {
            if (in_deg[i] == 0) {
                q.add(i);
            }
        }
        while (!q.isEmpty()) {
            int t = q.poll();
            keyList.push(vList.get(t).key);
            for (int n = 0; n < adjacencyMatrix.length; n++) {
                if (adjacencyMatrix[t][n] == 1){
                    in_deg[n]--;
                    if (in_deg[n] == 0) {
                        q.add(n);
                    }
                }
            }
        }
        if (keyList.size() != vList.size()) {
            System.out.println("The graph has a cycle.");
        }
    }

}