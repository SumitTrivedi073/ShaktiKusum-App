package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class SiteAuditListBean {


    public String enqdoc,
            pernr,
            customer_name,
            father_name,
            billno,
            gstbillno,
            billdate,
            dispdate,
            state,
            statetxt,
            city,
            citytxt,
            contact_no,
            regisno,
            projectno,
            address,
            beneficiary, Vendor;


    public SiteAuditListBean() {


    }


    public SiteAuditListBean(String enqdoc_text,
                             String pernr_text,
                             String customer_name_text,
                             String father_name_text,
                             String bill_no_text,
                             String gstbill_no_text,
                             String bill_date_text,
                             String disp_date_text,
                             String stateid,
                             String state_text,
                             String cityid,
                             String city_text,
                             String contact_no_text,
                             String regisno_text,
                             String projectno_text,
                             String address_text,
                             String beneficiary_text,
                             String vendor) {

        enqdoc = enqdoc_text;
        pernr = pernr_text;
        customer_name = customer_name_text;
        father_name = father_name_text;
        billno = bill_no_text;
        gstbillno = gstbill_no_text;
        billdate = bill_date_text;
        dispdate = disp_date_text;
        state = stateid;
        statetxt = state_text;
        city = cityid;
        citytxt = city_text;
        contact_no = contact_no_text;
        regisno = regisno_text;
        projectno = projectno_text;
        address = address_text;
        beneficiary = beneficiary_text;
        Vendor = vendor;


    }


    public String getEnqdoc() {
        return enqdoc;
    }

    public void setEnqdoc(String enqdoc) {
        this.enqdoc = enqdoc;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getGstbillno() {
        return gstbillno;
    }

    public void setGstbillno(String gstbillno) {
        this.gstbillno = gstbillno;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getDispdate() {
        return dispdate;
    }

    public void setDispdate(String dispdate) {
        this.dispdate = dispdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatetxt() {
        return statetxt;
    }

    public void setStatetxt(String statetxt) {
        this.statetxt = statetxt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitytxt() {
        return citytxt;
    }

    public void setCitytxt(String citytxt) {
        this.citytxt = citytxt;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getRegisno() {
        return regisno;
    }

    public void setRegisno(String regisno) {
        this.regisno = regisno;
    }

    public String getProjectno() {
        return projectno;
    }

    public void setProjectno(String projectno) {
        this.projectno = projectno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        this.Vendor = vendor;
    }
}
