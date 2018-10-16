
package org.obrien.normal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        "Progress",
        "SalesAmount",
        "VATAmount"
        //"ServiceFee",
        //"PaidAmount"
    };
    
    private final Class[] columns = new Class[] {
        String.class,
        String.class,
        String.class,
        String.class,
        Integer.class,
        Double.class,
        Double.class
        //Double.class,
        //Double.class
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
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
       
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
                retObject = dateFormat.format(serviceList.get(rowIndex).getPeriedDate());
                //System.out.println(retObject);
                break;
            case 4:
                retObject = serviceList.get(rowIndex).getProgress();
                break;
            case 5:
                retObject = serviceList.get(rowIndex).getSalesAmount();
                break;
            case 6:
                retObject = serviceList.get(rowIndex).getVATAmount();
                break;
            //case 7:
            //    retObject = serviceList.get(rowIndex).getServiceFee();
            //    break;
            //case 8:
            //    retObject = serviceList.get(rowIndex).getPaidAmount();
            //    break;
                
        }
        
        return retObject;
    }
    
}
