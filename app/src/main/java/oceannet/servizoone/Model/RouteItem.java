package oceannet.servizoone.Model;

/**
 * Created by oceannet on 25/05/17.
 */

public class RouteItem {

    int  ID;
    Boolean isCompany;
    Double Latitude,Longitude;

    String Name;
    int  Order;

    public RouteItem(int ID, Boolean isCompany, Double latitude, Double longitude, String name, int order) {
        this.ID = ID;
        this.isCompany = isCompany;
        Latitude = latitude;
        Longitude = longitude;
        Name = name;
        Order = order;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Boolean getCompany() {
        return isCompany;
    }

    public void setCompany(Boolean company) {
        isCompany = company;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }
}
