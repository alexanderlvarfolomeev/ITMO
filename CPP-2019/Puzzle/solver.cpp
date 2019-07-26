//
// Created by F on 28.05.2019.
//

#include "solver.hpp"
#include <queue>
#include <map>

size_t solver::moves() {
    return !path.empty() ? path.size() - 1 : 0;
}

struct Compare {
    bool
    operator()(const solver::board_iterator &board_iterator1, const solver::board_iterator &board_iterator2) const {
        return *board_iterator1 > *board_iterator2;
    }
};

solver::solver(const board &b) {
    solvable = b.is_solvable();
    if (!solvable) {
        path = {};
        return;
    }
    std::vector<std::set<board>> all_boards;
    auto size_b = b.size();
    all_boards.resize(static_cast<size_t>((size_b.first + size_b.second) * size_b.first * size_b.second));
    all_boards[b.manhattan()].insert(b);
    std::priority_queue<board_iterator, std::vector<board_iterator>, Compare> opened;
    opened.push(all_boards[b.manhattan()].begin());
    std::map<board_iterator, board_iterator, Compare> came_from;
    while (!opened.empty()) {
        auto current = opened.top();
        opened.pop();
        if (current->is_goal()) {
            path.push_front(*current);
            auto cur = came_from.find(current);
            while (cur != came_from.end()) {
                path.push_front(*cur->second);
                cur = came_from.find(cur->second);
            }
            return;
        }
        for (auto &neighbor : current->generate_neighbors()) {
            if (all_boards[neighbor.manhattan()].find(neighbor) != all_boards[neighbor.manhattan()].end()) {
                continue;
            }
            auto iter = all_boards[neighbor.manhattan()].insert(std::move(neighbor));
            came_from.insert({iter.first, current});
            opened.push(iter.first);
        }
    }
}

solver::solver(solver &other) = default;

std::list<board, std::allocator<board>>::const_iterator solver::begin() const {
    return path.cbegin();
}

std::list<board, std::allocator<board>>::const_iterator solver::end() const {
    return path.cend();
}