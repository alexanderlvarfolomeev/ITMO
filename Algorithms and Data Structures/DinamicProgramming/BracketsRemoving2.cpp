#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <array>
#include <utility>
#include <stack>
 
using namespace std;
 
int n, k, m;
string s;
int a[100][100];
 
string getseq(int l, int r) {
    if (r < l + 1) return "";
    if (((s[l] == '(' && s[r] == ')') || (s[l] == '[' && s[r] == ']') || (s[l] == '{' && s[r] == '}')) &&
        a[l][r] == a[l + 1][r - 1] + 2) {
        return (s[l] + getseq(l + 1, r - 1) + s[r]);
    }
    if (a[l][r] == a[l + 1][r]) {
        return getseq(l + 1, r);
    }
    if (a[l][r] == a[l][r - 1]) {
        return getseq(l, r - 1);
    }
    for (int i = l; i < r - 1; ++i) {
        if (a[l][r] == a[l][i] + a[i + 1][r]) {
            return getseq(l, i) + getseq(i+1, r);
        }
    }
}
 
int main() {
    cin >> s;
    int r;
    n = s.length();
    for (int j = 0; j < n; ++j) {
        for (int i = 0; i < n - j; ++i) {
            r = i + j;
            if (j == 0) {
                a[i][r] = 0;
                continue;
            }
            a[i][r] = 1000;
            if ((s[i] == '(' && s[r] == ')') || (s[i] == '[' && s[r] == ']') || (s[i] == '{' && s[r] == '}')) {
                a[i][r] = 2 + a[i + 1][r - 1];
            } else {
                a[i][r] = max(a[i + 1][r], a[i][r - 1]);
            }
            for (int l = i; l < r; ++l) {
                a[i][r] = max(a[i][r], a[i][l] + a[l + 1][r]);
            }
        }
    }
 
    cout << getseq(0, n - 1);
    return 0;
}
