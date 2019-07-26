#pragma once

#include <vector>
#include <cstdlib>
#include <numeric>
#include <algorithm>
#include <random>

static std::mt19937 generator(0);

inline size_t random(size_t n) {
    std::uniform_int_distribution<> dis(0, n - 1);
    return dis(generator);
}

template<typename T>
class queue_iterator;

template<typename T>
class randomized_queue {
    std::vector<T> data;
public:
    randomized_queue() = default;

    bool empty() {
        return data.empty();
    }

    size_t size() {
        return data.size();
    }

    void enqueue(const T &element) {
        data.push_back(element);
    }

    T &sample() {
        return data[random(size())];
    }

    T dequeue() {
        size_t index = random(size());
        T result = data[index];
        std::swap(data[index], data[size() - 1]);
        data.pop_back();
        return result;
    }

    queue_iterator<T> begin() {
        typename std::vector<T>::iterator b = data.begin();
        return queue_iterator<T>(b, data.size());
    }

    queue_iterator<T> end() {
        return queue_iterator<T>(data.end());
    }
};

template<typename T>
class queue_iterator {
    friend class randomized_queue<T>;

    using vector_iterator = typename std::vector<T>::iterator;

    std::vector<int> permutation;
    vector_iterator data_iterator;
    int position;

    bool is_end() const {
        return position >= permutation.size();
    }

    void make_permutation(size_t n) {
        permutation.resize(n);
        std::iota(permutation.begin(), permutation.end(), 0);
        std::shuffle(permutation.begin(), permutation.end(), generator);
    }

public:
    using difference_type = std::ptrdiff_t;
    using value_type = T;
    using pointer = value_type *;
    using reference = value_type &;
    using iterator_category = std::forward_iterator_tag;

    explicit queue_iterator(vector_iterator iter, size_t n = 0) : data_iterator(iter), position(0) {
        make_permutation(n);
    }

    queue_iterator &operator++() {
        position++;
        return *this;
    }

    queue_iterator operator++(int) {
        auto temp = *this;
        position++;
        return temp;
    }

    T &operator*() {
        return data_iterator[permutation[position]];
    }

    friend bool operator==(const queue_iterator &iter1, const queue_iterator &iter2) {
        return iter1.is_end() && iter2.is_end();
    }

    friend bool operator!=(const queue_iterator &iter1, const queue_iterator &iter2) {
        return !(iter1 == iter2);
    }

    explicit operator int() {
        return position;
    }
};