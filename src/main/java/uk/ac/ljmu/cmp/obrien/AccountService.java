
package uk.ac.ljmu.cmp.obrien;

import java.util.Date;

/**
 *
 * @author HF
 */
public class AccountService {
    private String clientId;
    private int serviceId;
    private Date periodEnd;
    private char serviceType;
    private double salesAmount;
    private double vatAmount;
    private double serviceFee;
    private double paidAmount;
    
    AccountService(int sId, String cId, Date pE, char type,
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
    
    public void setServiceId(int sId)
    {
        this.serviceId = sId;
    }
    
    public int getServiceId()
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
    
    public void setPeriodEnd(Date periodDate)
    {
        this.periodEnd = periodDate;
    }
    
    public Date getPeriedDate()
    {
        return this.periodEnd;
    }
    
    public void setServiceType(char sType)
    {
        this.serviceType = sType;
    }
    
    public char getServiceType()
    {
        return this.serviceType;
    }
    
     public void setSalesAmount(double sAmount)
    {
        this.salesAmount = sAmount;
    }
    
    public double getSalesAmount()
    {
        return this.salesAmount;
    }
    
     public void setVATAmount(double vatAmount)
    {
        this.vatAmount = vatAmount;
    }
    
    public double getVATAmount()
    {
        return this.vatAmount;
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
