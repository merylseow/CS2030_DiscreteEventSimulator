/**
 * The ArriveEvent class takes in an arriving customer,
 * and generates a Shop and either a ServeEvent, WaitEvent
 * or DoneEvent. It extends from the abstract Event class.
 * 
 * @author Meryl Seow
 */

package cs2030.simulator;

import java.util.Optional;

public class ArriveEvent extends Event {

    /** This creates a new ArriveEvent object.
     * @param customer customer
     */
    public ArriveEvent(Customer customer) {

        /** Function generates a pair of Shop and Event depending on
         * whether there are available servers and waiting customers
         * which will decide whether the Event generated is a
         * ServeEvent, WaitEvent or LeaveEvent.
         */
        super(customer, null, customer.getArrivalTime(), (shop) -> {
            // If a server is available, serve the customer
            Optional<Server> possibleServer = shop.find(s -> s.isAvailable());
            if (possibleServer.isPresent()) {
                return new Pair<Shop, Event>(shop, 
                    new ServeEvent(customer, possibleServer.get(), customer.getArrivalTime()));
            }
            // If no server is available, determine where to wait
            possibleServer = shop.find(s -> 
            shop.getWaitingCustomers(s.getId()).size() < shop.getMaxQueueSize());
            // If customer is greedy, pick using different method
            if (customer.isGreedy()) {
                possibleServer = shop.findGreedy();
            }
            // If we can wait at a server, choose to wait
            if (possibleServer.isPresent()) {
                return new Pair<Shop, Event>(shop,
                    new WaitEvent(customer, possibleServer.get(), customer.getArrivalTime()));
            // Otherwise, choose to leave
            } else {
                return new Pair<Shop, Event>(shop,
                new LeaveEvent(customer, customer.getArrivalTime()));
            }
        });
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", super.getTime(), super.getCustomerString());
    }
}
