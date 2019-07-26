#include <iostream>
#include <cmath>
#include <ctime>

#include "board.hpp"
#include "solver.hpp"
#include <cassert>

int main() {
    long long t = std::time(nullptr);

    board b1 = board({{8,  3,  14, 12, 7},
                      {2,  11, 5,  10, 6},
                      {13, 1,  15, 4,  9}});
    board b2 = board({{6,  1,  21, 15, 14},
                      {19, 10, 9,  25, 16},
                      {24, 18, 5,  8,  23},
                      {13, 2,  20, 11, 22},
                      {7,  12, 17, 4,  3}});

    for (const auto &i : solver(b1)) {
        std::cout << i << '\n';
    }
    std::cout << std::endl;
    for (const auto &i : solver(b2)) {
        std::cout << i << '\n';
    }
    std::cout << std::endl;

    board board1(5, 5);
    board board2(5, 5);
    assert(board1 != board2);

    std::cout << board2 << std::endl;
    solver s = solver(board2);
    for (const auto &i : s) {
        std::cout << i << '\n';
    }
    std::cout << "Moves: " << s.moves() << '\n' << std::endl;

    board b(3);
    std::cout << "b:\n" << b << std::endl;
    std::cout << "b[1][1] = " << b[1][1] << '\n' << std::endl;

    t = std::time(nullptr) - t;
    std::cout << t << " seconds had passed" << std::endl;
    return 0;
}