
package uk.ac.ljmu.cmp.obrien;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author cmphfang
 */
public class ClientButtonPanel extends JPanel {
    JButton mailButton;
    JButton manButton;
    JButton activeButton;
    JButton actionButton;
    
    ClientButtonPanel()
    {
        mailButton = new JButton("Import from Mail");
        manButton = new JButton("Import manually");
        activeButton = new JButton("All/Active Clients");
        actionButton = new JButton("Clients need Actions");
        
        this.setLayout(new FlowLayout());
        this.add(mailButton);
        this.add(manButton);
        this.add(activeButton);
        this.add(actionButton);
    }
}
