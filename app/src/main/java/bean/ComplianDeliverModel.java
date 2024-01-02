package bean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ComplianDeliverModel{

    @SerializedName("Complaint_data")
    @Expose
    private  List<ComplianDeliverModel.ComplaintDatum> complaintDatum;

    public  List<ComplianDeliverModel.ComplaintDatum> getResponse() {
        return complaintDatum;
    }

    public void setResponse(List<ComplianDeliverModel.ComplaintDatum> response) {
        complaintDatum = response;
    }

    public static class ComplaintDatum {

        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("process_no")
        @Expose
        private String processNo;
        @SerializedName("project_login_no")
        @Expose
        private String projectLoginNo;
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("remark")
        @Expose
        private String remark;
        @SerializedName("damage_pump")
        @Expose
        private String damagePump;
        @SerializedName("damage_motor")
        @Expose
        private String damageMotor;
        @SerializedName("damage_cont")
        @Expose
        private String damageCont;
        @SerializedName("billno")
        @Expose
        private String billno;
        @SerializedName("pump")
        @Expose
        private String pump;
        @SerializedName("motor")
        @Expose
        private String motor;
        @SerializedName("controller")
        @Expose
        private String controller;
        @SerializedName("other")
        @Expose
        private String other;
        @SerializedName("userid")
        @Expose
        private String userid;

        public String getRegisno() {
            return regisno;
        }

        public void setRegisno(String regisno) {
            this.regisno = regisno;
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

        public String getProjectLoginNo() {
            return projectLoginNo;
        }

        public void setProjectLoginNo(String projectLoginNo) {
            this.projectLoginNo = projectLoginNo;
        }

        public String getBeneficiary() {
            return beneficiary;
        }

        public void setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDamagePump() {
            return damagePump;
        }

        public void setDamagePump(String damagePump) {
            this.damagePump = damagePump;
        }

        public String getDamageMotor() {
            return damageMotor;
        }

        public void setDamageMotor(String damageMotor) {
            this.damageMotor = damageMotor;
        }

        public String getDamageCont() {
            return damageCont;
        }

        public void setDamageCont(String damageCont) {
            this.damageCont = damageCont;
        }

        public String getBillno() {
            return billno;
        }

        public void setBillno(String billno) {
            this.billno = billno;
        }

        public String getPump() {
            return pump;
        }

        public void setPump(String pump) {
            this.pump = pump;
        }

        public String getMotor() {
            return motor;
        }

        public void setMotor(String motor) {
            this.motor = motor;
        }

        public String getController() {
            return controller;
        }

        public void setController(String controller) {
            this.controller = controller;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

    }
}