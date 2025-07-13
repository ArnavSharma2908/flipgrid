import java.util.*;

class TestClass {

    // Inner class to encapsulate the logic for finding Articulation Points (Type A cities) 
    // and Type B cities.
    static class Solution {
        private int N;
        private int[][] adj;
        private int[] disc; // Discovery time
        private int[] low;  // Low-link value
        private int[] parent;
        private int time;
        private Set<Integer> articulationPoints; // Set of Type A cities

        public Solution(int[][] arr, int n) {
            this.N = n;
            this.adj = arr;
            this.disc = new int[N];
            this.low = new int[N];
            this.parent = new int[N];
            this.time = 0;
            this.articulationPoints = new HashSet<>();

            // Initialize arrays for DFS
            Arrays.fill(disc, -1);
            Arrays.fill(parent, -1);
        }

        /**
         * Finds all articulation points (Type A cities) in the graph.
         */
        public void findArticulationPoints() {
            // Iterate through all vertices in case the graph is disconnected
            for (int i = 0; i < N; i++) {
                if (disc[i] == -1) {
                    DFS(i);
                }
            }
        }

        /**
         * DFS traversal to calculate discovery times and low-link values, 
         * and identify articulation points.
         */
        private void DFS(int u) {
            int children = 0;
            // Initialize discovery time and low-link value for u
            disc[u] = low[u] = ++time;

            // Iterate through neighbors of u
            for (int v = 0; v < N; v++) {
                // Check if v is a neighbor (adj[u][v] == 1) and not the same vertex (u != v)
                if (adj[u][v] == 1 && u != v) {
                    
                    if (disc[v] == -1) { // v is not visited (Tree edge)
                        children++;
                        parent[v] = u;
                        DFS(v);

                        // Update low[u] based on low[v]
                        low[u] = Math.min(low[u], low[v]);

                        // Check if u is an articulation point based on DFS criteria:
                        
                        // 1. If u is the root of the DFS tree and has at least two children.
                        if (parent[u] == -1 && children > 1) {
                            articulationPoints.add(u);
                        }
                        
                        // 2. If u is not the root, and there is a child v such that low[v] >= disc[u].
                        if (parent[u] != -1 && low[v] >= disc[u]) {
                            articulationPoints.add(u);
                        }
                    } else if (v != parent[u]) { // v is visited and not the parent (Back edge)
                        // Update low[u] using the discovery time of v
                        low[u] = Math.min(low[u], disc[v]);
                    }
                }
            }
        }

        /**
         * Identifies Type B cities.
         * Type B cities are defined as those connected directly to Type A cities.
         * * @return A sorted set of Type B city indices.
         */
        public Set<Integer> findTypeBCities() {
            // Ensure articulation points are identified first
            findArticulationPoints();

            // Use TreeSet to store Type B cities, which automatically keeps them sorted.
            Set<Integer> typeBCities = new TreeSet<>();

            // Iterate through all cities
            for (int city = 0; city < N; city++) {
                
                // A city cannot be both Type A and Type B simultaneously based on the definition
                if (articulationPoints.contains(city)) {
                    continue;
                }

                // Check if the current city is directly connected to any Type A city
                for (int ap : articulationPoints) {
                    if (adj[city][ap] == 1) {
                        typeBCities.add(city);
                        // Once connected to one Type A city, it's a Type B city.
                        break; 
                    }
                }
            }
            return typeBCities;
        }
    }

    // The main function to solve the problem
    static void connectedVertex(int[][] arr, int n) {
        // Instantiate the solution helper with the provided graph data
        Solution solver = new Solution(arr, n);
        
        // Find Type B cities
        Set<Integer> typeBCities = solver.findTypeBCities();
        
        // Print Type B cities in ascending order separated by single space
        StringBuilder result = new StringBuilder();
        for (int city : typeBCities) {
            result.append(city).append(" ");
        }
        
        // Print the result, trimming the trailing space
        if (result.length() > 0) {
            System.out.println(result.toString().trim());
        }
    }

    // Main method provided in the template for input handling.
    public static void main(String[] args) {
        // We use Scanner for input as specified in the provided Java code snippet (File 3).
        Scanner input = new Scanner(System.in);
        
        // Read N (number of cities)
        int n = input.nextInt();
        
        // Read the N x N adjacency matrix
        int arr[][] = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = input.nextInt();
            }
        }
        
        // Call the method to find and print Type B cities
        connectedVertex(arr, n);
        
        input.close();
    }
}
