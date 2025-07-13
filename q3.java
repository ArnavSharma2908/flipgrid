import java.util.*;

public class TestClass {
    // We use static variables to store grid dimensions, number of slabs, 
    // the grid data, total sum of elements, and the maximum covered sum found.
    static int N, K;
    static int[][] arr;
    static long totalSum = 0;
    static long maxCoveredSum = 0;
    static List<Slab> allSlabs = new ArrayList<>();

    // Slab class to represent a wooden slab covering two adjacent cells.
    static class Slab {
        int r1, c1, r2, c2; // Coordinates of the two cells covered
        long weight; // Sum of values of the two covered cells

        Slab(int r1, int c1, int r2, int c2, long weight) {
            this.r1 = r1; this.c1 = c1;
            this.r2 = r2; this.c2 = c2;
            this.weight = weight;
        }

        // Checks if this slab overlaps with another slab.
        boolean overlaps(Slab other) {
            // Overlap occurs if any cell of this slab is the same as any cell of the other slab.
            return (r1 == other.r1 && c1 == other.c1) || (r1 == other.r2 && c1 == other.c2) ||
                   (r2 == other.r1 && c2 == other.c1) || (r2 == other.r2 && c2 == other.c2);
        }
    }

    // Optimized backtracking function to find the maximum covered sum.
    // 'startIdx' is the index in 'allSlabs' from which to start searching.
    // 'kRem' is the remaining number of slabs to place.
    // 'currentSum' is the sum of weights of slabs placed in the current configuration.
    // 'usedIndices' stores indices of slabs already placed in the current configuration.
    static void backtrack(int startIdx, int kRem, long currentSum, Set<Integer> usedIndices) {
        if (kRem == 0) {
            maxCoveredSum = Math.max(maxCoveredSum, currentSum);
            return;
        }

        for (int i = startIdx; i < allSlabs.size(); i++) {
            Slab currentSlab = allSlabs.get(i);
            
            // Check for overlap with previously placed slabs.
            boolean overlaps = false;
            for (int index : usedIndices) {
                if (currentSlab.overlaps(allSlabs.get(index))) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                // If no overlap, include the slab and recurse.
                usedIndices.add(i);
                backtrack(i + 1, kRem - 1, currentSum + currentSlab.weight, usedIndices);
                usedIndices.remove(i); // Backtrack and remove the slab for the next iteration.
            }
        }
    }

    public static void sumOfVisibleCells(int nVal, int kVal, int[][] arrVal) {
        N = nVal;
        K = kVal;
        arr = arrVal;
        
        // 1. Calculate the total sum of all elements in the grid.
        for (int[] row : arr) {
            for (int val : row) {
                totalSum += val;
            }
        }
        
        // 2. Generate all possible horizontal and vertical slabs.
        
        // Horizontal slabs
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N - 1; j++) {
                allSlabs.add(new Slab(i, j, i, j + 1, (long) arr[i][j] + arr[i][j + 1]));
            }
        }
        
        // Vertical slabs
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N; j++) {
                allSlabs.add(new Slab(i, j, i + 1, j, (long) arr[i][j] + arr[i + 1][j]));
            }
        }
        
        // 3. Use backtracking to find the maximum covered sum with K non-overlapping slabs.
        // Since K is very small (<=8), this backtracking approach is feasible.
        backtrack(0, K, 0, new HashSet<>());
        
        // 4. The minimum visible sum is the total sum minus the maximum covered sum.
        System.out.println(totalSum - maxCoveredSum);
    }
    
    public static void main(String[] args) {
        // Main function handles input reading and calls sumOfVisibleCells.
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[][] arr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = sc.nextInt();
            }
        }
        
        sumOfVisibleCells(n, k, arr);
        sc.close();
    }
}
