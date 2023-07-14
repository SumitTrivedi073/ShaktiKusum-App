package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SurveyListModel implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private List<Response> response;

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

    public static class Response implements Serializable  {

        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("regio_txt")
        @Expose
        private String regioTxt;
        @SerializedName("cityc_txt")
        @Expose
        private String citycTxt;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("process_no")
        @Expose
        private String processNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("lifnr")
        @Expose
        private String lifnr;

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getLifnr() {
            return lifnr;
        }

        public void setLifnr(String lifnr) {
            this.lifnr = lifnr;
        }

    }

}
