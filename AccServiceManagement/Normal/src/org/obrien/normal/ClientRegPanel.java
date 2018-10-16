
package org.obrien.normal;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author cmphfang
 */
public class ClientRegPanel extends JPanel{
    
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
    
    ClientRegPanel()
    {
        this.setLayout(new GridLayout(8,2));
            
        cNo = new JLabel("Client Unique Reference");
        cNoBox = new JTextField("",30);
        cNameEnglish = new JLabel("Company Name(E)");
        cNameEBox = new JTextField("",30);
        cNameChinese = new JLabel("Company Name(C)");
        cNameCBox = new JTextField("",30);
        cAddress = new JLabel("Company Address");
        cAddressBox = new JTextField("",30);
        repName = new JLabel("Representative Name");
        repNameBox = new JTextField("",30);
        repDob = new JLabel("Rep. DOB (Format:yyyy-mm-dd)");
        repDobBox = new JTextField("",30);
        contactEmail = new JLabel("Contact Email");
        contactEmailBox = new JTextField("",30);
        tel = new JLabel("Contact No.");
        telBox = new JTextField("",30);
        
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
    }
    
}
