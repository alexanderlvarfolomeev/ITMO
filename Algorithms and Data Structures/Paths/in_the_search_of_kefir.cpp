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

const ll LLM = 3000000000000000LL;

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

    void clear() {
        replace(mark.begin(), mark.end(), true, false);
    }
};

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
    int a, b, c;
    cin >> a >> b >> c;
    a--;
    b--;
    c--;
    set<pair<ll, int>> q;
    vector<ll> a1(n, LLM), a2(n, LLM), a3(n, LLM);
    set_v mk(n);
    mk.insert(a);
    a1[a] = 0;
    for (auto p : edges[a]) {
        a1[p.first] = p.second;
        q.emplace(p.second, p.first);
    }

    while (!q.empty()) {
        auto mn = q.begin();
        int v = mn->second;
        mk.insert(mn->second);
        for (auto i : edges[v]) {
            if (!mk.mark[i.first]) {
                if (a1[i.first] > a1[v] + i.second) {
                    q.erase({a1[i.first], i.first});
                    a1[i.first] = a1[v] + i.second;
                    q.emplace(a1[i.first], i.first);
                }
            }
        }
        q.erase(mn);
    }
    mk.clear();
    a2[b] = 0;
    for (auto p : edges[b]) {
        a2[p.first] = p.second;
        q.emplace(p.second, p.first);
    }

    while (!q.empty()) {
        auto mn = q.begin();
        int v = mn->second;
        mk.insert(mn->second);
        for (auto i : edges[v]) {
            if (!mk.mark[i.first]) {
                if (a2[i.first] > a2[v] + i.second) {
                    q.erase({a2[i.first], i.first});
                    a2[i.first] = a2[v] + i.second;
                    q.emplace(a2[i.first], i.first);
                }
            }
        }
        q.erase(mn);
    }
    mk.clear();
    a3[c] = 0;
    for (auto p : edges[c]) {
        a3[p.first] = p.second;
        q.emplace(p.second, p.first);
    }

    while (!q.empty()) {
        auto mn = q.begin();
        int v = mn->second;
        mk.insert(mn->second);
        for (auto i : edges[v]) {
            if (!mk.mark[i.first]) {
                if (a3[i.first] > a3[v] + i.second) {
                    q.erase({a3[i.first], i.first});
                    a3[i.first] = a3[v] + i.second;
                    q.emplace(a3[i.first], i.first);
                }
            }
        }
        q.erase(mn);
    }
    for (int i = 0; i < n; ++i) {
        a1[i] = a1[i] + a2[i] + a3[i] + min(a1[i], min(a2[i], a3[i]));
    }
    sort(a1.begin(), a1.end());
    cout << (a1[0] > LLM ? -1 : a1[0]);
    return 0;
}