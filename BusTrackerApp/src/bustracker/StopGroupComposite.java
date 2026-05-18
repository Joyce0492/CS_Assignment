// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The StopGroupComposite class represents a composite node in the Composite design pattern hierarchy.
// It models a group of bus stops or a hub that contains multiple RouteComponent objects.

package bustracker;
import java.util.*;
//Composite stop group or hub
public class StopGroupComposite extends RouteComponent {
    private List<RouteComponent> children = new ArrayList<>();
    public StopGroupComposite(String name, int minutes) {
        super(name, minutes);
    }
    public void add(RouteComponent c) {
        children.add(c);
    }
    public void remove(RouteComponent c) {
        children.remove(c);
    }
    public int getTotalDwellMinutes() {
        int mins = this.dwellMinutes;
        for (RouteComponent c : children) {
            mins += c.getTotalDwellMinutes();
        }
        return mins;
    }

    @Override
    public String toString() {
        return String.format("%s(%dmin) %s", name, dwellMinutes, children);
    }
}
