#pragma once

#include <vector>
#include <string>

#include "binary_search_deluxe.hpp"
#include "term.hpp"

class autocomplete {
    std::vector<term> terms;
public:
    explicit autocomplete(const std::vector<term> &);

    autocomplete &operator=(const autocomplete &) = delete;

    std::vector<term> all_matches(const std::string &prefix) const;

    int number_of_matches(const std::string &prefix) const;
};