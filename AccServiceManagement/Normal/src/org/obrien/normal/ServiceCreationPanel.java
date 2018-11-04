
package org.obrien.normal;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author cmphfang
 */
public class ServiceCreationPanel extends JPanel implements ActionListener {
    
    ButtonGroup bg;
    
    JRadioButton cTaxServiceButton;
    JRadioButton cRegButton;
    JRadioButton cRenameButton;
    JRadioButton cChAddButton;
    JRadioButton cEORIButton;
    JRadioButton cQuitButton;
    
    
    JLabel clientID;
    JTextField cClientIDBox;
    
    JLabel cYearMonth;
    JTextField cYearMonthBox;
    String initLetter="R";
    
    ServiceCreationPanel(String selClientID)
    {
        this.setLayout(new GridLayout(5,2));
            
        bg = new ButtonGroup();
        cTaxServiceButton = new JRadioButton("Regular tax return");
        cTaxServiceButton.setActionCommand("R");
        cTaxServiceButton.setSelected(true);
        cRegButton = new JRadioButton("New Registration");
        cRegButton.setActionCommand("N");
        cRenameButton = new JRadioButton("Change Name");
        cRenameButton.setActionCommand("U");
        cChAddButton = new JRadioButton("Change Address");
        cChAddButton.setActionCommand("A");
        cEORIButton = new JRadioButton("Apply EORI");
        cEORIButton.setActionCommand("E");
        cQuitButton = new JRadioButton("Cancel Account");
        cQuitButton.setActionCommand("Q");
        
        bg.add(cTaxServiceButton);
        bg.add(cRegButton);
        bg.add(cRenameButton);
        bg.add(cChAddButton);
        bg.add(cEORIButton);
        bg.add(cQuitButton);
        
        cTaxServiceButton.addActionListener(this);
        cRegButton.addActionListener(this);
        cRenameButton.addActionListener(this);
        
        clientID = new JLabel("Client ID");
        cYearMonth = new JLabel("Service Month (Format: YYYY-MM)");
        cClientIDBox = new JTextField(selClientID);
        cYearMonthBox = new JTextField("");
        
        this.add(cTaxServiceButton);
        this.add(cRegButton);
        this.add(cRenameButton);
        this.add(cChAddButton);
        this.add(cEORIButton);
        this.add(cQuitButton);
        this.add(clientID);
        this.add(cClientIDBox);
        this.add(cYearMonth);
        this.add(cYearMonthBox);
 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       initLetter = String.valueOf(e.getActionCommand());
    }

    public String getInitLetter()
    {
        return this.initLetter;
    }
}
