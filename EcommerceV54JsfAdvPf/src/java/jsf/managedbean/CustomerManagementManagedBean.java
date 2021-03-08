/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.CustomerEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Mitsuki
 */
@Named(value = "customerManagementManagedBean")
@ViewScoped
public class CustomerManagementManagedBean implements Serializable {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBean;

    private CustomerEntity customer;
    private String password;

    /**
     * Creates a new instance of CustomerManagementManagedBean
     */
    public CustomerManagementManagedBean() {
        customer = new CustomerEntity();
    }

    @PostConstruct
    public void postConstruct() {

    }

    public void createNewCustomer(ActionEvent event) throws IOException {
        try {
            boolean isSame = checkPassword(getCustomer().getPassword(), password);
            if (isSame) {
                Long customerId = customerEntitySessionBean.createNewCustomer(getCustomer());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account created successfully!", null));

            }

        } catch (CustomerExistException | UnknownPersistenceException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

    private boolean checkPassword(String password, String password1) {
        if (password.equals(password1)) {
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password not the same", null));
            return false;
        }
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
