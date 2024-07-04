package bean;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocDistListBean implements Serializable {

    @SerializedName("listing")
    @Expose
    private List<Listing> listing;

    public List<Listing> getListing() {
        return listing;
    }

    public void setListing(List<Listing> listing) {
        this.listing = listing;
    }

    public DocDistListBean withListing(List<Listing> listing) {
        this.listing = listing;
        return this;
    }



public class Listing implements Serializable{

    @SerializedName("regisno")
    @Expose
    private String regisno;
    @SerializedName("billno")
    @Expose
    private String billno;
    @SerializedName("project_no")
    @Expose
    private String projectNo;
    @SerializedName("process_no")
    @Expose
    private String processNo;
    @SerializedName("regis_date")
    @Expose
    private String regisDate;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("project_login_no")
    @Expose
    private String projectLoginNo;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("beneficiary")
    @Expose
    private String beneficiary;

    public String getRegisno() {
        return regisno;
    }

    public void setRegisno(String regisno) {
        this.regisno = regisno;
    }

    public Listing withRegisno(String regisno) {
        this.regisno = regisno;
        return this;
    }

    public String getbillno() {
        return billno;
    }

    public void setbillno(String billno) {
        this.billno = billno;
    }

    public Listing withbillno(String billno) {
        this.billno = billno;
        return this;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public Listing withProjectNo(String projectNo) {
        this.projectNo = projectNo;
        return this;
    }

    public String getProcessNo() {
        return processNo;
    }

    public void setProcessNo(String processNo) {
        this.processNo = processNo;
    }

    public Listing withProcessNo(String processNo) {
        this.processNo = processNo;
        return this;
    }

    public String getRegisDate() {
        return regisDate;
    }

    public void setRegisDate(String regisDate) {
        this.regisDate = regisDate;
    }

    public Listing withRegisDate(String regisDate) {
        this.regisDate = regisDate;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Listing withUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getProjectLoginNo() {
        return projectLoginNo;
    }

    public void setProjectLoginNo(String projectLoginNo) {
        this.projectLoginNo = projectLoginNo;
    }

    public Listing withProjectLoginNo(String projectLoginNo) {
        this.projectLoginNo = projectLoginNo;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Listing withCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Listing withBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
        return this;
    }
  }
}