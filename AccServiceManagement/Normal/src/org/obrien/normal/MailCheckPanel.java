
package org.obrien.normal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    int noOthers = 0;
    
    MailCheckPanel()
    {
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
            area.append("Other Unannotated Service: " + noOthers + "\n");
            //System.out.println("messages.length---" + messages.length);
            /*
            for (int i = 0, n = messages.length; i < n; i++) {
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
               
               if(subject.contains("注册"))
               {
                    //new registration
                    tmpRecord.setServiceType("N");
                    noReg++;
               }
               else if(subject.contains("第")&&subject.contains("次"))
               {
                    tmpRecord.setServiceType("R");
                    noTax++;
               }
               else if(subject.contains("改")&&subject.contains("名"))
               {
                    noCName++;
               }
               else if(subject.contains("改地址"))
               {
                    noCAddress++;
               }
               else
               {
                   noOthers++;
               }
               
            } catch (MessagingException ex) {
                Exceptions.printStackTrace(ex);
            }           
            
        }
        
    }
    
}
