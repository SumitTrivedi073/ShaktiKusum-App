package com.shaktipumplimited.shaktikusum.bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class RegistrationBean {


    public String enqdoc, pernr,
            project_no,
            login_no,
            customer_name,
            father_name,
            date,
            lat,
            lng,
            state,
            statetxt,
            city,
            citytxt,
            tehsil,
            village,
            contact_no,
            aadhar_no,
            bank_name,
            bank_acc_no,
            account_type,
            branch_name,
            ifsc_code,
            amount, pdf, sync;

    public String photo1 = "";
    public String photo2 = "";
    public String photo3 = "";
    public String photo4 = "";
    public String photo5 = "";
    public String photo6 = "";
    public String photo7 = "";
    public String photo8 = "";
    public String photo9 = "";
    public String photo10 = "";
    public String photo11 = "";
    public String photo12 = "";


    public RegistrationBean() {


    }

    public RegistrationBean(String enqdoc_text,
                            String pernr_text,
                            String project_no_text,
                            String login_no_text,
                            String date_text,
                            String lat_text,
                            String lng_text,
                            String customer_name_text,
                            String father_name_text,
                            String state,
                            String state_text,
                            String city,
                            String city_text,
                            String tehsil_text,
                            String village_text,
                            String contact_no_text,
                            String aadhar_no_text,
                            String bank_name_text,
                            String bank_acc_no_text,
                            String account_type_text,
                            String branch_name_text,
                            String ifsc_code_text,
                            String amount_text,
                            String pdf_text


    ) {

        enqdoc = enqdoc_text;
        pernr = pernr_text;
        project_no = project_no_text;
        login_no = login_no_text;
        date = date_text;
        lat = lat_text;
        lng = lng_text;
        customer_name = customer_name_text;
        father_name = father_name_text;
        state = state;
        statetxt = state_text;
        city = city;
        citytxt = city_text;
        tehsil = tehsil_text;
        village = village_text;
        contact_no = contact_no_text;
        aadhar_no = aadhar_no_text;
        bank_name = bank_name_text;
        bank_acc_no = bank_acc_no_text;
        account_type = account_type_text;
        branch_name = branch_name_text;
        ifsc_code = ifsc_code_text;
        amount = amount_text;
        pdf = pdf_text;

    }


    public RegistrationBean(String enqdoc_text,
                            String pernr_text,
                            String project_no_text,
                            String login_no_text,
                            String date_text,
                            String lat_text,
                            String lng_text,
                            String customer_name_text,
                            String father_name_text,
                            String stateid,
                            String state_text,
                            String cityid,
                            String city_text,
                            String tehsil_text,
                            String village_text,
                            String contact_no_text,
                            String aadhar_no_text,
                            String bank_name_text,
                            String bank_acc_no_text,
                            String account_type_text,
                            String branch_name_text,
                            String ifsc_code_text,
                            String amount_text,
                            String pdf_text,
                            String sync_text


    ) {

        enqdoc = enqdoc_text;
        pernr = pernr_text;
        project_no = project_no_text;
        login_no = login_no_text;
        date = date_text;
        lat = lat_text;
        lng = lng_text;
        customer_name = customer_name_text;
        father_name = father_name_text;
        state = stateid;
        statetxt = state_text;
        city = cityid;
        citytxt = city_text;
        tehsil = tehsil_text;
        village = village_text;
        contact_no = contact_no_text;
        aadhar_no = aadhar_no_text;
        bank_name = bank_name_text;
        bank_acc_no = bank_acc_no_text;
        account_type = account_type_text;
        branch_name = branch_name_text;
        ifsc_code = ifsc_code_text;
        amount = amount_text;
        pdf = pdf_text;
        sync = sync_text;

    }


    public RegistrationBean(
            String enqdoc_text,
            String pernr_text,
            String sync_text
    ) {
        pernr = pernr_text;
        enqdoc = enqdoc_text;
        sync = sync_text;

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

    public String getLogin_no() {
        return login_no;
    }

    public void setLogin_no(String login_no) {
        this.login_no = login_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
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

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_acc_no() {
        return bank_acc_no;
    }

    public void setBank_acc_no(String bank_acc_no) {
        this.bank_acc_no = bank_acc_no;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getPhoto6() {
        return photo6;
    }

    public void setPhoto6(String photo6) {
        this.photo6 = photo6;
    }

    public String getPhoto7() {
        return photo7;
    }

    public void setPhoto7(String photo7) {
        this.photo7 = photo7;
    }

    public String getPhoto8() {
        return photo8;
    }

    public void setPhoto8(String photo8) {
        this.photo8 = photo8;
    }

    public String getPhoto9() {
        return photo9;
    }

    public void setPhoto9(String photo9) {
        this.photo9 = photo9;
    }

    public String getPhoto10() {
        return photo10;
    }

    public void setPhoto10(String photo10) {
        this.photo10 = photo10;
    }

    public String getPhoto11() {
        return photo11;
    }

    public void setPhoto11(String photo11) {
        this.photo11 = photo11;
    }

    public String getPhoto12() {
        return photo12;
    }

    public void setPhoto12(String photo12) {
        this.photo12 = photo12;
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


}
