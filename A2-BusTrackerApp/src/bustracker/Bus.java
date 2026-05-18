// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The Bus class represents a single bus in the bus tracking system.
// It stores the bus number, the assigned driver, and the list of bus stops along its route.

package bustracker;
import java.util.*;

public class Bus {
    private String busNumber = "500";
    private Driver driver;
    private ArrayList<BusStop> busStops = new ArrayList<>();
    public Bus(String bn, Driver d) {
        busNumber = bn;
        if (d.getBuses().size() == Driver.MAX_BUSES) throw new IllegalArgumentException("Cannot assign more than " + Driver.MAX_BUSES + " buses.");
        driver = d;
        this.assignDriver(driver);
    }
    public String getBusNumber() {
        return busNumber;
    }
    public Driver getDriver() {
        return driver;
    }
    public void assignDriver(Driver d) {
        driver = d;
        driver.allocate(this);
    }
    public ArrayList<BusStop> getStops() {
        return busStops;
    }
    public void addStop(BusStop bs) {
        busStops.add(bs);
    }
    public void setBusStops(ArrayList<BusStop> bs) {
        busStops = bs;
    }
    public String toString() {
        return busNumber + "(" + driver.getName() + ")";
    }
}