// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The RouteComponent class is an abstract base class in the Composite design pattern hierarchy.
// It represents a general component of a bus route, which can be either a single bus stop or a collection of stops.

package bustracker;
//Base class composite hierarchy
public abstract class RouteComponent {
    protected String name;
    protected int dwellMinutes;
    public RouteComponent(String name, int minutes) {
        this.name = name;
        dwellMinutes = minutes;
    }
    public String toString() {
        return name + "(" + dwellMinutes + "min)";
    }
    public abstract int getTotalDwellMinutes();
}
