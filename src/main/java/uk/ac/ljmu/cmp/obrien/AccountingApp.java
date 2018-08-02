/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ljmu.cmp.obrien;

import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author HF
 */
public class AccountingApp {
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Obrien Clients Management");
        
        //get database connection and load data
        DataDAO dao = new DataDAO("jdbc:mysql://localhost:3306","root","12345");
        ArrayList<Client> clientList = new ArrayList<>();
        dao.retrieveClients(clientList);
        System.out.println(clientList.size());
        
        ClientPanel cp = new ClientPanel(clientList);
        frame.getContentPane().add(cp);
        
        frame.pack();
        frame.setVisible(true);
    }
}
