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
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity

public class CustomerEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(max = 64)
    @Email
    private String email;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 8, max = 32)
    private String password;    
    
    @OneToMany(mappedBy = "customerEntity", fetch = FetchType.LAZY)
    private List<SaleTransactionEntity> saleTransactionEntities;
    

    
    
    public CustomerEntity()
    {        
        saleTransactionEntities = new ArrayList<>();
    }

    
    
    public CustomerEntity(String firstName, String lastName, String email, String password) 
    {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        
        setPassword(password);
    }
    
    
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.customerId != null ? this.customerId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof CustomerEntity)) 
        {
            return false;
        }
        
        CustomerEntity other = (CustomerEntity) object;
        
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.CustomerEntity[ customerId=" + this.customerId + " ]";
    }

    
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }
}