// Student Name: Wenqing Luo
// UPI: wluo256
// Date Last Modified: 11 Oct
// Class Responsibility: Custom TableModel for JTable to display Bus and ETA messages.
// Maintains synchronized lists of buses and ETA messages, notifies JTable on updates,
// and supports duplicate bus entries for multiple ETA messages.
package bustracker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
//Table model for JTable needed to store ETA
public class EtaTableModel extends AbstractTableModel {
	private List<Bus> data = new ArrayList<>();
    private List<String> etaMessages = new ArrayList<>();
    private final String[] columnNames = {"Bus Number", "ETA Message"};    

    public void addEtaMessage(String msg) {
        etaMessages.add(msg);
        int index = etaMessages.size() - 1;
        fireTableRowsInserted(index, index);
    }
    
    public void addBus(Bus bus) {
        data.add(bus);
        int index = data.size() - 1;
        fireTableRowsInserted(index, index);
    }

    @Override
    public int getRowCount() { return etaMessages.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bus bus = data.get(rowIndex);
        String msg = etaMessages.get(rowIndex);
        switch (columnIndex) {
            case 0: return bus.getBusNumber();
            case 1: return msg;
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    public Bus getBusAt(int row) { return data.get(row); }
}
