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

const ll LLM = 5000000000LL;

struct edge {
    int v1;
    int v2;
    int w;
};

struct set_v {
    std::vector<bool> mark;
    int sz;

    explicit set_v(int n) {
        mark.resize(n);
        sz = n;
    }

    bool insert(int v) {
        bool f = mark[v];
        mark[v] = true;
        if (!f) {
            sz--;
        }
        return f;
    }

    bool empty() {
        return sz == 0;
    }
};

bool operator<(edge e1, edge e2) {
    if (e1.w == e2.w) {
        int e1n, e1x, e2n, e2x;
        e1n = std::min(e1.v1, e1.v2);
        e1x = std::max(e1.v1, e1.v2);;
        e2n = std::min(e2.v1, e2.v2);
        e2x = std::max(e2.v1, e2.v2);
        if (e1n == e2n) {
            return e1x < e2x;
        } else {
            return e1n < e2n;
        }
    } else {
        return e1.w < e2.w;
    }
}

vector<list<pair<int, int>>> edges;

int main() {
    int n, m;
    cin >> n >> m;
    edges.resize(n);
    for (int i = 0; i < m; ++i) {
        int x, y, z;
        cin >> x >> y >> z;
        x--;
        y--;
        edges[x].emplace_back(y, z);
        edges[y].emplace_back(x, z);
    }
    set<pair<ll, int>> q;
    vector<ll> a(n, LLM);
    set_v mk(n);
    mk.insert(0);
    a[0] = 0;
    for (auto p : edges[0]) {
        a[p.first] = p.second;
        q.emplace(p.second, p.first);
    }

    while (!mk.empty()) {
        auto mn = q.begin();
        int v = mn ->second;
        mk.insert(mn->second);
        for (auto i : edges[v]) {
            if (!mk.mark[i.first]) {
                if (a[i.first] > a[v] + i.second) {
                    q.erase({a[i.first], i.first});
                    a[i.first] = a[v] + i.second;
                    q.emplace(a[i.first], i.first);
                }
            }
        }
        q.erase(mn);
    }

    for (auto i : a) {
        cout << i << ' ';
    }
    return 0;
}