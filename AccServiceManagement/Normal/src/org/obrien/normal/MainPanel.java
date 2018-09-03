
package org.obrien.normal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * We use this panel to integrate client panel and service panel for quick development
 * @author cmphfang
 */
public class MainPanel extends JPanel{
    
    final JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    final JSplitPane clientSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    //final JSplitPane serviceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    
    //two popup menu for inserting tax info and update service info
    final JPopupMenu popupClient = new JPopupMenu();
    final JPopupMenu popupService = new JPopupMenu();
    
    String selClientID;
    String selServiceID;
    
    DataDAO dao = new DataDAO("jdbc:mysql://localhost:3306","abc","54321");
    
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
    JButton cImportFromFile = new JButton("Import from File");
    JButton cImportManual = new JButton("Manual Import");
    JButton cSearch = new JButton("Search Client");
    
    //service management buttons
    JPanel sButtonPanel = new JPanel();
    JButton sSingleCreation = new JButton("New Service Creation");
    JButton sBatchCreation = new JButton("Services Batch Creation");
    JButton sHighlight = new JButton("Show Uncompleted Services");
    JButton sSetReceipts = new JButton("Set Payment Batch");
    JButton sUndoReceipts = new JButton("Un-set Payment Batch");
    JButton sServiceCharge = new JButton("Generate Xml Receipts");
    JButton sReceivePayment = new JButton("Transaction Completion");
    
    MainPanel()
    {
        //retrieve the data
        dao.retrieveClients(clientList);
        dao.retrieveServices(serviceList);
        
        //build up the client table
        ctm = new ClientTableModel(clientList);
        clientTable = new JTable(ctm);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientTable.requestFocus();
        clientTable.changeSelection(0, 0, false, false);
        selClientID = clientList.get(0).getAgencyID();
        clientTableContainer = new JScrollPane(clientTable);
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BorderLayout());
        cPanel.add(clientTableContainer, BorderLayout.CENTER);
        
        cButtonPanel.setLayout(new FlowLayout());
        cButtonPanel.add(cImportFromFile);
        cButtonPanel.add(cImportManual);
        cButtonPanel.add(cSearch);
        cPanel.add(cButtonPanel, BorderLayout.SOUTH);

        //insert a new client into the database three listener
        cImportFromFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileInputStream fileStream = new FileInputStream(file);
                    HSSFWorkbook wb = new HSSFWorkbook(fileStream);
                    HSSFSheet sheet = wb.getSheetAt(0);
                    HSSFCell cell;
                    cell = sheet.getRow(1).getCell(1);
                    cell = sheet.getRow(1).getCell(2);
                    String chWords = cell.getStringCellValue();
                    System.out.println(chWords);
                    System.out.println(cell.getStringCellValue());
                    } catch (IOException ex) {
                        Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Cancel the selection");
                }
            }
        });
        
        cImportManual.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientRegPanel p = new ClientRegPanel();
                int click = JOptionPane.showConfirmDialog(null, p, "Input Client", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                    AccountClient c = new AccountClient(p.cNoBox.getText(), p.cNameCBox.getText());
                    dao.insertNewClient(c);
                    clientList.clear();
                    dao.retrieveClients(clientList);
                    ctm.setClientList(clientList);
                    //System.out.println(selClientID);
                    ctm.fireTableDataChanged();
                    
                    for(int i = 0; i<clientList.size(); i++)
                    {
                        //System.out.println(clientList.get(i).getAgencyID());
                        if(clientList.get(i).getAgencyID().equals(selClientID))
                        {
                            clientTable.requestFocus();
                            clientTable.changeSelection(i,0, false,false);
                            break;
                        }
                    }
                }
            }
        });
        
        cSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientSearchPanel p = new ClientSearchPanel();
                int click = JOptionPane.showConfirmDialog(null, p, "Search Client", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                    //System.out.println(p.getSearchBy());
                    //System.out.println(p.getSearchText());
                    for(int i = 0; i<clientTable.getRowCount();i++)
                    {
                        //will add English name and tax no into the table for searching later
                        if(p.getSearchBy()==0 && p.getSearchText().equals(clientTable.getValueAt(i, 0)))
                        {
                            //get ith row
                            clientTable.requestFocus();
                            clientTable.changeSelection(i, 0, false, false);
                            dao.retrieveSelectedServices(selServiceList, selClientID);
                        
                            //selServiceID = null;
                            sstm.fireTableDataChanged();
                            break;
                        }
                        //else if
                    }
                }
            }
        });
        
        //client table right button
        JMenuItem menuItem1 = new JMenuItem("Import Tax from Pdf file");
        JMenuItem menuItem2 = new JMenuItem("Update Client Info");
        menuItem1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //popup file selection dialog and read tax no, return type and first return date
                //System.out.println("Input Tax info");
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                    try {
                        PDDocument pdDoc = PDDocument.load(file);
                        PDFTextStripper pdfStripper = new PDFTextStripper();
                        String parsedText = pdfStripper.getText(pdDoc);
                                                
                        if(parsedText.length() > 100)
                        {    
                            int a = parsedText.indexOf("Registration Number:");

                            String taxno = parsedText.substring(a+21,a+32);
                            System.out.println(taxno);
                        }
                        else
                        {
                            System.out.println("Fail to read pdf file, maybe try manual import.");
                        }
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
            } else {
                System.out.println("Cancel the selection");
            }
            }
        });
        menuItem2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Will implement/Allow client info changes");
                AccountClient tmpC = new AccountClient(); 
                dao.retrieveClientDetails(tmpC);
                ClientUpdatePanel p = new ClientUpdatePanel(tmpC);
                int click = JOptionPane.showConfirmDialog(null, p, "Update Client", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                
                }
            }
        });
        popupClient.add(menuItem1);
        popupClient.add(menuItem2);
        
        //click left button
        clientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                 if (! event.getValueIsAdjusting()){
                    //System.out.println(clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString());
                    if(clientTable.getSelectedRow()>= 0)
                    {
                        selClientID = clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString();                
                        dao.retrieveSelectedServices(selServiceList, selClientID);
                        
                        //selServiceID = null;
                        sstm.fireTableDataChanged();
                    }
                 }
            }
        });
        
        //click right button
        clientTable.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
               if(e.getButton()==MouseEvent.BUTTON3){
                //System.out.println("Right button clicked");
                    popupClient.show(e.getComponent(),e.getX(),e.getY());
               }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        //retrieve selecte services
        dao.retrieveSelectedServices(selServiceList, selClientID);
        
        sstm = new ShortServiceTableModel(selServiceList);
        selServiceTable = new JTable(sstm);
        selServiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selServiceTableContainer = new JScrollPane(selServiceTable);
        JPanel ssPanel = new JPanel();
        ssPanel.setLayout(new BorderLayout());
        //ssPanel.add(selServiceTable.getTableHeader(),BorderLayout.NORTH);
        ssPanel.add(selServiceTableContainer, BorderLayout.CENTER);
        
        
        //add selection of short service table
        selServiceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                 if (! event.getValueIsAdjusting()){
                    //System.out.println(clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString());
                    if(clientTable.getSelectedRow()>= 0 && selServiceTable.getSelectedRow()>=0)
                    {
                        selServiceID = (String) selServiceTable.getValueAt(selServiceTable.getSelectedRow(), 0); 
                        //System.out.println(selServiceID);
                        //find the individual service
                        //dao.retrieveSelectedServices(selServiceList, selClientID);
                        serviceTable.requestFocus();
                        for(int i = 0; i<serviceList.size(); i++)
                        {
                            if(selServiceID.equals(serviceList.get(i).getServiceId()))
                            {
                                serviceTable.requestFocus();
                                serviceTable.changeSelection(i, 0, false, false);
                                break;
                            }
                        }
                    }
                 }
            }
        });
        
        
        stm = new ServiceTableModel(serviceList);
        serviceTable = new JTable(stm);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTableContainer = new JScrollPane(serviceTable);
        JPanel sPanel = new JPanel();
        sPanel.setLayout(new BorderLayout());
        //sPanel.add(serviceTable.getTableHeader(),BorderLayout.NORTH);
        sPanel.add(serviceTableContainer, BorderLayout.CENTER);
        
        sButtonPanel.setLayout(new FlowLayout());
        sButtonPanel.add(sSingleCreation);
        sButtonPanel.add(sBatchCreation);
        sButtonPanel.add(sHighlight);
        sButtonPanel.add(sSetReceipts);
        sButtonPanel.add(sUndoReceipts);
        sButtonPanel.add(sServiceCharge);
        sButtonPanel.add(sReceivePayment);
        sPanel.add(sButtonPanel, BorderLayout.SOUTH);
        
        sSingleCreation.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Single Service");
            }
        });
        
        sHighlight.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //now we just choose the service list and show them
                ArrayList<AccountService> unServices = new ArrayList<>();
                dao.retrieveUncompletedServices(unServices);
                UnCompleteServicePanel p = new UnCompleteServicePanel(unServices);
                JOptionPane.showMessageDialog(null, p, "Uncompleted Services", JOptionPane.INFORMATION_MESSAGE);
                
            }
        });
         //service table right button
        JMenuItem sMenuItem1 = new JMenuItem("Calculate SaleAmount from file");
        JMenuItem sMenuItem2 = new JMenuItem("Update Service Progress");
        //this button is for updating the pdf receipt and lable as complete
        JMenuItem sMenuItem3 = new JMenuItem("Update Receipt Pdf");
        sMenuItem1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //popup file selection dialog and read tax no, return type and first return date
                //System.out.println("Input Tax info");
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                System.out.println("Load and calculate the sale amount");
            } else {
                System.out.println("Cancel the selection");
            }
            }
        });
        sMenuItem2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update the Service stage");
                
            }
        });
        sMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update Receipt, Calculate the service Fee & mark as complete!");
            }
        });
        popupService.add(sMenuItem1);
        popupService.add(sMenuItem2);
        popupService.add(sMenuItem3);
        
                
        //click right button
        serviceTable.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
               if(e.getButton()==MouseEvent.BUTTON3){
                //System.out.println("Right button clicked");
                if(serviceTable.getSelectedRow()>=0)
                {
                    popupService.show(e.getComponent(),e.getX(),e.getY());
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Please select one service!","Error", JOptionPane.ERROR_MESSAGE);
                }
               }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
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