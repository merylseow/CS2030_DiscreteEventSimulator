/**
* The Shop class holds all the servers. Uses class
* RandomGenerator to generate random values to be
* used in the simulation.
*
* @author Meryl Seow
*/

package cs2030.simulator;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shop {
    
    private final List<Server> serverList;
    private final RandomGenerator rng;
    private final double restingProbability;
    private final double greedyCustomerProbability;
    private final int numServers;
    private final int numSelfCheckout;
    private final int maxQueueSize;
    private final List<Queue<Customer>> queues;
    private final int numCustomersServed;
    private final double totalWaitingTime;

    /** This creates a new Shop object.
     * @param serverList list of servers
     */
    public Shop(List<Server> serverList) {
        this.serverList = serverList;
        this.rng = null;
        this.restingProbability = 0.0;
        this.greedyCustomerProbability = 0.0;
        this.numServers = serverList.size();
        this.numSelfCheckout = 0;
        this.maxQueueSize = 1;
        // For the queues, need to check on whether there are waiting customers
        this.queues = serverList.stream().map(server -> {
            // create a Queue for every server
            Queue<Customer> serverQueue = new LinkedList<Customer>();
            if (server.hasWaitingCustomer()) {
                serverQueue.add(new Customer(0, 0.0));
            }
            return serverQueue;
        }).collect(Collectors.toList());
        this.numCustomersServed = 0;
        this.totalWaitingTime = 0.0;
    }

    /** This creates a new Shop object.
     * @param numServers no. of servers
     */
    public Shop(int numServers) {
        this.serverList = Stream.iterate(1, i -> i + 1).limit(numServers)
        .map(i -> new Server(i, true, false, 0.0))
        .collect(Collectors.toList());
        this.rng = null;
        this.restingProbability = 0.0;
        this.greedyCustomerProbability = 0.0;
        this.numServers = numServers;
        this.numSelfCheckout = 0;
        this.maxQueueSize = 1;
        this.queues = Stream.iterate(0, i -> i).limit(numServers + 1)
        .map(i -> new LinkedList<Customer>()).collect(Collectors.toList());
        this.numCustomersServed = 0;
        this.totalWaitingTime = 0.0;
    }
    
    /** This creates a new Shop object.
     * @param numCustomers no. of customers
     * @param seed base seed for the RandomGenerator object
     * @param numServers no. of servers
     * @param numSelfCheckout no. of self-checkout counters
     * @param maxQueueSize max queue length
     * @param arrivalRate arrival rate
     * @param serviceRate service rate
     * @param restingRate resting rate
     * @param restingProbability probability of resting
     * @param greedyCustomerProbability probability of greedy customer occurring
     */
    public Shop(int numCustomers, int seed, int numServers, int numSelfCheckout, int maxQueueSize,
                double arrivalRate, double serviceRate, double restingRate,
                double restingProbability, double greedyCustomerProbability) {
        // Number of entities to serve customers
        this.numServers = numServers;
        this.numSelfCheckout = numSelfCheckout;
        this.maxQueueSize = maxQueueSize;
        // Generate all server IDs (available at time 0.0 with no waiting customers)
        this.serverList = Stream.iterate(1, i -> i + 1)
        .limit(numServers + numSelfCheckout)
        .map(i -> {
            Integer firstSelfCheckout = null;
            // get index of first self-checkout counter
            if (i > numServers) {
                firstSelfCheckout = numServers + 1;
            }
            return new Server(i, true, false, 0.0, firstSelfCheckout);
        })
        .collect(Collectors.toList());
        // Create waiting queues in the shop for all entities (+1 for self-checkouts)
        this.queues = Stream.iterate(0, i -> i).limit(numServers + 1)
        .map(i -> new LinkedList<Customer>()).collect(Collectors.toList());
        // Specify the rng server and probabilities
        this.rng = new RandomGenerator(seed, arrivalRate, serviceRate, restingRate);
        this.restingProbability = restingProbability;
        this.greedyCustomerProbability = greedyCustomerProbability;
        // Statistics initialization
        this.numCustomersServed = 0;
        this.totalWaitingTime = 0.0;
    }
    
    /** This creates a new Shop object.
     * @param numServers no. of servers
     * @param numSelfCheckout no. of self-checkout counters
     * @param maxQueueSize max queue length
     * @param rng RandomGenerator object
     * @param restingProbability probability of resting
     * @param greedyCustomerProbability probability of greedy customer occurring
     * @param serverList list of servers
     * @param queues list of waiting queues
     * @param numCustomersServed no. of customers served
     * @param totalWaitingTime total waiting time
     */
    private Shop(int numServers, int numSelfCheckout, int maxQueueSize,
                 RandomGenerator rng, double restingProbability, double greedyCustomerProbability, 
                 List<Server> serverList, List<Queue<Customer>> queues,
                 int numCustomersServed, double totalWaitingTime) {
        this.numServers = numServers;
        this.numSelfCheckout = numSelfCheckout;
        this.maxQueueSize = maxQueueSize;
        this.rng = rng;
        this.restingProbability = restingProbability;
        this.greedyCustomerProbability = greedyCustomerProbability;
        this.serverList = serverList;
        this.queues = queues;
        this.numCustomersServed = numCustomersServed;
        this.totalWaitingTime = totalWaitingTime;
    }

    public double genCustomerInterArrivalTime() {
        return this.rng.genInterArrivalTime();
    }

    public double genCustomerServiceTime() {
        return this.rng.genServiceTime();
    }

    public double genServerRestTime() {
        return this.rng.genRestPeriod();
    }

    public boolean genIsCustomerGreedy() {
        return this.rng.genCustomerType() < this.greedyCustomerProbability;
    }

    /** Returns true if random value generated is less than resting probability.
     */
    public boolean shouldServerRest(Server server) {
        // Ignore for self-checkout counters
        return !server.isSelfCheckout() && 
               this.rng.genRandomRest() < this.restingProbability;
    }

    /** Returns the waiting queue of the server.
     */
    public Queue<Customer> getWaitingCustomers(int serverId) {
        if (serverId <= numServers) {
            return this.queues.get(serverId - 1);
        } else {
            return this.queues.get(this.numServers);
        }
    }

    public int getNumServers() {
        return this.numServers;
    }

    public List<Server> getServerList() {
        return this.serverList;
    }

    public int getMaxQueueSize() {
        return this.maxQueueSize;
    }

    public int getNumCustomersServed() {
        return this.numCustomersServed;
    }

    public double getTotalWaitingTime() {
        return this.totalWaitingTime;
    }

    /** Finds the first instance in the serverList where the predicate
     * is true and returns the Server wrapped in an Optional.
     */
    public Optional<Server> find(Predicate<Server> pred) {
        return this.serverList.stream()
        .filter(pred)
        .findFirst();
    }

    /** Finds the shortest queue for a greedy customer, and returns the
     * server with the shortest queue.
     */
    public Optional<Server> findGreedy() {
        return Optional.ofNullable(this.serverList.stream()
        .reduce(null, (bestServer, server) -> {
            // Ignore any server with a full queue
            int serverQueueSize = this.getWaitingCustomers(server.getId()).size();
            if (this.getWaitingCustomers(server.getId()).size() >= this.maxQueueSize) {
                return bestServer;
            // Use current server if we have not found one yet
            } else if (bestServer == null) {
                return server;
            // Attempt to find server with lowest Id with minimum queue length
            } else {
                int bestServerQueueSize = this.getWaitingCustomers(bestServer.getId()).size();
                if (serverQueueSize < bestServerQueueSize) {
                    return server;
                } else if (serverQueueSize > bestServerQueueSize) {
                    return bestServer;
                } else if (server.getId() < bestServer.getId()) {
                    return server;
                } else {
                    return bestServer;
                }
            }
        }));
    }

    /** Replaces current server with an updated server, and
     * returns a Shop with the updated server.
     */
    public Shop replace(Server updatedServer) {
        List<Server> updatedList = this.serverList.stream()
            .map(server -> {
                if (updatedServer.getId() == server.getId()) {
                    return updatedServer;
                } else {
                    return server;
                }
            }).collect(Collectors.toList());
        return new Shop(this.numServers, this.numSelfCheckout, this.maxQueueSize,
                        this.rng, this.restingProbability, this.greedyCustomerProbability,
                        updatedList, this.queues, this.numCustomersServed, this.totalWaitingTime);
    }

    /** Updates the total waiting time and returns a new Shop with the
     * updated time.
     */
    public Shop recordServedCustomer(double customerWaitingTime) {
        return new Shop(this.numServers, this.numSelfCheckout, this.maxQueueSize,
                        this.rng, this.restingProbability, this.greedyCustomerProbability,
                        this.serverList, this.queues, this.numCustomersServed + 1,
                        this.totalWaitingTime + customerWaitingTime);
    }

    @Override
    public String toString() {
        return this.serverList.toString();
    }
}
