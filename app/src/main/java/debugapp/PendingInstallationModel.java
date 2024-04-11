package debugapp;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PendingInstallationModel implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private List<Response> response;

    protected PendingInstallationModel(Parcel in) {
        status = in.readString();
        message = in.readString();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }



    public static class Response implements Serializable {

        @SerializedName("mandt")
        @Expose
        private String mandt;
        @SerializedName("vbeln")
        @Expose
        private String vbeln;
        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("process_no")
        @Expose
        private String processNo;
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("project_login_no")
        @Expose
        private String projectLoginNo;
        @SerializedName("instdate")
        @Expose
        private String instdate;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("father_name")
        @Expose
        private String fatherName;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("tehsil_code")
        @Expose
        private String tehsilCode;
        @SerializedName("tehsil")
        @Expose
        private String tehsil;
        @SerializedName("village")
        @Expose
        private String village;
        @SerializedName("contact_no")
        @Expose
        private String contactNo;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("make")
        @Expose
        private String make;
        @SerializedName("rms_status")
        @Expose
        private String rmsStatus;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("erdat")
        @Expose
        private String erdat;
        @SerializedName("ertim")
        @Expose
        private String ertim;
        @SerializedName("solar_pannel_watt")
        @Expose
        private String solarPannelWatt;
        @SerializedName("hp")
        @Expose
        private String hp;
        @SerializedName("panel_install_qty")
        @Expose
        private String panelInstallQty;
        @SerializedName("total_watt")
        @Expose
        private String totalWatt;
        @SerializedName("panel_module_qty")
        @Expose
        private String panelModuleQty;
        @SerializedName("motor_sernr")
        @Expose
        private String motorSernr;
        @SerializedName("pump_sernr")
        @Expose
        private String pumpSernr;
        @SerializedName("controller_sernr")
        @Expose
        private String controllerSernr;
        @SerializedName("sim_opretor")
        @Expose
        private String simOpretor;
        @SerializedName("simno")
        @Expose
        private String simno;
        @SerializedName("connection_type")
        @Expose
        private String connectionType;
        @SerializedName("borewellstatus")
        @Expose
        private String borewellstatus;
        @SerializedName("total_plate_watt")
        @Expose
        private String totalPlateWatt;
        @SerializedName("delay_reason")
        @Expose
        private String delayReason;
        @SerializedName("setting_check")
        @Expose
        private String settingCheck;
        @SerializedName("dbug_mob_1")
        @Expose
        private String dbugMob1;
        @SerializedName("dbug_mob_2")
        @Expose
        private String dbugMob2;
        @SerializedName("dbug_mob_3")
        @Expose
        private String dbugMob3;
        @SerializedName("dbug_ofline")
        @Expose
        private String dbugOfline;
        @SerializedName("app_version")
        @Expose
        private String appVersion;
        @SerializedName("otp_status")
        @Expose
        private String otpStatus;
        @SerializedName("otp_remark")
        @Expose
        private String otpRemark;
        @SerializedName("offlinephoto")
        @Expose
        private String offlinephoto;
        @SerializedName("ver_otp")
        @Expose
        private String verOtp;
        @SerializedName("ver_otp_dats")
        @Expose
        private String verOtpDats;
        @SerializedName("imei")
        @Expose
        private String imei;

        @SerializedName("dongle")
        @Expose
        private String dongle;
        @SerializedName("latlng")
        @Expose
        private String latlng;

        public String getMandt() {
            return mandt;
        }

        public void setMandt(String mandt) {
            this.mandt = mandt;
        }

        public String getVbeln() {
            return vbeln;
        }

        public void setVbeln(String vbeln) {
            this.vbeln = vbeln;
        }

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public String getRegisno() {
            return regisno;
        }

        public void setRegisno(String regisno) {
            this.regisno = regisno;
        }

        public String getProcessNo() {
            return processNo;
        }

        public void setProcessNo(String processNo) {
            this.processNo = processNo;
        }

        public String getBeneficiary() {
            return beneficiary;
        }

        public void setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getProjectLoginNo() {
            return projectLoginNo;
        }

        public void setProjectLoginNo(String projectLoginNo) {
            this.projectLoginNo = projectLoginNo;
        }

        public String getInstdate() {
            return instdate;
        }

        public void setInstdate(String instdate) {
            this.instdate = instdate;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTehsilCode() {
            return tehsilCode;
        }

        public void setTehsilCode(String tehsilCode) {
            this.tehsilCode = tehsilCode;
        }

        public String getTehsil() {
            return tehsil;
        }

        public void setTehsil(String tehsil) {
            this.tehsil = tehsil;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getRmsStatus() {
            return rmsStatus;
        }

        public void setRmsStatus(String rmsStatus) {
            this.rmsStatus = rmsStatus;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getErdat() {
            return erdat;
        }

        public void setErdat(String erdat) {
            this.erdat = erdat;
        }

        public String getErtim() {
            return ertim;
        }

        public void setErtim(String ertim) {
            this.ertim = ertim;
        }

        public String getSolarPannelWatt() {
            return solarPannelWatt;
        }

        public void setSolarPannelWatt(String solarPannelWatt) {
            this.solarPannelWatt = solarPannelWatt;
        }

        public String getHp() {
            return hp;
        }

        public void setHp(String hp) {
            this.hp = hp;
        }

        public String getPanelInstallQty() {
            return panelInstallQty;
        }

        public void setPanelInstallQty(String panelInstallQty) {
            this.panelInstallQty = panelInstallQty;
        }

        public String getTotalWatt() {
            return totalWatt;
        }

        public void setTotalWatt(String totalWatt) {
            this.totalWatt = totalWatt;
        }

        public String getPanelModuleQty() {
            return panelModuleQty;
        }

        public void setPanelModuleQty(String panelModuleQty) {
            this.panelModuleQty = panelModuleQty;
        }

        public String getMotorSernr() {
            return motorSernr;
        }

        public void setMotorSernr(String motorSernr) {
            this.motorSernr = motorSernr;
        }

        public String getPumpSernr() {
            return pumpSernr;
        }

        public void setPumpSernr(String pumpSernr) {
            this.pumpSernr = pumpSernr;
        }

        public String getControllerSernr() {
            return controllerSernr;
        }

        public void setControllerSernr(String controllerSernr) {
            this.controllerSernr = controllerSernr;
        }

        public String getSimOpretor() {
            return simOpretor;
        }

        public void setSimOpretor(String simOpretor) {
            this.simOpretor = simOpretor;
        }

        public String getSimno() {
            return simno;
        }

        public void setSimno(String simno) {
            this.simno = simno;
        }

        public String getConnectionType() {
            return connectionType;
        }

        public void setConnectionType(String connectionType) {
            this.connectionType = connectionType;
        }

        public String getBorewellstatus() {
            return borewellstatus;
        }

        public void setBorewellstatus(String borewellstatus) {
            this.borewellstatus = borewellstatus;
        }

        public String getTotalPlateWatt() {
            return totalPlateWatt;
        }

        public void setTotalPlateWatt(String totalPlateWatt) {
            this.totalPlateWatt = totalPlateWatt;
        }

        public String getDelayReason() {
            return delayReason;
        }

        public void setDelayReason(String delayReason) {
            this.delayReason = delayReason;
        }

        public String getSettingCheck() {
            return settingCheck;
        }

        public void setSettingCheck(String settingCheck) {
            this.settingCheck = settingCheck;
        }

        public String getDbugMob1() {
            return dbugMob1;
        }

        public void setDbugMob1(String dbugMob1) {
            this.dbugMob1 = dbugMob1;
        }

        public String getDbugMob2() {
            return dbugMob2;
        }

        public void setDbugMob2(String dbugMob2) {
            this.dbugMob2 = dbugMob2;
        }

        public String getDbugMob3() {
            return dbugMob3;
        }

        public void setDbugMob3(String dbugMob3) {
            this.dbugMob3 = dbugMob3;
        }

        public String getDbugOfline() {
            return dbugOfline;
        }

        public void setDbugOfline(String dbugOfline) {
            this.dbugOfline = dbugOfline;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getOtpStatus() {
            return otpStatus;
        }

        public void setOtpStatus(String otpStatus) {
            this.otpStatus = otpStatus;
        }

        public String getOtpRemark() {
            return otpRemark;
        }

        public void setOtpRemark(String otpRemark) {
            this.otpRemark = otpRemark;
        }

        public String getOfflinephoto() {
            return offlinephoto;
        }

        public void setOfflinephoto(String offlinephoto) {
            this.offlinephoto = offlinephoto;
        }

        public String getVerOtp() {
            return verOtp;
        }

        public void setVerOtp(String verOtp) {
            this.verOtp = verOtp;
        }

        public String getVerOtpDats() {
            return verOtpDats;
        }

        public void setVerOtpDats(String verOtpDats) {
            this.verOtpDats = verOtpDats;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getDongle() {
            return dongle;
        }

        public void setDongle(String dongle) {
            this.dongle = dongle;
        }

        public String getLatlng() {
            return latlng;
        }

        public void setLatlng(String latlng) {
            this.latlng = latlng;
        }
    }
}
