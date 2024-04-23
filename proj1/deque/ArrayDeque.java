package deque;

public class ArrayDeque<Item> {
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

    public void addFirst(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        first = (first - 1 + items.length) % items.length;
        items[first] = item;
        size += 1;
    }

    public void addLast(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        last = (last + 1) % items.length;
        items[last] = item;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = first; i != last; i = (i + 1) % items.length) {
            System.out.print(items[i]);
            System.out.println(' ');
        }
        System.out.println(items[last]);
    }

    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item firstItem = items[first];
        first = (first + 1) % items.length;
        size -= 1;
        return firstItem;
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item lastItem = items[last];
        last = (last - 1 + items.length) % items.length;
        size -= 1;
        return lastItem;
    }

    public Item get(int index) {
        if (size <= index) {
            return null;
        }
        int realIndex = (first + index) % items.length;
        return items[realIndex];
    }


}
