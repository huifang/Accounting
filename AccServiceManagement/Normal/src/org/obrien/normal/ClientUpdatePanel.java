
package org.obrien.normal;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author cmphfang
 */
public class ClientUpdatePanel extends JPanel{
    
    JLabel cNo;
    JTextField cNoBox;
    
    JLabel cNameEnglish;
    JTextField cNameEBox;
    
    JLabel cNameChinese;
    JTextField cNameCBox;
    
    JLabel cAddress;
    JTextField cAddressBox;
    
    JLabel repName;
    JTextField repNameBox;
    
    JLabel repDob;
    JTextField repDobBox;
    
    JLabel contactEmail;
    JTextField contactEmailBox;
    
    JLabel tel;
    JTextField telBox;
    
    JLabel taxno;
    JTextField taxnoBox;
    
    JLabel rtype;
    JTextField rtypeBox;
    
    JLabel retDate;
    JTextField retDateBox;
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    
    ClientUpdatePanel(AccountClient ac)
    {
        this.setLayout(new GridLayout(11,2));
            
        cNo = new JLabel("Client Ref.:");
        cNoBox = new JTextField();
        cNoBox.setText(ac.getAgencyID());
        cNameEnglish = new JLabel("Company Name(E):");
        cNameEBox = new JTextField();
        cNameEBox.setText(ac.getEname());
        cNameChinese = new JLabel("Company Name(C):");
        cNameCBox = new JTextField();
        cNameCBox.setText(ac.getCname());
        cAddress = new JLabel("Company Address:");
        cAddressBox = new JTextField();
        cAddressBox.setText(ac.getAddress());
        repName = new JLabel("Representative Name:");
        repNameBox = new JTextField();
        repNameBox.setText(ac.getRepName());
        repDob = new JLabel("Representative DOB:");
        repDobBox = new JTextField();
        repDobBox.setText(dateFormat.format(ac.getRepDOB()));
        contactEmail = new JLabel("Contact Email:");
        contactEmailBox = new JTextField();
        contactEmailBox.setText(ac.getMail());
        tel = new JLabel("Contact No.:");
        telBox = new JTextField();
        telBox.setText(ac.getContactNo());
        taxno = new JLabel("Tax Ref. No.:");
        taxnoBox = new JTextField();
        
        rtype = new JLabel("Tax Return Type:");
        rtypeBox = new JTextField();
        retDate = new JLabel("Latest Return Date:");
        retDateBox = new JTextField();
    
        this.add(cNo);
        this.add(cNoBox);
        this.add(cNameEnglish);
        this.add(cNameEBox);
        this.add(cNameChinese);
        this.add(cNameCBox);
        this.add(cAddress);
        this.add(cAddressBox);
        this.add(repName);
        this.add(repNameBox);
        this.add(repDob);
        this.add(repDobBox);
        this.add(contactEmail);
        this.add(contactEmailBox);
        this.add(tel);
        this.add(telBox);
        this.add(taxno);
        this.add(taxnoBox);
        this.add(rtype);
        this.add(rtypeBox);
        this.add(retDate);
        this.add(retDateBox);
        
    }
    
}
