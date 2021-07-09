/** EventComparator takes in 2 Events and sorts them 
* from earliest to latest arrival time. If same arrival
* time, sort by customer ID.
*
* @author Meryl Seow
*/

package cs2030.simulator;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {

    /**
     * Returns a negative int if o1.startTime < o2.startTime, a positive
     * int if o1.startTime > o2.startTime. If both are equal, return a
     * negative integer for the smaller customer ID.
     * The earliest events are inserted first into the priority queue.
     * @param event1 first event to compare
     * @param event2 second event to compare to the first
     * @return an int to determine the event's position in the queue
     */

    @Override
    public int compare(Event event1, Event event2) {
        if (event1.getTime() < event2.getTime()) {
            return -1;
        } else if (event1.getTime() > event2.getTime()) {
            return 1;
        } else {
            return event1.getCustomer().getId() - event2.getCustomer().getId();
        }
    }
}
