
package uk.ac.ljmu.cmp.obrien;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmphfang
 */
class ShortServiceTableModel extends AbstractTableModel {
    
    private ArrayList<AccountService> selServiceList;
    
    private final String[] columnNames = {
        "ServiceID",
        "ClientID",
        "Type",
        "PeriodEnd",
        "SalesAmount",
    };
    
    private final Class[] columns = new Class[] {
        Integer.class,
        String.class,
        String.class,
        String.class,
        Double.class,
    };
    
    public ShortServiceTableModel(ArrayList<AccountService> serviceList)
    {
        this.selServiceList = serviceList;
    }
    
    @Override
    public int getRowCount() {
        return selServiceList.size();
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
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
       
        switch (columnIndex) {
            case 0:
                retObject = selServiceList.get(rowIndex).getServiceId();
                break;
            case 1:
                retObject = selServiceList.get(rowIndex).getClientId();
                break;
            case 2: 
                retObject = selServiceList.get(rowIndex).getServiceType();
                break;
            case 3:
                retObject = dateFormat.format(selServiceList.get(rowIndex).getPeriedDate());
                break;
            case 4:
                retObject = selServiceList.get(rowIndex).getSalesAmount();
                break;                
        }
        
        return retObject;
    }
    
}
