package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Remote

public interface CustomerEntitySessionBeanRemote {

    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;
}
