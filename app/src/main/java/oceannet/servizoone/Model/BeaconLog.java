package oceannet.servizoone.Model;

/**
 * Created by oceannet on 29/05/17.
 */

public class BeaconLog {

    String beaconuuid, major, minor;
    int status;
    Double latitude,longitude;


    public BeaconLog(String beaconuuid, String major, String minor, int status, Double latitude, Double longitude) {
        this.beaconuuid = beaconuuid;
        this.major = major;
        this.minor = minor;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getBeaconuuid() {
        return beaconuuid;
    }

    public void setBeaconuuid(String beaconuuid) {
        this.beaconuuid = beaconuuid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
