package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class InstallationBean {


    public String pernr = "";
    public String latitude = "";
    public String longitude = "";
    public String project_no = "";
    public String login_no = "";
    public String inst_bill_no = "";
    public String inst_date = "";
    public String bill_date = "";
    public String delay_reason = "";
    public String rms_data_status = "";
    public String customer_name = "";
    public String fathers_name = "";
    public String mobile_no = "";
    public String state_ins_id = "";
    public String state_ins_txt = "";
    public String district_ins_id = "";
    public String district_ins_txt = "";
    public String tehsil_ins = "";
    public String village_ins = "";
    public String address_ins = "";
    public String make_ins = "";

    public String solarpanel_wattage = "";
    public String solarpanel_stand_ins_quantity = "";
    public String total_watt = "";
    public String inst_hp = "";

    public String no_of_module_qty = "";
    public String no_of_module_value = "";

    public String module_total_plate_watt = "";

    public String solar_motor_model_details = "";
    public String smmd_sno = "";

    public String splar_pump_model_details = "";
    public String spmd_sno = "";

    public String solar_controller_model = "";
    public String scm_sno = "";

    public String simoprator = "";
    public String conntype = "";
    public String simcard_num = "";
    public String regis_no = "";
    public String BeneficiaryNo = "";


    public InstallationBean() {

    }


    public InstallationBean(String pernr_txt, String project_no_text, String login_no_text, String latitude_txt, String longitude_txt, String inst_bill_no_txt, String inst_date_txt,
                            String bill_date_txt, String dely_rea_txt, String rms_data_status_txt, String customer_name_txt, String father_name_txt, String mobile_no_txt,
                            String state_ins_Id, String state_ins_text, String district_ins_Id, String district_ins_text, String tehsil_ins_txt, String village_ins_txt,
                            String address_ins_txt, String make_ins_txt, String solarpanel_wattage_txt, String solarpanel_stand_ins_quantity_txt, String total_watt_txt,
                            String inst_hp_txt, String no_of_module_qty_txt, String no_of_module_value_txt, String module_total_plate_watt_txt, String solar_motor_model_details_txt,
                            String smmd_sno_txt, String splar_pump_model_details_txt, String spmd_sno_txt, String solar_controller_model_txt, String scm_sno_txt, String simoprator_txt,
                            String conntype_txt, String simcard_num_txt, String regis_no_txt, String BeneficiaryNo

    ) {


        pernr = pernr_txt;
        project_no = project_no_text;
        login_no = login_no_text;
        latitude = latitude_txt;
        longitude = longitude_txt;
        inst_bill_no = inst_bill_no_txt;
        inst_date = inst_date_txt;
        bill_date = bill_date_txt;
        delay_reason = dely_rea_txt;
        rms_data_status = rms_data_status_txt;
        customer_name = customer_name_txt;
        fathers_name = father_name_txt;
        mobile_no = mobile_no_txt;
        address_ins = address_ins_txt;
        state_ins_id = state_ins_Id;
        state_ins_txt = state_ins_text;
        district_ins_id = district_ins_Id;
        district_ins_txt = district_ins_text;
        tehsil_ins = tehsil_ins_txt;
        village_ins = village_ins_txt;
        address_ins = address_ins_txt;
        make_ins = make_ins_txt;
        solarpanel_wattage = solarpanel_wattage_txt;
        solarpanel_stand_ins_quantity = solarpanel_stand_ins_quantity_txt;
        total_watt = total_watt_txt;
        inst_hp = inst_hp_txt;
        no_of_module_qty = no_of_module_qty_txt;
        no_of_module_value = no_of_module_value_txt;
        module_total_plate_watt = module_total_plate_watt_txt;
        solar_motor_model_details = solar_motor_model_details_txt;
        smmd_sno = smmd_sno_txt;
        splar_pump_model_details = splar_pump_model_details_txt;
        spmd_sno = spmd_sno_txt;
        solar_controller_model = solar_controller_model_txt;
        scm_sno = scm_sno_txt;
        simoprator = simoprator_txt;
        conntype = conntype_txt;
        simcard_num = simcard_num_txt;
        regis_no = regis_no_txt;
        BeneficiaryNo = BeneficiaryNo;

    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getInst_bill_no() {
        return inst_bill_no;
    }

    public void setInst_bill_no(String inst_bill_no) {
        this.inst_bill_no = inst_bill_no;
    }

    public String getInst_date() {
        return inst_date;
    }

    public void setInst_date(String inst_date) {
        this.inst_date = inst_date;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getDelay_reason() {
        return delay_reason;
    }

    public void setDelay_reason(String delay_reason) {
        this.delay_reason = delay_reason;
    }

    public String getRms_data_status() {
        return rms_data_status;
    }

    public void setRms_data_status(String rms_data_status) {
        this.rms_data_status = rms_data_status;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getFathers_name() {
        return fathers_name;
    }

    public void setFathers_name(String fathers_name) {
        this.fathers_name = fathers_name;
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

    public String getLogin_no() {
        return login_no;
    }

    public void setLogin_no(String login_no) {
        this.login_no = login_no;
    }

    public String getState_ins_id() {
        return state_ins_id;
    }

    public void setState_ins_id(String state_ins_id) {
        this.state_ins_id = state_ins_id;
    }

    public String getState_ins_txt() {
        return state_ins_txt;
    }

    public void setState_ins_txt(String state_ins_txt) {
        this.state_ins_txt = state_ins_txt;
    }

    public String getDistrict_ins_id() {
        return district_ins_id;
    }

    public void setDistrict_ins_id(String district_ins_id) {
        this.district_ins_id = district_ins_id;
    }

    public String getDistrict_ins_txt() {
        return district_ins_txt;
    }

    public void setDistrict_ins_txt(String district_ins_txt) {
        this.district_ins_txt = district_ins_txt;
    }

    public String getMake_ins() {
        return make_ins;
    }

    public void setMake_ins(String make_ins) {
        this.make_ins = make_ins;
    }

    public String getTehsil_ins() {
        return tehsil_ins;
    }

    public void setTehsil_ins(String tehsil_ins) {
        this.tehsil_ins = tehsil_ins;
    }

    public String getVillage_ins() {
        return village_ins;
    }

    public void setVillage_ins(String village_ins) {
        this.village_ins = village_ins;
    }

    public String getAddress_ins() {
        return address_ins;
    }

    public void setAddress_ins(String address_ins) {
        this.address_ins = address_ins;
    }

    public String getSolarpanel_wattage() {
        return solarpanel_wattage;
    }

    public void setSolarpanel_wattage(String solarpanel_wattage) {
        this.solarpanel_wattage = solarpanel_wattage;
    }

    public String getSolarpanel_stand_ins_quantity() {
        return solarpanel_stand_ins_quantity;
    }

    public void setSolarpanel_stand_ins_quantity(String solarpanel_stand_ins_quantity) {
        this.solarpanel_stand_ins_quantity = solarpanel_stand_ins_quantity;
    }

    public String getTotal_watt() {
        return total_watt;
    }

    public void setTotal_watt(String total_watt) {
        this.total_watt = total_watt;
    }

    public String getInst_hp() {
        return inst_hp;
    }

    public void setInst_hp(String inst_hp) {
        this.inst_hp = inst_hp;
    }

    public String getNo_of_module_qty() {
        return no_of_module_qty;
    }

    public void setNo_of_module_qty(String no_of_module_qty) {
        this.no_of_module_qty = no_of_module_qty;
    }

    public String getNo_of_module_value() {
        return no_of_module_value;
    }

    public void setNo_of_module_value(String no_of_module_value) {
        this.no_of_module_value = no_of_module_value;
    }

    public String getModule_total_plate_watt() {
        return module_total_plate_watt;
    }

    public void setModule_total_plate_watt(String module_total_plate_watt) {
        this.module_total_plate_watt = module_total_plate_watt;
    }

    public String getSolar_motor_model_details() {
        return solar_motor_model_details;
    }

    public void setSolar_motor_model_details(String solar_motor_model_details) {
        this.solar_motor_model_details = solar_motor_model_details;
    }

    public String getSmmd_sno() {
        return smmd_sno;
    }

    public void setSmmd_sno(String smmd_sno) {
        this.smmd_sno = smmd_sno;
    }

    public String getSplar_pump_model_details() {
        return splar_pump_model_details;
    }

    public void setSplar_pump_model_details(String splar_pump_model_details) {
        this.splar_pump_model_details = splar_pump_model_details;
    }

    public String getSpmd_sno() {
        return spmd_sno;
    }

    public void setSpmd_sno(String spmd_sno) {
        this.spmd_sno = spmd_sno;
    }

    public String getSolar_controller_model() {
        return solar_controller_model;
    }

    public void setSolar_controller_model(String solar_controller_model) {
        this.solar_controller_model = solar_controller_model;
    }

    public String getScm_sno() {
        return scm_sno;
    }

    public void setScm_sno(String scm_sno) {
        this.scm_sno = scm_sno;
    }

    public String getSimoprator() {
        return simoprator;
    }

    public void setSimoprator(String simoprator) {
        this.simoprator = simoprator;
    }

    public String getConntype() {
        return conntype;
    }

    public void setConntype(String conntype) {
        this.conntype = conntype;
    }

    public String getSimcard_num() {
        return simcard_num;
    }

    public void setSimcard_num(String simcard_num) {
        this.simcard_num = simcard_num;
    }

    public String getRegis_no() {
        return regis_no;
    }

    public void setRegis_no(String regis_no) {
        this.regis_no = regis_no;
    }

    public String getBeneficiaryNo() {
        return BeneficiaryNo;
    }

    public void setBeneficiaryNo(String beneficiaryNo) {
        BeneficiaryNo = beneficiaryNo;
    }
}




