import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Graph bg = new Graph();
        File file = new File("file1.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            bg.addEdge(a, b);
        }
        sc.close();
        // bg.createAdjM(5);
        // bg.getGraphMatrix();
        // bg.tajranSortGM();
        // bg.getNotDirectedAdjacencyMatrix();
        // bg.getHamiltonianCycleDir();
        // System.out.println(bg.findpath());
        // bg.kahnSortGM();
        // bg.printSorted();
        // bg.printList();
        bg.generateNotDirectedAdjM(6, 40);
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
    public int ndAdjacencyMatrix[][];
    public int graphMatrix[][];

    public Graph() {
        vList = new ArrayList<>();
    }

    // Adding a vertex to a graph

    public void addVertex(int key) {
        if (!vList.isEmpty()) {
            vList.sort(Comparator.comparing((Vertex v) -> v.key));
        }
        vList.add(new Vertex(key));
    }

    // Adding an edge to a graph

    public void addEdge(int from, int to) {
        if (vList.stream().filter(v -> v.key == from).findFirst().orElse(null) == null) {
            addVertex(from);
        }
        if (vList.stream().filter(v -> v.key == to).findFirst().orElse(null) == null) {
            addVertex(to);
        }
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

    // Adjacency matrix generator for directed graph (rather good one)

    public void generateDirectedAdjM(int n, int concentration) {
        int maxEdges = (int) (n * (n - 1) * concentration / 100);
        adjacencyMatrix = new int[n][n];
        Random random = new Random();
        int edges = 0;
        while (edges < maxEdges) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);
            if (from != to && adjacencyMatrix[from][to] == 0) {
                adjacencyMatrix[from][to] = 1;
                edges++;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Adjacency matrix generator for undirected graph (dunno if good one)

    public void generateNotDirectedAdjM(int n, int concentration) {
        int maxEdges = (int) (n * (n - 1) * concentration / 200);
        ndAdjacencyMatrix = new int[n][n];
        Random random = new Random();
        int edges = 0;
        while (edges < maxEdges) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);
            if (from != to && ndAdjacencyMatrix[from][to] == 0 && ndAdjacencyMatrix[to][from] == 0) {
                ndAdjacencyMatrix[from][to] = 1;
                ndAdjacencyMatrix[to][from] = 1;
                edges++;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(ndAdjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Generating vList based on adjacency matrix

    public void generateInOutLists(int n, int concentration) {
        generateDirectedAdjM(n, concentration);
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0 ; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    addEdge(i, j);
                }
            }
        }
    }

    // Generating matrix of graph (work smart not hard)

    public void generateMatrixOfGraph(int n, int concentration) {
        generateInOutLists(n, concentration);
        getGraphMatrix();
    }

    // Filling adjacency matrix for directed graph

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

    // Filling adjacency matrix for undirected graph

    public void getNotDirectedAdjacencyMatrix() {
        ndAdjacencyMatrix = new int[vList.size()][vList.size()];
        for (int i = 0; i < vList.size(); i++) {
            for (int j = 0; j < vList.size(); j++) {
                ndAdjacencyMatrix[i][j] = 0;
            }
        }
        for (Vertex v : vList) {
            if (!v.in.isEmpty())
                for (Vertex vIn : v.in) {
                    ndAdjacencyMatrix[vList.indexOf(v)][vList.indexOf(vIn)] = 1;
                }
            if (!v.out.isEmpty())
                for (Vertex vOut : v.out) {
                    ndAdjacencyMatrix[vList.indexOf(v)][vList.indexOf(vOut)] = 1;
                }
        }
        /*
         * for (int i = 0; i < ndAdjacencyMatrix.length; i++) {
         * for (int j = 0; j < ndAdjacencyMatrix.length; j++) {
         * System.out.print(ndAdjacencyMatrix[i][j] + " ");
         * }
         * System.out.println();
         * }
         */
    }

    // Filling "matrix of graph"

    public void getGraphMatrix() {
        vList.sort(Comparator.comparing(v -> v.key));
        for (var vertex : vList) {
            vertex.in.sort(Comparator.comparing(v -> v.key));
            vertex.out.sort(Comparator.comparing(v -> v.key));
        }
        graphMatrix = new int[vList.size()][vList.size() + 3];
        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).out.isEmpty()) {
                graphMatrix[i][vList.size()] = 0;
            } else {
                graphMatrix[i][vList.size()] = vList.get(i).out.get(0).key;
            }
            if (vList.get(i).in.isEmpty()) {
                graphMatrix[i][vList.size() + 1] = 0;
            } else {
                graphMatrix[i][vList.size() + 1] = vList.get(i).in.get(0).key;
            }
            int index = i;
            graphMatrix[i][vList.size() + 2] = vList.stream()
                    .filter(v -> !v.in.contains(vList.get(index)) && !v.out.contains(vList.get(index))).findFirst()
                    .orElse(null).key;
            for (int j = 0; j < vList.size(); j++) {
                if (vList.get(i).out.contains(vList.get(j))) {
                    graphMatrix[i][j] = vList.get(i).out.get(vList.get(i).out.size() - 1).key;
                } else if (vList.get(i).in.contains(vList.get(j))) {
                    graphMatrix[i][j] = vList.get(i).in.get(vList.get(i).in.size() - 1).key + vList.size();
                } else if (!(vList.get(i).in.size() + 1 == vList.size()
                        || vList.get(i).out.size() + 1 == vList.size())) {
                    int last = 0;
                    for (var v : vList) {
                        if (!v.in.contains(vList.get(i)) && !v.out.contains(vList.get(i)))
                            last = v.key;
                    }
                    int f = last;
                    graphMatrix[i][j] = -vList.stream().filter(v -> v.key == f).findFirst().orElse(null).key;
                } else {
                    graphMatrix[i][j] = 0;
                }
            }
        }
        // for (int i = 0; i < vList.size(); i++) {
        // for (int j = 0; j < vList.size() + 3; j++) {
        // System.out.print(graphMatrix[i][j] + " ");
        // }
        // System.out.println();
        // }
    }

    // Removing an edge (or a vertex idk) from adjacency matrix

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

    // Printing sorted sequence

    Stack<Integer> keyList = new Stack<>();

    public void printSorted() {
        while (!keyList.isEmpty()) {
            System.out.print(keyList.pop() + " ");
        }
    }

    // Tarjan's topological sort using adjacency matrix

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
        } while (!cycle && keyList.size() < vList.size());
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

    // Kahn's topological sort using adjacency matrix

    public void kahnSort() {
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
                if (adjacencyMatrix[t][n] == 1) {
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

    // Tarjan's topological sort using "matrix of graph"

    public void tajranSortGM() {
        int i = 0;
        int color[] = new int[vList.size()];
        do {
            if (color[i] == 0) {
                dfsGM(i, color);
            } else {
                i++;
            }
        } while (!cycle && keyList.size() < vList.size());
    }

    private void dfsGM(int index, int[] color) {
        if (color[index] != 2) {
            color[index] = 1;
            for (int i = 0; i < vList.size(); i++) {
                if (graphMatrix[index][i] > 0 && graphMatrix[index][i] <= vList.size() && color[i] == 0) {
                    dfsGM(i, color);
                }
            }
            color[index] = 2;
            keyList.push(vList.get(index).key);
        } else {
            cycle = true;
            System.out.println("Graph has a cycle. Sort impossible");
        }
    }

    // Kahn's topological sort using "matrix of graph"

    public void kahnSortGM() {
        int in_deg[] = new int[vList.size()];
        for (int i = 0; i < vList.size(); i++) {
            for (int j = 0; j < vList.size(); j++) {
                if (graphMatrix[i][j] > vList.size()) {
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
            for (int n = 0; n < vList.size(); n++) {
                if (graphMatrix[t][n] > 0 && graphMatrix[t][n] <= vList.size()) {
                    if (--in_deg[n] == 0) {
                        q.add(n);
                    }
                }
            }
        }
        if (keyList.size() != vList.size()) {
            System.out.println("The graph has a cycle.");
        }
    }

    // dead code ;-;

    private void generate(int size) {
        cycle = false;
        adjacencyMatrix = new int[size][size];
        int color[] = new int[size];
        for (int i = 0; i < size; i++) {
            color[i] = 0;
            for (int j = 0; j < size; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    public void createAdjM(int size) {
        generate(size);
        Random r = new Random();
        for (int i = 1; i < size; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (r.nextInt(1) == 1)
                    adjacencyMatrix[i][j] = 1;
            }
        }
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Finding Hamiltonian cycle in undirected graph using adjacency matrix

    int[] path;

    private boolean hasHamiltonianCycle(int[] path, int position) {
        int n = ndAdjacencyMatrix.length;
        if (position == n) {
            if (ndAdjacencyMatrix[path[position - 1]][path[0]] == 1) {
                return true;
            } else {
                return false;
            }
        }
        for (int i = 0; i < n; i++) {
            if (canGo(i, path, position)) {
                path[position] = i;
                if (hasHamiltonianCycle(path, position + 1))
                    return true;
                path[position] = -1;
            }
        }
        return false;
    }

    private boolean canGo(int v, int[] path, int pos) {
        if (ndAdjacencyMatrix[path[pos - 1]][v] == 0 && ndAdjacencyMatrix[v][path[pos - 1]] == 0)
            return false;
        for (int i = 0; i < pos; i++)
            if (path[i] == v)
                return false;

        return true;
    }

    public void getHamiltonianCycle() {
        path = new int[ndAdjacencyMatrix.length];
        for (int i = 0; i < ndAdjacencyMatrix.length; i++) {
            path[i] = -1;
        }
        path[0] = 0;
        if (!hasHamiltonianCycle(path, 1)) {
            System.out.println("Graf nie zawiera cyklu Hamiltona");
            return;
        }
        for (int i = 0; i < path.length; i++) {
            System.out.print(path[i] + " ");
        }
    }

    // Finding Hamiltonian cycle in directed graph using list of successors

    private boolean hasHamiltonianCycleDir(int[] path, int position) {
        int n = vList.size();
        if (position == n) {
            if (vList.get(path[position - 1]).in.contains(vList.get(path[0]))
                    || vList.get(path[position - 1]).out.contains(vList.get(path[0]))) {
                return true;
            } else {
                return false;
            }
        }
        for (int i = 0; i < n; i++) {
            if (canGoDir(i, path, position)) {
                path[position] = i;
                if (hasHamiltonianCycle(path, position + 1))
                    return true;
                path[position] = -1;
            }
        }
        return false;
    }

    private boolean canGoDir(int v, int[] path, int pos) {
        if (!vList.get(path[pos - 1]).out.contains(vList.get(v)))
            return false;
        for (int i = 0; i < pos; i++)
            if (path[i] == v)
                return false;

        return true;
    }

    public void getHamiltonianCycleDir() {
        path = new int[ndAdjacencyMatrix.length];
        for (int i = 0; i < ndAdjacencyMatrix.length; i++) {
            path[i] = -1;
        }
        path[0] = 0;
        if (!hasHamiltonianCycleDir(path, 1)) {
            System.out.println("Graf nie zawiera cyklu Hamiltona");
            return;
        }
        for (int i = 0; i < path.length; i++) {
            System.out.print(path[i] + " ");
        }
    }

    // Finding Eulerian cycle in undirected graph using adjacency matrix

    private void dfsDir(Vertex v, Stack<Integer> stack) {
        while (!v.out.isEmpty()) {
            Vertex next = v.out.get(0);
            v.out.remove(next);
            dfsDir(next, stack);
        }
        stack.push(v.key);
    }

    public List<Integer> findEulerianCycle() {
        Stack<Integer> stack = new Stack<>();
        dfsDir(vList.get(0), stack);
        for (Vertex v : vList) {
            if (!v.out.isEmpty()) {
                System.out.println("Graf nie zawiera cyklu Eulera");
                return null;
            }
        }
        List<Integer> eulerianCycle = new ArrayList<>();
        while (!stack.isEmpty()) {
            eulerianCycle.add(stack.pop());
        }
        return eulerianCycle;
    }

    // Finding Eulerian cycle in directed graph using list of successors

    public List<Integer> findEulerianCycleDir() {
        Vector<Integer> adjacent = new Vector<>();
        for (int i = 0; i < ndAdjacencyMatrix.length; i++)
            adjacent.add(accumulate(ndAdjacencyMatrix[i], 0));
        int start = 0, numofodd = 0;
        for (int i = ndAdjacencyMatrix.length - 1; i >= 0; i--) {
            if (adjacent.elementAt(i) % 2 == 1) {
                numofodd++;
                start = i;
            }
        }
        if (numofodd > 2) {
            System.out.println("Graf nie zawiera cyklu Eulera");
            return null;
        }
        Stack<Integer> stack = new Stack<>();
        Vector<Integer> path = new Vector<>();
        int current = start;
        while (!stack.isEmpty() || accumulate(ndAdjacencyMatrix[current], 0) != 0) {
            if (accumulate(ndAdjacencyMatrix[current], 0) == 0) {
                path.add(current);
                current = stack.pop();
            } else {
                for (int i = 0; i < ndAdjacencyMatrix.length; i++) {
                    if (ndAdjacencyMatrix[current][i] == 1) {
                        stack.push(current);
                        ndAdjacencyMatrix[current][i] = 0;
                        ndAdjacencyMatrix[i][current] = 0;
                        current = i;
                        break;
                    }
                }
            }
        }
        path.add(current);
        Collections.reverse(path);
        return path;
    }

    private int accumulate(int[] arr, int sum) {
        for (int i : arr)
            sum += i;
        return sum;
    }

}