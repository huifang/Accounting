
package uk.ac.ljmu.cmp.obrien;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmphfang
 */
public class ServiceTableModel extends AbstractTableModel {

    private ArrayList<AccountService> serviceList;
    
    private final String[] columnNames = {
        "ServiceID",
        "ClientID",
        "Type",
        "PeriodEnd",
        "SalesAmount",
        "VATAmount",
        "ServiceFee",
        "PaidAmount"
    };
    
    private final Class[] columns = new Class[] {
        Integer.class,
        String.class,
        String.class,
        Date.class,
        Double.class,
        Double.class,
        Double.class,
        Double.class
    };
    
    public ServiceTableModel(ArrayList<AccountService> serviceList)
    {
        this.serviceList = serviceList;
    }
    
    @Override
    public int getRowCount() {
        return serviceList.size();
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
                retObject = serviceList.get(rowIndex).getServiceId();
                break;
            case 1:
                retObject = serviceList.get(rowIndex).getClientId();
                break;
            case 2: 
                retObject = serviceList.get(rowIndex).getServiceType();
                break;
            case 3:
                retObject = serviceList.get(rowIndex).getPeriedDate();
                break;
            case 4:
                retObject = serviceList.get(rowIndex).getSalesAmount();
                break;
            case 5:
                retObject = serviceList.get(rowIndex).getVATAmount();
                break;
            case 6:
                retObject = serviceList.get(rowIndex).getServiceFee();
                break;
            case 7:
                retObject = serviceList.get(rowIndex).getPaidAmount();
                break;
                
        }
        
        return retObject;
    }
    
}
