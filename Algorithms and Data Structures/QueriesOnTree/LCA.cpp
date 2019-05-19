#include <iostream>
#include <vector>
#include <cmath>

using namespace std;

int main() {
    int n, k, m, x, y;
    vector<vector<int>> p;
    vector<int> depth;
    cin >> n;
    p.emplace_back();
    depth.push_back(0);
    for (int i = 1; i < n; ++i) {
        cin >> k;
        p.emplace_back();
        p[i].push_back(k - 1);
        depth.push_back(depth[k - 1] + 1);
    }
    for (int j = 1; j < ceil(log2(n)); ++j) {
        for (int i = 0; i < n; ++i) {
            if ((p[i].size() >= j) && (p[p[i][j - 1]].size() >= j)) {
                p[i].push_back(p[p[i][j - 1]][j - 1]);
            }
        }
    }
    cin >> m;
    for (int i = 0; i < m; ++i) {
        cin >> x >> y;
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
            cout << x + 1 << '\n';
        } else {
            for (int j = static_cast<int>(ceil(log2(n))); j >= 0; --j) {
                if ((p[x].size() > j) && (p[y].size() > j) && (p[x][j] != p[y][j])) {
                    x = p[x][j];
                    y = p[y][j];
                }
            }
            cout << p[x][0] + 1 << '\n';
        }
    }
    return 0;
}