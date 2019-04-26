#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>


using namespace std;

long long sum(int i, int j, int k);

long long sum(int x1, int x2, int y1, int y2, int z1, int z2);

void add(int i, int j, int k, int v);

int n;
int a[128][128][128];

int main() {
    int m, x1, y1, x2, y2, z1, z2, x, y, z, k;
    vector<long long> b;
    x = 0;
    y = 0;
    cin >> k;
    n = lround(exp2(ceil(log2(k))));
    while (true) {
        cin >> m;
        if (m == 3) {
            break;
        } else {
            if (m == 1) {
                cin >> x >> y >> z >> k;
                add(x, y, z, k);
            } else {
                cin >> x1 >> y1 >> z1 >> x2 >> y2 >> z2;
                b.push_back(sum(x1 - 1, x2, y1 - 1, y2, z1 - 1, z2));
            }
        }
    }
    for (long long i : b) {
        cout << i << '\n';
    }
    return 0;
}

void add(int i, int j, int k, int v) {
    int x;
    int y;
    int z;
    x = i;
    while (x < n) {
        y = j;
        while (y < n) {
            z = k;
            while (z < n) {
                a[x][y][z] += v;
                z = z | (z + 1);
            }
            y = y | (y + 1);
        }
        x = x | (x + 1);
    }
}

long long sum(int i, int j, int k) {
    if (i == -1 || j == -1 || k == -1) {
        return 0;
    }
    long long s = 0;
    int x = i;
    while (x >= 0) {
        int y = j;
        while (y >= 0) {
            int z = k;
            while (z >= 0) {
                s += a[x][y][z];
                z = (z & (z + 1)) - 1;
            }
            y = (y & (y + 1)) - 1;
        }
        x = (x & (x + 1)) - 1;
    }
    return s;
}

long long sum(int x1, int x2, int y1, int y2, int z1, int z2) {
    return sum(x2, y2, z2) - sum(x1, y2, z2) - sum(x2, y1, z2) - sum(x2, y2, z1) + sum(x1, y1, z2) + sum(x1, y2, z1) +
           sum(x2, y1, z1) - sum(x1, y1, z1);
}
