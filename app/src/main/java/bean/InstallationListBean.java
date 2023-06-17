package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class InstallationListBean {
    public String enqdoc,
            pernr,
            customer_name,
            father_name,
            billno,
            kunnr,
            gstbillno,
            billdate,
            dispdate,
            state,
            statetxt,
            city,
            citytxt,
            tehsil,
            village,
            contact_no,
            controller,
            motor,
            pump,
            regisno,
            projectno,
            loginno,
            moduleqty,
            address,
            simno,
            beneficiary,
            set_matno,
            simha2,
            sync,
            CONTACT_NO,noOfModule;
//    streetName;

    public InstallationListBean() {}

    public InstallationListBean(String enqdoc_text,
                                String pernr_text,
                                String customer_name_text,
                                String father_name_text,
                                String bill_no_text,
                                String kunnr_text,
                                String gstbill_no_text,
                                String bill_date_text,
                                String disp_date_text,
                                String stateid,
                                String state_text,
                                String cityid,
                                String city_text,
                                String tehsil_text,
                                String village_text,
                                String contact_no_text,
                                String controller_text,
                                String motor_text,
                                String pump_text,
                                String regisno_text,
                                String projectno_text,
                                String loginno_text,
                                String moduleqty_text,
                                String address_text,
                                String simno_text,
                                String beneficiary_text,
                                String set_matno_text,
                                String simha2_text,
                                String sync_text,
                                String mCONTACT_NO,
                                 String NoOfModule
    ) {
        enqdoc = enqdoc_text;
        pernr = pernr_text;
        customer_name = customer_name_text;
        father_name = father_name_text;
        billno = bill_no_text;
        kunnr = kunnr_text;
        gstbillno = gstbill_no_text;
        billdate = bill_date_text;
        dispdate = disp_date_text;
        state = stateid;
        statetxt = state_text;
        city = cityid;
        citytxt = city_text;
        tehsil = tehsil_text;
        village = village_text;
        contact_no = contact_no_text;
        controller = controller_text;
        motor = motor_text;
        pump = pump_text;
        regisno = regisno_text;
        projectno = projectno_text;
        loginno = loginno_text;
        moduleqty = moduleqty_text;
        address = address_text;
        simno = simno_text;
        beneficiary = beneficiary_text;
        set_matno = set_matno_text;
        simha2 = simha2_text;
        sync = sync_text;
        CONTACT_NO = mCONTACT_NO;
        noOfModule = NoOfModule;
    }

    public String getCUS_CONTACT_NO() {
        return CONTACT_NO;
    }

    public void setCUS_CONTACT_NO(String CONTACT_NO) {
        this.CONTACT_NO = CONTACT_NO;
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

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getPump() {
        return pump;
    }

    public void setPump(String pump) {
        this.pump = pump;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getSimha2() {
        return simha2;
    }

    public void setSimha2(String simha2) {
        this.simha2 = simha2;
    }

    public String getStatetxt() {
        return statetxt;
    }

    public void setStatetxt(String statetxt) {
        this.statetxt = statetxt;
    }

    public String getCitytxt() {
        return citytxt;
    }

    public void setCitytxt(String citytxt) {
        this.citytxt = citytxt;
    }

    public String getGstbillno() {
        return gstbillno;
    }

    public void setGstbillno(String gstbillno) {
        this.gstbillno = gstbillno;
    }

    public String getSimno() {
        return simno;
    }

    public void setSimno(String simno) {
        this.simno = simno;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getSet_matno() {
        return set_matno;
    }

    public void setSet_matno(String set_matno) {
        this.set_matno = set_matno;
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

    public String getLoginno() {
        return loginno;
    }

    public void setLoginno(String loginno) {
        this.loginno = loginno;
    }

    public String getModuleqty() {
        return moduleqty;
    }

    public void setModuleqty(String moduleqty) {
        this.moduleqty = moduleqty;
    }

    public String getNoOfModule() {
        return noOfModule;
    }

    public void setNoOfModule(String noOfModule) {
        this.noOfModule = noOfModule;
    }
}
