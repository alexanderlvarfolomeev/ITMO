#include <iostream>
#include <vector>
#include <string>
#include <cmath>
#include <numeric>
#include <ctime>

size_t random(unsigned int n) {
    return std::time(nullptr) % (n + 1);
}

class board {
    std::vector<std::vector<int>> state;
    size_t height;
    size_t width;
    std::pair<size_t, size_t> space;
public:
    board() : height(), width(), space() {};

    explicit board(const std::vector<std::vector<int>> &state) {
        /*
        height = state.size();
        width = height == 0 ? 0 : state[0].size();
        this->state.reserve(height);
        for (int i = 0; i < height; ++i) {
            this->state[i].reserve(width);
            std::copy(state[i].begin(), state[i].end(), this->state.begin());
        }
        */
        height = state.size();
        width = height == 0 ? 0 : state[0].size();
        this->state = state;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (state[i][j] == height * width) {
                    space = {i, j};
                    return;
                }
            }
        }
    }

    board(size_t height, size_t width) {
        this->height = height;
        this->width = width;
        generate_state(height, width, state, space);
    }

    static void generate_state(const size_t height, const size_t width, std::vector<std::vector<int>> &state,
                               std::pair<size_t, size_t> &space) {
        std::vector<int> permutation(height * width);
        std::iota(permutation.begin(), permutation.end(), 1);
        for (int i = permutation.size() - 1; i >= 0; --i) {
            int j = random(static_cast<unsigned int>(i));
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

    bool is_space(size_t i, size_t j) {
        return space == std::make_pair(i, j);
    }

    std::pair<unsigned int, unsigned int> size() {
        return {height, width};
    }

    unsigned int hamming() {
        unsigned int result = 0;
        for (size_t i = 0; i < height; ++i) {
            for (size_t j = 0; j < width; ++j) {
                if (state[i][j] != i * width + j + 1 && !is_space(i, j)) {
                    result++;
                }
            }
        }
        return result;
    }

    unsigned int manhattan() {
        unsigned int result = 0;
        for (auto i = 0; i < height; ++i) {
            for (auto j = 0; j < width; ++j) {
                if (state[i][j] != i * width + j + 1) {
                    if (is_space(static_cast<size_t>(i), static_cast<size_t>(j))) {
                        continue;
                    }
                    result += std::abs(i - (state[i][j] - 1) / static_cast<int>(width)) +
                              std::abs(j - (state[i][j] - 1) % static_cast<int>(width));
                }
            }
        }
        return result;
    }

    bool is_goal() {
        return hamming() == 0;
    }

    bool is_solvable() {
        if (height == 0 || width == 0) {
            return true;
        }
        if (height == 1) {
            auto max = is_space(0, 0) && width > 1 ? 1 : 0;
            for (int i = max + 1; i < width; ++i) {
                if (is_space(0, static_cast<size_t>(i))) {
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
        std::vector<int> count = std::vector(height * width - 1, 0);
        for (size_t i = 0; i < height; ++i) {
            for (size_t j = 0; j < width; ++j) {
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

    bool operator==(const board &other) {
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

    bool operator!=(const board &other) {
        return !operator==(other);
    }

    std::vector<int> &operator[](int n) {
        return state[n];
    }

    std::string &n_times(std::string &str, const size_t count, const char c = ' ') {
        str.insert(0, count, c);
        return str;
    }

    std::string to_string() {
        std::string result;
        unsigned int number_size = std::to_string(height * width).length();
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

    friend std::ostream &operator<<(std::ostream &stream, board &b) {
        return stream << b.to_string();
    }
};

int main() {
    board b1 = board({{1, 3, 4},
                      {2, 5, 6}});
    board b(2, 3);
    std::cout << b << '\n' << '(' << b.size().first << ':' << b.size().second << ')' << ' '
              << b.hamming() << ' ' << b.manhattan() << ' '
              << (b.is_solvable() ? "solvable" : "not solvable") << '\n';
    return 0;
}