//
// Created by F on 28.05.2019.
//

#pragma onceonce

#include <iostream> //TODO remove it
#include <list>
#include <map>
#include "board.hpp"


class path_iterator : public std::iterator<std::input_iterator_tag, const board> {

    friend class solver;

    std::list<board, std::allocator<board>>::const_iterator ptr;


public:
    explicit path_iterator(std::list<board, std::allocator<board>>::const_iterator iterator) : ptr(iterator) {}

    bool operator==(path_iterator const &it) const {
        return ptr == it.ptr;
    }

    bool operator!=(path_iterator const &it) const {
        return ptr != it.ptr;
    }

    path_iterator::reference operator*() const {
        return *ptr;
    }

    std::list<board, std::allocator<board>>::const_iterator operator->() const {
        return ptr;
    }

    path_iterator operator++() {
        ptr++;
        return *this;
    }
};

class solver {
    bool solvable;

public:
    std::list<board> path;

    size_t moves();

    explicit solver(const board &);

    solver(solver &);

    path_iterator begin() const;

    path_iterator end() const;

};
