package queue;

import java.util.function.*;

public interface Queue implements Copiable {
    // Q:I → X, I - set of indexes, X - elements
    // head - first index
    // tail - last index

    // pre: element ≠ null
    // post: (new)size = size + 1 ∧ (new)head = head ∧ (new)tail = tail + 1 ∧
    // ∧ Q([previous]tail) = element ∧ (new)X = X ∪ {element}
    void enqueue(Object element);

    // pre: size > 0
    // post: ℝ = Q(head) ∧ (new)size = size − 1 ∧ (new)head = head + 1 ∧ (new)tail = tail ∧ (new)X = X\{ℝ}
    Object dequeue();

    // pre: size > 0
    // post: ℝ = Q(head) ∧ size = (new)size ∧ head = (new)head ∧ (new)elements = elements
    Object element();

    // post: ℝ = size ∧ (new)size = size ∧ (new)X = X
    int size();

    // post: ℝ = (size > 0) ∧ size = (new)size ∧ (new)X = X
    boolean isEmpty();

    // post: size = 0 ∧ tail = (primordial)tail ∧ head = (primordial)head ∧ X =∅
    void clear();

    // post: ℝ = Q' ∧ Q' = Q (size' = size ∧ tail' = tail ∧ head' = head
    Queue makeCopy();

    // post: ℝ = X ∧ X[i - head] = Q(i) ∀i∈I
    Object[] toArray();

    // post: ℝ = Q' ∧ (∀x∈X: condition(x) ⇒ x∈X') ∧ (indexOf(x1) < indexOf(x2) ⇒ indexOf(x1') < indexOf(x2'))
    Queue filter(Predicate<Object> condition);

    // post: ℝ = Q' ∧ (∀x∈X: function(x) = x' ⇒ x'∈X') ∧ (indexOf(x1) < indexOf(x2) ⇒ indexOf(x1') < indexOf(x2'))
    Queue map(Function<Object, Object> function);
}