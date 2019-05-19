#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <unordered_set>

using namespace std;

unordered_set<int> rs[200001];

int decom[200001], n, weight[200001], parent[200001];

void dfs(int v, int par = -1) {
    parent[v] = par;
    weight[v] = 1;
    for (int i : rs[v]) {
        if (i == par) {
            continue;
        }
        dfs(i, v);
        weight[v] += weight[i];
    }
}

void decomp(int root, int anc) {
    if (rs[root].empty()) {
        decom[root] = anc;
        return;
    }
    dfs(root);
    int t = root;
    while (true) {
        int next = -1;
        for (int r : rs[t]) {
            if (r == parent[t]) continue;
            if (2 * weight[r] > weight[root]) {
                next = r;
                break;
            }
        }
        if (next == -1) {
            break;
        }
        t = next;
    }
    vector<int> smez;
    for (int r : rs[t]) {
        smez.push_back(r);
        rs[r].erase(t);
        decomp(r, t);
    }
    decom[t] = anc;
}

int main() {
    int m, x, y;
    long long k;
    char c;
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);
    cin >> n;
    for (int i = 1; i < n; ++i) {
        cin >> x >> y;
        rs[x].insert(y);
        rs[y].insert(x);
    }
    decomp(1, 0);
    for (int i = 1; i <= n; ++i) {
        cout << decom[i] << ' ';
    }
    return 0;
}