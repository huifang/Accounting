
package org.obrien.normal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author cmphfang
 */
public class DataDAO {
    
    //private PreparedStatement loadStmt;
    private String url;
    private String user;
    private String pw;
    private Connection con;
    
    DataDAO(String url, String user, String pw)
    {
        this.url = url;
        this.user = user;
        this.pw = pw;
    }
    
    public void getConnection()
    {
        try {
           con = DriverManager.getConnection(url, user, pw);
        } catch(SQLException ex) {
            System.out.println("Cannot Connect to the Database!");
        }
    }
    
    public void retrieveClients(ArrayList<AccountClient> clientList)
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT agencyID, cname, email, taxno "
                + "from obriensmanagement.clients;";
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpAgencyID = rs.getString(1);
                String tmpname = rs.getString(2);
                String email = "";
                String taxno = "";
                if(rs.getString(3)!=null)
                    email = rs.getString(3);
                if(rs.getString(3)!=null)
                    taxno = rs.getString(4);
                
                AccountClient tmpClient = new AccountClient(tmpAgencyID, tmpname);
                tmpClient.setMail(email);
                tmpClient.setTaxNo(taxno);
                
                clientList.add(tmpClient);
            }
            
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Client data!");
        }
    }
    
    void retrieveClientDetails(AccountClient tmpC) {
        System.out.println("This needs to be implemented to retrieve client details");
    }
    
      public void retrieveServices(ArrayList<AccountService> serviceList)
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
                + "from obriensmanagement.services;";
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpsID = rs.getString(1);
                String tmpcID = rs.getString(2);
                Date tmpPE = new Date(rs.getDate(3).getTime());
                String tmpStype = rs.getString(4);
                double tmpsAmount = rs.getDouble(5);
                double tmpvAmount = rs.getDouble(6);
                double tmpsFee = rs.getDouble(7);
                double tmppAmount = rs.getDouble(8);
                int progress = rs.getInt(9);
                
                AccountService tmpService = new AccountService(tmpsID, tmpcID, tmpPE, tmpStype,tmpsAmount, tmpvAmount,tmpsFee, tmppAmount);
                tmpService.setProgress(progress);
                serviceList.add(tmpService);
            }
            
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Service data!");
        }
    }
    
    public void retrieveSelectedServices(ArrayList<AccountService> serviceList, String clientID)
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
                + "from obriensmanagement.services where services.clientID = \"" + clientID +"\";";
        //System.out.println(query);
        serviceList.clear();
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpsID = rs.getString(1);
                String tmpcID = rs.getString(2);
                Date tmpPE = new Date(rs.getDate(3).getTime());
                String tmpStype = rs.getString(4);
                double tmpsAmount = rs.getDouble(5);
                double tmpvAmount = rs.getDouble(6);
                double tmpsFee = rs.getDouble(7);
                double tmppAmount = rs.getDouble(8);
                int progress = rs.getInt(9);
                
                AccountService tmpService = new AccountService(tmpsID, tmpcID, tmpPE, tmpStype,tmpsAmount, tmpvAmount,tmpsFee, tmppAmount);
                tmpService.setProgress(progress);
                serviceList.add(tmpService);
            }
            
            con.close();
            
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Service data!");
        }
    }
    
    public void retrieveUncompletedServices(ArrayList<AccountService> serviceList)
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
                + "from obriensmanagement.services where services.progress != 3 and services.progress != 4;";
        //System.out.println(query);
        serviceList.clear();
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpsID = rs.getString(1);
                String tmpcID = rs.getString(2);
                Date tmpPE = new Date(rs.getDate(3).getTime());
                String tmpStype = rs.getString(4);
                double tmpsAmount = rs.getDouble(5);
                double tmpvAmount = rs.getDouble(6);
                double tmpsFee = rs.getDouble(7);
                double tmppAmount = rs.getDouble(8);
                int progress = rs.getInt(9);
                
                AccountService tmpService = new AccountService(tmpsID, tmpcID, tmpPE, tmpStype,tmpsAmount, tmpvAmount,tmpsFee, tmppAmount);
                tmpService.setProgress(progress);
                serviceList.add(tmpService);
            }
            
            con.close();
            
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Service data!");
        }
    }
        
    //here is for insert
    public void insertNewClient(AccountClient c)
    {
        //Statement st = null;
        //String query = "insert into obriensmanagement.clients(agencyID, cname, ename,"
        //        + "address, repname,regdate,conatactno,email) values(?,?,?,?,?,?,?,?,?);";
          String query = "insert into obriensmanagement.clients(agencyID, cname) values(?,?);";      
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,c.getAgencyID());
            st.setString(2,c.getCname());
            //st.setString(3,c.getEname());
            //st.setString(4,c.getAddress());
            //st.setString(5,c.getRepName());
            //st.setDate(6, java.sql.Date.valueOf(c.getRepDOB().));
            //LocalDate ld = LocalDate.now();
            //st.setDate(6, java.sql.Date.valueOf(ld));
            //st.setString(7, c.getContactNo());
            //st.setString(8, c.getMail());
            
            st.execute();
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot insert client due to ...");
        }
    }

}
