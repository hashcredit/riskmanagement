package com.newdumai.util.excel.vo;

import com.newdumai.util.excel.ExcelField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出电核结果vo
 * <p>
 * Created by zhang on 2017/3/28.
 */
public class GpsDeviceVo {

    private String deviceId;//设备编号
    private String company;//所属公司
    private String vin;//车架号
    private String carType;//车型
    private String lender;//借款人姓名
    private String simId;//SIM卡号
    private Date periodOfValidity;//有效期


    @ExcelField(title = "设备编号", align = 2, type = 2, sort = 10)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        if (deviceId.contains("E") && deviceId.contains(".")) {
            BigDecimal db = new BigDecimal(deviceId);
            this.deviceId = db.toPlainString();
        } else {
            this.deviceId = deviceId;
        }
    }

    @ExcelField(title = "所属公司", align = 2, type = 2, sort = 20)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @ExcelField(title = "车号", align = 2, type = 2, sort = 30)
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @ExcelField(title = "车型", align = 2, type = 2, sort = 40)
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @ExcelField(title = "姓名", align = 2, type = 2, sort = 50)
    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    @ExcelField(title = "SIM卡号", align = 2, type = 2, sort = 60)
    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    @ExcelField(title = "有效期", align = 2, type = 2, sort = 70)
    public Date getPeriodOfValidity() {
        return periodOfValidity;
    }

    public void setPeriodOfValidity(Date periodOfValidity) {
        this.periodOfValidity = periodOfValidity;
    }

    @Override
    public String toString() {
        return "GpsDeviceVo{" +
                "deviceId='" + deviceId + '\'' +
                ", company='" + company + '\'' +
                ", vin='" + vin + '\'' +
                ", carType='" + carType + '\'' +
                ", lender='" + lender + '\'' +
                ", simId='" + simId + '\'' +
                ", periodOfValidity='" + periodOfValidity + '\'' +
                '}';
    }
}
