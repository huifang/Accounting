
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
public class ServiceProgressUpdatePanel extends JPanel implements ActionListener {
    
    ButtonGroup bg;
    JRadioButton cWaitButton;
    JRadioButton cCompleteButton;
    JRadioButton cManualButton;
    int cProgress = 0;
    
    ServiceProgressUpdatePanel()
    {
        this.setLayout(new GridLayout(4,1));
        bg = new ButtonGroup();
        cWaitButton = new JRadioButton("Wait reply from client");
        cWaitButton.setActionCommand("2");
        cWaitButton.setSelected(true);
        cCompleteButton = new JRadioButton("Service Completion");
        cCompleteButton.setActionCommand("3");
        cManualButton = new JRadioButton("Require action");
        cManualButton.setActionCommand("1");
        bg.add(cWaitButton);
        bg.add(cCompleteButton);
        bg.add(cManualButton);
        
        cWaitButton.addActionListener(this);
        cCompleteButton.addActionListener(this);
        cManualButton.addActionListener(this);
        
        this.add(cWaitButton);
        this.add(cCompleteButton);
        this.add(cManualButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cProgress = Integer.valueOf(e.getActionCommand());
    }
    
    public int getSearchBy()
    {
        return this.cProgress;
    }

}
