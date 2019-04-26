#include <iostream>
#include <vector>
#include <array>
#include <cmath>
#include <fstream>
#include <algorithm>
#include <map>


using namespace std;

void push(int v);

void add(int i, int j, int x, int v, int l, int r);

int find(int v, int l, int r);

struct quinta {
public:
    int x1;
    int y1;
    int y2;
    bool opening;

    quinta(int x1, int y1, int y2, bool opening) {
        this->x1 = x1;
        this->y1 = y1;
        this->y2 = y2;
        this->opening = opening;
    }

    static int comp(quinta a, quinta b) {
        return a.x1 == b.x1 ? a.opening == b.opening ? false : a.opening : a.x1 < b.x1;
    }
};

struct triple {
public:
    int value;
    pair<int, bool> d;

    triple() {
        this->value = 0;
        this->d = make_pair(0, false);
    }

    int true_value() {
        return value + (d.second ? d.first : 0);
    }
};

const int n = 1 << 19;
vector<triple> a;

int main() {
    int m, x1, y1, x2, y2, x, y;
    int mx = 0;
    
    x = 0;
    y = 0;
    for (int i = 0; i < 1 << 20; ++i) {
        a.emplace_back();
    }
    vector<quinta> point;
    cin >> m;
    for (int i = 0; i < m; ++i) {
        cin >> x1 >> y1 >> x2 >> y2;
        point.emplace_back(x1 + 200001, y1 + 200001, y2 + 200001, true);
        point.emplace_back(x2 + 200001, y2 + 200001, y1 + 200001, false);
    }
    sort(point.begin(), point.end(), quinta::comp);
    for (quinta i : point) {
        if (i.opening) {
            add(i.y1, i.y2 + 1, 1, 0, 0, n);
            if (mx < a[0].true_value()) {
                mx = a[0].true_value();
                x = i.x1 - 200001;
                y = find(0, 0, n) - 200001;
            }
        } else {
            add(i.y2, i.y1 + 1, -1, 0, 0, n);
        }
    }
    cout << mx << '\n' << x << ' ' << y;
    return 0;
}

int find(int v, int l, int r) {
    if (r - l == 1) {
        return l;
    }
    push(v);
    int m = (l + r) / 2;
    if (a[2 * v + 1].true_value() > a[2 * v + 2].true_value()) {
        return find(2 * v + 1, l, m);
    } else {
        return find(2 * v + 2, m, r);
    }
}

void add(int i, int j, int x, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        a[v].d.second = true;
        a[v].d.first += x;
        return;
    }
    push(v);
    int m = (l + r) / 2;
    add(i, j, x, 2 * v + 1, l, m);
    add(i, j, x, 2 * v + 2, m, r);
    a[v].value = max(a[2 * v + 1].true_value(), a[2 * v + 2].true_value());
}

void push(int v) {
    if (v >= n - 1) {
        a[v].value = a[v].true_value();
        a[v].d.second = false;
        a[v].d.first = 0;
        return;
    }
    if (a[v].d.second) {
        int left = 2 * v + 1;
        int right = left + 1;
        a[left].d.first += a[v].d.first;
        a[right].d.first += a[v].d.first;
        a[left].d.second = true;
        a[right].d.second = true;
        a[v].value = a[v].true_value();
        a[v].d.second = false;
        a[v].d.first = 0;
    }
}

