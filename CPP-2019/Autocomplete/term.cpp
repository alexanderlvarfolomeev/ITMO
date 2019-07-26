#include <utility>
#include <algorithm>

#include "term.hpp"

term::term(const std::string &str, int w) : _string(str), weight(w) {}

term::term(const term &) = default;

term::term(term &&t) noexcept : _string(std::move(t._string)), weight(t.weight) {}

term &term::operator=(const term &t) = default;

bool operator==(const term &term1, const term &term2) {
    return term1._string == term2._string && term1.weight == term2.weight;
}

bool operator!=(const term &term1, const term &term2) {
    return !(term1 == term2);
}

bool operator<(const term &term1, const term &term2) {
    return term1._string < term2._string || (term1._string == term2._string && term1.weight < term2.weight);
}

bool operator>(const term &term1, const term &term2) {
    return term1._string > term2._string || (term1._string == term2._string && term1.weight > term2.weight);
}

bool operator<=(const term &term1, const term &term2) {
    return !(term1 > term2);
}

bool operator>=(const term &term1, const term &term2) {
    return !(term1 < term2);
}

std::string term::to_string() {
    return std::to_string(weight) + ' ' + _string;
}

std::ostream &operator<<(std::ostream &strm, term &t) {
    return strm << t.to_string();
}

term::Func term::by_reverse_weight_order() {
    return [](const term &term1, const term &term2) -> bool { return term2.weight < term1.weight; };
}

term::Func term::by_prefix_order(int r) {
    return [r](const term &term1, const term &term2) -> bool {
        return std::lexicographical_compare(term1._string.begin(),
                                            term1._string.begin() +
                                            std::min(static_cast<size_t>(r), term1._string.size()),
                                            term2._string.begin(),
                                            term2._string.begin() +
                                            std::min(static_cast<size_t>(r), term2._string.size()));
    };
}