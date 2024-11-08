package database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import bean.AuditSiteBean;
import bean.DeviceMappingModel;
import bean.BeneficiaryRegistrationBean;
import bean.ImageModel;
import bean.InstallationBean;
import bean.InstallationListBean;
import bean.InstallationOfflineBean;
import bean.ItemNameBean;
import bean.KusumCSurveyBean;
import bean.LoginBean;
import bean.ParameterSettingListModel;
import bean.RegistrationBean;
import bean.RejectListBean;
import bean.SimCardBean;
import bean.SiteAuditListBean;
import bean.SubmitOfflineDataInput;
import bean.SurveyBean;
import bean.SurveyListBean;
import bean.unloadingDataBean;
import settingParameter.model.MotorParamListModel;
import utility.CustomUtility;


public class DatabaseHelper extends SQLiteOpenHelper {

    int checkFirstInsert = 0;
    // Database Name
    public static final String DATABASE_NAME = "db_shakti_kusum";
    // Database Version
    public static final int DATABASE_VERSION = 5;
    // Table Names
    public static final String TABLE_LOGIN = "tbl_login";
    public static final String TABLE_LOGIN_SELECTION = "tbl_login_selec";
    public static final String TABLE_DASHBOARD = "tbl_dashboard";
    public static final String TABLE_STATE_DISTRICT = "tbl_state_district";
    public static final String TABLE_REGISTRATION = "tbl_registration";
    public static final String TABLE_INSTALLATION_LIST = "tbl_installation_list";
    public static final String TABLE_INSTALLATION_UNLOAD_LIST = "tbl_installation_unload_list";
    public static final String TABLE_INSTALLATION_OFFLINE_LIST = "tbl_installation_offline_list";
    public static final String TABLE_OFFLINE_SUBMITTED_LIST = "tbl_offline_submitted_list";
    public static final String TABLE_SETTING_PARAMETER_LIST = "tbl_setting_parameter_list";
    public static final String TABLE_PARAMETER_SET_DATA = "tbl_parameter_set";
    public static final String TABLE_AUDITSITE_LIST = "tbl_auditsite_list";
    public static final String TABLE_REJECTION_LIST = "tbl_rejection_list";
    public static final String TABLE_SURVEY_LIST = "tbl_survey_list";
    public static final String TABLE_INSTALLATION_PUMP_DATA = "tbl_installation_pump_data";
    public static final String TABLE_KUSUMCSURVEYFORM = "tbl_kusumcsurvetform";

    public static final String TABLE_INSTALLATION_IMAGE_DATA = "tbl_installation_image_data";
    public static final String TABLE_BENEFICIARY_IMAGE_DATA = "tbl_beneficiary_image_data";
    public static final String TABLE_REJECTED_INSTALLATION_IMAGE_DATA = "tbl_rejectinstallation_image_data";

    public static final String TABLE_UNLOADING_IMAGE_DATA = "tbl_unloading_image_data";
    public static final String TABLE_UNLOADING_FORM_DATA = "tbl_unloading_form_data";
    public static final String TABLE_OFFLINE_CONTROLLER_IMAGE_DATA = "tbl_offline_controller_image_data";
    public static final String TABLE_AUDIT_PUMP_DATA = "tbl_audit_pump_data";
    public static final String TABLE_SURVEY_PUMP_DATA = "tbl_survey_pump_data";
    public static final String TABLE_SIM_REPLACMENT_DATA = "tbl_sim_card_replacement";
    public static final String TABLE_DAMAGE_MISS_COMPLAIN = "tbl_damage_midd_complain";

    public static final String TABLE_SETTING_PENDING_LIST = "tbl_setting_pending_list";

    public static final String TABLE_SITE_AUDIT = "tbl_site_audit";
    public static final String TABLE_KusumCImages = "tbl_kusumCImages";

    public static final String TABLE_DEVICE_MAPPING_DATA = "tbl_device_mapping_data";

    public static final String TABLE_BENEFICIARY_REGISTRATION = "tbl_Beneficiary_Registration";

    public static final String TABLE_PARAMERSLIST_NAME = "ParameterList";

    //TABLE_OFFLINE_SUBMITTED_LIST field name
    public static final String KEY_OFFLINE_BILL_NO = "bill_no";
    public static final String KEY_OFFLINE_BENEFICIARY = "beneficiary";
    public static final String KEY_OFFLINE_CUSTOMER_NAME = "customer_name";
    public static final String KEY_OFFLINE_PROJECT_NO = "project_no";
    public static final String KEY_OFFLINE_USERID = "userid";
    public static final String KEY_OFFLINE_REGISNO = "regisno";
    public static final String KEY_OFFLINE_OFFPHOTO = "offphoto";

    //fields name
    public static final String KEY_ENQ_DOC = "enq_doc";
    public static final String KEY_No_OF_Module = "inst_no_of_module_value";
    public static final String KEY_USERMOB = "person_mob";
    public static final String KEY_USERID = "userid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERTYPE = "usertype";

    public static final String KEY_PROJ_NO = "proj_no";
    public static final String KEY_REGISNO = "regis_no";
    public static final String KEY_PROCESS_NO = "process_no";
    public static final String KEY_PROCESS_NM = "process_nm";
    public static final String KEY_PROJ_NM = "proj_nm";
    public static final String KEY_LOGIN_NO = "login_no";
    public static final String KEY_LOGIN_NM = "login_nm";

    //TABLE_BENEFICIARY_REGISTRATION Fields
    public static final String KEY_SERIAL_ID = "serial_id";
    public static final String KEY_FAMILY_ID = "family_id";
    public static final String KEY_APPLICANT_NAME = "applicant_name";
    public static final String KEY_APPLICANT_FATHER_NAME = "applicant_father_name";
    public static final String KEY_APPLICANT_MOBILE_NO = "applicant_mobile_no";
    public static final String KEY_APPLICANT_VILLAGE = "applicant_village";
    public static final String KEY_APPLICANT_BLOCK = "applicant_block";
    public static final String KEY_APPLICANT_TEHSIL = "applicant_tehsil";
    public static final String KEY_APPLICANT_DISTRICT = "applicant_district";
    public static final String KEY_PUMP_CAPACITY = "pump_capacity";
    public static final String KEY_PUMP_AC_DC = "pump_ac_dc";
    public static final String KEY_CONTROLLER_TYPE = "controller_type";
    public static final String KEY_APPLICANT_ACCOUNT_NO = "applicant_account_no";
    public static final String KEY_APPLICANT_IFSC_CODE = "applicant_ifsc_code";


    public static final String KEY_PROJ_TXT = "project_txt";
    public static final String KEY_LOGIN_TXT = "login_txt";
    public static final String KEY_LOGIN_ID = "login_id";
    public static final String KEY_PROJ_ID = "project_id";

    public static final String KEY_STATE = "state";
    public static final String KEY_STATE_TEXT = "state_text";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_DISTRICT_TEXT = "district_text";
    public static final String KEY_TEHSIL = "tehsil";
    public static final String KEY_TEHSIL_TEXT = "tehsil_text";

    public static final String KEY_PERNR = "pernr";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_CUST_NAME = "custname";
    public static final String KEY_FATH_NAME = "fathname";
    public static final String KEY_VILLAGE = "village";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_CONTACT_NO = "contactno";
    public static final String KEY_AADHAR_NO = "aadharno";
    public static final String KEY_AADHAR_MOBILE = "aadhar_mobile";
    public static final String KEY_BANK_NAME = "bankname";
    public static final String KEY_BANK_ACC_NO = "bankaccno";
    public static final String KEY_ACC_TYPE = "accounttype";
    public static final String KEY_BRANCH_NAME = "branchname";
    public static final String KEY_IFSC_CODE = "ifsccode";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";
    public static final String KEY_INST_DATE = "instdate";
    public static final String KEY_AUD_DATE = "auditdate";
    public static final String KEY_NAME = "name";
    public static final String KEY_FOUND = "foundation";
    public static final String KEY_FOUND_RMK = "foundation_rmk";
    public static final String KEY_STRUCT = "structure";
    public static final String KEY_STRUCT_RMK = "structure_rmk";
    public static final String KEY_DRV_MOUNT = "drivemount";
    public static final String KEY_DRV_MOUNT_RMK = "drivemount_rmk";
    public static final String KEY_LA_EARTH = "laearth";
    public static final String KEY_LA_EARTH_RMK = "laearth_rmk";
    public static final String KEY_WRK_QLTY = "wrkqlty";
    public static final String KEY_WRK_QLTY_RMK = "wrkqlty_rmk";
    public static final String KEY_SITE_RAT = "siterat";
    public static final String KEY_RMS_DATA_STATUS = "rmsdata";
    public static final String KEY_SOLAR_PANEL_WATT = "solarpanelwatt";
    public static final String KEY_SOLAR_PANEL_STAND_INSTALL_QTY = "solarpanelinstallqty";
    public static final String KEY_HP = "hp";
    public static final String KEY_PUMPLoad = "pumpLoad";
    public static final String KEY_TOTAL_WATT = "totalwatt";
    public static final String KEY_PANEL_MODULE_QTY = "panelmoduleqty";
    public static final String KEY_PANEL_MODULE_SER_NO = "panelmoduleserialno";
    public static final String KEY_TOTAL_PLATES_PER_WATT = "totalplateperwatt";
    public static final String KEY_MOTOR_MODAL_DETAILS = "motormodaldetails";
    public static final String KEY_MOTOR_MODAL_SER_NO = "motormodalserialno";
    public static final String KEY_PUMP_MODAL_DETAILS = "pumpmodaldetails";
    public static final String KEY_PUMP_MODAL_SER_NO = "pumpmodalserialno";
    public static final String KEY_CONTROLER_MODAL_DETAILS = "controlermodaldetails";
    public static final String KEY_CONTROLER_MODAL_SER_NO = "controlermodalserialno";
    public static final String KEY_SIM_OPERATOR_TYPE = "simoperatortype";
    public static final String KEY_CONNECTION_TYPE = "connectiontype";
    public static final String KEY_SIM_NO = "simno";
    public static final String KEY_PDF = "pdf";
    public static final String KEY_SYNC = "sync";
    public static final String KEY_SET_MATNO = "set_matno";

    public static final String KEY_MOTOR_MATNO = "motor_matno";
    public static final String KEY_SIMHA2 = "simha2";
    public static final String KEY_CUS_CONTACT_NO = "cus_contact_no";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_COUNTRY_TEXT = "country_text";

    public static final String KEY_CONTROLLER = "controller";
    public static final String KEY_CONTROLLER_MAT_NO = "controller_mat_no";
    public static final String KEY_MOTOR = "motor";
    public static final String KEY_PROJECT_NO = "project_no";
    public static final String KEY_PUMP = "pump";
    public static final String KEY_DISPATCH_DATE = "disp_date";
    public static final String KEY_CUSTOMER_CODE = "customer_code";
    public static final String KEY_CITY_CODE = "city_code";
    public static final String KEY_BILL_NO = "bill_no";
    public static final String KEY_KUNNR = "kunnr";
    public static final String KEY_BEN_NO = "ben_no";
    public static final String KEY_REG_NO = "reg_no";
    public static final String KEY_GST_BILL_NO = "gst_bill_no";
    public static final String KEY_BILL_DATE = "bill_date";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_STREET = "street";

    public static final String KEY_BENEFICIARY_NO = "street";

    public static final String KEY_Vendor = "vendor";

    public static final String TABLE_STATE_SEARCH = "tbl_state_detail";
    public static final String KEY_SIMMOB = "sim_mob";
    public static final String KEY_ADD1 = "add1";
    public static final String KEY_ADD2 = "add2";
    public static final String KEY_ADD3 = "add3";
    public static final String KEY_ADD4 = "add4";
    public static final String KEY_ADD5 = "add5";
    public static final String KEY_ADD6 = "add6";
    public static final String KEY_ADD7 = "add7";
    public static final String KEY_ADD8 = "add8";
    public static final String KEY_ADD9 = "add9";
    public static final String KEY_ADD10 = "add10";
    public static final String KEY_ADD11 = "add11";
    public static final String KEY_ADD12 = "add12";
    public static final String KEY_ADD13 = "add13";
    public static final String KEY_ADD14 = "add14";
    public static final String KEY_ADD15 = "add15";
    public static final String KEY_ADD16 = "add16";
    public static final String KEY_ADD17 = "add17";
    public static final String KEY_ADD18 = "add18";
    public static final String KEY_ADD19 = "add19";
    public static final String KEY_ADD20 = "add20";


    public static final String KEY_PHOTO1 = "photo1";
    public static final String KEY_PHOTO1_2 = "photo1_2";
    public static final String KEY_PHOTO1_3 = "photo1_3";
    public static final String KEY_PHOTO1_4 = "photo1_4";
    public static final String KEY_PHOTO1_5 = "photo1_5";
    public static final String KEY_PHOTO2 = "photo2";

    public static final String KEY_PHOTO3 = "photo3";

    public static final String KEY_PHOTO4 = "photo4";

    public static final String KEY_PHOTO5 = "photo5";

    public static final String KEY_PHOTO6 = "photo6";
    public static final String KEY_PHOTO7 = "photo7";
    public static final String KEY_PHOTO8 = "photo8";
    public static final String KEY_PHOTO9 = "photo9";
    public static final String KEY_PHOTO10 = "photo10";
    public static final String KEY_PHOTO11 = "photo11";
    public static final String KEY_PHOTO12 = "photo12";
    public static final String KEY_PHOTO13 = "photo13";
    public static final String KEY_PHOTO14 = "photo14";
    public static final String KEY_PHOTO15 = "photo15";
    public static final String KEY_PHOTO16 = "photo16";
    public static final String KEY_DELIVERY_PHOTO1 = "delivery_photo_1";


    public static final String KEY_REMARK1 = "remark1";
    public static final String KEY_REMARK2 = "remark2";
    public static final String KEY_REMARK3 = "remark3";
    public static final String KEY_REMARK4 = "remark4";
    public static final String KEY_REMARK5 = "remark5";
    public static final String KEY_REMARK6 = "remark6";
    public static final String KEY_REMARK7 = "remark7";
    public static final String KEY_REMARK8 = "remark8";
    public static final String KEY_REMARK9 = "remark9";
    public static final String KEY_REMARK10 = "remark10";
    public static final String KEY_REMARK11 = "remark11";
    public static final String KEY_REMARK12 = "remark12";
    public static final String KEY_REMARK13 = "remark13";
    public static final String KEY_REMARK14 = "remark14";
    public static final String KEY_REMARK15 = "remark15";

    public static final String KEY_REMARK16 = "remark16";
    public static final String KEY_PMID = "pmId", KEY_PARAMETERS_NAME = "parametersName", KEY_MODBUS_ADDRESS = "modbusaddress", KEY_MOB_BT_ADDRESS = "mobBTAddress", KEY_FACTOR = "factor", KEY_PVALUE = "pValue", KEY_MATERIAL_CODE = "materialCode", KEY_UNIT = "unit", KEY_OFFSET = "app_offset";


    public static final String KEY_INSTALLATION_ID = "installationId", KEY_INSTALLATION_NAME = "installationImageName",
            KEY_INSTALLATION_PATH = "installtionPath", KEY_INSTALLATION_IMAGE_SELECTED = "installtionImageSelected",
            KEY_INSTALLATION_BILL_NO = "InstalltionBillNo", KEY_INSTALLATION_LATITUDE = "InstalltionLatitude",
            KEY_INSTALLATION_LONGITUDE = "InstalltionLongitude", KEY_INSTALLATION_POSITION = "InstalltionPosition";


    public static final String KEY_UNLOADING_ID = "unloadingId", KEY_UNLOADING_NAME = "unloadingImageName", KEY_UNLOADING_PATH = "unloadingPath", KEY_UNLOADING_IMAGE_SELECTED = "unloadingImageSelected", KEY_UNLOADING_BILL_NO = "unloadingBillNo";

    public static final String KEY_MODULE_QTY="unloading_Module_Qty", KEY_MODULE_VALUES="unloading_module_values", KEY_PUMP_SERIAL_NO="unloading_Pump_Serial_no", KEY_MOTOR_SERIAL_NO="unloading_Motor_Serial_no", KEY_CONTROLLER_SERIAL_NO="unloading_Controlling_Serial_no",KEY_MATERIAL_STATUS="unloading_Material_status", KEY_UNLOADING_REAMRK="unloading_Remark";
    public static final String KEY_PROJEDCT_NO = "project_no";
    public static final String KEY_USER_ID_ = "userid";
    public static final String KEY_PROJECT_LOGIN_NO = "project_login_no";
    public static final String KEY_FARMER_CONTACT_NO = "FARMER_CONTACT_NO";
    public static final String KEY_APPLICANT_NO = "APPLICANT_NO";
    public static final String KEY_REGIS_NO = "REGISNO";
    public static final String KEY_BENEFICIARY = "BENEFICIARY";
    public static final String KEY_SITE_ADRC = "SITE_ADRC";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "long";
    public static final String KEY_CATEGORY = "CATEGORY";
    public static final String KEY_WATER_SOURCE = "WATER_SOURCE";
    public static final String KEY_INTERNET_TYPE = "INTERNET_TYPE";
    public static final String KEY_CROP_PATTERN = "CROP_PATTERN";
    public static final String KEY_TYPE_OF_IRIGATN = "TYPE_OF_IRIGATN";
    public static final String KEY_SHADOW_FREE_LAND = "SHADOW_FREE_LAND";
    public static final String KEY_ELEC_CON = "ELEC_CON";
    public static final String KEY_ELEC_IDEN_NO = "ELEC_IDEN_NO";
    public static final String KEY_ELEC_RATING = "ELEC_RATING";

    public static final String KEY_PUMP_TYPE = "PUMP_TYPE";
    public static final String KEY_PUMP_SET_RATING = "PUMP_SET_RATING";
    public static final String KEY_PUMP_MAKE = "PUMP_MAKE";
    public static final String KEY_CABLE_DETAIL_WITH_MAKE = "CABLE_DETAIL_WITH_MAKE";
    public static final String KEY_POWER_IN_VOLT = "powerInVolt";
    public static final String KEY_EXDISCHARGE = "exDischarge";
    public static final String KEY_EXDYNAMIC = "exDynamic";
    public static final String KEY_PHASE_VOL_V1 = "PHASE_VOL_V1";
    public static final String KEY_PHASE_VOL_V2 = "PHASE_VOL_V2";
    public static final String KEY_PHASE_VOL_V3 = "PHASE_VOL_V3";
    public static final String KEY_LINE_VOL_V1 = "LINE_VOL_V1";
    public static final String KEY_LINE_VOL_V2 = "LINE_VOL_V2";
    public static final String KEY_LINE_VOL_V3 = "LINE_VOL_V3";
    public static final String KEY_LINE_CRNT_AMP1 = "LINE_CRNT_AMP1";
    public static final String KEY_LINE_CRNT_AMP2 = "LINE_CRNT_AMP2";
    public static final String KEY_LINE_CRNT_AMP3 = "LINE_CRNT_AMP3";
    public static final String KEY_FREQ_HERTZ = "FREQ_HERTZ";
    public static final String KEY_LINE_POWFACT_1 = "LINE_POWFACT_1";
    public static final String KEY_LINE_POWFACT_2 = "LINE_POWFACT_2";
    public static final String KEY_LINE_POWFACT_3 = "LINE_POWFACT_3";
    public static final String KEY_BOREWELL_SIZE = "BOREWELL_SIZE";
    public static final String KEY_BOREWELL_DEPTH = "BOREWELL_DEPTH";
    public static final String KEY_PUMP_SET_DEPTH = "PUMP_SET_DEPTH";
    public static final String KEY_DIS_PUMP_LPM = "DIS_PUMP_LPM";
    public static final String KEY_DEL_PUMP_LPM = "DEL_PUMP_LPM";
    public static final String KEY_DEL_PIPE_LINE = "DEL_PIPE_LINE";
    public static final String KEY_TOTAL_DYNAMIC_HEAD = "TOTAL_DYNAMIC_HEAD";
    public static final String KEY_TRANSFORMER_RATING = "TRANSFORMER_RATING";
    public static final String KEY_SERVICE_LINE = "SERVICE_LINE";
    public static final String KEY_THREE_PHASE = "THREE_PHASE";
    public static final String KEY_ELECTRICITY_BILL = "ELECTRICITY_BILL";
    public static final String KEY_NEUTRAL_AVAILABILITY = "NEUTRAL_AVAILABILITY";

    public static final String KEY_STURCTURE_WATER_SOURCE = "STURCTURE_WATER_SOURCE";
    public static final String KEY_FEEDER_TO_FARMER = "FEEDER_TO_FARMER";
    public static final String KEY_ADDITIONAL_INFO = "ADDITIONAL_INFO";

    public static final String KEY_DISTANCE = "DISTANCE";


    //Sim card Installation field name

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_SIM_CARD_REP_DATE = "sim_card_rep_date";
    public static final String KEY_SIM_CARD_LAT = "sim_card_lat";
    public static final String KEY_SIM_CARD_LNG = "sim_card_lng";
    public static final String KEY_DEVICE_NO = "device_no";
    public static final String KEY_CUST_MOBILE = "cust_no";
    public static final String KEY_CUST_ADDRESS = "cust_address";
    public static final String KEY_SIM_OLD_NO = "sim_old_no";
    public static final String KEY_SIM_NEW_NO = "sim_new_no";
    public static final String KEY_SIM_OLD_PHOTO = "sim_old_photo";
    public static final String KEY_SIM_NEW_PHOTO = "sim_new_photo";
    public static final String KEY_DRIVE_PHOTO = "drive_photo";


    //Survey data
    public static final String KEY_WATER_RES = "water_res";
    public static final String KEY_BORWELL_SIZE = "borwell_size";
    public static final String KEY_BORWELL_DEPTH = "borwell_depth";
    public static final String KEY_CBL_LEN = "cbl_len";
    public static final String KEY_SURF_HEAD = "surf_head";
    public static final String KEY_LEN_DIA_PIP = "len_dia_pip";

    public static final String KEY_DAMAGE_MISS1 = "key_damage_miss1";
    public static final String KEY_DAMAGE_MISS2 = "key_damage_miss2";
    public static final String KEY_DAMAGE_MISS3 = "key_damage_miss3";
    public static final String KEY_DAMAGE_MISS4 = "key_damage_miss4";
    public static final String KEY_DAMAGE_MISS5 = "key_damage_miss5";

    public static final String KEY_RADIO1 = "key_radio1";
    public static final String KEY_RADIO2 = "key_radio2";
    public static final String KEY_RADIO3 = "key_radio3";
    public static final String KEY_RADIO4 = "key_radio4";
    public static final String KEY_RADIO5 = "key_radio5";

    public static final String KEY_QUNTITY1 = "key_quntity1";
    public static final String KEY_QUNTITY2 = "key_quntity2";
    public static final String KEY_QUNTITY3 = "key_quntity3";
    public static final String KEY_QUNTITY4 = "key_quntity4";
    public static final String KEY_QUNTITY5 = "key_quntity5";

    public static final String KEY_REMARKD1 = "key_remark1";
    public static final String KEY_REMARKD2 = "key_remark2";
    public static final String KEY_REMARKD3 = "key_remark3";
    public static final String KEY_REMARKD4 = "key_remark4";
    public static final String KEY_REMARKD5 = "key_remark5";
    public static final String KEY_SITE_AUDIT_ID = "siteAuditId", KEY_SITE_AUDIT_NAME = "siteAuditImageName", KEY_SITE_AUDIT_PATH = "siteAuditPath", KEY_SITE_AUDIT_IMAGE_SELECTED = "siteAuditImageSelected", KEY_SITE_AUDIT_BILL_NO = "siteAuditBillNo";
    public static final String KEY_KUSUMC_ID = "Id", KEY_KUSUMC_NAME = "ImageName", KEY_KUSUMC_PATH = "Path", KEY_KUSUMC_IMAGE_SELECTED = "ImageSelected", KEY_KUSUMC_BILL_NO = "BillNo";


    public static final String COLUMN_pmID = "PmId";
    public static final String COLUMN_ParametersName = "ParametersName";
    public static final String COLUMN_ModBusAddress = "ModBusAddress";
    public static final String COLUMN_MobBTAddress = "MobBTAddress";
    public static final String COLUMN_factor = "Factor";
    public static final String COLUMN_pValue = "PValue";
    public static final String COLUMN_MaterialCode = "MaterialCode";
    public static final String COLUMN_Unit = "Unit";
    public static final String COLUMN_offset = "Offsets";

    public static final String KEY_PARAMETER_ID = "parameter_id";

    public static final String KEY_PARAMETER_SET = "parameter_set";


// Table Create Statements

    private static final String CREATE_TABLE_DAMAGE_MISS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DAMAGE_MISS_COMPLAIN + "("
            + KEY_BILL_NO + " TEXT,"
            + KEY_DAMAGE_MISS1 + " TEXT,"
            + KEY_DAMAGE_MISS2 + " TEXT,"
            + KEY_DAMAGE_MISS3 + " TEXT,"
            + KEY_DAMAGE_MISS4 + " TEXT,"
            + KEY_DAMAGE_MISS5 + " TEXT,"
            + KEY_RADIO1 + " TEXT,"
            + KEY_RADIO2 + " TEXT,"
            + KEY_RADIO3 + " TEXT,"
            + KEY_RADIO4 + " TEXT,"
            + KEY_RADIO5 + " TEXT,"
            + KEY_QUNTITY1 + " TEXT,"
            + KEY_QUNTITY2 + " TEXT,"
            + KEY_QUNTITY3 + " TEXT,"
            + KEY_QUNTITY4 + " TEXT,"
            + KEY_QUNTITY5 + " TEXT,"
            + KEY_REMARKD1 + " TEXT,"
            + KEY_REMARKD2 + " TEXT,"
            + KEY_REMARKD3 + " TEXT,"
            + KEY_REMARKD4 + " TEXT,"
            + KEY_REMARKD5 + " TEXT,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO1_2 + " BLOB,"
            + KEY_PHOTO1_3 + " BLOB,"
            + KEY_PHOTO1_4 + " BLOB,"
            + KEY_PHOTO1_5 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_PHOTO3 + " BLOB,"
            + KEY_PHOTO4 + " BLOB,"
            + KEY_PHOTO5 + " BLOB)";


    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN + "("
            + KEY_USERID + " PRIMARY KEY ,"
            + KEY_USERNAME + " TEXT,"
            + KEY_USERTYPE + " TEXT,"
            + KEY_USERMOB + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_LOGIN_SELEC = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN_SELECTION + "("
            + KEY_PROJ_NO + " TEXT ,"
            + KEY_PROJ_NM + " TEXT,"
            + KEY_LOGIN_NO + " TEXT,"
            + KEY_LOGIN_NM + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_DASHBOARD = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DASHBOARD + "("
            + KEY_PROJ_NO + " TEXT ,"
            + KEY_PROCESS_NO + " TEXT ,"
            + KEY_PROCESS_NM + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_STATE_DISTRICT = "CREATE TABLE "
            + TABLE_STATE_DISTRICT + "("
            + KEY_STATE + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_TEHSIL + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_REGISTRATION = "CREATE TABLE "
            + TABLE_REGISTRATION + "("
            + KEY_ENQ_DOC + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_PROJ_NO + " TEXT,"
            + KEY_LOGIN_NO + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_FATH_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT,"
            + KEY_VILLAGE + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_AADHAR_NO + " TEXT,"
            + KEY_BANK_NAME + " TEXT,"
            + KEY_BANK_ACC_NO + " TEXT,"
            + KEY_ACC_TYPE + " TEXT,"
            + KEY_BRANCH_NAME + " TEXT,"
            + KEY_IFSC_CODE + " TEXT,"
            + KEY_AMOUNT + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_PDF + " BLOB,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_PHOTO3 + " BLOB,"
            + KEY_PHOTO4 + " BLOB,"
            + KEY_PHOTO5 + " BLOB,"
            + KEY_PHOTO6 + " BLOB,"
            + KEY_PHOTO7 + " BLOB,"
            + KEY_PHOTO8 + " BLOB,"
            + KEY_PHOTO9 + " BLOB,"
            + KEY_PHOTO10 + " BLOB,"
            + KEY_PHOTO11 + " BLOB,"
            + KEY_PHOTO12 + " BLOB,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_INSTALLATION_PUMP = "CREATE TABLE "
            + TABLE_INSTALLATION_PUMP_DATA + "(" + KEY_BILL_NO + " TEXT," + KEY_BILL_DATE + " TEXT," + KEY_PERNR + " TEXT,"
            + KEY_PROJ_NO + " TEXT," + KEY_LOGIN_NO + " TEXT," + KEY_INST_DATE + " TEXT," + KEY_RMS_DATA_STATUS + " TEXT,"
            + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_CUST_NAME + " TEXT," + KEY_FATH_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT," + KEY_STATE + " TEXT," + KEY_DISTRICT_TEXT + " TEXT," + KEY_DISTRICT + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT," + KEY_VILLAGE + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_CONTACT_NO + " TEXT,"
            + KEY_SOLAR_PANEL_WATT + " TEXT," + KEY_SOLAR_PANEL_STAND_INSTALL_QTY + " TEXT," + KEY_HP + " TEXT," + KEY_TOTAL_WATT + " TEXT,"
            + KEY_PANEL_MODULE_QTY + " TEXT," + KEY_PANEL_MODULE_SER_NO + " TEXT," + KEY_TOTAL_PLATES_PER_WATT + " TEXT,"
            + KEY_MOTOR_MODAL_DETAILS + " TEXT," + KEY_MOTOR_MODAL_SER_NO + " TEXT," + KEY_PUMP_MODAL_DETAILS + " TEXT,"
            + KEY_PUMP_MODAL_SER_NO + " TEXT," + KEY_CONTROLER_MODAL_DETAILS + " TEXT,"
            + KEY_CONTROLER_MODAL_SER_NO + " TEXT," + KEY_SIM_OPERATOR_TYPE + " TEXT," + KEY_CONNECTION_TYPE + " TEXT,"
            + KEY_SIM_NO + " TEXT," + KEY_SYNC + " TEXT," + KEY_PDF + " BLOB," + KEY_PHOTO1 + " BLOB," + KEY_PHOTO2 + " BLOB,"
            + KEY_PHOTO3 + " BLOB," + KEY_PHOTO4 + " BLOB," + KEY_PHOTO5 + " BLOB," + KEY_PHOTO6 + " BLOB," + KEY_PHOTO7 + " BLOB,"
            + KEY_PHOTO8 + " BLOB," + KEY_PHOTO9 + " BLOB," + KEY_PHOTO10 + " BLOB," + KEY_PHOTO11 + " BLOB," + KEY_PHOTO12 + " BLOB,"
            + KEY_ADD1 + " TEXT," + KEY_ADD2 + " TEXT," + KEY_ADD3 + " TEXT," + KEY_ADD4 + " TEXT," + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT," + KEY_ADD7 + " TEXT," + KEY_ADD8 + " TEXT," + KEY_ADD9 + " TEXT," + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT," + KEY_ADD12 + " TEXT," + KEY_ADD13 + " TEXT," + KEY_ADD14 + " TEXT," + KEY_ADD15 + " TEXT," + KEY_ADD16 + " TEXT," + KEY_BENEFICIARY_NO + " TEXT," + KEY_AADHAR_NO + " TEXT," + KEY_PUMPLoad + " TEXT)";


    private static final String CREATE_TABLE_KUSUMCSURVEYFORM = "CREATE TABLE "
            + TABLE_KUSUMCSURVEYFORM + "(" +
            KEY_PROJECT_NO + " TEXT," +
            KEY_USER_ID_ + " TEXT," +
            KEY_PROJECT_LOGIN_NO + " TEXT," +
            KEY_FARMER_CONTACT_NO + " TEXT," +
            KEY_APPLICANT_NO + " TEXT," +
            KEY_REGISNO + " TEXT," + KEY_BENEFICIARY + " TEXT," +
            KEY_SITE_ADRC + " TEXT," + KEY_LAT + " TEXT," + KEY_LNG + " TEXT," +
            KEY_CATEGORY + " TEXT," + KEY_WATER_SOURCE + " TEXT," +
            KEY_INTERNET_TYPE + " TEXT," + KEY_CROP_PATTERN + " TEXT," + KEY_TYPE_OF_IRIGATN + " TEXT,"
            + KEY_SHADOW_FREE_LAND + " TEXT," + KEY_ELEC_CON + " TEXT," + KEY_ELEC_IDEN_NO + " TEXT," + KEY_ELEC_RATING + " TEXT,"+ KEY_PUMP_TYPE + " TEXT,"
            + KEY_PUMP_SET_RATING + " TEXT," + KEY_PUMP_MAKE + " TEXT," +KEY_CABLE_DETAIL_WITH_MAKE + " TEXT,"+  KEY_PHASE_VOL_V1 + " TEXT," + KEY_PHASE_VOL_V2 + " TEXT,"
            + KEY_PHASE_VOL_V3 + " TEXT," + KEY_LINE_VOL_V1 + " TEXT," + KEY_LINE_VOL_V2 + " TEXT,"
            + KEY_LINE_VOL_V3 + " TEXT," + KEY_LINE_CRNT_AMP1 + " TEXT," + KEY_LINE_CRNT_AMP2 + " TEXT,"
            + KEY_LINE_CRNT_AMP3 + " TEXT," + KEY_FREQ_HERTZ + " TEXT,"
            + KEY_LINE_POWFACT_1 + " TEXT," + KEY_LINE_POWFACT_2 + " TEXT," + KEY_LINE_POWFACT_3 + " TEXT,"
            + KEY_BOREWELL_SIZE + " TEXT," + KEY_BOREWELL_DEPTH + " TEXT," + KEY_PUMP_SET_DEPTH + " TEXT," + KEY_DIS_PUMP_LPM + " TEXT," +
            KEY_DEL_PUMP_LPM + " TEXT," + KEY_DEL_PIPE_LINE + " TEXT," + KEY_TOTAL_DYNAMIC_HEAD + " TEXT," +KEY_TRANSFORMER_RATING + " TEXT,"
            +KEY_SERVICE_LINE + " TEXT,"+KEY_THREE_PHASE + " TEXT,"+KEY_ELECTRICITY_BILL + " TEXT,"+KEY_NEUTRAL_AVAILABILITY + " TEXT,"
            +KEY_STURCTURE_WATER_SOURCE + " TEXT," +KEY_FEEDER_TO_FARMER + " TEXT," +KEY_ADDITIONAL_INFO + " TEXT,"
            + KEY_EXDISCHARGE + " TEXT,"+ KEY_POWER_IN_VOLT + " TEXT,"+  KEY_EXDYNAMIC + " TEXT,"+  KEY_AADHAR_NO + " TEXT,"+  KEY_AADHAR_MOBILE + " TEXT,"
            + KEY_PHOTO1 + " BLOB," + KEY_PHOTO2 + " BLOB," + KEY_PHOTO3 + " BLOB," + KEY_PHOTO4 + " BLOB," + KEY_PHOTO5 + " BLOB," + KEY_PHOTO6 + " BLOB," + KEY_DISTANCE + " TEXT)";


    private static final String CREATE_TABLE_INSTALLATION_IMAGES = "CREATE TABLE "
            + TABLE_INSTALLATION_IMAGE_DATA + "(" + KEY_INSTALLATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_INSTALLATION_NAME + " TEXT,"
            + KEY_INSTALLATION_PATH + " TEXT,"
            + KEY_INSTALLATION_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_INSTALLATION_BILL_NO + " TEXT,"
            + KEY_INSTALLATION_LATITUDE + " TEXT,"
            + KEY_INSTALLATION_LONGITUDE + " TEXT,"
            + KEY_INSTALLATION_POSITION + " TEXT)";

    private static final String CREATE_TABLE_BENEFICIARY_IMAGES = "CREATE TABLE "
            + TABLE_BENEFICIARY_IMAGE_DATA + "(" + KEY_INSTALLATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_INSTALLATION_NAME + " TEXT,"
            + KEY_INSTALLATION_PATH + " TEXT,"
            + KEY_INSTALLATION_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_INSTALLATION_BILL_NO + " TEXT,"
            + KEY_INSTALLATION_LATITUDE + " TEXT,"
            + KEY_INSTALLATION_LONGITUDE + " TEXT,"
            + KEY_INSTALLATION_POSITION + " TEXT)";

    private static final String CREATE_TABLE_REJECTED_INSTALLATION_IMAGES = "CREATE TABLE "
            + TABLE_REJECTED_INSTALLATION_IMAGE_DATA + "(" + KEY_INSTALLATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_INSTALLATION_NAME + " TEXT,"
            + KEY_INSTALLATION_PATH + " TEXT,"
            + KEY_INSTALLATION_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_INSTALLATION_BILL_NO + " TEXT,"
            + KEY_INSTALLATION_LATITUDE + " TEXT,"
            + KEY_INSTALLATION_LONGITUDE + " TEXT,"
            + KEY_INSTALLATION_POSITION + " TEXT)";


    private static final String CREATE_TABLE_SITE_AUDIT_IMAGES = "CREATE TABLE "
            + TABLE_SITE_AUDIT + "(" + KEY_SITE_AUDIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_SITE_AUDIT_NAME + " TEXT," + KEY_SITE_AUDIT_PATH + " TEXT," + KEY_SITE_AUDIT_IMAGE_SELECTED + " BOOLEAN," + KEY_SITE_AUDIT_BILL_NO + " TEXT)";

    private static final String CREATE_TABLE_KusumCImages = "CREATE TABLE "
            + TABLE_KusumCImages + "(" + KEY_KUSUMC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_KUSUMC_NAME + " TEXT," + KEY_KUSUMC_PATH + " TEXT," + KEY_KUSUMC_IMAGE_SELECTED + " BOOLEAN," + KEY_KUSUMC_BILL_NO + " TEXT)";

    private static final String CREATE_TABLE_UNLOADING_IMAGES = "CREATE TABLE "
            + TABLE_UNLOADING_IMAGE_DATA + "(" + KEY_UNLOADING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_UNLOADING_NAME + " TEXT," + KEY_UNLOADING_PATH + " TEXT," + KEY_UNLOADING_IMAGE_SELECTED + " TEXT," + KEY_UNLOADING_BILL_NO + " TEXT)";

    private static final String CREATE_TABLE_UNLOADING_FORM_DATA = "CREATE TABLE "
            + TABLE_UNLOADING_FORM_DATA + "(" + KEY_UNLOADING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +KEY_BILL_NO+ " TEXT, " + KEY_MODULE_QTY+ " TEXT, " +KEY_MODULE_VALUES+" TEXT, "+KEY_PUMP_SERIAL_NO+" TEXT, "+KEY_MOTOR_SERIAL_NO+" TEXT, "+KEY_CONTROLLER_SERIAL_NO+" TEXT, "+KEY_MATERIAL_STATUS+" TEXT, "+KEY_UNLOADING_REAMRK+" TEXT)";

    private static final String CREATE_TABLE_OFFLINE_CONTROLLER_IMAGE = "CREATE TABLE "
            + TABLE_OFFLINE_CONTROLLER_IMAGE_DATA + "(" + KEY_UNLOADING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_UNLOADING_NAME + " TEXT," + KEY_UNLOADING_PATH + " TEXT," + KEY_UNLOADING_IMAGE_SELECTED + " TEXT," + KEY_UNLOADING_BILL_NO + " TEXT)";

    private static final String CREATE_TABLE_AUDIT_PUMP = "CREATE TABLE "
            + TABLE_AUDIT_PUMP_DATA + "("
            + KEY_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_PROJ_NO + " TEXT,"
            + KEY_REGISNO + " TEXT,"
            + KEY_AUD_DATE + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_FOUND + " TEXT,"
            + KEY_FOUND_RMK + " TEXT,"
            + KEY_STRUCT + " TEXT,"
            + KEY_STRUCT_RMK + " TEXT,"
            + KEY_DRV_MOUNT + " TEXT,"
            + KEY_DRV_MOUNT_RMK + " TEXT,"
            + KEY_LA_EARTH + " TEXT,"
            + KEY_LA_EARTH_RMK + " TEXT,"
            + KEY_WRK_QLTY + " TEXT,"
            + KEY_WRK_QLTY_RMK + " TEXT,"
            + KEY_SITE_RAT + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_PHOTO3 + " BLOB,"
            + KEY_PHOTO4 + " BLOB,"
            + KEY_PHOTO5 + " BLOB,"
            + KEY_PHOTO6 + " BLOB,"
            + KEY_PHOTO7 + " BLOB,"
            + KEY_PHOTO8 + " BLOB,"
            + KEY_PHOTO9 + " BLOB,"
            + KEY_PHOTO10 + " BLOB,"
            + KEY_PHOTO11 + " BLOB,"
            + KEY_PHOTO12 + " BLOB,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_SURVEY_DATA = "CREATE TABLE "
            + TABLE_SURVEY_PUMP_DATA + "("
            + KEY_PERNR + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_PROJ_NO + " TEXT,"
            + KEY_LOGIN_NO + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_BORWELL_SIZE + " TEXT,"
            + KEY_BORWELL_DEPTH + " TEXT,"
            + KEY_CBL_LEN + " TEXT,"
            + KEY_SURF_HEAD + " TEXT,"
            + KEY_LEN_DIA_PIP + " TEXT,"
            + KEY_WATER_RES + " TEXT,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_PHOTO3 + " BLOB,"
            + KEY_PHOTO4 + " BLOB,"
            + KEY_PHOTO5 + " BLOB,"
            + KEY_PHOTO6 + " BLOB,"
            + KEY_PHOTO7 + " BLOB,"
            + KEY_PHOTO8 + " BLOB,"
            + KEY_PHOTO9 + " BLOB,"
            + KEY_PHOTO10 + " BLOB,"
            + KEY_PHOTO11 + " BLOB,"
            + KEY_PHOTO12 + " BLOB,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_STATE_SEARCH = "CREATE TABLE "
            + TABLE_STATE_SEARCH + "("
            + KEY_COUNTRY + " TEXT,"
            + KEY_COUNTRY_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_TEHSIL + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT)";

    private static final String CREATE_TABLE_INSTALLATION_LIST = "CREATE TABLE "
            + TABLE_INSTALLATION_LIST + "("
            + KEY_ENQ_DOC + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_GST_BILL_NO + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_KUNNR + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_AADHAR_NO + " TEXT,"
            + KEY_AADHAR_MOBILE + " TEXT,"
            + KEY_FATH_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT,"
            + KEY_VILLAGE + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_CONTROLLER + " TEXT,"
            + KEY_MOTOR + " TEXT,"
            + KEY_PUMP + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_SET_MATNO + " TEXT,"
            + KEY_SIMHA2 + " TEXT,"
            + KEY_CUS_CONTACT_NO + " TEXT,"
            + KEY_PANEL_MODULE_SER_NO + " TEXT,"
            + KEY_HP + " TEXT,"
            + KEY_PUMPLoad + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_INSTALLATION_UNLOAD_LIST = "CREATE TABLE "
            + TABLE_INSTALLATION_UNLOAD_LIST + "("
            + KEY_ENQ_DOC + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_GST_BILL_NO + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_KUNNR + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_FATH_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT,"
            + KEY_VILLAGE + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_CONTROLLER + " TEXT,"
            + KEY_MOTOR + " TEXT,"
            + KEY_PUMP + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_SET_MATNO + " TEXT,"
            + KEY_SIMHA2 + " TEXT,"
            + KEY_CUS_CONTACT_NO + " TEXT,"
            + KEY_PANEL_MODULE_SER_NO + " TEXT,"
            + KEY_HP + " TEXT,"
            + KEY_PUMP_SERIAL_NO + " TEXT,"
            + KEY_PUMPLoad + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";


    private static final String CREATE_TABLE_INSTALLATION_OFFLINE_LIST = "CREATE TABLE "
            + TABLE_INSTALLATION_OFFLINE_LIST + "("
            + KEY_BILL_NO + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_GST_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_DISPATCH_DATE + " TEXT,"
            + KEY_CUSTOMER_CODE + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_MOBILE + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_CITY_CODE + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT,"
            + KEY_STREET + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_PUMP + " TEXT,"
            + KEY_CONTROLLER + " TEXT,"
            + KEY_CONTROLLER_MAT_NO + " TEXT,"
            + KEY_SET_MATNO + " TEXT,"
            + KEY_MOTOR + " TEXT,"
            + KEY_SIMHA2 + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_SIMMOB + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_PROJECT_NO + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_OFFLINE_SUBMITTED_LIST = "CREATE TABLE "
            + TABLE_OFFLINE_SUBMITTED_LIST + "("
            + KEY_OFFLINE_BILL_NO + " TEXT,"
            + KEY_OFFLINE_BENEFICIARY + " TEXT,"
            + KEY_OFFLINE_CUSTOMER_NAME + " TEXT,"
            + KEY_OFFLINE_PROJECT_NO + " TEXT,"
            + KEY_OFFLINE_USERID + " TEXT,"
            + KEY_OFFLINE_REGISNO + " TEXT,"
            + KEY_OFFLINE_OFFPHOTO + " BLOB)";


    private static final String CREATE_TABLE_AUDITSITE_LIST = "CREATE TABLE "
            + TABLE_AUDITSITE_LIST + "("
            + KEY_ENQ_DOC + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_GST_BILL_NO + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_FATH_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_Vendor + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_REJECTION_LIST = "CREATE TABLE "
            + TABLE_REJECTION_LIST + "("
            + KEY_BILL_NO + " TEXT,"
            + KEY_BEN_NO + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_REG_NO + " TEXT,"
            + KEY_REMARK1 + " TEXT,"
            + KEY_REMARK2 + " TEXT,"
            + KEY_REMARK3 + " TEXT,"
            + KEY_REMARK4 + " TEXT,"
            + KEY_REMARK5 + " TEXT,"
            + KEY_REMARK6 + " TEXT,"
            + KEY_REMARK7 + " TEXT,"
            + KEY_REMARK8 + " TEXT,"
            + KEY_REMARK9 + " TEXT,"
            + KEY_REMARK10 + " TEXT,"
            + KEY_REMARK11 + " TEXT,"
            + KEY_REMARK12 + " TEXT,"
            + KEY_REMARK13 + " TEXT,"
            + KEY_REMARK14 + " TEXT,"
            + KEY_REMARK15 + " TEXT,"
            + KEY_REMARK16 + " TEXT,"
            + KEY_PHOTO1 + " TEXT,"
            + KEY_PHOTO2 + " TEXT,"
            + KEY_PHOTO3 + " TEXT,"
            + KEY_PHOTO4 + " TEXT,"
            + KEY_PHOTO5 + " TEXT,"
            + KEY_PHOTO6 + " TEXT,"
            + KEY_PHOTO7 + " TEXT,"
            + KEY_PHOTO8 + " TEXT,"
            + KEY_PHOTO9 + " TEXT,"
            + KEY_PHOTO10 + " TEXT,"
            + KEY_PHOTO11 + " TEXT,"
            + KEY_PHOTO12 + " TEXT,"
            + KEY_PHOTO13 + " TEXT,"
            + KEY_PHOTO14 + " TEXT,"
            + KEY_PHOTO15 + " TEXT,"
            + KEY_PHOTO16 + " TEXT)";

    private static final String CREATE_TABLE_SURVEY_LIST = "CREATE TABLE "
            + TABLE_SURVEY_LIST + "("
            + KEY_ENQ_DOC + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_GST_BILL_NO + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_FATH_NAME + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT,"
            + KEY_VILLAGE + " TEXT,"
            + KEY_CONTACT_NO + " TEXT,"
            + KEY_CONTROLLER + " TEXT,"
            + KEY_MOTOR + " TEXT,"
            + KEY_PUMP + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT)";

    private static final String CREATE_SIM_CARD_REPLACEMENT = "CREATE TABLE " + TABLE_SIM_REPLACMENT_DATA + "("
            + KEY_ENQ_DOC + " TEXT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_USER_TYPE + " TEXT,"
            + KEY_SIM_CARD_REP_DATE + " TEXT,"
            + KEY_SIM_CARD_LAT + " TEXT,"
            + KEY_SIM_CARD_LNG + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_CUST_MOBILE + " TEXT,"
            + KEY_CUST_ADDRESS + " TEXT,"
            + KEY_DEVICE_NO + " TEXT,"
            + KEY_SIM_NEW_NO + " TEXT,"
            + KEY_SIM_OLD_NO + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT,"
            + KEY_ADD17 + " TEXT,"
            + KEY_ADD18 + " TEXT,"
            + KEY_ADD19 + " TEXT,"
            + KEY_ADD20 + " TEXT,"
            + KEY_SIM_NEW_PHOTO + " BLOB,"
            + KEY_DRIVE_PHOTO + " BLOB,"
            + KEY_SIM_OLD_PHOTO + " BLOB)";


    public static final String KEY_DEVICE_MAPPING_ID = "unloadingId",KEY_DEVICE_MAPPING_READ ="read",KEY_DEVICE_MAPPING_WRITE ="write",
            KEY_DEVICE_MAPPING_UPDATE ="updates",KEY_DEVICE_MAPPING_4GUPDATE ="fourg_Updates";
    private static final String CREATE_TABLE_DEVICE_MAPPING_DATA = "CREATE TABLE "
            + TABLE_DEVICE_MAPPING_DATA + "(" + KEY_DEVICE_MAPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_DEVICE_MAPPING_READ + " TEXT," + KEY_DEVICE_MAPPING_WRITE + " TEXT,"  + KEY_DEVICE_MAPPING_UPDATE + " TEXT,"  +KEY_DEVICE_MAPPING_4GUPDATE + " TEXT,"  + KEY_BILL_NO + " TEXT)";



    private static final String CREATE_BENEFICIARY_REGISTRAION = "CREATE TABLE " + TABLE_BENEFICIARY_REGISTRATION + "("
            + KEY_SERIAL_ID + " TEXT,"
            + KEY_FAMILY_ID + " TEXT,"
            + KEY_APPLICANT_NAME + " TEXT,"
            + KEY_APPLICANT_FATHER_NAME + " TEXT,"
            + KEY_APPLICANT_MOBILE_NO + " TEXT,"
            + KEY_APPLICANT_VILLAGE + " TEXT,"
            + KEY_APPLICANT_BLOCK + " TEXT,"
            + KEY_APPLICANT_TEHSIL + " TEXT,"
            + KEY_APPLICANT_DISTRICT + " TEXT,"
            + KEY_PUMP_CAPACITY + " TEXT,"
            + KEY_PUMP_AC_DC + " TEXT,"
            + KEY_PUMP_TYPE + " TEXT,"
            + KEY_CONTROLLER_TYPE + " TEXT,"
            + KEY_APPLICANT_ACCOUNT_NO + " TEXT,"
            + KEY_APPLICANT_IFSC_CODE + " TEXT,"
            + KEY_AADHAR_NO + " TEXT)";


    private static final String CREATE_TABLE_SETTING_PENDING_LIST  = " CREATE TABLE " + TABLE_SETTING_PENDING_LIST + " ( "
            + KEY_BILL_NO + " TEXT ,"
            + KEY_CUST_NAME + " TEXT ,"
            + KEY_CUSTOMER_CODE + " TEXT ,"
            + KEY_PUMP_SERIAL_NO + " TEXT ,"
            + KEY_MOTOR_SERIAL_NO + " TEXT ,"
            + KEY_CONTROLLER_SERIAL_NO + " TEXT ,"
            + KEY_CONTROLLER_MAT_NO + " TEXT ,"
            + KEY_SET_MATNO + " TEXT ,"
            + KEY_MOTOR_MATNO + " TEXT ,"
            + KEY_BENEFICIARY + " TEXT)";

    private static final String CREATE_TABLE_SETTING_PARAMETER_LIST  = " CREATE TABLE " + TABLE_SETTING_PARAMETER_LIST + " ( "
            + COLUMN_pmID + " TEXT ,"
            + COLUMN_ParametersName + " TEXT ,"
            + COLUMN_ModBusAddress + " TEXT ,"
            + COLUMN_MobBTAddress + " TEXT ,"
            + COLUMN_factor + " TEXT ,"
            + COLUMN_pValue + " TEXT ,"
            + COLUMN_MaterialCode + " TEXT ,"
            + COLUMN_Unit + " TEXT ,"
            + COLUMN_offset + " TEXT)";

    private static final String CREATE_PARAMETER_SET_DATA = "CREATE TABLE "
            + TABLE_PARAMETER_SET_DATA + "(" + KEY_PARAMETER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +KEY_BILL_NO+ " TEXT, " + KEY_PARAMETER_SET+" TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_DAMAGE_MISS);
        db.execSQL(CREATE_TABLE_LOGIN_SELEC);
        db.execSQL(CREATE_TABLE_DASHBOARD);
        db.execSQL(CREATE_TABLE_STATE_DISTRICT);
        db.execSQL(CREATE_TABLE_REGISTRATION);
        db.execSQL(CREATE_TABLE_INSTALLATION_LIST);
        db.execSQL(CREATE_TABLE_INSTALLATION_UNLOAD_LIST);
        db.execSQL(CREATE_TABLE_INSTALLATION_OFFLINE_LIST);
        db.execSQL(CREATE_TABLE_OFFLINE_SUBMITTED_LIST);
        db.execSQL(CREATE_TABLE_SETTING_PARAMETER_LIST);
        db.execSQL(CREATE_TABLE_AUDITSITE_LIST);
        db.execSQL(CREATE_TABLE_AUDIT_PUMP);
        db.execSQL(CREATE_TABLE_REJECTION_LIST);
        db.execSQL(CREATE_TABLE_SURVEY_LIST);
        db.execSQL(CREATE_TABLE_STATE_SEARCH);
        db.execSQL(CREATE_TABLE_INSTALLATION_PUMP);
        db.execSQL(CREATE_TABLE_KUSUMCSURVEYFORM);
        db.execSQL(CREATE_SIM_CARD_REPLACEMENT);
        db.execSQL(CREATE_TABLE_SURVEY_DATA);
        db.execSQL(CREATE_TABLE_INSTALLATION_IMAGES);
        db.execSQL(CREATE_TABLE_BENEFICIARY_IMAGES);
        db.execSQL(CREATE_TABLE_REJECTED_INSTALLATION_IMAGES);
        db.execSQL(CREATE_TABLE_SITE_AUDIT_IMAGES);
        db.execSQL(CREATE_TABLE_KusumCImages);
        db.execSQL(CREATE_TABLE_UNLOADING_IMAGES);
        db.execSQL(CREATE_TABLE_UNLOADING_FORM_DATA);
        db.execSQL(CREATE_TABLE_OFFLINE_CONTROLLER_IMAGE);
        db.execSQL(CREATE_TABLE_DEVICE_MAPPING_DATA);
        db.execSQL(CREATE_BENEFICIARY_REGISTRAION);
        db.execSQL(CREATE_TABLE_SETTING_PENDING_LIST);
        db.execSQL(CREATE_PARAMETER_SET_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAMAGE_MISS_COMPLAIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_SELECTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_DISTRICT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_UNLOAD_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_OFFLINE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE_SUBMITTED_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING_PARAMETER_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REJECTION_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_PUMP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KUSUMCSURVEYFORM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIM_REPLACMENT_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_PUMP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_SEARCH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITSITE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIT_PUMP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_IMAGE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BENEFICIARY_IMAGE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REJECTED_INSTALLATION_IMAGE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE_AUDIT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KusumCImages);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOADING_IMAGE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOADING_FORM_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE_CONTROLLER_IMAGE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_MAPPING_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BENEFICIARY_REGISTRATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING_PENDING_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARAMETER_SET_DATA);
            // create newworkorder tables
            onCreate(db);
        }
    }


    public void deleteSimData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_ENQ_DOC + "='" + value + "'";
        db.delete(TABLE_SIM_REPLACMENT_DATA, where, null);
    }


    public void insertSimCardData(String key, SimCardBean simCardBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_USER_ID, simCardBean.getUser_id());
            values.put(KEY_USER_TYPE, simCardBean.getUser_type());
            values.put(KEY_ENQ_DOC, simCardBean.getEnq_docno());
            values.put(KEY_DEVICE_NO, simCardBean.getDevice_no());
            values.put(KEY_SIM_CARD_LAT, simCardBean.getSim_lat());
            values.put(KEY_SIM_CARD_LNG, simCardBean.getSim_lng());
            values.put(KEY_CUST_NAME, simCardBean.getCust_name());
            values.put(KEY_CUST_MOBILE, simCardBean.getCust_mobile());
            values.put(KEY_CUST_ADDRESS, simCardBean.getCust_address());
            values.put(KEY_SIM_CARD_REP_DATE, simCardBean.getSim_rep_date());
            values.put(KEY_SIM_NEW_NO, simCardBean.getSim_new_no());
            values.put(KEY_SIM_OLD_NO, simCardBean.getSim_old_no());
            values.put(KEY_SIM_NEW_PHOTO, simCardBean.getSim_new_photo());
            values.put(KEY_SIM_OLD_PHOTO, simCardBean.getSim_old_photo());
            values.put(KEY_DRIVE_PHOTO, simCardBean.getDrive_photo());
            // Insert Row
            long i = db.insert(TABLE_SIM_REPLACMENT_DATA, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateSimData(String enqdoc, SimCardBean simCardBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        CustomUtility customUtility = new CustomUtility();
        LoginBean loginBean = new LoginBean();
        try {
            values = new ContentValues();
            String where = " ";

            values.put(KEY_USER_ID, simCardBean.getUser_id());
            values.put(KEY_USER_TYPE, simCardBean.getUser_type());
            values.put(KEY_ENQ_DOC, simCardBean.getEnq_docno());
            values.put(KEY_DEVICE_NO, simCardBean.getDevice_no());
            values.put(KEY_SIM_CARD_LAT, simCardBean.getSim_lat());
            values.put(KEY_SIM_CARD_LNG, simCardBean.getSim_lng());
            values.put(KEY_CUST_NAME, simCardBean.getCust_name());
            values.put(KEY_CUST_MOBILE, simCardBean.getCust_mobile());
            values.put(KEY_CUST_ADDRESS, simCardBean.getCust_address());
            values.put(KEY_SIM_CARD_REP_DATE, simCardBean.getSim_rep_date());
            values.put(KEY_SIM_NEW_NO, simCardBean.getSim_new_no());
            values.put(KEY_SIM_OLD_NO, simCardBean.getSim_old_no());
            values.put(KEY_SIM_NEW_PHOTO, simCardBean.getSim_new_photo());
            values.put(KEY_SIM_OLD_PHOTO, simCardBean.getSim_old_photo());
            values.put(KEY_DRIVE_PHOTO, simCardBean.getDrive_photo());

            where = KEY_ENQ_DOC + "='" + enqdoc + "'";

            i = db.update(TABLE_SIM_REPLACMENT_DATA, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertLoginData(Context context, LoginBean loginBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_USERID, loginBean.getUserid());
            values.put(KEY_USERNAME, loginBean.getUsername());
            values.put(KEY_USERTYPE, loginBean.getUsertype());
            if (CustomUtility.doesTableExist(db, TABLE_LOGIN)) {
                db.insert(TABLE_LOGIN, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertLoginSelectionData(String proj_no, String proj_nm, String login_no, String login_nm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_PROJ_NO, proj_no);
            values.put(KEY_PROJ_NM, proj_nm);
            values.put(KEY_LOGIN_NO, login_no);
            values.put(KEY_LOGIN_NM, login_nm);
            long i = db.insert(TABLE_LOGIN_SELECTION, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertStateData(String country, String country_text, String state, String state_text, String district, String district_text, String tehsil, String tehsil_text) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_COUNTRY, country);
            values.put(KEY_COUNTRY_TEXT, country_text);
            values.put(KEY_STATE, state);
            values.put(KEY_STATE_TEXT, state_text);
            values.put(KEY_DISTRICT, district);
            values.put(KEY_DISTRICT_TEXT, district_text);
            values.put(KEY_TEHSIL, tehsil);
            values.put(KEY_TEHSIL_TEXT, tehsil_text);


            // Insert Row
            long i = db.insert(TABLE_STATE_SEARCH, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void insertDashboardData(String proj_no, String process_no, String process_nm) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PROJ_NO, proj_no);
            values.put(KEY_PROCESS_NO, process_no);
            values.put(KEY_PROCESS_NM, process_nm);


            // Insert Row
            long i = db.insert(TABLE_DASHBOARD, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    public void insertRegistrationData(String enqdoc, RegistrationBean registrationBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, registrationBean.getPernr());
            values.put(KEY_PROJ_NO, registrationBean.getProject_no());
            values.put(KEY_LOGIN_NO, registrationBean.getLogin_no());
            values.put(KEY_DATE, registrationBean.getDate());
            values.put(KEY_LATITUDE, registrationBean.getLat());
            values.put(KEY_LONGITUDE, registrationBean.getLng());
            values.put(KEY_CUST_NAME, registrationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, registrationBean.getFather_name());
            values.put(KEY_STATE, registrationBean.getState());
            values.put(KEY_STATE_TEXT, registrationBean.getStatetxt());
            values.put(KEY_DISTRICT, registrationBean.getCity());
            values.put(KEY_DISTRICT_TEXT, registrationBean.getCitytxt());
            values.put(KEY_TEHSIL_TEXT, registrationBean.getTehsil());
            values.put(KEY_VILLAGE, registrationBean.getVillage());
            values.put(KEY_CONTACT_NO, registrationBean.getContact_no());
            values.put(KEY_AADHAR_NO, registrationBean.getAadhar_no());
            values.put(KEY_BANK_NAME, registrationBean.getBank_name());
            values.put(KEY_BANK_ACC_NO, registrationBean.getBank_acc_no());
            values.put(KEY_ACC_TYPE, registrationBean.getAccount_type());
            values.put(KEY_BRANCH_NAME, registrationBean.getBank_name());
            values.put(KEY_IFSC_CODE, registrationBean.getIfsc_code());
            values.put(KEY_AMOUNT, registrationBean.getAmount());
            values.put(KEY_PDF, registrationBean.getPdf());
            values.put(KEY_SYNC, registrationBean.getSync());

            // Insert Row
            long i = db.insert(TABLE_REGISTRATION, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }



    public void insertInstallationListData(String enqdoc, InstallationListBean installationBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, installationBean.getPernr());
            values.put(KEY_CUST_NAME, installationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, installationBean.getFather_name());
            values.put(KEY_BILL_NO, installationBean.getBillno());
            values.put(KEY_KUNNR, installationBean.getKunnr());
            values.put(KEY_GST_BILL_NO, installationBean.getGstbillno());
            values.put(KEY_BILL_DATE, installationBean.getBilldate());
            values.put(KEY_STATE, installationBean.getState());
            values.put(KEY_STATE_TEXT, installationBean.getStatetxt());
            values.put(KEY_DISTRICT, installationBean.getCity());
            values.put(KEY_DISTRICT_TEXT, installationBean.getCitytxt());
            values.put(KEY_TEHSIL_TEXT, installationBean.getTehsil());
            values.put(KEY_VILLAGE, installationBean.getVillage());
            values.put(KEY_CONTACT_NO, installationBean.getContact_no());
            values.put(KEY_CONTROLLER, installationBean.getController());
            values.put(KEY_MOTOR, installationBean.getMotor());
            values.put(KEY_PUMP, installationBean.getPump());
            values.put(KEY_ADDRESS, installationBean.getAddress());
            values.put(KEY_ADD1, installationBean.getSimno());
            values.put(KEY_ADD2, installationBean.getRegisno());
            values.put(KEY_ADD3, installationBean.getProjectno());
            values.put(KEY_ADD4, installationBean.getLoginno());
            values.put(KEY_ADD5, installationBean.getModuleqty());
            values.put(KEY_ADD6, installationBean.getBeneficiary());
            values.put(KEY_ADD7, installationBean.getDispdate());
            values.put(KEY_SYNC, installationBean.getSync());
            values.put(KEY_SET_MATNO, installationBean.getSet_matno());
            values.put(KEY_SIMHA2, installationBean.getSimha2());
            values.put(KEY_CUS_CONTACT_NO, installationBean.getCUS_CONTACT_NO());
            values.put(KEY_PANEL_MODULE_SER_NO, installationBean.getNoOfModule());
            values.put(KEY_HP, installationBean.getHP());
            values.put(KEY_PUMPLoad, installationBean.getPump_load());
            values.put(KEY_AADHAR_NO, installationBean.getAadhar_no());
            values.put(KEY_AADHAR_MOBILE, installationBean.getAadhar_mobile());
            long i = db.insert(TABLE_INSTALLATION_LIST, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertUnloadInstallationListData(String enqdoc, InstallationListBean installationBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, installationBean.getPernr());
            values.put(KEY_CUST_NAME, installationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, installationBean.getFather_name());
            values.put(KEY_BILL_NO, installationBean.getBillno());
            values.put(KEY_KUNNR, installationBean.getKunnr());
            values.put(KEY_GST_BILL_NO, installationBean.getGstbillno());
            values.put(KEY_BILL_DATE, installationBean.getBilldate());
            values.put(KEY_STATE, installationBean.getState());
            values.put(KEY_STATE_TEXT, installationBean.getStatetxt());
            values.put(KEY_DISTRICT, installationBean.getCity());
            values.put(KEY_DISTRICT_TEXT, installationBean.getCitytxt());
            values.put(KEY_TEHSIL_TEXT, installationBean.getTehsil());
            values.put(KEY_VILLAGE, installationBean.getVillage());
            values.put(KEY_CONTACT_NO, installationBean.getContact_no());
            values.put(KEY_CONTROLLER, installationBean.getController());
            values.put(KEY_MOTOR, installationBean.getMotor());
            values.put(KEY_PUMP, installationBean.getPump());
            values.put(KEY_ADDRESS, installationBean.getAddress());
            values.put(KEY_ADD1, installationBean.getSimno());
            values.put(KEY_ADD2, installationBean.getRegisno());
            values.put(KEY_ADD3, installationBean.getProjectno());
            values.put(KEY_ADD4, installationBean.getLoginno());
            values.put(KEY_ADD5, installationBean.getModuleqty());
            values.put(KEY_ADD6, installationBean.getBeneficiary());
            values.put(KEY_ADD7, installationBean.getDispdate());
            values.put(KEY_SYNC, installationBean.getSync());
            values.put(KEY_SET_MATNO, installationBean.getSet_matno());
            values.put(KEY_SIMHA2, installationBean.getSimha2());
            values.put(KEY_CUS_CONTACT_NO, installationBean.getCUS_CONTACT_NO());
            values.put(KEY_PANEL_MODULE_SER_NO, installationBean.getNoOfModule());
            values.put(KEY_HP, installationBean.getHP());
            values.put(KEY_PUMP_SERIAL_NO, installationBean.getPump_ser());
            values.put(KEY_PUMPLoad, installationBean.getPump_load());
            long i = db.insert(TABLE_INSTALLATION_UNLOAD_LIST, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertInstallationOfflineListData(InstallationOfflineBean installationOfflineBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, installationOfflineBean.getVbeln());
            values.put(KEY_PERNR, installationOfflineBean.getUserId());
            values.put(KEY_GST_BILL_NO, installationOfflineBean.getGstInvNo());
            values.put(KEY_BILL_DATE, installationOfflineBean.getFkdat());
            values.put(KEY_DISPATCH_DATE, installationOfflineBean.getDispatchDate());
            values.put(KEY_CUSTOMER_CODE, installationOfflineBean.getKunnr());
            values.put(KEY_CUST_NAME, installationOfflineBean.getName());
            values.put(KEY_STATE, installationOfflineBean.getRegio());
            values.put(KEY_MOBILE, installationOfflineBean.getMobile());
            values.put(KEY_CONTACT_NO, installationOfflineBean.getContactNo());
            values.put(KEY_CITY_CODE, installationOfflineBean.getCityc());
            values.put(KEY_TEHSIL_TEXT, installationOfflineBean.getOrt02());
            values.put(KEY_STREET, installationOfflineBean.getStras());
            values.put(KEY_ADDRESS, installationOfflineBean.getAddress());
            values.put(KEY_STATE_TEXT, installationOfflineBean.getRegioTxt());
            values.put(KEY_DISTRICT, installationOfflineBean.getCitycTxt());
            values.put(KEY_PUMP, installationOfflineBean.getPumpSernr());
            values.put(KEY_CONTROLLER, installationOfflineBean.getControllerSernr());
            values.put(KEY_CONTROLLER_MAT_NO, installationOfflineBean.getControllerMatno());
            values.put(KEY_SET_MATNO, installationOfflineBean.getSetMatno());
            values.put(KEY_MOTOR, installationOfflineBean.getMotorSernr());
            values.put(KEY_SIMHA2, installationOfflineBean.getSimha2());
            values.put(KEY_ADD1, installationOfflineBean.getSimno());
            values.put(KEY_SIMMOB, installationOfflineBean.getSimmob());
            values.put(KEY_ADD6, installationOfflineBean.getBeneficiary());
            values.put(KEY_PROJECT_NO, installationOfflineBean.getProjectNo());
            values.put(KEY_ADD4, installationOfflineBean.getProcessNo());
            values.put(KEY_ADD2, installationOfflineBean.getRegisno());
            values.put(KEY_ADD5, installationOfflineBean.getModuleQty());
            values.put(KEY_SYNC, installationOfflineBean.getSync());

            long i = db.insert(TABLE_INSTALLATION_OFFLINE_LIST, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
    }

    public void insertInstallationOfflineSubmittedData(SubmitOfflineDataInput submitOfflineDataInput, boolean isInsert, String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        long i = 0;
        try {
            values = new ContentValues();
            values.put(KEY_OFFLINE_BILL_NO, submitOfflineDataInput.getBillNo());
            values.put(KEY_OFFLINE_BENEFICIARY, submitOfflineDataInput.getBeneficiary());
            values.put(KEY_OFFLINE_CUSTOMER_NAME, submitOfflineDataInput.getCustomerName());
            values.put(KEY_OFFLINE_PROJECT_NO, submitOfflineDataInput.getProjectNo());
            values.put(KEY_OFFLINE_USERID, submitOfflineDataInput.getUserId());
            values.put(KEY_OFFLINE_REGISNO, submitOfflineDataInput.getRegisno());
            values.put(KEY_OFFLINE_OFFPHOTO, submitOfflineDataInput.getOffPhoto());
            if (isInsert) {
                i = db.insert(TABLE_OFFLINE_SUBMITTED_LIST, null, values);
            } else {
                where = KEY_OFFLINE_BILL_NO + "='" + billNo + "'";
                i = db.update(TABLE_OFFLINE_SUBMITTED_LIST, values, where, null);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
    }

    public void updateInstallationOfflineListData(String billNo, InstallationOfflineBean installationOfflineBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, installationOfflineBean.getVbeln());
            values.put(KEY_PERNR, installationOfflineBean.getUserId());
            values.put(KEY_GST_BILL_NO, installationOfflineBean.getGstInvNo());
            values.put(KEY_BILL_DATE, installationOfflineBean.getFkdat());
            values.put(KEY_DISPATCH_DATE, installationOfflineBean.getDispatchDate());
            values.put(KEY_CUSTOMER_CODE, installationOfflineBean.getKunnr());
            values.put(KEY_CUST_NAME, installationOfflineBean.getName());
            values.put(KEY_STATE, installationOfflineBean.getRegio());
            values.put(KEY_MOBILE, installationOfflineBean.getMobile());
            values.put(KEY_CONTACT_NO, installationOfflineBean.getContactNo());
            values.put(KEY_CITY_CODE, installationOfflineBean.getCityc());
            values.put(KEY_TEHSIL_TEXT, installationOfflineBean.getOrt02());
            values.put(KEY_STREET, installationOfflineBean.getStras());
            values.put(KEY_ADDRESS, installationOfflineBean.getAddress());
            values.put(KEY_STATE_TEXT, installationOfflineBean.getRegioTxt());
            values.put(KEY_DISTRICT, installationOfflineBean.getCitycTxt());
            values.put(KEY_PUMP, installationOfflineBean.getPumpSernr());
            values.put(KEY_CONTROLLER, installationOfflineBean.getControllerSernr());
            values.put(KEY_CONTROLLER_MAT_NO, installationOfflineBean.getControllerMatno());
            values.put(KEY_SET_MATNO, installationOfflineBean.getSetMatno());
            values.put(KEY_MOTOR, installationOfflineBean.getMotorSernr());
            values.put(KEY_SIMHA2, installationOfflineBean.getSimha2());
            values.put(KEY_ADD1, installationOfflineBean.getSimno());
            values.put(KEY_SIMMOB, installationOfflineBean.getSimmob());
            values.put(KEY_ADD6, installationOfflineBean.getBeneficiary());
            values.put(KEY_PROJECT_NO, installationOfflineBean.getProjectNo());
            values.put(KEY_ADD4, installationOfflineBean.getProcessNo());
            values.put(KEY_ADD2, installationOfflineBean.getRegisno());
            values.put(KEY_ADD5, installationOfflineBean.getModuleQty());
            values.put(KEY_SYNC, installationOfflineBean.getSync());
            where = KEY_BILL_NO + "='" + billNo + "'";
            i = db.update(TABLE_INSTALLATION_OFFLINE_LIST, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
    }

    private void closeDb(SQLiteDatabase db) {
        db.endTransaction();
        db.close();
    }

    public void insertAuditSiteListData(String enqdoc, SiteAuditListBean auditListBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, auditListBean.getPernr());
            values.put(KEY_CUST_NAME, auditListBean.getCustomer_name());
            values.put(KEY_FATH_NAME, auditListBean.getFather_name());
            values.put(KEY_BILL_NO, auditListBean.getBillno());
            values.put(KEY_GST_BILL_NO, auditListBean.getGstbillno());
            values.put(KEY_BILL_DATE, auditListBean.getBilldate());
            values.put(KEY_STATE, auditListBean.getState());
            values.put(KEY_STATE_TEXT, auditListBean.getStatetxt());
            values.put(KEY_DISTRICT, auditListBean.getCity());
            values.put(KEY_DISTRICT_TEXT, auditListBean.getCitytxt());
            values.put(KEY_CONTACT_NO, auditListBean.getContact_no());
            values.put(KEY_ADDRESS, auditListBean.getAddress());
            values.put(KEY_Vendor, auditListBean.getVendor());
            values.put(KEY_ADD1, auditListBean.getRegisno());
            values.put(KEY_ADD2, auditListBean.getProjectno());
            values.put(KEY_ADD3, auditListBean.getBeneficiary());
            values.put(KEY_ADD4, auditListBean.getDispdate());


            long i = db.insert(TABLE_AUDITSITE_LIST, null, values);

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertAuditData(String enqdoc, AuditSiteBean auditSiteBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, enqdoc);
            values.put(KEY_BILL_DATE, auditSiteBean.getBill_date());
            values.put(KEY_PERNR, auditSiteBean.getPernr());
            values.put(KEY_PROJ_NO, auditSiteBean.getProject_no());
            values.put(KEY_AUD_DATE, auditSiteBean.getAud_date());
            values.put(KEY_REGISNO, auditSiteBean.getRegis_no());
            values.put(KEY_NAME, auditSiteBean.getName());
            values.put(KEY_STATE, auditSiteBean.getState_ins_id());
            values.put(KEY_DISTRICT, auditSiteBean.getDistrict_ins_id());
            values.put(KEY_ADDRESS, auditSiteBean.getAddress_ins());
            values.put(KEY_CONTACT_NO, auditSiteBean.getMobile_no());
            values.put(KEY_FOUND, auditSiteBean.getFound());
            values.put(KEY_FOUND_RMK, auditSiteBean.getFound_rmk());
            values.put(KEY_STRUCT, auditSiteBean.getStruc_assem());
            values.put(KEY_STRUCT_RMK, auditSiteBean.getStruc_assem_rmk());
            values.put(KEY_DRV_MOUNT, auditSiteBean.getDrv_mount());
            values.put(KEY_DRV_MOUNT_RMK, auditSiteBean.getDrv_mount_rmk());
            values.put(KEY_LA_EARTH, auditSiteBean.getLa_earthing());
            values.put(KEY_LA_EARTH_RMK, auditSiteBean.getLa_earthing_rmk());
            values.put(KEY_WRK_QLTY, auditSiteBean.getWrk_quality());
            values.put(KEY_WRK_QLTY_RMK, auditSiteBean.getWrk_quality_rmk());
            values.put(KEY_SITE_RAT, auditSiteBean.getSite_art());
            long i = db.insert(TABLE_AUDIT_PUMP_DATA, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertRejectListData(String enqdoc, RejectListBean rejectListBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, rejectListBean.getBillno());
            values.put(KEY_BEN_NO, rejectListBean.getBenno());
            values.put(KEY_REG_NO, rejectListBean.getRegno());
            values.put(KEY_CUST_NAME, rejectListBean.getCustnm());
            values.put(KEY_PHOTO1, rejectListBean.getPhoto1());
            values.put(KEY_PHOTO2, rejectListBean.getPhoto2());
            values.put(KEY_PHOTO3, rejectListBean.getPhoto3());
            values.put(KEY_PHOTO4, rejectListBean.getPhoto4());
            values.put(KEY_PHOTO5, rejectListBean.getPhoto5());
            values.put(KEY_PHOTO6, rejectListBean.getPhoto6());
            values.put(KEY_PHOTO7, rejectListBean.getPhoto7());
            values.put(KEY_PHOTO8, rejectListBean.getPhoto8());
            values.put(KEY_PHOTO9, rejectListBean.getPhoto9());
            values.put(KEY_PHOTO10, rejectListBean.getPhoto10());
            values.put(KEY_PHOTO11, rejectListBean.getPhoto11());
            values.put(KEY_PHOTO12, rejectListBean.getPhoto12());
            values.put(KEY_REMARK1, rejectListBean.getRemark1());
            values.put(KEY_REMARK2, rejectListBean.getRemark2());
            values.put(KEY_REMARK3, rejectListBean.getRemark3());
            values.put(KEY_REMARK4, rejectListBean.getRemark4());
            values.put(KEY_REMARK5, rejectListBean.getRemark5());
            values.put(KEY_REMARK6, rejectListBean.getRemark6());
            values.put(KEY_REMARK7, rejectListBean.getRemark7());
            values.put(KEY_REMARK8, rejectListBean.getRemark8());
            values.put(KEY_REMARK9, rejectListBean.getRemark9());
            values.put(KEY_REMARK10, rejectListBean.getRemark10());
            values.put(KEY_REMARK11, rejectListBean.getRemark11());
            values.put(KEY_REMARK12, rejectListBean.getRemark12());
            long i = db.insert(TABLE_REJECTION_LIST, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateRejectListData(String enqdoc, RejectListBean rejectListBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";

        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, rejectListBean.getBillno());
            values.put(KEY_BEN_NO, rejectListBean.getBenno());
            values.put(KEY_REG_NO, rejectListBean.getRegno());
            values.put(KEY_CUST_NAME, rejectListBean.getCustnm());
            values.put(KEY_PHOTO1, rejectListBean.getPhoto1());
            values.put(KEY_PHOTO2, rejectListBean.getPhoto2());
            values.put(KEY_PHOTO3, rejectListBean.getPhoto3());
            values.put(KEY_PHOTO4, rejectListBean.getPhoto4());
            values.put(KEY_PHOTO5, rejectListBean.getPhoto5());
            values.put(KEY_PHOTO6, rejectListBean.getPhoto6());
            values.put(KEY_PHOTO7, rejectListBean.getPhoto7());
            values.put(KEY_PHOTO8, rejectListBean.getPhoto8());
            values.put(KEY_PHOTO9, rejectListBean.getPhoto9());
            values.put(KEY_PHOTO10, rejectListBean.getPhoto10());
            values.put(KEY_PHOTO11, rejectListBean.getPhoto11());
            values.put(KEY_PHOTO12, rejectListBean.getPhoto12());
            values.put(KEY_REMARK1, rejectListBean.getRemark1());
            values.put(KEY_REMARK2, rejectListBean.getRemark2());
            values.put(KEY_REMARK3, rejectListBean.getRemark3());
            values.put(KEY_REMARK4, rejectListBean.getRemark4());
            values.put(KEY_REMARK5, rejectListBean.getRemark5());
            values.put(KEY_REMARK6, rejectListBean.getRemark6());
            values.put(KEY_REMARK7, rejectListBean.getRemark7());
            values.put(KEY_REMARK8, rejectListBean.getRemark8());
            values.put(KEY_REMARK9, rejectListBean.getRemark9());
            values.put(KEY_REMARK10, rejectListBean.getRemark10());
            values.put(KEY_REMARK11, rejectListBean.getRemark11());
            values.put(KEY_REMARK12, rejectListBean.getRemark12());
            where = KEY_BILL_NO + "='" + enqdoc + "'";
            i = db.update(TABLE_REJECTION_LIST, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateInstallationListData(String enqdoc, InstallationListBean installationBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, installationBean.getPernr());
            values.put(KEY_CUST_NAME, installationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, installationBean.getFather_name());
            values.put(KEY_BILL_NO, installationBean.getBillno());
            values.put(KEY_KUNNR, installationBean.getKunnr());
            values.put(KEY_GST_BILL_NO, installationBean.getGstbillno());
            values.put(KEY_BILL_DATE, installationBean.getBilldate());
            values.put(KEY_STATE, installationBean.getState());
            values.put(KEY_STATE_TEXT, installationBean.getStatetxt());
            values.put(KEY_DISTRICT, installationBean.getCity());
            values.put(KEY_DISTRICT_TEXT, installationBean.getCitytxt());
            values.put(KEY_TEHSIL_TEXT, installationBean.getTehsil());
            values.put(KEY_VILLAGE, installationBean.getVillage());
            values.put(KEY_CONTACT_NO, installationBean.getContact_no());
            values.put(KEY_CONTROLLER, installationBean.getController());
            values.put(KEY_MOTOR, installationBean.getMotor());
            values.put(KEY_PUMP, installationBean.getPump());
            values.put(KEY_ADDRESS, installationBean.getAddress());
            values.put(KEY_ADD1, installationBean.getSimno());
            values.put(KEY_ADD2, installationBean.getRegisno());
            values.put(KEY_ADD3, installationBean.getProjectno());
            values.put(KEY_ADD4, installationBean.getLoginno());
            values.put(KEY_ADD5, installationBean.getModuleqty());
            values.put(KEY_ADD6, installationBean.getBeneficiary());
            values.put(KEY_ADD7, installationBean.getDispdate());
            values.put(KEY_SYNC, installationBean.getSync());
            values.put(KEY_SET_MATNO, installationBean.getSet_matno());
            values.put(KEY_SIMHA2, installationBean.getSimha2());
            values.put(KEY_CUS_CONTACT_NO, installationBean.getCUS_CONTACT_NO());
            values.put(KEY_PANEL_MODULE_SER_NO, installationBean.getNoOfModule());
            values.put(KEY_HP, installationBean.getHP());
            values.put(KEY_PUMPLoad, installationBean.getPump_load());
            values.put(KEY_AADHAR_NO, installationBean.getAadhar_no());
            values.put(KEY_AADHAR_MOBILE, installationBean.getAadhar_mobile());
            where = KEY_ENQ_DOC + "='" + enqdoc + "'";
            i = db.update(TABLE_INSTALLATION_LIST, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateUnloadInstallationListData(String enqdoc, InstallationListBean installationBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, installationBean.getPernr());
            values.put(KEY_CUST_NAME, installationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, installationBean.getFather_name());
            values.put(KEY_BILL_NO, installationBean.getBillno());
            values.put(KEY_KUNNR, installationBean.getKunnr());
            values.put(KEY_GST_BILL_NO, installationBean.getGstbillno());
            values.put(KEY_BILL_DATE, installationBean.getBilldate());
            values.put(KEY_STATE, installationBean.getState());
            values.put(KEY_STATE_TEXT, installationBean.getStatetxt());
            values.put(KEY_DISTRICT, installationBean.getCity());
            values.put(KEY_DISTRICT_TEXT, installationBean.getCitytxt());
            values.put(KEY_TEHSIL_TEXT, installationBean.getTehsil());
            values.put(KEY_VILLAGE, installationBean.getVillage());
            values.put(KEY_CONTACT_NO, installationBean.getContact_no());
            values.put(KEY_CONTROLLER, installationBean.getController());
            values.put(KEY_MOTOR, installationBean.getMotor());
            values.put(KEY_PUMP, installationBean.getPump());
            values.put(KEY_ADDRESS, installationBean.getAddress());
            values.put(KEY_ADD1, installationBean.getSimno());
            values.put(KEY_ADD2, installationBean.getRegisno());
            values.put(KEY_ADD3, installationBean.getProjectno());
            values.put(KEY_ADD4, installationBean.getLoginno());
            values.put(KEY_ADD5, installationBean.getModuleqty());
            values.put(KEY_ADD6, installationBean.getBeneficiary());
            values.put(KEY_ADD7, installationBean.getDispdate());
            values.put(KEY_SYNC, installationBean.getSync());
            values.put(KEY_SET_MATNO, installationBean.getSet_matno());
            values.put(KEY_SIMHA2, installationBean.getSimha2());
            values.put(KEY_CUS_CONTACT_NO, installationBean.getCUS_CONTACT_NO());
            values.put(KEY_PANEL_MODULE_SER_NO, installationBean.getNoOfModule());
            values.put(KEY_HP, installationBean.getHP());
            values.put(KEY_PUMP_SERIAL_NO, installationBean.getPump_ser());
            values.put(KEY_PUMPLoad, installationBean.getPump_load());
            where = KEY_ENQ_DOC + "='" + enqdoc + "'";
            i = db.update(TABLE_INSTALLATION_UNLOAD_LIST, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateAuditData(String enqdoc, AuditSiteBean auditSiteBean) {

        long i = 0;
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        String where = " ";

        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, enqdoc);
            values.put(KEY_BILL_DATE, auditSiteBean.getBill_date());
            values.put(KEY_PERNR, auditSiteBean.getPernr());
            values.put(KEY_PROJ_NO, auditSiteBean.getProject_no());
            values.put(KEY_REGISNO, auditSiteBean.getRegis_no());
            values.put(KEY_AUD_DATE, auditSiteBean.getAud_date());
            values.put(KEY_NAME, auditSiteBean.getName());
            values.put(KEY_STATE, auditSiteBean.getState_ins_id());
            values.put(KEY_DISTRICT, auditSiteBean.getDistrict_ins_id());
            values.put(KEY_ADDRESS, auditSiteBean.getAddress_ins());
            values.put(KEY_CONTACT_NO, auditSiteBean.getMobile_no());
            values.put(KEY_FOUND, auditSiteBean.getFound());
            values.put(KEY_FOUND_RMK, auditSiteBean.getFound_rmk());
            values.put(KEY_STRUCT, auditSiteBean.getStruc_assem());
            values.put(KEY_STRUCT_RMK, auditSiteBean.getStruc_assem_rmk());
            values.put(KEY_DRV_MOUNT, auditSiteBean.getDrv_mount());
            values.put(KEY_DRV_MOUNT_RMK, auditSiteBean.getDrv_mount_rmk());
            values.put(KEY_LA_EARTH, auditSiteBean.getLa_earthing());
            values.put(KEY_LA_EARTH_RMK, auditSiteBean.getLa_earthing_rmk());
            values.put(KEY_WRK_QLTY, auditSiteBean.getWrk_quality());
            values.put(KEY_WRK_QLTY_RMK, auditSiteBean.getWrk_quality_rmk());
            values.put(KEY_SITE_RAT, auditSiteBean.getSite_art());

            where = KEY_BILL_NO + "='" + enqdoc + "'";

            i = db.update(TABLE_AUDIT_PUMP_DATA, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void updateAuditSiteListData(String enqdoc, SiteAuditListBean siteAuditListBean) {
        long i = 0;
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        String where = " ";

        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, siteAuditListBean.getPernr());
            values.put(KEY_CUST_NAME, siteAuditListBean.getCustomer_name());
            values.put(KEY_FATH_NAME, siteAuditListBean.getFather_name());
            values.put(KEY_BILL_NO, siteAuditListBean.getBillno());
            values.put(KEY_GST_BILL_NO, siteAuditListBean.getGstbillno());
            values.put(KEY_BILL_DATE, siteAuditListBean.getBilldate());
            values.put(KEY_STATE, siteAuditListBean.getState());
            values.put(KEY_STATE_TEXT, siteAuditListBean.getStatetxt());
            values.put(KEY_DISTRICT, siteAuditListBean.getCity());
            values.put(KEY_DISTRICT_TEXT, siteAuditListBean.getCitytxt());
            values.put(KEY_CONTACT_NO, siteAuditListBean.getContact_no());
            values.put(KEY_ADDRESS, siteAuditListBean.getAddress());
            values.put(KEY_Vendor, siteAuditListBean.getVendor());
            values.put(KEY_ADD1, siteAuditListBean.getRegisno());
            values.put(KEY_ADD2, siteAuditListBean.getProjectno());
            values.put(KEY_ADD3, siteAuditListBean.getBeneficiary());
            values.put(KEY_ADD4, siteAuditListBean.getDispdate());

            where = KEY_ENQ_DOC + "='" + enqdoc + "'";

            i = db.update(TABLE_AUDITSITE_LIST, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void insertSurveyListData(String enqdoc, SurveyListBean surveyListBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, surveyListBean.getPernr());
            values.put(KEY_CUST_NAME, surveyListBean.getCustnam());
            values.put(KEY_CONTACT_NO, surveyListBean.getContctno());
            values.put(KEY_STATE, surveyListBean.getState());
            values.put(KEY_DISTRICT, surveyListBean.getDistrict());
            values.put(KEY_ADDRESS, surveyListBean.getAddress());
            values.put(KEY_ADD2, surveyListBean.getRegino());

            // Insert Row
            long i = db.insert(TABLE_SURVEY_LIST, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void updateSurveyListData(String enqdoc, SurveyListBean surveyListBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, surveyListBean.getPernr());
            values.put(KEY_CUST_NAME, surveyListBean.getCustnam());
            values.put(KEY_CONTACT_NO, surveyListBean.getContctno());
            values.put(KEY_STATE, surveyListBean.getState());
            values.put(KEY_DISTRICT, surveyListBean.getDistrict());
            values.put(KEY_ADDRESS, surveyListBean.getAddress());
            values.put(KEY_ADD2, surveyListBean.getRegino());

            where = KEY_ENQ_DOC + "='" + enqdoc + "'";

            i = db.update(TABLE_SURVEY_LIST, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void updateRegistrationData(String enqdoc, RegistrationBean registrationBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_ENQ_DOC, enqdoc);
            values.put(KEY_PERNR, registrationBean.getPernr());
            values.put(KEY_PROJ_NO, registrationBean.getProject_no());
            values.put(KEY_LOGIN_NO, registrationBean.getLogin_no());
            values.put(KEY_DATE, registrationBean.getDate());
            values.put(KEY_LATITUDE, registrationBean.getLat());
            values.put(KEY_LONGITUDE, registrationBean.getLng());
            values.put(KEY_CUST_NAME, registrationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, registrationBean.getFather_name());
            values.put(KEY_STATE, registrationBean.getState());
            values.put(KEY_STATE_TEXT, registrationBean.getStatetxt());
            values.put(KEY_DISTRICT, registrationBean.getCity());
            values.put(KEY_DISTRICT_TEXT, registrationBean.getCitytxt());
            values.put(KEY_TEHSIL_TEXT, registrationBean.getTehsil());
            values.put(KEY_VILLAGE, registrationBean.getVillage());
            values.put(KEY_CONTACT_NO, registrationBean.getContact_no());
            values.put(KEY_AADHAR_NO, registrationBean.getAadhar_no());
            values.put(KEY_BANK_NAME, registrationBean.getBank_name());
            values.put(KEY_BANK_ACC_NO, registrationBean.getBank_acc_no());
            values.put(KEY_ACC_TYPE, registrationBean.getAccount_type());
            values.put(KEY_BRANCH_NAME, registrationBean.getBank_name());
            values.put(KEY_IFSC_CODE, registrationBean.getIfsc_code());
            values.put(KEY_AMOUNT, registrationBean.getAmount());
            values.put(KEY_PDF, registrationBean.getPdf());
            values.put(KEY_SYNC, registrationBean.getSync());

            where = KEY_ENQ_DOC + "='" + enqdoc + "'";

            i = db.update(TABLE_REGISTRATION, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void insertInstallationData(String enqdoc, InstallationBean installationBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, installationBean.getInst_bill_no());
            values.put(KEY_BILL_DATE, installationBean.getBill_date());
            values.put(KEY_PERNR, installationBean.getPernr());
            values.put(KEY_PROJ_NO, installationBean.getProject_no());
            values.put(KEY_LOGIN_NO, installationBean.getLogin_no());
            values.put(KEY_INST_DATE, installationBean.getInst_date());
            values.put(KEY_RMS_DATA_STATUS, installationBean.getRms_data_status());
            values.put(KEY_LATITUDE, installationBean.getLatitude());
            values.put(KEY_LONGITUDE, installationBean.getLongitude());
            values.put(KEY_CUST_NAME, installationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, installationBean.getFathers_name());
            values.put(KEY_STATE_TEXT, installationBean.getState_ins_txt());
            values.put(KEY_STATE, installationBean.getState_ins_id());
            values.put(KEY_CONTACT_NO, installationBean.getMobile_no());
            values.put(KEY_ADDRESS, installationBean.getAddress_ins());
            values.put(KEY_DISTRICT_TEXT, installationBean.getDistrict_ins_txt());
            values.put(KEY_DISTRICT, installationBean.getDistrict_ins_id());
            values.put(KEY_TEHSIL_TEXT, installationBean.getTehsil_ins());
            values.put(KEY_VILLAGE, installationBean.getVillage_ins());
            values.put(KEY_SOLAR_PANEL_WATT, installationBean.getSolarpanel_wattage());
            values.put(KEY_SOLAR_PANEL_STAND_INSTALL_QTY, installationBean.getSolarpanel_stand_ins_quantity());
            values.put(KEY_HP, installationBean.getInst_hp());
            values.put(KEY_TOTAL_WATT, installationBean.getTotal_watt());
            values.put(KEY_PANEL_MODULE_QTY, installationBean.getNo_of_module_qty());
            values.put(KEY_PANEL_MODULE_SER_NO, installationBean.getNo_of_module_value());
            values.put(KEY_TOTAL_PLATES_PER_WATT, installationBean.getModule_total_plate_watt());
            values.put(KEY_MOTOR_MODAL_DETAILS, installationBean.getSolar_motor_model_details());
            values.put(KEY_MOTOR_MODAL_SER_NO, installationBean.getSmmd_sno());
            values.put(KEY_PUMP_MODAL_DETAILS, installationBean.getSplar_pump_model_details());
            values.put(KEY_PUMP_MODAL_SER_NO, installationBean.getSpmd_sno());
            values.put(KEY_CONTROLER_MODAL_DETAILS, installationBean.getSolar_controller_model());
            values.put(KEY_CONTROLER_MODAL_SER_NO, installationBean.getScm_sno());
            values.put(KEY_SIM_OPERATOR_TYPE, installationBean.getSimoprator());
            values.put(KEY_CONNECTION_TYPE, installationBean.getConntype());
            values.put(KEY_SIM_NO, installationBean.getSimcard_num());
            values.put(KEY_ADD1, installationBean.getRegis_no());
            values.put(KEY_ADD2, installationBean.getDelay_reason());
            values.put(KEY_ADD3, installationBean.getMake_ins());
            values.put(KEY_BENEFICIARY_NO, installationBean.getBeneficiaryNo());
            values.put(KEY_PUMPLoad, installationBean.getPumpLoad());
            values.put(KEY_AADHAR_NO, installationBean.getAadhar_no());
            // Insert Row
            long i = db.insert(TABLE_INSTALLATION_PUMP_DATA, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    public void updateInstallationData(String billno, InstallationBean installationBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, installationBean.getInst_bill_no());
            values.put(KEY_BILL_DATE, installationBean.getBill_date());
            values.put(KEY_PERNR, installationBean.getPernr());
            values.put(KEY_PROJ_NO, installationBean.getProject_no());
            values.put(KEY_LOGIN_NO, installationBean.getLogin_no());
            values.put(KEY_INST_DATE, installationBean.getInst_date());
            values.put(KEY_RMS_DATA_STATUS, installationBean.getRms_data_status());
            values.put(KEY_LATITUDE, installationBean.getLatitude());
            values.put(KEY_LONGITUDE, installationBean.getLongitude());
            values.put(KEY_CUST_NAME, installationBean.getCustomer_name());
            values.put(KEY_FATH_NAME, installationBean.getFathers_name());
            values.put(KEY_STATE_TEXT, installationBean.getState_ins_txt());
            values.put(KEY_STATE, installationBean.getState_ins_id());
            values.put(KEY_CONTACT_NO, installationBean.getMobile_no());
            values.put(KEY_ADDRESS, installationBean.getAddress_ins());
            values.put(KEY_DISTRICT_TEXT, installationBean.getDistrict_ins_txt());
            values.put(KEY_DISTRICT, installationBean.getDistrict_ins_id());
            values.put(KEY_TEHSIL_TEXT, installationBean.getTehsil_ins());
            values.put(KEY_VILLAGE, installationBean.getVillage_ins());
            values.put(KEY_SOLAR_PANEL_WATT, installationBean.getSolarpanel_wattage());
            values.put(KEY_SOLAR_PANEL_STAND_INSTALL_QTY, installationBean.getSolarpanel_stand_ins_quantity());
            values.put(KEY_HP, installationBean.getInst_hp());
            values.put(KEY_TOTAL_WATT, installationBean.getTotal_watt());
            values.put(KEY_PANEL_MODULE_QTY, installationBean.getNo_of_module_qty());
            values.put(KEY_PANEL_MODULE_SER_NO, installationBean.getNo_of_module_value());
            values.put(KEY_TOTAL_PLATES_PER_WATT, installationBean.getModule_total_plate_watt());
            values.put(KEY_MOTOR_MODAL_DETAILS, installationBean.getSolar_motor_model_details());
            values.put(KEY_MOTOR_MODAL_SER_NO, installationBean.getSmmd_sno());
            values.put(KEY_PUMP_MODAL_DETAILS, installationBean.getSplar_pump_model_details());
            values.put(KEY_PUMP_MODAL_SER_NO, installationBean.getSpmd_sno());
            values.put(KEY_CONTROLER_MODAL_DETAILS, installationBean.getSolar_controller_model());
            values.put(KEY_CONTROLER_MODAL_SER_NO, installationBean.getScm_sno());
            values.put(KEY_SIM_OPERATOR_TYPE, installationBean.getSimoprator());
            values.put(KEY_CONNECTION_TYPE, installationBean.getConntype());
            values.put(KEY_SIM_NO, installationBean.getSimcard_num());
            values.put(KEY_ADD1, installationBean.getRegis_no());
            values.put(KEY_ADD2, installationBean.getDelay_reason());
            values.put(KEY_ADD3, installationBean.getMake_ins());
            values.put(KEY_BENEFICIARY_NO, installationBean.getBeneficiaryNo());
            values.put(KEY_PUMPLoad, installationBean.getPumpLoad());
            values.put(KEY_AADHAR_NO, installationBean.getAadhar_no());
            where = KEY_BILL_NO + "='" + billno + "'";

            i = db.update(TABLE_INSTALLATION_PUMP_DATA, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertKusumCSurveyform(String enqdoc, KusumCSurveyBean kusumCSurveyBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_PROJECT_NO, kusumCSurveyBean.getProject_no());
            values.put(KEY_USER_ID_, kusumCSurveyBean.getUserid());
            values.put(KEY_PROJECT_LOGIN_NO, "01");
            values.put(KEY_FARMER_CONTACT_NO, kusumCSurveyBean.getFARMER_CONTACT_NO());
            values.put(KEY_APPLICANT_NO, kusumCSurveyBean.getAPPLICANT_NO());
            values.put(KEY_REGISNO, kusumCSurveyBean.getREGISNO());
            values.put(KEY_BENEFICIARY, kusumCSurveyBean.getBENEFICIARY());
            values.put(KEY_SITE_ADRC, kusumCSurveyBean.getSITE_ADRC());
            values.put(KEY_LAT, kusumCSurveyBean.getLAT());
            values.put(KEY_LNG, kusumCSurveyBean.getLNG());
            values.put(KEY_CATEGORY, kusumCSurveyBean.getCATEGORY());
            values.put(KEY_WATER_SOURCE, kusumCSurveyBean.getWATER_SOURCE());
            values.put(KEY_INTERNET_TYPE, kusumCSurveyBean.getINTERNET_TYPE());
            values.put(KEY_CROP_PATTERN, kusumCSurveyBean.getCROP_PATTERN());
            values.put(KEY_TYPE_OF_IRIGATN, kusumCSurveyBean.getTYPE_OF_IRIGATN());
            values.put(KEY_SHADOW_FREE_LAND, kusumCSurveyBean.getSHADOW_FREE_LAND());
            values.put(KEY_ELEC_CON, kusumCSurveyBean.getELEC_CON());
            values.put(KEY_ELEC_IDEN_NO, kusumCSurveyBean.getELEC_IDEN_NO());
            values.put(KEY_PUMP_TYPE, kusumCSurveyBean.getPUMP_TYPE());
            values.put(KEY_ELEC_RATING,kusumCSurveyBean.getELEC_CONN_RAT());
            values.put(KEY_PUMP_SET_RATING, kusumCSurveyBean.getPUMP_SET_RATING());
            values.put(KEY_PUMP_MAKE, kusumCSurveyBean.getPUMP_MAKE());
            values.put(KEY_CABLE_DETAIL_WITH_MAKE,kusumCSurveyBean.getCABLE_DET_MAKE());
            values.put(KEY_PHASE_VOL_V1, kusumCSurveyBean.getPHASE_VOL_V1());
            values.put(KEY_PHASE_VOL_V2, kusumCSurveyBean.getPHASE_VOL_V2());
            values.put(KEY_PHASE_VOL_V3, kusumCSurveyBean.getPHASE_VOL_V3());
            values.put(KEY_LINE_VOL_V1, kusumCSurveyBean.getLINE_VOL_V1());
            values.put(KEY_LINE_VOL_V2, kusumCSurveyBean.getLINE_VOL_V2());
            values.put(KEY_LINE_VOL_V3, kusumCSurveyBean.getLINE_VOL_V3());

            values.put(KEY_LINE_CRNT_AMP1, kusumCSurveyBean.getLINE_CRNT_AMP1());
            values.put(KEY_LINE_CRNT_AMP2, kusumCSurveyBean.getLINE_CRNT_AMP2());
            values.put(KEY_LINE_CRNT_AMP3, kusumCSurveyBean.getLINE_CRNT_AMP3());
            values.put(KEY_FREQ_HERTZ, kusumCSurveyBean.getFREQ_HERTZ());
            values.put(KEY_LINE_POWFACT_1, kusumCSurveyBean.getLINE_POWFACT_1());
            values.put(KEY_LINE_POWFACT_2, kusumCSurveyBean.getLINE_POWFACT_2());
            values.put(KEY_LINE_POWFACT_3, kusumCSurveyBean.getLINE_POWFACT_3());
            values.put(KEY_BOREWELL_SIZE, kusumCSurveyBean.getBOREWELL_SIZE());

            values.put(KEY_BOREWELL_DEPTH, kusumCSurveyBean.getBOREWELL_DEPTH());
            values.put(KEY_PUMP_SET_DEPTH, kusumCSurveyBean.getPUMP_SET_DEPTH());
            values.put(KEY_DIS_PUMP_LPM, kusumCSurveyBean.getDIS_PUMP_LPM());
            values.put(KEY_DEL_PUMP_LPM, kusumCSurveyBean.getDEL_PUMP_LPM());
            values.put(KEY_DEL_PIPE_LINE,kusumCSurveyBean.getPIPE_LEN_SIZE());
            values.put(KEY_TOTAL_DYNAMIC_HEAD,kusumCSurveyBean.getDYNAMIC_HEAD());

            values.put(KEY_TRANSFORMER_RATING,kusumCSurveyBean.getTRANSF_RATING());
            values.put(KEY_SERVICE_LINE,kusumCSurveyBean.getSERVICE_LINE());
            values.put(KEY_THREE_PHASE,kusumCSurveyBean.getTHREE_PH_SUPPLY());
            values.put(KEY_ELECTRICITY_BILL,kusumCSurveyBean.getELECTRIC_BILL());
            values.put(KEY_NEUTRAL_AVAILABILITY,kusumCSurveyBean.getNEUTRL_GRID_AVBL());
            values.put(KEY_STURCTURE_WATER_SOURCE,kusumCSurveyBean.getWATER_SOURC_LEN());
            values.put(KEY_FEEDER_TO_FARMER,kusumCSurveyBean.getDIST_FARMAR());
            values.put(KEY_ADDITIONAL_INFO,kusumCSurveyBean.getIFNO_REMARK());

            values.put(KEY_POWER_IN_VOLT,kusumCSurveyBean.getpowerInVolt());
            values.put(KEY_EXDISCHARGE,kusumCSurveyBean.getExDischarge());
            values.put(KEY_EXDYNAMIC,kusumCSurveyBean.getExDynamichead());
            values.put(KEY_AADHAR_NO,kusumCSurveyBean.getAadharNo());
            values.put(KEY_AADHAR_MOBILE,kusumCSurveyBean.getAadharRegMob());

            values.put(KEY_PHOTO1, kusumCSurveyBean.getPhoto1());
            values.put(KEY_PHOTO2, kusumCSurveyBean.getPhoto2());
            values.put(KEY_PHOTO3, kusumCSurveyBean.getPhoto3());
            values.put(KEY_PHOTO4, kusumCSurveyBean.getPhoto4());
            values.put(KEY_PHOTO5, kusumCSurveyBean.getPhoto5());
            values.put(KEY_PHOTO6, kusumCSurveyBean.getPhoto6());
            values.put(KEY_DISTANCE, kusumCSurveyBean.getDISTANCE());


            // Insert Row
            long i = db.insert(TABLE_KUSUMCSURVEYFORM, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    public void updateKusumCSurveyform(String enqdoc, KusumCSurveyBean kusumCSurveyBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();

            values.put(KEY_PROJECT_NO, kusumCSurveyBean.getProject_no());
            values.put(KEY_USER_ID_, kusumCSurveyBean.getUserid());
            values.put(KEY_PROJECT_LOGIN_NO, "01");
            values.put(KEY_FARMER_CONTACT_NO, kusumCSurveyBean.getFARMER_CONTACT_NO());
            values.put(KEY_APPLICANT_NO, kusumCSurveyBean.getAPPLICANT_NO());
            values.put(KEY_REGISNO, kusumCSurveyBean.getREGISNO());
            values.put(KEY_BENEFICIARY, kusumCSurveyBean.getBENEFICIARY());
            values.put(KEY_SITE_ADRC, kusumCSurveyBean.getSITE_ADRC());
            values.put(KEY_LAT, kusumCSurveyBean.getLAT());
            values.put(KEY_LNG, kusumCSurveyBean.getLNG());
            values.put(KEY_CATEGORY, kusumCSurveyBean.getCATEGORY());

            values.put(KEY_WATER_SOURCE, kusumCSurveyBean.getWATER_SOURCE());
            values.put(KEY_INTERNET_TYPE, kusumCSurveyBean.getINTERNET_TYPE());
            values.put(KEY_CROP_PATTERN, kusumCSurveyBean.getCROP_PATTERN());
            values.put(KEY_TYPE_OF_IRIGATN, kusumCSurveyBean.getTYPE_OF_IRIGATN());
            values.put(KEY_SHADOW_FREE_LAND, kusumCSurveyBean.getSHADOW_FREE_LAND());
            values.put(KEY_ELEC_CON, kusumCSurveyBean.getELEC_CON());
            values.put(KEY_ELEC_IDEN_NO, kusumCSurveyBean.getELEC_IDEN_NO());
            values.put(KEY_PUMP_TYPE, kusumCSurveyBean.getPUMP_TYPE());
            values.put(KEY_ELEC_RATING,kusumCSurveyBean.getELEC_CONN_RAT());

            values.put(KEY_PUMP_SET_RATING, kusumCSurveyBean.getPUMP_SET_RATING());
            values.put(KEY_PUMP_MAKE, kusumCSurveyBean.getPUMP_MAKE());
            values.put(KEY_CABLE_DETAIL_WITH_MAKE,kusumCSurveyBean.getCABLE_DET_MAKE());

            values.put(KEY_PHASE_VOL_V1, kusumCSurveyBean.getPHASE_VOL_V1());
            values.put(KEY_PHASE_VOL_V2, kusumCSurveyBean.getPHASE_VOL_V2());
            values.put(KEY_PHASE_VOL_V3, kusumCSurveyBean.getPHASE_VOL_V3());
            values.put(KEY_LINE_VOL_V1, kusumCSurveyBean.getLINE_VOL_V1());
            values.put(KEY_LINE_VOL_V2, kusumCSurveyBean.getLINE_VOL_V2());
            values.put(KEY_LINE_VOL_V3, kusumCSurveyBean.getLINE_VOL_V3());

            values.put(KEY_LINE_CRNT_AMP1, kusumCSurveyBean.getLINE_CRNT_AMP1());
            values.put(KEY_LINE_CRNT_AMP2, kusumCSurveyBean.getLINE_CRNT_AMP2());
            values.put(KEY_LINE_CRNT_AMP3, kusumCSurveyBean.getLINE_CRNT_AMP3());
            values.put(KEY_FREQ_HERTZ, kusumCSurveyBean.getFREQ_HERTZ());
            values.put(KEY_LINE_POWFACT_1, kusumCSurveyBean.getLINE_POWFACT_1());
            values.put(KEY_LINE_POWFACT_2, kusumCSurveyBean.getLINE_POWFACT_2());
            values.put(KEY_LINE_POWFACT_3, kusumCSurveyBean.getLINE_POWFACT_3());
            values.put(KEY_BOREWELL_SIZE, kusumCSurveyBean.getBOREWELL_SIZE());

            values.put(KEY_BOREWELL_DEPTH, kusumCSurveyBean.getBOREWELL_DEPTH());
            values.put(KEY_PUMP_SET_DEPTH, kusumCSurveyBean.getPUMP_SET_DEPTH());
            values.put(KEY_DIS_PUMP_LPM, kusumCSurveyBean.getDIS_PUMP_LPM());
            values.put(KEY_DEL_PUMP_LPM, kusumCSurveyBean.getDEL_PUMP_LPM());
            values.put(KEY_DEL_PIPE_LINE,kusumCSurveyBean.getPIPE_LEN_SIZE());
            values.put(KEY_TOTAL_DYNAMIC_HEAD,kusumCSurveyBean.getDYNAMIC_HEAD());

            values.put(KEY_TRANSFORMER_RATING,kusumCSurveyBean.getTRANSF_RATING());
            values.put(KEY_SERVICE_LINE,kusumCSurveyBean.getSERVICE_LINE());
            values.put(KEY_THREE_PHASE,kusumCSurveyBean.getTHREE_PH_SUPPLY());
            values.put(KEY_ELECTRICITY_BILL,kusumCSurveyBean.getELECTRIC_BILL());
            values.put(KEY_NEUTRAL_AVAILABILITY,kusumCSurveyBean.getNEUTRL_GRID_AVBL());
            values.put(KEY_STURCTURE_WATER_SOURCE,kusumCSurveyBean.getWATER_SOURC_LEN());
            values.put(KEY_FEEDER_TO_FARMER,kusumCSurveyBean.getDIST_FARMAR());
            values.put(KEY_ADDITIONAL_INFO,kusumCSurveyBean.getIFNO_REMARK());

            values.put(KEY_POWER_IN_VOLT,kusumCSurveyBean.getpowerInVolt());
            values.put(KEY_EXDISCHARGE,kusumCSurveyBean.getExDischarge());
            values.put(KEY_EXDYNAMIC,kusumCSurveyBean.getExDynamichead());
            values.put(KEY_AADHAR_NO,kusumCSurveyBean.getAadharNo());
            values.put(KEY_AADHAR_MOBILE,kusumCSurveyBean.getAadharRegMob());

            values.put(KEY_PHOTO1, kusumCSurveyBean.getPhoto1());
            values.put(KEY_PHOTO2, kusumCSurveyBean.getPhoto2());
            values.put(KEY_PHOTO3, kusumCSurveyBean.getPhoto3());
            values.put(KEY_PHOTO4, kusumCSurveyBean.getPhoto4());
            values.put(KEY_PHOTO5, kusumCSurveyBean.getPhoto5());
            values.put(KEY_PHOTO6, kusumCSurveyBean.getPhoto6());
            values.put(KEY_DISTANCE, kusumCSurveyBean.getDISTANCE());

            // Insert Row
            where = KEY_APPLICANT_NO + "='" + enqdoc + "'";

            long i = db.update(TABLE_KUSUMCSURVEYFORM, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void insertSurveyData(String enqdoc, SurveyBean surveyBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, surveyBean.getSurvy_bill_no());
            values.put(KEY_PERNR, surveyBean.getPernr());
            values.put(KEY_PROJ_NO, surveyBean.getProject_no());
            values.put(KEY_LOGIN_NO, surveyBean.getLogin_no());
            values.put(KEY_LATITUDE, surveyBean.getInst_latitude());
            values.put(KEY_LONGITUDE, surveyBean.getInst_longitude());
            values.put(KEY_WATER_RES, surveyBean.getSpinner_water_resource());
            values.put(KEY_BORWELL_SIZE, surveyBean.getBorewell_size());
            values.put(KEY_BORWELL_DEPTH, surveyBean.getBorwell_depth());
            values.put(KEY_CBL_LEN, surveyBean.getCbl_length());
            values.put(KEY_SURF_HEAD, surveyBean.getSurf_head());
            values.put(KEY_LEN_DIA_PIP, surveyBean.getLen_dia_dis_pip());
            long i = db.insert(TABLE_SURVEY_PUMP_DATA, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateSurveyData(String billno, SurveyBean surveyBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, surveyBean.getSurvy_bill_no());
            values.put(KEY_PERNR, surveyBean.getPernr());
            values.put(KEY_PROJ_NO, surveyBean.getProject_no());
            values.put(KEY_LOGIN_NO, surveyBean.getLogin_no());
            values.put(KEY_LATITUDE, surveyBean.getInst_latitude());
            values.put(KEY_LONGITUDE, surveyBean.getInst_longitude());
            values.put(KEY_WATER_RES, surveyBean.getSpinner_water_resource());
            values.put(KEY_BORWELL_SIZE, surveyBean.getBorewell_size());
            values.put(KEY_BORWELL_DEPTH, surveyBean.getBorwell_depth());
            values.put(KEY_CBL_LEN, surveyBean.getCbl_length());
            values.put(KEY_SURF_HEAD, surveyBean.getSurf_head());
            values.put(KEY_LEN_DIA_PIP, surveyBean.getLen_dia_dis_pip());
            where = KEY_BILL_NO + "='" + billno + "'";
            i = db.update(TABLE_SURVEY_PUMP_DATA, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateDashboard(String proj_no, String process_no, String process_nm) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            String where = " ";
            values.put(KEY_PROJ_NO, proj_no);
            values.put(KEY_PROCESS_NO, process_no);
            values.put(KEY_PROCESS_NM, process_nm);
            where = KEY_PROCESS_NO + "='" + process_no + "'";
            i = db.update(TABLE_DASHBOARD, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @SuppressLint("Range")
    public boolean getLogin() {
        LoginBean loginBean = new LoginBean();
        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    loginBean.setUserid(cursor.getString(cursor.getColumnIndex(KEY_USERID)));
                    loginBean.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                    loginBean.setUsertype(cursor.getString(cursor.getColumnIndex(KEY_USERTYPE)));
                    return true;
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    @SuppressLint("Range")
    public ArrayList<String> getList(String key, String text) {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        list.clear();

        db.beginTransaction();
        try {

            String selectQuery = null;
            String result = null;

            switch (key) {

                case KEY_PROJ_TXT:
                    selectQuery = "SELECT  DISTINCT " + KEY_PROJ_NM + " FROM " + TABLE_LOGIN_SELECTION;
                    list.add("Select Project Type");
                    break;
                case KEY_LOGIN_TXT:
                    selectQuery = "SELECT  DISTINCT " + KEY_LOGIN_NM + " FROM " + TABLE_LOGIN_SELECTION
                            + " WHERE " + KEY_PROJ_NM + " = '" + text + "'";
                    list.add("Select Login Type");
                    break;


            }


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("SIZE", "&&&" + cursor.getCount());


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        switch (key) {

                            case KEY_PROJ_TXT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_PROJ_NM)));
                                break;
                            case KEY_LOGIN_TXT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_NM)));
                                break;


                        }
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list;
    }

    @SuppressLint("Range")
    public ArrayList<String> getList1(String key, String text) {

        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();


        list.clear();

        db.beginTransaction();
        try {

            String selectQuery = null;

            switch (key) {

                case KEY_STATE:
                    selectQuery = "SELECT  DISTINCT " + KEY_STATE_TEXT + " FROM " + TABLE_STATE_SEARCH;
                    list.add("Select State");
                    break;
                case KEY_DISTRICT:

                    selectQuery = "SELECT  DISTINCT " + KEY_DISTRICT_TEXT + " FROM " + TABLE_STATE_SEARCH
                            + " WHERE " + KEY_STATE_TEXT + " = '" + text + "'";

                    list.add("Select District");
                    break;

                case KEY_TEHSIL:
                    selectQuery = "SELECT  DISTINCT " + KEY_TEHSIL_TEXT + " FROM " + TABLE_STATE_SEARCH
                            + " WHERE " + KEY_DISTRICT_TEXT + " = '" + text + "'";
                    list.add("Select Tehsil");
                    break;

            }


            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        switch (key) {

                            case KEY_STATE:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                                break;
                            case KEY_DISTRICT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                                break;
                            case KEY_TEHSIL:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                                break;

                        }
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list;
    }


    @SuppressLint("Range")
    public String getProjLoginValue(String key, String text) {
        String result = null;

        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            switch (key) {

                case KEY_PROJ_ID:
                    selectQuery = "SELECT  * FROM " + TABLE_LOGIN_SELECTION + " WHERE " + KEY_PROJ_NM + " = '" + text + "'";
                    break;
                case KEY_LOGIN_ID:
                    selectQuery = "SELECT  * FROM " + TABLE_LOGIN_SELECTION + " WHERE " + KEY_LOGIN_NM + " = '" + text + "'";
                    break;

            }
            c = db.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {

                if (c.moveToFirst()) {
                    switch (key) {

                        case KEY_PROJ_ID:
                            result = c.getString(c.getColumnIndex(KEY_PROJ_NO));
                            break;
                        case KEY_LOGIN_ID:
                            result = c.getString(c.getColumnIndex(KEY_LOGIN_NO));
                            break;

                    }
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public String getStateDistrictValue(String key, String text) {
        String result = null;

        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            switch (key) {

                case KEY_STATE:
                    selectQuery = "SELECT  * FROM " + TABLE_STATE_SEARCH + " WHERE " + KEY_STATE_TEXT + " = '" + text + "'";
                    break;
                case KEY_DISTRICT:
                    selectQuery = "SELECT  * FROM " + TABLE_STATE_SEARCH + " WHERE " + KEY_DISTRICT_TEXT + " = '" + text + "'";
                    break;

            }
            c = db.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {

                if (c.moveToFirst()) {
                    switch (key) {

                        case KEY_STATE:
                            result = c.getString(c.getColumnIndex(KEY_STATE));
                            break;
                        case KEY_DISTRICT:
                            result = c.getString(c.getColumnIndex(KEY_DISTRICT));
                            break;

                    }
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public ArrayList<ItemNameBean> getItemData() {

        ItemNameBean itemNameBean = new ItemNameBean();
        ArrayList<ItemNameBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_DASHBOARD;


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        itemNameBean = new ItemNameBean();

                        itemNameBean.setItem_id(cursor.getString(cursor.getColumnIndex(KEY_PROCESS_NO)));
                        itemNameBean.setItem_name(cursor.getString(cursor.getColumnIndex(KEY_PROCESS_NM)));

                        list_document.add(itemNameBean);

                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<String> getStateDistrictList(String key, String text) {

        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();


        list.clear();

        db.beginTransaction();
        try {

            String selectQuery = null;

            switch (key) {

                case KEY_STATE_TEXT:
                    selectQuery = "SELECT  DISTINCT " + KEY_STATE_TEXT + " FROM " + TABLE_STATE_SEARCH;
                    list.add("Select State");
                    break;
                case KEY_DISTRICT_TEXT:

                    selectQuery = "SELECT  DISTINCT " + KEY_DISTRICT_TEXT + " FROM " + TABLE_STATE_SEARCH
                            + " WHERE " + KEY_STATE_TEXT + " = '" + text + "'";
                    list.add("Select District");
                    break;

            }

            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        switch (key) {

                            case KEY_STATE_TEXT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                                break;
                            case KEY_DISTRICT_TEXT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                                break;
                        }
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list;
    }

    @SuppressLint("Range")
    public RegistrationBean getRegistrationData(String user_id, String enq_docno) {

        RegistrationBean registrationBean = new RegistrationBean();

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_REGISTRATION + " WHERE " + KEY_PERNR + " = '" + user_id + "'" + "AND " + KEY_ENQ_DOC + " = '" + enq_docno + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        registrationBean = new RegistrationBean();

                        registrationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        registrationBean.setProject_no(cursor.getString(cursor.getColumnIndex(KEY_PROJ_NO)));
                        registrationBean.setLogin_no(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_NO)));
                        registrationBean.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                        registrationBean.setLat(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                        registrationBean.setLng(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                        registrationBean.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        registrationBean.setFather_name(cursor.getString(cursor.getColumnIndex(KEY_FATH_NAME)));
                        registrationBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        registrationBean.setStatetxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        registrationBean.setCity(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        registrationBean.setCitytxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                        registrationBean.setTehsil(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                        registrationBean.setVillage(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE)));
                        registrationBean.setContact_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        registrationBean.setAadhar_no(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));
                        registrationBean.setBank_name(cursor.getString(cursor.getColumnIndex(KEY_BANK_NAME)));
                        registrationBean.setBank_acc_no(cursor.getString(cursor.getColumnIndex(KEY_BANK_ACC_NO)));
                        registrationBean.setAccount_type(cursor.getString(cursor.getColumnIndex(KEY_ACC_TYPE)));
                        registrationBean.setBranch_name(cursor.getString(cursor.getColumnIndex(KEY_BRANCH_NAME)));
                        registrationBean.setIfsc_code(cursor.getString(cursor.getColumnIndex(KEY_IFSC_CODE)));
                        registrationBean.setAmount(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
                        registrationBean.setPdf(cursor.getString(cursor.getColumnIndex(KEY_PDF)));
                        registrationBean.setSync(cursor.getString(cursor.getColumnIndex(KEY_SYNC)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return registrationBean;
    }

    @SuppressLint("Range")
    public ArrayList<InstallationListBean> getInstallationListData(String userid) {
        InstallationListBean installationBean = new InstallationListBean();
        ArrayList<InstallationListBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_INSTALLATION_LIST + " WHERE " + KEY_PERNR + " = '" + userid + "'" + " AND " + KEY_SYNC + " = '" + "" + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationBean = new InstallationListBean();
                        installationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        installationBean.setEnqdoc(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setBillno(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_KUNNR)));
                        installationBean.setGstbillno(cursor.getString(cursor.getColumnIndex(KEY_GST_BILL_NO)));
                        installationBean.setBilldate(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                        installationBean.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationBean.setFather_name(cursor.getString(cursor.getColumnIndex(KEY_FATH_NAME)));
                        installationBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        installationBean.setStatetxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        installationBean.setCity(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        installationBean.setCitytxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                        installationBean.setTehsil(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                        installationBean.setVillage(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE)));
                        installationBean.setContact_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        installationBean.setController(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER)));
                        installationBean.setPump(cursor.getString(cursor.getColumnIndex(KEY_PUMP)));
                        installationBean.setSimno(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        installationBean.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        installationBean.setProjectno(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        installationBean.setLoginno(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        installationBean.setModuleqty(cursor.getString(cursor.getColumnIndex(KEY_ADD5)));
                        installationBean.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_ADD6)));
                        installationBean.setMotor(cursor.getString(cursor.getColumnIndex(KEY_MOTOR)));
                        installationBean.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        installationBean.setDispdate(cursor.getString(cursor.getColumnIndex(KEY_ADD7)));
                        installationBean.setSync(cursor.getString(cursor.getColumnIndex(KEY_SYNC)));
                        installationBean.setSet_matno(cursor.getString(cursor.getColumnIndex(KEY_SET_MATNO)));
                        installationBean.setSimha2(cursor.getString(cursor.getColumnIndex(KEY_SIMHA2)));
                        installationBean.setCUS_CONTACT_NO(cursor.getString(cursor.getColumnIndex(KEY_CUS_CONTACT_NO)));
                        installationBean.setNoOfModule(cursor.getString(cursor.getColumnIndex(KEY_PANEL_MODULE_SER_NO)));
                        installationBean.setHP(cursor.getString(cursor.getColumnIndex(KEY_HP)));
                        installationBean.setPump_load(cursor.getString(cursor.getColumnIndex(KEY_PUMPLoad)));
                        installationBean.setAadhar_no(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));
                        installationBean.setAadhar_mobile(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_MOBILE)));
                        list_document.add(installationBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return list_document;
    }
    @SuppressLint("Range")
    public ArrayList<unloadingDataBean> getUnloadingData(String billno) {
        unloadingDataBean unloadingBean = new unloadingDataBean();
        ArrayList<unloadingDataBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_UNLOADING_FORM_DATA + " WHERE " + KEY_BILL_NO + " = '" + billno + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        unloadingBean = new unloadingDataBean();
                        unloadingBean.setPanel_module_qty(cursor.getString(cursor.getColumnIndex(KEY_MODULE_QTY)));
                        unloadingBean.setPanel_values(cursor.getString(cursor.getColumnIndex(KEY_MODULE_VALUES)));
                        unloadingBean.setPump_serial_no(cursor.getString(cursor.getColumnIndex(KEY_PUMP_SERIAL_NO)));
                        unloadingBean.setMotor_serial_no(cursor.getString(cursor.getColumnIndex(KEY_MOTOR_SERIAL_NO)));
                        unloadingBean.setController_serial_no(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER_SERIAL_NO)));
                        unloadingBean.setMaterial_status(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_STATUS)));
                        unloadingBean.setRemark(cursor.getString(cursor.getColumnIndex(KEY_UNLOADING_REAMRK)));
                        unloadingBean.setBill_no(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        list_document.add(unloadingBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<InstallationListBean> getUnloadInstallationListData(String userid) {
        InstallationListBean installationBean = new InstallationListBean();
        ArrayList<InstallationListBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_INSTALLATION_UNLOAD_LIST + " WHERE " + KEY_PERNR + " = '" + userid + "'" + " AND " + KEY_SYNC + " = '" + "" + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
       //     Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationBean = new InstallationListBean();
                        installationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        installationBean.setEnqdoc(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setBillno(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_KUNNR)));
                        installationBean.setGstbillno(cursor.getString(cursor.getColumnIndex(KEY_GST_BILL_NO)));
                        installationBean.setBilldate(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                        installationBean.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationBean.setFather_name(cursor.getString(cursor.getColumnIndex(KEY_FATH_NAME)));
                        installationBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        installationBean.setStatetxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        installationBean.setCity(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        installationBean.setCitytxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                        installationBean.setTehsil(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                        installationBean.setVillage(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE)));
                        installationBean.setContact_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        installationBean.setController(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER)));
                        installationBean.setPump(cursor.getString(cursor.getColumnIndex(KEY_PUMP)));
                        installationBean.setSimno(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        installationBean.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        installationBean.setProjectno(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        installationBean.setLoginno(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        installationBean.setModuleqty(cursor.getString(cursor.getColumnIndex(KEY_ADD5)));
                        installationBean.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_ADD6)));
                        installationBean.setMotor(cursor.getString(cursor.getColumnIndex(KEY_MOTOR)));
                        installationBean.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        installationBean.setDispdate(cursor.getString(cursor.getColumnIndex(KEY_ADD7)));
                        installationBean.setSync(cursor.getString(cursor.getColumnIndex(KEY_SYNC)));
                        installationBean.setSet_matno(cursor.getString(cursor.getColumnIndex(KEY_SET_MATNO)));
                        installationBean.setSimha2(cursor.getString(cursor.getColumnIndex(KEY_SIMHA2)));
                        installationBean.setCUS_CONTACT_NO(cursor.getString(cursor.getColumnIndex(KEY_CUS_CONTACT_NO)));
                        installationBean.setNoOfModule(cursor.getString(cursor.getColumnIndex(KEY_PANEL_MODULE_SER_NO)));
                        installationBean.setHP(cursor.getString(cursor.getColumnIndex(KEY_HP)));
                        installationBean.setPump_ser(cursor.getString(cursor.getColumnIndex(KEY_PUMP_SERIAL_NO)));
                        installationBean.setPump_load(cursor.getString(cursor.getColumnIndex(KEY_PUMPLoad)));
                        list_document.add(installationBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<InstallationOfflineBean> getInstallationOfflineListData(String userid) {
        InstallationOfflineBean installationOfflineBean = new InstallationOfflineBean();
        ArrayList<InstallationOfflineBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_INSTALLATION_OFFLINE_LIST + " WHERE " + KEY_PERNR + " = '" + userid + "'" + "AND " + KEY_SYNC + " = '" + "" + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationOfflineBean = new InstallationOfflineBean();
                        installationOfflineBean.setVbeln(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationOfflineBean.setUserID(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        installationOfflineBean.setGstInvNo(cursor.getString(cursor.getColumnIndex(KEY_GST_BILL_NO)));
                        installationOfflineBean.setFkdat(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                        installationOfflineBean.setDispatchDate(cursor.getString(cursor.getColumnIndex(KEY_DISPATCH_DATE)));
                        installationOfflineBean.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
                        installationOfflineBean.setName(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationOfflineBean.setRegio(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        installationOfflineBean.setMobile(cursor.getString(cursor.getColumnIndex(KEY_MOBILE)));
                        installationOfflineBean.setContactNo(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        installationOfflineBean.setCityc(cursor.getString(cursor.getColumnIndex(KEY_CITY_CODE)));
                        installationOfflineBean.setOrt02(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                        installationOfflineBean.setStras(cursor.getString(cursor.getColumnIndex(KEY_STREET)));
                        installationOfflineBean.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        installationOfflineBean.setRegioTxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        installationOfflineBean.setCitycTxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        installationOfflineBean.setPumpSernr(cursor.getString(cursor.getColumnIndex(KEY_PUMP)));
                        installationOfflineBean.setControllerSernr(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER)));
                        installationOfflineBean.setControllerMatno(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER_MAT_NO)));
                        installationOfflineBean.setSetMatno(cursor.getString(cursor.getColumnIndex(KEY_SET_MATNO)));
                        installationOfflineBean.setMotorSernr(cursor.getString(cursor.getColumnIndex(KEY_MOTOR)));
                        installationOfflineBean.setSimha2(cursor.getString(cursor.getColumnIndex(KEY_SIMHA2)));
                        installationOfflineBean.setSimno(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        installationOfflineBean.setSimmob(cursor.getString(cursor.getColumnIndex(KEY_SIMMOB)));
                        installationOfflineBean.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_ADD6)));
                        installationOfflineBean.setProjectNo(cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NO)));
                        installationOfflineBean.setProcessNo(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        installationOfflineBean.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        installationOfflineBean.setModuleQty(cursor.getString(cursor.getColumnIndex(KEY_ADD5)));
                        installationOfflineBean.setSync(cursor.getString(cursor.getColumnIndex(KEY_SYNC)));
                        list_document.add(installationOfflineBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<SubmitOfflineDataInput> getInstallationOfflineSubmittedListData() {
        SubmitOfflineDataInput submitOfflineDataInput = new SubmitOfflineDataInput();
        ArrayList<SubmitOfflineDataInput> submitOfflineDataInputArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_OFFLINE_SUBMITTED_LIST;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        submitOfflineDataInput = new SubmitOfflineDataInput();

                        submitOfflineDataInput.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_BILL_NO)));
                        submitOfflineDataInput.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_BENEFICIARY)));
                        submitOfflineDataInput.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_CUSTOMER_NAME)));
                        submitOfflineDataInput.setProjectNo(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_PROJECT_NO)));
                        submitOfflineDataInput.setUserId(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_USERID)));
                        submitOfflineDataInput.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_REGISNO)));
                        submitOfflineDataInput.setOffPhoto(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_OFFPHOTO)));
                        submitOfflineDataInputArrayList.add(submitOfflineDataInput);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return submitOfflineDataInputArrayList;
    }

    @SuppressLint("Range")
    public InstallationOfflineBean getInstallationOfflineData(String billno) {
        InstallationOfflineBean installationOfflineBean = null;
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_INSTALLATION_OFFLINE_LIST + " WHERE " + KEY_BILL_NO + " = '" + billno + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    installationOfflineBean = new InstallationOfflineBean();

                    installationOfflineBean.setVbeln(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                    installationOfflineBean.setUserID(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    installationOfflineBean.setGstInvNo(cursor.getString(cursor.getColumnIndex(KEY_GST_BILL_NO)));
                    installationOfflineBean.setFkdat(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                    installationOfflineBean.setDispatchDate(cursor.getString(cursor.getColumnIndex(KEY_DISPATCH_DATE)));
                    installationOfflineBean.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
                    installationOfflineBean.setName(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                    installationOfflineBean.setRegio(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                    installationOfflineBean.setMobile(cursor.getString(cursor.getColumnIndex(KEY_MOBILE)));
                    installationOfflineBean.setContactNo(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                    installationOfflineBean.setCityc(cursor.getString(cursor.getColumnIndex(KEY_CITY_CODE)));
                    installationOfflineBean.setOrt02(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                    installationOfflineBean.setStras(cursor.getString(cursor.getColumnIndex(KEY_STREET)));
                    installationOfflineBean.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                    installationOfflineBean.setRegioTxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                    installationOfflineBean.setCitycTxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                    installationOfflineBean.setPumpSernr(cursor.getString(cursor.getColumnIndex(KEY_PUMP)));
                    installationOfflineBean.setControllerSernr(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER)));
                    installationOfflineBean.setControllerMatno(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER_MAT_NO)));
                    installationOfflineBean.setSetMatno(cursor.getString(cursor.getColumnIndex(KEY_SET_MATNO)));
                    installationOfflineBean.setMotorSernr(cursor.getString(cursor.getColumnIndex(KEY_MOTOR)));
                    installationOfflineBean.setSimha2(cursor.getString(cursor.getColumnIndex(KEY_SIMHA2)));
                    installationOfflineBean.setSimno(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                    installationOfflineBean.setSimmob(cursor.getString(cursor.getColumnIndex(KEY_SIMMOB)));
                    installationOfflineBean.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_ADD6)));
                    installationOfflineBean.setProjectNo(cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NO)));
                    installationOfflineBean.setProcessNo(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                    installationOfflineBean.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                    installationOfflineBean.setModuleQty(cursor.getString(cursor.getColumnIndex(KEY_ADD5)));
                    installationOfflineBean.setSync(cursor.getString(cursor.getColumnIndex(KEY_SYNC)));
                    cursor.moveToNext();
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return installationOfflineBean;
    }

    @SuppressLint("Range")
    public SubmitOfflineDataInput getInstallationOfflineSubmittedData(String billNo) {
        SubmitOfflineDataInput submitOfflineDataInput = new SubmitOfflineDataInput();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_OFFLINE_SUBMITTED_LIST + " WHERE " + KEY_OFFLINE_BILL_NO + " = '" + billNo + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    submitOfflineDataInput = new SubmitOfflineDataInput();
                    submitOfflineDataInput.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_BILL_NO)));
                    submitOfflineDataInput.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_BENEFICIARY)));
                    submitOfflineDataInput.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_CUSTOMER_NAME)));
                    submitOfflineDataInput.setProjectNo(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_PROJECT_NO)));
                    submitOfflineDataInput.setUserId(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_USERID)));
                    submitOfflineDataInput.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_REGISNO)));
                    submitOfflineDataInput.setOffPhoto(cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_OFFPHOTO)));
                    cursor.moveToNext();
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return submitOfflineDataInput;
    }

    @SuppressLint("Range")
    public ArrayList<SiteAuditListBean> getAuditSiteListData(String userid) {

        SiteAuditListBean installationBean = new SiteAuditListBean();
        ArrayList<SiteAuditListBean> list_document = new ArrayList<>();
        list_document.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_AUDITSITE_LIST + " WHERE " + KEY_PERNR + " = '" + userid + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationBean = new SiteAuditListBean();

                        installationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        installationBean.setEnqdoc(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setBillno(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setGstbillno(cursor.getString(cursor.getColumnIndex(KEY_GST_BILL_NO)));
                        installationBean.setBilldate(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                        installationBean.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationBean.setFather_name(cursor.getString(cursor.getColumnIndex(KEY_FATH_NAME)));
                        installationBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        installationBean.setStatetxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        installationBean.setCity(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        installationBean.setCitytxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                        installationBean.setContact_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        installationBean.setRegisno(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        installationBean.setProjectno(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        installationBean.setBeneficiary(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        installationBean.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        installationBean.setVendor(cursor.getString(cursor.getColumnIndex(KEY_Vendor)));
                        installationBean.setDispdate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));


                        list_document.add(installationBean);

                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<RejectListBean> getRejectionListData() {
        RejectListBean installationBean = new RejectListBean();
        ArrayList<RejectListBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_REJECTION_LIST;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationBean = new RejectListBean();
                        installationBean.setBillno(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        installationBean.setBenno(cursor.getString(cursor.getColumnIndex(KEY_BEN_NO)));
                        installationBean.setRegno(cursor.getString(cursor.getColumnIndex(KEY_REG_NO)));
                        installationBean.setCustnm(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1)));
                        installationBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO2)));
                        installationBean.setPhoto3(cursor.getString(cursor.getColumnIndex(KEY_PHOTO3)));
                        installationBean.setPhoto4(cursor.getString(cursor.getColumnIndex(KEY_PHOTO4)));
                        installationBean.setPhoto5(cursor.getString(cursor.getColumnIndex(KEY_PHOTO5)));
                        installationBean.setPhoto6(cursor.getString(cursor.getColumnIndex(KEY_PHOTO6)));
                        installationBean.setPhoto7(cursor.getString(cursor.getColumnIndex(KEY_PHOTO7)));
                        installationBean.setPhoto8(cursor.getString(cursor.getColumnIndex(KEY_PHOTO8)));
                        installationBean.setPhoto9(cursor.getString(cursor.getColumnIndex(KEY_PHOTO9)));
                        installationBean.setPhoto10(cursor.getString(cursor.getColumnIndex(KEY_PHOTO10)));
                        installationBean.setPhoto11(cursor.getString(cursor.getColumnIndex(KEY_PHOTO11)));
                        installationBean.setPhoto12(cursor.getString(cursor.getColumnIndex(KEY_PHOTO12)));
                        installationBean.setRemark1(cursor.getString(cursor.getColumnIndex(KEY_REMARK1)));
                        installationBean.setRemark2(cursor.getString(cursor.getColumnIndex(KEY_REMARK2)));
                        installationBean.setRemark3(cursor.getString(cursor.getColumnIndex(KEY_REMARK3)));
                        installationBean.setRemark4(cursor.getString(cursor.getColumnIndex(KEY_REMARK4)));
                        installationBean.setRemark5(cursor.getString(cursor.getColumnIndex(KEY_REMARK5)));
                        installationBean.setRemark6(cursor.getString(cursor.getColumnIndex(KEY_REMARK6)));
                        installationBean.setRemark7(cursor.getString(cursor.getColumnIndex(KEY_REMARK7)));
                        installationBean.setRemark8(cursor.getString(cursor.getColumnIndex(KEY_REMARK8)));
                        installationBean.setRemark9(cursor.getString(cursor.getColumnIndex(KEY_REMARK9)));
                        installationBean.setRemark10(cursor.getString(cursor.getColumnIndex(KEY_REMARK10)));
                        installationBean.setRemark11(cursor.getString(cursor.getColumnIndex(KEY_REMARK11)));
                        installationBean.setRemark12(cursor.getString(cursor.getColumnIndex(KEY_REMARK12)));


                        list_document.add(installationBean);

                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<SurveyListBean> getSurveyListData(String userid) {
        SurveyListBean installationBean = new SurveyListBean();
        ArrayList<SurveyListBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_SURVEY_LIST + " WHERE " + KEY_PERNR + " = '" + userid + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationBean = new SurveyListBean();
                        installationBean.setBen_id(cursor.getString(cursor.getColumnIndex(KEY_ENQ_DOC)));
                        installationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        installationBean.setCustnam(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationBean.setContctno(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        installationBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        installationBean.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        installationBean.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        installationBean.setRegino(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        list_document.add(installationBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<RegistrationBean> getRegistrationData() {
        RegistrationBean registrationBean = new RegistrationBean();
        ArrayList<RegistrationBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_REGISTRATION + " WHERE " + KEY_SYNC + " = '" + "1" + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        registrationBean = new RegistrationBean();
                        registrationBean.setEnqdoc(cursor.getString(cursor.getColumnIndex(KEY_ENQ_DOC)));
                        registrationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        registrationBean.setProject_no(cursor.getString(cursor.getColumnIndex(KEY_PROJ_NO)));
                        registrationBean.setLogin_no(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_NO)));
                        registrationBean.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                        registrationBean.setLat(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                        registrationBean.setLng(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                        registrationBean.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        registrationBean.setFather_name(cursor.getString(cursor.getColumnIndex(KEY_FATH_NAME)));
                        registrationBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        registrationBean.setStatetxt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        registrationBean.setCity(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        registrationBean.setCitytxt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                        registrationBean.setTehsil(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                        registrationBean.setVillage(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE)));
                        registrationBean.setContact_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        registrationBean.setAadhar_no(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));
                        registrationBean.setBank_name(cursor.getString(cursor.getColumnIndex(KEY_BANK_NAME)));
                        registrationBean.setBank_acc_no(cursor.getString(cursor.getColumnIndex(KEY_BANK_ACC_NO)));
                        registrationBean.setAccount_type(cursor.getString(cursor.getColumnIndex(KEY_ACC_TYPE)));
                        registrationBean.setBranch_name(cursor.getString(cursor.getColumnIndex(KEY_BRANCH_NAME)));
                        registrationBean.setIfsc_code(cursor.getString(cursor.getColumnIndex(KEY_IFSC_CODE)));
                        registrationBean.setAmount(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
                        registrationBean.setPdf(cursor.getString(cursor.getColumnIndex(KEY_PDF)));
                        registrationBean.setSync(cursor.getString(cursor.getColumnIndex(KEY_SYNC)));
                        list_document.add(registrationBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return list_document;
    }

    public void deleteLoginData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_LOGIN)) {
            db.delete(TABLE_LOGIN, null, null);
        }
    }

    public void deleteStateSearchHelpData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_STATE_SEARCH)) {
            db.delete(TABLE_STATE_SEARCH, null, null);
        }
    }

    public void deleteStateDistrictData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_STATE_DISTRICT)) {
            db.delete(TABLE_STATE_DISTRICT, null, null);
        }
    }

    public void deleteLoginSelecData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_LOGIN_SELECTION)) {
            db.delete(TABLE_LOGIN_SELECTION, null, null);
        }
    }

    public void deleteDashboardData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_DASHBOARD)) {
            db.delete(TABLE_DASHBOARD, null, null);
        }
    }

    public void deleteRegistrationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_REGISTRATION)) {
            db.delete(TABLE_REGISTRATION, null, null);
        }
    }

    public void deleteInstallationListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_LIST)) {
            db.delete(TABLE_INSTALLATION_LIST, null, null);
        }
    }

    public void deleteUnloadInstallationListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_UNLOAD_LIST)) {
            db.delete(TABLE_INSTALLATION_UNLOAD_LIST, null, null);
        }
    }

    public void deleteInstallationOfflineListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_OFFLINE_LIST)) {
            db.delete(TABLE_INSTALLATION_OFFLINE_LIST, null, null);
        }
    }

    public void deleteOfflineSubmittedData(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_OFFLINE_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_OFFLINE_SUBMITTED_LIST)) {
            int value = db.delete(TABLE_OFFLINE_SUBMITTED_LIST, where, null);
            Log.i("DeleteValue", value + "");
        }

        getInstallationOfflineSubmittedListData();
    }

    public void deleteInstallationListData1(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_LIST)) {
            db.delete(TABLE_INSTALLATION_LIST, where, null);
        }
    }

    public void deleteUnloadInstallationListData1(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_UNLOAD_LIST)) {
            db.delete(TABLE_INSTALLATION_UNLOAD_LIST, where, null);
        }
    }

    public void deleteAuditSiteListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_AUDITSITE_LIST)) {
            db.delete(TABLE_AUDITSITE_LIST, null, null);
        }
    }

    public void deleteAuditSiteListData1(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_AUDITSITE_LIST)) {
            db.delete(TABLE_AUDITSITE_LIST, where, null);
        }
    }

    public void deleteAuditImages(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_SITE_AUDIT_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_SITE_AUDIT)) {
            db.delete(TABLE_SITE_AUDIT, where, null);
        }
    }


    public void deleteRejectListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_REJECTION_LIST)) {
            db.delete(TABLE_REJECTION_LIST, null, null);
        }
    }

    public void deleteSurveyListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_SURVEY_LIST)) {
            db.delete(TABLE_SURVEY_LIST, null, null);
        }
    }

    public void deleteInstallationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_PUMP_DATA)) {
            db.delete(TABLE_INSTALLATION_PUMP_DATA, null, null);
        }
    }

    public void deleteKusumCSurveyFromSpecificItem(String applicationNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_KUSUMCSURVEYFORM)) {
       //  String Query = "DELETE FROM " + TABLE_KUSUMCSURVEYFORM + " WHERE " + DatabaseHelper.KEY_APPLICANT_NO + " = '" + applicationNumber + "'";

            String where = "";
            where =  DatabaseHelper.KEY_APPLICANT_NO + " = '" + applicationNumber + "'";

            db.delete(TABLE_KUSUMCSURVEYFORM, where, null);
        }
    }

    public void deleteKusumCSurveyFrom() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_KUSUMCSURVEYFORM)) {

            db.delete(TABLE_KUSUMCSURVEYFORM, null, null);
        }
    }
    public void deleteBeneficiaryregistration() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_BENEFICIARY_REGISTRATION)) {
            db.delete(TABLE_BENEFICIARY_REGISTRATION, null, null);
        }
        if (CustomUtility.doesTableExist(db, TABLE_BENEFICIARY_IMAGE_DATA)) {
            db.delete(TABLE_BENEFICIARY_IMAGE_DATA, null, null);
        }
    }

    public void deleteAuditData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_AUDIT_PUMP_DATA)) {
            db.delete(TABLE_AUDIT_PUMP_DATA, null, null);
        }
    }

    public void deleteSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_SURVEY_PUMP_DATA)) {
            db.delete(TABLE_SURVEY_PUMP_DATA, null, null);
        }
    }

    public void deleteInstallationData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_PUMP_DATA)) {
            db.delete(TABLE_INSTALLATION_PUMP_DATA, where, null);
        }
    }

    public void deleteDAMAGEMISSData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_DAMAGE_MISS_COMPLAIN)) {
            db.delete(TABLE_DAMAGE_MISS_COMPLAIN, where, null);
        }
    }

    public void deleteAuditData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_AUDIT_PUMP_DATA)) {
            db.delete(TABLE_AUDIT_PUMP_DATA, where, null);
        }
    }

    public void deleteSurveyData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_SURVEY_PUMP_DATA)) {
            db.delete(TABLE_SURVEY_PUMP_DATA, where, null);
        }
    }

    public void deleteRegistrationData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_ENQ_DOC + "='" + value + "'";
        if (CustomUtility.doesTableExist(db, TABLE_REGISTRATION)) {
            db.delete(TABLE_REGISTRATION, where, null);
        }
    }

    public void deleteInstallationImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_IMAGE_DATA)) {
            db.delete(TABLE_INSTALLATION_IMAGE_DATA, null, null);
        }
    }

    public void deleteRejectedInstallationImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_IMAGE_DATA)) {
            db.delete(TABLE_INSTALLATION_IMAGE_DATA, null, null);
        }
    }
    public void deleteBeneficiaryImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_BENEFICIARY_IMAGE_DATA)) {
            db.delete(TABLE_BENEFICIARY_IMAGE_DATA, null, null);
        }
    }
    public void deleteSiteAuditImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_SITE_AUDIT)) {
            db.delete(TABLE_SITE_AUDIT, null, null);
        }
    }

    public void deletekusumCImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_KusumCImages)) {
            db.delete(TABLE_KusumCImages, null, null);
        }
    }

    public void deleteUnloadingImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_UNLOADING_IMAGE_DATA)) {
            db.delete(TABLE_UNLOADING_IMAGE_DATA, null, null);
        }
    }

    public void deleteUnloadingFormData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_UNLOADING_FORM_DATA)) {
            db.delete(TABLE_UNLOADING_FORM_DATA, null, null);
        }
    }

    @SuppressLint("Range")
    public InstallationBean getInstallationData(String user_id, String bill_no) {
        InstallationBean installationBean = new InstallationBean();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_INSTALLATION_PUMP_DATA + " WHERE " + KEY_PERNR + " = '" + user_id + "'" + "AND " + KEY_BILL_NO + " = '" + bill_no + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        installationBean = new InstallationBean();
                        installationBean.setInst_bill_no(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));

                        installationBean.setBill_date(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                        installationBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        installationBean.setProject_no(cursor.getString(cursor.getColumnIndex(KEY_PROJ_NO)));
                        installationBean.setLogin_no(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_NO)));
                        installationBean.setInst_date(cursor.getString(cursor.getColumnIndex(KEY_INST_DATE)));
                        installationBean.setRms_data_status(cursor.getString(cursor.getColumnIndex(KEY_RMS_DATA_STATUS)));
                        installationBean.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                        installationBean.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                        installationBean.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        installationBean.setFathers_name(cursor.getString(cursor.getColumnIndex(KEY_FATH_NAME)));
                        installationBean.setState_ins_id(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        installationBean.setState_ins_txt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                        installationBean.setDistrict_ins_id(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        installationBean.setDistrict_ins_txt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                        installationBean.setMobile_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        installationBean.setAddress_ins(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        installationBean.setTehsil_ins(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                        installationBean.setVillage_ins(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE)));
                        installationBean.setSolarpanel_wattage(cursor.getString(cursor.getColumnIndex(KEY_SOLAR_PANEL_WATT)));
                        installationBean.setSolarpanel_stand_ins_quantity(cursor.getString(cursor.getColumnIndex(KEY_SOLAR_PANEL_STAND_INSTALL_QTY)));
                        installationBean.setInst_hp(cursor.getString(cursor.getColumnIndex(KEY_HP)));
                        installationBean.setTotal_watt(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_WATT)));
                        installationBean.setNo_of_module_qty(cursor.getString(cursor.getColumnIndex(KEY_PANEL_MODULE_QTY)));
                        installationBean.setNo_of_module_value(cursor.getString(cursor.getColumnIndex(KEY_PANEL_MODULE_SER_NO)));
                        installationBean.setModule_total_plate_watt(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_PLATES_PER_WATT)));
                        installationBean.setSolar_motor_model_details(cursor.getString(cursor.getColumnIndex(KEY_MOTOR_MODAL_DETAILS)));
                        installationBean.setSmmd_sno(cursor.getString(cursor.getColumnIndex(KEY_MOTOR_MODAL_SER_NO)));
                        installationBean.setSplar_pump_model_details(cursor.getString(cursor.getColumnIndex(KEY_PUMP_MODAL_DETAILS)));
                        installationBean.setSpmd_sno(cursor.getString(cursor.getColumnIndex(KEY_PUMP_MODAL_SER_NO)));
                        installationBean.setSolar_controller_model(cursor.getString(cursor.getColumnIndex(KEY_CONTROLER_MODAL_DETAILS)));
                        installationBean.setScm_sno(cursor.getString(cursor.getColumnIndex(KEY_CONTROLER_MODAL_SER_NO)));
                        installationBean.setSimoprator(cursor.getString(cursor.getColumnIndex(KEY_SIM_OPERATOR_TYPE)));
                        installationBean.setConntype(cursor.getString(cursor.getColumnIndex(KEY_CONNECTION_TYPE)));
                        installationBean.setSimcard_num(cursor.getString(cursor.getColumnIndex(KEY_SIM_NO)));
                        installationBean.setRegis_no(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        installationBean.setDelay_reason(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        installationBean.setMake_ins(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        installationBean.setBeneficiaryNo(cursor.getString(cursor.getColumnIndex(KEY_BENEFICIARY_NO)));
                        installationBean.setPumpLoad(cursor.getString(cursor.getColumnIndex(KEY_PUMPLoad)));
                        installationBean.setAadhar_no(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));
                        Log.e("aadhar==>",cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return installationBean;
    }


    @SuppressLint("Range")
    public KusumCSurveyBean getKusumCSurvey(String bill_no) {
        KusumCSurveyBean kusumCSurveyBean = new KusumCSurveyBean();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_KUSUMCSURVEYFORM + " WHERE " + KEY_APPLICANT_NO + " = '" + bill_no + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        kusumCSurveyBean = new KusumCSurveyBean();

                        kusumCSurveyBean.setCATEGORY(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                        kusumCSurveyBean.setWATER_SOURCE(cursor.getString(cursor.getColumnIndex(KEY_WATER_SOURCE)));
                        kusumCSurveyBean.setINTERNET_TYPE(cursor.getString(cursor.getColumnIndex(KEY_INTERNET_TYPE)));
                        kusumCSurveyBean.setCROP_PATTERN(cursor.getString(cursor.getColumnIndex(KEY_CROP_PATTERN)));
                        kusumCSurveyBean.setTYPE_OF_IRIGATN(cursor.getString(cursor.getColumnIndex(KEY_TYPE_OF_IRIGATN)));
                        kusumCSurveyBean.setSHADOW_FREE_LAND(cursor.getString(cursor.getColumnIndex(KEY_SHADOW_FREE_LAND)));
                        kusumCSurveyBean.setELEC_CON(cursor.getString(cursor.getColumnIndex(KEY_ELEC_CON)));
                        kusumCSurveyBean.setELEC_IDEN_NO(cursor.getString(cursor.getColumnIndex(KEY_ELEC_IDEN_NO)));

                        kusumCSurveyBean.setPUMP_TYPE(cursor.getString(cursor.getColumnIndex(KEY_PUMP_TYPE)));
                        kusumCSurveyBean.setELEC_CONN_RAT(cursor.getString(cursor.getColumnIndex(KEY_ELEC_RATING)));
                        kusumCSurveyBean.setPUMP_SET_RATING(cursor.getString(cursor.getColumnIndex(KEY_PUMP_SET_RATING)));
                        kusumCSurveyBean.setPUMP_MAKE(cursor.getString(cursor.getColumnIndex(KEY_PUMP_MAKE)));
                        kusumCSurveyBean.setCABLE_DET_MAKE(cursor.getString(cursor.getColumnIndex(KEY_CABLE_DETAIL_WITH_MAKE)));
                        kusumCSurveyBean.setPHASE_VOL_V1(cursor.getString(cursor.getColumnIndex(KEY_PHASE_VOL_V1)));
                        kusumCSurveyBean.setPHASE_VOL_V2(cursor.getString(cursor.getColumnIndex(KEY_PHASE_VOL_V2)));
                        kusumCSurveyBean.setPHASE_VOL_V3(cursor.getString(cursor.getColumnIndex(KEY_PHASE_VOL_V3)));
                        kusumCSurveyBean.setLINE_VOL_V1(cursor.getString(cursor.getColumnIndex(KEY_LINE_VOL_V1)));
                        kusumCSurveyBean.setLINE_VOL_V2(cursor.getString(cursor.getColumnIndex(KEY_LINE_VOL_V2)));
                        kusumCSurveyBean.setLINE_VOL_V3(cursor.getString(cursor.getColumnIndex(KEY_LINE_VOL_V3)));

                        kusumCSurveyBean.setLINE_CRNT_AMP1(cursor.getString(cursor.getColumnIndex(KEY_LINE_CRNT_AMP1)));
                        kusumCSurveyBean.setLINE_CRNT_AMP2(cursor.getString(cursor.getColumnIndex(KEY_LINE_CRNT_AMP2)));
                        kusumCSurveyBean.setLINE_CRNT_AMP3(cursor.getString(cursor.getColumnIndex(KEY_LINE_CRNT_AMP3)));
                        kusumCSurveyBean.setLINE_POWFACT_1(cursor.getString(cursor.getColumnIndex(KEY_LINE_POWFACT_1)));
                        kusumCSurveyBean.setLINE_POWFACT_2(cursor.getString(cursor.getColumnIndex(KEY_LINE_POWFACT_2)));
                        kusumCSurveyBean.setLINE_POWFACT_3(cursor.getString(cursor.getColumnIndex(KEY_LINE_POWFACT_3)));

                        kusumCSurveyBean.setBOREWELL_SIZE(cursor.getString(cursor.getColumnIndex(KEY_BOREWELL_SIZE)));
                        kusumCSurveyBean.setBOREWELL_DEPTH(cursor.getString(cursor.getColumnIndex(KEY_BOREWELL_DEPTH)));
                        kusumCSurveyBean.setDISTANCE(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        kusumCSurveyBean.setDIS_PUMP_LPM(cursor.getString(cursor.getColumnIndex(KEY_DIS_PUMP_LPM)));
                        kusumCSurveyBean.setDEL_PUMP_LPM(cursor.getString(cursor.getColumnIndex(KEY_DEL_PUMP_LPM)));
                        kusumCSurveyBean.setPUMP_SET_DEPTH(cursor.getString(cursor.getColumnIndex(KEY_PUMP_SET_DEPTH)));
                        kusumCSurveyBean.setPIPE_LEN_SIZE(cursor.getString(cursor.getColumnIndex(KEY_DEL_PIPE_LINE)));
                        kusumCSurveyBean.setDYNAMIC_HEAD(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_DYNAMIC_HEAD)));


                        kusumCSurveyBean.setTRANSF_RATING(cursor.getString(cursor.getColumnIndex(KEY_TRANSFORMER_RATING)));
                        kusumCSurveyBean.setSERVICE_LINE(cursor.getString(cursor.getColumnIndex(KEY_SERVICE_LINE)));
                        kusumCSurveyBean.setTHREE_PH_SUPPLY(cursor.getString(cursor.getColumnIndex(KEY_THREE_PHASE)));
                        kusumCSurveyBean.setELECTRIC_BILL(cursor.getString(cursor.getColumnIndex(KEY_ELECTRICITY_BILL)));
                        kusumCSurveyBean.setNEUTRL_GRID_AVBL(cursor.getString(cursor.getColumnIndex(KEY_NEUTRAL_AVAILABILITY)));
                        kusumCSurveyBean.setWATER_SOURC_LEN(cursor.getString(cursor.getColumnIndex(KEY_STURCTURE_WATER_SOURCE)));
                        kusumCSurveyBean.setDIST_FARMAR(cursor.getString(cursor.getColumnIndex(KEY_FEEDER_TO_FARMER)));
                        kusumCSurveyBean.setIFNO_REMARK(cursor.getString(cursor.getColumnIndex(KEY_ADDITIONAL_INFO)));

                        kusumCSurveyBean.setpowerInVolt(cursor.getString(cursor.getColumnIndex(KEY_POWER_IN_VOLT)));
                        kusumCSurveyBean.setExDischarge(cursor.getString(cursor.getColumnIndex(KEY_EXDISCHARGE)));
                        kusumCSurveyBean.setExDynamichead(cursor.getString(cursor.getColumnIndex(KEY_EXDYNAMIC)));
                        kusumCSurveyBean.setAadharNo(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));
                        kusumCSurveyBean.setAadharRegMob(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_MOBILE)));


                        kusumCSurveyBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1)));
                        kusumCSurveyBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO2)));
                        kusumCSurveyBean.setPhoto3(cursor.getString(cursor.getColumnIndex(KEY_PHOTO3)));
                        kusumCSurveyBean.setPhoto4(cursor.getString(cursor.getColumnIndex(KEY_PHOTO4)));
                        kusumCSurveyBean.setPhoto5(cursor.getString(cursor.getColumnIndex(KEY_PHOTO5)));
                        kusumCSurveyBean.setPhoto6(cursor.getString(cursor.getColumnIndex(KEY_PHOTO6)));

                        kusumCSurveyBean.setLAT(cursor.getString(cursor.getColumnIndex(KEY_LAT)));
                        kusumCSurveyBean.setLNG(cursor.getString(cursor.getColumnIndex(KEY_LNG)));
                        kusumCSurveyBean.setFREQ_HERTZ(cursor.getString(cursor.getColumnIndex(KEY_FREQ_HERTZ)));

                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return kusumCSurveyBean;
    }


    @SuppressLint("Range")
    public AuditSiteBean getAuditData(String user_id, String bill_no) {
        AuditSiteBean auditSiteBean = new AuditSiteBean();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_AUDIT_PUMP_DATA + " WHERE " + KEY_PERNR + " = '" + user_id + "'" + "AND " + KEY_BILL_NO + " = '" + bill_no + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        auditSiteBean = new AuditSiteBean();
                        auditSiteBean.setInst_bill_no(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        auditSiteBean.setBill_date(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
                        auditSiteBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        auditSiteBean.setProject_no(cursor.getString(cursor.getColumnIndex(KEY_PROJ_NO)));
                        auditSiteBean.setRegis_no(cursor.getString(cursor.getColumnIndex(KEY_REGISNO)));
                        auditSiteBean.setAud_date(cursor.getString(cursor.getColumnIndex(KEY_AUD_DATE)));
                        auditSiteBean.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                        auditSiteBean.setState_ins_id(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        auditSiteBean.setDistrict_ins_id(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        auditSiteBean.setAddress_ins(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        auditSiteBean.setMobile_no(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NO)));
                        auditSiteBean.setFound(cursor.getString(cursor.getColumnIndex(KEY_FOUND)));
                        auditSiteBean.setFound_rmk(cursor.getString(cursor.getColumnIndex(KEY_FOUND_RMK)));
                        auditSiteBean.setStruc_assem(cursor.getString(cursor.getColumnIndex(KEY_STRUCT)));
                        auditSiteBean.setStruc_assem_rmk(cursor.getString(cursor.getColumnIndex(KEY_STRUCT_RMK)));
                        auditSiteBean.setDrv_mount(cursor.getString(cursor.getColumnIndex(KEY_DRV_MOUNT)));
                        auditSiteBean.setDrv_mount_rmk(cursor.getString(cursor.getColumnIndex(KEY_DRV_MOUNT_RMK)));
                        auditSiteBean.setLa_earthing(cursor.getString(cursor.getColumnIndex(KEY_LA_EARTH)));
                        auditSiteBean.setLa_earthing_rmk(cursor.getString(cursor.getColumnIndex(KEY_LA_EARTH_RMK)));
                        auditSiteBean.setWrk_quality(cursor.getString(cursor.getColumnIndex(KEY_WRK_QLTY)));
                        auditSiteBean.setWrk_quality_rmk(cursor.getString(cursor.getColumnIndex(KEY_WRK_QLTY_RMK)));
                        auditSiteBean.setSite_art(cursor.getFloat(cursor.getColumnIndex(KEY_SITE_RAT)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return auditSiteBean;
    }

    @SuppressLint("Range")
    public SurveyBean getSurveyData(String user_id, String bill_no) {
        SurveyBean surveyBean = new SurveyBean();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_SURVEY_PUMP_DATA + " WHERE " + KEY_PERNR + " = '" + user_id + "'" + "AND " + KEY_BILL_NO + " = '" + bill_no + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        surveyBean = new SurveyBean();
                        surveyBean.setSurvy_bill_no(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        surveyBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        surveyBean.setProject_no(cursor.getString(cursor.getColumnIndex(KEY_PROJ_NO)));
                        surveyBean.setLogin_no(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_NO)));
                        surveyBean.setInst_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                        surveyBean.setInst_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                        surveyBean.setSpinner_water_resource(cursor.getString(cursor.getColumnIndex(KEY_WATER_RES)));
                        surveyBean.setBorewell_size(cursor.getString(cursor.getColumnIndex(KEY_BORWELL_SIZE)));
                        surveyBean.setBorwell_depth(cursor.getString(cursor.getColumnIndex(KEY_BORWELL_DEPTH)));
                        surveyBean.setCbl_length(cursor.getString(cursor.getColumnIndex(KEY_CBL_LEN)));
                        surveyBean.setSurf_head(cursor.getString(cursor.getColumnIndex(KEY_SURF_HEAD)));
                        surveyBean.setLen_dia_dis_pip(cursor.getString(cursor.getColumnIndex(KEY_LEN_DIA_PIP)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return surveyBean;
    }


    public boolean isRecordExist(String tablename, String field, String fieldvalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + tablename + " WHERE " + field + " = '" + fieldvalue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    @SuppressLint("Range")
    public boolean isImageSaved(String tablename, String where, String docno, String field) {
        String result = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String Query = "SELECT * FROM " + tablename + " WHERE " + where + " = '" + docno + "'";

        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    result = cursor.getString(cursor.getColumnIndex(field));
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        return result != null && !result.isEmpty();
    }

    public boolean checkRecord(String tablename, String field1, String value1,
                               String field2, String value2,
                               String field3, String value3,
                               String field4, String value4) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + tablename + " WHERE " + field1 + " = '" + value1 + "'"
                + " AND " + field2 + " = '" + value2 + "'"
                + " AND " + field3 + " = '" + value3 + "'"
                + " AND " + field4 + " = '" + value4 + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkRecord(String tablename, String field1, String value1,
                               String field2, String value2,
                               String field3, String value3,
                               String field4, String value4,
                               String field5, String value5) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;
        String Query = "SELECT * FROM " + tablename + " WHERE " + field1 + " = '" + value1 + "'"
                + " AND " + field2 + " = '" + value2 + "'"
                + " AND " + field3 + " = '" + value3 + "'"
                + " AND " + field4 + " = '" + value4 + "'"
                + " AND " + field5 + " = '" + value5 + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getPosition(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
                Log.e("INDEX", "&&&&&" + index);
            }
        }
        return index;
    }

    @SuppressLint("Range")
    public SimCardBean getSimDataformation(String user_id, String enq_docno) {
        SimCardBean simCardBean = new SimCardBean();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_SIM_REPLACMENT_DATA + " WHERE " + KEY_USER_ID + " = '" + user_id + "'" + "AND " + KEY_ENQ_DOC + " = '" + enq_docno + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        simCardBean = new SimCardBean();
                        simCardBean.setUser_id(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                        simCardBean.setUser_type(cursor.getString(cursor.getColumnIndex(KEY_USER_TYPE)));
                        simCardBean.setEnq_docno(cursor.getString(cursor.getColumnIndex(KEY_ENQ_DOC)));
                        simCardBean.setCust_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        simCardBean.setCust_mobile(cursor.getString(cursor.getColumnIndex(KEY_CUST_MOBILE)));
                        simCardBean.setCust_address(cursor.getString(cursor.getColumnIndex(KEY_CUST_ADDRESS)));
                        simCardBean.setDevice_no(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NO)));
                        simCardBean.setSim_rep_date(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_REP_DATE)));
                        simCardBean.setSim_old_no(cursor.getString(cursor.getColumnIndex(KEY_SIM_OLD_NO)));
                        simCardBean.setSim_new_no(cursor.getString(cursor.getColumnIndex(KEY_SIM_NEW_NO)));
                        simCardBean.setSim_new_photo(cursor.getString(cursor.getColumnIndex(KEY_SIM_NEW_PHOTO)));
                        simCardBean.setSim_old_photo(cursor.getString(cursor.getColumnIndex(KEY_SIM_OLD_PHOTO)));
                        simCardBean.setDrive_photo(cursor.getString(cursor.getColumnIndex(KEY_DRIVE_PHOTO)));
                        simCardBean.setSim_lat(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_LAT)));
                        simCardBean.setSim_lng(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_LNG)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return simCardBean;
    }

    @SuppressLint("Range")
    public ArrayList<SimCardBean> getSimData() {
        SimCardBean simCardBean = new SimCardBean();
        ArrayList<SimCardBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_SIM_REPLACMENT_DATA;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        simCardBean = new SimCardBean();
                        simCardBean.setUser_id(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                        simCardBean.setUser_type(cursor.getString(cursor.getColumnIndex(KEY_USER_TYPE)));
                        simCardBean.setEnq_docno(cursor.getString(cursor.getColumnIndex(KEY_ENQ_DOC)));
                        simCardBean.setCust_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                        simCardBean.setCust_mobile(cursor.getString(cursor.getColumnIndex(KEY_CUST_MOBILE)));
                        simCardBean.setCust_address(cursor.getString(cursor.getColumnIndex(KEY_CUST_ADDRESS)));
                        simCardBean.setDevice_no(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NO)));
                        simCardBean.setSim_rep_date(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_REP_DATE)));
                        simCardBean.setSim_old_no(cursor.getString(cursor.getColumnIndex(KEY_SIM_NEW_NO)));
                        simCardBean.setSim_new_no(cursor.getString(cursor.getColumnIndex(KEY_SIM_OLD_NO)));
                        simCardBean.setSim_new_photo(cursor.getString(cursor.getColumnIndex(KEY_SIM_NEW_PHOTO)));
                        simCardBean.setSim_old_photo(cursor.getString(cursor.getColumnIndex(KEY_SIM_OLD_PHOTO)));
                        simCardBean.setDrive_photo(cursor.getString(cursor.getColumnIndex(KEY_DRIVE_PHOTO)));
                        simCardBean.setSim_lat(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_LAT)));
                        simCardBean.setSim_lng(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_LNG)));
                        list_document.add(simCardBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return list_document;
    }

    @SuppressLint("Range")
    public ArrayList<SimCardBean> getSimData(String userid) {
        ArrayList<SimCardBean> simCardBeans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {
            selectQuery = "SELECT * FROM " + TABLE_SIM_REPLACMENT_DATA
                    + " WHERE " + KEY_USER_ID + " = '" + userid + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SimCardBean simCardBean = new SimCardBean();
                    simCardBean.setUser_id(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                    simCardBean.setUser_type(cursor.getString(cursor.getColumnIndex(KEY_USER_TYPE)));
                    simCardBean.setEnq_docno(cursor.getString(cursor.getColumnIndex(KEY_ENQ_DOC)));
                    simCardBean.setCust_name(cursor.getString(cursor.getColumnIndex(KEY_CUST_NAME)));
                    simCardBean.setCust_mobile(cursor.getString(cursor.getColumnIndex(KEY_CUST_MOBILE)));
                    simCardBean.setCust_address(cursor.getString(cursor.getColumnIndex(KEY_CUST_ADDRESS)));
                    simCardBean.setDevice_no(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NO)));
                    simCardBean.setSim_rep_date(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_REP_DATE)));
                    simCardBean.setSim_old_no(cursor.getString(cursor.getColumnIndex(KEY_SIM_OLD_NO)));
                    simCardBean.setSim_new_no(cursor.getString(cursor.getColumnIndex(KEY_SIM_NEW_NO)));
                    simCardBean.setSim_new_photo(cursor.getString(cursor.getColumnIndex(KEY_SIM_NEW_PHOTO)));
                    simCardBean.setSim_old_photo(cursor.getString(cursor.getColumnIndex(KEY_SIM_OLD_PHOTO)));
                    simCardBean.setDrive_photo(cursor.getString(cursor.getColumnIndex(KEY_DRIVE_PHOTO)));
                    simCardBean.setSim_lat(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_LAT)));
                    simCardBean.setSim_lng(cursor.getString(cursor.getColumnIndex(KEY_SIM_CARD_LNG)));
                    simCardBeans.add(simCardBean);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return simCardBeans;
    }

    public boolean getcount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        return icount > 0;
    }



    public void insertRejectedInstallationImage(String name, String path, boolean isSelected, String billNo, String latitude, String longitude, int position) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INSTALLATION_NAME, name);
        contentValues.put(KEY_INSTALLATION_PATH, path);
        contentValues.put(KEY_INSTALLATION_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_INSTALLATION_BILL_NO, billNo);
        contentValues.put(KEY_INSTALLATION_LATITUDE, latitude);
        contentValues.put(KEY_INSTALLATION_LONGITUDE, longitude);
        contentValues.put(KEY_INSTALLATION_POSITION, position);
        database.insert(TABLE_REJECTED_INSTALLATION_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void updateRejectedInstallationImage(String name, String path, boolean isSelected, String billNo, String latitude, String longitude, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INSTALLATION_NAME, name);
        values.put(KEY_INSTALLATION_PATH, path);
        values.put(KEY_INSTALLATION_IMAGE_SELECTED, isSelected);
        values.put(KEY_INSTALLATION_BILL_NO, billNo);
        values.put(KEY_INSTALLATION_LATITUDE, latitude);
        values.put(KEY_INSTALLATION_LONGITUDE, longitude);
        values.put(KEY_INSTALLATION_POSITION, position);
        // update Row
        db.update(TABLE_REJECTED_INSTALLATION_IMAGE_DATA, values, "installationImageName = '" + name + "'", null);
        db.close();
    }


    public void insertInstallationImage(String name, String path, boolean isSelected, String billNo, String latitude, String longitude, int position) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INSTALLATION_NAME, name);
        contentValues.put(KEY_INSTALLATION_PATH, path);
        contentValues.put(KEY_INSTALLATION_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_INSTALLATION_BILL_NO, billNo);
        contentValues.put(KEY_INSTALLATION_LATITUDE, latitude);
        contentValues.put(KEY_INSTALLATION_LONGITUDE, longitude);
        contentValues.put(KEY_INSTALLATION_POSITION, position);
        database.insert(TABLE_INSTALLATION_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void insertBeneficiaryImage(ImageModel imageModel,boolean bool) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INSTALLATION_NAME, imageModel.getName());
        contentValues.put(KEY_INSTALLATION_PATH, imageModel.getImagePath());
        contentValues.put(KEY_INSTALLATION_IMAGE_SELECTED, bool);
        contentValues.put(KEY_INSTALLATION_BILL_NO, imageModel.getBillNo());
        contentValues.put(KEY_INSTALLATION_LATITUDE, imageModel.getLatitude());
        contentValues.put(KEY_INSTALLATION_LONGITUDE, imageModel.getLongitude());
        contentValues.put(KEY_INSTALLATION_POSITION, imageModel.getPoistion());
        database.insert(TABLE_BENEFICIARY_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void updateRecordBeneficiary(ImageModel imageModel, boolean bool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INSTALLATION_NAME, imageModel.getName());
        values.put(KEY_INSTALLATION_PATH, imageModel.getImagePath());
        values.put(KEY_INSTALLATION_IMAGE_SELECTED, bool);
        values.put(KEY_INSTALLATION_BILL_NO, imageModel.getBillNo());
        values.put(KEY_INSTALLATION_LATITUDE, imageModel.getLatitude());
        values.put(KEY_INSTALLATION_LONGITUDE, imageModel.getLongitude());
        values.put(KEY_INSTALLATION_POSITION, imageModel.getPoistion());
        // update Row
        db.update(TABLE_BENEFICIARY_IMAGE_DATA, values, "installationImageName = '" + imageModel.getName() + "'", null);
        db.close();
    }


    public void updateRecordAlternate(String name, String path, boolean isSelected, String billNo, String latitude, String longitude, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INSTALLATION_NAME, name);
        values.put(KEY_INSTALLATION_PATH, path);
        values.put(KEY_INSTALLATION_IMAGE_SELECTED, isSelected);
        values.put(KEY_INSTALLATION_BILL_NO, billNo);
        values.put(KEY_INSTALLATION_LATITUDE, latitude);
        values.put(KEY_INSTALLATION_LONGITUDE, longitude);
        values.put(KEY_INSTALLATION_POSITION, position);
        // update Row
        db.update(TABLE_INSTALLATION_IMAGE_DATA, values, "installationImageName = '" + name + "'", null);
        db.close();
    }



    public void insertSiteAuditImage(String name, String path, boolean isSelected, String billno) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_SITE_AUDIT_NAME, name);
        contentValues.put(KEY_SITE_AUDIT_PATH, path);
        contentValues.put(KEY_SITE_AUDIT_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_SITE_AUDIT_BILL_NO, billno);
        database.insert(TABLE_SITE_AUDIT, null, contentValues);
        database.close();
    }

    public void updateSiteAuditRecord(String name, String path, boolean isSelected, String billno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SITE_AUDIT_NAME, name);
        values.put(KEY_SITE_AUDIT_PATH, path);
        values.put(KEY_SITE_AUDIT_IMAGE_SELECTED, isSelected);
        values.put(KEY_SITE_AUDIT_BILL_NO, billno);
        // update Row
        db.update(TABLE_SITE_AUDIT, values, "siteAuditImageName = '" + name + "'", null);
        db.close();
    }


    public void insertKusumCImages(String name, String path, boolean isSelected, String billno) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_KUSUMC_NAME, name);
        contentValues.put(KEY_KUSUMC_PATH, path);
        contentValues.put(KEY_KUSUMC_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_KUSUMC_BILL_NO, billno);
        database.insert(TABLE_KusumCImages, null, contentValues);
        database.close();
    }

    public void updateKusumCImages(String name, String path, boolean isSelected, String billno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KUSUMC_NAME, name);
        values.put(KEY_KUSUMC_PATH, path);
        values.put(KEY_KUSUMC_IMAGE_SELECTED, isSelected);
        values.put(KEY_KUSUMC_BILL_NO, billno);
        // update Row
        db.update(TABLE_KusumCImages, values, "ImageName = '" + name + "'", null);
        db.close();
    }

    public List<ImageModel> getAllAuditSiteImages() {
        ArrayList<ImageModel> siteAuditImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_SITE_AUDIT)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_SITE_AUDIT, null);
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    siteAuditImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return siteAuditImages;
    }

    public List<ImageModel> getAllkusumCImages() {
        ArrayList<ImageModel> siteAuditImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_KusumCImages)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_KusumCImages, null);
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    siteAuditImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return siteAuditImages;
    }

    public void deleteInstallationImages(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_INSTALLATION_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_INSTALLATION_IMAGE_DATA)) {
            db.delete(TABLE_INSTALLATION_IMAGE_DATA, where, null);
        }
    }

    public void deleteBeneficiaryImages(String serialId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_INSTALLATION_BILL_NO + "='" + serialId + "'";
        if (CustomUtility.doesTableExist(db, TABLE_BENEFICIARY_IMAGE_DATA)) {
            db.delete(TABLE_BENEFICIARY_IMAGE_DATA, where, null);
        }
    }

    public void deleteRejectedInstallationImages(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_INSTALLATION_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_REJECTED_INSTALLATION_IMAGE_DATA)) {
            db.delete(TABLE_REJECTED_INSTALLATION_IMAGE_DATA, where, null);
        }
    }


    public void deleteUnloadingImages(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_UNLOADING_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_UNLOADING_IMAGE_DATA)) {
            db.delete(TABLE_UNLOADING_IMAGE_DATA, where, null);
        }
    }
    public void deleteUnloadingForm(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_UNLOADING_FORM_DATA)) {
            db.delete(TABLE_UNLOADING_FORM_DATA, where, null);
        }
    }


    public void deleteOfflineControllerImages(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_UNLOADING_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_OFFLINE_CONTROLLER_IMAGE_DATA)) {
            db.delete(TABLE_OFFLINE_CONTROLLER_IMAGE_DATA, where, null);
        }
    }

    public void deleteDeviceMappingRecords(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + billNo + "'";
        if (CustomUtility.doesTableExist(db, TABLE_DEVICE_MAPPING_DATA)) {
            db.delete(TABLE_DEVICE_MAPPING_DATA, where, null);
        }
    }


    public void deleteOfflineControllerData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_OFFLINE_CONTROLLER_IMAGE_DATA)) {
            db.delete(TABLE_OFFLINE_CONTROLLER_IMAGE_DATA, null, null);
        }
    }

        public void deleteDeviceMappingData() {
            SQLiteDatabase db = this.getWritableDatabase();
            if (CustomUtility.doesTableExist(db, TABLE_DEVICE_MAPPING_DATA)) {
                db.delete(TABLE_DEVICE_MAPPING_DATA, null, null);
            }
    }

    public ArrayList<ImageModel> getAllInstallationImages() {
        ArrayList<ImageModel> installationImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_INSTALLATION_IMAGE_DATA)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_INSTALLATION_IMAGE_DATA, null);

            installationImages.clear();
            ImageModel imageModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    imageModel.setLatitude(mcursor.getString(5));
                    imageModel.setLongitude(mcursor.getString(6));
                    imageModel.setPoistion(mcursor.getInt(7));
                    installationImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return installationImages;
    }

    public ArrayList<ImageModel> getAllBeneficiaryImages() {
        ArrayList<ImageModel> beneficiaryImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_BENEFICIARY_IMAGE_DATA)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_BENEFICIARY_IMAGE_DATA, null);

            beneficiaryImages.clear();
            ImageModel imageModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    imageModel.setLatitude(mcursor.getString(5));
                    imageModel.setLongitude(mcursor.getString(6));
                    imageModel.setPoistion(mcursor.getInt(7));
                    beneficiaryImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
    }
        return  beneficiaryImages;
    }

    public ArrayList<ImageModel> getRejectedInstallationImages() {
        ArrayList<ImageModel> installationImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_REJECTED_INSTALLATION_IMAGE_DATA)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_REJECTED_INSTALLATION_IMAGE_DATA, null);

            installationImages.clear();
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    imageModel.setLatitude(mcursor.getString(5));
                    imageModel.setLongitude(mcursor.getString(6));
                    imageModel.setPoistion(mcursor.getInt(7));
                    installationImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return installationImages;
    }


    public void insertUnloadingImage(String name, String path, boolean isSelected, String billNo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UNLOADING_NAME, name);
        contentValues.put(KEY_UNLOADING_PATH, path);
        contentValues.put(KEY_UNLOADING_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_UNLOADING_BILL_NO, billNo);
        database.insert(TABLE_UNLOADING_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void insertUnloadingFormData(unloadingDataBean unloadingBean) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MODULE_QTY, unloadingBean.getPanel_module_qty());
        contentValues.put(KEY_MODULE_VALUES, unloadingBean.getPanel_values());
        contentValues.put(KEY_PUMP_SERIAL_NO, unloadingBean.getPump_serial_no());
        contentValues.put(KEY_MOTOR_SERIAL_NO, unloadingBean.getMotor_serial_no());
        contentValues.put(KEY_CONTROLLER_SERIAL_NO, unloadingBean.getController_serial_no());
        contentValues.put(KEY_MATERIAL_STATUS, unloadingBean.getMaterial_status());
        contentValues.put(KEY_UNLOADING_REAMRK, unloadingBean.getRemark());
        contentValues.put(KEY_BILL_NO, unloadingBean.getBill_no());
        database.insert(TABLE_UNLOADING_FORM_DATA, null, contentValues);
        database.close();
    }

    public void updateUnloadingAlternate(String name, String path, boolean isSelected, String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UNLOADING_NAME, name);
        values.put(KEY_UNLOADING_PATH, path);
        values.put(KEY_UNLOADING_IMAGE_SELECTED, isSelected);
        values.put(KEY_UNLOADING_BILL_NO, billNo);
        // update Row
        db.update(TABLE_UNLOADING_IMAGE_DATA, values, "unloadingImageName = '" + name + "'", null);
        db.close();
    }

        public void updateUnloadingForm(unloadingDataBean unloadingBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_MODULE_QTY, unloadingBean.getPanel_module_qty());
            contentValues.put(KEY_MODULE_VALUES, unloadingBean.getPanel_values());
            contentValues.put(KEY_PUMP_SERIAL_NO, unloadingBean.getPump_serial_no());
            contentValues.put(KEY_MOTOR_SERIAL_NO, unloadingBean.getMotor_serial_no());
            contentValues.put(KEY_CONTROLLER_SERIAL_NO, unloadingBean.getController_serial_no());
            contentValues.put(KEY_MATERIAL_STATUS, unloadingBean.getMaterial_status());
            contentValues.put(KEY_UNLOADING_REAMRK, unloadingBean.getRemark());
            contentValues.put(KEY_BILL_NO, unloadingBean.getBill_no());
        // update Row
        db.update(TABLE_UNLOADING_FORM_DATA, contentValues, KEY_BILL_NO + "= '" + unloadingBean.getBill_no() + "'", null);
        db.close();
    }



    public ArrayList<ImageModel> getAllUnloadingImages() {
        ArrayList<ImageModel> UnloadingImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_UNLOADING_IMAGE_DATA)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_UNLOADING_IMAGE_DATA, null);

            UnloadingImages.clear();
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    UnloadingImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return UnloadingImages;
    }



    public void insertOfflineControllerImage(ImageModel imageModel, boolean bool) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UNLOADING_NAME, imageModel.getName());
        contentValues.put(KEY_UNLOADING_PATH, imageModel.getImagePath());
        contentValues.put(KEY_UNLOADING_IMAGE_SELECTED, bool);
        contentValues.put(KEY_UNLOADING_BILL_NO, imageModel.getBillNo());
        database.insert(TABLE_OFFLINE_CONTROLLER_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void updateOfflineControllerImage(ImageModel imageModel, boolean bool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UNLOADING_NAME,  imageModel.getName());
        values.put(KEY_UNLOADING_PATH, imageModel.getImagePath());
        values.put(KEY_UNLOADING_IMAGE_SELECTED, bool);
        values.put(KEY_UNLOADING_BILL_NO, imageModel.getBillNo());
        // update Row
        db.update(TABLE_OFFLINE_CONTROLLER_IMAGE_DATA, values, "unloadingImageName = '" + imageModel.getName() + "'", null);
        db.close();
    }

    public ArrayList<ImageModel> getAllOfflineControllerImages() {
        ArrayList<ImageModel> UnloadingImages = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_OFFLINE_CONTROLLER_IMAGE_DATA)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_OFFLINE_CONTROLLER_IMAGE_DATA, null);

            UnloadingImages.clear();
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    UnloadingImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return UnloadingImages;
    }


    public void insertDeviceMappingData(DeviceMappingModel deviceMappingModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DEVICE_MAPPING_READ, deviceMappingModel.getRead());
        contentValues.put(KEY_DEVICE_MAPPING_WRITE, deviceMappingModel.getWrite());
        contentValues.put(KEY_DEVICE_MAPPING_UPDATE, deviceMappingModel.getUpdate());
        contentValues.put(KEY_DEVICE_MAPPING_4GUPDATE, deviceMappingModel.getUpdate4G());
        contentValues.put(KEY_BILL_NO, deviceMappingModel.getBillNo());
        database.insert(TABLE_DEVICE_MAPPING_DATA, null, contentValues);
        database.close();
    }

    public void updateDeviceMappingData(DeviceMappingModel deviceMappingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEVICE_MAPPING_READ, deviceMappingModel.getRead());
        values.put(KEY_DEVICE_MAPPING_WRITE, deviceMappingModel.getWrite());
        values.put(KEY_DEVICE_MAPPING_UPDATE, deviceMappingModel.getUpdate());
        values.put(KEY_DEVICE_MAPPING_4GUPDATE, deviceMappingModel.getUpdate4G());
        values.put(KEY_BILL_NO, deviceMappingModel.getBillNo());
        // update Row
        db.update(TABLE_DEVICE_MAPPING_DATA, values, KEY_BILL_NO + " = '" + deviceMappingModel.getBillNo() + "'", null);
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<DeviceMappingModel> getAllDeviceMappingData() {
        ArrayList<DeviceMappingModel> deviceMappingList = new ArrayList<DeviceMappingModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(database, TABLE_DEVICE_MAPPING_DATA)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_DEVICE_MAPPING_DATA, null);

            deviceMappingList.clear();
            DeviceMappingModel deviceMappingModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    deviceMappingModel = new DeviceMappingModel();
                    deviceMappingModel.setId(mcursor.getString(0));
                    deviceMappingModel.setRead(mcursor.getString(1));
                    deviceMappingModel.setWrite(mcursor.getString(2));
                    deviceMappingModel.setUpdate(mcursor.getString(3));
                    deviceMappingModel.setUpdate4G(mcursor.getString(4));
                    deviceMappingModel.setBillNo(mcursor.getString(5));
                    deviceMappingList.add(deviceMappingModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return deviceMappingList;

    }

    public void insertBeneficiaryRegistrationData(BeneficiaryRegistrationBean beneficiaryRegistrationBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_SERIAL_ID, beneficiaryRegistrationBean.getSerialId());
            values.put(KEY_FAMILY_ID, beneficiaryRegistrationBean.getFamilyId());
            values.put(KEY_APPLICANT_NAME, beneficiaryRegistrationBean.getBeneficiaryFormApplicantName());
            values.put(KEY_APPLICANT_FATHER_NAME, beneficiaryRegistrationBean.getApplicantFatherName());
            values.put(KEY_APPLICANT_MOBILE_NO, beneficiaryRegistrationBean.getApplicantMobile());
            values.put(KEY_APPLICANT_VILLAGE, beneficiaryRegistrationBean.getApplicantVillage());
            values.put(KEY_APPLICANT_BLOCK, beneficiaryRegistrationBean.getApplicantBlock());
            values.put(KEY_APPLICANT_TEHSIL, beneficiaryRegistrationBean.getApplicantTehsil());
            values.put(KEY_APPLICANT_DISTRICT, beneficiaryRegistrationBean.getApplicantDistrict());
            values.put(KEY_PUMP_CAPACITY, beneficiaryRegistrationBean.getPumpCapacity());
            values.put(KEY_PUMP_AC_DC, beneficiaryRegistrationBean.getPumpAcDc());
            values.put(KEY_PUMP_TYPE, beneficiaryRegistrationBean.getPumpType());
            values.put(KEY_CONTROLLER_TYPE, beneficiaryRegistrationBean.getControllerType());
            values.put(KEY_APPLICANT_ACCOUNT_NO, beneficiaryRegistrationBean.getApplicantAccountNo());
            values.put(KEY_APPLICANT_IFSC_CODE, beneficiaryRegistrationBean.getApplicantIFSC());
            values.put(KEY_AADHAR_NO, beneficiaryRegistrationBean.getAadharNo());

            // Insert Row
            long i = db.insert(TABLE_BENEFICIARY_REGISTRATION, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateBeneficiaryRegistrationData(BeneficiaryRegistrationBean beneficiaryRegistrationBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_SERIAL_ID, beneficiaryRegistrationBean.getSerialId());
            values.put(KEY_FAMILY_ID, beneficiaryRegistrationBean.getFamilyId());
            values.put(KEY_APPLICANT_NAME, beneficiaryRegistrationBean.getBeneficiaryFormApplicantName());
            values.put(KEY_APPLICANT_FATHER_NAME, beneficiaryRegistrationBean.getApplicantFatherName());
            values.put(KEY_APPLICANT_MOBILE_NO, beneficiaryRegistrationBean.getApplicantMobile());
            values.put(KEY_APPLICANT_VILLAGE, beneficiaryRegistrationBean.getApplicantVillage());
            values.put(KEY_APPLICANT_BLOCK, beneficiaryRegistrationBean.getApplicantBlock());
            values.put(KEY_APPLICANT_TEHSIL, beneficiaryRegistrationBean.getApplicantTehsil());
            values.put(KEY_APPLICANT_DISTRICT, beneficiaryRegistrationBean.getApplicantDistrict());
            values.put(KEY_PUMP_CAPACITY, beneficiaryRegistrationBean.getPumpCapacity());
            values.put(KEY_PUMP_AC_DC, beneficiaryRegistrationBean.getPumpAcDc());
            values.put(KEY_PUMP_TYPE, beneficiaryRegistrationBean.getPumpType());
            values.put(KEY_CONTROLLER_TYPE, beneficiaryRegistrationBean.getControllerType());
            values.put(KEY_APPLICANT_ACCOUNT_NO, beneficiaryRegistrationBean.getApplicantAccountNo());
            values.put(KEY_APPLICANT_IFSC_CODE, beneficiaryRegistrationBean.getApplicantIFSC());
            values.put(KEY_AADHAR_NO, beneficiaryRegistrationBean.getAadharNo());

            // Insert Row
            db.update(TABLE_BENEFICIARY_REGISTRATION, values, "serial_id = '" + beneficiaryRegistrationBean.getSerialId() + "'", null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    public void deleteBeneficiaryRegistration(String serialId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_SERIAL_ID + "='" + serialId + "'";
        if (CustomUtility.doesTableExist(db, TABLE_BENEFICIARY_REGISTRATION)) {
            db.delete(TABLE_BENEFICIARY_REGISTRATION, where, null);
        }
    }
    @SuppressLint("Range")
    public ArrayList<BeneficiaryRegistrationBean> getBeneficiaryListData() {
        BeneficiaryRegistrationBean beneficiaryRegistrationBean=new BeneficiaryRegistrationBean();
        ArrayList<BeneficiaryRegistrationBean> list_beneficiary = new ArrayList<>();
        list_beneficiary.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_BENEFICIARY_REGISTRATION ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        beneficiaryRegistrationBean = new BeneficiaryRegistrationBean();
                        beneficiaryRegistrationBean.setSerialId(cursor.getString(cursor.getColumnIndex(KEY_SERIAL_ID)));
                        beneficiaryRegistrationBean.setFamilyId(cursor.getString(cursor.getColumnIndex(KEY_FAMILY_ID)));
                        beneficiaryRegistrationBean.setBeneficiaryFormApplicantName(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_NAME)));
                        beneficiaryRegistrationBean.setApplicantFatherName(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_FATHER_NAME)));
                        beneficiaryRegistrationBean.setApplicantMobile(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_MOBILE_NO)));
                        beneficiaryRegistrationBean.setApplicantVillage(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_VILLAGE)));
                        beneficiaryRegistrationBean.setApplicantBlock(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_BLOCK)));
                        beneficiaryRegistrationBean.setApplicantTehsil(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_TEHSIL)));
                        beneficiaryRegistrationBean.setApplicantDistrict(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_DISTRICT)));
                        beneficiaryRegistrationBean.setPumpCapacity(cursor.getString(cursor.getColumnIndex(KEY_PUMP_CAPACITY)));
                        beneficiaryRegistrationBean.setPumpAcDc(cursor.getString(cursor.getColumnIndex(KEY_PUMP_AC_DC)));
                        beneficiaryRegistrationBean.setPumpType(cursor.getString(cursor.getColumnIndex(KEY_PUMP_TYPE)));
                        beneficiaryRegistrationBean.setControllerType(cursor.getString(cursor.getColumnIndex(KEY_CONTROLLER_TYPE)));
                        beneficiaryRegistrationBean.setApplicantAccountNo(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_ACCOUNT_NO)));
                        beneficiaryRegistrationBean.setApplicantIFSC(cursor.getString(cursor.getColumnIndex(KEY_APPLICANT_IFSC_CODE)));
                        beneficiaryRegistrationBean.setAadharNo(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_NO)));

                        list_beneficiary.add(beneficiaryRegistrationBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDb(db);
        }
        return list_beneficiary;
    }


    public void insertParameterRecord(MotorParamListModel.Response response, String pValue) {
        SQLiteDatabase   database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_pmID, String.valueOf(response.getPmId()));
        contentValues.put(COLUMN_ParametersName, response.getParametersName());
        contentValues.put(COLUMN_ModBusAddress, response.getModbusaddress());
        contentValues.put(COLUMN_MobBTAddress, response.getMobBTAddress());
        contentValues.put(COLUMN_factor, String.valueOf(response.getFactor()));
        contentValues.put(COLUMN_pValue, pValue.toString());
        contentValues.put(COLUMN_MaterialCode, response.getMaterialCode());
        contentValues.put(COLUMN_Unit, response.getUnit());
        contentValues.put(COLUMN_offset, String.valueOf(response.getOffset()));
        database.insert(TABLE_SETTING_PARAMETER_LIST, null, contentValues);
        database.close();
    }

    public void updateParameterRecord(MotorParamListModel.Response response) {

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(COLUMN_pValue, response.getpValue());
            values.put(COLUMN_ParametersName,response.getParametersName());
            String  where = COLUMN_ParametersName + "='" + response.getParametersName() + "'" + " AND " +
                    COLUMN_pmID + "='" + response.getPmId() + "'" ;

            database.update(TABLE_SETTING_PARAMETER_LIST, values, where, null);

            // Insert into database successfully.
            database.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            database.close();


        }

    }
    @SuppressLint("Range")
    public ArrayList<MotorParamListModel.Response> getParameterRecordDetails(String matCode){
        ArrayList<MotorParamListModel.Response> arrayList = new ArrayList<>();
      SQLiteDatabase  database = this.getWritableDatabase();
        Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_SETTING_PARAMETER_LIST + " WHERE " + COLUMN_MaterialCode + " = " + matCode + "", null);
        if(mcursor.getCount()>0){
            Log.e("Count====>", String.valueOf(mcursor.getCount()));
            while (mcursor.moveToNext()) {

                MotorParamListModel.Response motorPumpList = new MotorParamListModel.Response();
                motorPumpList.setPmId(Integer.parseInt(mcursor.getString(mcursor.getColumnIndex(COLUMN_pmID))));
                motorPumpList.setParametersName(mcursor.getString(mcursor.getColumnIndex(COLUMN_ParametersName)));
                motorPumpList.setModbusaddress(mcursor.getString(mcursor.getColumnIndex(COLUMN_ModBusAddress)));
                motorPumpList.setMobBTAddress(mcursor.getString(mcursor.getColumnIndex(COLUMN_MobBTAddress)));
                motorPumpList.setpValue(Float.parseFloat(mcursor.getString(mcursor.getColumnIndex(COLUMN_pValue))));
                motorPumpList.setMaterialCode(mcursor.getString(mcursor.getColumnIndex(COLUMN_MaterialCode)));
                motorPumpList.setFactor(Integer.parseInt(mcursor.getString(mcursor.getColumnIndex(COLUMN_factor))));
                motorPumpList.setOffset(Integer.parseInt(mcursor.getString(mcursor.getColumnIndex(COLUMN_offset))));
                arrayList.add(motorPumpList);
            }

        }

        mcursor.close();
        database.close();


        return arrayList;
    }

    public void updateParameter(MotorParamListModel.Response response) {

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(COLUMN_pValue, response.getpValue());
            values.put(COLUMN_ParametersName,response.getParametersName());
            String  where = COLUMN_ParametersName + "='" + response.getParametersName() + "'" + " AND " +
                    COLUMN_pmID + "='" + response.getPmId() + "'" ;

            database.update(TABLE_SETTING_PARAMETER_LIST, values, where, null);

            // Insert into database successfully.
            database.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            database.close();


        }

    }

    public void deleteParametersData() {
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_SETTING_PARAMETER_LIST)) {
            database.delete(TABLE_SETTING_PARAMETER_LIST, null, null);
        }

    }


    public void insertSettingPendingData(ParameterSettingListModel.InstallationDatum pendingSettingModel) {
        SQLiteDatabase   database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_BILL_NO, String.valueOf(pendingSettingModel.getVbeln()));
        contentValues.put(KEY_CUST_NAME, pendingSettingModel.getName());
        contentValues.put(KEY_CUSTOMER_CODE, pendingSettingModel.getKunnr());
        contentValues.put(KEY_PUMP_SERIAL_NO, pendingSettingModel.getPumpSernr());
        contentValues.put(KEY_MOTOR_SERIAL_NO, String.valueOf(pendingSettingModel.getMotorSernr()));
        contentValues.put(KEY_CONTROLLER_SERIAL_NO, pendingSettingModel.getControllerSernr());
        contentValues.put(KEY_CONTROLLER_MAT_NO, pendingSettingModel.getControllerMatno());
        contentValues.put(KEY_SET_MATNO, pendingSettingModel.getSetMatno());
        contentValues.put(KEY_MOTOR_MATNO, String.valueOf(pendingSettingModel.getMotorMatnr()));
        contentValues.put(KEY_BENEFICIARY, String.valueOf(pendingSettingModel.getBeneficiary()));
        database.insert(TABLE_SETTING_PENDING_LIST, null, contentValues);
        database.close();
    }

    public void updateSettingPendingData(ParameterSettingListModel.InstallationDatum pendingSettingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues;
        try {
            contentValues = new ContentValues();
            contentValues.put(KEY_BILL_NO, String.valueOf(pendingSettingModel.getVbeln()));
            contentValues.put(KEY_CUST_NAME, pendingSettingModel.getName());
            contentValues.put(KEY_CUSTOMER_CODE, pendingSettingModel.getKunnr());
            contentValues.put(KEY_PUMP_SERIAL_NO, pendingSettingModel.getPumpSernr());
            contentValues.put(KEY_MOTOR_SERIAL_NO, String.valueOf(pendingSettingModel.getMotorSernr()));
            contentValues.put(KEY_CONTROLLER_SERIAL_NO, pendingSettingModel.getControllerSernr());
            contentValues.put(KEY_CONTROLLER_MAT_NO, pendingSettingModel.getControllerMatno());
            contentValues.put(KEY_SET_MATNO, pendingSettingModel.getSetMatno());
            contentValues.put(KEY_MOTOR_MATNO, String.valueOf(pendingSettingModel.getMotorMatnr()));
            contentValues.put(KEY_BENEFICIARY, String.valueOf(pendingSettingModel.getBeneficiary()));

            // Insert Row
            db.update(TABLE_SETTING_PENDING_LIST, contentValues, KEY_SET_MATNO+" = '" + pendingSettingModel.getSetMatno() + "'", null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    @SuppressLint("Range")
    public ArrayList<ParameterSettingListModel.InstallationDatum> getPendingSettingList(){
        ArrayList<ParameterSettingListModel.InstallationDatum> arrayList = new ArrayList<>();
        SQLiteDatabase  database = this.getWritableDatabase();
        Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_SETTING_PENDING_LIST, null);
        if(mcursor.getCount()>0){
            Log.e("Count====>", String.valueOf(mcursor.getCount()));
            while (mcursor.moveToNext()) {

                ParameterSettingListModel.InstallationDatum pendingSettingModel = new ParameterSettingListModel.InstallationDatum();
                pendingSettingModel.setVbeln(mcursor.getString(mcursor.getColumnIndex(KEY_BILL_NO)));
                pendingSettingModel.setName(mcursor.getString(mcursor.getColumnIndex(KEY_CUST_NAME)));
                pendingSettingModel.setKunnr(mcursor.getString(mcursor.getColumnIndex(KEY_CUSTOMER_CODE)));
                pendingSettingModel.setPumpSernr(mcursor.getString(mcursor.getColumnIndex(KEY_PUMP_SERIAL_NO)));
                pendingSettingModel.setMotorSernr(mcursor.getString(mcursor.getColumnIndex(KEY_MOTOR_SERIAL_NO)));
                pendingSettingModel.setControllerSernr(mcursor.getString(mcursor.getColumnIndex(KEY_CONTROLLER_SERIAL_NO)));
                pendingSettingModel.setControllerMatno(mcursor.getString(mcursor.getColumnIndex(KEY_CONTROLLER_MAT_NO)));
                pendingSettingModel.setSetMatno(mcursor.getString(mcursor.getColumnIndex(KEY_SET_MATNO)));
                pendingSettingModel.setMotorMatnr(mcursor.getString(mcursor.getColumnIndex(KEY_CONTROLLER_MAT_NO)));
                pendingSettingModel.setBeneficiary(mcursor.getString(mcursor.getColumnIndex(KEY_BENEFICIARY)));
                arrayList.add(pendingSettingModel);
            }

        }

        mcursor.close();
        database.close();


        return arrayList;
    }


    public void insertParameterSet(String billNo, String isParameterSet) {
        SQLiteDatabase   database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_BILL_NO, billNo);
        contentValues.put(KEY_PARAMETER_SET, isParameterSet);
        database.insert(TABLE_PARAMETER_SET_DATA, null, contentValues);
        database.close();
    }

    public void updateParameterSet(String billNo, String isParameterSet) {
        SQLiteDatabase   database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_BILL_NO, billNo);
        contentValues.put(KEY_PARAMETER_SET, isParameterSet);
        database.update(TABLE_PARAMETER_SET_DATA, contentValues, KEY_BILL_NO+" = '" + billNo + "'", null);
        database.close();
    }

    public boolean isParameterSet(String billno) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  *  FROM " + TABLE_PARAMETER_SET_DATA + " WHERE " + KEY_BILL_NO + " = '" +
                billno + "'" + "AND " + KEY_PARAMETER_SET + " = '" + "true" + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
