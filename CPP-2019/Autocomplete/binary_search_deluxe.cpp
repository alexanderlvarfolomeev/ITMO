#include "binary_search_deluxe.hpp"

int binary_search_deluxe::first_index_of(const std::vector<term> &a, const term &key, const term::Func &comparator) {
    int leftBound = -1;
    int rightBound = a.size();
    while (leftBound + 1 != rightBound) {
        int middle = (leftBound + rightBound) / 2;

        if (!comparator(a[middle], key)) {
            rightBound = middle;
        } else {
            leftBound = middle;
        }
    }
    return rightBound;
}

int binary_search_deluxe::last_index_of(const std::vector<term> &a, const term &key, const term::Func &comparator) {
    int leftBound = -1;
    int rightBound = a.size();
    while (leftBound + 1 != rightBound) {
        int middle = (leftBound + rightBound) / 2;

        if (comparator(key, a[middle])) {
            rightBound = middle;
        } else {
            leftBound = middle;
        }
    }
    return rightBound;

}