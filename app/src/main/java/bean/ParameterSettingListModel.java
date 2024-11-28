package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ParameterSettingListModel implements Serializable {
    @SerializedName("installation_data")
    @Expose
    private List<InstallationDatum> installationData;

    public List<InstallationDatum> getInstallationData() {
        return installationData;
    }

    public void setInstallationData(List<InstallationDatum> installationData) {
        this.installationData = installationData;
    }
    public static class InstallationDatum implements Serializable{

        @SerializedName("vbeln")
        @Expose
        private String vbeln;
        @SerializedName("kunnr")
        @Expose
        private String kunnr;
        @SerializedName("name")
        @Expose
        private String name;
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
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;

        public String getVbeln() {
            return vbeln;
        }

        public void setVbeln(String vbeln) {
            this.vbeln = vbeln;
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

        public String getRegisno() {
            return regisno;
        }

        public void setRegisno(String regisno) {
            this.regisno = regisno;
        }

    }
}
