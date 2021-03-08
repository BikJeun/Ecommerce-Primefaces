package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;



@Entity

public class ProductEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(nullable = false, unique = true, length = 7)
    @NotNull
    @Size(min = 7, max = 7)
    private String skuCode;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer quantityOnHand;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer reorderQuantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice;    
    @Column(nullable = false)
    @NotNull
    @Positive
    @Min(1)
    @Max(5)
    private Integer productRating;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CategoryEntity categoryEntity;
    
    @ManyToMany(mappedBy = "productEntities", fetch = FetchType.LAZY)
    private List<TagEntity> tagEntities;

    
    
    public ProductEntity() 
    {
        tagEntities = new ArrayList<>();
    }

    
        
    public ProductEntity(String skuCode, String name, String description, Integer quantityOnHand, Integer reorderQuantity, BigDecimal unitPrice /*, String category*/ , Integer productRating) 
    {
        this();
        
        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.unitPrice = unitPrice;
        this.productRating = productRating;
    }
    
    
    
    public void addTag(TagEntity tagEntity)
    {
        if(tagEntity != null)
        {
            if(!this.tagEntities.contains(tagEntity))
            {
                this.tagEntities.add(tagEntity);
                
                if(!tagEntity.getProductEntities().contains(this))
                {                    
                    tagEntity.getProductEntities().add(this);
                }
            }
        }
    }
    
    
    
    public void removeTag(TagEntity tagEntity)
    {
        if(tagEntity != null)
        {
            if(this.tagEntities.contains(tagEntity))
            {
                this.tagEntities.remove(tagEntity);
                
                if(tagEntity.getProductEntities().contains(this))
                {
                    tagEntity.getProductEntities().remove(this);
                }
            }
        }
    }
    
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.productId != null ? this.productId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof ProductEntity)) 
        {
            return false;
        }
        
        ProductEntity other = (ProductEntity) object;
        
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.pos.ProductEntity[ productId=" + this.productId + " ]";
    }

    
    
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }
    
    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Integer getProductRating() {
        return productRating;
    }

    public void setProductRating(Integer productRating) {
        this.productRating = productRating;
    }
    
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) 
    {
        if(this.categoryEntity != null)
        {
            if(this.categoryEntity.getProductEntities().contains(this))
            {
                this.categoryEntity.getProductEntities().remove(this);
            }
        }
        
        this.categoryEntity = categoryEntity;
        
        if(this.categoryEntity != null)
        {
            if(!this.categoryEntity.getProductEntities().contains(this))
            {
                this.categoryEntity.getProductEntities().add(this);
            }
        }
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }
}