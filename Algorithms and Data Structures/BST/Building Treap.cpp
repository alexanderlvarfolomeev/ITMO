#include <utility>

#include <utility>

#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>
#include <set>


using namespace std;

struct triple {
    int first;
    int second;
    int third;
};

struct Tree {
    struct Node {
        long long key;
        int keyx;
        int value;
        Node *parent;
        Node *leftChild;
        Node *rightChild;

        explicit Node(long long _key, int _keyx, int _value) {
            leftChild = nullptr;
            rightChild = nullptr;
            parent = nullptr;
            key = _key;
            keyx = _keyx;
            value = _value;
        }

        static int comp(Node * node1, Node * node2) {
            return node2->keyx > node1->keyx;
        }
        static int comp2(Node * node1, Node * node2) {
            return node2->value > node1->value;
        }
    };

    Node *root;

    Tree() {
        root = nullptr;
    }

    static bool check(Node *node) {
        return !node;
    }

    pair<Node *, Node *> split(Node *node, int count) {
        if (check(node)) {
            return {nullptr, nullptr};
        }
        if (node->keyx >= count) {
            auto l = split(node->leftChild, count);
            node->leftChild = l.second;
            return {l.first, node};
        } else {
            auto l = split(node->rightChild, count);
            node->rightChild = l.first;
            return {node, l.second};
        }
    }

    Node *merge(Node *tree1, Node *tree2) {
        if (check(tree1)) {
            return tree2;
        }
        if (check(tree2)) {
            return tree1;
        }
        if (tree1->key < tree2->key) {
            tree1->rightChild = merge(tree1->rightChild, tree2);
            return tree1;
        } else {
            tree2->leftChild = merge(tree1, tree2->leftChild);
            return tree2;
        }
    }

    void insert(long long key, int keyx, int value) {
        if (check(root)) {
            root = new Node(key, keyx, value);
        } else {
            auto s = split(root, keyx);
            root = merge(merge(s.first, new Node(key, keyx, value)), s.second);
        }
    }

    void remove(int value) {
        if (check(root)) {
            return;
        }
        auto s = split(root, value);
        root = merge(s.first, split(s.second, 1).second);
    }

    vector<pair<Node *, int>> getArray(vector<pair<Node *, int>> *array) {
        return getArray(root, 0, array);
    }

    vector<pair<Node *, int>> getArray(Node *node, int val, vector<pair<Node *, int>> *array) {
        if (check(node)) {
            return *array;
        }
        auto a = getArray(node->leftChild, node->value, array);
        auto b = getArray(node->rightChild, node->value, array);
        array->emplace_back(node, val);
        return *array;
    }
};

int main() {
    int x, y, n, m;
    string s;
    Tree tree;
    random_device rd;
    ranlux48_base gen(rd());
    cin >> n;
    vector<Tree::Node *> res;
    uniform_int_distribution<> dis(0, INT32_MAX);
    for (int i = 0; i < n; i++) {
        cin >> x >> y;
        res.emplace_back(new Tree::Node(y, x, i + 1));
    }
    sort(res.begin(), res.end(), Tree::Node::comp);
    Tree::Node *t = res[0];
    for (int i = 1; i < res.size(); ++i) {
        if (t->key < res[i]->key) {
            t->rightChild = res[i];
            res[i]->parent = t;
        } else {
            while (t->parent && t->key > res[i]->key) {
                t = t->parent;
            }
            if (t->key < res[i]->key) {
                res[i]->leftChild = t->rightChild;
                res[i]->leftChild->parent = res[i];
                t->rightChild = res[i];
                res[i]->parent = t;
            } else {
                res[i]->leftChild = t;
                t->parent = res[i];
            }
        }
        t = res[i];
    }
    sort(res.begin(), res.end(), Tree::Node::comp2);
    cout << "YES\n";
    for (auto i : res) {
        cout << (i->parent ? i->parent->value : 0);
        cout << ' ' << (i->leftChild ? i->leftChild->value : 0);
        cout << ' ' << (i->rightChild ? i->rightChild->value : 0) << '\n';
    }
    return 0;
}