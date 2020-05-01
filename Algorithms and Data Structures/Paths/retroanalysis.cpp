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

vector<list<int>> to, out;
vector<int> e;
vector<int> stack, mk;
vector<bool> mark;

void dfs(int v) {
    mark[v] = true;
    for (int i : to[v]) {
        if (mark[i]) {
            continue;
        }
        if (mk[v] == -1) {
            mk[i] = 1;
            dfs(i);
        } else {
            e[i]--;
            if (e[i] == 0) {
                mk[i] = -1;
                dfs(i);
            }
        }
    }
}

int main() {
    int n, m, s;
    while (cin >> n >> m) {
        out.clear();
        to.clear();
        mark.clear();
        mk.clear();
        e.clear();
        e.resize(n);
        to.resize(n);
        out.resize(n);
        mark.resize(n);
        mk.resize(n);
        for (int i = 0; i < m; ++i) {
            int x, y;
            cin >> x >> y;
            x--;
            y--;
            out[x].push_back(y);
            to[y].push_back(x);
        }
        for (int i = 0; i < n; ++i) {
            e[i] = out[i].size();
            if (out[i].empty()) {
                stack.push_back(i);
            }
        }
        for (int i : stack) {
            mk[i] = -1;
            dfs(i);
        }
        stack.clear();
        for (int i = 0; i < n; ++i) {
            if (mk[i] == 0) {
                cout << "DRAW\n";
                continue;
            }
            if (mk[i] == 1) {
                cout << "FIRST\n";
                continue;
            } else {
                cout << "SECOND\n";
                continue;
            }
        }
        cout << '\n';
    }
    return 0;
}