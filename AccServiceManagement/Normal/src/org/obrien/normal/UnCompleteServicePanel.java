/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obrien.normal;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author cmphfang
 */
class UnCompleteServicePanel extends JPanel {
    JTable unServiceTable;
    ServiceTableModel stm;
    JScrollPane pane;
    
    UnCompleteServicePanel(ArrayList<AccountService> service)
    {
        stm = new ServiceTableModel(service);
        unServiceTable = new JTable(stm);
        pane = new JScrollPane(unServiceTable);
        this.setLayout(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);
        //this.add(unServiceTable.getTableHeader(),BorderLayout.NORTH);
    }
    
}
