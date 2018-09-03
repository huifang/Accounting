
package org.obrien.normal;

import java.util.Date;

/**
 *
 * @author HF
 */
public class AccountService {
    private String clientId;
    private String serviceId;
    private Date periodEnd;
    private String rType;
    private double salesAmount;
    private double vatAmount;
    private double serviceFee;
    private double paidAmount;
    private Date agentNoticeDate;
    private int claimno;
    private int progress;
    
    AccountService(String sId, String cId, Date pE)
    {
        this.serviceId = sId;
        this.clientId = cId;
        this.periodEnd = pE;
    }
    
    AccountService(String sId, String cId, Date pE, String type,
            double sAmount, double vAmount, double sFee, double pAmount)
    {
        this.serviceId = sId;
        this.clientId = cId;
        this.periodEnd = pE;
        this.rType = type;
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
    
    public void setPeriodEnd(Date periodDate)
    {
        this.periodEnd = periodDate;
    }
    
    public Date getPeriedDate()
    {
        return this.periodEnd;
    }
    
    public void setServiceType(String sType)
    {
        this.rType = sType;
    }
    
    public String getServiceType()
    {
        return this.rType;
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
    
    public void setClaimNo(int cno)
    {
        this.claimno = cno;    
    }
    
    public int getClaimNo()
    {
        return this.claimno;    
    }
    
    public void setPaidAmount(double pAmount)
    {
        this.paidAmount = pAmount;
    }
    
    public double getPaidAmount()
    {
        return this.paidAmount;
    }
    
        
    public void setAgentNoticeDate(Date adate)
    {
        this.agentNoticeDate = adate;
    }
    
    public Date getAgentNoticeDate()
    {
        return this.agentNoticeDate;
    }
    
    public void setProgress(int prog)
    {
        this.progress = prog;
    }
    
    public int getProgress()
    {
        return this.progress;
    }
    
}
