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
    stack<int> s;
    cin >> n;
    int b[n], c[n], d[n];
    int mx = 0;
    for (int i = 0; i < n; ++i) {
        cin >> b[i];
        c[i] = 1;
        d[i] = i;
        for (int j = i-1; j >= 0 ; j--) {
            if (b[i]>b[j] && c[i]<=c[j]){
                c[i]=c[j]+1;
                d[i] = j;
            }
        }
        if (c[mx] < c[i]) {
            mx = i;
        }
    }
    k = c[mx];
    while (d[mx]!=mx)
    {
        s.push(b[mx]);
        mx = d[mx];
    }
    s.push(b[mx]);
    cout << k << '\n';
    while (!s.empty()){
        cout << s.top() << ' ';
        s.pop();
    }
    return 0;
}
