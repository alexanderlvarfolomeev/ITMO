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
    stack<int> a;
    while (cin >> ch)
    {
        switch (ch)
        {
        case '+':
            x = a.top();
            a.pop();
            y = a.top();
            a.pop();
            a.push(x+y);
            break;
        case '*':
            x = a.top();
            a.pop();
            y = a.top();
            a.pop();
            a.push(x*y);
            break;
        case '-':
            x = a.top();
            a.pop();
            y = a.top();
            a.pop();
            a.push(y-x);
            break;
        default:
            a.push(ch - '0');
            break;
        }
    }
    cout << a.top();
	return 0;
}
