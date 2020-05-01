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
vector<int> stack;
vector<bool> mark, mk;

int main() {
    int n, m, s;
    ifstream fin("game.in");
    ofstream fout("game.out");
    fin >> n >> m >> s;
    s--;
    to.resize(n);
    out.resize(n);
    mark.resize(n);
    mk.resize(n);
    for (int i = 0; i < m; ++i) {
        int x, y;
        fin >> x >> y;
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
        }
    }
    while (!stack.empty()) {
        int x = stack.back();
        stack.pop_back();
        mk[x] = mark[x];
        for (int i : to[x]) {
            mark[i] = mark[i] || !mk[x];
            e[i]--;
            if (e[i] == 0) {
                stack.push_back(i);
            }
        }
    }
    if (mk[s]) {
        fout << "First player wins";
    } else {
        fout << "Second player wins";
    }
    return 0;
}