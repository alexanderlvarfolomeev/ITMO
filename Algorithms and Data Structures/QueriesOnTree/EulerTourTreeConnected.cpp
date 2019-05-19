#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <cmath>

using namespace std;

struct Node {
    int weight;
    int value;
    Node *leftChild;
    Node *rightChild;
    Node *parent;

    explicit Node(int v = -1) {
        leftChild = nullptr;
        rightChild = nullptr;
        parent = nullptr;
        weight = 1;
        value = v;
    }
};

int getWeight(Node *node) {
    return node ? node->weight : 0;
}

void recalculate(Node *node) {
    if (!node) {
        return;
    }
    node->weight = getWeight(node->leftChild) + getWeight(node->rightChild) + 1;
}

void rotateRight(Node *node) {
    Node *t = node->rightChild;
    node->rightChild = node->parent;
    node->rightChild->leftChild = t;
    if (t) {
        t->parent = node->rightChild;
    }
    node->parent = node->parent->parent;
    if (node->parent) {
        if (node->parent->leftChild == node->rightChild) {
            node->parent->leftChild = node;
        } else {
            node->parent->rightChild = node;
        }
    }
    node->rightChild->parent = node;
    recalculate(node->rightChild);
    recalculate(node);
}

void rotateLeft(Node *node) {
    Node *t = node->leftChild;
    node->leftChild = node->parent;
    node->leftChild->rightChild = t;
    if (t) {
        t->parent = node->leftChild;
    }
    node->parent = node->parent->parent;
    if (node->parent) {
        if (node->parent->leftChild == node->leftChild) {
            node->parent->leftChild = node;
        } else {
            node->parent->rightChild = node;
        }
    }
    node->leftChild->parent = node;
    recalculate(node->leftChild);
    recalculate(node);
}

Node *splay(Node *node) {
    while (node->parent) {
        if (node->parent->parent == nullptr) {
            if (node == node->parent->leftChild) {
                rotateRight(node);
            } else {
                rotateLeft(node);
            }
        } else if (node == node->parent->leftChild && node->parent == node->parent->parent->leftChild) {
            rotateRight(node->parent);
            rotateRight(node);
        } else if (node == node->parent->rightChild && node->parent == node->parent->parent->rightChild) {
            rotateLeft(node->parent);
            rotateLeft(node);
        } else if (node == node->parent->rightChild && node->parent == node->parent->parent->leftChild) {
            rotateLeft(node);
            rotateRight(node);
        } else if (node == node->parent->leftChild && node->parent == node->parent->parent->rightChild) {
            rotateRight(node);
            rotateLeft(node);
        }
    }
    recalculate(node);
    return node;
}

Node *get(Node *node, int index) {
    auto leftWeight = getWeight(node->leftChild);
    if (leftWeight == index) {
        splay(node);
        return node;
    }
    if (leftWeight < index) {
        return get(node->rightChild, index - 1 - leftWeight);
    } else {
        return get(node->leftChild, index);
    }
}

Node *merge(Node *node1, Node *node2) {
    if (node1 == nullptr) {
        return node2;
    }
    if (node2 == nullptr) {
        return node1;
    }
    node1 = get(node1, node1->weight - 1);
    node1->rightChild = node2;
    node2->parent = node1;
    recalculate(node1);
    return node1;
}

pair<Node *, Node *> split(Node *node) {
    splay(node);
    Node *first = node->leftChild;
    if (first) {
        first->parent = nullptr;
    }
    node->leftChild = nullptr;
    recalculate(node);
    return {first, node};

}

pair<Node *, Node *> split(Node *node, int index) {
    if (index >= getWeight(node)) {
        return {node, nullptr};
    }
    if (index < 0) {
        return {nullptr, node};
    }
    Node *res = get(node, index);
    return split(res);
}

Node *push_front(int index, Node *node) {
    auto s = split(node, index);
    return merge(s.second, s.first);
}
Node *push_front(Node *node) {
    auto s = split(node);
    return merge(s.second, s.first);
}

Node *root(Node *node) {
    while (node->parent) {
        node = node->parent;
    }
    return node;
}

class EulerTourTree {
    Node *data;
    map<pair<int, int>, pair<Node *, Node *>> rs;
public:
    explicit EulerTourTree(int n) {
        data = new Node[n];
        for (int i = 0; i < n; ++i) {
            data[i].value = i;
        }
    }

    void link(int u, int v) {
        Node *f = new Node();
        Node *s = new Node();
        rs.insert({{u, v},
                   {f, s}});
        push_front(&data[u]);
        push_front(&data[v]);
        splay(&data[u]);
        splay(&data[v]);
        pair<Node *, Node *> sp = split(&data[u]);
        merge(merge(sp.first, merge(f, merge(&data[v], s))), sp.second);
    }

    void cut(int u, int v) {
        pair<Node *, Node *> r;
        if (rs.find({u, v}) == rs.end()) {
            r = rs[{v, u}];
            rs.erase({v, u});
        } else {
            r = rs[{u, v}];
            rs.erase({u, v});
        }
        auto p = split(r.first);
        if (root(r.second) == p.second) {
            p.second = p.second->rightChild;
            if (p.second) {
                p.second->parent = nullptr;
            }
            auto p2 = split(r.second);
            p2.second = p2.second->rightChild;
            if (p2.second) {
                p2.second->parent = nullptr;
            }
            merge(p.first, p2.second);
        } else {
            p.second = p.second->rightChild;
            if (p.second) {
                p.second->parent = nullptr;
            }
            auto p2 = split(r.second);
            p2.second = p2.second->rightChild;
            if (p2.second) {
                p2.second->parent = nullptr;
            }
            merge(p2.first, p.second);
        }
    }

    bool connected(int u, int v) {
        if (u == v) {
            return true;
        }
        splay(&data[u]);
        splay(&data[v]);
        return data[u].parent;
    }
};

int num = 0, ch = 0, n;

int main() {
    int m, x, y;
    long long k;
    string s;
    cin >> n >> m;
    EulerTourTree tree(n);
    for (int i = 0; i < m; ++i) {
        cin >> s;
        if (s == "link") {
            cin >> x >> y;
            tree.link(x - 1, y - 1);
        } else if (s == "cut") {
            cin >> x >> y;
            tree.cut(x - 1, y - 1);
        } else {
            cin >> x >> y;
            cout << tree.connected(x - 1, y - 1) << '\n';
        }
    }
    return 0;
}