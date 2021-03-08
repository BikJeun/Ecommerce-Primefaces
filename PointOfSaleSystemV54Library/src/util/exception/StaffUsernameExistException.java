package util.exception;



public class StaffUsernameExistException extends Exception
{
    public StaffUsernameExistException()
    {
    }
    
    
    
    public StaffUsernameExistException(String msg)
    {
        super(msg);
    }
}