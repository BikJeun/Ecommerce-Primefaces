/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Named(value = "saleTransactionManagedBean")
@ViewScoped
public class saleTransactionManagedBean implements Serializable {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBean;

    private CustomerEntity cus;
    private List<SaleTransactionEntity> sales;
    private SaleTransactionEntity purchase;
    private List<SaleTransactionLineItemEntity> lineItem;

    /**
     * Creates a new instance of saleTransactionManagedBean
     */
    public saleTransactionManagedBean() {
        cus = new CustomerEntity();
        sales = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        cus = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        if (cus != null) {
            try {
                sales = customerEntitySessionBean.retrieveCustomerById(cus.getCustomerId()).getSaleTransactionEntities();
            } catch (CustomerNotFoundException ex) {
                Logger.getLogger(saleTransactionManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sales = new ArrayList<>();
        }
    }

    public void getLineItems(ActionEvent event) {
        System.out.println(">>>>>>>> Line Items <<<<<<<<<");
        purchase = (SaleTransactionEntity) event.getComponent().getAttributes().get("transaction");
        System.out.println("Purchase: " + purchase.toString());
        lineItem = purchase.getSaleTransactionLineItemEntities();
        for (int i = 0; i < lineItem.size(); i++) {
            System.out.println(lineItem.get(i).getProductEntity().getName());
        }

    }

    public CustomerEntity getCus() {
        return cus;
    }

    public void setCus(CustomerEntity cus) {
        this.cus = cus;
    }

    public List<SaleTransactionEntity> getSales() {
        return sales;
    }

    public void setSales(List<SaleTransactionEntity> sales) {
        this.sales = sales;
    }

    public SaleTransactionEntity getPurchase() {
        return purchase;
    }

    public void setPurchase(SaleTransactionEntity purchase) {
        this.purchase = purchase;
    }

    public List<SaleTransactionLineItemEntity> getLineItem() {
        return lineItem;
    }

    public void setLineItem(List<SaleTransactionLineItemEntity> lineItem) {
        this.lineItem = lineItem;
    }

}
