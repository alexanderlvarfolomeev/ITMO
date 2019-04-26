#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>

using namespace std;

struct Node {
    long long key;
    int weight;
    bool line;
    Node *leftChild;
    Node *rightChild;
    Node *parent;

    explicit Node(long long _key) {
        leftChild = nullptr;
        rightChild = nullptr;
        parent = nullptr;
        weight = 1;
        key = _key;
        line = true;
    }
};

static vector<Node *> nodes;

struct Tree {
    static int safeGetWeight(Node *node) {
        return node ? node->weight : 0;
    }

    static void recalculate(Node *node) {
        node->weight = safeGetWeight(node->leftChild) + safeGetWeight(node->rightChild) + 1;
    }

    static pair<Node *, Node *> split(Node *node, int count) {
        if (!node) {
            return {nullptr, nullptr};
        }
        int size = safeGetWeight(node->leftChild);
        if (size >= count) {
            auto l = split(node->leftChild, count);
            node->leftChild = l.second;
            if (l.second) {
                l.second->parent = node;
            }
            if (l.first) {
                l.first->parent = nullptr;
            }
            recalculate(node);
            return {l.first, node};
        } else {
            auto l = split(node->rightChild, count - size - 1);
            node->rightChild = l.first;
            if (l.second) {
                l.second->parent = nullptr;
            }
            if (l.first) {
                l.first->parent = node;
            }
            recalculate(node);
            return {node, l.second};
        }
    }

    static Node *merge(Node *tree1, Node *tree2) {
        if (!tree1) {
            return tree2;
        }
        if (!tree2) {
            return tree1;
        }
        if (tree1->key > tree2->key) {
            tree1->rightChild = merge(tree1->rightChild, tree2);
            if (tree1->rightChild) {
                tree1->rightChild->parent = tree1;
            }
            recalculate(tree1);
            recalculate(tree2);
            return tree1;
        } else {
            tree2->leftChild = merge(tree1, tree2->leftChild);
            if (tree2->leftChild) {
                tree2->leftChild->parent = tree2;
            }
            recalculate(tree1);
            recalculate(tree2);
            return tree2;
        }
    }

    static Node *push_front(int index, Node *node) {
        auto s = split(node, index);
        return merge(s.second, s.first);
    }

    static void reverse(Node *node) {
        if (!node) {
            return;
        }
        reverse(node->leftChild);
        reverse(node->rightChild);
        swap(node->rightChild, node->leftChild);
    }

    static int findRout(int x, int y, Node *node) {
        int res = abs(x - y);
        if (!node->line) {
            res = min(res, safeGetWeight(node) - res);
        }
        return res - 1;
    }

    static pair<int, Node *> indexRoot(Node *node) {
        Node *s = node;
        int res = 0;
        res += safeGetWeight(s->leftChild);
        while (s->parent) {
            node = s;
            s = s->parent;
            if (s->rightChild == node) {
                res += safeGetWeight(s->leftChild) + 1;
            }
        }
        return {res, s};
    }
};

int main() {
    int x, y, n, m, q;
    char s;
    random_device rd;
    ranlux48_base gen(rd());
    cin >> n >> m >> q;
    uniform_int_distribution<> dis(0, INT32_MAX);
    nodes.reserve(static_cast<unsigned int>(n));
    for (int i = 0; i < n; i++) {
        nodes.push_back(new Node(dis(gen)));
    }
    pair<int, Node *> indX, indY;
    for (int i = 0; i < m; ++i) {
        cin >> x >> y;
        x--;
        y--;
        indX = Tree::indexRoot(nodes[x]);
        indY = Tree::indexRoot(nodes[y]);
        if (indX.first > indY.first) {
            swap(indX, indY);
        }
        if (indX.second == indY.second) {
            (indX.second)->line = false;
        } else {
            if (indX.first != 0 && indY.first == 0) {
                (Tree::merge(indX.second, indY.second))->line = true;
            } else if (indX.first == 0 && indY.first != 0) {
                (Tree::merge(indY.second, indX.second))->line = true;
            } else if (indX.first == 0 && indY.first == 0) {
                Tree::reverse(indX.second);
                (Tree::merge(indX.second, indY.second))->line = true;
            } else {
                Tree::reverse(indY.second);
                (Tree::merge(indX.second, indY.second))->line = true;
            }
        }
    }
    for (int i = 0; i < q; ++i) {
        cin >> s >> x >> y;
        x--;
        y--;
        indX = Tree::indexRoot(nodes[x]);
        indY = Tree::indexRoot(nodes[y]);
        switch (s) {
            case '+':
                if (indX.second == indY.second) {
                    (indX.second)->line = false;
                } else {
                    if (indX.first != 0 && indY.first == 0) {
                        (Tree::merge(indX.second, indY.second))->line = true;
                    } else if (indX.first == 0 && indY.first == indY.second->weight - 1) {
                        (Tree::merge(indY.second, indX.second))->line = true;
                    } else if (indX.first == 0 && indY.first == 0) {
                        Tree::reverse(indX.second);
                        (Tree::merge(indX.second, indY.second))->line = true;
                    } else {
                        Tree::reverse(indY.second);
                        (Tree::merge(indX.second, indY.second))->line = true;
                    }
                }
                break;
            case '-':
                if (indX.first > indY.first) {
                    swap(indX, indY);
                }
                if (!indX.second->line) {
                    if (indX.first != 0 || indY.first != indY.second->weight - 1) {
                        (Tree::push_front(indY.first, indX.second))->line = true;
                    } else {
                        indX.second->line = true;
                    }
                } else {
                    auto r = Tree::split(indX.second, indY.first);
                    (r.first)->line = true;
                    if (r.second) {
                        (r.second)->line = true;
                    }
                }
                break;
            case '?':
                if (x == y) {
                    cout << "0\n";
                } else if (indX.second != indY.second) {
                    cout << -1 << '\n';
                } else {
                    cout << Tree::findRout(indX.first, indY.first, indX.second) << '\n';
                }
                break;
            default:
                break;
        }
    }
    return 0;
}