#include <iostream>
#include <vector>
#include <array>
#include <cmath>
#include <fstream>
#include <algorithm>


using namespace std;

int r;

void set(long long i, long long j, long long x, int v, int l, int r);

long long get(int i, int v, int l, int r);

long long min(int i, int j, int v, int l, int r);

void push(int v);

long long arr(int v, long long x);

array<pair<long long, bool>, 1 << 18> a{};
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
    long long n, m, x, y, z;
    ifstream fin("rmq.in");
    ofstream fout("rmq.out");
    vector<triple> result;
    fin >> n >> m;
    k = lround(exp2(ceil(log2(n))));
    for (int i = 0; i < m; i++) {
        fin >> x >> y >> z;
        result.emplace_back(x - 1, y, z);
    }
    sort(result.begin(), result.end(), triple::comp);
    for (int i = 0; i < m; i++) {
        set(result[i].first, result[i].second, result[i].third, 0, 0, k);
    }
    for (int i = 0; i < n; i++) {
        a[k - 1 + i].first = get(i, 0, 0, k);
        a[k - 1 + i].second = true;
        //cout << a[k - 1 + i].first << ' ';
    }
    for (int i = k - 2; i >= 0; --i) {
        a[i].first = min(a[2 * i + 1].first, a[2 * i + 2].first);
    }
    bool b = true;
    for (int i = 0; i < m; i++) {
        b &= (min(result[i].first, result[i].second, 0, 0, k) == result[i].third);
    }

    if (b) {
        fout << "consistent\n";
        for (int i = 0; i < n; ++i) {
            fout << a[i + k - 1].first << ' ';
        }
    } else {
        fout << "inconsistent\n";
    }
    return 0;
}

long long arr(int v, long long x) {
    if (x < a[v].first) {
        return -((long long) 1 << 32) - 1;
    } else {
        if (v == 0) {
            return a[v].first;
        }
        return arr(((v - 1) / 2), a[v].first);
    }
}

void set(long long i, long long j, long long x, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        push(v);
        a[v].first = x;
        a[v].second = true;
        return;
    }
    push(v);
    int m = (l + r) / 2;
    set(i, j, x, 2 * v + 1, l, m);
    set(i, j, x, 2 * v + 2, m, r);
}

long long get(int i, int v, int l, int r) {
    if (a[v].second) {
        return a[v].first;
    }
    if (r - l == 1) {
        return 0;
    }
    int m = (l + r) / 2;
    if (i < m) {
        return get(i, 2 * v + 1, l, m);
    } else {
        return get(i, 2 * v + 2, m, r);
    }
}

long long min(int i, int j, int v, int l, int r) {
    if (j <= l || i >= r) return (long long) 1 << 32;
    if (j >= r && i <= l) {
        return a[v].first;
    }
    int m = (l + r) / 2;
    return min(min(i, j, 2 * v + 1, l, m), min(i, j, 2 * v + 2, m, r));

}

void push(int v) {
    if (v >= k - 1) {
        return;
    }
    int left = 2 * v + 1;
    int right = left + 1;
    if (a[v].second) {
        a[left] = a[v];
        a[right] = a[v];
        a[v].second = false;
    }
}

