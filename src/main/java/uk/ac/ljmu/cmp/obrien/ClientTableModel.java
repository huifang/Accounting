
package uk.ac.ljmu.cmp.obrien;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmphfang
 */
public class ClientTableModel extends AbstractTableModel {

    private ArrayList<Client> clientList;
    
    private final String[] columnNames = {
        "ClientID",
        "AgencyID",
        "CompanyName"
    };
    
    private final Class[] columns = new Class[] {
        Integer.class,
        String.class,
        String.class};
    
    public ClientTableModel(ArrayList<Client> clientList)
    {
        this.clientList = clientList;
    }
    
    @Override
    public int getRowCount() {
        return clientList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
       return columnNames[col]; 
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       Object retObject = null;
       
        switch (columnIndex) {
            case 0:
                retObject = clientList.get(rowIndex).getClientID();
                break;
            case 1:
                retObject = clientList.get(rowIndex).getAgencyID();
                break;
            case 2: 
                retObject = clientList.get(rowIndex).getCname();
                break;
        }
        
        return retObject;
    }
    
}
