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
    int h, t, m;
    h = 0;
    t = 0;
    int a[200000];
    int b[100001];
    int c[100000];
    m = 0;
    cin >> n;
    int k, l;
    for (int i = 0; i < n; i++)
    {
        cin >> l;
        switch (l)
        {
        case 1:
            cin >> k;
            b[k] = h;
            a[h++] = k;
            break;
        case 2:
            t++;
            break;
        case 3:
            h--;
            break;
        case 4:
            cin >> k;
            c[m++] = b[k] - t;
            break;
        case 5:
            c[m++] = a[t];
            break;
        }
    }
    for (int i = 0; i < m; i++)
    {
        cout << c[i] << '\n';
    }
	return 0;
}
