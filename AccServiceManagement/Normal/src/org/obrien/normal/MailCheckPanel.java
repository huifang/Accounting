
package org.obrien.normal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;

/**
 *
 * @author cmphfang
 */
class MailCheckPanel extends JPanel implements ActionListener {
    
    DataDAO dao;
    JPanel mailPanel;
    
    JLabel emailLabel;
    JTextField emailBox;
    
    JLabel passwordLabel;
    JPasswordField passwordBox;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    JLabel dateFromLabel;
    JTextField dateFromBox;
    
    JLabel dateToLabel;
    JTextField dateToBox;
    
    JButton connectButton;
    
    JTextArea area = new JTextArea();

    ArrayList<MessageRecord> records = new ArrayList<>();
    
    int noReg = 0;
    int noTax = 0;
    int noCName = 0;
    int noCAddress = 0;
    int noEORI = 0;
    int noOthers = 0;
    
    MailCheckPanel(DataDAO dao)
    {
        this.dao = dao;
        mailPanel = new JPanel();

        emailLabel = new JLabel("Email Address:");
        emailBox = new JTextField("obsacctesing@gmail.com");
        passwordLabel = new JLabel("Password");
        passwordBox = new JPasswordField();
        
        dateFromLabel = new JLabel("Received from (yyyy-MM-dd):");
        dateToLabel = new JLabel("Received to (yyyy-MM-dd):");
        Calendar cal = Calendar.getInstance();
        Date current = cal.getTime();
        cal.add(Calendar.DATE,-1);
        Date prev = cal.getTime();
        //LocalDate prev = LocalDate.now().minusDays(300);
        dateFromBox = new JTextField(sdf.format(prev));
        dateToBox = new JTextField(sdf.format(current));
        
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        
        mailPanel.setLayout(new GridLayout(5,2));
        mailPanel.add(emailLabel);
        mailPanel.add(emailBox);
        mailPanel.add(passwordLabel);
        mailPanel.add(passwordBox);
        mailPanel.add(dateFromLabel);
        mailPanel.add(dateFromBox);
        mailPanel.add(dateToLabel);
        mailPanel.add(dateToBox);
        mailPanel.add(connectButton);
        
        area.setRows(20);
        
        this.setLayout(new BorderLayout());
        this.add(mailPanel,BorderLayout.NORTH);
        this.add(area, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        //clear textarea and clear arraylist for get new messages
        area.setText("");
        records.clear();
            
        noReg = 0;
        noTax = 0;
        noCName = 0;
        noCAddress = 0;
        noEORI = 0;
        noOthers = 0;
         
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.imap.host", "imap.gmail.com");
            properties.put("mail.imap.port", "993");
            //properties.put("mail.imap.starttls.enable", "true");
            // SSL setting
            properties.setProperty(
                  String.format("mail.%s.socketFactory.class", "imap"),
                      "javax.net.ssl.SSLSocketFactory");
            properties.setProperty(
                  String.format("mail.%s.socketFactory.fallback", "imap"),
                      "false");
            properties.setProperty(
                  String.format("mail.%s.socketFactory.port", "imap"),
                  String.valueOf("993"));
            //properties.put("mail.mime.decodetext.strict","false");
            Session emailSession = Session.getDefaultInstance(properties);

            //get filtered date parsed
            String dateFrom = dateFromBox.getText();
            String dateTo = dateToBox.getText();
            Date pastDate = convertToDate(dateFrom,"00:00:00");
            Date futureDate = convertToDate(dateTo, "23:59:59");
                        
            Store store = emailSession.getStore("imap");
            store.connect(emailBox.getText(), passwordBox.getText());

                //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
       
            SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT, futureDate);
            SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, pastDate);
            SearchTerm andTerm = new AndTerm(olderThan, newerThan);
                        
                    // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.search(andTerm);//emailFolder.getMessages(); //
            area.append("Loading and Processing mails......\n\n");
            area.append("Messages received: " + messages.length + "\n");
            
            mailProecess(messages);
            
            area.append("New Registration clients: " + noReg + "\n");
            area.append("Regular Tax communication: " + noTax + "\n");
            area.append("Change Name Service: " + noCName + "\n");
            area.append("Change Address Service: " + noCAddress + "\n");
            area.append("EROI Service: " + noEORI + "\n");
            area.append("Other Unannotated Service: " + noOthers + "\n");
            //System.out.println("messages.length---" + messages.length);
            
            /*for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
                System.out.println("Received date: "+ message.getSentDate());
            
            }*/

            //close the store and folder objects
            emailFolder.close(false);
            
        } catch (NoSuchProviderException npe) {
               npe.printStackTrace();
               JOptionPane.showMessageDialog(null,"Cannot connect to the mail server!");
        } catch (MessagingException me) {
               me.printStackTrace();
               JOptionPane.showMessageDialog(null,"Cannot retrieve messages from the mail server!");
        } catch (Exception ex) {
               ex.printStackTrace();
        }
    }
    
    Date convertToDate(String dateString, String time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String year = dateString.substring(0,4);
        String month = dateString.substring(5,7);
        String day = dateString.substring(8,10);
    
        String dateConvert = day + "-" + month + "-" + year + " " + time;
        try {
            return formatter.parse(dateConvert);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null,"The date format is not right");
            return null;
        }
    }

    private int checkRegistrations(Message[] messages) {
        
        String subject;
        
        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            try {
               subject = message.getSubject();
                if(subject.contains("注册"))
                {
                    noReg++;
                }
            } catch (MessagingException ex) {
                Exceptions.printStackTrace(ex);
            }           
            
        }
        
        return noReg;
    }

    private void mailProecess(Message[] messages) {

        String subject;
        Date receiveTime;
        MessageRecord tmpRecord;
        
        for (int i = 0, n = messages.length; i < n; i++) {
            String tmpServiceID;
            String requestLikeID;
            Message message = messages[i];
            try {
               subject = message.getSubject();
               receiveTime = message.getReceivedDate();
               tmpRecord = new MessageRecord(subject,receiveTime);
               
               //now process and get clientID, serviceType, and serviceID
               int startInd = 0;
               while (startInd < subject.length() && !Character.isDigit(subject.charAt(startInd))) startInd++;
               int endInd = startInd;
               while (endInd < subject.length() && Character.isDigit(subject.charAt(endInd))) endInd++;
               String id = subject.substring(startInd, endInd);
               
               //now decide to which agency it belongs
               if(startInd<3)
               {
                   //agency is missing so use the default
                   tmpRecord.setClientID("USA"+id);
               }
               else if ((!Character.isLetter(subject.charAt(startInd-3)))
                       ||(!Character.isLetter(subject.charAt(startInd-2)))
                       ||(!Character.isLetter(subject.charAt(startInd-1))))
               {
                   //any of the 3 charaters before it is not letter, use default
                   tmpRecord.setClientID("USA"+id);
               }
               else
               {
                   tmpRecord.setClientID(subject.substring(startInd-3,startInd)+id);
               }
               //System.out.println(tmpRecord.getClientID());
               
               if(subject.contains("注册"))
               {
                    //new registration
                    tmpRecord.setServiceType("N");
                    tmpServiceID = "N" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + "01" + tmpRecord.getClientID();
                    tmpRecord.setRelatedServiceID(tmpServiceID);
                    records.add(tmpRecord);
                    noReg++;
               }
               else if(subject.contains("申报"))
               {
                    tmpRecord.setServiceType("R");
                    requestLikeID = "R" + "%" + tmpRecord.getClientID();
                    //System.out.println(requestLikeID);
                    tmpServiceID = dao.findServiceID(requestLikeID);
                    //System.out.println(tmpServiceID);
                    tmpRecord.setRelatedServiceID(tmpServiceID);
                    records.add(tmpRecord);
                    noTax++;
               }
               else if(subject.contains("改")&&subject.contains("名"))
               {
                   tmpRecord.setServiceType("U");
                   requestLikeID = "U" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + "%" + tmpRecord.getClientID();
                   
                   int index = dao.findNoService(requestLikeID, receiveTime);
                   tmpServiceID = "U" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + PaddingMonth(index+1) + tmpRecord.getClientID();
                   tmpRecord.setRelatedServiceID(tmpServiceID);
                   records.add(tmpRecord);
                   noCName++;
               }
               else if(subject.contains("改地址")||subject.contains("转地址"))
               {
                   tmpRecord.setServiceType("A");
                   requestLikeID = "A" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + "%" + tmpRecord.getClientID();
                   int index = dao.findNoService(requestLikeID, receiveTime);
                   
                   tmpServiceID = "A" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + PaddingMonth(index+1) + tmpRecord.getClientID();
                   tmpRecord.setRelatedServiceID(tmpServiceID);
                   records.add(tmpRecord);
                   noCAddress++;
               }
               else if(subject.toLowerCase().contains("eori"))
               {
                   tmpRecord.setServiceType("E");
                   requestLikeID = "E" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + "%" + tmpRecord.getClientID();
                   int index = dao.findNoService(requestLikeID, receiveTime);
                   
                   tmpServiceID = "E" + String.valueOf(receiveTime.getYear()+1900) + String.valueOf(PaddingMonth(receiveTime.getMonth())) + PaddingMonth(index+1) + tmpRecord.getClientID();
                   tmpRecord.setRelatedServiceID(tmpServiceID);
                   records.add(tmpRecord);
                   noEORI++;
               }
               else
               {
                   noOthers++;
                   records.add(tmpRecord);
               }
               //System.out.println(tmpRecord.getRelatedServiceID());
               
            } catch (MessagingException ex) {
                Exceptions.printStackTrace(ex);
            }           
            
        }
        
    }
    
    public ArrayList<MessageRecord> getRecords()
    {
        return records;
    }
    
    String PaddingMonth(int month)
    {
        if(month<10)
        {
            return "0"+String.valueOf(month);
        }
        else
        {
            return String.valueOf(month);
        }
    }
    
}
