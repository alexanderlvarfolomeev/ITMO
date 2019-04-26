#include <iostream>
#include <vector>
#include <array>
#include <cmath>


using namespace std;

class element {
public:
    int count;
    int length;
    bool left;
    bool right;
    bool changed;

    element() {
        count = 0;
        length = 0;
        left = false;
        right = false;
        changed = false;
    }

    element(int c, int lth, bool l, bool r, bool ch) {
        count = c;
        length = lth;
        left = l;
        right = r;
        changed = ch;
    }

    static element op(element x, element y) {
        int result = x.count + y.count;
        if (x.right && y.left) {
            result--;
        }
        return element(result, x.length + y.length, x.left, y.right, false);
    }
};

void set(int i, int j, bool x, int v, int l, int r);

void push(int v);

element a[1 << 21];
int n;

int main() {
    int x, y;
    char c;
    const int delta = 500000;
    vector<pair<int, int>> result;
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> c >> x >> y;
        x+=delta;
        set(x, x + y, c != 'W', 0, 0, 1 << 20);
        result.emplace_back(a[0].count, a[0].length);
    }
    for (pair<int, int> k : result) {
        cout << k.first << ' ' << k.second << '\n';
    }
    return 0;
}

void set(int i, int j, bool x, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        a[v].left = x;
        a[v].right = x;
        a[v].count = x ? 1 : 0;
        a[v].length = x ? (r - l) : 0;
        a[v].changed = true;
        return;
    }
    push(v);
    int m = (l + r) / 2;
    set(i, j, x, 2 * v + 1, l, m);
    set(i, j, x, 2 * v + 2, m, r);
    a[v] = element::op(a[2 * v + 1], a[2 * v + 2]);
}

void push(int v) {
    if (v >= (1 << 20) - 1) {
        a[v].changed = false;
        return;
    }
    int left = 2 * v + 1;
    int right = left + 1;
    if (a[v].changed) {
        a[left].right = a[v].right;
        a[right].right = a[v].right;
        a[left].left = a[v].left;
        a[right].left = a[v].left;
        a[left].length = a[v].length / 2;
        a[right].length = a[v].length / 2;
        a[left].count = a[v].count;
        a[right].count = a[v].count;
        a[left].changed = true;
        a[right].changed = true;
    }
}