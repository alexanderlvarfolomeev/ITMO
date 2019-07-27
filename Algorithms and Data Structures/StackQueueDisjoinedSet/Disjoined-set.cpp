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
 
int p[300001];
int r[300001];
int mi[300001];
int ma[300001];
int co[300001];
 
int get(int x)
{
    if (p[x]!=x) p[x]=get(p[x]);
    return p[x];
}
 
void unite(int x, int y)
{
    x = get(x);
    y = get(y);
    if (x!=y) {
    if (r[y]<r[x]) swap(x,y);
    p[x] = y;
    if (r[x]==r[y]) r[y]++;
    mi[y] = min(mi[y],mi[x]);
    ma[y] = max(ma[y],ma[x]);
    co[y] = co[y]+co[x];
    }
}
 
int main()
{
    int n;
    int k, l;
    string s;
    stack<int> a;
    cin >> n;
    for (int i = 0; i<=n; i++)
    {
        p[i]=i;
        r[i] = 0;
        mi[i]=i;
        ma[i] = i;
        co[i] = 1;
    }
    while (cin >> s)
    {
        if (s == "union")
        {
            cin >> k >> l;
            unite(k,l);
        }
        if (s=="get")
        {
            cin >> k;
            k = get(k);
            cout << mi[k] << ' ' << ma[k] << ' ' << co[k] << '\n';
        }
    }
	return 0;
}
