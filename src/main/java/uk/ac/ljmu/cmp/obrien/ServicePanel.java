
package uk.ac.ljmu.cmp.obrien;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author cmphfang
 */
public class ServicePanel extends JPanel{
    
    ArrayList<AccountService> serviceList;
    ServiceButtonPanel sbp;
    JTable serviceTable;
    ServiceTableModel stm;
    JScrollPane tableContainer;
    
    //will be deleted later
    ServicePanel()
    {
        
    }
    
    ServicePanel(ArrayList<AccountService> sList)
    {
        this.serviceList = sList;
        stm = new ServiceTableModel(sList);
        
        sbp = new ServiceButtonPanel();
        serviceTable = new JTable(stm);
        tableContainer = new JScrollPane(serviceTable);
        
        this.setLayout(new BorderLayout());
        this.add(tableContainer, BorderLayout.CENTER);
        this.add(sbp, BorderLayout.NORTH);
    }
}
