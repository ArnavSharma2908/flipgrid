#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>

using namespace std;

// Function to check if a string is a palindrome
bool isPalindrome(const string& s) {
    string reversed_s = s;
    reverse(reversed_s.begin(), reversed_s.end());
    return s == reversed_s;
}

void solve() {
    int N;
    if (!(cin >> N)) return;

    // Read the grid
    vector<string> grid(N);
    for (int i = 0; i < N; ++i) {
        cin >> grid[i];
    }

    // Read the target word
    string targetWord;
    cin >> targetWord;

    int L = targetWord.length();
    long long totalCount = 0;

    // Determine if the target word is a palindrome
    bool isPal = isPalindrome(targetWord);
    
    // Define the 8 directions (dx, dy)
    // {N, NE, E, SE, S, SW, W, NW}
    int directions[8][2] = {
        {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, 
        {1, 0}, {1, -1}, {0, -1}, {-1, -1}
    };

    // Iterate through every cell in the grid
    for (int r = 0; r < N; ++r) {
        for (int c = 0; c < N; ++c) {
            
            // Check if the starting character matches the first character of the word
            if (grid[r][c] != targetWord[0]) {
                continue;
            }

            // Iterate through all 8 directions
            for (int d = 0; d < 8; ++d) {
                int dr = directions[d][0];
                int dc = directions[d][1];

                // Check if the word can fit in the grid starting from (r, c) in direction (dr, dc)
                
                int currentR = r;
                int currentC = c;
                bool match = true;

                // Check subsequent characters of the word
                for (int k = 1; k < L; ++k) {
                    currentR += dr;
                    currentC += dc;

                    // Check boundaries
                    if (currentR < 0 || currentR >= N || currentC < 0 || currentC >= N) {
                        match = false;
                        break;
                    }
                    
                    // Check character match
                    if (grid[currentR][currentC] != targetWord[k]) {
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

    cout << totalCount << endl;
}

int main() {
    // Optimization for faster I/O
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    solve();
    return 0;
}
