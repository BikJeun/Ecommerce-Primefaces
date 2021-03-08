package ejb.session.singleton;

import datamodel.ws.RemoteCheckoutManager;
import ejb.session.stateful.CheckoutSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.exception.RemoteCheckoutManagerNotFoundException;



@Singleton
@LocalBean

public class RemoteCheckoutSessionBean 
{
    private List<datamodel.ws.RemoteCheckoutManager> remoteCheckoutManagers;

    
    
    public RemoteCheckoutSessionBean() 
    {
        remoteCheckoutManagers = new ArrayList<>();
    }
    
    
    
    @Lock(LockType.WRITE)
    public String createNewRemoteCheckoutManager()
    {        
        CheckoutSessionBeanLocal checkoutSessionBeanLocal = lookupCheckoutSessionBeanLocal();
        
        if(checkoutSessionBeanLocal != null)
        {
            String sessionKey = UUID.randomUUID().toString();
            Date expiryDateTime = new Date();
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(expiryDateTime);
            gregorianCalendar.add(GregorianCalendar.MINUTE, 5);
            expiryDateTime = gregorianCalendar.getTime();
            RemoteCheckoutManager remoteCheckoutManager = new RemoteCheckoutManager(sessionKey, checkoutSessionBeanLocal, expiryDateTime);
            remoteCheckoutManagers.add(remoteCheckoutManager);
            
            System.out.println("********** RemoteCheckoutSessionBean.createNewRemoteCheckoutManager(): " + sessionKey);
            
            return sessionKey;
        }
        else
        {
            return null;
        }
    }
    
    
    
    @Lock(LockType.READ)
    public CheckoutSessionBeanLocal retrieveRemoteCheckoutManager(String sessionKey) throws RemoteCheckoutManagerNotFoundException
    {
        for(RemoteCheckoutManager remoteCheckoutManager:remoteCheckoutManagers)
        {
            if(remoteCheckoutManager.getSessionKey().equals(sessionKey))
            {
                return remoteCheckoutManager.getCheckoutSessionBeanLocal();
            }
        }
        
        throw new RemoteCheckoutManagerNotFoundException("The remote checkout session " + sessionKey + " either does not exist or has expired");
    }
    
    
    
    @Lock(LockType.WRITE)
    public void removeRemoteCheckoutManager(String sessionKey)
    {
        datamodel.ws.RemoteCheckoutManager remoteCheckoutManagerToRemove = null;
        
        for(RemoteCheckoutManager remoteCheckoutManager:remoteCheckoutManagers)
        {
            if(remoteCheckoutManager.getSessionKey().equals(sessionKey))
            {
                remoteCheckoutManagerToRemove = remoteCheckoutManager;
                
                break;
            }
        }
        
        if(remoteCheckoutManagerToRemove != null)
        {
            remoteCheckoutManagerToRemove.getCheckoutSessionBeanLocal().remove();
            remoteCheckoutManagerToRemove.setCheckoutSessionBeanLocal(null);
            remoteCheckoutManagers.remove(remoteCheckoutManagerToRemove);
        }
    }
    
    
    
    @Schedule(hour = "*", minute = "*/1")
    @Lock(LockType.WRITE)
    public void removeExpiredRemoteCheckoutManagers()
    {
        System.out.println("********** RemoteCheckoutManager.removeExpiredRemoteCheckoutManagers()");
        
        List<RemoteCheckoutManager> expiredRemoteCheckoutManagers = new ArrayList<>();
        
        for(RemoteCheckoutManager remoteCheckoutManager:remoteCheckoutManagers)
        {
            System.out.println("********** Checking: " + remoteCheckoutManager.getSessionKey() + "; " + remoteCheckoutManager.getExpiryDateTime());
            
            if(remoteCheckoutManager.getExpiryDateTime().compareTo(new Date()) < 0)
            {
                expiredRemoteCheckoutManagers.add(remoteCheckoutManager);
            }
        }
        
        if(!expiredRemoteCheckoutManagers.isEmpty())
        {
            remoteCheckoutManagers.removeAll(expiredRemoteCheckoutManagers);
        }
    }
    
    
    
    private CheckoutSessionBeanLocal lookupCheckoutSessionBeanLocal()
    {
        try
        {
            Context c = new InitialContext();
            CheckoutSessionBeanLocal checkoutSessionBeanLocal = (CheckoutSessionBeanLocal)c.lookup("java:module/CheckoutSessionBean!ejb.session.stateful.CheckoutSessionBeanLocal");
            
            return checkoutSessionBeanLocal;
        }
        catch(NamingException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
