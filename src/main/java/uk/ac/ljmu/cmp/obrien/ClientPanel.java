
package uk.ac.ljmu.cmp.obrien;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author cmphfang
 */
public class ClientPanel extends JPanel{

    ArrayList<AccountClient> clientList;
    
    ClientButtonPanel cbp;
    JTable clientTable;
    ClientTableModel ctm;
    JScrollPane tableContainer;

    ClientPanel(ArrayList<AccountClient> cList)
    {
        this.clientList = cList;
        
        ctm = new ClientTableModel(cList);
        
        cbp = new ClientButtonPanel();
        clientTable = new JTable(ctm);
        clientTable.requestFocus();
        clientTable.changeSelection(0, 0, false, false);
        
        clientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                 if (! event.getValueIsAdjusting()){
                System.out.println(clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString());
                //selectedIDs.clear();
                //selectedIDs.add(clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString());
                
            }
            }
        });
        
        tableContainer = new JScrollPane(clientTable);
        
        this.setLayout(new BorderLayout());
        this.add(tableContainer, BorderLayout.CENTER);
        this.add(cbp, BorderLayout.NORTH);
    }
}
