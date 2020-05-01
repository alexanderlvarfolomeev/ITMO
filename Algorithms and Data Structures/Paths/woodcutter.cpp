#include <iostream>
#include <fstream>
#include <vector>
#include <list>
#include <algorithm>
#include <set>
#include <unordered_set>
#include <map>
#include <cmath>

using namespace std;

vector<list<pair<int, int>>> edges;
vector<list<int>> to;
vector<int> g;

void dfs(int r, int from = -1);

void grandy_tree(int r);

int find_edge(int r, int gr);

int f_e(int r, int v);

int main() {
    int n, r;
    cin >> n >> r;
    r--;
    edges.resize(n);
    to.resize(n);
    g.resize(n);
    for (int i = 1; i < n; ++i) {
        int x, y;
        cin >> x >> y;
        x--;
        y--;
        edges[x].emplace_back(y, i);
        edges[y].emplace_back(x, i);
    }
    dfs(r);
    grandy_tree(r);
    if (g[r] == 0) {
        cout << 2;
        return 0;
    }
    int ans = find_edge(r, g[r]);
    if (ans == -1) {
        return 0;
    }
    cout << 1 << '\n' << ans;
    return 0;
}

int find_edge(int r, int gr) {
    int i = to[r].front();
    for (int v : to[r]) {
        if (((g[v] + 1) ^ gr) == 0) return f_e(r, v);
        if (g[v] > (((g[v] + 1) ^ gr) - 1)) {
            i = v;
        }
    }
    if (((g[i] + 1) ^ gr) == 0) return f_e(r, i);
    return find_edge(i, g[i] ^ (((g[i] + 1) ^ gr) - 1));
}

int f_e(int r, int v) {
    if (edges[r].size() > edges[v].size()) {
        swap(r, v);
    }
    for (auto p : edges[r]) {
        if (p.first == v) {
            return p.second;
        }
    }
    return -1;
}

void grandy_tree(int r) {
    if (to[r].empty()) {
        g[r] = 0;
    } else {
        for (int v : to[r]) {
            grandy_tree(v);
            g[r] ^= (1 + g[v]);
        }
    }
}

void dfs(int r, int from) {
    for (auto v : edges[r]) {
        if (v.first == from) {
            continue;
        }
        to[r].push_back(v.first);
        dfs(v.first, r);
    }
}