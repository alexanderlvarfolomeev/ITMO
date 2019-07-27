#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <cmath>
#include <string>
#include <utility>
#include <stack>
 
using namespace std;
 
int main()
{
    int n;
    int x, y, m, k, l;
    char ch;
    bool b[200000];
    int c[100000];
    m = 0;
    l = 0;
    stack<int> a;
    cin >> n;
    for (int i = 0; i<n; i++)
    {
        cin >> k;
        while (!a.empty() && (a.top() < k))
        {
            b[m++] = true;
            c[l++] = a.top();
            a.pop();
        }
        a.push(k);
        b[m++]=false;
    }
    while (!a.empty())
    {
        b[m++] = true;
        c[l++] = a.top();
        a.pop();
    }
    for (int i = 1; i<n; i++)
    {
        if (c[i] < c[i-1])
        {
            cout << "impossible";
            return 0;
        }
    }
    for (int i = 0; i<m; i++)
    {
        if (b[i]) cout << "pop" << '\n';
        else cout << "push" << '\n';
    }
	return 0;
}
