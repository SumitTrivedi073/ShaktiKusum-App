package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RejectInstallationModel implements Serializable {
    @SerializedName("reject_data")
    @Expose
    private List<RejectDatum> rejectData;

    public List<RejectDatum> getRejectData() {
        return rejectData;
    }

    public void setRejectData(List<RejectDatum> rejectData) {
        this.rejectData = rejectData;
    }

    public static class RejectDatum implements  Serializable{

        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("vbeln")
        @Expose
        private String vbeln;
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("photos1")
        @Expose
        private String photos1;
        @SerializedName("photos2")
        @Expose
        private String photos2;
        @SerializedName("photos3")
        @Expose
        private String photos3;
        @SerializedName("photos4")
        @Expose
        private String photos4;
        @SerializedName("photos5")
        @Expose
        private String photos5;
        @SerializedName("photos6")
        @Expose
        private String photos6;
        @SerializedName("photos7")
        @Expose
        private String photos7;
        @SerializedName("photos8")
        @Expose
        private String photos8;
        @SerializedName("photos9")
        @Expose
        private String photos9;
        @SerializedName("photos10")
        @Expose
        private String photos10;
        @SerializedName("photos11")
        @Expose
        private String photos11;
        @SerializedName("photos12")
        @Expose
        private String photos12;
        @SerializedName("remark1")
        @Expose
        private String remark1;
        @SerializedName("remark2")
        @Expose
        private String remark2;
        @SerializedName("remark3")
        @Expose
        private String remark3;
        @SerializedName("remark4")
        @Expose
        private String remark4;
        @SerializedName("remark5")
        @Expose
        private String remark5;
        @SerializedName("remark6")
        @Expose
        private String remark6;
        @SerializedName("remark7")
        @Expose
        private String remark7;
        @SerializedName("remark8")
        @Expose
        private String remark8;
        @SerializedName("remark9")
        @Expose
        private String remark9;
        @SerializedName("remark10")
        @Expose
        private String remark10;
        @SerializedName("remark11")
        @Expose
        private String remark11;
        @SerializedName("remark12")
        @Expose
        private String remark12;

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

        public String getVbeln() {
            return vbeln;
        }

        public void setVbeln(String vbeln) {
            this.vbeln = vbeln;
        }

        public String getBeneficiary() {
            return beneficiary;
        }

        public void setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getPhotos1() {
            return photos1;
        }

        public void setPhotos1(String photos1) {
            this.photos1 = photos1;
        }

        public String getPhotos2() {
            return photos2;
        }

        public void setPhotos2(String photos2) {
            this.photos2 = photos2;
        }

        public String getPhotos3() {
            return photos3;
        }

        public void setPhotos3(String photos3) {
            this.photos3 = photos3;
        }

        public String getPhotos4() {
            return photos4;
        }

        public void setPhotos4(String photos4) {
            this.photos4 = photos4;
        }

        public String getPhotos5() {
            return photos5;
        }

        public void setPhotos5(String photos5) {
            this.photos5 = photos5;
        }

        public String getPhotos6() {
            return photos6;
        }

        public void setPhotos6(String photos6) {
            this.photos6 = photos6;
        }

        public String getPhotos7() {
            return photos7;
        }

        public void setPhotos7(String photos7) {
            this.photos7 = photos7;
        }

        public String getPhotos8() {
            return photos8;
        }

        public void setPhotos8(String photos8) {
            this.photos8 = photos8;
        }

        public String getPhotos9() {
            return photos9;
        }

        public void setPhotos9(String photos9) {
            this.photos9 = photos9;
        }

        public String getPhotos10() {
            return photos10;
        }

        public void setPhotos10(String photos10) {
            this.photos10 = photos10;
        }

        public String getPhotos11() {
            return photos11;
        }

        public void setPhotos11(String photos11) {
            this.photos11 = photos11;
        }

        public String getPhotos12() {
            return photos12;
        }

        public void setPhotos12(String photos12) {
            this.photos12 = photos12;
        }

        public String getRemark1() {
            return remark1;
        }

        public void setRemark1(String remark1) {
            this.remark1 = remark1;
        }

        public String getRemark2() {
            return remark2;
        }

        public void setRemark2(String remark2) {
            this.remark2 = remark2;
        }

        public String getRemark3() {
            return remark3;
        }

        public void setRemark3(String remark3) {
            this.remark3 = remark3;
        }

        public String getRemark4() {
            return remark4;
        }

        public void setRemark4(String remark4) {
            this.remark4 = remark4;
        }

        public String getRemark5() {
            return remark5;
        }

        public void setRemark5(String remark5) {
            this.remark5 = remark5;
        }

        public String getRemark6() {
            return remark6;
        }

        public void setRemark6(String remark6) {
            this.remark6 = remark6;
        }

        public String getRemark7() {
            return remark7;
        }

        public void setRemark7(String remark7) {
            this.remark7 = remark7;
        }

        public String getRemark8() {
            return remark8;
        }

        public void setRemark8(String remark8) {
            this.remark8 = remark8;
        }

        public String getRemark9() {
            return remark9;
        }

        public void setRemark9(String remark9) {
            this.remark9 = remark9;
        }

        public String getRemark10() {
            return remark10;
        }

        public void setRemark10(String remark10) {
            this.remark10 = remark10;
        }

        public String getRemark11() {
            return remark11;
        }

        public void setRemark11(String remark11) {
            this.remark11 = remark11;
        }

        public String getRemark12() {
            return remark12;
        }

        public void setRemark12(String remark12) {
            this.remark12 = remark12;
        }

    }

}

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */



