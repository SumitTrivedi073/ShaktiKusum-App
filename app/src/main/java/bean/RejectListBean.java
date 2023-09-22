package bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 12/30/2016.
 */
public class RejectListBean implements Serializable {
    @SerializedName("reject_data")
    @Expose
    private List<RejectDatum> rejectData;

    public List<RejectDatum> getRejectData() {
        return rejectData;
    }

    public void setRejectData(List<RejectDatum> rejectData) {
        this.rejectData = rejectData;
    }
    public static class RejectDatum implements Serializable {

        @SerializedName("mandt")
        @Expose
        private String mandt;
        @SerializedName("vbeln")
        @Expose
        private String vbeln;
        @SerializedName("bill_dt")
        @Expose
        private String billDt;
        @SerializedName("aud_dt")
        @Expose
        private String audDt;
        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("regisno")
        @Expose
        private String regisno;
        @SerializedName("beneficiary")
        @Expose
        private String beneficiary;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("contactno")
        @Expose
        private String contactno;
        @SerializedName("erdat")
        @Expose
        private String erdat;
        @SerializedName("ertim")
        @Expose
        private String ertim;
        @SerializedName("foundation")
        @Expose
        private String foundation;
        @SerializedName("found_remark")
        @Expose
        private String foundRemark;
        @SerializedName("stru_assem")
        @Expose
        private String struAssem;
        @SerializedName("stru_remark")
        @Expose
        private String struRemark;
        @SerializedName("drv_mount")
        @Expose
        private String drvMount;
        @SerializedName("drv_mount_remark")
        @Expose
        private String drvMountRemark;
        @SerializedName("la_earth")
        @Expose
        private String laEarth;
        @SerializedName("la_earth_remark")
        @Expose
        private String laEarthRemark;
        @SerializedName("wrkmn_qlty")
        @Expose
        private String wrkmnQlty;
        @SerializedName("wrkmn_qlty_remark")
        @Expose
        private String wrkmnQltyRemark;
        @SerializedName("site_rating")
        @Expose
        private String siteRating;

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

        public String getBillDt() {
            return billDt;
        }

        public void setBillDt(String billDt) {
            this.billDt = billDt;
        }

        public String getAudDt() {
            return audDt;
        }

        public void setAudDt(String audDt) {
            this.audDt = audDt;
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

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContactno() {
            return contactno;
        }

        public void setContactno(String contactno) {
            this.contactno = contactno;
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

        public String getFoundation() {
            return foundation;
        }

        public void setFoundation(String foundation) {
            this.foundation = foundation;
        }

        public String getFoundRemark() {
            return foundRemark;
        }

        public void setFoundRemark(String foundRemark) {
            this.foundRemark = foundRemark;
        }

        public String getStruAssem() {
            return struAssem;
        }

        public void setStruAssem(String struAssem) {
            this.struAssem = struAssem;
        }

        public String getStruRemark() {
            return struRemark;
        }

        public void setStruRemark(String struRemark) {
            this.struRemark = struRemark;
        }

        public String getDrvMount() {
            return drvMount;
        }

        public void setDrvMount(String drvMount) {
            this.drvMount = drvMount;
        }

        public String getDrvMountRemark() {
            return drvMountRemark;
        }

        public void setDrvMountRemark(String drvMountRemark) {
            this.drvMountRemark = drvMountRemark;
        }

        public String getLaEarth() {
            return laEarth;
        }

        public void setLaEarth(String laEarth) {
            this.laEarth = laEarth;
        }

        public String getLaEarthRemark() {
            return laEarthRemark;
        }

        public void setLaEarthRemark(String laEarthRemark) {
            this.laEarthRemark = laEarthRemark;
        }

        public String getWrkmnQlty() {
            return wrkmnQlty;
        }

        public void setWrkmnQlty(String wrkmnQlty) {
            this.wrkmnQlty = wrkmnQlty;
        }

        public String getWrkmnQltyRemark() {
            return wrkmnQltyRemark;
        }

        public void setWrkmnQltyRemark(String wrkmnQltyRemark) {
            this.wrkmnQltyRemark = wrkmnQltyRemark;
        }

        public String getSiteRating() {
            return siteRating;
        }

        public void setSiteRating(String siteRating) {
            this.siteRating = siteRating;
        }

    }
}
