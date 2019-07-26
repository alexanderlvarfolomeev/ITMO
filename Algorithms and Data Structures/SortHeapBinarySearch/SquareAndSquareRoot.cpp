#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <cmath>
 
using namespace std;
 
double x,c;
 
int func(double k)
{
	return  ((k*k + 2*k*x - 2*x - 1+sqrt(x+k) > 0) ? 1:0);
}
 
int main()
{
    cin >> c;
    x = sqrt(c)-1;
    double l = 0;
	double r = 1;
    while (r>l + 0.00000001)
    {
        double m = (r+l) / 2;
        if (func(m)) r = m;
        else l = m;
    }
    x = x+l;
    printf("%f", x);
	return 0;
}
