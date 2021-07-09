/**
* The ServerBackEvent class represents a server
* returning from a break. It extends from the
* abstract Event class.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class ServerBackEvent extends Event {
    
    /** This creates a new ServerBackEvent object.
     * @param server resting server
     * @param time time server returns from break
     */
    public ServerBackEvent(Server server, double time) {

        /** Function updates server as available, and
         * generates a pair of Shop and null Event.
         */
        super(null, server, time, (shop) -> {
            // Mark server that is back as available now
            Server updatedServer = new Server(server.getId(), true,
                                              server.hasWaitingCustomer(), time,
                                              server.getFirstSelfCheckout());
            return new Pair<Shop, Event>(shop.replace(updatedServer), null);
        });
    }

    @Override
    public String toString() {
        return String.format("%.3f", super.getTime()) + " " +
            super.getServerString(false) + " back";
    }
}
