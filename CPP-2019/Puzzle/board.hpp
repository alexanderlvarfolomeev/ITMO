//
// Created by F on 28.05.2019.
//

#pragma once

#include <vector>
#include <string>

class board {
    std::vector<std::vector<char>> state;
    char height;
    char width;
    unsigned int manhattan_result;
    std::pair<char, char> space;

    static char random(char);

    static void generate_state(char, char,
                               std::vector<std::vector<char>> &,
                               std::pair<char, char> &);

    static std::string &n_times(std::string &, size_t, char);
public:
    std::vector<board> generate_neighbors();

    board();

    explicit board(const std::vector<std::vector<char>> &);

    board(char, char);

    board(const board &);

    const std::pair<char, char> get_space() const;

    bool is_space(char, char) const;

    std::pair<char, char> size() const;

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

    const std::vector<char> &operator[](int n);

    std::string to_string() const;

    friend std::ostream &operator<<(std::ostream &stream, const board &b);
};