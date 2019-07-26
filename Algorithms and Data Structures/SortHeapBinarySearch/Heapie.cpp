#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
 
using namespace std;
 
int a[100000], b[50000];
int n = 0, m = 0;
 
 
void add(int x)
{
    a[n++] = x;
    int i=n-1;
    while (i>0 && a[(i-1)/2]<a[i])
    {
        swap(a[i],a[(i-1)/2]);
        i=(i-1)/2;
    }
}
 
void remove_max()
{
    swap(a[0],a[n-1]);
    b[m++] = a[n-1];
    n--;
    int i=0;
    int j;
    while (2*i+1 < n)
    {
        j = 2*i+1;
        if (j+1<n && a[j+1]>a[j]) j++;
        if (a[i]>=a[j]) break;
        swap(a[i],a[j]);
        i=j;
    }
}
 
int main()
{
 
	int k,command,num;
	cin >> k;
	for (int i = 0; i < k; i++)
    {
        cin >> command;
        if (command) {
                        remove_max();
                     }
        else {
                cin >> num;
                add(num);
             }
    }
 
    for (int i = 0; i < m; i++)
    {
        cout << b[i] << "\n";
    }
	return 0;
}
