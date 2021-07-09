/**
* Main method reads a series of inputs by user
* and starts the simulation.
*
* @author Meryl Seow
*/

import cs2030.simulator.Customer;
import cs2030.simulator.Server;
import cs2030.simulator.Event;
import cs2030.simulator.ArriveEvent;
import cs2030.simulator.ServeEvent;
import cs2030.simulator.EventComparator;
import cs2030.simulator.Pair;
import cs2030.simulator.Shop;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {

    /**
     * Process all events in the priority queue and
     * compute average waiting time for those who have
     * been served, number of customers served and
     * number of customers who left without being served.
     */
    public static void main(String[] args) {
        int argsCounter = 0;
        int seed = Integer.parseInt(args[argsCounter++]);
        int numServers = Integer.parseInt(args[argsCounter++]);
        int numSelfCheckout = 0;
        if (args.length >= 9) {
            numSelfCheckout = Integer.parseInt(args[argsCounter++]);
        }
        int maxQueueSize = 1;
        if (args.length >= 6) {
            maxQueueSize = Integer.parseInt(args[argsCounter++]);
        }
        int numCustomers = Integer.parseInt(args[argsCounter++]);
        double arrivalRate = Double.parseDouble(args[argsCounter++]);
        double serviceRate = Double.parseDouble(args[argsCounter++]);
        double restingRate = 0.0;
        double restingProbability = 0.0;
        if (args.length >= 8) {
            restingRate = Double.parseDouble(args[argsCounter++]);
            restingProbability = Double.parseDouble(args[argsCounter++]);
        }
        double greedyCustomerProbability = 0.0;
        if (args.length >= 10) {
            greedyCustomerProbability = Double.parseDouble(args[argsCounter++]);
        }
        // Priority queue for event processing
        PriorityQueue<Event> queue = new PriorityQueue<>(new EventComparator());
        // Create a shop with all the servers (along with the RNG generator inside the shop)
        Shop shop = new Shop(numCustomers, seed, numServers, numSelfCheckout, maxQueueSize,
                             arrivalRate, serviceRate, restingRate,
                             restingProbability, greedyCustomerProbability);
        // Generate all arrival events for each customer
        double arrivalTime = 0.0;
        for (int i = 1; i <= numCustomers; i++) {
            queue.add(new ArriveEvent(new Customer(i, arrivalTime, shop.genIsCustomerGreedy())));
            arrivalTime += shop.genCustomerInterArrivalTime();
        }
        // Process all events in order of time
        while (!queue.isEmpty()) {
            // Obtain the next event
            Event currentEvent = queue.poll();
            Pair<Shop, Event> executionResult = currentEvent.execute(shop);
            // Display the current event if it is not server rest or server back
            if (!currentEvent.toString().contains("rests") &&
                !currentEvent.toString().contains("back")) {
                System.out.println(currentEvent);
            }
            // If there is a subsequent event, queue it for processing
            shop = executionResult.first();
            if (executionResult.second() != null) {
                queue.add(executionResult.second());
            // Otherwise, if it is a done or server back event, check for waiting customers
            } else if (currentEvent.toString().contains("done") ||
                       currentEvent.toString().contains("back")) {
                // Obtain server queue
                Server currServer = currentEvent.getServer();
                Queue<Customer> currServerQ = shop.getWaitingCustomers(currServer.getId());
                // If there is someone to serve
                if (!currServerQ.isEmpty()) {
                    // Update server details (next available time updated by serve event)
                    Server updatedServer = new Server(currServer.getId(), currServer.isAvailable(),
                                                      currServerQ.size() > 1,
                                                      currServer.getNextAvailableTime(),
                                                      currServer.getFirstSelfCheckout());
                    // Update the shop
                    shop = shop.replace(updatedServer);
                    // Add new serve event to the queue
                    queue.add(new ServeEvent(currServerQ.poll(), updatedServer,
                                             currentEvent.getTime()));
                }
            }
        }
        double averageWaitingtime = 0.0;
        int numServed = shop.getNumCustomersServed();
        double totalWaitTime = shop.getTotalWaitingTime();
        int numLeft = numCustomers - numServed;
        if (numServed > 0) {
            averageWaitingtime = totalWaitTime / (double) numServed;
        }
        System.out.printf("[%.3f %d %d]\n", averageWaitingtime,
                          shop.getNumCustomersServed(), numLeft);
    }
}