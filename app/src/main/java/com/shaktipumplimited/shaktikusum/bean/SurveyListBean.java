package com.shaktipumplimited.shaktikusum.bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class SurveyListBean {




    public String ben_id,
            pernr,
            regino,
            custnam,
            contctno,
            state,
            district,
            address;


    public SurveyListBean() {


    }


    public SurveyListBean(String benid_text,
                          String pernr_text,
                          String regino_text,
                          String customer_name_text,
                          String contact_no_text,
                          String state_text,
                          String district_text,
                          String address_text



    ) {

        ben_id = benid_text;
        pernr = pernr_text;
        regino = regino_text;
        custnam = customer_name_text;
        contctno = contact_no_text;
        state = state_text;
        district = district_text;
        address = address_text;


    }

    public String getRegino() {
        return regino;
    }

    public void setRegino(String regino) {
        this.regino = regino;
    }

    public String getBen_id() {
        return ben_id;
    }

    public void setBen_id(String ben_id) {
        this.ben_id = ben_id;
    }

    public String getCustnam() {
        return custnam;
    }

    public void setCustnam(String custnam) {
        this.custnam = custnam;
    }

    public String getContctno() {
        return contctno;
    }

    public void setContctno(String contctno) {
        this.contctno = contctno;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


}
