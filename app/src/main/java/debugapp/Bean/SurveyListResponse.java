
package debugapp.Bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SurveyListResponse implements Serializable {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("beneficiary")
    private String mBeneficiary;
    @SerializedName("city")
    private String mCity;
    @SerializedName("cityc_txt")
    private String mCitycTxt;
    @SerializedName("customer_name")
    private String mCustomerName;
    @SerializedName("lifnr")
    private String mLifnr;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("process_no")
    private String mProcessNo;
    @SerializedName("project_no")
    private String mProjectNo;
    @SerializedName("regio_txt")
    private String mRegioTxt;
    @SerializedName("regisno")
    private String mRegisno;
    @SerializedName("state")
    private String mState;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getBeneficiary() {
        return mBeneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        mBeneficiary = beneficiary;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCitycTxt() {
        return mCitycTxt;
    }

    public void setCitycTxt(String citycTxt) {
        mCitycTxt = citycTxt;
    }

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        mCustomerName = customerName;
    }

    public String getLifnr() {
        return mLifnr;
    }

    public void setLifnr(String lifnr) {
        mLifnr = lifnr;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getProcessNo() {
        return mProcessNo;
    }

    public void setProcessNo(String processNo) {
        mProcessNo = processNo;
    }

    public String getProjectNo() {
        return mProjectNo;
    }

    public void setProjectNo(String projectNo) {
        mProjectNo = projectNo;
    }

    public String getRegioTxt() {
        return mRegioTxt;
    }

    public void setRegioTxt(String regioTxt) {
        mRegioTxt = regioTxt;
    }

    public String getRegisno() {
        return mRegisno;
    }

    public void setRegisno(String regisno) {
        mRegisno = regisno;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

}
