#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>

using namespace std;

vector<int> rs[100001];
int depth[100001], parent[100001], heavy[100001], weight[100001], number[100001], chain[100001];
vector<pair<int, int>> nthPair;
pair<int, bool> segTree[1 << 18];
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
    if (nthPair.size() == ch) {
        nthPair.emplace_back(v, 0);
    }
    nthPair[ch].second++;
    if (heavy[v] != 0) {
        hld(heavy[v], v);
    }
    for (int i : rs[v]) {
        if (i != par && i != heavy[v]) {
            ch++;
            hld(i, v);
        }
    }
}

void push(int v, int l, int r) {
    if (v >= n - 1) {
        if (segTree[v].second) {
            segTree[v] = {1, false};
        }
        return;
    }
    int left = 2 * v + 1;
    int right = left + 1;
    if (segTree[v].second) {
        segTree[v] = {r - l, false};
        segTree[left].second = true;
        segTree[right].second = true;
    }
}

void setTree(int i, int j, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        segTree[v].second = true;
        return;
    }
    push(v, l, r);
    int m = (l + r) / 2;
    setTree(i, j, 2 * v + 1, l, m);
    setTree(i, j, 2 * v + 2, m, r);
    int v1, v2;
    v1 = segTree[2 * v + 1].second ? (r - l) / 2 : segTree[2 * v + 1].first;
    v2 = segTree[2 * v + 2].second ? (r - l) / 2 : segTree[2 * v + 2].first;
    segTree[v].first = v1 + v2;
}

int getTree(int L, int R, int v, int l, int r) {
    if (R <= l || r <= L) {
        return 0;
    }
    if (L <= l && r <= R) {
        return segTree[v].second ? r - l : segTree[v].first;
    }
    push(v, l, r);
    int m = (l + r) / 2;
    return getTree(L, R, 2 * v + 1, l, m) + getTree(L, R, 2 * v + 2, m, r);
}

void turn(int x, int y) {
    while (chain[x] != chain[y]) {
        if (depth[nthPair[chain[x]].first] < depth[nthPair[chain[y]].first]) {
            swap(x, y);
        }
        int first = nthPair[chain[x]].first;
        setTree(number[first], number[x] + 1, 0, 0, n);
        x = parent[first];
    }
    if (depth[x] > depth[y]) {
        swap(x, y);
    }
    setTree(number[x] + 1, number[y] + 1, 0, 0, n);
}

int main() {
    int m, x, y, k;
    cin >> n;
    k = n - 1;
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
        cin >> x >> y;
        x--;
        y--;
        turn(x, y);
    }
    cout << k - segTree[0].first;
    return 0;
}