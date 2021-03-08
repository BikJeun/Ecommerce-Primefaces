package util.exception;



public class ProductInsufficientQuantityOnHandException extends Exception
{
    public ProductInsufficientQuantityOnHandException()
    {
    }
    
    
    
    public ProductInsufficientQuantityOnHandException(String msg)
    {
        super(msg);
    }
}