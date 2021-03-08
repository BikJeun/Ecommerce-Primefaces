package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

@Stateless

public class CustomerEntitySessionBean implements CustomerEntitySessionBeanLocal, CustomerEntitySessionBeanRemote {

    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerEntitySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            CustomerEntity customer = retrieveCustomerByEmail(email);

            if (customer.getPassword().equals(password)) {
                customer.getSaleTransactionEntities().size();
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Password invalid");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Email invalid");
        }
    }

    @Override
    public Long createNewCustomer(CustomerEntity customer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(customer);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(customer);
                em.flush();

                return customer.getCustomerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :email");
        query.setParameter("email", email);

        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer does not exist");
        }

    }

    @Override
    public CustomerEntity retrieveCustomerById(Long id) throws CustomerNotFoundException {
        CustomerEntity cus = em.find(CustomerEntity.class, id);

        if (cus != null) {
            return cus;
        } else {
            throw new CustomerNotFoundException("Customer does not exist");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
