package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import util.enumeration.AccessRightEnum;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StaffEntitySessionBeanTest
{
    private static StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    
    
    
    public StaffEntitySessionBeanTest()
    {
    }
    
    
    
    @BeforeClass
    public static void setUpClass() 
    {
        staffEntitySessionBeanRemote = lookupStaffEntitySessionBeanRemote();
    }
    
    
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    
    
    @Before
    public void setUp() 
    {
    }
    
    
    
    @After
    public void tearDown() 
    {
    }
    
    
    
    @Test
    public void test01CreateNewStaff01() throws InputDataValidationException, StaffUsernameExistException, UnknownPersistenceException
    {
        StaffEntity expectedSaffEntity = new StaffEntity("One", "Cashier", AccessRightEnum.CASHIER, "cashier1", "password");
        Long actualStaffEntityId = staffEntitySessionBeanRemote.createNewStaff(expectedSaffEntity);
        
        assertNotNull(actualStaffEntityId);
        assertEquals(2l, actualStaffEntityId.longValue());        
    }
    
    
    
    @Test(expected = StaffUsernameExistException.class)
    public void test02CreateNewStaff02() throws InputDataValidationException, StaffUsernameExistException, UnknownPersistenceException
    {
        StaffEntity expectedSaffEntity = new StaffEntity("One", "Cashier", AccessRightEnum.CASHIER, "cashier1", "password");
        Long actualStaffEntityId = staffEntitySessionBeanRemote.createNewStaff(expectedSaffEntity);     
    }
    
    
    
    @Test(expected = InputDataValidationException.class)
    public void test03CreateNewStaff03() throws InputDataValidationException, StaffUsernameExistException, UnknownPersistenceException
    {
        StaffEntity expectedSaffEntity = new StaffEntity("Two", "Cashier", AccessRightEnum.CASHIER, "cas", "password");
        Long actualStaffEntityId = staffEntitySessionBeanRemote.createNewStaff(expectedSaffEntity);     
    }
    
    
    
    @Test
    public void test04RetrieveAllStaffs01()
    {
        List<StaffEntity> actualStaffEntities = staffEntitySessionBeanRemote.retrieveAllStaffs();
        
        assertFalse(actualStaffEntities.isEmpty());
        assertEquals(2, actualStaffEntities.size());
    }
    
    
    
    @Test
    public void test05RetrieveStaffByStaffId01() throws StaffNotFoundException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(1l);
        
        assertEquals(1l, actualStaffEntity.getStaffId().longValue());
    }
    
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test06RetrieveStaffByStaffId02() throws StaffNotFoundException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(3l);
    }
    
    
    
    @Test
    public void test07StaffLogin01() throws InvalidLoginCredentialException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.staffLogin("manager", "password");
        
        assertEquals("manager", actualStaffEntity.getUsername());
    }
    
    
    
    @Test(expected = InvalidLoginCredentialException.class)
    public void test08StaffLogin02() throws InvalidLoginCredentialException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.staffLogin("manager", "123456");
    }
    
    
    
    @Test
    public void test09UpdateStaff01() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setFirstName("Default First Name");
        staffEntitySessionBeanRemote.updateStaff(actualStaffEntity);
        actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(1l);
        
        assertEquals("Default First Name", actualStaffEntity.getFirstName());
    }
    
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test10UpdateStaff02() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setStaffId(10l);
        actualStaffEntity.setFirstName("Default First Name");
        staffEntitySessionBeanRemote.updateStaff(actualStaffEntity);        
    }
    
    
    
    @Test(expected = UpdateStaffException.class)
    public void test11UpdateStaff03() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setUsername("manager1");
        actualStaffEntity.setFirstName("Default First Name");
        staffEntitySessionBeanRemote.updateStaff(actualStaffEntity);        
    }
    
        
    
    @Test(expected = InputDataValidationException.class)
    public void test12UpdateStaff04() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntitySessionBeanRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setFirstName(null);
        staffEntitySessionBeanRemote.updateStaff(actualStaffEntity);        
    }
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test13DeleteStaff01() throws StaffNotFoundException, DeleteStaffException
    {
        staffEntitySessionBeanRemote.deleteStaff(2l);
        staffEntitySessionBeanRemote.retrieveStaffByStaffId(2l);
    }
    
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test14DeleteStaff02() throws StaffNotFoundException, DeleteStaffException
    {
        staffEntitySessionBeanRemote.deleteStaff(3l);
    }
    
    
    
    @Test
    public void test15RetrieveAllStaffs02()
    {
        List<StaffEntity> staffEntities = staffEntitySessionBeanRemote.retrieveAllStaffs();
        
        assertFalse(staffEntities.isEmpty());
        assertEquals(1, staffEntities.size());
    }
    
        
    
    private static StaffEntitySessionBeanRemote lookupStaffEntitySessionBeanRemote()
    {
        try 
        {
            Context c = new InitialContext();
            
            return (StaffEntitySessionBeanRemote) c.lookup("java:global/PointOfSaleSystemV45/PointOfSaleSystemV45-ejb/StaffEntitySessionBean!ejb.session.stateless.StaffEntitySessionBeanRemote");
        }
        catch (NamingException ne)
        {
            throw new RuntimeException(ne);
        }
    }
}