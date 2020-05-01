#include <iostream>
#include <fstream>
#include <vector>
#include <list>
#include <algorithm>
#include <set>
#include <unordered_set>
#include <map>
#include <cmath>

#define ll long long

using namespace std;

struct comp {
    bool operator()(const pair<int, ll>& e1, const pair<int, ll>& e2) const {
        return e1.first < e2.first;
    }
};

vector<set<pair<int, ll>, comp>> edges;
vector<bool> mark;
vector<bool> mark2;
vector<vector<ll>> a;

const ll LLM = 8223372036854775807LL;

void dfs(int v) {
    mark2[v] = true;
    for (auto u : edges[v]) {
        if (!mark2[u.first]) {
            dfs(u.first);
        }
    }
}

int main() {
    int n, m, s;
    cin >> n >> m >> s;
    s--;
    set<pair<int, ll>, comp> st{};
    edges.resize(n, st);
    a.resize(n + 1, vector<ll>(n, LLM));
    mark.resize(n);
    mark2.resize(n);
    for (int i = 0; i < m; ++i) {
        int x, y;
        ll w;
        cin >> x >> y >> w;
        x--;
        y--;
        if (x==y && w >= 0) continue;
        auto it = edges[x].find({y, w});
        if (it == edges[x].end() || it->second <= w) {
            edges[x].emplace(y, w);
        } else {
            edges[x].erase(it);
            edges[x].emplace(y, w);
        }
    }
    a[0][s] = 0;
    for (int i1 = 1; i1 <= n; ++i1) {
        for (int i = 0; i < n; ++i) {
            a[i1][i] = a[i1 - 1][i];
        }
        for (int i = 0; i < n; ++i) {
            if (a[i1 - 1][i] == LLM) {
                continue;
            }
            for (auto e : edges[i]) {
                a[i1][e.first] = min(a[i1][e.first], a[i1 - 1][i] + e.second);
            }
        }
    }
    for (int i = 0; i < n; ++i) {
        for (auto e : edges[i]) {
            if (!mark2[i] && (a[n][i] < LLM) && (a[n][e.first] > a[n][i] + e.second)) {
                dfs(e.first);
            }
        }
    }
    for (int i = 0; i < n; ++i) {
        if (a[n][i] == LLM) {
            cout << "*\n";
        } else if (mark2[i]) {
            cout << "-\n";
        } else {
            cout << a[n][i] << '\n';
        }
    }
    return 0;
}