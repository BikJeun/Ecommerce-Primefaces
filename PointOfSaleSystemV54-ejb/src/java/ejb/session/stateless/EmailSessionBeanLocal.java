package ejb.session.stateless;

import entity.SaleTransactionEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;



@Local

public interface EmailSessionBeanLocal 
{
    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress);
    
    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress) throws InterruptedException;    
}
