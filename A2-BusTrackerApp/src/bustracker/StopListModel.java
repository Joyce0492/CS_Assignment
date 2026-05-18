// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: The StopListModel class is a custom ListModel for displaying a list of BusStop objects in a JList.
// It provides methods to manage the list of stops and notifies the GUI when the list changes, enabling real-time updates in the interface.

package bustracker;
import javax.swing.*;
import java.util.*;
//ListModel for Stops' List
public class StopListModel extends AbstractListModel<BusStop> {
    private ArrayList<BusStop> stopList;
    public StopListModel() {
        stopList = new ArrayList<>();
    }
    public int getSize() {
        return stopList.size();
    }
    public BusStop getElementAt(int index) {
        return stopList.get(index);
    }
    public void addElement(BusStop element) {
        ArrayList<String> stopNames = new ArrayList<>();
        for (int i = 0; i < stopList.size(); i++) {
            stopNames.add(stopList.get(i).getName().toLowerCase());
        }
        if (!stopNames.contains(element.getName())) {
            stopList.add(element);
            int index0 = stopList.size() - 1;
            int index1 = index0;
            fireIntervalAdded(this, index0, index1);
        }
    }
    public void removeElementAt(int index) {
        stopList.remove(stopList.get(index));
        fireIntervalRemoved(this, index, index);
    }
    public int findStopByName(String name) {
        for (int i = 0; i < stopList.size(); i++) {
            if (stopList.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
    public void clear() {
        stopList.clear();
        fireContentsChanged(this, 0, stopList.size());
    }

}
