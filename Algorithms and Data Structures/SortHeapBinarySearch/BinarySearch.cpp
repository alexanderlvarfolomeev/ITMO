#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
 
using namespace std;
 
int len, x, y;
 
int func1(int k, vector<int>* a)
{
	return (((*a)[k]>y) ? 1 : 0);
}
 
int func2(int k, vector<int>* a)
{
	return (((*a)[k]>=x) ? 1 : 0);
}
 
int bs1(vector<int>* a)
{
	int l = -1;
	int r = len;
	while (r>l + 1)
	{
		int m = (l + r) / 2;
		if (func1(m, a)) r = m;
		else l = m;
	}
	return r;
}
 
int bs2(vector<int>* a)
{
	int l = -1;
	int r = len;
	while (r>l + 1)
	{
		int m = (l + r) / 2;
		if (func2(m, a)) r = m;
		else l = m;
	}
	return r;
}
 
int main()
{
	int n;
	int b[100000];
	cin >> n;
	vector<int> a(n);
	for (int i = 0; i<n; ++i)
		cin >> a[i];
	vector<int> lr(0);
	for (int i = 1; i <= n; i *= 2)
	{
		for (int j = 0; j < n - 1; j += i * 2)
		{
			int l = j, m = min(j + i, n), r = min(j + i * 2, n);
			int m1 = m;
			while ((l < m1) || (m < r))
			{
				if ((m == r) || ((l < m1) && (a[l] <= a[m]))) { lr.push_back(a[l]); l++; }
				else { lr.push_back(a[m]); m++; }
			}
			for (int k = j; k < r; k++)
			{
				a[k] = lr[k - j];
			}
			lr.clear();
		}
	}
	len = n;
	cin >> n;
	for (int i = 0; i < n; i++)
	{
		cin >> x >> y;
		b[i] = bs1(&a) - bs2(&a);
	}
	for (int i = 0; i < n; i++)
	{
		cout << b[i] << " ";
	}
	return 0;
}
