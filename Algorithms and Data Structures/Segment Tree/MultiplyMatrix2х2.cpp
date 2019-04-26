#include <iostream>
#include <vector>
#include <array>
#include <cmath>
#include <fstream>


using namespace std;

int r;

class element {
public:
    int a11;
    int a12;
    int a21;
    int a22;

    element() {
        a11 = 1;
        a12 = 0;
        a21 = 0;
        a22 = 1;
    }

    element(int b11, int b12, int b21, int b22) {
        a11 = abs(b11 % r);
        a12 = abs(b12 % r);
        a21 = abs(b21 % r);
        a22 = abs(b22 % r);
    }

    static element op(element x, element y) {
        return element(x.a11 * y.a11 + x.a12 * y.a21, x.a11 * y.a12 + x.a12 * y.a22,
                       x.a21 * y.a11 + x.a22 * y.a21, x.a21 * y.a12 + x.a22 * y.a22);
    }
};

element get(int i, int j, int v, int l, int r);

void push(int v);

element a[1 << 19];

int main() {
    int n, m, x, b, c, d;
    ifstream fin("crypto.in");
    ofstream fout("crypto.out");
    vector<element> result;
    fin >> r >> n >> m;
    for (int i = 0; i < n; i++) {
        fin >> x >> b >> c >> d;
        a[(1 << 18) - 1 + i] = element(x, b, c, d);
    }
    for (int j = (1 << 18) - 2; j >= 0; --j) {
        a[j] = element::op(a[2 * j + 1], a[2 * j + 2]);
    }
    for (int i = 0; i < m; i++) {
        fin >> b >> c;
        result.push_back(get(b - 1, c, 0, 0, 1 << 18));
    }
    for (element k : result) {
        fout << k.a11 << ' ' << k.a12 << '\n';
        fout << k.a21 << ' ' << k.a22 << '\n' << '\n';
    }
    return 0;
}

element get(int i, int j, int v, int l, int r) {
    if (j <= l || i >= r) return element();
    if (j >= r && i <= l) {
        return a[v];
    }
    int m = (l + r) / 2;
    return element::op(get(i, j, 2 * v + 1, l, m), get(i, j, 2 * v + 2, m, r));
}