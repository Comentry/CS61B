package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        first = 0;
        last = -1;
    }

    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int index = 0;
        for (int i = first; i != (last + items.length) % items.length; i = (i + 1) % items.length) {
            newArray[index++] = items[i];
        }
        newArray[index] = items[(last + items.length) % items.length];
        first = 0;
        last = index;
        items = newArray;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        first = (first - 1 + items.length) % items.length;
        items[first] = item;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        last = (last + 1) % items.length;
        items[last] = item;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = first; i != (last + items.length) % items.length; i = (i + 1) % items.length) {
            System.out.print(items[i]);
            System.out.println(' ');
        }
        System.out.println(items[(last + items.length) % items.length]);
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T firstItem = items[first];
        items[first] = null;
        first = (first + 1) % items.length;
        size -= 1;
        return firstItem;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = (last + items.length) % items.length;
        T lastItem = items[lastIndex];
        items[lastIndex] = null;
        last = (last - 1 + items.length) % items.length;
        size -= 1;
        return lastItem;
    }

    @Override
    public T get(int index) {
        if (size <= index) {
            return null;
        }
        int realIndex = (first + index) % items.length;
        return items[realIndex];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        ArrayDeque other = (ArrayDeque) o;
        if (this.size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {

        private int index;

        public ArrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return get(index) != null;
        }

        @Override
        public T next() {
            T returnItem = get(index);
            index += 1;
            return returnItem;
        }
    }
}

