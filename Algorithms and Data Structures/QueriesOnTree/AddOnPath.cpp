#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>

using namespace std;

class element {
public:
    long long value;
    pair<long long, bool> d;

    element() {
        value = 0;
        d = make_pair(0, false);
    }

    long long true_value() {
        long long result = value;
        if (d.second) {
            result += d.first;
        }
        return result;
    }
};

class SegTree {
    int nn;
    element *tree;

    void push(long long v) {
        if (v >= nn - 1) {
            tree[v].value = tree[v].true_value();
            tree[v].d.second = false;
            tree[v].d.first = 0;
            return;
        }
        long long left = 2 * v + 1;
        long long right = left + 1;
        if (tree[v].d.second) {
            tree[left].d.second = true;
            tree[right].d.second = true;
            tree[left].d.first += tree[v].d.first;
            tree[right].d.first += tree[v].d.first;
            tree[v].value = tree[v].true_value();
            tree[v].d.second = false;
            tree[v].d.first = 0;
        }
    }

    void add(long long i, long long j, long long x, long long v, long long l, long long r) {
        if (j <= l || i >= r) return;
        if (j >= r && i <= l) {
            tree[v].d.second = true;
            tree[v].d.first += x;
            return;
        }
        push(v);
        long long m = (l + r) / 2;
        add(i, j, x, 2 * v + 1, l, m);
        add(i, j, x, 2 * v + 2, m, r);
        tree[v].value = min(tree[2 * v + 1].true_value(), tree[2 * v + 2].true_value());
    }

    long long get(long long L, long long R, long long v, long long l, long long r) {
        if (R <= l || r <= L) {
            return 0;
        }
        if (L <= l && r <= R) {
            return tree[v].true_value();
        }
        push(v);
        long long m = (l + r) / 2;
        return get(L, R, 2 * v + 1, l, m) + get(L, R, 2 * v + 2, m, r);
    }
public:
    explicit SegTree(int n) {
        nn = lround(exp2(ceil(log2(n))));
        tree = new element[2*nn];
    }
    long long getTree(long long L, long long R) {
        return get(L, R, 0, 0, nn);
    }
    void addTree(long long i, long long j, long long x) {
        add(i, j, x, 0, 0, nn);
    }
};

vector<int> rs[300001];
int depth[300001], parent[300001], heavy[300001], weight[300001], number[300001], chain[300001];
vector<int> nthTree;
vector<SegTree *> trees;
int num = 0, ch = 0, n;

void dfs(int v, int par = -1) {
    parent[v] = par;
    weight[v] = 1;
    for (int i : rs[v]) {
        if (i == par) {
            continue;
        }
        depth[i] = depth[v] + 1;
        dfs(i, v);
        weight[v] += weight[i];
        if (heavy[v] == 0 || (weight[heavy[v]] < weight[i])) {
            heavy[v] = i;
        }
    }
}

void hld(int v, int par = -1) {
    chain[v] = ch;
    number[v] = num;
    num++;
    if (nthTree.size() == ch) {
        nthTree.push_back(v);
    }
    if (heavy[v] != 0) {
        hld(heavy[v], v);
    }
    if (trees.size() == ch) {
        trees.push_back(new SegTree(num));
    }
    num = 0;
    for (int i : rs[v]) {
        if (i != par && i != heavy[v]) {
            ch++;
            hld(i, v);
        }
    }
}

void turn(int x, int y, long long d) {
    while (chain[x] != chain[y]) {
        if (depth[nthTree[chain[x]]] < depth[nthTree[chain[y]]]) {
            swap(x, y);
        }
        int first = nthTree[chain[x]];
        trees[chain[first]]->addTree(number[first], number[x] + 1, d);
        x = parent[first];
    }
    if (depth[x] > depth[y]) {
        swap(x, y);
    }
    trees[chain[x]]->addTree(number[x], number[y] + 1, d);
}

int main() {
    int m, x, y;
    long long k;
    char c;
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);
    cin >> n;
    for (int i = 1; i < n; ++i) {
        cin >> x >> y;
        x--;
        y--;
        rs[x].push_back(y);
        rs[y].push_back(x);
    }
    n = lround(exp2(ceil(log2(n))));
    depth[0] = 0;
    dfs(0);
    hld(0);
    cin >> m;
    for (int i = 0; i < m; ++i) {
        cin >> c;
        if (c == '+') {
            cin >> x >> y >> k;
            x--;
            y--;
            turn(x, y, k);
        } else {
            cin >> x;
            x--;
            cout << trees[chain[x]]->getTree(number[x], number[x] + 1) << '\n';
        }
    }
    return 0;
}