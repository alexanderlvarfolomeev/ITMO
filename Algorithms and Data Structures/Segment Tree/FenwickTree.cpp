#include <iostream>
#include <vector>
#include <array>

std::array<long long, 500000> a{};
long long n;

long long sum(long long i);

long long sum(long long i, long long j);

void add(long long i, long long x);

void insert(long long i, long long x);

using namespace std;

int main() {
    long long x, y;
    vector<long long> sums;
    cin >> n;
    for (int i = 0; i < n; ++i) {
        cin >> a[i];
    }
    for (int i = 0; i < n; i++) {
        int j = i | (i + 1);
        if (j < n) {
            a[j] += a[i];
        }
    }
    string s;

    while (cin >> s) {
        cin >> x >> y;
        if (s == "sum") {
            sums.push_back(sum(x - 1, y - 1));
        } else {
            insert(x - 1, y);
        }
    }
    for (long long sum : sums) {
        cout << sum << '\n';
    }
    return 0;
}

void add(long long i, long long x) {
    while (i < n) {
        a[i] += x;
        i = i | (i + 1);
    }
}

void insert(long long i, long long x) {
    add(i, x - sum(i, i));
}

long long sum(long long i) {
    long long s = 0;
    while (i >= 0) {
        s += a[i];
        i = (i & (i + 1)) - 1;
    }
    return s;
}

long long sum(long long i, long long j) {
    if (i == 0) {
        return sum(j);
    }
    return sum(j) - sum(i - 1);
}