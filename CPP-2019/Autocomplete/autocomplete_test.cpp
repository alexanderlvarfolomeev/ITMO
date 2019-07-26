#include <cassert>
#include <iostream>
#include <algorithm>
#include "autocomplete.hpp"

int main() {
    std::vector<term> terms{{"w",    -3},
                            {"abc",  1},
                            {"auto", 10},
                            {"bbc",  100},
                            {"abs",  -1},
                            {"as",   2},
                            {"w",    -3}};
    std::vector<term> sorted_terms = terms;
    std::sort(sorted_terms.begin(), sorted_terms.end(), term::by_reverse_weight_order());
    const autocomplete ac(terms);
    assert(sorted_terms == ac.all_matches(""));
    std::cout << "\"...\":\n";
    for (auto &i : ac.all_matches("")) {
        std::cout << i << std::endl;
    }
    assert(ac.number_of_matches("x") == 0);
    assert(ac.all_matches("x").empty());
    assert(ac.number_of_matches("a") == 4);
    std::cout << "\n\"a...\":\n";
    for (auto &i : ac.all_matches("a")) {
        std::cout << i << std::endl;
    }
    assert(ac.number_of_matches("ab") == 2);
    std::cout << "\n\"ab...\":\n";
    for (auto &i : ac.all_matches("ab")) {
        std::cout << i << std::endl;
    }
    std::cout << "\n\"w...\" count: " << ac.number_of_matches("w") << std::endl;
    return 0;
}
