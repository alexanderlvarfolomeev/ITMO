#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>


using namespace std;

struct Node {
    long long key;
    int value;
    int index;
    int weight;
    Node *leftChild;
    Node *rightChild;

    explicit Node(long long _key, int _index, int _value) {
        leftChild = nullptr;
        rightChild = nullptr;
        weight = 1;
        key = _key;
        index = _index;
        value = _value;
    }
};

struct Tree {
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
        node->weight = safeGetWeight(node->leftChild) + safeGetWeight(node->leftChild) + 1;
    }

    pair<Node *, Node *> split(Node *node, int count) {
        if (check(node)) {
            return {nullptr, nullptr};
        }
        int size = safeGetWeight(node->leftChild);
        if (size >= count) {
            auto [l, r] = split(node->leftChild, count);
            node->leftChild = r;
            recalculate(node);
            return {l, node};
        } else {
            auto [l, r] = split(node->rightChild, count - size - 1);
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
            tree2->leftChild = merge(tree2->leftChild, tree1);
            recalculate(tree2);
            return tree2;
        }
    }

    void insert(long long key, int index, int value, bool b) {
        if (check(root)) {
            root = new Node(key, index, value);
        } else {
            if (b) {
                auto a = get(index);
                if (a->value == 0) {
                    a->value = value;
                    return;
                }
            }
            auto s = split(root, index);
            root = merge(merge(s.first, new Node(key, index, value)), s.second);
        }
    }
    void remove(int index) {
        if (check(root)) {
            return;
        }
        auto s = split(root, index);
        root = merge(s.first, split(s.second, 1).second);
    }

    vector<pair<int, int>> getArray() {
        return getArray(root);
    }

    vector<pair<int, int>> getArray(Node *node) {
        if (check(node)) {
            return {};
        }
        auto a = getArray(node->leftChild);
        auto b = getArray(node->rightChild);
        vector<pair<int, int>> c;
        c.reserve(a.size() + b.size() + 1);
        c.insert(c.end(), a.begin(), a.end());
        c.emplace_back(node->value, node->index);
        c.insert(c.end(), b.begin(), b.end());
        return c;
    }
    Node *get(int index, Node *node) {
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
};

int main() {
    int x,j,n,m;
    string s;
    Tree tree;
    random_device rd;
    mt19937_64 gen(rd());
    cin >> n >> m;
    uniform_int_distribution<> dis(1000*m);
    for (int i = 0; i < n; i++) {
        tree.insert(dis(gen), i+1, 0, false);
    }
    for (int i = 0; i < n; i++) {
        cin >> x;
        tree.insert(dis(gen), x-1, i+1, true);
    }
    int ind = -1;
    vector<int> res;
    for (auto p : tree.getArray()) {
        if (ind < p.second) {
            for (int i = ind; i < p.second -1; i++) {
                res.push_back(0);
            }
            res.push_back(p.first);
            ind = p.second;
        } else {
            res.push_back(p.first);
            ind++;
        }
    }
    cout << res.size() << '\n';
    for (int i : res) {
        cout << i << ' ';
    }
    return 0;
}