package queue;

public class ArrayQueue {
    private int tail;
    private int head;
    private int size;
    private Object[] elements = new Object[10];

    // pre: element ≠ null
    // post: (new)size = size + 1 ∧ (new)tail = tail + 1 ∧
    // ∀i=[head; tail) ∧ ∀i=[0; tail) ∪ [head; elements.length) : (new)a[i] = a[i] ∧ a[tail - 1] = element
    public void enqueue(Object element) {
        assert element != null;

        ensureCapacity(size + 1);
        elements[tail] = element;
        size++;
        tail = (tail + 1) % elements.length;
    }

    // pre: element ≠ null
    // post: (new)size = size + 1 ∧ (new)head = head - 1 ∧
    // ∀i=[head; tail) ∧ ∀i=[0; tail) ∪ [head; elements.length) : (new)a[i] = a[i] ∧ a[(new)head] = element
    public void push(Object element) {
        assert element != null;

        ensureCapacity(size + 1);
        head = (head + elements.length - 1) % elements.length;
        elements[head] = element;
        size++;
    }

    private void ensureCapacity(int capacity) {
        if (capacity <= elements.length) {
            return;
        }
        Object[] newElements = new Object[2 * capacity];
        if (head < tail) {
            System.arraycopy(elements, head, newElements, 0, size);
        } else {
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, tail);
        }
        elements = newElements;
        head = 0;
        tail = size;
    }

    // pre: size > 0
    // post: ℝ = a[head] ∧ (new)size = size − 1 ∧ (new)head = head + 1 ∧
    // ∀i=[head; tail) ∧ ∀i=[0; tail) ∪ [head; elements.length) : (new)a[i] = a[i]
    public Object dequeue() {
        assert size > 0;

        size--;
        try {
            return elements[head];
        } finally {
            head = (head + 1) % elements.length;
        }
    }

    // pre: size > 0
    // post: ℝ = a[tail - 1] ∧ (new)size = size − 1 ∧ (new)tail = tail - 1 ∧
    // ∀i=[head; tail) ∧ ∀i=[0; tail) ∪ [head; elements.length) : (new)a[i] = a[i]
    public Object remove() {
        assert size > 0;

        size--;
        tail = (tail + elements.length - 1) % elements.length;
        return elements[tail];
    }

    // pre: size > 0
    // post: ℝ = a[head] ∧ size = (new)size ∧ head = (new)head ∧ (new)elements = elements
    public Object element() {
        assert size > 0;

        return elements[head];
    }

    // pre: size > 0
    // post: ℝ = a[(previous)tail] ∧ size = (new)size ∧ tail = (new)tail ∧ (new)elements = elements
    public Object peek() {
        assert size > 0;

        int positionOfLastElement = (tail + elements.length - 1) % elements.length;
        return elements[positionOfLastElement];
    }

    // post: ℝ = size ∧ (new)size = size ∧ (new)elements = elements
    public int size() {
        return size;
    }

    // post: ℝ = (size > 0) ∧ size = (new)size ∧ (new)elements = elements
    public boolean isEmpty() {
        return size == 0;
    }

    // post: size = 0 ∧ tail = 0 ∧ head = 0
    public void clear() {
        size = 0;
        tail = 0;
        head = 0;
    }
}