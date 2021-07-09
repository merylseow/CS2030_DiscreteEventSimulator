/**
* The DoneEvent class takes in a customer, server
* and the time service ends. It extends from the
* abstract Event class.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class DoneEvent extends Event {
    
    /** This creates a new DoneEvent object.
     * @param customer customer
     * @param server serving server
     * @param time time service ends
     */
    public DoneEvent(Customer customer, Server server, double time) {

        /** Function generates a pair of Shop and ServerRestEvent if
         * server rests, else it updates server as available and generates
         * a null Event since there are no succeeding events after DoneEvent.
         */
        super(customer, server, time, (shop) -> {
            // If server rests, create event to indicate so
            if (shop.shouldServerRest(server)) {
                return new Pair<Shop, Event>(shop, new ServerRestEvent(server, time));
            // Otherwise, mark current done server as available now
            } else {
                Server updatedServer = new Server(server.getId(), true, 
                                                  server.hasWaitingCustomer(), time,
                                                  server.getFirstSelfCheckout());
                return new Pair<Shop, Event>(shop.replace(updatedServer), null);
            }
        });
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done serving by %s", super.getTime(),
                             super.getCustomerString(),
                             super.getServerString(false));
    }
}
