// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The BusAssignable class represents a bus entity that can be assigned to a driver,
// implementing the Assignable interface. It stores essential operational information.

package bustracker;

public class BusAssignable implements Assignable {
    private String route;
    private int passengers;
    private double farePerPassenger;

    public BusAssignable(String route, int passengers, double farePerPassenger) {
        this.route = route;
        this.passengers = passengers;
        this.farePerPassenger = farePerPassenger;
    }

    public String getRoute() {
        return route;
    }

    public int getPassengers() {
        return passengers;
    }

    public double getFarePerPassenger() {
        return farePerPassenger;
    }
    public double calculateEarnings() {
        return getPassengers() * getFarePerPassenger();
    }

    public void assignDriver(String name) {
        System.out.println("Bus assigned to driver " + name);
    }

}
