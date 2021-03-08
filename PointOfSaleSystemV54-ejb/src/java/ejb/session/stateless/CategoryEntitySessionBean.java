package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;



@Stateless

// Newly added in v5.0

public class CategoryEntitySessionBean implements CategoryEntitySessionBeanLocal, CategoryEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    
    
    public CategoryEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public CategoryEntity createNewCategoryEntity(CategoryEntity newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException
    {
        Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(newCategoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if(parentCategoryId != null)
                {
                    CategoryEntity parentCategoryEntity = retrieveCategoryByCategoryId(parentCategoryId);

                    if(!parentCategoryEntity.getProductEntities().isEmpty())
                    {
                        throw new CreateNewCategoryException("Parent category cannot be associated with any product");
                    }

                    newCategoryEntity.setParentCategoryEntity(parentCategoryEntity);
                }
                
                entityManager.persist(newCategoryEntity);
                entityManager.flush();

                return newCategoryEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewCategoryException("Category with same name already exist");
                }
                else
                {
                    throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<CategoryEntity> retrieveAllCategories()
    {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c ORDER BY c.name ASC");
        List<CategoryEntity> categoryEntities = query.getResultList();
        
        for(CategoryEntity categoryEntity:categoryEntities)
        {
            categoryEntity.getParentCategoryEntity();
            categoryEntity.getSubCategoryEntities().size();
            categoryEntity.getProductEntities().size();
        }
        
        return categoryEntities;
    }
    
    
    
    @Override
    public List<CategoryEntity> retrieveAllRootCategories()
    {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.parentCategoryEntity IS NULL ORDER BY c.name ASC");
        List<CategoryEntity> rootCategoryEntities = query.getResultList();
        
        for(CategoryEntity rootCategoryEntity:rootCategoryEntities)
        {            
            lazilyLoadSubCategories(rootCategoryEntity);
            
            rootCategoryEntity.getProductEntities().size();
        }
        
        return rootCategoryEntities;
    }
    
    
    
    @Override
    public List<CategoryEntity> retrieveAllLeafCategories()
    {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.subCategoryEntities IS EMPTY ORDER BY c.name ASC");
        List<CategoryEntity> leafCategoryEntities = query.getResultList();
        
        for(CategoryEntity leafCategoryEntity:leafCategoryEntities)
        {
            leafCategoryEntity.getParentCategoryEntity();
            leafCategoryEntity.getProductEntities().size();
        }
        
        return leafCategoryEntities;
    }
    
    
    
    @Override
    public List<CategoryEntity> retrieveAllCategoriesWithoutProduct()
    {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.productEntities IS EMPTY ORDER BY c.name ASC");
        List<CategoryEntity> rootCategoryEntities = query.getResultList();
        
        for(CategoryEntity rootCategoryEntity:rootCategoryEntities)
        {
            rootCategoryEntity.getParentCategoryEntity();            
        }
        
        return rootCategoryEntities;
    }
    
    
    
    @Override
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException
    {
        CategoryEntity categoryEntity = entityManager.find(CategoryEntity.class, categoryId);
        
        if(categoryEntity != null)
        {
            return categoryEntity;
        }
        else
        {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public void updateCategory(CategoryEntity categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException
    {
        Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(categoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(categoryEntity.getCategoryId()!= null)
            {
                CategoryEntity categoryEntityToUpdate = retrieveCategoryByCategoryId(categoryEntity.getCategoryId());
                
                Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.name = :inName AND c.categoryId <> :inCategoryId");
                query.setParameter("inName", categoryEntity.getName());
                query.setParameter("inCategoryId", categoryEntity.getCategoryId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateCategoryException("The name of the category to be updated is duplicated");
                }
                
                categoryEntityToUpdate.setName(categoryEntity.getName());
                categoryEntityToUpdate.setDescription(categoryEntity.getDescription());                               
                
                if(parentCategoryId != null)
                {
                    if(categoryEntityToUpdate.getCategoryId().equals(parentCategoryId))
                    {
                        throw new UpdateCategoryException("Category cannot be its own parent");
                    }
                    else if(categoryEntityToUpdate.getParentCategoryEntity() == null || (!categoryEntityToUpdate.getParentCategoryEntity().getCategoryId().equals(parentCategoryId)))
                    {
                        CategoryEntity parentCategoryEntityToUpdate = retrieveCategoryByCategoryId(parentCategoryId);
                        
                        if(!parentCategoryEntityToUpdate.getProductEntities().isEmpty())
                        {
                            throw new UpdateCategoryException("Parent category cannot have any product associated with it");
                        }
                        
                        categoryEntityToUpdate.setParentCategoryEntity(parentCategoryEntityToUpdate);
                    }
                }
                else
                {
                    categoryEntityToUpdate.setParentCategoryEntity(null);
                }                
            }
            else
            {
                throw new CategoryNotFoundException("Category ID not provided for category to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException
    {
        CategoryEntity categoryEntityToRemove = retrieveCategoryByCategoryId(categoryId);
        
        if(!categoryEntityToRemove.getSubCategoryEntities().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing sub-categories and cannot be deleted!");
        }
        else if(!categoryEntityToRemove.getProductEntities().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            categoryEntityToRemove.setParentCategoryEntity(null);
            
            entityManager.remove(categoryEntityToRemove);
        }                
    }
    
    
    
    private void lazilyLoadSubCategories(CategoryEntity categoryEntity)
    {
        for(CategoryEntity ce:categoryEntity.getSubCategoryEntities())
        {
            lazilyLoadSubCategories(ce);
        }
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CategoryEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
