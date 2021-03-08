package ejb.session.stateless;

import entity.MessageOfTheDayEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;



@Local

public interface MessageOfTheDayEntitySessionBeanLocal
{
    MessageOfTheDayEntity createNewMessageOfTheDay(MessageOfTheDayEntity newMessageOfTheDayEntity) throws InputDataValidationException;

    List<MessageOfTheDayEntity> retrieveAllMessagesOfTheDay();
}
