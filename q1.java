import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class WordSearch {

    // Function to check if a string is a palindrome
    private static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // Read N
        int N = Integer.parseInt(br.readLine());

        // Read the grid
        char[][] grid = new char[N][N];
        for (int i = 0; i < N; i++) {
            String row = br.readLine();
            for (int j = 0; j < N; j++) {
                grid[i][j] = row.charAt(j);
            }
        }

        // Read the target word
        String targetWord = br.readLine();
        int L = targetWord.length();
        
        long totalCount = 0;
        
        // Determine if the target word is a palindrome
        boolean isPal = isPalindrome(targetWord);

        // Define the 8 directions: {dr, dc}
        // {N, NE, E, SE, S, SW, W, NW}
        int[][] directions = {
            {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, 
            {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        };

        // Iterate through every cell in the grid
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                
                // Optimization: Check if the first character matches
                if (grid[r][c] != targetWord.charAt(0)) {
                    continue;
                }

                // Iterate through all 8 directions
                for (int[] dir : directions) {
                    int dr = dir[0];
                    int dc = dir[1];

                    // Check for the word match in the current direction
                    
                    int currentR = r;
                    int currentC = c;
                    boolean match = true;

                    // Check subsequent characters of the word (starting from index 1)
                    for (int k = 1; k < L; k++) {
                        currentR += dr;
                        currentC += dc;

                        // Check boundaries
                        if (currentR < 0 || currentR >= N || currentC < 0 || currentC >= N) {
                            match = false;
                            break;
                        }
                        
                        // Check character match
                        if (grid[currentR][currentC] != targetWord.charAt(k)) {
                            match = false;
                            break;
                        }
                    }

                    // If a full match is found
                    if (match) {
                        // If the word is a palindrome, count it twice, otherwise once.
                        if (isPal) {
                            totalCount += 2;
                        } else {
                            totalCount += 1;
                        }
                    }
                }
            }
        }

        System.out.println(totalCount);
    }
}
