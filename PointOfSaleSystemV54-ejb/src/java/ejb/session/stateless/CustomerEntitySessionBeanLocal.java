package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

@Local

public interface CustomerEntitySessionBeanLocal {

    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public Long createNewCustomer(CustomerEntity customer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException;

    public CustomerEntity retrieveCustomerById(Long id) throws CustomerNotFoundException;
}
