#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <stack>
 
using namespace std;
 
vector<int> a;
int n, k, m;
 
int main() {
    ifstream fin("input.txt");
    ofstream fout("output.txt");
    stack<int> b;
    fin >> n >> k;
    a.push_back(0);
    a.push_back(0);
    b.push(n);
    for (int i = 0; i < n - 2; i++) {
        fin >> m;
        a.push_back(m);
    }
    a.push_back(0);
    for (int j = 1; j <= n; ++j) {
        int mx = a[j-1];
        for (int i = 1; i <= k; ++i) {
            mx = max(mx, a[max(j-i, 0)]);
        }
        a[j] = mx + a[j];
    }
    int i = n;
    while (i != 1) {
        int i2 = i;
        i--;
        for (int j = max(i2 - k, 1); j <= i2-1; ++j) {
            if (a[j] > a[i]) i = j;
        }
        b.push(i);
    }
    fout << a[n] << '\n' << b.size() - 1 << '\n';
    while (!b.empty()) {
        fout << b.top() << ' ';
        b.pop();
    }
    fin.close();
    fout.close();
    return 0;
}
