package gh2;

import deque.Deque;
import deque.LinkedListDeque;

public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    private Deque<Double> buffer;

    public GuitarString(double frequency) {
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < (int) Math.round(SR / frequency); i++) {
            buffer.addFirst(0.0);
        }

    }

    public void pluck() {
        for (int i = 0; i < buffer.size(); i++) {
            double r = Math.random() - 0.5;
            buffer.removeLast();
            buffer.addFirst(r);
        }
    }


    public void tic() {
        double first = buffer.removeFirst();
        double newItem = (buffer.get(0) + first) / 2.0 * 0.996;
        buffer.addLast(newItem);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }


}

