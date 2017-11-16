package oceannet.servizoone.Model;

/**
 * Created by Erdinc on 9.5.2017.
 */

public class Authorized {
    public String authorizedFullName;
    public String authorizedPhone;

    public Authorized(String authorizedFullName, String authorizedPhone) {
        this.authorizedFullName = authorizedFullName;
        this.authorizedPhone = authorizedPhone;
    }

    public String getAuthorizedFullName() {
        return authorizedFullName;
    }

    public void setAuthorizedFullName(String authorizedFullName) {
        this.authorizedFullName = authorizedFullName;
    }

    public String getAuthorizedPhone() {
        return authorizedPhone;
    }

    public void setAuthorizedPhone(String authorizedPhone) {
        this.authorizedPhone = authorizedPhone;
    }
}
