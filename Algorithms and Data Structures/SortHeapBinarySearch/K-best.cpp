#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <cmath>
 
using namespace std;
 
int n,k;
int v[100000],w[100000],b[100000];
double a[100000];
 
void qsort(int l,int r)
{
  int i = l, j = r;
  double x = a[(l+r)/2];
  do
  {
    while (a[i] > x)
    {
        i++;
    }
    while (a[j] < x)
    {
        j--;
    }
    if ( i <= j )
    {
 
      double y=a[i];
      a[i]=a[j];
      a[j]=y;
      i++;
      j--;
    }
  }
  while (i <= j);
  if (l < j) qsort(l,j);
  if (i < r) qsort(i,r);
}
 
double func(double x)
{
    double s = 0;
    for (int i = 0; i<n;i++)
    {
        a[i]=v[i]-x*w[i];
    }
    qsort(0,n-1);
	for (int i=0;i<k;i++)
        s+=a[i];
	return s;
}
 
int main()
{
    ifstream fin("kbest.in");
    ofstream fout("kbest.out");
    fin >> n >> k;
    double r = 0;
    for (int i=0;i<n;i++)
    {
        fin >> v[i] >> w[i];
        r+=v[i];
    }
    double l = 0;
    double m;
    while (r > l+ 0.00000002)
    {
        m =(r+l)/2;
        if (func(m)<0) r=m;
        else l=m;
    }
    int i=0,count=0;
    func(l);
    while (i<n && count<k)
    {
        if (v[i]-l*w[i] > a[k-1]) {
            b[count]=i+1;
            count++;
        }
        i++;
    }
    i=0;
    while (i<n && count<k)
    {
        if (v[i]-l*w[i] == a[k-1]) {
            b[count]=i+1;
            count++;
        }
        i++;
    }
	for (int i = 0; i<k;i++)
        fout << b[i] <<"\n";
    fin.close();
    fout.close();
	return 0;
}
