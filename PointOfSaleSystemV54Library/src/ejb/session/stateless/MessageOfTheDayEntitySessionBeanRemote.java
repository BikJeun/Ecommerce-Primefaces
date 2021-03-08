package ejb.session.stateless;

import entity.MessageOfTheDayEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;



@Remote

public interface MessageOfTheDayEntitySessionBeanRemote
{
    MessageOfTheDayEntity createNewMessageOfTheDay(MessageOfTheDayEntity newMessageOfTheDayEntity) throws InputDataValidationException;

    List<MessageOfTheDayEntity> retrieveAllMessagesOfTheDay();
}
