package uk.ac.ljmu.cmp.obrien;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author cmphfang
 */
public class ServiceButtonPanel extends JPanel {
    JButton mailButton;
    JButton manButton;
    JButton calButton;
    JButton actionButton;
    
    ServiceButtonPanel()
    {
        mailButton = new JButton("Update from Mail");
        manButton = new JButton("Update manually");
        calButton = new JButton("Calculate Service Fees");
        actionButton = new JButton("Services need Actions");
        
        this.setLayout(new FlowLayout());
        this.add(mailButton);
        this.add(manButton);
        this.add(calButton);
        this.add(actionButton);
    }
}
