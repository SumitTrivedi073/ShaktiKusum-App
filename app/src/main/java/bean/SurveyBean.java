package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class SurveyBean {

    String project_no= "";
    String login_no="";
    String inst_latitude="";
    String inst_longitude="";
    String survy_bill_no="";
    String spinner_water_resource = "";
    String borewell_size = "";
    String borwell_depth = "";
    String cbl_length = "";
    String surf_head = "";
    String len_dia_dis_pip = "";
    String pernr = "";


    public SurveyBean() {

    }


    public SurveyBean(String pernr_txt,
            String project_no_txt,
            String login_no_txt,
            String inst_latitude_txt,
            String inst_longitude_txt,
            String survy_bill_no_txt,
            String spinner_water_resource_txt,
            String borewell_size_txt,
            String borwell_depth_txt,
            String cbl_length_txt,
            String surf_head_txt,
            String len_dia_dis_pip_txt


    ) {

        pernr = pernr_txt;
        project_no = project_no_txt;
        login_no = login_no_txt;
        inst_latitude = inst_latitude_txt;
        inst_longitude = inst_longitude_txt;
        survy_bill_no = survy_bill_no_txt;
        spinner_water_resource = spinner_water_resource_txt;
        borewell_size = borewell_size_txt;
        borwell_depth = borwell_depth_txt;
        cbl_length = cbl_length_txt;
        surf_head = surf_head_txt;
        len_dia_dis_pip = len_dia_dis_pip_txt;

    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public String getLogin_no() {
        return login_no;
    }

    public void setLogin_no(String login_no) {
        this.login_no = login_no;
    }

    public String getInst_latitude() {
        return inst_latitude;
    }

    public void setInst_latitude(String inst_latitude) {
        this.inst_latitude = inst_latitude;
    }

    public String getInst_longitude() {
        return inst_longitude;
    }

    public void setInst_longitude(String inst_longitude) {
        this.inst_longitude = inst_longitude;
    }

    public String getSurvy_bill_no() {
        return survy_bill_no;
    }

    public void setSurvy_bill_no(String survy_bill_no) {
        this.survy_bill_no = survy_bill_no;
    }

    public String getSpinner_water_resource() {
        return spinner_water_resource;
    }

    public void setSpinner_water_resource(String spinner_water_resource) {
        this.spinner_water_resource = spinner_water_resource;
    }

    public String getBorewell_size() {
        return borewell_size;
    }

    public void setBorewell_size(String borewell_size) {
        this.borewell_size = borewell_size;
    }

    public String getBorwell_depth() {
        return borwell_depth;
    }

    public void setBorwell_depth(String borwell_depth) {
        this.borwell_depth = borwell_depth;
    }

    public String getCbl_length() {
        return cbl_length;
    }

    public void setCbl_length(String cbl_length) {
        this.cbl_length = cbl_length;
    }

    public String getSurf_head() {
        return surf_head;
    }

    public void setSurf_head(String surf_head) {
        this.surf_head = surf_head;
    }

    public String getLen_dia_dis_pip() {
        return len_dia_dis_pip;
    }

    public void setLen_dia_dis_pip(String len_dia_dis_pip) {
        this.len_dia_dis_pip = len_dia_dis_pip;
    }
}




