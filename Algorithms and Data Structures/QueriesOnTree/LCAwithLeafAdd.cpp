#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>

using namespace std;

int main() {
    int n, k, m, x, y;
    char c;
    vector<vector<int>> p;
    vector<int> depth, opek;
    cin >> m;
    n = 0;
    opek.push_back(0);
    p.emplace_back();
    depth.push_back(0);
    for (int i = 0; i < m; ++i) {
        cin >> c >> x;
        switch (c) {
            case '+':
                x--;
                n++;
                p.emplace_back();
                p[n].push_back(x);
                for (int j = 1; j < ceil(log2(n)) + 1; ++j) {
                    if ((p[n].size() >= j) && (p[p[n][j - 1]].size() >= j)) {
                        p[n].push_back(p[p[n][j - 1]][j - 1]);
                    }
                }
                depth.push_back(depth[x] + 1);
                opek.push_back(n);
                break;
            case '-':
                x--;
                opek[x] = opek[p[x][0]];
                break;
            case '?':
                cin >> y;
                x--;
                y--;
                if (depth[x] < depth[y]) {
                    swap(x, y);
                }
                k = depth[x] - depth[y];
                for (int j = static_cast<int>(ceil(log2(n))); j >= 0; --j) {
                    if (pow(2, j) <= k) {
                        k -= static_cast<int>(pow(2, j));
                        x = p[x][j];
                    }
                }
                if (x == y) {
                    k = x;
                    while (k != opek[k]) {
                        int t = opek[k];
                        opek[k] = opek[opek[k]];
                        k = t;
                    }
                    cout << x + 1 << '\n';
                } else {
                    for (int j = static_cast<int>(ceil(log2(n))); j >= 0; --j) {
                        if ((p[x].size() > j) && (p[y].size() > j) && (p[x][j] != p[y][j])) {
                            x = p[x][j];
                            y = p[y][j];
                        }
                    }
                    k = p[x][0];
                    while (k != opek[k]) {
                        int t = opek[k];
                        opek[k] = opek[opek[k]];
                        k = t;
                    }
                    cout << k + 1 << '\n';
                }
                break;
        }
    }
    return 0;
}