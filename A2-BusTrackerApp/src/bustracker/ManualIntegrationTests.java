package bustracker;

public class ManualIntegrationTests {

    public static void main(String[] args) {
        ManualIntegrationTests tests = new ManualIntegrationTests();
        tests.runAllTests();
    }

    public void runAllTests() {
        // Bus & Driver
        testBusDriverAssignment();        // Expect driver assigned to bus
        testBusDriverLimit();             // Expect exception after 3rd bus
        testDriverBusList();              // Expect driver's bus list correct
        testBusToString();                // Expect "BusNumber(Driver)"

        // Composite
        testLeafDwellTime();              // Expect dwell time = leaf's dwell
        testCompositeDwellTime();         // Expect dwell = hub + children
        testNestedComposite();            // Expect nested dwell summed
        testCompositeNoChildren();        // Expect dwell = hub's dwell only
        testCompositeRemoveChild();       // Expect dwell recalculated

        // BusTracker
        testBusTrackerSingleObserver();   // Expect one notification printed
        testBusTrackerMultipleObservers();// Expect two notifications printed
        testBusTrackerDifferentStops();   // Expect two different messages
        testBusTrackerRemoveObserver();   // Expect no output after removal
        testBusTrackerBusNumber();        // Expect bus number in message

        // StopListModel
        testStopListModelAdd();           // Expect size=1
        testStopListModelDuplicate();     // Expect no duplicate added
        testStopListModelRemove();        // Expect size decreases
        testStopListModelFind();          // Expect index returned
        testStopListModelDisplay();       // Expect stop printed

        // Integration
        testIntegrationSelectionUpdatesStops(); // Expect bus stops count printed

        // ETA TableModel
        testEtaTableAddSingle();          // Expect RowCount=1
        testEtaTableMultiple();           // Expect RowCount=2
        testEtaTableColumnValues();       // Expect correct busNo & msg
        testEtaTableColumnNames();        // Expect col names "Bus Number", "ETA Message"
        testEtaTableDuplicateBusRows();   // Expect 2 rows for same bus
    }

    // ---------------- Bus & Driver ----------------
    public void testBusDriverAssignment() {
        Driver d = new Driver("Alice");
        Bus b = new Bus("Bus-101", d);
        System.out.println("Test1: " + b + " assigned to " + d.getName());
        // Expect: Bus-101(Alice) assigned to Alice
    }

    public void testBusDriverLimit() {
        try {
            Driver d = new Driver("Bob");
            new Bus("Bus-201", d);
            new Bus("Bus-202", d);
            new Bus("Bus-203", d);
            new Bus("Bus-204", d); // should fail
        } catch (IllegalArgumentException e) {
            System.out.println("Test2: Exception OK -> " + e.getMessage());
            // Expect: "Cannot assign more than 3 buses."
        }
    }

    public void testDriverBusList() {
        Driver d = new Driver("Carl");
        new Bus("Bus-301", d);
        new Bus("Bus-302", d);
        System.out.println("Test3: Driver buses -> " + d.getBuses());
        // Expect: Carl's bus list with 2 buses
    }

    public void testBusToString() {
        Driver d = new Driver("Dana");
        Bus b = new Bus("Bus-401", d);
        System.out.println("Test4: " + b);
        // Expect: Bus-401(Dana)
    }

    // ---------------- Composite ----------------
    public void testLeafDwellTime() {
        RouteComponent stop = new BusStopLeaf("Downtown", 5);
        System.out.println("Test5: Dwell = " + stop.getTotalDwellMinutes());
        // Expect: 5
    }

    public void testCompositeDwellTime() {
        StopGroupComposite hub = new StopGroupComposite("Hub1", 3);
        hub.add(new BusStopLeaf("Stop1", 4));
        hub.add(new BusStopLeaf("Stop2", 6));
        System.out.println("Test6: Total dwell = " + hub.getTotalDwellMinutes());
        // Expect: 3 + 4 + 6 = 13
    }

    public void testNestedComposite() {
        StopGroupComposite hub = new StopGroupComposite("MainHub", 2);
        StopGroupComposite subHub = new StopGroupComposite("SubHub", 1);
        subHub.add(new BusStopLeaf("StopX", 3));
        hub.add(subHub);
        System.out.println("Test7: Nested dwell = " + hub.getTotalDwellMinutes());
        // Expect: 2 + (1+3) = 6
    }

    public void testCompositeNoChildren() {
        StopGroupComposite hub = new StopGroupComposite("EmptyHub", 7);
        System.out.println("Test8: Empty dwell = " + hub.getTotalDwellMinutes());
        // Expect: 7
    }

    public void testCompositeRemoveChild() {
        StopGroupComposite hub = new StopGroupComposite("RemovableHub", 2);
        RouteComponent stop = new BusStopLeaf("StopY", 4);
        hub.add(stop);
        hub.remove(stop);
        System.out.println("Test9: After remove dwell = " + hub.getTotalDwellMinutes());
        // Expect: 2
    }

    // ---------------- BusTracker ----------------
    public void testBusTrackerSingleObserver() {
        Driver d = new Driver("Eve");
        Bus b = new Bus("Bus-501", d);
        BusTracker tracker = new BusTracker(b);
        tracker.addObserver((o, arg) -> System.out.println("Test10: Received -> " + arg));
        tracker.notifyArrival(new BusStop(new BusStopLeaf("Airport", 5)), 10);
        // Expect: "Arrival at Airport(5min) in 10 min"
    }

    public void testBusTrackerMultipleObservers() {
        Driver d = new Driver("Frank");
        Bus b = new Bus("Bus-601", d);
        BusTracker tracker = new BusTracker(b);
        tracker.addObserver((o, arg) -> System.out.println("Test11a: " + arg));
        tracker.addObserver((o, arg) -> System.out.println("Test11b: " + arg));
        tracker.notifyArrival(new BusStop(new BusStopLeaf("Station", 3)), 8);
        // Expect: Both observers print the same message
    }

    public void testBusTrackerDifferentStops() {
        Driver d = new Driver("George");
        Bus b = new Bus("Bus-701", d);
        BusTracker tracker = new BusTracker(b);
        tracker.addObserver((o, arg) -> System.out.println("Test12: " + arg));
        tracker.notifyArrival(new BusStop(new BusStopLeaf("StopA", 2)), 4);
        tracker.notifyArrival(new BusStop(new BusStopLeaf("StopB", 3)), 6);
        // Expect: Two different messages
    }

    public void testBusTrackerRemoveObserver() {
        Driver d = new Driver("Hank");
        Bus b = new Bus("Bus-801", d);
        BusTracker tracker = new BusTracker(b);
        java.util.Observer obs = (o, arg) -> System.out.println("Test13: " + arg);
        tracker.addObserver(obs);
        tracker.deleteObserver(obs);
        tracker.notifyArrival(new BusStop(new BusStopLeaf("IgnoredStop", 1)), 5);
        System.out.println("Test13: No output expected after removal");
    }

    public void testBusTrackerBusNumber() {
        Driver d = new Driver("Ian");
        Bus b = new Bus("Bus-901", d);
        BusTracker tracker = new BusTracker(b);
        tracker.addObserver((o, arg) -> System.out.println("Test14: " + b.getBusNumber() + " -> " + arg));
        tracker.notifyArrival(new BusStop(new BusStopLeaf("Central", 4)), 7);
        // Expect: Bus number printed with ETA message
    }

    // ---------------- StopListModel ----------------
    public void testStopListModelAdd() {
        StopListModel model = new StopListModel();
        model.addElement(new BusStop(new BusStopLeaf("Stop1", 2)));
        System.out.println("Test15: Size = " + model.getSize());
        // Expect: 1
    }

    public void testStopListModelDuplicate() {
        StopListModel model = new StopListModel();
        BusStop stop = new BusStop(new BusStopLeaf("Stop1", 2));
        model.addElement(stop);
        model.addElement(stop);
        System.out.println("Test16: Should be size 1 -> " + model.getSize());
    }

    public void testStopListModelRemove() {
        StopListModel model = new StopListModel();
        BusStop stop = new BusStop(new BusStopLeaf("Stop2", 3));
        model.addElement(stop);
        model.removeElementAt(0);
        System.out.println("Test17: Size = " + model.getSize());
        // Expect: 0
    }

    public void testStopListModelFind() {
        StopListModel model = new StopListModel();
        BusStop stop = new BusStop(new BusStopLeaf("Stop3", 4));
        model.addElement(stop);
        System.out.println("Test18: Index of Stop3 = " + model.findStopByName("Stop3"));
        // Expect: 0
    }

    public void testStopListModelDisplay() {
        StopListModel model = new StopListModel();
        model.addElement(new BusStop(new BusStopLeaf("Stop4", 5)));
        for (int i = 0; i < model.getSize(); i++) {
            System.out.println("Test19: " + model.getElementAt(i));
        }
        // Expect: Stop4 displayed
    }

    public void testIntegrationSelectionUpdatesStops() {
        Driver d = new Driver("Jack");
        Bus b = new Bus("Bus-1001", d);
        StopListModel model = new StopListModel();
        model.addElement(new BusStop(new BusStopLeaf("StopX", 3)));
        model.addElement(new BusStop(new BusStopLeaf("StopY", 6)));
        System.out.println("Test20: Stops for Bus " + b.getBusNumber() + " -> " + model.getSize() + " stops");
        // Expect: 2 stops
    }

    // ---------------- ETA TableModel ----------------
    public void testEtaTableAddSingle() {
        EtaTableModel etaModel = new EtaTableModel();
        Driver d = new Driver("Liam");
        Bus b = new Bus("Bus-1101", d);
        etaModel.addBus(b);
        etaModel.addEtaMessage("Arrival at Downtown in 5 min");
        System.out.println("Test21: RowCount = " + etaModel.getRowCount());
        // Expect: 1
    }

    public void testEtaTableMultiple() {
        EtaTableModel etaModel = new EtaTableModel();
        Driver d = new Driver("Mia");
        Bus b1 = new Bus("Bus-1201", d);
        Bus b2 = new Bus("Bus-1202", d);
        etaModel.addBus(b1);
        etaModel.addEtaMessage("Arrival at Station in 4 min");
        etaModel.addBus(b2);
        etaModel.addEtaMessage("Arrival at Airport in 8 min");
        System.out.println("Test22: RowCount = " + etaModel.getRowCount());
        // Expect: 2
    }

    public void testEtaTableColumnValues() {
        EtaTableModel etaModel = new EtaTableModel();
        Driver d = new Driver("Noah");
        Bus b = new Bus("Bus-1301", d);
        etaModel.addBus(b);
        etaModel.addEtaMessage("Arrival at Hub in 10 min");
        System.out.println("Test23: BusNo=" + etaModel.getValueAt(0,0)
                + ", Msg=" + etaModel.getValueAt(0,1));
        // Expect: Bus-1301, "Arrival at Hub in 10 min"
    }

    public void testEtaTableColumnNames() {
        EtaTableModel etaModel = new EtaTableModel();
        System.out.println("Test24: Col0=" + etaModel.getColumnName(0)
                + ", Col1=" + etaModel.getColumnName(1));
        // Expect: "Bus Number", "ETA Message"
    }

    public void testEtaTableDuplicateBusRows() {
        EtaTableModel etaModel = new EtaTableModel();
        Driver d = new Driver("Olivia");
        Bus b = new Bus("Bus-1401", d);
        etaModel.addBus(b);
        etaModel.addEtaMessage("Arrival at Stop1 in 2 min");
        etaModel.addBus(b);
        etaModel.addEtaMessage("Arrival at Stop2 in 6 min");
        System.out.println("Test25: RowCount=" + etaModel.getRowCount()
                + " (expect 2 rows for same bus)");
        // Expect: 2
    }
}