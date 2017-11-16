package oceannet.servizoone.Model;

/**
 * Created by oceannet on 26/05/17.
 */

public class AnswerList {

     String Message,DateTime;


    public AnswerList(String message, String dateTime) {
        Message = message;
        DateTime = dateTime;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}
