
package uk.ac.ljmu.cmp.obrien;

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
    private Date repdob;
    private Date regdate;
    private String contactno;
    private String email;
    private boolean active;
    private String taxno;
    
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
}
