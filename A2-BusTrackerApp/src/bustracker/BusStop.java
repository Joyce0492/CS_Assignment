// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The BusStop class acts as an Adapter between the system and the BusStopLeaf.
//It allows the BusStopLeaf objects — which are part of the Composite structure — to be used conveniently within GUI components like JList.
//This class provides simplified access to the stop’s name and adapts the internal structure of BusStopLeaf to a format suitable for UI display.

package bustracker;

//Adapter (to Adaptee BusStopLeaf) for JList
public class BusStop {
	private BusStopLeaf adaptee;

	public BusStop(BusStopLeaf adaptee) {
		this.adaptee = adaptee;
	}

	public String getName() {
		return adaptee.getName();
	}

	public BusStopLeaf getLeaf() {
		return adaptee;
	}

	@Override
	public String toString() {
		return adaptee.getName();  // what JList displays
	}
}
