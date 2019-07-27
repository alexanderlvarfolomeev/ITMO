#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <stack>
 
using namespace std;
 
int n, k, m;
 
int main() {
    cin >> n;
    int a[n+1][10];
    for (int i = 0; i < 10; ++i) {
        if (i!=0 && i!=8) {
            a[1][i] = 1;
        } else a[1][i]=0;
    }
    for (int i = 2; i <= n; ++i) {
        a[i][0] = (a[i-1][4] + a[i-1][6])%1000000000;
        a[i][1] = (a[i-1][8] + a[i-1][6])%1000000000;
        a[i][2] = (a[i-1][7] + a[i-1][9])%1000000000;
        a[i][3] = (a[i-1][4] + a[i-1][8])%1000000000;
        a[i][4] = ((a[i-1][3] + a[i-1][9])%1000000000 + a[i-1][0])%1000000000;
        a[i][5] = 0;
        a[i][6] = ((a[i-1][1] + a[i-1][7])%1000000000 + a[i-1][0])%1000000000;
        a[i][7] = (a[i-1][2] + a[i-1][6])%1000000000;
        a[i][8] = (a[i-1][1] + a[i-1][3])%1000000000;
        a[i][9] = (a[i-1][4] + a[i-1][2])%1000000000;
    }
    int res=0;
    for (int i = 0; i < 10; ++i) {
        res = (res+a[n][i])%1000000000;
    }
    cout << res;
    return 0;
}
