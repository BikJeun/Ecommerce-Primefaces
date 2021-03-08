package datamodel.ws;

import ejb.session.stateful.CheckoutSessionBeanLocal;
import java.util.Date;



public class RemoteCheckoutManager 
{
    private String sessionKey;
    private CheckoutSessionBeanLocal checkoutSessionBeanLocal;
    private Date expiryDateTime;

    
    
    public RemoteCheckoutManager() 
    {
    }

    
    
    public RemoteCheckoutManager(String sessionKey, CheckoutSessionBeanLocal checkoutSessionBeanLocal, Date expiryDateTime) 
    {
        this.sessionKey = sessionKey;
        this.checkoutSessionBeanLocal = checkoutSessionBeanLocal;
        this.expiryDateTime = expiryDateTime;
    }

    
    
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public CheckoutSessionBeanLocal getCheckoutSessionBeanLocal() {
        return checkoutSessionBeanLocal;
    }

    public void setCheckoutSessionBeanLocal(CheckoutSessionBeanLocal checkoutSessionBeanLocal) {
        this.checkoutSessionBeanLocal = checkoutSessionBeanLocal;
    }

    public Date getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(Date expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }
}