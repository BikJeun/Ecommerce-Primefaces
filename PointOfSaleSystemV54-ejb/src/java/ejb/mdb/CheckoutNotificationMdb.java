package ejb.mdb;

import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.SaleTransactionEntity;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import util.exception.SaleTransactionNotFoundException;
import ejb.session.stateless.EmailSessionBeanLocal;



@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/queueCheckoutNotification"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})

public class CheckoutNotificationMdb implements MessageListener 
{
    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;
    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;
    
    
    
    public CheckoutNotificationMdb() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
    }
    
    
    
    @PreDestroy
    public void preDestroy()
    {
    }


    
    @Override
    public void onMessage(Message message)
    {
        try
        {
            if (message instanceof MapMessage)
            {
                MapMessage mapMessage = (MapMessage)message;                
                String toEmailAddress = mapMessage.getString("toEmailAddress");
                Long saleTransactionEntityId = (Long)mapMessage.getLong("saleTransactionEntityId");
                SaleTransactionEntity saleTransactionEntity = saleTransactionEntitySessionBeanLocal.retrieveSaleTransactionBySaleTransactionId(saleTransactionEntityId);
                
                emailSessionBeanLocal.emailCheckoutNotificationSync(saleTransactionEntity, toEmailAddress);
                
                System.err.println("********** CheckoutNotificationMdb.onMessage: " + saleTransactionEntity.getSaleTransactionId() + "; " + toEmailAddress);
            }
        }
        catch(SaleTransactionNotFoundException | JMSException ex)
        {
            System.err.println("CheckoutNotificationMdb.onMessage(): " + ex.getMessage());
        }
    }    
}
