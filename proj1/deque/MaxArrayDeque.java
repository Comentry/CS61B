package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    public Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> comparator) {
        this.comparator=comparator;
    }

    public T max() {
        T maxItem = this.get(0);
        for(T item:this) {
            if(comparator.compare(maxItem,item)<0) {
                maxItem=item;
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> comparator) {
        T maxItem = this.get(0);
        for(T item:this) {
            if(comparator.compare(maxItem,item)<0) {
                maxItem=item;
            }
        }
        return maxItem;
    }
}
