package ejb.session.stateless;

import entity.ProductEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.DeleteProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductInsufficientQuantityOnHandException;
import util.exception.ProductNotFoundException;
import util.exception.ProductSkuCodeExistException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateProductException;



@Remote

public interface ProductEntitySessionBeanRemote
{
    public ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds) throws ProductSkuCodeExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProductException;
  
    List<ProductEntity> retrieveAllProducts();
    
    public List<ProductEntity> searchProductsByName(String searchString);
    
    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException;
    
    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition);

    ProductEntity retrieveProductByProductId(Long productId) throws ProductNotFoundException;

    ProductEntity retrieveProductByProductSkuCode(String skuCode) throws ProductNotFoundException;

    public void updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws ProductNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateProductException, InputDataValidationException;
    
    void deleteProduct(Long productId) throws ProductNotFoundException, DeleteProductException;
    
    void debitQuantityOnHand(Long productId, Integer quantityToDebit) throws ProductNotFoundException, ProductInsufficientQuantityOnHandException;
    
    void creditQuantityOnHand(Long productId, Integer quantityToCredit) throws ProductNotFoundException;
}
