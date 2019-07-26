#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
 
using namespace std;
 
long long n,w,h;
 
long long func(long long k)
{
    long long m = (k/w)*(k/h);
	return  ((m>= n) ? 1:0);
}
 
long long bs()
{
	long long l = 0;
	long long r = n*max(w,h);
	while (r>l + 1)
	{
		long long m = l+(r-l) / 2;
		if (func(m)) r = m;
		else l = m;
	}
	return r;
}
 
int main()
{
    cin >> w >> h >> n;
    cout << bs();
	return 0;
}
