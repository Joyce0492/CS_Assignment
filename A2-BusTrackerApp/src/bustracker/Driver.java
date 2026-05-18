// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The Driver class represents a bus driver who can be assigned to multiple buses.
// It manages the association between a driver and the buses they operate.

package bustracker;
import java.util.*;

public class Driver {
    private String name;
    public static final int MAX_BUSES = 3;
    private ArrayList<Bus> buses = new ArrayList<Bus>();
    public Driver(String name) {
        this.name = name;
    }
    public boolean canAssign() {
        return buses.size() < MAX_BUSES;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Bus> getBuses() {
        return buses;
    }
    public void allocate(Bus b) {
        buses.add(b);
    }
    public String toString() {
        return "Driver:" + name + "\n" + buses.toString();
    }
}
