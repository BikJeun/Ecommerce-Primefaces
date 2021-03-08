package ejb.session.stateless;

import entity.ProductEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;



@Stateless

// Newly added in v4.2

public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal, EjbTimerSessionBeanRemote
{
    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;
    
    
        
    @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
    // For testing purpose, can change the timer to trigger every 5 seconds instead of every 5 minutes
    // @Schedule(hour = "*", minute = "*", second = "*/5", info = "productEntityReorderQuantityCheckTimer")
    public void productEntityReorderQuantityCheckTimer()
    {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.productEntityReorderQuantityCheckTimer(): Timeout at " + timeStamp);
        
        List<ProductEntity> productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
        
        for(ProductEntity productEntity:productEntities)
        {
            if(productEntity.getQuantityOnHand().compareTo(productEntity.getReorderQuantity()) <= 0)
            {
                System.out.println("********** Product " + productEntity.getSkuCode() + " requires reordering: QOH = " + productEntity.getQuantityOnHand() + "; RQ = " + productEntity.getReorderQuantity());
            }
        }
    }
}
