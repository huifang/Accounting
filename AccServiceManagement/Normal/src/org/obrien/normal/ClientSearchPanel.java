/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obrien.normal;

import java.awt.BorderLayout;
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
public class ClientSearchPanel extends JPanel implements ActionListener {
    
    JTextField inputField = new JTextField();
    ButtonGroup bg;
    JRadioButton cIDButton;
    JRadioButton cNameButton;
    JRadioButton cTaxButton;
    int searchBy = 0;
    
    ClientSearchPanel()
    {
        this.setLayout(new GridLayout(4,1));
        bg = new ButtonGroup();
        cIDButton = new JRadioButton("By ClientID");
        cIDButton.setActionCommand("0");
        cIDButton.setSelected(true);
        cNameButton = new JRadioButton("By Company English Name");
        cNameButton.setActionCommand("1");
        cTaxButton = new JRadioButton("By Tax Ref. NO");
        cTaxButton.setActionCommand("2");
        bg.add(cIDButton);
        bg.add(cNameButton);
        bg.add(cTaxButton);
        
        cIDButton.addActionListener(this);
        cNameButton.addActionListener(this);
        cTaxButton.addActionListener(this);
        
        this.add(cIDButton);
        this.add(cNameButton);
        this.add(cTaxButton);
        this.add(inputField);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        searchBy = Integer.valueOf(e.getActionCommand());
    }
    
    public int getSearchBy()
    {
        return this.searchBy;
    }
    
    public String getSearchText()
    {
        return inputField.getText();
    }
}
