
package uk.ac.ljmu.cmp.obrien;

import java.util.Date;

/**
 *
 * @author HF
 */
public class AccountService {
    private String clientId;
    private String serviceId;
    private Date periodEnd;
    private char serviceType;
    private double salesAmount;
    private double vatAmount;
    private double serviceFee;
    private double paidAmount;
    
    AccountService(String sId, String cId, Date pE, char type,
            double sAmount, double vAmount, double sFee, double pAmount)
    {
        this.serviceId = sId;
        this.clientId = cId;
        this.periodEnd = pE;
        this.serviceType = type;
        this.salesAmount = sAmount;
        this.vatAmount = vAmount;
        this.serviceFee = sFee;
        this.paidAmount = pAmount;
    }
    
    public void setServiceId(String sId)
    {
        this.serviceId = sId;
    }
    
    public String getServiceId()
    {
        return this.serviceId;
    }
    
    public void setClientId(String cId)
    {
        this.clientId = cId;
    }
    
    public String getClientId()
    {
        return this.clientId;
    }
    
    public void setServiceType(char sType)
    {
        this.serviceType = sType;
    }
    
    public char getServiceType()
    {
        return this.serviceType;
    }
    
    public void setServiceFee(double sFee)
    {
        this.serviceFee = sFee;
    }
    
    public double getServiceFee()
    {
        return this.serviceFee;
    }
    
    public void setPaidAmount(double pAmount)
    {
        this.paidAmount = pAmount;
    }
    
    public double getPaidAmount()
    {
        return this.paidAmount;
    }
}
