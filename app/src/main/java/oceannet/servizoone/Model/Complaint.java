package oceannet.servizoone.Model;

/**
 * Created by oceannet on 24/05/17.
 */

public class Complaint {

    int ID;
    String  Category,Title,Message,DateTime;
    Boolean isAnswered;

    public Complaint(int ID, String category, String title, String message, String dateTime, Boolean isAnswered) {
        this.ID = ID;
        Category = category;
        Title = title;
        Message = message;
        DateTime = dateTime;
        this.isAnswered = isAnswered;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }
}
