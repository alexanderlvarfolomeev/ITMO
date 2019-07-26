#include <iostream>
#include <vector>
#include <algorithm>
 
using namespace std;
 
 
int main()
{
    int n=0,k=0,j = 0;
    int count[101];
    for (int i = 0;i < 101;i++)
    {
        count[i]=0;
    }
	while (cin >> k)
	{
        count[k]++;
        n++;
	}
	while (n > 0)
    {
        if (count[j] > 0) {
                            cout << j << " ";
                            count[j]--;
                            n--;
                          }
        else {
                j++;
             }
    }
	return 0;
}