#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <utility> // for std::min

using namespace std;

// Helper function for DFS-based Articulation Point (Type A) finding.
// This function relies on external state passed by reference for discovery times, low-link values, 
// parent tracking, a timer, and the set of articulation points.
void find_aps_dfs(int u, int N, int** adj, vector<int>& disc, vector<int>& low, 
                  vector<int>& parent, int& timer, set<int>& aps) {
    
    int children = 0;
    disc[u] = low[u] = ++timer;

    // Iterate through all cities to find neighbors (using adjacency matrix)
    for (int v = 0; v < N; ++v) {
        if (adj[u][v] == 1 && u != v) {
            if (disc[v] == -1) { // Tree edge
                children++;
                parent[v] = u;
                
                // Recurse
                find_aps_dfs(v, N, adj, disc, low, parent, timer, aps);

                // Update low[u] based on low[v]
                low[u] = min(low[u], low[v]);

                // Check for Articulation Point criteria:
                // 1. Root of DFS tree with >= 2 children
                if (parent[u] == -1 && children > 1) {
                    aps.insert(u);
                }
                // 2. Non-root, and no back edge from v's subtree to an ancestor of u
                if (parent[u] != -1 && low[v] >= disc[u]) {
                    aps.insert(u);
                }
            } else if (v != parent[u]) { // Back edge
                low[u] = min(low[u], disc[v]);
            }
        }
    }
}

// Function that identifies and prints Type B cities following the template.
void connectedVertex(int **arr, int n) {
    // WRITE YOU CODE HERE

    // 1. Setup for Articulation Points (Type A) finding
    vector<int> disc(n, -1), low(n, -1), parent(n, -1);
    int timer = 0;
    set<int> typeA; // Type A cities (Articulation Points)

    // Run DFS to find all articulation points
    for (int i = 0; i < n; ++i) {
        if (disc[i] == -1) {
            find_aps_dfs(i, n, arr, disc, low, parent, timer, typeA);
        }
    }

    // 2. Identify Type B cities
    // Type B cities are connected directly to Type A cities, but are not Type A themselves.
    set<int> typeB; // Use set for sorted output

    for (int i = 0; i < n; ++i) {
        if (typeA.count(i)) continue; // Skip if Type A

        for (int ap : typeA) {
            // Check connectivity between city i and articulation point ap
            if (arr[i][ap] == 1) {
                typeB.insert(i);
                break; // Found connection, move to the next city
            }
        }
    }

    // 3. Print Type B cities in ascending order separated by a single whitespace.
    // // Print Type B cities in ascending order separated by a single whitespace.
    bool first = true;
    for (int city : typeB) {
        if (!first) cout << " ";
        cout << city;
        first = false;
    }
}

// The main function structure provided in the template:
// int main() {
//     int n;
//     cin >> n;
//     int **arr;
//     arr = new int*[n];
//     for(int i=0; i<n; ++i){
//         arr[i] = new int[n];
//         for(int j=0; j<n; ++j){
//             cin >> arr[i][j];
//         }
//     }
//     
//     connectedVertex(arr, n);
//     return 0;
// }
