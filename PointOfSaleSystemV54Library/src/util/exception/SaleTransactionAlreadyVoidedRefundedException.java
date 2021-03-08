package util.exception;



public class SaleTransactionAlreadyVoidedRefundedException extends Exception
{
    public SaleTransactionAlreadyVoidedRefundedException()
    {
    }
    
    
    
    public SaleTransactionAlreadyVoidedRefundedException(String msg)
    {
        super(msg);
    }
}