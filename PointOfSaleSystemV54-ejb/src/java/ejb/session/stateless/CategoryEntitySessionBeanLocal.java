package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;



@Local

public interface CategoryEntitySessionBeanLocal 
{
    CategoryEntity createNewCategoryEntity(CategoryEntity newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException;
    
    List<CategoryEntity> retrieveAllCategories();
    
    List<CategoryEntity> retrieveAllRootCategories();
    
    List<CategoryEntity> retrieveAllLeafCategories();
    
    List<CategoryEntity> retrieveAllCategoriesWithoutProduct();
    
    CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    
    void updateCategory(CategoryEntity categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;
    
    void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;
}
