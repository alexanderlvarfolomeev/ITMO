//
// Created by F on 28.05.2019.
//

#pragma once

#include <vector>
#include <string>

class board {
    std::vector<std::vector<int>> state;
    int height;
    int width;
    unsigned int manhattan_result;
    std::pair<int, int> space;

    static void generate_state(int, int,
                               std::vector<std::vector<int>> &,
                               std::pair<int, int> &);

    static std::string &n_times(std::string &, size_t, char);
public:
    std::vector<board> generate_neighbors() const;

    board();

    explicit board(const std::vector<std::vector<int>> &);

    board(int, int);

    explicit board(int);

    board(const board &);

    bool is_space(int, int) const;

    std::pair<int, int> size() const;

    unsigned int hamming() const;

    unsigned int manhattan_precalc() const;

    unsigned int manhattan() const;

    bool is_goal() const;

    bool is_solvable() const;

    friend bool operator==(const board &, const board &);

    friend bool operator!=(const board &, const board &);

    friend bool operator<(const board &, const board &);

    friend bool operator>(const board &, const board &);

    board &operator=(const board &);

    const std::vector<int> &operator[](int n);

    std::string to_string() const;

    friend std::ostream &operator<<(std::ostream &stream, const board &b);
};