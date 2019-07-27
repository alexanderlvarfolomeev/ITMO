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
    int m;
    m=0;
    int a[200000];
    cin >> n;
    int k, l;
    for (int i = 0; i < n; i++)
    {
        cin >> k;
        if (k != l)
        {
        l=10;
        if (m > 1)
        {
            if ((a[m-1] == k)&&(a[m-2] == k))
            {
                m = m - 2;
                l = k;
            }
            else a[m++] = k;
        }
        else if (m < 2)
        {
            a[m++] = k;
        }
        }
    }
    cout << (n - m);
	return 0;
}
