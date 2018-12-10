package org.obrien.normal;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String repdob;
    private Date regdate;
    private String contactno;
    private String email;
    private boolean active;
    private String taxno;
    private String comments;
    
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
    
    AccountClient(String agencyid)
    {
        this.agencyid = agencyid;
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
    
    public String getRepDOB()
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
    
    public void setCName(String cname)
    {
        this.cname = cname;
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
    
    public void setRepDob (String dob)
    {
        this.repdob = dob;
    }
    
    public void setContactNo(String tel)
    {
        this.contactno = tel;
    }
    
    public void setTaxNo(String taxno)
    {
        this.taxno = taxno;
    }
}
