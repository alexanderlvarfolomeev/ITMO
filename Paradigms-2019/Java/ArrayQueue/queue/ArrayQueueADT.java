package queue;

public class ArrayQueueADT {
    private int tail;
    private int head;
    private int size;
    private Object[] elements = new Object[10];

    // pre: element ≠ null
    // post: (new)size = size + 1 ∧ (new)tail = tail + 1 ∧
    // ∀i=[head; tail) ∧ [0; tail) ∪ [head; elements.length) : (new)a[i] = a[i] ∧ a[tail - 1] = element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = element;
        queue.size++;
        queue.tail = (queue.tail + 1) % queue.elements.length;
    }

    // pre: element ≠ null
    // post: (new)size = size + 1 ∧ (new)head = head - 1 ∧
    // ∀i=[head; tail) ∧ [0; tail) ∪ [head; elements.length) : (new)a[i] = a[i] ∧ a[(new)head] = element
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, queue.size + 1);
        queue.head = (queue.head + queue.elements.length - 1) % queue.elements.length;
        queue.elements[queue.head] = element;
        queue.size++;
    }

    static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity <= queue.elements.length) {
            return;
        }
        Object[] newElements = new Object[2 * capacity];
        if (queue.head < queue.tail) {
            System.arraycopy(queue.elements, queue.head, newElements, 0, queue.size);
        } else {
            System.arraycopy(queue.elements, queue.head,
                    newElements, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0,
                    newElements, queue.elements.length - queue.head, queue.tail);
        }
        queue.elements = newElements;
        queue.head = 0;
        queue.tail = queue.size;
    }

    // pre: size > 0
    // post: ℝ = a[head] ∧ (new)size = size − 1 ∧ (new)head = head + 1 ∧
    // ∀i=[head; tail) ∧ ∀i=[0; tail) ∪ [head; elements.length) : (new)a[i] = a[i]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;

        queue.size--;
        try {
            return queue.elements[queue.head];
        } finally {
            queue.head = (queue.head + 1) % queue.elements.length;
        }
    }

    // pre: size > 0
    // post: ℝ = a[tail - 1] ∧ (new)size = size − 1 ∧ (new)tail = tail - 1 ∧
    // ∀i=[head; tail) ∧ ∀i=[0; tail) ∪ [head; elements.length) : (new)a[i] = a[i]
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;

        queue.size--;
        queue.tail = (queue.tail + queue.elements.length - 1) % queue.elements.length;
        return queue.elements[queue.tail];
    }

    // pre: size > 0
    // post: ℝ = a[head] ∧ size = (new)size ∧ head = (new)head ∧ (new)elements = elements
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.head];
    }

    // pre: size > 0
    // post: ℝ = a[(previous)tail] ∧ size = (new)size ∧ tail = (new)tail ∧ (new)elements = elements
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;

        int positionOfLastElement = (queue.tail + queue.elements.length - 1) % queue.elements.length;
        return queue.elements[positionOfLastElement];
    }

    // post: ℝ = size ∧ (new)size = size ∧ (new)elements = elements
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // post: ℝ = (size > 0) ∧ size = (new)size ∧ (new)elements = elements
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // post: size = 0 ∧ tail = 0 ∧ head = 0
    public static void clear(ArrayQueueADT queue) {
        queue.size = 0;
        queue.head = 0;
        queue.tail = 0;
    }
}