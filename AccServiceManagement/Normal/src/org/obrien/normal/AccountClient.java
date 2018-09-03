package org.obrien.normal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmphfang
 */
public class AccountClient {
    
    private String agencyid;
    private String cname;
    private String ename;
    private String address;
    private String repname;
    private Date repdob;
    private Date regdate;
    private String contactno;
    private String email;
    private boolean active;
    private String taxno;
    private String comments;
    
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
    
    AccountClient()
    {
        agencyid = "";
        cname = "";
        ename = "";
        address = "";
        repname = "";
        contactno = "";
        email = "";
        taxno = "";
        //try {
            //repdob = sf.parse("1985-06");
            //regdate = sf.parse("2017-08");
        //} catch (ParseException ex) {
        //    Logger.getLogger(AccountClient.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
    
    AccountClient(String agencyid, String cname)
    {
        this.agencyid = agencyid;
        this.cname = cname;
    }
    
    public String getAgencyID()
    {
        return this.agencyid;
    }
    
    public String getCname()
    {
        return this.cname;
    }
    
    public String getEname()
    {
        return this.ename;
    }
    
    public String getAddress()
    {
        return this.address;
    }
    
    public String getRepName()
    {
        return this.repname;
    }
    
    public Date getRepDOB()
    {
        return this.repdob;
    }
    
    public String getMail()
    {
        return this.email;
    }
    
    public String getContactNo()
    {
        return this.contactno;
    }
    
    public String getTaxNo()
    {
        return this.taxno;
    }
    
    public String getComments()
    {
        return this.comments;
    }
    
    public void setMail(String mail)
    {
        this.email = mail;
    }
    
    public void setEName(String ename)
    {
        this.ename = ename;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public void setRepName(String repname)
    {
        this.repname = repname;
    }
    
    public void setTaxNo(String taxno)
    {
        this.taxno = taxno;
    }
}
