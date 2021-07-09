/**
* The abstract Event class is the parent class
* of the 7 sub-classes: ArriveEvent, WaitEvent,
* ServeEvent, LeaveEvent, DoneEvent, ServerRestEvent
* and ServerBackEvent.
*
* @author Meryl Seow
*/

package cs2030.simulator;

import java.util.function.Function;

public abstract class Event {
    private final Customer customer;
    private final Server server;
    private final double time;
    private final Function<Shop, Pair<Shop, Event>> func;

    /** This creates a new Event object.
     * @param customer customer
     * @param server server
     * @param time start time of event
     * @param func functionality of the event
     */
    public Event(Customer customer, Server server, double time,
        Function<Shop, Pair<Shop, Event>> func) {
        this.customer = customer;
        this.server = server;
        this.time = time;
        this.func = func;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    /** If customer is greedy, update customer's string.
     */
    public String getCustomerString() {
        if (this.customer.isGreedy()) {
            return this.customer.getId() + "(greedy)";
        } else {
            return String.valueOf(this.customer.getId());
        }
    }

    public Server getServer() {
        return this.server;
    }

    /** If server is self-checkout, update server's string.
     */
    public String getServerString(boolean isWaitEvent) {
        if (this.server.isSelfCheckout()) {
            if (isWaitEvent) {
                return "self-check " + this.server.getFirstSelfCheckout();
            } else {
                return "self-check " + this.server.getId();
            }
        } else {
            return "server " + this.server.getId();
        }
    }

    public double getTime() {
        return this.time;
    }

    public final Pair<Shop, Event> execute(Shop shop) {
        return this.func.apply(shop);
    }
}
