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

vector<pair<pair<int, int>, int>> edges;
vector<vector<int>> a;

int main() {
    int n, m, k, s;
    cin >> n >> m >> k >> s;
    s--;
    a.resize(k + 1, vector<int>(n, 2000000000));
    for (int i = 0; i < m; ++i) {
        int x, y, w;;
        cin >> x >> y >> w;
        x--;
        y--;
        edges.push_back({{x, y}, w});
    }
    a[0][s] = 0;
    for (int i1 = 1; i1 <= k; ++i1) {
        for (auto e : edges) {
            a[i1][e.first.second] = min(a[i1][e.first.second], a[i1 - 1][e.first.first] + e.second);
        }
    }
    for (int i = 0; i < n; ++i) {
        cout << (a[k][i] > 1000000000 ? -1 : a[k][i]) << '\n';
    }
    return 0;
}