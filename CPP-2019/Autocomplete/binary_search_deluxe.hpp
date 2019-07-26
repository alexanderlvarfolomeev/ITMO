#pragma once

#include <vector>
#include <functional>

#include "term.hpp"

class binary_search_deluxe {
public:
    binary_search_deluxe() = delete;

    binary_search_deluxe(const binary_search_deluxe &) = delete;

    binary_search_deluxe &operator=(const binary_search_deluxe &) = delete;

    static int first_index_of(const std::vector<term> &, const term &, const term::Func &);

    static int last_index_of(const std::vector<term> &, const term &, const term::Func &);
};