#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <unordered_set>
#include <unordered_map>


#define ll long long

using namespace std;

struct Centroid;

unordered_set<int> rs[200001];

bool colour[200001];

Centroid *num2cen[200001];

struct Centroid {
    unordered_map<int, int> dist;
    unordered_map<int, int> dist2;
    int v;
    int v2;
    int parent;
    int size;
    int size_black;
    ll ans_black;
    ll ans_white;
    ll ans_black2;
    ll ans_white2;

    void dfs(int u, int depth = 0, int par = -1) {
        this->size++;
        size_black++;
        ans_black+=depth;
        dist[u] = depth;
        for (int i : rs[u]) {
            if (i == par) {
                continue;
            }
            dfs(i, depth + 1, u);
        }
    }

    void dfs2(int u2, int depth = 0, int par = -1) {
        ans_black2+=depth;
        dist2[u2] = depth;
        for (int i : rs[u2]) {
            if (i == par) {
                continue;
            }
            dfs2(i, depth + 1, u2);
        }
    }

    Centroid(int num, int num2, int p) {
        v = num;
        v2 = num2;
        ans_white = 0;
        ans_black = 0;
        ans_white2 = 0;
        ans_black2 = 0;
        size_black = 0;
        this->size = 0;
        parent = p;
        dfs(v);
        if (p != 0) {
            dfs2(v2);
        }

    }

    explicit Centroid(int num, int p) {
        v = num;
        v2 = num;
        this->parent = p;
        this->size = 1;
        size_black = 1;
        ans_black = 0;
        ans_white = 0;
        ans_black2 = 0;
        ans_white2 = 0;
        dist[v] = 0;
        dist2[v2] = 0;
    }

    void change(int u) {
        if (colour[u]) {
            int t = dist[u];
            int t2 = dist2[u];
            ans_white-=t;
            ans_black+=t;
            ans_white2-=t2;
            ans_black2+=t2;
            size_black++;
        } else {
            int t = dist[u];
            int t2 = dist2[u];
            ans_white+=t;
            ans_black-=t;
            ans_white2+=t2;
            ans_black2-=t2;
            size_black--;
        }
        if (parent != 0) {
            num2cen[parent]->change(u);
        }
    }

    ll query(int u, ll prev_ans = 0, int prev_size = 0) {
        ll res = 0;
        if (colour[u]) {
            res+=ans_white - prev_ans - prev_size;
            if (u != v) {
                res += (ll)(size - size_black - prev_size) * dist[u];
            }
            if (parent != 0) {
                res+=num2cen[parent]->query(u, ans_white2, size - size_black);
            }
        } else {
            res+= ans_black - prev_ans - prev_size;
            if (u != v) {
                res += (ll)(size_black - prev_size) * dist[u];
            }
            if (parent != 0) {
                res+=num2cen[parent]->query(u, ans_black2, size_black);
            }
        }
        return res;
    }
};
int n, weight[200001], parent[200001];

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
        num2cen[root] = new Centroid(root, anc);
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
    num2cen[t] = new Centroid(t, root, anc);
    for (int r : rs[t]) {
        rs[r].erase(t);
        decomp(r, t);
    }
}

int main() {
    int m, x, y;
    ll k = 1 << 30;
    char c;
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);
    cin >> n >> m;
    for (int i = 1; i < n; ++i) {
        cin >> x >> y;
        rs[x].insert(y);
        rs[y].insert(x);
    }
    decomp(1, 0);
    for (int i = 0; i < m; ++i) {
        cin >> x >> y;
        if (x == 1) {
            num2cen[y]->change(y);
            colour[y] = !colour[y];
        } else {
            cout << num2cen[y]->query(y) << '\n';
        }
    }
    return 0;
}