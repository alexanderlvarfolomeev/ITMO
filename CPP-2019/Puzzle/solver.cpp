//
// Created by F on 28.05.2019.
//

#include "solver.hpp"
#include <set>

size_t solver::moves() {
    return !path.empty() ? path.size() - 1 : 0;
}

solver::solver(const board &b) {
    solvable = b.is_solvable();
    if (!solvable) {
        path = {};
        return;
    }
    std::set<board> closed;
    std::set<board> opened;
    opened.insert(b);

    std::map<const board, const board> came_from;
    std::map<const board, unsigned int> g_score;
    g_score.insert({b, 0});
    while (!opened.empty()) {
        //std::cout << '(' << closed.size() << ' ' << opened.size() /*<< ' ' << came_from.size()*/ << ") ";
        auto current = *opened.begin();
        if (current.is_goal()) {
            path.push_front(current);
            auto cur = came_from.find(current);
            while (cur != came_from.end()) {
                path.push_front(cur->second);
                cur = came_from.find(cur->second);
            }
            std::cout << '\n' << opened.size() + closed.size() << '\n';
            return;
        }
        closed.insert(current);
        opened.erase(current);
        for (auto &neighbor : current.generate_neighbors()) {
            if (closed.find(neighbor) != closed.end()) {
                continue;
            }
            unsigned score = g_score.find(current)->second + 1;
            std::cout << current.manhattan() << ' ';
            if (opened.find(neighbor) == opened.end() && closed.find(neighbor) == closed.end()) {
                opened.insert(neighbor);
                came_from.insert({neighbor, current});
                g_score.insert({neighbor, score});
            } else {
                auto neighbor_score = g_score.find(neighbor);
                neighbor_score->second = std::min(g_score.find(current)->second + 1, neighbor_score->second);
            }
        }
        g_score.erase(current);
        //closed.erase(current);
    }
}

solver::solver(solver &other) = default;

path_iterator solver::begin() const {
    return path_iterator(path.cbegin());
}

path_iterator solver::end() const {
    return path_iterator(path.cend());
}