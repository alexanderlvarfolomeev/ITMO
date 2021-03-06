#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>


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

    ~Node() {
        delete leftChild;
        delete rightChild;
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

struct Tree {
    Node *root;

    Tree() {
        root = nullptr;
    }

    ~Tree() {
        delete root;
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

int main() {
    int x;
    string s;
    Tree tree;
    while (cin >> s) {
        cin >> x;
        if (s == "insert") {
            tree.insert(x);
        } else if (s == "delete") {
            tree.remove(x);
        } else if (s == "exists") {
            cout << (tree.contains(x) ? "true" : "false") << '\n';
        } else if (s == "next") {
            int k = tree.next(x);
            if (k == x) {
                cout << "none" << '\n';
            } else {
                cout << k << '\n';
            }
        } else {
            int k = tree.previous(x);
            if (k == x) {
                cout << "none" << '\n';
            } else {
                cout << k << '\n';
            }
        }
    }

    return 0;
}