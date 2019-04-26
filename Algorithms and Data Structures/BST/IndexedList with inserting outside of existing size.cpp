#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>
#include <set>


using namespace std;

struct Node {
    int key;
    Node *leftChild;
    Node *rightChild;
    Node *parent;

    explicit Node(int _key) {
        leftChild = nullptr;
        rightChild = nullptr;
        parent = nullptr;
        key = _key;
    }

    static bool check(Node *node) {
        return node != nullptr;
    }

    Node *contains(int key) {
        if (this->key == key) {
            splay();
            return this;
        }
        if (this->key < key) {
            if (check(rightChild)) {
                return rightChild->contains(key);
            } else {
                return nullptr;
            }
        } else {
            if (check(leftChild)) {
                return leftChild->contains(key);
            } else {
                return nullptr;
            }
        }
    }

    Node *next(int key) {
        if (key >= this->key) {
            if (check(rightChild)) {
                return rightChild->next(key);
            } else {
                return nullptr;
            }
        } else {
            if (!check(leftChild)) {
                return this;
            }
            Node *t = leftChild->next(key);
            if (t == nullptr) {
                return this;
            } else {
                return t;
            }
        }
    }

    Node *previous(int key) {
        if (key <= this->key) {
            if (check(leftChild)) {
                return leftChild->previous(key);
            } else {
                return nullptr;
            }
        } else {
            if (!check(rightChild)) {
                return this;
            }
            Node *t = rightChild->previous(key);
            if (t == nullptr) {
                return this;
            } else {
                return t;
            }
        }
    }

    void rotateRight() {
        Node *t = rightChild;
        rightChild = parent;
        rightChild->leftChild = t;
        if (check(t)) {
            t->parent = rightChild;
        }
        parent = parent->parent;
        if (check(parent)) {
            if (parent->leftChild == rightChild) {
                parent->leftChild = this;
            } else {
                parent->rightChild = this;
            }
        }
        rightChild->parent = this;
    }

    void rotateLeft() {
        Node *t = leftChild;
        leftChild = parent;
        leftChild->rightChild = t;
        if (check(t)) {
            t->parent = leftChild;
        }
        parent = parent->parent;
        if (check(parent)) {
            if (parent->leftChild == leftChild) {
                parent->leftChild = this;
            } else {
                parent->rightChild = this;
            }
        }
        leftChild->parent = this;
    }

    Node *splay() {
        while (parent != nullptr) {
            if (parent->parent == nullptr) {
                if (this == parent->leftChild) {
                    rotateRight();
                } else {
                    rotateLeft();
                }
            } else if (this == parent->leftChild && parent == parent->parent->leftChild) {
                parent->rotateRight();
                rotateRight();
            } else if (this == parent->rightChild && parent == parent->parent->rightChild) {
                parent->rotateLeft();
                rotateLeft();
            } else if (this == parent->rightChild && parent == parent->parent->leftChild) {
                rotateLeft();
                rotateRight();
            } else if (this == parent->leftChild && parent == parent->parent->rightChild) {
                rotateRight();
                rotateLeft();
            }
        }
        return this;
    }

    Node *max() {
        if (check(rightChild)) {
            return rightChild->max();
        } else {
            return this;
        }
    }

    static Node *merge(Node *node1, Node *node2) {
        if (node1 == nullptr) {
            return node2;
        }
        if (node2 == nullptr) {
            return node1;
        }
        node1 = node1->max()->splay();
        node1->rightChild = node2;
        node2->parent = node1;
        return node1;
    }

    pair<Node *, Node *> split(int key) {
        Node *res = next(key - 1);
        if (res != nullptr) {
            res = res->splay();
            Node *first = res->leftChild;
            if (check(first)) {
                first->parent = nullptr;
            }
            res->leftChild = nullptr;
            if (res->key == key) {
                res = res->rightChild;
                if (check(res)) {
                    res->parent = nullptr;
                }
            }
            return make_pair(first, res);
        } else {
            return make_pair(this, nullptr);
        }
    }

    Node *insert(int key) {
        auto p = split(key);
        Node *res = new Node(key);
        res->leftChild = p.first;
        res->rightChild = p.second;
        if (check(p.first)) {
            res->leftChild->parent = res;
        }
        if (check(p.second)) {
            res->rightChild->parent = res;
        }

        return res;
    }

    Node *remove(int key) {
        Node *t = contains(key);
        if (t) {
            auto p = t->split(key);
            return merge(p.first, p.second);
        } else {
            return this;
        }
    }
};

struct SplayTree {
    Node *root;

    SplayTree() {
        root = nullptr;
    }

    void insert(int key) {
        if (root == nullptr) {
            root = new Node(key);
        } else {
            root = root->insert(key);
        }
    }

    Node *remove(int key) {
        if (root != nullptr) {
            root = root->remove(key);
        }
        return root;
    }

    bool contains(int key) {
        if (root == nullptr) {
            return false;
        } else {
            Node *t = root->contains(key);
            if (Node::check(t)) {
                root = t;
            }
            return t;
        }
    }

    int next(int key) {
        if (root == nullptr) {
            return key;
        } else {
            Node *t = root->next(key);
            return t ? t->key : key;
        }
    }

    int previous(int key) {
        if (root == nullptr) {
            return key;
        } else {
            Node *t = root->previous(key);
            return t ? t->key : key;
        }
    }
};

SplayTree tree2;

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
            return make_pair(nullptr, nullptr);
        }
        int size = safeGetWeight(node->leftChild);
        if (size >= count) {
            auto l = split(node->leftChild, count);
            node->leftChild = l.second;
            recalculate(node);
            return make_pair(l.first, node);
        } else {
            auto l = split(node->rightChild, count - size - 1);
            node->rightChild = l.first;
            recalculate(node);
            return make_pair(node, l.second);
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

    void insertZero(long long key, int index) {
        if (check(root)) {
            root = new Node(key, 0);
        } else {
            auto s = split(root, index);
            root = merge(merge(s.first, new Node(key, 0)), s.second);
        }
    }

    void insert(long long key, int index, int value) {
        if (check(root)) {
            root = new Node(key, value);
        } else {
            auto a = get(index);
            if (a->value == 0) {
                a->value = value;
                tree2.remove(index);
                return;
            }
            auto k = tree2.next(index);
            if (k != index) {
                remove(k);
                tree2.remove(k);
            }
            auto s = split(root, index);
            root = merge(merge(s.first, new Node(key, value)), s.second);
        }
    }

    void remove(Node *node, int index) {
        if (check(node)) {
            return;
        }
        auto s = split(root, index);
        root = merge(s.first, split(s.second, 1).second);
        recalculate(node);
    }

    void remove(int index) {
        remove(root, index);
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
};

int main() {
    int x, j, n, m;
    string s;
    Tree tree;
    random_device rd;
    ranlux48_base gen(rd());
    cin >> n >> m;
    uniform_int_distribution<> dis(0, INT32_MAX);
    for (int i = 0; i < 2 * m; i++) {
        tree.insertZero(dis(gen), i);
        tree2.insert(i);
    }
    for (int i = 0; i < n; i++) {
        cin >> x;
        tree.insert(dis(gen), x - 1, i + 1);
    }
    int ind = 0;
    vector<int> res = tree.getArray();
    for (int k = 0; k < res.size(); ++k) {
        if (res[k] != 0) {
            ind = k;
        }
    }
    //ind = res.size();
    cout << ind + 1 << '\n';
    for (int i = 0; i <= ind; i++) {
        cout << res[i] << ' ';
    }
    return 0;
}