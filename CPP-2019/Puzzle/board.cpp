//
// Created by F on 28.05.2019.
//

#include "board.hpp"

#include <numeric>
#include <ctime>

char board::random(char n) {
    return static_cast<char>(std::time(nullptr) % (n + 1));
}

std::vector<board> board::generate_neighbors() {
    std::vector<board> neighbors;
    auto st = state;
    if (space.second != 0) {
        std::swap(st[space.first][space.second], st[space.first][space.second - 1]);
        neighbors.emplace_back(st);
        std::swap(st[space.first][space.second], st[space.first][space.second - 1]);
    }
    if (space.second != width - 1) {
        std::swap(st[space.first][space.second], st[space.first][space.second + 1]);
        neighbors.emplace_back(st);
        std::swap(st[space.first][space.second], st[space.first][space.second + 1]);
    }
    if (space.first != 0) {
        std::swap(st[space.first][space.second], st[space.first - 1][space.second]);
        neighbors.emplace_back(st);
        std::swap(st[space.first][space.second], st[space.first - 1][space.second]);
    }
    if (space.first != height - 1) {
        std::swap(st[space.first][space.second], st[space.first + 1][space.second]);
        neighbors.emplace_back(st);
    }
    return neighbors;
}

board::board() = default;

board::board(const std::vector<std::vector<char>> &state) :
        state(state),
        height(static_cast<char>(state.size())),
        width(static_cast<char>(height == 0 ? 0 : state[0].size())) {
    for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
            if (state[i][j] == height * width) {
                space = {i, j};
            }
        }
    }
    manhattan_result = manhattan_precalc();
}

board::board(char height, char width) : height(height), width(width) {
    generate_state(height, width, state, space);
    manhattan_result = manhattan_precalc();
}

board::board(const board &b) = default;

void board::generate_state(char height, char width,
                           std::vector<std::vector<char>> &state,
                           std::pair<char, char> &space) {

    std::vector<char> permutation(static_cast<unsigned int>(height * width));
    std::iota(permutation.begin(), permutation.end(), 1);
    for (int i = permutation.size() - 1; i >= 0; --i) {
        char j = random(static_cast<char>(i));
        std::swap(permutation[i], permutation[j]);
    }
    for (int i = 0; i < height; ++i) {
        state.emplace_back();
        for (int j = 0; j < width; ++j) {
            state[i].push_back(permutation[i * width + j]);
            if (state[i][j] == height * width) {
                space = {i, j};
            }
        }
    }
}

const std::pair<char, char> board::get_space() const {
    return space;
}

bool board::is_space(char i, char j) const {
    return space == std::make_pair(i, j);
}

std::pair<char, char> board::size() const {
    return {height, width};
}

unsigned int board::hamming() const {
    unsigned int result = 0;
    for (unsigned char i = 0; i < height; ++i) {
        for (unsigned char j = 0; j < width; ++j) {
            if (state[i][j] != i * width + j + 1 && !is_space(i, j)) {
                result++;
            }
        }
    }
    return result;
}

unsigned int board::manhattan_precalc() const {
    unsigned int result = 0;
    for (unsigned char i = 0; i < height; ++i) {
        for (unsigned char j = 0; j < width; ++j) {
            if (state[i][j] != i * width + j + 1) {
                if (is_space(i, j)) {
                    continue;
                }
                result += std::abs(i - (state[i][j] - 1) / width) +
                          std::abs(j - (state[i][j] - 1) % width);
            }
        }
    }
    return result;
}

unsigned int board::manhattan() const {
    return manhattan_result;
}

bool board::is_goal() const {
    return hamming() == 0;
}

bool board::is_solvable() const {
    if (height == 0 || width == 0) {
        return true;
    }
    if (height == 1) {
        auto max = is_space(0, 0) && width > 1 ? 1 : 0;
        for (auto i = static_cast<unsigned char>(max + 1); i < width; ++i) {
            if (is_space(0, i)) {
                continue;
            }
            if (state[0][max] > state[0][i]) {
                return false;
            }
            max = i;
        }
        return true;
    }
    int result = 0;
    std::vector<unsigned char> count = std::vector(height * width, static_cast<unsigned char>(0));
    for (char i = static_cast<char>(height - 1); i >= 0; --i) {
        for (char j = static_cast<char>(width - 1); j >= 0; --j) {
            if (is_space(static_cast<unsigned char>(i), static_cast<unsigned char>(j))) {
                result += i + height*width + 1 + i*width;
                continue;
            }
            result += count[state[i][j] - 1];
            for (auto k = state[i][j]; k < count.size(); ++k) {
                count[k]++;
            }
        }
    }
    return result % 2 == 0;
}

bool operator==(const board &board1, const board &board2) {
    if (board1.width != board2.width || board1.height != board2.height) {
        return false;
    }
    bool result = true;
    for (auto i = 0; i < board1.height; ++i) {
        for (auto j = 0; j < board1.width; ++j) {
            result &= board1.state[i][j] == board2.state[i][j];
        }
    }
    return result;
}

bool operator!=(const board &board1, const board &board2) {
    return !(board1 == board2);
}

bool operator<(const board &board1, const board &board2) {
    if (board1.manhattan() != board2.manhattan()) {
        return board1.manhattan() < board2.manhattan();
    }
    if (board1.height != board2.height || board1.width != board2.width) {
        return false;
    }
    for (int i = 0; i < board1.height; ++i) {
        for (int j = 0; j < board1.width; ++j) {
            if (board1.state[i][j] > board2.state[i][j]) {
                return false;
            }
            if (board1.state[i][j] < board2.state[i][j]) {
                return true;
            }
        }
    }
    return false;
}

bool operator>(const board &board1, const board &board2) {
    return board2 < board1;
}

board &board::operator=(const board &other) = default;

const std::vector<char> &board::operator[](int n) {
    return state[n];
}

std::string &board::n_times(std::string &str, size_t count, char c = ' ') {
    str.insert(0, count, c);
    return str;
}

std::string board::to_string() const {
    std::string result;
    unsigned int number_size = std::to_string(height * width - 1).length();
    std::string empty;
    for (auto line : state) {
        for (auto number : line) {
            if (number == height * width) {
                result += n_times(empty, number_size, '_') + ' ';
            } else {
                std::string res = std::to_string(number);
                result += n_times(res, number_size - res.length(), ' ') + ' ';
            }
        }
        result += '\n';
    }
    return result;
}

std::ostream &operator<<(std::ostream &stream, const board &b) {
    return stream << b.to_string();
}