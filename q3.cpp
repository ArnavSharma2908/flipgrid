#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>

using namespace std;

// Constants defined in the template
const int MAXN = 1000;
const int MAXK = 8; 

// Define a struct for Slab
struct Slab {
    int r1, c1, r2, c2;
    long long weight;

    // Helper to check for overlap.
    bool overlaps(const Slab& other) const {
        // A slab overlaps if they share any cell.
        return (r1 == other.r1 && c1 == other.c1) || (r1 == other.r2 && c1 == other.c2) ||
               (r2 == other.r1 && c2 == other.c1) || (r2 == other.r2 && c2 == other.c2);
    }
};

// Global variables to store all generated slabs, total sum, and maximum covered sum.
vector<Slab> allSlabs;
long long totalSum = 0;
long long maxCoveredSum = 0;
int N_global, K_global;

// Backtracking function to find the maximum weight of K non-overlapping slabs.
// We iterate through all slabs and recursively select K of them, ensuring no overlap.
void backtrack(int startIdx, int kRem, long long currentSum, vector<int>& usedIndices) {
    // Base case: K slabs have been placed.
    if (kRem == 0) {
        maxCoveredSum = max(maxCoveredSum, currentSum);
        return;
    }

    // Iterate through remaining slabs starting from startIdx.
    for (int i = startIdx; i < allSlabs.size(); ++i) {
        const Slab& currentSlab = allSlabs[i];
        
        // Check for overlap with previously placed slabs.
        bool overlaps = false;
        for (int idx : usedIndices) {
            if (currentSlab.overlaps(allSlabs[idx])) {
                overlaps = true;
                break;
            }
        }

        if (!overlaps) {
            // If no overlap, include the slab and recurse.
            usedIndices.push_back(i);
            // Recurse for the next slab, reducing kRem by 1 and increasing currentSum.
            backtrack(i + 1, kRem - 1, currentSum + currentSlab.weight, usedIndices);
            usedIndices.pop_back(); // Backtrack
        }
    }
}

// Main solving function.
void sumOfVisibleCells(int N, int K, int arr[][MAXN]) {
    N_global = N;
    K_global = K;
    
    // 1. Calculate total sum of all elements in the grid.
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            totalSum += arr[i][j];
        }
    }
    
    // 2. Generate all possible slabs.
    
    // Horizontal slabs (rows, col j to j+1)
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N - 1; ++j) {
            allSlabs.push_back({i, j, i, j + 1, (long long)arr[i][j] + arr[i][j+1]});
        }
    }
    
    // Vertical slabs (row i to i+1, cols)
    for (int i = 0; i < N - 1; ++i) {
        for (int j = 0; j < N; ++j) {
            allSlabs.push_back({i, j, i + 1, j, (long long)arr[i][j] + arr[i+1][j]});
        }
    }
    
    // 3. Run backtracking to find the maximum covered sum.
    vector<int> usedIndices;
    backtrack(0, K, 0, usedIndices);
    
    // 4. Output the minimum visible sum (Total Sum - Maximum Covered Sum).
    cout << totalSum - maxCoveredSum << endl;
}

// The main function from the template
int main() {
    // Fast IO
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n, k;
    cin >> n >> k;

    // Input array
    int arr[MAXN][MAXN];

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> arr[i][j];
        }
    }

    sumOfVisibleCells(n, k, arr);

    return 0;
}
