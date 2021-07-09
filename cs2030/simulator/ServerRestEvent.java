/**
* The ServerRestEvent class represents a server
* taking a break. It extends from the abstract
* Event class.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class ServerRestEvent extends Event {
    
    /** This creates a new ServerRestEvent object.
     * @param server resting server
     * @param time time server starts to rest
     */
    public ServerRestEvent(Server server, double time) {

        /** Function generates a pair of Shop and ServerBackEvent.
         */
        super(null, server, time, (shop) -> {
            // Queue a server back event
            return new Pair<Shop, Event>(shop,
                new ServerBackEvent(server, time + shop.genServerRestTime()));
        });
    }

    @Override
    public String toString() {
        return String.format("%.3f", super.getTime()) + " " +
            super.getServerString(false) + " rests";
    }
}
