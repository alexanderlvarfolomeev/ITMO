#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <array>
#include <utility>
#include <stack>
 
using namespace std;
 
int n, k, m;
 
int main() {
    cin >> n;
    int a[n + 1];
    int b[n + 1][n + 1];
    int bl;
    for (int i = 1; i <= n; ++i) {
        cin >> a[i];
    }
 
    b[0][0] = 0;
    for (int i = 1; i <= n; ++i) {
        b[0][i] = 5000000;
    }
    for (int i = 1; i <= n; ++i) {
        b[i][0] = 5000000;
        for (int j = 0; j <= n; ++j) {
            bl = a[i] > 100 ? 1 : 0;
            b[i][j + bl] = b[i - 1][j] + a[i];
            if (j < i - 1) b[i][j] = min(b[i][j], b[i - 1][j + 1]);
        }
    }
    int mn = 5000000;
    for (int i = 0; i <= n; ++i) {
        mn = min(mn, b[n][i]);
    }
    int mx = n;
    while (mx >= 0 && b[n][mx] != mn) {
        mx--;
    }
    int res = mx;
    k = n;
    stack<int> s;
    while (k != 0) {
        bl = a[k] > 100 ? 1 : 0;
        if (b[k][mx] == (b[k - 1][mx - bl] + a[k])) {
            k--;
            mx -= bl;
        } else {
            k--;
            mx++;
            s.push(k + 1);
        }
    }
    cout << b[n][res] << '\n' << res << ' ' << s.size() << '\n';
    while (!s.empty()) {
        cout << s.top() << '\n';
        s.pop();
    }
    return 0;
}
