/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obrien.normal;

import javax.swing.JFrame;

/**
 *
 * @author HF
 */
public class AccountingApp {
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Obrien Clients Management");
        
        /*
        DataDAO dao = new DataDAO("jdbc:mysql://localhost:3306","root","12345");
        ArrayList<AccountClient> clientList = new ArrayList<>();
        dao.retrieveClients(clientList);
        //System.out.println(clientList.size());
        
        ArrayList<AccountService> serviceList = new ArrayList<>();
        dao.retrieveServices(serviceList);
        
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        ClientPanel cp = new ClientPanel(clientList);
        ServicePanel sp = new ServicePanel(serviceList);
        splitPane.add(cp);
        splitPane.add(sp);
        splitPane.setDividerLocation(0.3);
        
        frame.getContentPane().add(splitPane);*/
        MainPanel mp = new MainPanel();
        frame.getContentPane().add(mp);
        
        frame.pack();
        frame.setVisible(true);
    }
}
