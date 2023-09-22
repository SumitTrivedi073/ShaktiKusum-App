package debugapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoutePlanModel {
    @SerializedName("installation_data")
    @Expose
    private List<InstallationDatum> installationData;

    public List<InstallationDatum> getInstallationData() {
        return installationData;
    }

    public void setInstallationData(List<InstallationDatum> installationData) {
        this.installationData = installationData;
    }
    public static class InstallationDatum {

        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("process_no")
        @Expose
        private String processNo;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("site_adrc")
        @Expose
        private String siteAdrc;
        @SerializedName("exis_pump_hp")
        @Expose
        private String exisPumpHp;
        @SerializedName("pump_ac_dc")
        @Expose
        private String pumpAcDc;

        private boolean isChecked;

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

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getBeneficiary() {
            return beneficiary;
        }

        public void setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
        }

        public String getSiteAdrc() {
            return siteAdrc;
        }

        public void setSiteAdrc(String siteAdrc) {
            this.siteAdrc = siteAdrc;
        }

        public String getExisPumpHp() {
            return exisPumpHp;
        }

        public void setExisPumpHp(String exisPumpHp) {
            this.exisPumpHp = exisPumpHp;
        }

        public String getPumpAcDc() {
            return pumpAcDc;
        }

        public void setPumpAcDc(String pumpAcDc) {
            this.pumpAcDc = pumpAcDc;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
