#include <iostream>
#include <vector>
#include <array>
#include <cmath>
#include <fstream>
#include <algorithm>


using namespace std;

int r;

int get(int u, int v);

array<array<int, 100000>, 17> a{};
int k;

class triple {
public:
    int first;
    int second;
    long long third;

    triple(int f, int s, long long t) {
        first = f;
        second = s;
        third = t;
    }

    static long long comp(triple x, triple y) {
        return x.third < y.third;
    }
};

int main() {
    int n, m, x, u, v;
    //ifstream fin("rmq.in");
    //ofstream fout("rmq.out");
    vector<triple> result;
    cin >> n >> m >> x;
    k = static_cast<int>(ceil(log2(n)));
    a[0][0] = x;
    for (int i = 1; i < n; i++) {
        a[0][i] = (23 * a[0][i - 1] + 21563) % 16714589;
    }
    for (int i = 1; i < k; ++i) {
        for (int j = 0; j < n; ++j) {
            a[i][j] = a[i - 1][j];
            if (j + (1 << (i - 1)) < n) {
                a[i][j] = min(a[i - 1][j + (1 << (i - 1))], a[i][j]);
            }
        }
    }
    cin >> u >> v;
    u--;
    v--;
    int ans = get(min(u, v), max(u, v));
    for (int i = 1; i < m; ++i) {
        u = (17 * u + 751 + ans + 2 * i + 17) % n;
        v = (13 * v + 593 + ans + 5 * i + 13) % n;
        ans = get(min(u, v), max(u, v));
    }
    cout << u + 1 << ' ' << v + 1 << ' ' << ans;
    return 0;
}

int get(int u, int v) {
    int d = static_cast<int>(floor(log2(v - u + 1)));
    return min(a[d][u], a[d][v + 1 - (1 << d)]);
}

