#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
 
using namespace std;
 
int n,x,y;
 
int func(int k)
{
    int m = min(x,y);
	return (((1+(k-m)/x+(k-m)/y) >= n) ? 1:0);
}
 
int bs()
{
	int l = min(x,y)-1;
	int r = n*10+1;
	while (r>l + 1)
	{
		int m = l+(r-l) / 2;
		if (func(m)) r = m;
		else l = m;
	}
	return r;
}
 
int main()
{
    cin >> n >> x >> y;
    cout << bs();
	return 0;
}
