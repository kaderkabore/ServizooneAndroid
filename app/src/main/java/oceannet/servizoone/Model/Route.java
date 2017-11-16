package oceannet.servizoone.Model;

/**
 * Created by oceannet on 25/05/17.
 */

public class Route {


        int  ID;
         String RouteName,RoadInfo;
    int Capacity,Registered;
    Double  Average;


    public Route(int ID, String routeName, String roadInfo, int capacity, int registered, Double average) {
        this.ID = ID;
        RouteName = routeName;
        RoadInfo = roadInfo;
        Capacity = capacity;
        Registered = registered;
        Average = average;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public String getRoadInfo() {
        return RoadInfo;
    }

    public void setRoadInfo(String roadInfo) {
        RoadInfo = roadInfo;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public int getRegistered() {
        return Registered;
    }

    public void setRegistered(int registered) {
        Registered = registered;
    }

    public Double getAverage() {
        return Average;
    }

    public void setAverage(Double average) {
        Average = average;
    }
}
