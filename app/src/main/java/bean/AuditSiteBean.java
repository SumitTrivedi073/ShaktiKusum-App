package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class AuditSiteBean {


    public String pernr = "";
    public String project_no = "";
    public String inst_bill_no = "";
    public String aud_date = "";
    public String bill_date = "";
    public String name = "";
    public String mobile_no = "";
    public String state_ins_id = "";
    public String district_ins_id = "";
    public String address_ins = "";
    public String regis_no = "";
    public String Found = "";
    public String Found_rmk = "";
    public String struc_assem = "";
    public String struc_assem_rmk = "";
    public String drv_mount = "";
    public String drv_mount_rmk = "";
    public String la_earthing = "";
    public String la_earthing_rmk = "";
    public String wrk_quality = "";
    public String wrk_quality_rmk = "";
    public Float site_art ;


    public AuditSiteBean() {

    }


    public AuditSiteBean(String pernr_txt,
                         String project_no_text,
                         String inst_bill_no_txt,
                         String bill_date_txt,
                         String aud_date_txt,
                         String name_txt,
                         String mobile_no_txt,
                         String state_ins_Id,
                         String district_ins_Id,
                         String address_ins_txt,
                         String regis_no_txt,
                         String Found_txt,
                         String struc_assem_txt,
                         String drv_mount_txt,
                         String la_earthing_txt,
                         String wrk_quality_txt,
                         Float site_art_txt,
                         String Found_rmk_txt,
                         String struc_assem_rmk_txt,
                         String drv_mount_rmk_txt,
                         String la_earthing_rmk_txt,
                         String wrk_quality_rmk_txt


    ) {


        pernr = pernr_txt;
        project_no = project_no_text;
        inst_bill_no = inst_bill_no_txt;
        bill_date = bill_date_txt;
        aud_date = aud_date_txt;
        name = name_txt;
        mobile_no = mobile_no_txt;
        address_ins = address_ins_txt;
        state_ins_id = state_ins_Id;
        district_ins_id = district_ins_Id;
        address_ins = address_ins_txt;
        regis_no = regis_no_txt;
        Found = Found_txt;
        struc_assem = struc_assem_txt;
        drv_mount = drv_mount_txt;
        la_earthing = la_earthing_txt;
        wrk_quality = wrk_quality_txt;
        site_art = site_art_txt;
        Found_rmk = Found_rmk_txt;
        struc_assem_rmk = struc_assem_rmk_txt;
        drv_mount_rmk = drv_mount_rmk_txt;
        la_earthing_rmk = la_earthing_rmk_txt;
        wrk_quality_rmk = wrk_quality_rmk_txt;

    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getInst_bill_no() {
        return inst_bill_no;
    }

    public void setInst_bill_no(String inst_bill_no) {
        this.inst_bill_no = inst_bill_no;
    }


    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFound_rmk() {
        return Found_rmk;
    }

    public void setFound_rmk(String found_rmk) {
        Found_rmk = found_rmk;
    }

    public String getStruc_assem_rmk() {
        return struc_assem_rmk;
    }

    public void setStruc_assem_rmk(String struc_assem_rmk) {
        this.struc_assem_rmk = struc_assem_rmk;
    }

    public String getDrv_mount_rmk() {
        return drv_mount_rmk;
    }

    public void setDrv_mount_rmk(String drv_mount_rmk) {
        this.drv_mount_rmk = drv_mount_rmk;
    }

    public String getLa_earthing_rmk() {
        return la_earthing_rmk;
    }

    public void setLa_earthing_rmk(String la_earthing_rmk) {
        this.la_earthing_rmk = la_earthing_rmk;
    }

    public String getWrk_quality_rmk() {
        return wrk_quality_rmk;
    }

    public void setWrk_quality_rmk(String wrk_quality_rmk) {
        this.wrk_quality_rmk = wrk_quality_rmk;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public String getState_ins_id() {
        return state_ins_id;
    }

    public void setState_ins_id(String state_ins_id) {
        this.state_ins_id = state_ins_id;
    }

    public String getDistrict_ins_id() {
        return district_ins_id;
    }

    public void setDistrict_ins_id(String district_ins_id) {
        this.district_ins_id = district_ins_id;
    }

    public String getAddress_ins() {
        return address_ins;
    }

    public void setAddress_ins(String address_ins) {
        this.address_ins = address_ins;
    }


    public String getRegis_no() {
        return regis_no;
    }

    public String getAud_date() {
        return aud_date;
    }

    public void setAud_date(String aud_date) {
        this.aud_date = aud_date;
    }

    public void setRegis_no(String regis_no) {
        this.regis_no = regis_no;
    }

    public String getFound() {
        return Found;
    }

    public void setFound(String found) {
        Found = found;
    }

    public String getStruc_assem() {
        return struc_assem;
    }

    public void setStruc_assem(String struc_assem) {
        this.struc_assem = struc_assem;
    }

    public String getDrv_mount() {
        return drv_mount;
    }

    public void setDrv_mount(String drv_mount) {
        this.drv_mount = drv_mount;
    }

    public String getLa_earthing() {
        return la_earthing;
    }

    public void setLa_earthing(String la_earthing) {
        this.la_earthing = la_earthing;
    }

    public String getWrk_quality() {
        return wrk_quality;
    }

    public void setWrk_quality(String wrk_quality) {
        this.wrk_quality = wrk_quality;
    }

    public Float getSite_art() {
        return site_art;
    }

    public void setSite_art(Float site_art) {
        this.site_art = site_art;
    }
}




