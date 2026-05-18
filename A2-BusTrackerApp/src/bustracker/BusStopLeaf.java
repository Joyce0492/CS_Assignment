// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The BusStopLeaf class represents a leaf node in the Composite design pattern hierarchy for bus routes.
//It models an individual bus stop that does not contain any child components. Each BusStopLeaf stores its own name and dwell
// time (minutes spent at the stop), and provides access to this information for route calculations or display.

package bustracker;

//Composite hierarchy leaf
public class BusStopLeaf extends RouteComponent {
    public BusStopLeaf(String name, int minutes) {
        super(name, minutes);
    }
    public String getName() {
        return name;
    }
    public int getTotalDwellMinutes() {
        return dwellMinutes;
    }
}
