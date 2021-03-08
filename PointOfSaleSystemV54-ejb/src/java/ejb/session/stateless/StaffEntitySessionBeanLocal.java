package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;



@Local

public interface StaffEntitySessionBeanLocal
{
    Long createNewStaff(StaffEntity newStaffEntity) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    
    List<StaffEntity> retrieveAllStaffs();
    
    StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    void updateStaff(StaffEntity staffEntity) throws StaffNotFoundException, UpdateStaffException, InputDataValidationException;
    
    void deleteStaff(Long staffId) throws StaffNotFoundException, DeleteStaffException;
}
