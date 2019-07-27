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
    stack<int> a, b;
    vector<int> c;
    cin >> n;
    b.push(1000000001);
    for (int i = 0; i < n; i++)
    {
        int k;
        cin >> k;
        if (k == 1)
        {
            int l;
            cin >> l;
            a.push(l);
            b.push(min(l, b.top()));
        }
        if (k == 2) {
            a.pop();
            b.pop();
        }
        if (k == 3) {
            c.push_back(b.top());
        }
    }
    for (int i = 0; i<c.size(); i++)
    {
        cout << c[i] << '\n';
    }
	return 0;
}
