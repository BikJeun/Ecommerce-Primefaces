package ejb.session.ws;

import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.ProductEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import util.exception.CreateNewProductException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ProductNotFoundException;
import util.exception.ProductSkuCodeExistException;
import util.exception.UnknownPersistenceException;



@WebService(serviceName = "ProductEntityWebService")
@Stateless

// Newly added in v4.3

public class ProductEntityWebService
{
    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;
    
    
    
    @WebMethod(operationName = "retrieveAllProducts")
    public List<ProductEntity> retrieveAllProducts(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password) 
                                throws InvalidLoginCredentialException
    {
        StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
        System.out.println("********** ProductEntityWebService.retrieveAllProducts(): Staff " 
                            + staffEntity.getUsername() 
                            + " login remotely via web service");
        
        return productEntitySessionBeanLocal.retrieveAllProducts();
    }
    
    
    
    @WebMethod(operationName = "retrieveProductByProductId")
    public ProductEntity retrieveProductByProductId(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password,
                                                    @WebParam(name = "productId") Long productId) 
                                throws InvalidLoginCredentialException, ProductNotFoundException
    {
        StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
        System.out.println("********** ProductEntityWebService.retrieveProductByProductId(): Staff " 
                            + staffEntity.getUsername() 
                            + " login remotely via web service");
        
        return productEntitySessionBeanLocal.retrieveProductByProductId(productId);
    }
    
    
    
    // Updated in v5.0 to include category id
    
    @WebMethod(operationName = "createNewProduct")
    public ProductEntity createNewProduct(@WebParam(name = "username") String username,
                                            @WebParam(name = "password") String password,
                                            @WebParam(name = "newProductEntity") ProductEntity newProductEntity,
                                            @WebParam(name = "categoryId") Long categoryId,
                                            @WebParam(name = "tagIds") List<Long> tagIds) 
            throws InvalidLoginCredentialException, InputDataValidationException, ProductSkuCodeExistException, UnknownPersistenceException, CreateNewProductException
    {
        StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
        System.out.println("********** ProductEntityWebService.createNewProduct(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        return productEntitySessionBeanLocal.createNewProduct(newProductEntity, categoryId, tagIds);
    }
}
