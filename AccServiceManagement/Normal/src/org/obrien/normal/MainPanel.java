
package org.obrien.normal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openide.util.Exceptions;

/**
 * We use this panel to integrate client panel and service panel for quick development
 * @author cmphfang
 */
public class MainPanel extends JPanel{
    
    final JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    final JSplitPane clientSplitPane;
    //final JSplitPane serviceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    
    //two popup menu for inserting tax info and update service info
    final JPopupMenu popupClient = new JPopupMenu();
    final JPopupMenu popupService = new JPopupMenu();
    
    String selClientID;
    String selServiceID;
    
    DataDAO dao = new DataDAO("jdbc:mysql://localhost:3306","root","54321");
    //DataDAO dao = new DataDAO("jdbc:mysql://obriensca.co.uk:3306/obriensc_management","obriensc_gina","1234567890");
    
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
    JPanel ssPanel;
    JPanel sPanel;
    JPanel cPanel;
    
    //client management buttons
    JPanel cButtonPanel = new JPanel();
    JButton cImportFromFile = new JButton("Import from File");
    JButton cImportManual = new JButton("Manual Import");
    JButton cSearch = new JButton("Search Client");
    
    //service management buttons
    JPanel sButtonPanel = new JPanel();
    JButton sCheckMail = new JButton("Check Mails");
    JButton sSingleCreation = new JButton("New Service Creation");
    JButton sBatchCreation = new JButton("Services Batch Creation");
    JButton sHighlight = new JButton("Show Uncompleted Services");
    //JButton sSetReceipts = new JButton("Set Payment Batch");
    //JButton sUndoReceipts = new JButton("Un-set Payment Batch");
    //JButton sServiceCharge = new JButton("Generate Xml Receipts");
    //JButton sReceivePayment = new JButton("Transaction Completion");
    JLabel sAmountLabel = new JLabel("0.0");
    
    double threeSAmount;
    
    MainPanel()
    {
        //retrieve the data
        dao.retrieveClients(clientList);
        dao.retrieveServices(serviceList);
        
        //build up the client table
        System.out.println("The client list is loaded with " + clientList.size()+" clients.");
        System.out.println("The client list is loaded with " + serviceList.size()+" services.");
        ctm = new ClientTableModel(clientList);
        clientTable = new JTable(ctm);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientTable.requestFocus();
        clientTable.changeSelection(0, 0, false, false);
        selClientID = clientList.get(0).getAgencyID();
        //System.out.println(selClientID);
        clientTableContainer = new JScrollPane(clientTable);
        cPanel = new JPanel();
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
                String agencyID=null;
                String ccName=null;
                String ecName=null;
                String address=null;
                String repName=null;
                String email=null;
                String tel=null;
                Date dob=null;
                String dobstring = null;
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileInputStream fileStream = new FileInputStream(file);
                    HSSFWorkbook wb = new HSSFWorkbook(fileStream);
                    HSSFSheet sheet = wb.getSheetAt(0);
                    //HSSFCell cell;
                    //cell = sheet.getRow(1).getCell(1);
                    if(sheet.getRow(1).getCell(1)!=null)
                        agencyID = sheet.getRow(1).getCell(1).getStringCellValue();
                    if(sheet.getRow(2).getCell(1)!=null)
                        ccName = sheet.getRow(2).getCell(1).getStringCellValue();
                    if(sheet.getRow(2).getCell(2)!=null)
                        ecName = sheet.getRow(2).getCell(2).getStringCellValue(); 
                    if(sheet.getRow(3).getCell(2)!=null)
                        address= sheet.getRow(2).getCell(2).getStringCellValue();
                    if(sheet.getRow(4).getCell(1)!=null)
                        repName= sheet.getRow(4).getCell(1).getStringCellValue();
                    if(sheet.getRow(5).getCell(1)!=null)
                    {
                        dob = sheet.getRow(5).getCell(1).getDateCellValue();
                        dobstring = sf.format(dob);
                    }
                    if(sheet.getRow(9).getCell(1)!=null)
                        tel= sheet.getRow(9).getCell(1).getStringCellValue();
                    if(sheet.getRow(11).getCell(1)!=null)
                        email= sheet.getRow(11).getCell(1).getStringCellValue();
                    
                    //if get cliend id and cname
                    if(agencyID != null && ccName != null)
                    {
                        AccountClient c = new AccountClient(agencyID, ccName);
                        c.setEName(ecName);
                        c.setAddress(address);
                        c.setMail(email);
                        c.setRepName(repName);
                        c.setRepDob(dobstring);
                        c.setContactNo(tel);

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
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Fail to read xls file, maybe try manual creation.");
                    }
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
                    //here we have all the 8 attributes
                    String agencyID = p.cNoBox.getText();
                    String ccName = p.cNameCBox.getText();
                    String ecName = p.cNameEBox.getText();
                    String address = p.cAddressBox.getText();
                    String repName = p.repNameBox.getText();
                    String email = p.contactEmailBox.getText();
                    String tel = p.telBox.getText();
                    String dob = p.repDobBox.getText();
                    
                    AccountClient c = new AccountClient(agencyID, ccName);
                    c.setEName(ecName);
                    c.setAddress(address);
                    c.setMail(email);
                    c.setRepName(repName);
                    c.setRepDob(dob);
                    c.setContactNo(tel);
                    
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
        //search function - include 3 fields search
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
                        if(p.getSearchBy()==0 && p.getSearchText().toLowerCase().equals(((String)clientTable.getValueAt(i, 0)).toLowerCase()))
                        {
                            //get ith row
                            clientTable.requestFocus();
                            clientTable.changeSelection(i, 0, false, false);
                            dao.retrieveSelectedServices(selServiceList, selClientID);
                            sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                            
                            ssPanel.repaint();
                            //selServiceID = null;
                            sstm.fireTableDataChanged();
                            
                            break;
                        }
                        else if(p.getSearchBy()==2 && p.getSearchText().equals(clientTable.getValueAt(i, 4)))
                        {
                            //get ith row
                            clientTable.requestFocus();
                            clientTable.changeSelection(i, 0, false, false);
                            dao.retrieveSelectedServices(selServiceList, selClientID);
                        
                            sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                            
                            ssPanel.repaint();
                            sstm.fireTableDataChanged();
                            break;
                        }
                        else if(p.getSearchBy()==1)
                        {
                            String input = p.getSearchText().replaceAll("\\s+","").toLowerCase();
                            String match = ((String)clientTable.getValueAt(i, 2)).replaceAll("\\s+","").toLowerCase();
                            
                            if(match.contains(input))
                            {
                                clientTable.requestFocus();
                                clientTable.changeSelection(i, 0, false, false);
                                dao.retrieveSelectedServices(selServiceList, selClientID);

                                sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                            
                                ssPanel.repaint();
                            
                                sstm.fireTableDataChanged();
                                break;
                            }
                        }
                        
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
                String ename = clientTable.getValueAt(clientTable.getSelectedRow(), 2).toString().replaceAll("\\s+","").toLowerCase(); 
                String cid = clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString();
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        try (PDDocument pdDoc = PDDocument.load(file)) {
                            PDFTextStripper pdfStripper = new PDFTextStripper();
                            String parsedText = pdfStripper.getText(pdDoc);
                            
                            if(parsedText.length() > 100)
                            {
                                int indNo = parsedText.indexOf("Registration Number:");
                                int indName = parsedText.indexOf("Registered name:");
                                int indDate1 = parsedText.indexOf("Effective Date of Registration:");
                                int indDate2 = parsedText.indexOf("VAT return period end date");
                                int indType = parsedText.indexOf("Frequency of returns:");
                                
                                String taxno = parsedText.substring(indNo+21,indNo+32).replaceAll("\\s+","");
                                String regname = parsedText.substring(indName+17,indName+27);
                                String date1_string = parsedText.substring(indDate1+32,indDate1+43);
                                String date2_string = parsedText.substring(indDate2+29,indDate2+40);
                                String freq = parsedText.substring(indType+22,indType+23);
                                //if(ename.contains(regname))
                                String conv_date1 = date1_string.substring(7) + "-" + date1_string.substring(3,6) + "-" + date1_string.substring(0,2);
                                String conv_date2 = date2_string.substring(7) + "-" + date2_string.substring(3,6) + "-" + date2_string.substring(0,2);
                                //System.out.println(taxno);
                                //System.out.println(freq);
                                //System.out.println(conv_date1);
                                //System.out.println(conv_date2);
                                JOptionPane.showMessageDialog(null,"Inserting the tax info is \nThe taxno is "
                                +taxno+"\nThe first return is "+conv_date1+"\nThe effective date is "+conv_date2);
                                dao.updateTaxInfo(cid, taxno, freq, conv_date1, conv_date2);
                                
                                //here is for updating the view
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
                                //uphere
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,"Fail to read pdf file, maybe try manual import.");
                            }
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

                AccountClient tmpC = new AccountClient(selClientID); 
                dao.retrieveClientDetails(selClientID, tmpC);
                               
                ClientUpdatePanel p = new ClientUpdatePanel(tmpC);
                int click = JOptionPane.showConfirmDialog(null, p, "Update Client", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                   //verify the taxno and make sure it is 9 digits or empty,check the input 1by1 
                   AccountClient updateC = new AccountClient(selClientID);
                   updateC.setCName(p.cNameCBox.getText());
                   updateC.setEName(p.cNameEBox.getText());
                   updateC.setAddress(p.cAddressBox.getText());
                   updateC.setRepName(p.repNameBox.getText());
                   updateC.setRepDob(p.repDobBox.getText());
                   updateC.setMail(p.contactEmailBox.getText());
                   updateC.setContactNo(p.telBox.getText());
                   switch (p.taxnoBox.getText().length()) {
                        case 0:
                            dao.updateClientNoTax(updateC); 
                            break;
                        case 9:
                            updateC.setTaxNo(p.taxnoBox.getText());
                            dao.updateClient(updateC);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null,"You can either leave the tax no box empty or provide 9 digits input!");
                            break;
                    }
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

                        sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                            
                        ssPanel.repaint();
                            
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
        
        //retrieve select services
        dao.retrieveSelectedServices(selServiceList, selClientID);
        sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                            
        sstm = new ShortServiceTableModel(selServiceList);
        selServiceTable = new JTable(sstm);
        selServiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selServiceTableContainer = new JScrollPane(selServiceTable);
        ssPanel = new JPanel();
        ssPanel.setLayout(new BorderLayout());
        //ssPanel.add(selServiceTable.getTableHeader(),BorderLayout.NORTH);
        ssPanel.add(selServiceTableContainer, BorderLayout.CENTER);

        JPanel sAmountPanel = new JPanel();
        JLabel textLabel = new JLabel("Total Amount of previous three months: ");
        sAmountPanel.add(textLabel);
        sAmountPanel.add(sAmountLabel);
        ssPanel.add(sAmountPanel, BorderLayout.SOUTH);
        
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
        sPanel = new JPanel();
        sPanel.setLayout(new BorderLayout());
        //sPanel.add(serviceTable.getTableHeader(),BorderLayout.NORTH);
        sPanel.add(serviceTableContainer, BorderLayout.CENTER);
        
        sButtonPanel.setLayout(new FlowLayout());
        sButtonPanel.add(sCheckMail);
        sButtonPanel.add(sSingleCreation);
        sButtonPanel.add(sBatchCreation);
        sButtonPanel.add(sHighlight);
        //sButtonPanel.add(sSetReceipts);
        //sButtonPanel.add(sUndoReceipts);
        //sButtonPanel.add(sServiceCharge);
        //sButtonPanel.add(sReceivePayment);
        sPanel.add(sButtonPanel, BorderLayout.SOUTH);
        
        sCheckMail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MailCheckPanel mcp = new MailCheckPanel(dao);
                int click = JOptionPane.showConfirmDialog(null, mcp, "Check Mails", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                    //save the related records into a file
                    ArrayList<MessageRecord> mr = mcp.getRecords();
                    try {

                        Writer w = new OutputStreamWriter(new FileOutputStream("c:\\tmp\\mail-output.txt"),"gbk");
                        Integer i = 1;
                        for (MessageRecord mr1 : mr) {
                            //String b = mr1.getSubject();    
                           w.append(i.toString());
                           w.append(",");
                           w.append(mr1.getSubject());
                           w.append(",");
                           w.append(mr1.getRelatedServiceID());
                           w.append("\n");
                           i += 1; 
                                //System.out.println(mr1.getSubject());
                            }
                        w.close();
                        }catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    
                    //connect to the database and update record
                    for (MessageRecord mr1 : mr) {
                        String stype = mr1.getServiceType();
                        switch(stype) {
                        case "R"://update the service to 1
                           dao.updateServiceProgress(mr1.getRelatedServiceID(), 1);
                           break;
                        case "N"://create new serivce or do nothing
                           dao.insertNewService(mr1.getRelatedServiceID());
                           break; 
                        case "U"://create new change name service or do nothing
                           dao.insertNewService(mr1.getRelatedServiceID());
                           break;
                        case "A"://create new change address service
                           dao.insertNewService(mr1.getRelatedServiceID());
                           break; 
                        case "E"://create new eroi service
                           dao.insertNewService(mr1.getRelatedServiceID());
                           break; 
                    } 
                    }
                    
                    //update all the views
                    clientTable.changeSelection(0, 0, false, false);
                    dao.retrieveSelectedServices(selServiceList, selClientID);
                    sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                    ssPanel.repaint();
                    sstm.fireTableDataChanged();
                } 
                else
                {
                    
                }
            }
        }
        );
        sSingleCreation.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(selClientID);
                ServiceCreationPanel p = new ServiceCreationPanel(selClientID);
                int click = JOptionPane.showConfirmDialog(null, p, "Create Service", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                    //here we have all the 8 attributes
                    String newServiceID="";
                    boolean isCreate = false;
                    
                    String agencyID = p.cClientIDBox.getText();
                    String serviceTime = p.cYearMonthBox.getText().replace("-", "");
                    String serviceType = p.getInitLetter();
                    String serviceLikeID; //= serviceType + serviceTime + "%" + agencyID;
                    //System.out.println(serviceLikeID);
                    Date createTime = new Date();
                    int noReturn = 0;      
                    switch(serviceType) {
                        case "R":
                           serviceLikeID = serviceType + "%" + agencyID;
                           noReturn = dao.findNoService(serviceLikeID, createTime);
                           //System.out.println(noReturn);
                           if(noReturn == 0)
                           {
                               isCreate = true;
                               newServiceID = serviceType + serviceTime + "01" + agencyID;
                               System.out.println(newServiceID);
                           }
                           break;
                        case "N":
                           serviceLikeID = serviceType + "%" + agencyID;
                           noReturn = dao.findNoService(serviceLikeID, createTime);
                           if(noReturn == 0)
                           {
                               isCreate = true;
                               newServiceID = serviceType + serviceTime + "01" + agencyID;
                           }
                           break; 
                        case "U":
                           serviceLikeID = serviceType + serviceTime + "%" + agencyID;
                           noReturn = dao.findNoService(serviceLikeID, createTime);
                           isCreate = true;
                           newServiceID = serviceType + serviceTime + PaddingNumber(noReturn+1) + agencyID;
                           break;
                        case "A":
                           serviceLikeID = serviceType + serviceTime + "%" + agencyID;
                           noReturn = dao.findNoService(serviceLikeID, createTime);
                           isCreate = true;
                           newServiceID = serviceType + serviceTime + PaddingNumber(noReturn+1) + agencyID;
                           break; 
                        case "E":
                           serviceLikeID = serviceType + serviceTime + "%" + agencyID;
                           noReturn = dao.findNoService(serviceLikeID, createTime);
                           isCreate = true;
                           newServiceID = serviceType + serviceTime + PaddingNumber(noReturn+1) + agencyID;
                           break; 
                        case "Q":
                           serviceLikeID = serviceType + "%" + agencyID;
                           noReturn = dao.findNoService(serviceLikeID, createTime);
                           if(noReturn == 0)
                           {
                               isCreate = true;
                               newServiceID = serviceType + serviceTime + "01" + agencyID;
                           }
                           break;  
                    }
                    
                    if(isCreate==true)
                    {
                        dao.insertNewService(newServiceID);
                        
                        //update all the views
                        for(int i = 0;i<clientTable.getRowCount();i++)
                        {
                            if(clientTable.getValueAt(i, 0).equals(agencyID))
                            {
                                clientTable.changeSelection(i, 0, false, false);
                                dao.retrieveSelectedServices(selServiceList, agencyID);
                                break;
                            }
                        }
  
                        sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                        ssPanel.repaint();
                        sstm.fireTableDataChanged();
                    
                        dao.retrieveServices(serviceList);
                        stm.fireTableDataChanged();
                        for(int j=0; j<serviceTable.getRowCount();j++)
                        {
                            if(serviceTable.getValueAt(j,0).equals(newServiceID))
                            {
                                serviceTable.changeSelection(j, 0, false, false);
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null,"The new service has been successfully created!");
                    }
                    else
                    {
                       JOptionPane.showMessageDialog(null,"The record exists or the input is not correct");
                    }
                
                }
            }
        });
        
        sBatchCreation.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap serviceMonth = new HashMap<String, Date>();
                ArrayList<String> serviceIDAll = new ArrayList<>();
                ArrayList<String> newServiceID = new ArrayList<>();
                
                dao.retrieveLatestReturnDate(serviceMonth);
                dao.retrieveAllServiceID(serviceIDAll);
                
                int curYear = Calendar.getInstance().get(Calendar.YEAR);
                int curMonth = Calendar.getInstance().get(Calendar.MONTH);
                int cmpYear;
                int cmpMonth;
                
                //System.out.println(String.valueOf(curYear)+String.valueOf(curMonth));
                //System.out.println(serviceIDAll.size());
                for(Object name: serviceMonth.keySet())
                {
                    String key = name.toString();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date)serviceMonth.get(key));
                    cmpYear = cal.get(Calendar.YEAR);
                    cmpMonth = cal.get(Calendar.MONTH);
                    
                    boolean createFlag;
                    if(cmpMonth>curMonth)
                    {
                        createFlag = ((cmpMonth-curMonth) % 3 == 0);
                    }
                    else
                    {
                        createFlag = ((curMonth+12-cmpMonth) % 3 == 0);
                    }   
                    /*if(cmpYear==curYear)
                    {
                        createFlag = ((curMonth-cmpMonth) == 3);
                    }
                    else
                    {
                        createFlag = ((curMonth+12-cmpMonth) == 3);
                    }*/
                    
                    if(createFlag)
                    {
                        String monthString;
                        //generate new return service id
                        if(curMonth+1<10)
                        {
                            monthString = "0" + String.valueOf(curMonth+1);
                        }
                        else
                        {
                            monthString = String.valueOf(curMonth+1);
                        }
                        String newID = "R" + String.valueOf(curYear) + monthString + "01" + key;
                        //System.out.println(newID);
                        newServiceID.add(newID);
                    }
                    //System.out.println(key + "Month" + cal.get(Calendar.YEAR));
                }
                
                dao.insertBatchServices(newServiceID);
                
                //after insertion, retrieve all the services into the table again.
                dao.retrieveServices(serviceList);
                dao.retrieveSelectedServices(selServiceList, selClientID);
                sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                            
                ssPanel.repaint();         
                sPanel.repaint();
                sstm.fireTableDataChanged();
                stm.fireTableDataChanged();

            }
        });
                
        sHighlight.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //now we just choose the service list and show them
                ArrayList<AccountService> unServices = new ArrayList<>();
                dao.retrieveUncompletedServices(unServices);
                UnCompleteServicePanel p = new UnCompleteServicePanel(unServices);
                try {
                    //now save uncomplete services to a file
                    PrintWriter pw = new PrintWriter(new File("C:\\tmp\\uncompletion.csv"));
                    for(int i = 0; i < unServices.size(); i++)
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append(unServices.get(i).getClientId());
                        sb.append(',');
                        sb.append(unServices.get(i).getPeriedDate());
                        sb.append(',');
                        sb.append(unServices.get(i).getProgress());
                        sb.append('\n');
                        pw.write(sb.toString());
                    }
                    pw.close();
                } catch (FileNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
                
                JOptionPane.showMessageDialog(null, p, "Uncompleted Services", JOptionPane.INFORMATION_MESSAGE);
                
            }
        });
         //service table right button
        //JMenuItem sMenuItem1 = new JMenuItem("Calculate SaleAmount from file");
        JMenuItem sMenuItem2 = new JMenuItem("Update Service Progress");
        //this button is for updating the pdf receipt and lable as complete
        JMenuItem sMenuItem3 = new JMenuItem("Update Receipt Pdf");
        /*sMenuItem1.addActionListener(new ActionListener(){
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
        });*/
        sMenuItem2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String sid = serviceTable.getValueAt(serviceTable.getSelectedRow(), 0).toString();
                int curProg = (int)serviceTable.getValueAt(serviceTable.getSelectedRow(), 4);
                ServiceProgressUpdatePanel p = new ServiceProgressUpdatePanel();
                int click = JOptionPane.showConfirmDialog(null, p, "Update Service Progress", JOptionPane.OK_CANCEL_OPTION);
                if(click == JOptionPane.OK_OPTION) {
                    if(curProg>2)
                    {
                        JOptionPane.showMessageDialog(null,"The service has been completed! For reopening, please contact your manager.");
                    }
                    else if (curProg == p.getProgSelection())
                    {
                        JOptionPane.showMessageDialog(null,"You are updating the progress status while you did not change the status...");
                    }
                    else
                    {
                        //System.out.println(p.getProgSelection());
                        dao.updateServiceProgress(sid, p.getProgSelection());
                        
                        if(p.getProgSelection()==3)
                        {
                            dao.UpdateLatestReturn(sid);
                            JOptionPane.showMessageDialog(null,"You have successfully completed the service and the service is closed.");
                        }
                        
                        dao.retrieveServices(serviceList);
                        dao.retrieveSelectedServices(selServiceList, selClientID);
                        sAmountLabel.setText(String.valueOf(calcThreeSAmount()));
                        ssPanel.repaint();         
                        sPanel.repaint();
                        sstm.fireTableDataChanged();
                        stm.fireTableDataChanged();
                        
                        //focus on serviceid
                        for(int j=0; j<serviceTable.getRowCount();j++)
                        {
                            if(serviceTable.getValueAt(j,0).equals(sid))
                            {
                                serviceTable.changeSelection(j, 0, false, false);
                                break;
                            }
                        }
                    }
                }
                
            }
        });
        sMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Update Receipt, Calculate the service Fee & mark as complete!");
                String sid = serviceTable.getValueAt(serviceTable.getSelectedRow(), 0).toString();
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        try (PDDocument pdDoc = PDDocument.load(file)) {
                            PDFTextStripper pdfStripper = new PDFTextStripper();
                            String parsedText = pdfStripper.getText(pdDoc);
                            
                            //System.out.println(parsedText.length());
                            if(parsedText.length() > 100)
                            {
                                int indNo = parsedText.indexOf("Registration Number:");
                                int indVatAmount = parsedText.indexOf("(Box 1):");
                                int indEndVat = parsedText.indexOf("VAT due in this period on acquisitions");
                                int indSAmount = parsedText.indexOf("(Box 6):");
                                int indEndSales = parsedText.indexOf("Total value of purchases ");
                                String taxno = parsedText.substring(indNo+21,indNo+32).replaceAll("\\s+","");
                                String vatAmountString = parsedText.substring(indVatAmount+9, indEndVat).replaceAll("GBP","").replaceAll("\n","").replaceAll(",","");
                                String salesAmountString = parsedText.substring(indSAmount+9, indEndSales).replaceAll("GBP","").replaceAll("\n","").replaceAll(",","");
                                //System.out.println(taxno);
                                double vatAmount = Double.valueOf(vatAmountString);
                                double sAmount = Double.valueOf(salesAmountString);
                                //System.out.println(vatAmount);
                                //System.out.println(sAmount);
                                //if(ename.contains(regname))
                                //dao.updateTaxInfo(cid, taxno, freq, conv_date1, conv_date2);
                                JOptionPane.showMessageDialog(null,"Inserting the tax info for completing the service: \nThe client is "
                                +sid+"\nThe sales amount is "+sAmount+"\nThe VAT amount is "+vatAmount);
                                dao.CompleteTaxService(sid, sAmount, vatAmount); 
                                dao.UpdateLatestReturn(sid);
                                
                                //refresh the table                               
                                dao.retrieveServices(serviceList);
                                dao.retrieveSelectedServices(selServiceList, selClientID);
                                sAmountLabel.setText(String.valueOf(calcThreeSAmount()));

                                ssPanel.repaint();         

                                sstm.fireTableDataChanged();
                                stm.fireTableDataChanged();
                                 //update all the views
                                for(int j=0; j<serviceTable.getRowCount();j++)
                                {
                                    if(serviceTable.getValueAt(j,0).equals(sid))
                                    {
                                        serviceTable.changeSelection(j, 0, false, false);
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,"Fail to read pdf file, maybe try manual input later.");
                            }
                        }
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
            } else {
                System.out.println("Cancel the selection");
            }
            }
        });
        //popupService.add(sMenuItem1);
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
        
        clientSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cPanel, ssPanel);
        clientSplitPane.setResizeWeight(0.6);
        
        mainSplitPane.add(clientSplitPane);
        mainSplitPane.add(sPanel);
        //mainSplitPane.getRightComponent().setLayout(new BorderLayout());
        //mainSplitPane.getRightComponent().add(serviceTable.getTableHeader(), BorderLayout.PAGE_START);
        mainSplitPane.setResizeWeight(0.4);
        
        this.setLayout(new BorderLayout());
        this.add(mainSplitPane, BorderLayout.CENTER);
        //this.add(cbp, BorderLayout.NORTH);
    }
    
    private double calcThreeSAmount()
    {
        //we still get 4 record cuz the current one is set to 0 at the stage
        double retVal=0;    
        if(selServiceList.size()>=4)
        {
            for(int i = selServiceList.size()-1; i>selServiceList.size()-5;i--)
            {
                retVal += selServiceList.get(i).getSalesAmount();
            }
        }
        else if(selServiceList.size()>0)
        {
            for(int i = 0; i<selServiceList.size();i++)
            {
                retVal += selServiceList.get(i).getSalesAmount();
            }
        }
        else
        {
            retVal = 0;
        }
        return retVal;
    }
        
    String PaddingNumber(int no)
    {
        if(no<10)
        {
            return "0"+String.valueOf(no);
        }
        else
        {
            return String.valueOf(no);
        }
    }
}
