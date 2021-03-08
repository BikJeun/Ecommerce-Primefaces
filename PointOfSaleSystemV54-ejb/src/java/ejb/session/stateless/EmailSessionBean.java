package ejb.session.stateless;

import entity.SaleTransactionEntity;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import util.email.EmailManager;



@Stateless

public class EmailSessionBean implements EmailSessionBeanLocal, EmailSessionBeanRemote 
{
    private final String FROM_EMAIL_ADDRESS = "xxx <xxx@gmail.com>";
    private final String GMAIL_USERNAME = "xxx@gmail.com";
    private final String GMAIL_PASSWORD = "xxx";
    
    
    
    @Override
    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress)
    {
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCheckoutNotification(saleTransactionEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
        
        return result;
    } 
    
    
    
    @Asynchronous
    @Override
    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress) throws InterruptedException
    {        
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCheckoutNotification(saleTransactionEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
        
        return new AsyncResult<>(result);
    }
}
