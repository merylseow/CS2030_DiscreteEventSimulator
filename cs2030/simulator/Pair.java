/**
* The Pair class represents a pair of
* Shop and Event.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class Pair<T, U> {

    private final T input1;
    private final U input2;

    /** This creates a new Pair object.
     * @param input1 input of type T
     * @param input2 input of type U
     */
    Pair(T input1, U input2) {
        this.input1 = input1;
        this.input2 = input2;
    }

    public static <T,U> Pair<T, U> of(T i1, U i2) {
        return new Pair<T, U>(i1, i2);
    }

    public T first() {
        return this.input1;
    }

    public U second() {
        return this.input2;
    }
    
}
