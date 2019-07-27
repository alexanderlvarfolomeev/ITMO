#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <cmath>
#include <string>
#include <utility>
#include <list>
 
using namespace std;
 
int main()
{
    int n;
    int h, t, m;
    char ch;
    h = 0;
    t = 0;
    int c[100000];
    list<int> a;
    list<int>::iterator mid = a.begin();
    m = 0;
    cin >> n;
    int k, l;
    l=0;
    for (int i = 0; i < n; i++)
    {
        cin >> ch;
        switch (ch)
        {
        case '+':
            cin >> k;
            a.push_back(k);
            l++;
            break;
        case '*':
            cin >> k;
            ++mid;
            a.insert(mid,k);
            --mid;
            --mid;
            l++;
            break;
        case '-':
            c[m++] = a.front();
            a.pop_front();
            l--;
            break;
        }
        if (a.size() < 2)
        {
            mid = a.begin();
        }
        else if (a.size()%2 == 1)
        {
            ++mid;
        }
        /*auto iter = a.begin();
        while(iter!=a.end())
        {
            cout << *iter;
            if (mid == iter) cout << '!';
            cout << ' ';
            ++iter;
        }
        if (mid == iter) cout << '!';
        cout << '\n';*/
    }
    for (int i = 0; i < m; i++)
    {
        cout << c[i] << '\n';
    }
	return 0;
}
