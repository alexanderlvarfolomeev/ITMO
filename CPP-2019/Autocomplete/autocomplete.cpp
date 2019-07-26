#include <algorithm>

#include "autocomplete.hpp"

autocomplete::autocomplete(const std::vector<term> &a) : terms(a) {
    std::sort(terms.begin(), terms.end());
}

int autocomplete::number_of_matches(const std::string &prefix) const {
    term::Func f = term::by_prefix_order(prefix.size());
    term t(prefix, 0);
    return binary_search_deluxe::last_index_of(terms, t, f) - binary_search_deluxe::first_index_of(terms, t, f);
}

std::vector<term> autocomplete::all_matches(const std::string &prefix) const {
    term::Func f = term::by_prefix_order(prefix.size());
    term t(prefix, 0);
    int b = binary_search_deluxe::first_index_of(terms, t, f);
    int e = binary_search_deluxe::last_index_of(terms, t, f);
    std::vector<term> ts(terms.begin() + b, terms.begin() + e);
    std::sort(ts.begin(), ts.end(), term::by_reverse_weight_order());
    return ts;
}