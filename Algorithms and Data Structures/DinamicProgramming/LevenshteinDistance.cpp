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
    string s1, s2;
    cin >> s1 >> s2;
    int a[s1.length()+1][s2.length()+1];
    a[0][0] = 0;
    for (int i = 1; i <= s1.length(); ++i) {
        a[i][0]=i;
    }
    for (int i = 1; i <= s2.length(); ++i) {
        a[0][i]=i;
    }
    for (int i = 1; i <= s1.length(); ++i) {
        for (int j = 1; j <= s2.length(); ++j) {
            a[i][j]=1000000;
            if (s1[i-1]==s2[j-1]) {
                a[i][j] = min(a[i][j],a[i-1][j-1]);
            }
            a[i][j]=min(a[i][j],1+min(a[i-1][j],min(a[i][j-1],a[i-1][j-1])));
        }
    }
    cout << a[s1.length()][s2.length()];
    return 0;
}