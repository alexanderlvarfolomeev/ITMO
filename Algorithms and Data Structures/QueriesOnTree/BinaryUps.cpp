#include <iostream>
#include <vector>

using namespace std;

int main() {
    int n, k;
    vector<vector<int>> p;
    cin >> n;
    for (int i = 0; i < n; ++i) {
        cin >> k;
        p.emplace_back();
        if (k != 0) p[i].push_back(k - 1);
    }
    for (int j = 1; j < 17; ++j) {
        for (int i = 0; i < n; ++i) {
            if ((p[i].size() >= j) && (p[p[i][j - 1]].size() >= j)) {
                p[i].push_back(p[p[i][j - 1]][j - 1]);
            }
        }
    }
    for (int i = 0; i < n; ++i) {
        cout << i + 1 << ':';
        for (int j : p[i]) {
            cout << ' ' << j + 1;
        }
        cout << '\n';
    }
    return 0;
}