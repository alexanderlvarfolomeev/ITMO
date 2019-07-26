#include <string>
#include <cstring>
#include <cmath>
#include <iostream>

#include "subset.hpp"
#include "randomized_queue.hpp"

int main(int argc, char **argv) {
    auto count = static_cast<size_t>(std::strtol(argv[1], nullptr, 10));
    randomized_queue<std::string> queue;
    std::string s;
    while (std::getline(std::cin, s)) {
        queue.enqueue(s);
    }
    count = std::min(count, queue.size());
    for (auto i = queue.begin(); static_cast<int>(i) < count; ++i) {
        std::cout << *i << ' ';
    }
}