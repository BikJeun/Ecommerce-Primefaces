package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity

public class CategoryEntity implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(max = 32)
    private String name;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String description;
    
    @OneToMany(mappedBy = "parentCategoryEntity", fetch = FetchType.LAZY)
    private List<CategoryEntity> subCategoryEntities;
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity parentCategoryEntity;
    
    @OneToMany(mappedBy = "categoryEntity", fetch = FetchType.LAZY)
    private List<ProductEntity> productEntities;

    
    
    public CategoryEntity() 
    {
        subCategoryEntities = new ArrayList<>();
        productEntities = new ArrayList<>();
    }

    
    
    public CategoryEntity(String name, String description) 
    {
        this();
        
        this.name = name;
        this.description = description;
    }
    
    
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity other = (CategoryEntity) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CategoryEntity[ id=" + categoryId + " ]";
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CategoryEntity> getSubCategoryEntities() {
        return subCategoryEntities;
    }

    public void setSubCategoryEntities(List<CategoryEntity> subCategoryEntities) {
        this.subCategoryEntities = subCategoryEntities;
    }

    public CategoryEntity getParentCategoryEntity() {
        return parentCategoryEntity;
    }

    public void setParentCategoryEntity(CategoryEntity parentCategoryEntity) 
    {
        if(this.parentCategoryEntity != null)
        {
            if(this.parentCategoryEntity.getSubCategoryEntities().contains(this))
            {
                this.parentCategoryEntity.getSubCategoryEntities().remove(this);
            }
        }
        
        this.parentCategoryEntity = parentCategoryEntity;
        
        if(this.parentCategoryEntity != null)
        {
            if(!this.parentCategoryEntity.getSubCategoryEntities().contains(this))
            {
                this.parentCategoryEntity.getSubCategoryEntities().add(this);
            }
        }
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}