/**
* The LeaveEvent class takes in a customer and
* and time customer leaves. It extends from the
* abstract Event class.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class LeaveEvent extends Event {
    
    /** This creates a new LeaveEvent object.
     * @param customer customer
     * @param time time customer leaves
     */
    public LeaveEvent(Customer customer, double time) {

        /** Function generates a pair of Shope and null Event as
         * there are no suceeding events after LeaveEvent.
         */
        super(customer, null, time, shop -> new Pair<Shop, Event>(shop, null));
    }

    @Override
    public String toString() {
        return String.format("%.3f", super.getTime()) + " " + super.getCustomerString() + " leaves";
    }
}
