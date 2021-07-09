/**
* The WaitEvent class takes in a waiting customer,
* server and time customer starts waiting. It extends
* from the abstract Event class.
* 
* @author Meryl Seow
*/

package cs2030.simulator;

public class WaitEvent extends Event {

    /** This creates a new WaitEvent object.
     * @param customer waiting customer
     * @param server serving server
     * @param time time customer starts waiting
     */
    public WaitEvent(Customer customer, Server server, double time) {

        /** Function adds customer to the queue of the serving
         * server, updates the serving server and shop. Generates a
         * pair of Shop and null Event.
         */
        super(customer, server, time, shop -> {
            // Add customer to queue of the serving server
            shop.getWaitingCustomers(server.getId()).add(customer);
            // Refresh server state accordingly
            Server updatedServer = new Server(server.getId(), server.isAvailable(),
                                              false, server.getNextAvailableTime(),
                                              server.getFirstSelfCheckout());
            // No events occur as a result of waiting
            return new Pair<Shop, Event>(shop.replace(updatedServer), null);
        });
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits to be served by %s",
                            super.getCustomer().getArrivalTime(),
                            super.getCustomerString(),
                            super.getServerString(true));
    }
}
