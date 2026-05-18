// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The TransportManager class acts as a controller that interacts with assignable buses.
// It handles assigning drivers to buses and generating reports, such as earnings per route.

package bustracker;

import java.util.ArrayList;
import java.util.List;

public class TransportManager {
    private final List<Assignable> assignables = new ArrayList<>();

    public String reportEarnings(BusAssignable bus) {
        return "Route " + bus.getRoute() + " earned $" + bus.calculateEarnings();
    }

    public TransportManager() {
        // Default constructor - no specific assignable required
    }

    public void addAssignable(Assignable assignable) {
        assignables.add(assignable);
    }

    public void allocate(String driverName) {
        for (Assignable assignable : assignables) {
            assignable.assignDriver(driverName);
        }
    }
    
    public void allocateToSpecific(Assignable assignable, String driverName) {
        assignable.assignDriver(driverName);
    }
}
