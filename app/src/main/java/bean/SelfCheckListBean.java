package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SelfCheckListBean implements Serializable {

    @SerializedName("installation_data")
    @Expose
    private List<InstallationDatum> installationData;

    public List<InstallationDatum> getInstallationData() {
        return installationData;
    }

    public void setInstallationData(List<InstallationDatum> installationData) {
        this.installationData = installationData;
    }

    public class InstallationDatum implements Serializable {

        @SerializedName("vbeln")
        @Expose
        private String vbeln;
        @SerializedName("gst_inv_no")
        @Expose
        private String gstInvNo;
        @SerializedName("fkdat")
        @Expose
        private String fkdat;
        @SerializedName("dispatch_date")
        @Expose
        private String dispatchDate;
        @SerializedName("kunnr")
        @Expose
        private String kunnr;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("regio")
        @Expose
        private String regio;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("cityc")
        @Expose
        private String cityc;
        @SerializedName("ort02")
        @Expose
        private String ort02;
        @SerializedName("stras")
        @Expose
        private String stras;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("regio_txt")
        @Expose
        private String regioTxt;
        @SerializedName("cityc_txt")
        @Expose
        private String citycTxt;
        @SerializedName("pump_sernr")
        @Expose
        private String pumpSernr;
        @SerializedName("controller_sernr")
        @Expose
        private String controllerSernr;
        @SerializedName("controller_matno")
        @Expose
        private String controllerMatno;
        @SerializedName("set_matno")
        @Expose
        private String setMatno;
        @SerializedName("motor_matnr")
        @Expose
        private String motorMatnr;
        @SerializedName("motor_sernr")
        @Expose
        private String motorSernr;
        @SerializedName("simha2")
        @Expose
        private String simha2;
        @SerializedName("simno")
        @Expose
        private String simno;
        @SerializedName("simmob")
        @Expose
        private String simmob;
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("process_no")
        @Expose
        private String processNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("module_qty")
        @Expose
        private String moduleQty;
        @SerializedName("sync")
        @Expose
        private String sync;
        @SerializedName("contact_no")
        @Expose
        private String contactNo;
        @SerializedName("hp")
        @Expose
        private String hp;
        @SerializedName("pump_load")
        @Expose
        private String pumpLoad;

        public String getVbeln() {
            return vbeln;
        }

        public void setVbeln(String vbeln) {
            this.vbeln = vbeln;
        }

        public String getGstInvNo() {
            return gstInvNo;
        }

        public void setGstInvNo(String gstInvNo) {
            this.gstInvNo = gstInvNo;
        }

        public String getFkdat() {
            return fkdat;
        }

        public void setFkdat(String fkdat) {
            this.fkdat = fkdat;
        }

        public String getDispatchDate() {
            return dispatchDate;
        }

        public void setDispatchDate(String dispatchDate) {
            this.dispatchDate = dispatchDate;
        }

        public String getKunnr() {
            return kunnr;
        }

        public void setKunnr(String kunnr) {
            this.kunnr = kunnr;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegio() {
            return regio;
        }

        public void setRegio(String regio) {
            this.regio = regio;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCityc() {
            return cityc;
        }

        public void setCityc(String cityc) {
            this.cityc = cityc;
        }

        public String getOrt02() {
            return ort02;
        }

        public void setOrt02(String ort02) {
            this.ort02 = ort02;
        }

        public String getStras() {
            return stras;
        }

        public void setStras(String stras) {
            this.stras = stras;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRegioTxt() {
            return regioTxt;
        }

        public void setRegioTxt(String regioTxt) {
            this.regioTxt = regioTxt;
        }

        public String getCitycTxt() {
            return citycTxt;
        }

        public void setCitycTxt(String citycTxt) {
            this.citycTxt = citycTxt;
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

        public String getControllerMatno() {
            return controllerMatno;
        }

        public void setControllerMatno(String controllerMatno) {
            this.controllerMatno = controllerMatno;
        }

        public String getSetMatno() {
            return setMatno;
        }

        public void setSetMatno(String setMatno) {
            this.setMatno = setMatno;
        }

        public String getMotorMatnr() {
            return motorMatnr;
        }

        public void setMotorMatnr(String motorMatnr) {
            this.motorMatnr = motorMatnr;
        }

        public String getMotorSernr() {
            return motorSernr;
        }

        public void setMotorSernr(String motorSernr) {
            this.motorSernr = motorSernr;
        }

        public String getSimha2() {
            return simha2;
        }

        public void setSimha2(String simha2) {
            this.simha2 = simha2;
        }

        public String getSimno() {
            return simno;
        }

        public void setSimno(String simno) {
            this.simno = simno;
        }

        public String getSimmob() {
            return simmob;
        }

        public void setSimmob(String simmob) {
            this.simmob = simmob;
        }

        public String getBeneficiary() {
            return beneficiary;
        }

        public void setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
        }

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public String getProcessNo() {
            return processNo;
        }

        public void setProcessNo(String processNo) {
            this.processNo = processNo;
        }

        public String getRegisno() {
            return regisno;
        }

        public void setRegisno(String regisno) {
            this.regisno = regisno;
        }

        public String getModuleQty() {
            return moduleQty;
        }

        public void setModuleQty(String moduleQty) {
            this.moduleQty = moduleQty;
        }

        public String getSync() {
            return sync;
        }

        public void setSync(String sync) {
            this.sync = sync;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getHp() {
            return hp;
        }

        public void setHp(String hp) {
            this.hp = hp;
        }

        public String getPumpLoad() {
            return pumpLoad;
        }

        public void setPumpLoad(String pumpLoad) {
            this.pumpLoad = pumpLoad;
        }


    }
}



