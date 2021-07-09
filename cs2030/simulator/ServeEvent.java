/**
 * The ServeEvent class takes in a customer, server
 * and time customer is served. It transitions to a
 * DoneEvent. It extends from the abstract Event class.
 * 
 * @author Meryl Seow
 */

package cs2030.simulator;

public class ServeEvent extends Event {
    
    /** This creates a new ServeEvent object.
     * @param customer customer
     * @param server serving server
     * @param time time customer is served
     */
    public ServeEvent(Customer customer, Server server, double time) {

        /** Function updates next available time after serving, updates
         * the serving server and shop and generates a pair of Shop
         * and DoneEvent.
         */
        super(customer, server, time, (shop) -> {
            // Compute next available time after serving
            double nextAvailableTime = time + shop.genCustomerServiceTime();
            // Create a new server and shop representing updated state
            Server updatedServer = new Server(server.getId(), false, 
                                              server.hasWaitingCustomer(), nextAvailableTime,
                                              server.getFirstSelfCheckout());
            Shop updatedShop = shop.replace(updatedServer)
                                   .recordServedCustomer(time - customer.getArrivalTime());
            return new Pair<Shop, Event>(
                updatedShop, new DoneEvent(customer, updatedServer, nextAvailableTime));
        });
    }

    @Override
    public String toString() {
        return String.format("%.3f %s served by %s", super.getTime(),
                             super.getCustomerString(),
                             super.getServerString(false));
    }
}
