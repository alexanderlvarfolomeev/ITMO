#include <iostream>
#include <vector>
#include <array>
#include <cmath>

const long long zero = 1000000000000000001;

long long min(long long L, long long R, long long v, long long l, long long r);

void add(long long i, long long j, long long x, long long v, long long l, long long r);

void set(long long i, long long j, long long x, long long v, long long l, long long r);

void push(long long v);

using namespace std;

class element {
public:
    long long value;
    pair<long long, bool> d1;
    pair<long long, bool> d2;

    element() {
        value = zero;
        d1 = make_pair(0, false);
        d2 = make_pair(0, false);
    }

    long long true_value() {
        long long result = value;
        if (d1.second) {
            result = d1.first;
        }
        if (d2.second) {
            result += d2.first;
        }
        return result;
    }
};

element a[1 << 18];
long long n;

int main() {
    long long x, y, z;
    vector<long long> minimum;
    cin >> n;
    x = llround(exp2(ceil(log2(n))));
    for (long long i = x - 1; i < x + n - 1; ++i) {
        cin >> a[i].value;
    }
    n = x;
    for (long long i = n - 2; i >= 0; --i) {
        a[i].value = min(a[2 * i + 1].value, a[2 * i + 2].value);
    }
    string s;
    while (cin >> s) {
        cin >> x >> y;
        if (s == "min") {
            minimum.push_back(min(x - 1, y, 0, 0, n));
        } else {
            cin >> z;
            if (s == "set") {
                set(x - 1, y, z, 0, 0, n);
            } else {
                add(x - 1, y, z, 0, 0, n);
            }
        }
    }
    for (long long k : minimum) {
        cout << k << '\n';
    }
    return 0;
}

void add(long long i, long long j, long long x, long long v, long long l, long long r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        a[v].d2.second = true;
        a[v].d2.first += x;
        //push(v);
        return;
    }
    push(v);
    long long m = (l + r) / 2;
    add(i, j, x, 2 * v + 1, l, m);
    add(i, j, x, 2 * v + 2, m, r);
    a[v].value = min(a[2 * v + 1].true_value(), a[2 * v + 2].true_value());
}

void set(long long i, long long j, long long x, long long v, long long l, long long r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        a[v].d1.second = true;
        a[v].d1.first = x;
        a[v].d2.second = false;
        a[v].d2.first = 0;
        return;
    }
    push(v);
    long long m = (l + r) / 2;
    set(i, j, x, 2 * v + 1, l, m);
    set(i, j, x, 2 * v + 2, m, r);
    a[v].value = min(a[2 * v + 1].true_value(), a[2 * v + 2].true_value());
}

long long min(long long L, long long R, long long v, long long l, long long r) {
    if (R <= l || r <= L) {
        return zero;
    }
    if (L <= l && r <= R) {
        return a[v].true_value();
    }
    push(v);
    //a[v].value = min(a[2 * v + 1].true_value(), a[2 * v + 2].true_value());
    long long m = (l + r) / 2;
    return min(min(L, R, 2 * v + 1, l, m), min(L, R, 2 * v + 2, m, r));
}

void push(long long v) {
    if (v >= n - 1) {
        a[v].value = a[v].true_value();
        a[v].d1.second = false;
        a[v].d2.second = false;
        a[v].d2.first = 0;
        return;
    }
    long long left = 2 * v + 1;
    long long right = left + 1;
    if (a[v].d1.second) {
        a[left].d1 = a[v].d1;
        a[right].d1 = a[v].d1;
        a[left].d2 = a[v].d2;
        a[right].d2 = a[v].d2;
        a[v].value = a[v].true_value();
        a[v].d1.second = false;
        a[v].d2.second = false;
        a[v].d2.first = 0;
        return;
    }
    if (a[v].d2.second) {
        a[left].d2.second = true;
        a[right].d2.second = true;
        a[left].d2.first += a[v].d2.first;
        a[right].d2.first += a[v].d2.first;
        a[v].value = a[v].true_value();
        a[v].d1.second = false;
        a[v].d2.second = false;
        a[v].d2.first = 0;
    }
    /*
    if (a[v].d1.second || a[v].d2.second) {
        a[left].d1 = a[v].d1;
        a[right].d1 = a[v].d1;
        a[left].d2 = a[v].d2;
        a[right].d2 = a[v].d2;
    }
     */
}