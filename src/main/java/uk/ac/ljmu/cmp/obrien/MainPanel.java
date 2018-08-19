
package uk.ac.ljmu.cmp.obrien;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * We use this panel to integrate client panel and service panel for quick development
 * @author cmphfang
 */
public class MainPanel extends JPanel{
    
    final JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    final JSplitPane clientSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    //final JSplitPane serviceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    
    String selClientID;
    
    DataDAO dao = new DataDAO("jdbc:mysql://localhost:3306","root","12345");
    
    //members for client table
    ArrayList<AccountClient> clientList = new ArrayList<>();
    JTable clientTable;
    ClientTableModel ctm;
    JScrollPane clientTableContainer;
    
    //members for selected service table
    ArrayList<AccountService> selServiceList = new ArrayList<>();
    JTable selServiceTable;
    ShortServiceTableModel sstm;
    JScrollPane selServiceTableContainer;
    
    //member for service table
    ArrayList<AccountService> serviceList = new ArrayList<>();
    JTable serviceTable;
    ServiceTableModel stm;
    JScrollPane serviceTableContainer;
    
    //client management buttons
    JPanel cButtonPanel = new JPanel();
    JButton cImportFromMail = new JButton("Mail Import");
    JButton cImportManual = new JButton("Manual Import");
    
    MainPanel()
    {
        //retrieve the data
        dao.retrieveClients(clientList);
        dao.retrieveServices(serviceList);
        
        //build up the client table
        ctm = new ClientTableModel(clientList);
        clientTable = new JTable(ctm);
        clientTable.requestFocus();
        clientTable.changeSelection(0, 0, false, false);
        selClientID = clientList.get(0).getAgencyID();
        clientTableContainer = new JScrollPane(clientTable);
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BorderLayout());
        cPanel.add(clientTable.getTableHeader(),BorderLayout.NORTH);
        cPanel.add(clientTable, BorderLayout.CENTER);
        
        cButtonPanel.setLayout(new FlowLayout());
        cButtonPanel.add(cImportFromMail);
        cButtonPanel.add(cImportManual);
        cPanel.add(cButtonPanel, BorderLayout.SOUTH);
        
        //insert a new client into  the database
        cImportManual.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientRegPanel p = new ClientRegPanel();
                int click = JOptionPane.showConfirmDialog(null, p, "Input Client", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                    System.out.println(p.cNoBox.getText());
                    
                }
            }
        });
        
        //retrieve selecte services
        dao.retrieveSelectedServices(selServiceList, selClientID);
        
        sstm = new ShortServiceTableModel(selServiceList);
        selServiceTable = new JTable(sstm);
        selServiceTableContainer = new JScrollPane(selServiceTable);
        JPanel ssPanel = new JPanel();
        ssPanel.setLayout(new BorderLayout());
        ssPanel.add(selServiceTable.getTableHeader(),BorderLayout.NORTH);
        ssPanel.add(selServiceTable, BorderLayout.CENTER);
        
        stm = new ServiceTableModel(serviceList);
        serviceTable = new JTable(stm);
        serviceTableContainer = new JScrollPane(serviceTable);
        JPanel sPanel = new JPanel();
        sPanel.setLayout(new BorderLayout());
        sPanel.add(serviceTable.getTableHeader(),BorderLayout.NORTH);
        sPanel.add(serviceTable, BorderLayout.CENTER);
        
        clientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                 if (! event.getValueIsAdjusting()){
                    //System.out.println(clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString());
                    selClientID = clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString();                
                    dao.retrieveSelectedServices(selServiceList, selClientID);
                    sstm.fireTableDataChanged();
                 }
            }
        });
        
        clientSplitPane.add(cPanel);
        clientSplitPane.add(ssPanel);
        clientSplitPane.setDividerLocation(0.7);
        
        mainSplitPane.add(clientSplitPane);
        mainSplitPane.add(sPanel);
        //mainSplitPane.getRightComponent().setLayout(new BorderLayout());
        //mainSplitPane.getRightComponent().add(serviceTable.getTableHeader(), BorderLayout.PAGE_START);
        mainSplitPane.setDividerLocation(0.4);
        
        this.setLayout(new BorderLayout());
        this.add(mainSplitPane, BorderLayout.CENTER);
        //this.add(cbp, BorderLayout.NORTH);
    }
}
