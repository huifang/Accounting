/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obrien.normal;

import java.util.Date;

/**
 *
 * @author cmphfang
 */
public class MessageRecord {
    
    String subject;
    String clientID;
    String serviceType;
    String relatedserviceID;
    Date receiveTime;
    
    MessageRecord(String subject, Date receiveTime)
    {
        this.subject = subject;
        this.receiveTime = receiveTime;
    }
    
    public void setClientID(String cID)
    {
        this.clientID = cID;
    }
    
    public String getClientID()
    {
        return this.clientID;
    }
    
    public void setServiceType(String sType)
    {
        this.serviceType = sType;
    }
    
    public String getServiceType()
    {
        return this.serviceType;
    }
    
    public void setRelatedServiceID(String sID)
    {
        this.relatedserviceID = sID;
    }
    
    public String getRelatedServiceID()
    {
        return this.relatedserviceID;
    }
    
    public String getSubject()
    {
        return this.subject;        
    }
    
    public Date getReceiveTime()
    {
        return this.getReceiveTime();
    }
    
}
