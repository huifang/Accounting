
package org.obrien.normal;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmphfang
 */
public class ClientTableModel extends AbstractTableModel {

    private ArrayList<AccountClient> clientList;
    
    private final String[] columnNames = {
        "AgencyID",
        "CompanyName",
        "English Name",
        "Contact Email",
        "Tax No."
    };
    
    private final Class[] columns = new Class[] {
        String.class,
        String.class,
        String.class,
        String.class,
        String.class
    };
    
    public ClientTableModel(ArrayList<AccountClient> clientList)
    {
        this.clientList = clientList;
    }
    
    public void setClientList(ArrayList<AccountClient> cl)
    {
        clientList = cl;
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
                retObject = clientList.get(rowIndex).getAgencyID();
                break;
            case 1: 
                retObject = clientList.get(rowIndex).getCname();
                break;
            case 2: 
                if(clientList.get(rowIndex).getEname()!=null)
                   retObject = clientList.get(rowIndex).getEname();
                else
                   retObject = "";
                break;
            case 3: 
                if(clientList.get(rowIndex).getMail()!=null)
                   retObject = clientList.get(rowIndex).getMail();
                else
                   retObject = "";
                break;
            case 4:
                if(clientList.get(rowIndex).getTaxNo()!= null)
                    retObject = clientList.get(rowIndex).getTaxNo();
                else
                    retObject = "";
                break;
        }
        
        return retObject;
    }
    
}
