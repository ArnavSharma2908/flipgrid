import java.util.*;

class TestClass {

    // Helper class to encapsulate the Articulation Point (Type A city) finding logic.
    // This allows for cleaner state management (disc, low, parent arrays) for the DFS traversal.
    static class APFinder {
        int N;
        int[][] adj;
        int[] disc, low, parent;
        int time;
        Set<Integer> aps = new HashSet<>(); // Articulation Points (Type A cities)

        APFinder(int[][] a, int n) {
            N = n;
            adj = a;
            disc = new int[n];
            low = new int[n];
            parent = new int[n];
            Arrays.fill(disc, -1);
            Arrays.fill(parent, -1);
        }

        // DFS implementation to find Articulation Points
        void dfs(int u) {
            int children = 0;
            disc[u] = low[u] = ++time;

            for (int v = 0; v < N; v++) {
                if (adj[u][v] == 1 && u != v) {
                    if (disc[v] == -1) {
                        children++;
                        parent[v] = u;
                        dfs(v);

                        low[u] = Math.min(low[u], low[v]);

                        // Check 1: u is root of DFS tree and has >= 2 children
                        if (parent[u] == -1 && children > 1) {
                            aps.add(u);
                        }
                        // Check 2: u is not root, and low[v] >= disc[u]
                        if (parent[u] != -1 && low[v] >= disc[u]) {
                            aps.add(u);
                        }
                    } else if (v != parent[u]) {
                        low[u] = Math.min(low[u], disc[v]);
                    }
                }
            }
        }
    }

    // The method to find and print Type B cities (connected to Type A cities)
    static void connectedVertex(int[][] arr, int n) {
        
        // 1. Find Articulation Points (Type A cities)
        APFinder apFinder = new APFinder(arr, n);
        for(int i = 0; i < n; i++) {
            if(apFinder.disc[i] == -1) {
                apFinder.dfs(i);
            }
        }
        
        Set<Integer> typeA = apFinder.aps;
        // Use TreeSet to ensure Type B cities are stored and retrieved in ascending order
        Set<Integer> typeB = new TreeSet<>(); 

        // 2. Identify Type B cities
        for (int i = 0; i < n; i++) {
            // Skip if the city is Type A itself
            if (typeA.contains(i)) continue;

            // Check if city i is connected to any Type A city
            for (int ap : typeA) {
                if (arr[i][ap] == 1) {
                    typeB.add(i);
                    break; // Found a connection, it's a Type B city
                }
            }
        }

        // 3. Output Type B cities separated by space
        StringBuilder result = new StringBuilder();
        for (int city : typeB) {
            result.append(city).append(" ");
        }
        
        if (result.length() > 0) {
            System.out.println(result.toString().trim());
        }
    }

    // Main method provided for input handling
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int arr[][] = new int[n][n];
        
        // Read the adjacency matrix
        for (int i = 0; i < n; ++i){
            for (int j = 0; j < n; ++j){
                arr[i][j] = input.nextInt();
            }
        }
        
        // Execute the solution
        connectedVertex(arr, n);
        input.close();
    }
}
