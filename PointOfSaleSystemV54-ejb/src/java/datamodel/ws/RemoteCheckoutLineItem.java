package datamodel.ws;

import java.io.Serializable;



public class RemoteCheckoutLineItem implements Serializable
{
    private String skuCode;
    private Integer quantity;

    
    
    public RemoteCheckoutLineItem()
    {
    }

    
    
    public RemoteCheckoutLineItem(String skuCode, Integer quantity)
    {
        this.skuCode = skuCode;
        this.quantity = quantity;
    }
    
    
    
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }   
}