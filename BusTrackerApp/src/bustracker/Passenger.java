// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The Passenger class represents an observer in the Observer design pattern that listens for updates
// from a BusTracker. It models a passenger who receives real-time notifications about bus arrivals, delays, or other status changes.

package bustracker;

import java.util.Observable;
import java.util.Observer;
//Passenger observer
@SuppressWarnings("deprecation")
public class Passenger implements Observer {
    private BusTracker tracker;
    public Passenger(BusTracker tracker) {
        this.tracker = tracker;
    }
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        System.out.println(message);
    }
}