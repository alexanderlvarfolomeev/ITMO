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

vector<vector<int>> a,f;
vector<int> p;
vector<int> mk;

int out(int b, int c);

int main() {
    int n;
    cin >> n;
    a.resize(n, vector<int>(n));
    p.resize(n, -1);
    f.resize(n + 1, vector<int>(n));
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> a[i][j];
        }
    }
    int b;
    for (int k = 1; k <= n; ++k) {
        b = -1;
        for (int i = 0; i < n; ++i) {
            f[k][i] = f[k - 1][i];
            for (int j = 0; j < n; ++j) {
                if (a[j][i] == 100000) {
                    continue;
                }
                if (f[k - 1][i] > f[k - 1][j] + a[j][i]) {
                    p[i] = j;
                    b = i;
                }
                f[k][i] = min(f[k][i], f[k - 1][j] + a[j][i]);
            }
        }
    }
    if (b == -1) {
        cout << "NO";
        return 0;
    }
    cout << "YES\n";
    mk.resize(n, -1);
    out(b, 0);
    return 0;
}

int out(int b, int c) {
    if (mk[b] != -1) {
        cout << c - mk[b] << '\n' << b + 1 << ' ';
        return b;
    }
    mk[b] = c;
    int x = out(p[b], c + 1);
    if (x == b || x == -1) {
        return -1;
    }
    cout << b + 1 << ' ';
    return x;
}