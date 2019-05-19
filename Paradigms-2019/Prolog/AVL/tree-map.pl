map_get(node(Key, Value, _, _, _), Key, Value) :- !.
map_get(node(Key, Value, LeftChild, RightChild, _), K, V) :- 
	Key @> K, map_get(LeftChild, K, V); 
	Key @< K, map_get(RightChild, K, V).
node(x, 2, node(y, 1, node, node, 0), node(z, 3, node, node, 0), 0).
% map_get(node(2, x, node(1, y, node,node), node(3, z, node,node)), 4, z).
balance(D, D, 0) :- !.
balance(LD, RD, -1) :- LD \== RD.
% tree_build([(1, x), (2, z), (3, y)], X).
tree_build(ListMap, TreeMap) :- length(ListMap, Len), tree_build(Len, ListMap, [], _, TreeMap).
tree_build(1, [(Key, Value) | Rest], Rest, 1, node(Key, Value, node, node, 0)) :- !.
tree_build(0, Rest, Rest, 0, node) :- !.
tree_build(Len, List, Rest, Depth, node(Key, Value, LeftChild, RightChild, Balance)) :-
    LeftLen is Len div 2,
    RightLen is Len - LeftLen - 1,    
    tree_build(LeftLen, List, [(Key, Value) | Next], LeftDepth, LeftChild),
    tree_build(RightLen, Next, Rest, RightDepth, RightChild),
    Depth is LeftDepth + 1,
    balance(LeftDepth, RightDepth, Balance).