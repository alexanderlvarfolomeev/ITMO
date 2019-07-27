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
 
int p[200001];
int r[200001];
long gexp[200001];
 
int get(int x)
{
    if (p[x]!=x)
    {
        int root = get(p[x]);
        if (p[x]!=root) {
            gexp[x]+=gexp[p[x]];
        }
        p[x]=root;
    }
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
    gexp[x]-=gexp[y];
    }
}
 
int ex(int x)
{
    int res = 0;
    while (x!=p[x])
    {
        res += gexp[x];
        x=p[x];
    }
    res+=gexp[x];
    return res;
}
 
int main()
{
    int n, m;
    int k, l;
    string s;
    cin >> n >> m;
    for (int i = 0; i<=n; i++)
    {
        p[i]=i;
        r[i] = 0;
        gexp[i] = 0;
    }
    for (int i = 0; i<m; i++)
    {
        cin >> s;
        if (s == "join")
        {
            cin >> k >> l;
            unite(k,l);
        }
        if (s=="get")
        {
            cin >> k;
            cout << ex(k) << '\n';
        }
        if (s=="add")
        {
            cin >> k >> l;
            k = get(k);
            gexp[k]+=l;
        }
    }
	return 0;
}
