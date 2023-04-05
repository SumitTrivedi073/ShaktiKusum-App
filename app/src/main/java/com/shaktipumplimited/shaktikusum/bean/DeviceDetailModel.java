package com.shaktipumplimited.shaktikusum.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceDetailModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private Response response;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {

        @SerializedName("DeviceNo")
        @Expose
        private String deviceNo;
        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("CustomerPhoneNo")
        @Expose
        private String customerPhoneNo;
        @SerializedName("OperatorName")
        @Expose
        private String operatorName;
        @SerializedName("IsLogin")
        @Expose
        private Boolean isLogin;
        @SerializedName("PumpStatus")
        @Expose
        private Boolean pumpStatus;

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerPhoneNo() {
            return customerPhoneNo;
        }

        public void setCustomerPhoneNo(String customerPhoneNo) {
            this.customerPhoneNo = customerPhoneNo;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public Boolean getIsLogin() {
            return isLogin;
        }

        public void setIsLogin(Boolean isLogin) {
            this.isLogin = isLogin;
        }

        public Boolean getPumpStatus() {
            return pumpStatus;
        }

        public void setPumpStatus(Boolean pumpStatus) {
            this.pumpStatus = pumpStatus;
        }

    }

}
