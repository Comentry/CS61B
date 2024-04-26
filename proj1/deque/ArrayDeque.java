package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Iterable<Item>, Deque<Item> {
    private Item[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        first = 0;
        last = -1;
    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        int index = 0;
        for (int i = first; i != last; i = (i + 1) % items.length) {
            newArray[index++] = items[i];
        }
        newArray[index] = items[last];
        first = 0;
        last = index;
        items = newArray;
    }

    @Override
    public void addFirst(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        first = (first - 1 + items.length) % items.length;
        items[first] = item;
        size += 1;
    }

    @Override
    public void addLast(Item item) {
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
        for (int i = first; i != last; i = (i + 1) % items.length) {
            System.out.print(items[i]);
            System.out.println(' ');
        }
        System.out.println(items[last]);
    }

    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item firstItem = items[first];
        items[first] = null;
        first = (first + 1) % items.length;
        size -= 1;
        return firstItem;
    }

    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = (last + items.length) % items.length;
        Item lastItem = items[lastIndex];
        items[lastIndex] = null;
        last = (last - 1 + items.length) % items.length;
        size -= 1;
        return lastItem;
    }

    @Override
    public Item get(int index) {
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
        if (o instanceof ArrayDeque otherArrayDeque) {
            if (this.size != otherArrayDeque.size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (this.get(i) != otherArrayDeque.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Iterator<Item> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<Item> {

        private int index;

        public ArrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return get(index) != null;
        }

        @Override
        public Item next() {
            Item returnItem = get(index);
            index += 1;
            return returnItem;
        }
    }
}

