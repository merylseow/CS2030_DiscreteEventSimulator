/**
* The Server class represents a server who
* can serve one customer at a time, and shows
* whether the server is available, has waiting
* customers, the next available time and the
* index of the first self-checkout counter.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class Server {

    private final int id;
    private final boolean isServerAvailable;
    private final boolean serverHasWaitingCustomer;
    private final double nextAvailableTime;
    private final Integer firstSelfCheckout;

    /** This creates a new Server object.
     * @param id server ID
     * @param isAvailable server is available to serve
     * @param hasWaitingCustomer server has waiting customers
     * @param nextAvailableTime server's next available time
     */
    public Server(int id, boolean isAvailable, boolean hasWaitingCustomer,
        double nextAvailableTime) {
        this.id = id;
        this.isServerAvailable = isAvailable;
        this.serverHasWaitingCustomer = hasWaitingCustomer;
        this.nextAvailableTime = nextAvailableTime;
        this.firstSelfCheckout = null;
    }

    /** This creates a new Server object.
     * @param id server ID
     * @param isAvailable server is available to serve
     * @param hasWaitingCustomer server has waiting customers
     * @param nextAvailableTime server's next available time
     * @param firstSelfCheckout index of first self-checkout
     */
    public Server(int id, boolean isAvailable, boolean hasWaitingCustomer,
                  double nextAvailableTime, Integer firstSelfCheckout) {
        this.id = id;
        this.isServerAvailable = isAvailable;
        this.serverHasWaitingCustomer = hasWaitingCustomer;
        this.nextAvailableTime = nextAvailableTime;
        this.firstSelfCheckout = firstSelfCheckout;
    }

    public int getId() {
        return this.id;
    }

    public boolean isAvailable() {
        return this.isServerAvailable;
    }
    
    public boolean hasWaitingCustomer() {
        return this.serverHasWaitingCustomer;
    }

    public double getNextAvailableTime() {
        return this.nextAvailableTime;
    }

    public Integer getFirstSelfCheckout() {
        return this.firstSelfCheckout;
    }

    public boolean isSelfCheckout() {
        return this.firstSelfCheckout != null;
    }

    @Override
    public String toString() {
        if (this.isAvailable()) {
            return this.id + " is available";
        } else if (!this.hasWaitingCustomer()) {
            return this.id + " is busy; available at " + 
                   String.format("%.3f", this.nextAvailableTime);   
        } else {
            return this.id + " is busy; waiting customer to be served at " + 
                   String.format("%.3f", this.nextAvailableTime);
        }
    }
}