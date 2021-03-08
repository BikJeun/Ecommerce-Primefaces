package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.AccessRightEnum;
import util.security.CryptographicHelper;



@Entity

public class StaffEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private AccessRightEnum accessRightEnum;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;
    // Updated in v4.5 to use CHAR instead of VARCHAR
    // @Column(nullable = false, length = 32)
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    // The following bean validation constraint is not applicable since we are only storing the password hashsum which is always 128 bit represented as 32 characters (16 hexadecimal digits)
    // @Size(min = 8, max = 32)
    private String password;
    // Newly added in v4.5
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    
    @OneToMany(mappedBy = "staffEntity", fetch = FetchType.LAZY)
    private List<SaleTransactionEntity> saleTransactionEntities;
    
    
    
    public StaffEntity()
    {
        // Newly added in v4.5
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        
        saleTransactionEntities = new ArrayList<>();
    }

    
    
    public StaffEntity(String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password) 
    {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        
        // Updated in v4.5
        setPassword(password);
    }
    
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.staffId != null ? this.staffId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof StaffEntity)) 
        {
            return false;
        }
        
        StaffEntity other = (StaffEntity) object;
        
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.StaffEntity[ staffId=" + this.staffId + " ]";
    }

    
    
    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // Updated in v4.5
    public void setPassword(String password)
    {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }
    
    // Newly added in v4.5
    public String getSalt() {
        return salt;
    }

    // Newly added in v4.5
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }
}