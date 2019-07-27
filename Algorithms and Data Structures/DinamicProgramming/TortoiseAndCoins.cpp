#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <stack>
 
using namespace std;
 
vector<vector<int>> a;
vector<int> b;
int n, k, m;
 
int main() {
    ifstream fin("input.txt");
    ofstream fout("output.txt");
    string s;
    fin >> n >> m;
    for (int i = 0; i < n; ++i) {
        b.clear();
        for (int j = 0; j < m; ++j) {
            fin >> k;
            b.push_back(k);
        }
        a.push_back(b);
    }
    for (int l = 1; l < n; ++l) {
        a[l][0] = a[l-1][0]+a[l][0];
    }
    for (int l = 1; l < m; ++l) {
        a[0][l] = a[0][l-1]+a[0][l];
    }
    for (int i = 1; i < n; ++i) {
        for (int j = 1; j < m; ++j) {
            a[i][j]=max(a[i-1][j],a[i][j-1])+a[i][j];
        }
    }
    int i = n-1;
    int j = m-1;
    while (i > 0 || j > 0) {
        if (i == 0) {
            s = 'R'+s;
            j--;
        } else {
            if (j == 0) {
                s='D'+s;
                i--;
            } else {
                if (a[i-1][j]>a[i][j-1]) {
                    s='D'+s;
                    i--;
                } else {
                    s = 'R'+s;
                    j--;
                }
            }
        }
    }
    fout << a[n-1][m-1] << '\n';
    fout << s;
    fin.close();
    fout.close();
    return 0;
}
