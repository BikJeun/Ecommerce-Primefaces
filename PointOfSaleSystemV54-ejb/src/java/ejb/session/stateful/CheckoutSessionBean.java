package ejb.session.stateful;

import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import javax.ejb.Remove;
import util.exception.CreateNewSaleTransactionException;
import util.exception.StaffNotFoundException;



@Stateful

public class CheckoutSessionBean implements CheckoutSessionBeanLocal, CheckoutSessionBeanRemote
{
    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;
    
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private Integer totalLineItem;    
    private Integer totalQuantity;    
    private BigDecimal totalAmount;   

    
    
    public CheckoutSessionBean() 
    {
        initialiseState();
    }
    
    
    
    @Remove
    public void remove()
    {        
    }
    
    
    
    private void initialiseState()
    {
        saleTransactionLineItemEntities = new ArrayList<>();
        totalLineItem = 0;
        totalQuantity = 0;
        totalAmount = new BigDecimal("0.00");
    }
    
    
    
    @Override
    public BigDecimal addItem(ProductEntity productEntity, Integer quantity)
    {
        BigDecimal subTotal = productEntity.getUnitPrice().multiply(new BigDecimal(quantity));
        
        ++totalLineItem;
        saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, productEntity, quantity, productEntity.getUnitPrice(), subTotal));
        totalQuantity += quantity;
        totalAmount = totalAmount.add(subTotal);
        
        return subTotal;
    }
    
    
    
    @Override
    public SaleTransactionEntity doCheckout(Long staffId) throws StaffNotFoundException, CreateNewSaleTransactionException
    {
        SaleTransactionEntity newSaleTransactionEntity = saleTransactionEntitySessionBeanLocal.createNewSaleTransaction(staffId, new SaleTransactionEntity(totalLineItem, totalQuantity, totalAmount, new Date(), saleTransactionLineItemEntities, false));
        initialiseState();
        
        return newSaleTransactionEntity;
    }
    
    
    
    @Override
    public void clearShoppingCart()
    {
        initialiseState();
    }

    
    
    @Override
    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    @Override
    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    @Override
    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    @Override
    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    @Override
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    @Override
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
