// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The BusTracker class represents an Observable component in the Observer design pattern,
// responsible for tracking a specific bus and notifying subscribed observers about its arrival updates.

package bustracker;
import java.util.*;
//Observable bus tracker
public class BusTracker extends Observable {
    private Bus bus;
    public BusTracker(Bus bus) {
        this.bus = bus;
    }
    public Bus getBus() {
        return bus;
    }
    public void notifyArrival(BusStop bs, int etaMinutes) {
        setChanged();
        String message = bus.getBusNumber() +" arriving at " + bs.toString() + " in " + etaMinutes + " mins";
        notifyObservers(message);
    }
}
