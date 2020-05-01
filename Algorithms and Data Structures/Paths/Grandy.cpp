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

vector<list<int>> to, out;
vector<int> e;
vector<int> stack;
vector<int> g;

void grandy(int v) {
    vector<int> t;
    for (int i : out[v]) {
        t.push_back(g[i]);
    }
    sort(t.begin(), t.end());
    t.erase(unique(t.begin(), t.end()), t.end());
    int j = 0;
    for ( ; j < t.size() && j == t[j]; ++j) {}
    g[v] = j;
}

int main() {
    int n, m;
    cin >> n >> m;
    to.resize(n);
    out.resize(n);
    g.resize(n);
    for (int i = 0; i < m; ++i) {
        int x, y;
        cin >> x >> y;
        x--;
        y--;
        out[x].push_back(y);
        to[y].push_back(x);
    }
    e.resize(n);
    for (int i = 0; i < n; ++i) {
        e[i] = out[i].size();
        if (out[i].empty()) {
            stack.push_back(i);
            g[i] = 0;
        }
    }
    while (!stack.empty()) {
        int x = stack.back();
        stack.pop_back();
        grandy(x);
        for (int i : to[x]) {
            e[i]--;
            if (e[i] == 0) {
                stack.push_back(i);
            }
        }
    }
    for (int i = 0; i < n; ++i) {
        cout << g[i] << '\n';
    }
    return 0;
}