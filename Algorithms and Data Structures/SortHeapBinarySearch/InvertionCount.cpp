#include <iostream>
#include <vector>
#include <algorithm>
 
using namespace std;
 
 
int main()
{
	int n;
	unsigned long long count=0;
	cin >> n;
	vector<int> a(n);
	for (int i = 0; i<n; ++i)
		cin >> a[i];
	vector<int> lr(0);
	for (int i = 1; i <= n; i *= 2)
	{
		for (int j = 0; j < n - 1; j += i * 2)
		{
			int l = j, m = min(j + i,n), r = min(j + i * 2, n);
			int m1 = m;
			while ((l < m1) || (m < r))
			{
				if ((m == r) || ((l < m1) && (a[l] <= a[m]))) { lr.push_back(a[l]); l++; }
				else { lr.push_back(a[m]); m++; count+=m1-l; }
			}
			for (int k = j; k < r; k++)
			{
				a[k] = lr[k - j];
			}
			lr.clear();
		}
	}
		cout << count;
	return 0;
}
