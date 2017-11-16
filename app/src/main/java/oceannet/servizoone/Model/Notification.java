package oceannet.servizoone.Model;

/**
 * Created by oceannet on 24/05/17.
 */

public class Notification {

     int ID;
    String  Title,Message,DateTime;


    public Notification(int ID, String title, String message, String dateTime) {
        this.ID = ID;
        Title = title;
        Message = message;
        DateTime = dateTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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
