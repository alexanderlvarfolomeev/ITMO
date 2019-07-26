#include <iostream>
#include "randomized_deque.hpp"

int main() {
    randomized_deque<int> d;
    for (int i = 0; i < 10; ++i) {
        d.push_back(i);
    }
    for (auto &i : d) {
        std::cout << i << ' ';
    }
    std::cout << '\n';
    auto y = d.begin();
    auto &x = d.back();
    x = 100;
    *y = 50;
    for (auto &i : d) {
        std::cout << i << ' ';
    }
}
