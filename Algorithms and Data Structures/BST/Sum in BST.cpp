#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>


using namespace std;

struct Node {
    int key;
    long long sum;
    int lBound;
    int rBound;
    Node *leftChild;
    Node *rightChild;
    Node *parent;

    explicit Node(int _key) {
        leftChild = nullptr;
        rightChild = nullptr;
        parent = nullptr;
        key = _key;
        sum = _key;
        rBound = lBound = _key;
    }

    static long long getSum(Node *node) {
        return node ? node->sum : 0;
    }

    void recalculate() {
        sum = getSum(leftChild) + getSum(rightChild) + key;
        lBound = leftChild ? leftChild->lBound : key;
        rBound = rightChild ? rightChild->rBound : key;
    }

    Node *contains(int key) {
        if (this->key == key) {
            splay();
            return this;
        }
        if (this->key < key) {
            if (rightChild) {
                return rightChild->contains(key);
            } else {
                return nullptr;
            }
        } else {
            if (leftChild) {
                return leftChild->contains(key);
            } else {
                return nullptr;
            }
        }
    }

    Node *next(int key) {
        if (key >= this->key) {
            if (rightChild) {
                return rightChild->next(key);
            } else {
                return nullptr;
            }
        } else {
            if (!leftChild) {
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
            if (leftChild) {
                return leftChild->previous(key);
            } else {
                return nullptr;
            }
        } else {
            if (!rightChild) {
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
        if (t) {
            t->parent = rightChild;
        }
        parent = parent->parent;
        if (parent) {
            if (parent->leftChild == rightChild) {
                parent->leftChild = this;
            } else {
                parent->rightChild = this;
            }
        }
        rightChild->parent = this;
        if (rightChild) {
            rightChild->recalculate();
        }
        recalculate();
    }

    void rotateLeft() {
        Node *t = leftChild;
        leftChild = parent;
        leftChild->rightChild = t;
        if (t) {
            t->parent = leftChild;
        }
        parent = parent->parent;
        if (parent) {
            if (parent->leftChild == leftChild) {
                parent->leftChild = this;
            } else {
                parent->rightChild = this;
            }
        }
        leftChild->parent = this;
        if (leftChild) {
            leftChild->recalculate();
        }
        recalculate();
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
        if (rightChild) {
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
        node1->recalculate();
        return node1;
    }

    pair<Node *, Node *> split(int key) {
        Node *res = next(key - 1);
        if (res != nullptr) {
            res = res->splay();
            Node *first = res->leftChild;
            if (first) {
                first->parent = nullptr;
            }
            res->leftChild = nullptr;
            if (res->key == key) {
                res = res->rightChild;
                if (res) {
                    res->parent = nullptr;
                }
            }
            if (res) {
                res->recalculate();
            }
            return {first, res};
        } else {
            return {this, nullptr};
        }
    }

    Node *insert(int key) {
        auto p = split(key);
        Node *res = new Node(key);
        res->leftChild = p.first;
        res->rightChild = p.second;
        if (p.first) {
            res->leftChild->parent = res;
        }
        if (p.second) {
            res->rightChild->parent = res;
        }
        res->recalculate();
        return res;
    }

    Node *remove(int key) {
        Node *t = contains(key);
        if (t) {
            auto p = t->split(key);
            if (p.first) {
                p.first->recalculate();
            }
            if (p.second) {
                p.second->recalculate();
            }
            t = merge(p.first, p.second);
            if (t) {
                t->recalculate();
            }
            return t;
        } else {
            return this;
        }
    }

    long long find(int l, int r) {
        if (l <= lBound && r >= rBound) {
            return sum;
        }
        if (l > rBound || r < lBound) {
            return 0;
        }
        long long res = 0;
        if (l < key && leftChild) {
            res += leftChild->find(l, min(key - 1, r));
        }
        if (r > key && rightChild) {
            res += rightChild->find((key + 1 > l ? key + 1 : l), r);
        }
        if (l <= key && r >= key) {
            res += key;
        }
        return res;
    }
};

struct Tree {
    Node *root;

    Tree() {
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
            if (t) {
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

    long long find(int l, int r) {
        if (!root) {
            return 0;
        }
        return root->find(l, r);
    }
};

int main() {
    int x, y, n;
    long long k = 0;
    char c;
    bool b = false;
    string s;
    Tree tree;
    cin >> n;
    for (int i = 0; i < n; ++i) {
        cin >> c;
        switch (c) {
            case '+':
                cin >> x;
                x = static_cast<int>((x + (b ? k : 0)) % 1000000000);
                tree.insert(x);
                b = false;
                break;
            case '?':
                cin >> x >> y;
                k = tree.find(x, y);
                cout << k << '\n';
                b = true;
                break;
            default:
                break;
        }
    }

    return 0;
}