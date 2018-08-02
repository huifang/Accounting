
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
public class ClientPanel extends JPanel{
    
    ArrayList<Client> clientList;
    ClientButtonPanel cbp;
    JTable clientTable;
    ClientTableModel ctm;
    JScrollPane tableContainer;
    
    ClientPanel(ArrayList<Client> cList)
    {
        this.clientList = cList;
        ctm = new ClientTableModel(cList);
        
        cbp = new ClientButtonPanel();
        clientTable = new JTable(ctm);
        tableContainer = new JScrollPane(clientTable);
        
        this.setLayout(new BorderLayout());
        this.add(tableContainer, BorderLayout.CENTER);
        this.add(cbp, BorderLayout.NORTH);
    }
}
