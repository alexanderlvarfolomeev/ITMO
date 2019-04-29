#include <iostream>
#include <vector>
#include <list>
#include <string>
#include <cmath>
#include <numeric>
#include <ctime>
#include <set>
#include <map>

unsigned char random(unsigned char n) {
    return static_cast<unsigned char>(std::time(nullptr) % (n + 1));
}

class board {
    std::vector<std::vector<unsigned char>> state;
    unsigned char height;
    unsigned char width;
    unsigned int manhattan_result;
    std::pair<unsigned char, unsigned char> space;
public:
    board() : height(), width(), space(), manhattan_result() {};

    explicit board(const std::vector<std::vector<unsigned char>> &state) :
            state(state),
            height(static_cast<unsigned char>(state.size())),
            width(static_cast<unsigned char>(height == 0 ? 0 : state[0].size())) {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (state[i][j] == height * width) {
                    space = {i, j};
                }
            }
        }
        manhattan_result = manhattan_precalc();
    }

    board(unsigned char height, unsigned char width) : height(height), width(width) {
        generate_state(height, width, state, space);
        manhattan_result = manhattan_precalc();
    }

    board(const board &b) : height(b.size().first), width(b.size().second) {
        state = b.get_state();
        space = b.space;
        manhattan_result = b.manhattan_result;
    }

    static void generate_state(const unsigned char height, const unsigned char width,
                               std::vector<std::vector<unsigned char>> &state,
                               std::pair<unsigned char, unsigned char> &space) {

        std::vector<unsigned char> permutation(height * width);
        std::iota(permutation.begin(), permutation.end(), 1);
        for (int i = permutation.size() - 1; i >= 0; --i) {
            unsigned char j = random(static_cast<unsigned char>(i));
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

    const std::pair<unsigned char, unsigned char> get_space() const {
        return space;
    }

    const std::vector<std::vector<unsigned char>> &get_state() const {
        return state;
    }

    bool is_space(unsigned char i, unsigned char j) const {
        return space == std::make_pair(i, j);
    }

    std::pair<unsigned char, unsigned char> size() const {
        return {height, width};
    }

    unsigned int hamming() const {
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

    unsigned int manhattan_precalc() const {
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

    unsigned int manhattan() const {
        return manhattan_result;
    }

    bool is_goal() const {
        return hamming() == 0;
    }

    bool is_solvable() const {
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
        unsigned int result = 0;
        std::vector<unsigned char> count = std::vector(height * width - 1, static_cast<unsigned char>(0));
        for (unsigned char i = 0; i < height; ++i) {
            for (unsigned char j = 0; j < width; ++j) {
                if (is_space(i, j)) {
                    result += i + 1;
                    continue;
                }
                result += state[i][j] - count[state[i][j] - 1] - 1;
                for (auto k = state[i][j]; k < count.size(); ++k) {
                    count[k]++;
                }
            }
        }
        return (result + height) % 2 == 0;
    }

    bool operator==(const board &other) const {
        if (width != other.width || height != other.height) {
            return false;
        }
        bool result = true;
        for (auto i = 0; i < height; ++i) {
            for (auto j = 0; j < width; ++j) {
                result &= state[i][j] == other.state[i][j];
            }
        }
        return result;
    }

    bool operator!=(const board &other) const {
        return !operator==(other);
    }

    bool operator<(const board &other) const {
        if (this->manhattan() != other.manhattan()) {
            return this->manhattan() < other.manhattan();
        }
        if (height != other.height || width != other.width) {
            return false;
        }
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (state[i][j] > other.state[i][j]) {
                    return false;
                }
                if (state[i][j] < other.state[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    bool operator>(const board &other) const {
        return other < *this;
    }

    board &operator=(const board &other) = default;

    const std::vector<unsigned char> &operator[](int n) {
        return state[n];
    }

    static std::string &n_times(std::string &str, const size_t count, const char c = ' ') {
        str.insert(0, count, c);
        return str;
    }

    std::string to_string() const {
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

    friend std::ostream &operator<<(std::ostream &stream, const board &b) {
        return stream << b.to_string();
    }
};

class path_iterator : public std::iterator<std::input_iterator_tag, const board> {

    friend class solver;

    std::list<board, std::allocator<board>>::const_iterator ptr;


public:
    explicit path_iterator(std::list<board, std::allocator<board>>::const_iterator iterator) : ptr(iterator) {}

    bool operator==(path_iterator const &it) const {
        return ptr == it.ptr;
    }

    bool operator!=(path_iterator const &it) const {
        return ptr != it.ptr;
    }

    path_iterator::reference operator*() const {
        return *ptr;
    }

    std::list<board, std::allocator<board>>::const_iterator operator->() const {
        return ptr;
    }

    path_iterator operator++() {
        ptr++;
        return *this;
    }
};

class solver {
    bool solvable;

    void construct_path(std::map<const board, const board> &came_from, const board &goal) {
        std::map<const board, const board>::iterator current;
        path.push_front(goal);
        current = came_from.find(goal);
        while (current != came_from.end()) {
            path.push_front(current->second);
            current = came_from.find(current->second);
        }
    }

    void generate_neighbors(const board &b, std::vector<board> &neighbors) {
        auto[i, j] = b.get_space();
        auto table = b.get_state();
        neighbors.clear();
        if (j != 0) {
            std::swap(table[i][j], table[i][j - 1]);
            neighbors.emplace_back(table);
            std::swap(table[i][j], table[i][j - 1]);
        }
        if (j != b.size().second - 1) {
            std::swap(table[i][j], table[i][j + 1]);
            neighbors.emplace_back(table);
            std::swap(table[i][j], table[i][j + 1]);
        }
        if (i != 0) {
            std::swap(table[i][j], table[i - 1][j]);
            neighbors.emplace_back(table);
            std::swap(table[i][j], table[i - 1][j]);
        }
        if (i != b.size().first - 1) {
            std::swap(table[i][j], table[i + 1][j]);
            neighbors.emplace_back(table);
            std::swap(table[i][j], table[i + 1][j]);
        }
    }

public:
    std::list<board> path;

    size_t moves() {
        return !path.empty() ? path.size() - 1 : 0;
    }

    explicit solver(const board &b) {
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
        std::vector<board> neighbors;
        while (!opened.empty()) {
            //std::cout << '(' << closed.size() << ' ' << opened.size() /*<< ' ' << came_from.size()*/ << ") ";
            auto current = *opened.begin();
            if (current.is_goal()) {
                construct_path(came_from, current);
                return;
            }
            closed.insert(current);
            opened.erase(current);
            generate_neighbors(current, neighbors);
            for (auto &neighbor : neighbors) {
                if (closed.find(neighbor) != closed.end()) {
                    continue;
                }
                int score = g_score.find(current)->second + 1;
                std::cout << score << ' ';
                if (opened.find(neighbor) == opened.end()) {
                    opened.insert(neighbor);
                } else if (score >= g_score.find(neighbor)->second) {
                    continue;
                }
                opened.insert(neighbor);
                came_from.insert({neighbor, current});
                g_score.insert({neighbor, score});
            }
            g_score.erase(current);
        }
    }

    solver(solver &other) = default;

    path_iterator begin() const {
        return path_iterator(path.cbegin());
    }

    path_iterator end() const {
        return path_iterator(path.cend());
    }

};

int main() {
    board b1 = board({{8, 3, 4},
                      {2, 1, 5},
                      {6, 9, 7}});
    board b2 = board({{1, 3, 4},
                      {6, 2, 5}});
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
    board b7 = board({{8,  3,  14, 12, 7},
                      {2,  11, 5,  10, 6},
                      {13, 1,  15, 4,  9},
                      {17, 24, 18, 16, 20},
                      {21, 19, 23, 25, 22}});
    board b(4, 4);
    solver s = solver(b);
    /*
    std::cout << b << '\n' << '(' << b.size().first << ':' << b.size().second << ')' << ' '
              << b.hamming() << ' ' << b.manhattan() << ' '
              << (b.is_solvable() ? "solvable" : "not solvable") << "\n\n";
    */
    std::cout << '\n';
    for (const auto &i : s) {
        std::cout << i << '\n';
    }
    std::cout << s.moves();
    return 0;
}