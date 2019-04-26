#include <iostream>
#include <vector>
#include <array>
#include <cmath>
#include <algorithm>
#include <map>
#include <set>


using namespace std;

void operation(int i, int j, int x, int v, int l, int r);

void push(int v, int l, int m);

struct triple {
public:
    int first;
    bool second;
    int third;
    triple(int x, bool y, int z) {
        first = x;
        second = y;
        third = z;
    }
};

triple op(triple x, triple y) {
    if (x.third < y.third) {
        return x;
    } else {
        return y;
    }
}

triple get(int i, int j, int v, int l, int r);

vector<triple> a;
int n, m, k;

int main() {
    int x, y, z;
    vector<pair<int, int>> b;
    cin >> n >> m;
    string s;
    k = lround(exp2(ceil(log2(n))));
    for (int i = 0; i < 2 * k - 1; ++i) {
        a.emplace_back(0, false, 1 << 25);
    }
    for (int i = k - 1; i < n + k - 1; ++i) {
        a[i] = triple(i - k + 1, false, 0);
    }
    for (int i = k - 2; i >= 0; --i) {
        a[i] = a[2 * i + 1];
    }
    for (int i = 0; i < m; ++i) {
        cin >> s;
        if (s == "defend") {
            cin >> x >> y >> z;
            operation(x - 1, y, z, 0, 0, k);
        } else {
            cin >> x >> y;
            triple t = get(x - 1, y, 0, 0, k);
            b.emplace_back(t.third, t.first);
        }
    }
    cout << '\n';
    for (auto i : b) {
        cout << i.first << ' ' << i.second + 1 << '\n';
    }
    return 0;
}

void operation(int i, int j, int x, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        if (x > a[v].third) {
            a[v].second = true;
            a[v].third = x;
        }
        return;
    }
    int m = (l + r) / 2;
    push(v, l, m);
    operation(i, j, x, 2 * v + 1, l, m);
    operation(i, j, x, 2 * v + 2, m, r);
    a[v] = a[2 * v + 1].third < a[2 * v + 2].third ? a[2 * v + 1] : a[2 * v + 2];
}

triple get(int i, int j, int v, int l, int r) {
    if (j <= l || r <= i) {
        return triple(0, false, 1 << 25);
    }
    if (i <= l && r <= j) {
        return a[v];
    }
    int m = (l + r) / 2;
    push(v, l, m);
    auto ll = get(i, j, 2 * v + 1, l, m);
    auto rr = get(i, j, 2 * v + 2, m, r);
    return op(ll, rr);
}

void push(int v, int l, int m) {
    if (v >= k - 1) {
        a[v].second = true;
        return;
    }
    if (a[v].second) {
        int left = 2 * v + 1;
        int right = left + 1;
        if (a[left].third < a[v].third) {
            a[left].third = a[v].third;
            a[left].second = true;
           // a[left].first = l;
        }
        if (a[right].third < a[v].third) {
            a[right].third = a[v].third;
            a[right].second = true;
           // a[right].first = m;
        }
    }
}