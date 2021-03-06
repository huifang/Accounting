
package org.obrien.normal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;

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
        String query = "SELECT agencyID, cname, email, taxno, ename "
                + "from obriensmanagement.clients;";
        //String query = "SELECT agencyID, cname, email, taxno, ename "
        //        + "from obriensc_management.clients;";
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpAgencyID = rs.getString(1);
                String tmpname = rs.getString(2);
                String email = "";
                String taxno = "";
                String ename = "";
                if(rs.getString(3)!=null)
                    email = rs.getString(3);
                if(rs.getString(4)!=null)
                    taxno = rs.getString(4);
                if(rs.getString(5)!=null)
                    ename = rs.getString(5);
                
                AccountClient tmpClient = new AccountClient(tmpAgencyID, tmpname);
                tmpClient.setMail(email);
                tmpClient.setEName(ename);
                tmpClient.setTaxNo(taxno);
                
                clientList.add(tmpClient);
            }
            
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Client data!");
        }
    }
    
    void retrieveClientDetails(String cid, AccountClient tmpC) {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT agencyID, cname, ename, address, repname, repdob, email, contactno,taxno "
                + "from obriensmanagement.clients where agencyID =\"" + cid + "\";";
        //String query = "SELECT agencyID, cname, ename, address, repname, repdob, email, contactno,taxno "
        //        + "from obriensc_management.clients where agencyID =\"" + cid + "\";";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpAgencyID = rs.getString(1);
                String tmpname = rs.getString(2);
                
                tmpC.setCName(tmpname);
                if(rs.getString(3)!=null)
                   tmpC.setEName(rs.getString(3));
                if(rs.getString(4)!=null)
                   tmpC.setAddress(rs.getString(4));
                if(rs.getString(5)!=null)
                   tmpC.setRepName(rs.getString(5));
                //will get the dob here
                if(rs.getDate(6)!=null)
                {
                    java.sql.Date d1 = rs.getDate(6);
                    String dstr = sdf1.format(d1);
                    tmpC.setRepDob(dstr);
                }
                if(rs.getString(7)!=null)
                   tmpC.setMail(rs.getString(7));
                if(rs.getString(8)!=null)
                    tmpC.setContactNo(rs.getString(8));
                if(rs.getString(9)!=null)
                    tmpC.setTaxNo(rs.getString(9));

            }
            
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Client data!");
        }
    }
    
    public void retrieveLatestReturnDate(Map returnMap) {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT agencyID, latestreturn "
                + "from obriensmanagement.clients;";
        //String query = "SELECT agencyID, latestreturn "
        //        + "from obriensc_management.clients;";
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpcID;
                Date tmpDate;
                if (rs.getDate(2)!=null)
                {
                    tmpcID = rs.getString(1);
                    tmpDate = new Date(rs.getDate(2).getTime());
                    returnMap.put(tmpcID, tmpDate);
                }
            }
            
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Client data!");
        }
    }
     
    void retrieveAllServiceID(ArrayList<String> serviceIDAll) {
        Statement st;
        ResultSet rs;
        String query = "SELECT serviceID from obriensmanagement.services;";
        //String query = "SELECT serviceID from obriensc_management.services;";
        
        try {
            getConnection();
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                String tmpsID = rs.getString(1);
                serviceIDAll.add(tmpsID);
            }
            
            con.close();
        } catch (SQLException ex) {
             System.out.println("Cannot retrieve Service data!");
        }
    }

    public void retrieveServices(ArrayList<AccountService> serviceList)
    {
        Statement st = null;
        ResultSet rs = null;
        //String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
        //        + "from obriensmanagement.services;";
        String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
                + "from obriensmanagement.services;";
        
        //String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
        //        + "from obriensc_management.services;";
        
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
        //String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
        //        + "from obriensc_management.services where services.clientID = \"" + clientID +"\";";
        
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
        //String query = "SELECT serviceID, clientID, periodEnd, rtype, saleamount, vatamount, servicefee, paidamount, progress "
        //        + "from obriensc_management.services where services.progress != 3 and services.progress != 4;";
        
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
        
    public String findSingleService(String request)
    {
        String query = "select serviceID from obriensmanagement.services where serviceID like " + request;
        return "Not implemented";
    }
    
    
    public String findServiceID(String requestLikeID) {
        String retID="";
        
        String query = "select serviceID from obriensmanagement.services where serviceID like \"" + requestLikeID + "\" order by periodEnd DESC LIMIT 1;";
        
        //String query = "select serviceID from obriensc_management.services where serviceID like " + requestLikeID + "order by periodEnd DESC LIMIT 1;";
                       
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
            //st.setDate(1, sqlDate);
        
            ResultSet rs = st.executeQuery(query);
            
            if(rs.next()) {
                retID = rs.getString(1);
            }
            
            con.close();
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null,"Cannot retrieve any relevant services from the database...");
        }
        
        return retID;
    }
    
    public int findNoService(String request, Date receiveDate)
    {
        int noService = 0;
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        java.sql.Date sqlDate = new java.sql.Date(receiveDate.getTime());
        String query = "select count(*) from obriensmanagement.services where serviceID like \"" + request + "\" and (createtime <> ? || createtime is null)";
        //String query = "select count(*) from obriensc_management.services where serviceID like \"" + request + "\" and (createtime <> ? || createtime is null)";
                
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
            //st.setDate(1, sqlDate);
        
            ResultSet rs = st.executeQuery(query);
            
            if(rs.next()) {
                noService = rs.getInt(1);
            }
            
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Cannot insert client due to some inputs are not correct");
        }
        
        return noService;
    }
    
    //here is for insert
    public void insertNewClient(AccountClient c) 
    {
        //Statement st = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String query = "insert into obriensmanagement.clients(agencyID, cname, ename,"
                + "address,repname,repdob,regdate,contactno,email) values(?,?,?,?,?,?,?,?,?);";
        //String query = "insert into obriensc_management.clients(agencyID, cname, ename,"
        //        + "address,repname,repdob,regdate,contactno,email) values(?,?,?,?,?,?,?,?,?);";
        
        java.util.Date d =null;
        try {
            d = sdf1.parse(c.getRepDOB());
            
            try {
                getConnection();
                PreparedStatement st = con.prepareStatement(query);
            
                st.setString(1,c.getAgencyID());
                st.setString(2,c.getCname());
                st.setString(3,c.getEname());
                st.setString(4,c.getAddress());
                st.setString(5,c.getRepName());
                st.setDate(6, new java.sql.Date(d.getTime()));
                LocalDate ld = LocalDate.now();
                st.setDate(7, java.sql.Date.valueOf(ld));
                st.setString(8, c.getContactNo());
                st.setString(9, c.getMail());

                st.execute();
                con.close();
            } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,"Cannot insert client due to some inputs are not correct");
            }
        } catch(ParseException e){
            JOptionPane.showMessageDialog(null, "The input DOB is not in correct format!");
        }
    }

    void insertNewService(String newServiceID) {
        
        String query = "insert into obriensmanagement.services(serviceID, clientID, periodEnd, progress, createtime) values(?,?,?,?,?);";   
        //String query = "insert into obriensc_management.services(serviceID, clientID, periodEnd, progress, createtime) values(?,?,?,?,?);";   
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-yyyy");
        try {
            getConnection();
            
            PreparedStatement st = con.prepareStatement(query);

            String serviceID = newServiceID;
            String clientID = newServiceID.substring(9,newServiceID.length());
            String dateString = newServiceID.substring(5,7) + "-" + newServiceID.substring(1,5);
            java.util.Date date = sdf1.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());  
                //System.out.println(clientID + "  " + dateString);
            st.setString(1,serviceID);
            st.setString(2,clientID);
            st.setDate(3,sqlDate);
            st.setInt(4, 0);
            st.setDate(5, new java.sql.Date((new java.util.Date()).getTime()));
            st.execute();
            con.close();
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null,"Cannot insert service batch due to ...");
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Cannot convert the String to date");
        }
    }
    
    void insertBatchServices(ArrayList<String> newServiceID) {
        
        String query = "insert into obriensmanagement.services(serviceID, clientID, periodEnd, progress) values(?,?,?,?);";   
        //String query = "insert into obriensc_management.services(serviceID, clientID, periodEnd, progress) values(?,?,?,?);";   
        
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-yyyy");
        try {
            getConnection();
            con.setAutoCommit(false);
            
            PreparedStatement st = con.prepareStatement(query);
            for(int i = 0; i<newServiceID.size(); i++)
            {
                String serviceID = newServiceID.get(i);
                String clientID = newServiceID.get(i).substring(9,newServiceID.get(i).length());
                String dateString = newServiceID.get(i).substring(5,7) + "-" + newServiceID.get(i).substring(1,5);
                java.util.Date date = sdf1.parse(dateString);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());  
                //System.out.println(clientID + "  " + dateString);
                st.setString(1,serviceID);
                st.setString(2,clientID);
                st.setDate(3,sqlDate);
                st.setInt(4, 0);
                
                st.addBatch();
                //st.setString(4,c.getAddress());
                //st.setString(5,c.getRepName());
                //st.setDate(6, java.sql.Date.valueOf(c.getRepDOB().));
                //LocalDate ld = LocalDate.now();
                //st.setDate(6, java.sql.Date.valueOf(ld));
                //st.setString(7, c.getContactNo());
                //st.setString(8, c.getMail());
            }
            int [] updateCounts = st.executeBatch();
            con.commit();
            con.setAutoCommit(true);
            con.close();
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null,"Cannot insert service batch due to ...");
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Cannot convert the String to date");
        }
    }

    public void updateClientNoTax(AccountClient c) 
    {
        //Statement st = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String query = "update obriensmanagement.clients set cname=?,"
                + " ename=?, address=?, repname = ?, repdob=?, contactno=?,email=?"
                + " where agencyID= \"" + c.getAgencyID() + "\";";
        //String query = "update obriensc_management.clients set cname=?,"
        //        + " ename=?, address=?, repname = ?, repdob=?, contactno=?,email=?"
        //        + " where agencyID= \"" + c.getAgencyID() + "\";";
        //  String query = "insert into obriensmanagement.clients(agencyID, cname) values(?,?);";      
        java.util.Date d =null;
        try {
            d = sdf1.parse(c.getRepDOB());
            
            try {
                getConnection();
                PreparedStatement st = con.prepareStatement(query);
            
                st.setString(1,c.getCname());
                st.setString(2,c.getEname());
                st.setString(3,c.getAddress());
                st.setString(4,c.getRepName());
                st.setDate(5, new java.sql.Date(d.getTime()));
                st.setString(6, c.getContactNo());
                st.setString(7, c.getMail());

                st.execute();
                con.close();
            } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,"Cannot update client due to some inputs are not correct");
            }
        } catch(ParseException e){
            JOptionPane.showMessageDialog(null, "The input DOB is not in correct format!");
        }
    }
    
    public void updateClient(AccountClient c) 
    {
        //Statement st = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String query = "update obriensmanagement.clients set cname=?,"
                + " ename=?, address=?, repname = ?, repdob=?, contactno=?,email=?, taxno=?"
                + " where agencyID= \"" + c.getAgencyID() + "\";";
        //String query = "update obriensc_management.clients set cname=?,"
        //        + " ename=?, address=?, repname = ?, repdob=?, contactno=?,email=?, taxno=?"
        //        + " where agencyID= \"" + c.getAgencyID() + "\";";
      
        java.util.Date d =null;
        try {
            d = sdf1.parse(c.getRepDOB());
            
            try {
                getConnection();
                PreparedStatement st = con.prepareStatement(query);
            
                st.setString(1,c.getCname());
                st.setString(2,c.getEname());
                st.setString(3,c.getAddress());
                st.setString(4,c.getRepName());
                st.setDate(5, new java.sql.Date(d.getTime()));
                st.setString(6, c.getContactNo());
                st.setString(7, c.getMail());
                st.setString(8,c.getTaxNo());
                
                st.execute();
                con.close();
            } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Cannot update client due to some inputs are not correct");
            }
        } catch(ParseException e){
            JOptionPane.showMessageDialog(null, "The input DOB is not in correct format!");
        }
    }  
    
    //date1 is effective date and date 2 is first return date
    public void updateTaxInfo(String cid, String taxno, String type, String Date1, String Date2) 
    {
        //Statement st = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MMM-dd");
        String query = "update obriensmanagement.clients set taxno=?,"
                + " periodtype=?, effectivedate=?, firstreturn=?"
                + " where agencyID= \"" + cid + "\";";
        //String query = "update obriensc_management.clients set taxno=?,"
        //        + " periodtype=?, effectivedate=?, firstreturn=?"
        //        + " where agencyID= \"" + cid + "\";";
 
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
           
            st.setString(1,taxno);
            st.setString(2,type);
            java.util.Date date1 = sdf1.parse(Date1);
            java.util.Date date2 = sdf1.parse(Date2);
            st.setDate(3, new java.sql.Date(date1.getTime()));
            st.setDate(4,new java.sql.Date(date2.getTime()));
                
            st.execute();
            con.close();
        } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Cannot update client due to some inputs are not correct");
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
        //date1 is effective date and date 2 is first return date
    public void CompleteTaxService(String sid, double sAmount, double vatAmount) 
    {
        
        String query = "update obriensmanagement.services set saleamount=?,"
                + " vatamount=?, progress=?"
                + " where serviceID= \"" + sid + "\";";
        //String query = "update obriensc_management.services set saleamount=?,"
        //        + " vatamount=?, progress=?"
        //        + " where serviceID= \"" + sid + "\";";     
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
           
            st.setDouble(1, sAmount);
            st.setDouble(2, vatAmount);
            st.setInt(3, 3);
             
            System.out.println(query);
            
            st.execute();
            con.close();
        } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Cannot update service, please contact with the adminstrator.");
        } 
    }
    
    public void UpdateLatestReturn(String sid) 
    {
        String cid = sid.substring(9,sid.length());
        System.out.println(cid);
        String date = sid.substring(1,5) + "-" + sid.substring(5,7) + "-" + sid.substring(7,9);
        System.out.println(date);
        String query = "update obriensmanagement.clients set latestreturn=\"" + date + "\" where agencyID= \"" + cid + "\";";
        //String query = "update obriensc_management.services set saleamount=?,"
        //        + " vatamount=?, progress=?"
        //        + " where serviceID= \"" + sid + "\";";     
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
           

            System.out.println(query);
            
            st.execute();
            con.close();
        } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Cannot update service, please contact with the adminstrator.");
        } 
    }
    
    void updateServiceProgress(String sid, int progSelection) {
        
        String query = "update obriensmanagement.services set progress=?"
                + " where serviceID= \"" + sid + "\";";
        //String query = "update obriensc_management.services set progress=?"
        //        + " where serviceID= \"" + sid + "\";";
        
        try {
            getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, progSelection);
                        
            st.execute();
            con.close();
        } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Cannot update service, please contact with the adminstrator.");
        } 
    }
}
