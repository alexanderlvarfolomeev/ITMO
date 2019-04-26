#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>
#include <set>


using namespace std;

struct Tree {
    struct Node {
        long long key;
        int value;
        int weight;
        Node *leftChild;
        Node *rightChild;

        explicit Node(long long _key, int _value) {
            leftChild = nullptr;
            rightChild = nullptr;
            weight = 1;
            key = _key;
            value = _value;
        }
    };

    Node *root;

    Tree() {
        root = nullptr;
    }

    static bool check(Node *node) {
        return !node;
    }

    int safeGetWeight(Node *node) {
        return node ? node->weight : 0;
    }

    void recalculate(Node *node) {
        node->weight = safeGetWeight(node->leftChild) + safeGetWeight(node->rightChild) + 1;
    }

    pair<Node *, Node *> split(Node *node, int count) {
        if (check(node)) {
            return {nullptr, nullptr};
        }
        int size = safeGetWeight(node->leftChild);
        if (size >= count) {
            auto[l, r] = split(node->leftChild, count);
            node->leftChild = r;
            recalculate(node);
            return {l, node};
        } else {
            auto[l, r] = split(node->rightChild, count - size - 1);
            node->rightChild = l;
            recalculate(node);
            return {node, r};
        }
    }

    Node *merge(Node *tree1, Node *tree2) {
        if (check(tree1)) {
            return tree2;
        }
        if (check(tree2)) {
            return tree1;
        }
        if (tree1->key > tree2->key) {
            tree1->rightChild = merge(tree1->rightChild, tree2);
            recalculate(tree1);
            return tree1;
        } else {
            tree2->leftChild = merge(tree1, tree2->leftChild);
            recalculate(tree2);
            return tree2;
        }
    }

    void insert(long long key, int index, int value) {
        if (check(root)) {
            root = new Node(key, value);
        } else {
            auto s = split(root, index);
            root = merge(merge(s.first, new Node(key, value)), s.second);
        }
    }

    void remove(int index) {
        if (check(root)) {
            return;
        }
        auto s = split(root, index);
        root = merge(s.first, split(s.second, 1).second);
        recalculate(root);
    }

    vector<int> getArray() {
        return getArray(root);
    }

    vector<int> getArray(Node *node) {
        if (check(node)) {
            return {};
        }
        auto a = getArray(node->leftChild);
        auto b = getArray(node->rightChild);
        vector<int> c;
        c.reserve(a.size() + b.size() + 1);
        c.insert(c.end(), a.begin(), a.end());
        c.emplace_back(node->value);
        c.insert(c.end(), b.begin(), b.end());
        return c;
    }

    Node *get(int index, Node *node) {
        if (check(node)) {
            return nullptr;
        }
        int size = safeGetWeight(node->leftChild);
        if (size == index) {
            return node;
        }
        if (size > index) {
            return get(index, node->leftChild);
        } else {
            return get(index - 1 - size, node->rightChild);
        }
    }

    Node *get(int index) {
        return get(index, root);
    }

    void push_front(int first, int last) {
        auto s = split(root, last + 1);
        auto t = split(s.first, first);
        root = merge(merge(t.second, t.first), s.second);
    }
};

int main() {
    int x, y, n, m;
    string s;
    Tree tree;
    random_device rd;
    ranlux48_base gen(rd());
    cin >> n >> m;
    uniform_int_distribution<> dis(0, INT32_MAX);
    for (int i = 0; i < n; i++) {
        tree.insert(dis(gen), i + 1, i + 1);
    }
    for (int i = 0; i < m; i++) {
        cin >> x >> y;
        tree.push_front(x - 1, y - 1);
    }
    for (int i : tree.getArray()) {
        cout << i << ' ';
    }
    return 0;
}