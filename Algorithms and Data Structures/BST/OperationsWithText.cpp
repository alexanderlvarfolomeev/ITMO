#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>
#include <unordered_set>


using namespace std;

random_device rd;
ranlux48_base gen(rd());
uniform_int_distribution<> dis(0, INT32_MAX);

struct Tree {
    struct Node {
        long long key;
        char value;
        int count;
        int weight;
        bool cs[26];
        Node *leftChild;
        Node *rightChild;

        explicit Node(long long _key, char _value, int _count) {
            leftChild = nullptr;
            rightChild = nullptr;
            weight = _count;
            key = _key;
            value = _value;
            count = _count;
            fill(cs, cs + 26, false);
            cs[_value - 'a'] = true;
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
        node->weight = safeGetWeight(node->leftChild) + safeGetWeight(node->rightChild) + node->count;
        fill(node->cs, node->cs + 26, false);
        node->cs[node->value - 'a'] = true;
        if (node->leftChild) {
            for (int i = 0; i < 26; ++i) {
                node->cs[i] |= node->leftChild->cs[i];
            }
        }
        if (node->rightChild) {
            for (int i = 0; i < 26; ++i) {
                node->cs[i] |= node->rightChild->cs[i];
            }
        }
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
            if (count < size + node->count) {
                Node *l = new Node(dis(gen), node->value, count - size);
                Node *r = new Node(dis(gen), node->value, size + node->count - count);
                l->leftChild = node->leftChild;
                r->rightChild = node->rightChild;
                recalculate(l);
                recalculate(r);
                return {l, r};
            }
            auto[l, r] = split(node->rightChild, count - size - node->count);
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

    void insert(long long key, int index, char value, int count) {
        if (check(root)) {
            root = new Node(key, value, count);
        } else {
            auto s = split(root, index);
            root = merge(merge(s.first, new Node(key, value, count)), s.second);
        }
    }

    void remove(int index1, int index2) {
        if (check(root)) {
            return;
        }
        auto s = split(root, index2);
        auto t = split(s.first, index1);
        root = merge(t.first, s.second);
        recalculate(root);
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

    void query(int l, int r, bool *cs) {
        if (!root) {
            return;
        }
        query(l, r, cs, root);
    }

    void query(int l, int r, bool *cs, Node *node) {
        if (!node) {
            return;
        }
        int size = safeGetWeight(node->leftChild);
        int size2 = safeGetWeight(node->rightChild);
        if (l <= 0 && r >= node->weight) {
            for (int i = 0; i < 26; ++i) {
                cs[i] |= node->cs[i];
            }
            return;
        }
        if (l > node->weight || r < 0) {
            return;
        }
        query(l, r, cs, node->leftChild);
        query(l - size - node->count, r - size - node->count, cs, node->rightChild);
        if ((l < size + node->count && l >= size) ||
            (r < size + node->count && r >= size) ||
            (l < size && r >= size + node->count)) {
            cs[node->value - 'a'] = true;
        }
    }
};

int main() {
    int x, y, n, m;
    string s;
    char c, z;
    Tree tree;
    bool set1[26];
    int count;
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> c;
        switch (c) {
            case '+':
                cin >> x >> y >> z;
                tree.insert(dis(gen), x - 1, z, y);
                break;
            case '-':
                cin >> x >> y;
                tree.remove(x - 1, x + y - 1);
                break;
            case '?':
                cin >> x >> y;
                fill(set1, set1 + 26, false);
                tree.query(x - 1, y - 1, set1);
                count = 0;
                for (bool j : set1) {
                    if (j) {
                        count++;
                    }
                }
                cout << count << '\n';
                break;
            default:
                break;
        }
    }
    return 0;
}