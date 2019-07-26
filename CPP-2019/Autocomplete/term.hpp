#pragma once

#include <string>
#include <functional>

class term {
    int weight;
    std::string _string;
public:
    term(const std::string &, int);

    term(const term &);

    term(term &&) noexcept;

    term &operator=(const term &);

    friend bool operator==(const term &, const term &);

    friend bool operator!=(const term &, const term &);

    friend bool operator<(const term &, const term &);

    friend bool operator>(const term &, const term &);

    friend bool operator<=(const term &, const term &);

    friend bool operator>=(const term &, const term &);

    std::string to_string();

    friend std::ostream &operator<<(std::ostream &, term &);

    using Func = std::function<bool(const term &, const term &)>;

    static Func by_reverse_weight_order();

    static Func by_prefix_order(int);
};