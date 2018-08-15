
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
    JButton servicesButton;
    JButton actionButton;
    
    ClientButtonPanel()
    {
        mailButton = new JButton("Import from Mail");
        manButton = new JButton("Import manually");
        servicesButton = new JButton("Generate batch services");
        actionButton = new JButton("Clients need Actions");
        
        this.setLayout(new FlowLayout());
        this.add(mailButton);
        this.add(manButton);
        this.add(servicesButton);
        this.add(actionButton);
    }
}
