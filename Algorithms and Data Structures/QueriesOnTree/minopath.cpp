#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>

using namespace std;

int main() {
    int n, k, m, x, y, res;
    ifstream fin("minonpath.in");
    ofstream fout("minonpath.out");
    vector<vector<int>> p, pr;
    vector<int> depth;
    fin >> n;
    p.emplace_back();
    pr.emplace_back();
    depth.push_back(0);
    for (int i = 1; i < n; ++i) {
        fin >> x >> y;
        p.emplace_back();
        pr.emplace_back();
        p[i].push_back(x - 1);
        pr[i].push_back(y);
        depth.push_back(depth[x - 1] + 1);
    }
    for (int j = 1; j < ceil(log2(n)); ++j) {
        for (int i = 0; i < n; ++i) {
            if ((p[i].size() >= j) && (p[p[i][j - 1]].size() >= j)) {
                p[i].push_back(p[p[i][j - 1]][j - 1]);
                pr[i].push_back(min(pr[p[i][j - 1]][j - 1], pr[i][j - 1]));
            }
        }
    }
    fin >> m;
    for (int i = 0; i < m; ++i) {
        fin >> x >> y;
        x--;
        y--;
        if (depth[x] < depth[y]) {
            swap(x, y);
        }
        res = 2000000;
        k = depth[x] - depth[y];
        for (int j = static_cast<int>(ceil(log2(n))); j >= 0; --j) {
            if (pow(2, j) <= k) {
                k -= static_cast<int>(pow(2, j));
                res = min(res, pr[x][j]);
                x = p[x][j];
            }
        }
        if (x == y) {
            fout << res << '\n';
        } else {
            for (int j = static_cast<int>(ceil(log2(n))); j >= 0; --j) {
                if ((p[x].size() > j) && (p[y].size() > j) && (p[x][j] != p[y][j])) {
                    res = min(res, pr[x][j]);
                    res = min(res, pr[y][j]);
                    x = p[x][j];
                    y = p[y][j];
                }
            }
            res = min(res, pr[x][0]);
            res = min(res, pr[y][0]);
            fout << res << '\n';
        }
    }
    return 0;
}