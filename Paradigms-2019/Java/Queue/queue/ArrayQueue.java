package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private int tail;

    private int head;

    private Object[] elements = new Object[10];

    protected void enqueueImpl(Object element) {
        if (size + 1 >= elements.length) {
            alignElements(2*size);
        }
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    private void alignElements(int capacity) {
        if (capacity == elements.length && tail == size) {
            return;
        }
        Object[] newElements = new Object[capacity];
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

    protected Object elementImpl() {
        return elements[head];
    }

    protected void remove() {
        head = (head + 1) % elements.length;
    }

    protected void clearImpl() {
        tail = 0;
        head = 0;
    }

    public ArrayQueue makeCopy() {
        final ArrayQueue copy = new ArrayQueue();
        alignElements(elements.length);
        copy.size = size;
        copy.head = head;
        copy.tail = tail;
        copy.elements = new Object[size];
        System.arraycopy(elements, 0, copy.elements, 0, size);
        return copy;
    }

    public ArrayQueue filter(Predicate<Object> condition) {
        final ArrayQueue filtered = new ArrayQueue();
        alignElements(elements.length);
        for (int i = 0; i < size; i++) {
            if (condition.test(elements[i])) {
                filtered.enqueue(elements[i]);
            }
        }

        return filtered;
    }

    public ArrayQueue map(Function<Object, Object> function) {
        final ArrayQueue mapped = new ArrayQueue();
        alignElements(elements.length);

        for (int i = 0; i < size; i++) {
            mapped.enqueue(function.apply(this.elements[i]));
            }

        return mapped;
    }
}