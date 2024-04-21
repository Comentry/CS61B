package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();
        for(int i=1;i<=3;i++) {
            expected.addLast(i*10);
            actual.addLast(i*10);
        }
        assertEquals(expected.size(),actual.size());
        assertEquals(expected.removeLast(),actual.removeLast());
        assertEquals(expected.removeLast(),actual.removeLast());
        assertEquals(expected.removeLast(),actual.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> BL = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                BL.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(L.size(),BL.size());
            } else if(operationNumber==2){
                // removeLast
                if(L.size()>0) {
                    assertEquals(L.removeLast(),BL.removeLast());
                }
            } else {
                if(L.size()>0) {
                    assertEquals(L.getLast(),BL.getLast());
                }
            }
        }
    }
}
