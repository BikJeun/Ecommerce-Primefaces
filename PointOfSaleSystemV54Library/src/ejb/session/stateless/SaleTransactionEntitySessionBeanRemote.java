package ejb.session.stateless;

import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreateNewSaleTransactionException;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;
import util.exception.StaffNotFoundException;



@Remote

public interface SaleTransactionEntitySessionBeanRemote
{
    SaleTransactionEntity createNewSaleTransaction(Long staffId, SaleTransactionEntity newSaleTransactionEntity) throws StaffNotFoundException, CreateNewSaleTransactionException;

    List<SaleTransactionEntity> retrieveAllSaleTransactions();
    
    List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemsByProductId(Long productId);

    SaleTransactionEntity retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException;
    
    void updateSaleTransaction(SaleTransactionEntity saleTransactionEntity);

    void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException;
    
    void deleteSaleTransaction(SaleTransactionEntity saleTransactionEntity);
}
