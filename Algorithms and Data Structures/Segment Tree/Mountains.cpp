#include <iostream>
#include <vector>
#include <array>
#include <cmath>
#include <algorithm>
#include <map>
#include <set>


using namespace std;

void oper(int i, int j, int x, int v, int l, int r);

void push(int v);

int get(int x, int v);

struct triple {
public:
    int first;
    int second;
    int third;

    triple(int x) {
        first = x;
        second = 0;
        third = 0;
    }

    triple(int x, int y, int z) {
        first = x;
        second = y;
        third = z;
    }
};

struct quinta {
public:
    int sum;
    int pref;
    int count;
    pair<bool, int> d;

    quinta(int sum, int pref, int count, pair<bool, int> d) {
        this->sum = sum;
        this->pref = pref;
        this->count = count;
        this->d = d;
    }

    quinta(int x, int y) {
        this->sum = x;
        this->count = y;
        this->d = make_pair(false, 0);
        if (x >= 0) {
            this->pref = x;
        } else {
            this->pref = 0;
        }
    }

    quinta() {
        sum = 0;
        count = 0;
        pref = 0;
        d = make_pair(false, 0);
    }

    quinta true_value() {
        if (d.first) {
            return quinta(d.second * count, count);
        } else {
            return *this;
        }
    }

    static quinta op(quinta x, quinta y) {
        int res = max(x.sum + y.pref, x.pref);
        return quinta(x.sum + y.sum, res, x.count + y.count, make_pair(false, 0));
    }
};
//262144
vector<quinta> a;
vector<pair<bool, triple>> c{};
int n, m, r, k;

int main() {
    int x, y, z;
    vector<int> point;
    map<int, int> map1;
    cin >> n;
    string s;
    while (true) {
        cin >> s;
        if (s == "E") {
            break;
        }
        if (s == "Q") {
            cin >> x;
            c.emplace_back(false, triple(x));
        }
        if (s == "I") {
            cin >> x >> y >> z;
            c.emplace_back(true, triple(x - 1, y - 1, z));
            point.push_back(x - 1);
            point.push_back(x);
            point.push_back(y - 1);
            point.push_back(y);
            point.push_back(y - 2);
            point.push_back(x - 2);
        }
    }
    point.push_back(n - 1);
    sort(point.begin(), point.end());
    k = -1;
    for (int i : point) {
        if (map1.insert(make_pair(i, a.size())).second) {
            a.emplace_back(0, i - k);
            k = i;
        }
    }
    r = lround(exp2(ceil(log2(a.size()))));
    a.reserve(static_cast<unsigned int>(2 * r));
    for (int j = 0; j < r; ++j) {
        a[j + r - 1] = a[j];
    }
    for (int j = r - 2; j >= 0; --j) {
        a[j] = quinta::op(a[2 * j + 1], a[2 * j + 2]);
    }
    for (auto i : c) {
        if (i.first) {
            oper(map1.find(i.second.first)->second, map1.find(i.second.second)->second + 1, i.second.third, 0, 0, r);
        } else {
            cout << min(get(i.second.first, 0), n) << '\n';
        }
    }
    return 0;
}

void oper(int i, int j, int x, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        a[v].d.first = true;
        a[v].d.second = x;
        return;
    }
    push(v);
    int m = (l + r) / 2;
    oper(i, j, x, 2 * v + 1, l, m);
    oper(i, j, x, 2 * v + 2, m, r);
    a[v] = quinta::op(a[2 * v + 1].true_value(), a[2 * v + 2].true_value());
}

int get(int x, int v) {
    push(v);
    if (v >= r - 1) {
        return a[v].sum <= 0 ? a[v].count : x / (a[v].sum / a[v].count);
    }
    int m = 2 * v + 1;
    push(m);
    if (a[m].pref > x) {
        return get(x, m);
    } else {
        return a[m].count + get(x - a[m].sum, m + 1);
    }

    /*
    if (R <= l || r <= L) {
        return quinta(0, r - l);
    }
    if (L <= l && r <= R) {
        return a[v].true_value();
    }
    push(v);
    int m = (l + r) / 2;
    return quinta::op(get(L, R, 2 * v + 1, l, m), get(L, R, 2 * v + 2, m, r));
     */
}

void push(int v) {
    if (v >= r - 1) {
        a[v] = a[v].true_value();
        return;
    }
    if (a[v].d.first) {
        int left = 2 * v + 1;
        int right = left + 1;
        a[left].d = a[v].d;
        a[right].d = a[v].d;
        a[v] = a[v].true_value();
    }
}