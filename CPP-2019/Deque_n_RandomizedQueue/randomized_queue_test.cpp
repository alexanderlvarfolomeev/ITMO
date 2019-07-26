#include <iostream>
#include <cassert>
#include "randomized_queue.hpp"

int main() {
    randomized_queue<int> d;
    for (int i = 0; i < 10; ++i) {
        d.enqueue(i);
    }
    for (auto &i : d) {
        std::cout << i << ' ';
    }
    auto x = d.begin();
    *x = 100;
    std::cout << '\n';
    for (auto &i : d) {
        std::cout << i << ' ';
    }
    std::cout << '\n';
    for (int i = 0; i < 10; ++i) {
        std::cout << d.sample() << ' ';
    }
    std::cout << '\n';
    std::cout << std::boolalpha << d.empty() << ' ' << d.size() << ' ';
    std::cout << '\n';
    while (!d.empty()) {
        std::cout << d.dequeue() << ' ';
    }
    std::cout << '\n';
    std::cout << d.size();
    std::cout << '\n';
    for (auto &i : d) {
        std::cout << i << ' ';
    }

    randomized_queue<int> q;
    for (int i = 0; i < 5; ++i) {
        q.enqueue(i);
    }
    auto b1 = q.begin();
    auto e1 = q.end();
    auto b2 = q.begin();
    auto e2 = q.end();

    std::vector<int> v11, v12;
    std::copy(b1, e1, std::back_inserter(v11));
    std::copy(b1, e1, std::back_inserter(v12));
    assert(v11 == v12); // Два прохода одним итератором дают одинаковую последовательность

    std::vector<int> v21, v22;
    std::copy(b2, e2, std::back_inserter(v21));
    std::copy(b2, e2, std::back_inserter(v22));
    assert(v21 == v22); // Два прохода одним итератором дают одинаковую последовательность

    assert(v11 != v21); // С высокой степенью вероятности, два разных итератора задают разные последовательности

    // взятие итераторов не повлияло на очередь
    while (!q.empty()) {
        std::cout << q.dequeue() << ' ';
    }
}