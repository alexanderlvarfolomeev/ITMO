//
// Created by F on 28.05.2019.
//

#pragma onceonce

#include <list>
#include <set>
#include "board.hpp"

class solver {
    bool solvable;
public:
    using board_iterator = std::set<board>::const_iterator;

    std::list<board> path;

    size_t moves();

    explicit solver(const board &);

    solver(solver &);

    std::list<board, std::allocator<board>>::const_iterator begin() const;

    std::list<board, std::allocator<board>>::const_iterator end() const;

};
