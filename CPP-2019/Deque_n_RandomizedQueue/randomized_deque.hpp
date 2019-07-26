#pragma once

#include <list>

template<typename T>
class deque_iterator;

template<typename T>
class randomized_deque {
    std::list<T> data;
public:
    randomized_deque() = default;

    bool empty() {
        return data.empty();
    }

    size_t size() {
        return data.size();
    }

    void push_front(const T &element) {
        data.push_front(element);
    }

    void push_back(const T &element) {
        data.push_back(element);
    }

    void pop_front() {
        data.pop_front();
    }

    void pop_back() {
        data.pop_back();
    }

    T &front() {
        return data.front();
    }

    T &back() {
        return data.back();
    }

    const T &front() const {
        return data.front();
    }

    const T &back() const {
        return data.back();
    }

    deque_iterator<T> begin() {
        return deque_iterator<T>(data.begin());
    }

    deque_iterator<T> end() {
        return deque_iterator<T>(data.end());
    }
};

template<typename T>
class deque_iterator {
    friend class randomized_deque<T>;

    using list_iterator = typename std::list<T>::iterator;
    list_iterator data_iterator;
public:
    using difference_type = std::ptrdiff_t;
    using value_type = T;
    using pointer = value_type *;
    using reference = value_type &;
    using iterator_category = std::forward_iterator_tag;

    explicit deque_iterator(list_iterator iter) : data_iterator(iter) {}

    deque_iterator &operator++() {
        data_iterator++;
        return *this;
    }

    deque_iterator operator++(int) {
        auto temp = *this;
        data_iterator++;
        return temp;
    }

    T &operator*() {
        return *data_iterator;
    }

    const T &operator*() const {
        return *data_iterator;
    }

    friend bool operator==(const deque_iterator &iter1, const deque_iterator &iter2) {
        return iter1.data_iterator == iter2.data_iterator;
    }

    friend bool operator!=(const deque_iterator &iter1, const deque_iterator &iter2) {
        return iter1.data_iterator != iter2.data_iterator;
    }
};
