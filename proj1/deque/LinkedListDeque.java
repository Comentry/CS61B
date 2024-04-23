package deque;

public class LinkedListDeque<T> {
    public Node sentinel;
    public int size;

    public class Node {
        public T item;
        public Node prev;
        public Node next;

        public Node(T item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }
    }

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        size += 1;
        Node node = new Node(item);
        node.prev = sentinel;
        node.next = sentinel.next;
        sentinel.next = node;
        node.next.prev = node;
    }

    public void addLast(T item) {
        size += 1;
        Node node = new Node(item);
        Node lastNode = sentinel.prev;
        node.prev = lastNode;
        node.next = sentinel;
        lastNode.next = node;
        sentinel.prev = node;
    }

    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item);
            System.out.print(' ');
            p = p.next;
        }

        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        Node node = sentinel.next;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return node.item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        Node node = sentinel.prev;
        node.prev.next = sentinel;
        sentinel.prev = node.prev;
        size -= 1;
        return node.item;
    }

    public T get(int index) {
        if (size <= index) {
            return null;
        }

        Node p = sentinel;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }

        return p.item;
    }

    public T getRecursive(int index) {
        if (size <= index) {
            return null;
        }
        return getRecursive(sentinel.next, index);
    }

    private T getRecursive(Node sentinel, int index) {
        if (index == 0) {
            return sentinel.item;
        }
        return getRecursive(sentinel.next, index - 1);
    }
}
