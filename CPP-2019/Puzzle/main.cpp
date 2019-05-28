#include <iostream>
#include <cmath>
#include <ctime>

#include "board.hpp"
#include "solver.hpp"


int main() {
    board b0 = board({{1, 4},
                      {3, 2}});
    board b1 = board({{1, 2, 3},
                      {4, 5, 6},
                      {7, 8, 9}});
    board b2 = board({{1, 2, 3},
                      {4, 5, 6}});
    /*
    board b3 = board({{14, 11, 9,  12},
                      {15, 10, 13, 8},
                      {6,  7,  5,  1},
                      {3,  2,  4,  16}});
    board b4 = board({{8,  3,  4, 6},
                      {11, 1,  5, 10},
                      {2,  12, 7, 9}});
    board b5 = board({{8, 3, 4, 6,  7},
                      {2, 1, 5, 10, 9}});
    board b6 = board({{8,  3,  14, 12, 7},
                      {2,  11, 5,  10, 6},
                      {13, 1,  15, 4,  9}});
    */
    board b7 = board({{6,  1,  21, 15, 14},
                      {19, 10, 9,  25, 16},
                      {24, 18, 5,  8,  23},
                      {13, 2,  20, 11, 22},
                      {7,  12, 17, 4,  3}});
    long long t = std::time(nullptr);
    board b(5, 5);
    solver s = solver(b7);
    /*
    std::cout << b << '\n' << '(' << b.size().first << ':' << b.size().second << ')' << ' '
              << b.hamming() << ' ' << b.manhattan() << ' '
              << (b.is_solvable() ? "solvable" : "not solvable") << "\n\n";
    */
    std::cout << '\n';
    for (const auto &i : s) {
        std::cout << i << '\n';
    }
    t = std::time(nullptr) - t;
    std::cout << s.moves() << '\n' << t;
    return 0;
}