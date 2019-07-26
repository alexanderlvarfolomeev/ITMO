#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
 
using namespace std;
 
int n, f;
int a[100001], b[100001];
 
int func(int k)
{
	return ((a[k] >= f) ? 1 : 0);
}
 
int bs()
{
	int l = -1;
	int r = n;
	while (r>l + 1)
	{
		int m = (l + r) / 2;
		if (func(m)) r = m;
		else l = m;
	}
	if (r >= n) return a[n - 1];
	if (a[r] == f)return a[r];
	if (r == 0) return a[0];
	if ((r>0 && (a[r] - f < f - a[r - 1])) || r==0) return a[r];
	else return a[r - 1];
}
 
int main()
{
	int k;
	cin >> n >> k;
	for (int i = 0; i<n; i++)
	{
		cin >> a[i];
	}
	for (int i = 0; i<k; i++)
	{
		cin >> f;
		b[i] = bs();
	}
	for (int i = 0; i<k; i++)
	{
		cout << b[i] << "\n";
	}
	return 0;
}
