package ejb.session.stateless;

import entity.SaleTransactionEntity;
import java.util.concurrent.Future;
import javax.ejb.Remote;



@Remote

public interface EmailSessionBeanRemote 
{
    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress);
    
    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String toEmailAddress) throws InterruptedException;
}
