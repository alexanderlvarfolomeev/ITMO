#include <iostream>
#include <vector>
#include <array>

using namespace std;

int main() {
    int x, y, a;
    int z, t, b, c1, c0;
    unsigned int n, m;
    vector<long long> sums;
    cin >> n >> x >> y >> a;
    sums.resize(n);
    sums[0] = a;
    for (int i = 1; i < n; ++i) {
        a = (x * a + y) % (1 << 16);
        if (a < 0) {
            a += 1 << 16;
        }
        sums[i] = sums[i - 1] + a;
        //cout << sums[i] << ' ';
    }
    cin >> m >> z >> t >> b;
    long long sum = 0L;
    //cout << sum << ' ';
    for (int i = 0; i < m; ++i) {
        c0 = b % n;
        b = (z * b + t) % (1 << 30);
        if (b < 0) {
            b += 1 << 30;
        }
        c1 = b % n;
        b = (z * b + t) % (1 << 30);
        if (b < 0) {
            b += 1 << 30;
        }
        if (c0 == 0 || c1 == 0) {
            sum += sums[max(c0, c1)];
        } else {
            sum += sums[max(c0, c1)] - sums[min(c0, c1) - 1];
        }
        //cout << sum << ' ';
    }
    cout << '\n';
    cout << sum;
    return 0;
}