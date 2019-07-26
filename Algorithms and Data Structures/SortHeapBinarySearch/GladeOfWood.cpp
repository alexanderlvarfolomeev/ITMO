#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <cmath>
 
using namespace std;
 
double x,a;
    int v1,v2;
 
double func(double k)
{
	return (sqrt((1-a)*(1-a)+k*k)/v1+sqrt((1-k)*(1-k)+a*a)/v2) ;
}
 
int main()
{
    cin >> v1 >> v2 >> a;
    double l = 0;
	double r = 1;
    while (r>l + 0.00005)
    {
        double m1 = (r+2*l) / 3;
        double m2 = (2*r+l) / 3;
        if (func(m1) < func(m2)) r = m2;
        else l = m1;
    }
    cout << l;
	return 0;
}
