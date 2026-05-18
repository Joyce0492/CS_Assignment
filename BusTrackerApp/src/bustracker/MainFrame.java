package bustracker;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * MainFrame - UI:
 * - Left: JList<BusStop> (shows stops for selected bus only)
 * - Center: ETA JTable (each row corresponds to a bus arrival message)
 * - Right-top: Bus JTable (shows all buses with #stops and total dwell)
 * - Right-bottom: Hub JTable (shows hubs with total dwell minutes)
 */
public class MainFrame extends JFrame implements Observer {
    // left
    private JList<BusStop> stopList;
    private StopListModel stopModel;
    private JLabel stopsHeader;
    // center
    private JTable etaTable;
    
    private EtaTableModel etaModel;
    // right
    private JTable busTable;
    private BusTableModel busTableModel;
    private JTable hubTable;
    private HubTableModel hubTableModel;

    // Trackers and data
    private final List<BusTracker> trackers = new ArrayList<>();
    public JList<BusStop> getStopList() {
		return stopList;
	}

	public StopListModel getStopModel() {
		return stopModel;
	}

	public JLabel getStopsHeader() {
		return stopsHeader;
	}

	public JTable getEtaTable() {
		return etaTable;
	}

	public JTable getBusTable() {
		return busTable;
	}

	public JTable getHubTable() {
		return hubTable;
	}

	public List<BusTracker> getTrackers() {
		return trackers;
	}

	public void setHubTableModel(HubTableModel hubTableModel) {
		this.hubTableModel = hubTableModel;
	}

	public EtaTableModel getEtaModel() {
		return etaModel;
	}

	public BusTableModel getBusTableModel() {
		return busTableModel;
	}

	public HubTableModel getHubTableModel() {
		return hubTableModel;
	}	

    public MainFrame() {
        setTitle("Auckland Transport - Bus Tracker (Buses & Hubs demo)");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // ----- LEFT: Stops list (shows only stops for selected bus) -----
        stopsHeader = new JLabel("Stops for selected bus");
        stopModel = new StopListModel();                      // holds BusStop
        stopList = new JList<BusStop>(stopModel);            // explicit type
        stopList.setFixedCellWidth(220);
        JScrollPane stopScroll = new JScrollPane(stopList);
        stopScroll.setPreferredSize(new Dimension(260, 0));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(stopsHeader, BorderLayout.NORTH);
        leftPanel.add(stopScroll, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // ----- CENTER: ETA table (arrival messages) -----
        etaModel = new EtaTableModel();
        etaTable = new JTable(etaModel);
        etaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane etaScroll = new JScrollPane(etaTable);
        add(etaScroll, BorderLayout.CENTER);

        // ----- RIGHT: Bus table (top) and Hub table (bottom) -----
        busTableModel = new BusTableModel();
        busTable = new JTable(busTableModel);
        busTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        hubTableModel = new HubTableModel();
        hubTable = new JTable(hubTableModel);
        hubTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane busScroll = new JScrollPane(busTable);
        JScrollPane hubScroll = new JScrollPane(hubTable);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(4, 4));
        rightPanel.add(new JLabel("Buses (details)"), BorderLayout.NORTH);

        JPanel rightInner = new JPanel();
        rightInner.setLayout(new GridLayout(2, 1, 4, 4));
        rightInner.add(busScroll);

        JPanel hubPanel = new JPanel(new BorderLayout());
        hubPanel.add(new JLabel("Hubs (total dwell minutes)"), BorderLayout.NORTH);
        hubPanel.add(hubScroll, BorderLayout.CENTER);

        rightInner.add(hubPanel);
        rightPanel.add(rightInner, BorderLayout.CENTER);

        rightPanel.setPreferredSize(new Dimension(420, 0));
        add(rightPanel, BorderLayout.EAST);

        // ----- Wire selection listeners -----
        // 1) selecting a row in ETA table => update stops for the bus at that row
        etaTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int r = etaTable.getSelectedRow();
                if (r >= 0) {
                    Bus selectedBus = etaModel.getBusAt(r);
                    showStopsForBus(selectedBus);
                    // also highlight bus in bus table for convenience
                    int busRow = busTableModel.indexOfBus(selectedBus);
                    if (busRow >= 0) {
                        busTable.setRowSelectionInterval(busRow, busRow);
                    }
                }
            }
        });

        // 2) selecting a row in Bus table => update stops for that bus
        busTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int r = busTable.getSelectedRow();
                if (r >= 0) {
                    Bus selectedBus = busTableModel.getBusAt(r);
                    showStopsForBus(selectedBus);
                }
            }
        });

        // Note: selecting hub table intentionally does NOT change stopList (per requirement)

        // ----- Populate example buses, hubs and arrivals (demo dataset) -----
        populateDemoData();

        // Show frame
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Populate demo buses, hubs and ETA messages. This ensures:
     * - busTable shows bus details and total dwell times computed from stops
     * - hubTable shows hubs and getTotalDwellMinutes() is tested
     * - etaTable has multiple rows (arrivals)
     */
    private void populateDemoData() {
        // Drivers
        Driver d1 = new Driver("Richard");
        Driver d2 = new Driver("Alice");
        Driver d3 = new Driver("John");

        // Buses (using Auckland Transport route names)
        Bus bus1 = new Bus("NX1", d1);
        Bus bus2 = new Bus("27H", d2);
        Bus bus3 = new Bus("380", d3);

        // Bus stops (BusStop wraps BusStopLeaf)
        BusStop s1 = new BusStop(new BusStopLeaf("Albany Station", 5));
        BusStop s2 = new BusStop(new BusStopLeaf("Smales Farm", 6));
        BusStop s3 = new BusStop(new BusStopLeaf("Britomart", 8));

        BusStop s4 = new BusStop(new BusStopLeaf("Mt Roskill", 3));
        BusStop s5 = new BusStop(new BusStopLeaf("Balmoral Shops", 7));
        BusStop s6 = new BusStop(new BusStopLeaf("Britomart (27H)", 10));

        BusStop s7 = new BusStop(new BusStopLeaf("Auckland Airport", 1));
        BusStop s8 = new BusStop(new BusStopLeaf("Papatoetoe Station", 4));
        BusStop s9 = new BusStop(new BusStopLeaf("Manukau Bus Station", 6));
        BusStop s10 = new BusStop(new BusStopLeaf("Botany Town Centre", 9));

        // Assign stops to buses (maintain bus.getStops())
        bus1.addStop(s1); bus1.addStop(s2); bus1.addStop(s3);
        bus2.addStop(s4); bus2.addStop(s5); bus2.addStop(s6);
        bus3.addStop(s7); bus3.addStop(s8); bus3.addStop(s9); bus3.addStop(s10);

        // Add buses to BusTableModel
        busTableModel.addBus(bus1);
        busTableModel.addBus(bus2);
        busTableModel.addBus(bus3);

        // Create trackers for each bus (OLDMainFrame observes trackers)
        BusTracker t1 = new BusTracker(bus1);
        BusTracker t2 = new BusTracker(bus2);
        BusTracker t3 = new BusTracker(bus3);

        t1.addObserver(this);
        t2.addObserver(this);
        t3.addObserver(this);

        trackers.add(t1); trackers.add(t2); trackers.add(t3);

        // Add example hubs (StopGroupComposite) to hub table
        StopGroupComposite hubA = new StopGroupComposite("CentralHub", 1);
        hubA.add(new BusStopLeaf("Queen St", 2));
        hubA.add(new BusStopLeaf("Market St", 3));

        StopGroupComposite hubB = new StopGroupComposite("OuterHub", 2);
        StopGroupComposite inner = new StopGroupComposite("InnerHub", 3);
        inner.add(new BusStopLeaf("Newton", 2));
        hubB.add(inner);

        hubTableModel.addHub(hubA);
        hubTableModel.addHub(hubB);

        // Simulate arrival notifications to populate ETA table (10 rows)
        t1.notifyArrival(s1, 2);
        t1.notifyArrival(s2, 5);
        t1.notifyArrival(s3, 8);
        t2.notifyArrival(s4, 3);
        t2.notifyArrival(s5, 7);
        t2.notifyArrival(s6, 10);
        t3.notifyArrival(s7, 1);
        t3.notifyArrival(s8, 4);
        t3.notifyArrival(s9, 6);
        t3.notifyArrival(s10, 9);
    }

    /**
     * Show stops for the selected bus in the stopModel (clearing previous stops).
     * The requirement: the list must only show stops for selected buses (not hubs).
     */
    private void showStopsForBus(Bus bus) {
        if (bus == null) return;

        // Clear
        stopModel.clear();

        // Add the bus's stops (BusStop adapter objects)
        for (BusStop bs : bus.getStops()) {
            stopModel.addElement(bs);
        }

        // Update header to show selected bus and total dwell time of its stops
        int totalDwell = 0;
        for (BusStop bs : bus.getStops()) {
            // BusStop exposes getLeaf().getTotalDwellMinutes()
            totalDwell += bs.getLeaf().getTotalDwellMinutes();
        }
        stopsHeader.setText("Stops for bus " + bus.getBusNumber() + " � total dwell: " + totalDwell + " min");
    }

    @Override
    public void update(Observable o, Object arg) {
        // When a BusTracker notifies, add the bus (if new) and append an ETA row
        BusTracker bt = (BusTracker) o;
        Bus bus = bt.getBus();
        etaModel.addBus(bus);                 // adds bus internally (no duplicate check in model)
        etaModel.addEtaMessage(arg.toString());

        // Also refresh bus table row (if the bus was present, its #stops / dwell may have changed)
        busTableModel.fireTableDataChanged();
    }

    // ----------------- Inner model classes -----------------

    /**
     * BusTableModel - shows Bus number, Driver name, #stops, total dwell (mins)
     */
    private static class BusTableModel extends AbstractTableModel {
        private final List<Bus> buses = new ArrayList<>();
        private final String[] cols = {"Bus Number", "Driver", "#Stops", "Total Dwell (min)"};

        public void addBus(Bus b) {
            if (!buses.contains(b)) {
                buses.add(b);
                fireTableRowsInserted(buses.size() - 1, buses.size() - 1);
            }
        }

        public Bus getBusAt(int row) {
            if (row >= 0 && row < buses.size()) return buses.get(row);
            return null;
        }

        public int indexOfBus(Bus b) {
            return buses.indexOf(b);
        }

        @Override
        public int getRowCount() { return buses.size(); }

        @Override
        public int getColumnCount() { return cols.length; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Bus b = buses.get(rowIndex);
            switch (columnIndex) {
                case 0: return b.getBusNumber();
                case 1: return (b.getDriver() == null ? "" : b.getDriver().getName());
                case 2: return b.getStops().size();
                case 3: {
                    int total = 0;
                    for (BusStop bs : b.getStops()) total += bs.getLeaf().getTotalDwellMinutes();
                    return total;
                }
                default: return "";
            }
        }

        @Override
        public String getColumnName(int column) { return cols[column]; }
    }

    /**
     * HubTableModel - shows hub name and its total dwell minutes (testing composite)
     */
    private static class HubTableModel extends AbstractTableModel {
        private final List<StopGroupComposite> hubs = new ArrayList<>();
        private final String[] cols = {"Hub Name", "Total Dwell (min)"};

        public void addHub(StopGroupComposite h) {
            hubs.add(h);
            fireTableRowsInserted(hubs.size() - 1, hubs.size() - 1);
        }

        public StopGroupComposite getHubAt(int row) {
            if (row >= 0 && row < hubs.size()) return hubs.get(row);
            return null;
        }

        @Override
        public int getRowCount() { return hubs.size(); }

        @Override
        public int getColumnCount() { return cols.length; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            StopGroupComposite h = hubs.get(rowIndex);
            switch (columnIndex) {
                case 0: return h.toString();
                case 1: return h.getTotalDwellMinutes();
                default: return "";
            }
        }

        @Override
        public String getColumnName(int column) { return cols[column]; }
    }

    // ----------------- main -----------------
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
