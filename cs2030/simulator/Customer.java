/**
* The Customer class represents a Customer who has a customer ID,
* arrival time and boolean flag isCustomerGreedy.
*
* @author Meryl Seow
*/

package cs2030.simulator;

public class Customer {
    
    private final int id;
    private final double arrivalTime;
    private final boolean isCustomerGreedy;

    /** This creates a new Customer object.
     * @param id customer ID
     * @param arrivalTime customer's arrival time
     */
    public Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.isCustomerGreedy = false;
    }

    /** This creates a new Customer object.
     * @param id customer ID
     * @param arrivalTime customer's arrival time
     * @param isCustomerGreedy is customer greedy
     */
    public Customer(int id, double arrivalTime, boolean isCustomerGreedy) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.isCustomerGreedy = isCustomerGreedy;
    }

    public int getId() {
        return this.id;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public boolean isGreedy() {
        return this.isCustomerGreedy;
    }

    @Override
    public String toString() {
        return this.getId() + " arrives at " + String.format("%.1f", this.arrivalTime);
    }
}
