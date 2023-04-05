package com.shaktipumplimited.shaktikusum.bean;

import com.google.gson.annotations.SerializedName;

public class SubmitOfflineDataInput {

    @SerializedName("vbeln")
    private String BillNo;

    @SerializedName("“beneficiary”")
    private String beneficiary;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("project_no")
    private String projectNo;

    @SerializedName("“userid”")
    private String userId;

    @SerializedName("regisno")
    private String regisno;

    @SerializedName("offphoto")
    private String offPhoto;

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
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

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegisno() {
        return regisno;
    }

    public void setRegisno(String regisno) {
        this.regisno = regisno;
    }

    public String getOffPhoto() {
        return offPhoto;
    }

    public void setOffPhoto(String offPhoto) {
        this.offPhoto = offPhoto;
    }
}
