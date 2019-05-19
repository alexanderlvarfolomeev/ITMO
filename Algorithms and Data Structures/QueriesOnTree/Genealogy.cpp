#include <iostream>
#include <fstream>
#include <vector>
#include <unordered_set>
#include <unordered_map>
#include <cmath>
#include <memory.h>

using namespace std;

struct element {
    int value;
    pair<bool, bool> d;

    element() : value(0), d({false, false}){}
};

vector<int> rs[100001];
int depth[100001], parent[100001], heavy[100001], weight[100001], number[100001], chain[100001];
vector<pair<int, int>> nthPair;
vector<int> done;
//unordered_set<int> dd;
//unordered_map<int, int> mm;
element segTree[1 << 18];
int num = 0, ch = 0, n;
int root = 0;

void dfs(int v, int par = -1) {
    parent[v] = par;
    weight[v] = 1;
    for (int i : rs[v]) {
        if (i == par) {
            continue;
        }
        depth[i] = depth[v] + 1;
        dfs(i, v);
        weight[v] += weight[i];
        if (heavy[v] == -1 || (weight[heavy[v]] < weight[i])) {
            heavy[v] = i;
        }
    }
}

void hld(int v, int par = -1) {
    chain[v] = ch;
    number[v] = num;
    num++;
    if (nthPair.size() == ch) {
        nthPair.emplace_back(v, 0);
        done.push_back(-1);
    }
    nthPair[ch].second++;
    if (heavy[v] != -1) {
        hld(heavy[v], v);
    }
    for (int i : rs[v]) {
        if (i != par && i != heavy[v]) {
            ch++;
            hld(i, v);
        }
    }
}

void push(int v, int l, int r) {
    if (v >= n - 1) {
        if (segTree[v].d.second) {
            segTree[v].value = segTree[v].d.first ? 1 : 0;
            segTree[v].d = {false, false};
        }
        return;
    }
    int left = 2 * v + 1;
    int right = left + 1;
    if (segTree[v].d.second) {
        segTree[left].d.first = segTree[v].d.first;
        segTree[right].d.first = segTree[v].d.first;
        segTree[v].value = segTree[v].d.first ? r - l : 0;
        segTree[v].d = {false, false};
        segTree[left].d.second = true;
        segTree[right].d.second = true;
    }
}

void setTree(int i, int j, bool x, int v, int l, int r) {
    if (j <= l || i >= r) return;
    if (j >= r && i <= l) {
        segTree[v].d = {x, true};
        return;
    }
    push(v, l, r);
    int m = (l + r) / 2;
    setTree(i, j, x, 2 * v + 1, l, m);
    setTree(i, j, x, 2 * v + 2, m, r);
    int v1, v2;
    v1 = segTree[2 * v + 1].d.second ? (segTree[2*v+1].d.first ? (r - l) / 2 : 0) : segTree[2 * v + 1].value;
    v2 = segTree[2 * v + 2].d.second ? (segTree[2*v+2].d.first ? (r - l) / 2 : 0) : segTree[2 * v + 2].value;
    segTree[v].value = v1 + v2;
}

int getTree(int L, int R, int v, int l, int r) {
    if (R <= l || r <= L) {
        return 0;
    }
    if (L <= l && r <= R) {
        return segTree[v].d.second ? (segTree[v].d.first ? (r - l) : 0) : segTree[v].value;
    }
    push(v, l, r);
    int m = (l + r) / 2;
    return getTree(L, R, 2 * v + 1, l, m) + getTree(L, R, 2 * v + 2, m, r);
}

void turn(int x) {
    while (chain[x] != chain[root]) {
        if (done[chain[x]] != -1) {
            int first = done[chain[x]];
            if (first < number[x]) {
                setTree(first + 1, number[x] + 1, true, 0, 0, n);
                done[chain[x]] = number[x];
                //dd.insert(chain[x]);
            }
            return;
        }
        int first = nthPair[chain[x]].first;
        setTree(number[first], number[x] + 1, true, 0, 0, n);
        done[chain[x]] = number[x];
        //dd.insert(chain[x]);
        x = parent[nthPair[chain[x]].first];
    }

    if (done[chain[x]] != -1) {
        int first = done[chain[x]];
        if (first < number[x]) {
            setTree(first + 1, number[x] + 1, true, 0, 0, n);
            done[chain[x]] = number[x];
            //dd.insert(chain[x]);
        }
    } else {
        setTree(number[root], number[x] + 1, true, 0, 0, n);
        done[chain[root]] = number[x];
        //dd.insert(chain[x]);
    }
}

void zero() {
    setTree(0, n, false, 0, 0, n);
    for (int &i : done) {
        i = -1;
    }
    //dd.clear();
}

int main() {
    int m, x, y, k;
    y = 0;
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cin >> n;
    memset(heavy, -1, static_cast<size_t>(sizeof(int)*n));
    for (int i = 0; i < n; ++i) {
        cin >> x;
        if (x == -1) {
            root = i;
            continue;
        }
        x--;
        rs[x].push_back(i);
        rs[i].push_back(x);
    }
    n = lround(exp2(ceil(log2(n))));
    depth[0] = 0;
    dfs(root);
    hld(root);
    cin >> m;
    for (int i = 0; i < m; ++i) {
        cin >> k;
        for (int j = 0; j < k; ++j) {
            cin >> x;
            x--;
            turn(x);
            /*
            if (mm.find(chain[x]) != mm.end()) {
                if (number[mm[chain[x]]] < number[x]) {
                    mm[chain[x]] = x;
                }
            } else {
                mm[chain[x]] = x;
            }
             */
        }
        /*
        for (pair<const int, int> j : mm) {
            turn(j.second);
        }
         */
        cout << getTree(0, n, 0, 0, n) << '\n';
        zero();
        //mm.clear();
    }
    return 0;
}