package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private Node head;

    private Node tail;

    protected void enqueueImpl(Object element) {
        Node previousTail = tail;
        tail = new Node(element, null);
        if (isEmpty()) {
            head = tail;
        } else {
            previousTail.next = tail;
        }
    }

    protected void remove() {
        head = head.next;
    }

    protected Object elementImpl() {
        return head.value;
    }

    private class Node {
        private Object value;
        private Node next;

        Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

    protected void clearImpl() {
        head = null;
        tail = null;
    }

    public LinkedQueue makeCopy() {
        final LinkedQueue copy = new LinkedQueue();
        copy.size = size;
        copy.head = head;
        copy.tail = tail;
        return copy;
    }

    public LinkedQueue filter(Predicate<Object> condition) {
        final LinkedQueue filtered = new LinkedQueue();
        Node current = head;
        while (current != null) {
            if (condition.test(current.value)) {
                filtered.enqueue(current.value);
            }
            current = current.next;
        }
        return filtered;
    }
    public LinkedQueue map(Function<Object, Object> function) {
        final LinkedQueue mapped = new LinkedQueue();
        Node current = head;
        while (current != null) {
            mapped.enqueue(function.apply(current.value));
            current = current.next;
        }
        return mapped;
    }
}
