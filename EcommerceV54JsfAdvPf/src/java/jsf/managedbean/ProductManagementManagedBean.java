/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import static oracle.jrockit.jfr.events.Bits.intValue;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Named(value = "productManagementManagedBean")
@SessionScoped
public class ProductManagementManagedBean implements Serializable {

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;

    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBean;

    private List<ProductEntity> prod;
    private List<SaleTransactionLineItemEntity> saleTransactionLineItems;
    private List<SaleTransactionLineItemEntity> itemsToBuy;

    private BigDecimal totalAmt;
    private Integer totalLineItem;
    private Integer qty;

    /**
     * Creates a new instance of ProductManagementManagedBean
     */
    public ProductManagementManagedBean() {
        init();
    }

    private void init() {
        saleTransactionLineItems = new ArrayList<>();
        itemsToBuy = new ArrayList<>();

        totalAmt = new BigDecimal("0.00");
        totalLineItem = 0;
        qty = 0;
    }

    @PostConstruct
    public void postConstruct() {
        prod = productEntitySessionBean.retrieveAllProducts();
        for (ProductEntity prods : prod) {
            SaleTransactionLineItemEntity item = new SaleTransactionLineItemEntity(0, prods, 0, prods.getUnitPrice(), totalAmt);
            saleTransactionLineItems.add(item);
        }
    }

    public void addToCart(ActionEvent event) {
        System.out.println(">>>>>> Add To Cart <<<<<<<");
        SaleTransactionLineItemEntity item = (SaleTransactionLineItemEntity) event.getComponent().getAttributes().get("itemToBuy");
        System.out.println("Product: " + item.getProductEntity().getName());
        System.out.println("Quantity ordered: " + item.getQuantity());

        if (item.getSerialNumber() == 0) {
            item.setSerialNumber(++totalLineItem);
            qty += item.getQuantity();
            BigDecimal subTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubTotal(subTotal);
            totalAmt = totalAmt.add(subTotal);
            itemsToBuy.add(item);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart", null));
        } else {
            BigDecimal preQty = item.getSubTotal().divide(item.getUnitPrice());
            qty -= intValue(preQty);
            totalAmt = totalAmt.subtract(item.getSubTotal());

            qty += item.getQuantity();
            BigDecimal subTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubTotal(subTotal);
            totalAmt = totalAmt.add(subTotal);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Quantity Updated", null));
        }
        for (int i = 0; i < itemsToBuy.size(); i++) {
            System.out.println("LEts see");
            System.out.println(itemsToBuy.get(i).getProductEntity().getName() + " " + itemsToBuy.get(i).getQuantity());
        }
    }

    public void askToLogin(ActionEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please login to add item to cart :)", null));
    }

    public void doCheckout(ActionEvent event) throws CustomerNotFoundException {
        try {
            System.out.println(">>>>>> Checkout <<<<<<<");
            CustomerEntity cus = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            SaleTransactionEntity txn = new SaleTransactionEntity(getTotalLineItem(), this.getQty(), this.getTotalAmt(), new Date(), this.getItemsToBuy(), false);
            txn = saleTransactionEntitySessionBean.createNewSaleTransactionForCustomer(cus.getCustomerId(), txn);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Checkout successful", null));
            clearCart();
        } catch (CreateNewSaleTransactionException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), null));
        }

    }

    public void clearCart() {
        init();
        for (ProductEntity prods : prod) {
            SaleTransactionLineItemEntity item = new SaleTransactionLineItemEntity(0, prods, 0, prods.getUnitPrice(), totalAmt);
            saleTransactionLineItems.add(item);
        }
    }

    public List<ProductEntity> getProd() {
        return prod;
    }

    public void setProd(List<ProductEntity> prod) {
        this.prod = prod;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItems() {
        return saleTransactionLineItems;
    }

    public void setSaleTransactionLineItems(List<SaleTransactionLineItemEntity> saleTransactionLineItems) {
        this.saleTransactionLineItems = saleTransactionLineItems;
    }

    public List<SaleTransactionLineItemEntity> getItemsToBuy() {
        return itemsToBuy;
    }

    public void setItemsToBuy(List<SaleTransactionLineItemEntity> itemsToBuy) {
        this.itemsToBuy = itemsToBuy;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
