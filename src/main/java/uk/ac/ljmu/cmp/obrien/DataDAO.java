
package uk.ac.ljmu.cmp.obrien;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author cmphfang
 */
public class DataDAO {
    
    //private PreparedStatement loadStmt;
    private Connection con;
    DataDAO(String url, String user, String pw)
    {
        try {
           con = DriverManager.getConnection(url, user, pw);
        } catch(SQLException ex) {
            System.out.println("Cannot Connect to the Database!");
        }
    }
    
    public void retrieveClients(ArrayList<Client> clientList)
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT clientid, agencyID, cname "
                + "from obriensmanagement.clients;";
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                int tmpID = rs.getInt(1);
                String tmpAgencyID = rs.getString(2);
                String tmpname = rs.getString(3);
                Client tmpClient = new Client(tmpID, tmpAgencyID, tmpname);
                clientList.add(tmpClient);
            }
            
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Client data!");
        }
    }
}
