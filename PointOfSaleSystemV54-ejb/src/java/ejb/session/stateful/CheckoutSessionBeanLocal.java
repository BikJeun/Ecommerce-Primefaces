package ejb.session.stateful;

import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewSaleTransactionException;
import util.exception.StaffNotFoundException;



@Local

public interface CheckoutSessionBeanLocal
{
    void remove();
    
    
    
    BigDecimal addItem(ProductEntity productEntity, Integer quantity);
    
    SaleTransactionEntity doCheckout(Long staffId) throws StaffNotFoundException, CreateNewSaleTransactionException;

    void clearShoppingCart();

    

    List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities();
    
    void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities);

    Integer getTotalLineItem();
    
    void setTotalLineItem(Integer totalLineItem);
    
    Integer getTotalQuantity();
    
    void setTotalQuantity(Integer totalQuantity);
    
    BigDecimal getTotalAmount();

    void setTotalAmount(BigDecimal totalAmount);
}
