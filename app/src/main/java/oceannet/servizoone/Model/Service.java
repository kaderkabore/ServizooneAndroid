package oceannet.servizoone.Model;

import java.util.List;

/**
 * Created by Erdinc on 9.5.2017.
 */

public class Service {

    public int ServiceID;
    public String DriverFullName;
    public String DriverPhone;
    public String ServiceName;
    public String Plate;
    public String BrandModel;
    public String CompanyName;

    public Service(int serviceID, String driverFullName, String driverPhone, String serviceName, String plate, String brandModel, String companyName) {
        ServiceID = serviceID;
        DriverFullName = driverFullName;
        DriverPhone = driverPhone;
        ServiceName = serviceName;
        Plate = plate;
        BrandModel = brandModel;
        CompanyName = companyName;
    }

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
    }

    public String getDriverFullName() {
        return DriverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        DriverFullName = driverFullName;
    }

    public String getDriverPhone() {
        return DriverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        DriverPhone = driverPhone;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getPlate() {
        return Plate;
    }

    public void setPlate(String plate) {
        Plate = plate;
    }

    public String getBrandModel() {
        return BrandModel;
    }

    public void setBrandModel(String brandModel) {
        BrandModel = brandModel;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
