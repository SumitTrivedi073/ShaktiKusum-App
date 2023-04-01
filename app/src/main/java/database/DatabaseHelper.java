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
import bean.ImageModel;
import bean.InstallationBean;
import bean.InstallationListBean;
import bean.InstallationOfflineBean;
import bean.ItemNameBean;
import bean.LoginBean;
import bean.RegistrationBean;
import bean.RejectListBean;
import bean.SimCardBean;
import bean.SiteAuditListBean;
import bean.SubmitOfflineDataInput;
import bean.SurveyBean;
import bean.SurveyListBean;
import com.shaktipumplimited.DamageMissBean.DamageMissResponse;
import com.shaktipumplimited.SettingModel.SettingParameterResponse;
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
    public static final String TABLE_INSTALLATION_OFFLINE_LIST = "tbl_installation_offline_list";
    public static final String TABLE_OFFLINE_SUBMITTED_LIST = "tbl_offline_submitted_list";
    public static final String TABLE_SETTING_PARAMETER_LIST = "tbl_setting_parameter_list";
    public static final String TABLE_AUDITSITE_LIST = "tbl_auditsite_list";
    public static final String TABLE_REJECTION_LIST = "tbl_rejection_list";
    public static final String TABLE_SURVEY_LIST = "tbl_survey_list";
    public static final String TABLE_INSTALLATION_PUMP_DATA = "tbl_installation_pump_data";

    public static final String TABLE_INSTALLATION_IMAGE_DATA = "tbl_installation_image_data";

    public static final String TABLE_UNLOADING_IMAGE_DATA = "tbl_unloading_image_data";
    public static final String TABLE_AUDIT_PUMP_DATA = "tbl_audit_pump_data";
    public static final String TABLE_SURVEY_PUMP_DATA = "tbl_survey_pump_data";
    public static final String TABLE_SIM_REPLACMENT_DATA = "tbl_sim_card_replacement";
    public static final String TABLE_DAMAGE_MISS_COMPLAIN = "tbl_damage_midd_complain";
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

    public static final String KEY_INSTALLATION_ID = "installationId",KEY_INSTALLATION_NAME = "installationImageName",KEY_INSTALLATION_PATH = "installtionPath",KEY_INSTALLATION_IMAGE_SELECTED = "installtionImageSelected",KEY_INSTALLATION_BILL_NO = "InstalltionBillNo";

    public static final String KEY_UNLOADING_ID = "unloadingId",KEY_UNLOADING_NAME = "unloadingImageName",KEY_UNLOADING_PATH = "unloadingPath",KEY_UNLOADING_IMAGE_SELECTED = "unloadingImageSelected",KEY_UNLOADING_BILL_NO = "unloadingBillNo";


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

    //dataHelper.insertDamageMissData(billno,mDropDownList.get(i),mRoadioList.get(i),mQuentityList.get(i),mRemarkList.get(i),mPhotoList.get(i));
    public void insertDamageMissData(DamageMissResponse mDamageMissResponse) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, mDamageMissResponse.getMBillNo());

            values.put(KEY_DAMAGE_MISS1, mDamageMissResponse.getMDropdownValue1());
            values.put(KEY_DAMAGE_MISS2, mDamageMissResponse.getMDropdownValue2());
            values.put(KEY_DAMAGE_MISS3, mDamageMissResponse.getMDropdownValue3());
            values.put(KEY_DAMAGE_MISS4, mDamageMissResponse.getMDropdownValue4());
            values.put(KEY_DAMAGE_MISS5, mDamageMissResponse.getMDropdownValue5());

            values.put(KEY_RADIO1, mDamageMissResponse.getMRodioValue1());
            values.put(KEY_RADIO2, mDamageMissResponse.getMRodioValue2());
            values.put(KEY_RADIO3, mDamageMissResponse.getMRodioValue3());
            values.put(KEY_RADIO4, mDamageMissResponse.getMRodioValue4());
            values.put(KEY_RADIO5, mDamageMissResponse.getMRodioValue5());

            values.put(KEY_QUNTITY1, mDamageMissResponse.getMQuentityValue1());
            values.put(KEY_QUNTITY2, mDamageMissResponse.getMQuentityValue2());
            values.put(KEY_QUNTITY3, mDamageMissResponse.getMQuentityValue3());
            values.put(KEY_QUNTITY4, mDamageMissResponse.getMQuentityValue4());
            values.put(KEY_QUNTITY5, mDamageMissResponse.getMQuentityValue5());

            values.put(KEY_REMARKD1, mDamageMissResponse.getMRemarkValue1());
            values.put(KEY_REMARKD2, mDamageMissResponse.getMRemarkValue2());
            values.put(KEY_REMARKD3, mDamageMissResponse.getMRemarkValue3());
            values.put(KEY_REMARKD4, mDamageMissResponse.getMRemarkValue4());
            values.put(KEY_REMARKD5, mDamageMissResponse.getMRemarkValue5());

            values.put(KEY_PHOTO1, mDamageMissResponse.getMPhotoValue1());
            values.put(KEY_PHOTO1_2, mDamageMissResponse.getMPhotoValue1_2());
            values.put(KEY_PHOTO1_3, mDamageMissResponse.getMPhotoValue1_3());
            values.put(KEY_PHOTO1_4, mDamageMissResponse.getMPhotoValue1_4());
            values.put(KEY_PHOTO1_5, mDamageMissResponse.getMPhotoValue1_5());

            values.put(KEY_PHOTO2, mDamageMissResponse.getMPhotoValue2());
            values.put(KEY_PHOTO3, mDamageMissResponse.getMPhotoValue3());
            values.put(KEY_PHOTO4, mDamageMissResponse.getMPhotoValue4());
            values.put(KEY_PHOTO5, mDamageMissResponse.getMPhotoValue5());
            long i = db.insert(TABLE_DAMAGE_MISS_COMPLAIN, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //Updated Damage record
    public void updatedDamageMissData(DamageMissResponse mDamageMissResponse) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        String where = " ";
        try {
            values = new ContentValues();
            values.put(KEY_BILL_NO, mDamageMissResponse.getMBillNo());
            values.put(KEY_DAMAGE_MISS1, mDamageMissResponse.getMDropdownValue1());
            values.put(KEY_DAMAGE_MISS2, mDamageMissResponse.getMDropdownValue2());
            values.put(KEY_DAMAGE_MISS3, mDamageMissResponse.getMDropdownValue3());
            values.put(KEY_DAMAGE_MISS4, mDamageMissResponse.getMDropdownValue4());
            values.put(KEY_DAMAGE_MISS5, mDamageMissResponse.getMDropdownValue5());

            values.put(KEY_RADIO1, mDamageMissResponse.getMRodioValue1());
            values.put(KEY_RADIO2, mDamageMissResponse.getMRodioValue2());
            values.put(KEY_RADIO3, mDamageMissResponse.getMRodioValue3());
            values.put(KEY_RADIO4, mDamageMissResponse.getMRodioValue4());
            values.put(KEY_RADIO5, mDamageMissResponse.getMRodioValue5());

            values.put(KEY_QUNTITY1, mDamageMissResponse.getMQuentityValue1());
            values.put(KEY_QUNTITY2, mDamageMissResponse.getMQuentityValue2());
            values.put(KEY_QUNTITY3, mDamageMissResponse.getMQuentityValue3());
            values.put(KEY_QUNTITY4, mDamageMissResponse.getMQuentityValue4());
            values.put(KEY_QUNTITY5, mDamageMissResponse.getMQuentityValue5());

            values.put(KEY_REMARKD1, mDamageMissResponse.getMRemarkValue1());
            values.put(KEY_REMARKD2, mDamageMissResponse.getMRemarkValue2());
            values.put(KEY_REMARKD3, mDamageMissResponse.getMRemarkValue3());
            values.put(KEY_REMARKD4, mDamageMissResponse.getMRemarkValue4());
            values.put(KEY_REMARKD5, mDamageMissResponse.getMRemarkValue5());

            values.put(KEY_PHOTO1, mDamageMissResponse.getMPhotoValue1());
            values.put(KEY_PHOTO1_2, mDamageMissResponse.getMPhotoValue1_2());
            values.put(KEY_PHOTO1_3, mDamageMissResponse.getMPhotoValue1_3());
            values.put(KEY_PHOTO1_4, mDamageMissResponse.getMPhotoValue1_4());
            values.put(KEY_PHOTO1_5, mDamageMissResponse.getMPhotoValue1_5());

            values.put(KEY_PHOTO2, mDamageMissResponse.getMPhotoValue2());
            values.put(KEY_PHOTO3, mDamageMissResponse.getMPhotoValue3());
            values.put(KEY_PHOTO4, mDamageMissResponse.getMPhotoValue4());
            values.put(KEY_PHOTO5, mDamageMissResponse.getMPhotoValue5());

            // Insert Row
            // long i = db.insert(TABLE_DAMAGE_MISS_COMPLAIN , null, values);
            where = KEY_BILL_NO + "='" + mDamageMissResponse.getMBillNo() + "'";
            i = db.update(TABLE_DAMAGE_MISS_COMPLAIN, values, where, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @SuppressLint("Range")
    public DamageMissResponse getDamageMissData(String mBillNo) {
        DamageMissResponse mDamageMissResponse = new DamageMissResponse();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            //String selectQuery = "SELECT  *  FROM " + TABLE_REGISTRATION + " WHERE " + KEY_PERNR + " = '" + user_id + "'" + "AND " + KEY_ENQ_DOC + " = '" + enq_docno + "'";
            String selectQuery = "SELECT  *  FROM " + TABLE_DAMAGE_MISS_COMPLAIN + " WHERE " + KEY_BILL_NO + " = '" + mBillNo + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        mDamageMissResponse = new DamageMissResponse();

                        mDamageMissResponse.setMBillNo(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        mDamageMissResponse.setMDropdownValue1(cursor.getString(cursor.getColumnIndex(KEY_DAMAGE_MISS1)));
                        mDamageMissResponse.setMDropdownValue2(cursor.getString(cursor.getColumnIndex(KEY_DAMAGE_MISS2)));
                        mDamageMissResponse.setMDropdownValue3(cursor.getString(cursor.getColumnIndex(KEY_DAMAGE_MISS3)));
                        mDamageMissResponse.setMDropdownValue4(cursor.getString(cursor.getColumnIndex(KEY_DAMAGE_MISS4)));
                        mDamageMissResponse.setMDropdownValue5(cursor.getString(cursor.getColumnIndex(KEY_DAMAGE_MISS5)));

                        mDamageMissResponse.setMRodioValue1(cursor.getString(cursor.getColumnIndex(KEY_RADIO1)));
                        mDamageMissResponse.setMRodioValue2(cursor.getString(cursor.getColumnIndex(KEY_RADIO2)));
                        mDamageMissResponse.setMRodioValue3(cursor.getString(cursor.getColumnIndex(KEY_RADIO3)));
                        mDamageMissResponse.setMRodioValue4(cursor.getString(cursor.getColumnIndex(KEY_RADIO4)));
                        mDamageMissResponse.setMRodioValue5(cursor.getString(cursor.getColumnIndex(KEY_RADIO5)));

                        mDamageMissResponse.setMQuentityValue1(cursor.getString(cursor.getColumnIndex(KEY_QUNTITY1)));
                        mDamageMissResponse.setMQuentityValue2(cursor.getString(cursor.getColumnIndex(KEY_QUNTITY2)));
                        mDamageMissResponse.setMQuentityValue3(cursor.getString(cursor.getColumnIndex(KEY_QUNTITY3)));
                        mDamageMissResponse.setMQuentityValue4(cursor.getString(cursor.getColumnIndex(KEY_QUNTITY4)));
                        mDamageMissResponse.setMQuentityValue5(cursor.getString(cursor.getColumnIndex(KEY_QUNTITY5)));

                        mDamageMissResponse.setMRemarkValue1(cursor.getString(cursor.getColumnIndex(KEY_REMARKD1)));
                        mDamageMissResponse.setMRemarkValue2(cursor.getString(cursor.getColumnIndex(KEY_REMARKD2)));
                        mDamageMissResponse.setMRemarkValue3(cursor.getString(cursor.getColumnIndex(KEY_REMARKD3)));
                        mDamageMissResponse.setMRemarkValue4(cursor.getString(cursor.getColumnIndex(KEY_REMARKD4)));
                        mDamageMissResponse.setMRemarkValue5(cursor.getString(cursor.getColumnIndex(KEY_REMARKD5)));

                        mDamageMissResponse.setMPhotoValue1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1)));
                        mDamageMissResponse.setMPhotoValue1_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1_2)));
                        mDamageMissResponse.setMPhotoValue1_3(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1_3)));
                        mDamageMissResponse.setMPhotoValue1_4(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1_4)));
                        mDamageMissResponse.setMPhotoValue1_5(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1_5)));

                        mDamageMissResponse.setMPhotoValue2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO2)));
                        mDamageMissResponse.setMPhotoValue3(cursor.getString(cursor.getColumnIndex(KEY_PHOTO3)));
                        mDamageMissResponse.setMPhotoValue4(cursor.getString(cursor.getColumnIndex(KEY_PHOTO4)));
                        mDamageMissResponse.setMPhotoValue5(cursor.getString(cursor.getColumnIndex(KEY_PHOTO5)));
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
        return mDamageMissResponse;
    }

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
            + KEY_ADD11 + " TEXT," + KEY_ADD12 + " TEXT," + KEY_ADD13 + " TEXT," + KEY_ADD14 + " TEXT," + KEY_ADD15 + " TEXT," + KEY_ADD16 + " TEXT)";

    private static final String CREATE_TABLE_INSTALLATION_IMAGES = "CREATE TABLE "
            + TABLE_INSTALLATION_IMAGE_DATA + "("  + KEY_INSTALLATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ KEY_INSTALLATION_NAME + " TEXT," + KEY_INSTALLATION_PATH + " TEXT," + KEY_INSTALLATION_IMAGE_SELECTED + " BOOLEAN," + KEY_INSTALLATION_BILL_NO + " TEXT)";

    private static final String CREATE_TABLE_UNLOADING_IMAGES = "CREATE TABLE "
            + TABLE_UNLOADING_IMAGE_DATA + "("  + KEY_UNLOADING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ KEY_UNLOADING_NAME + " TEXT," + KEY_UNLOADING_PATH + " TEXT," + KEY_UNLOADING_IMAGE_SELECTED + " TEXT," + KEY_UNLOADING_BILL_NO + " TEXT)";



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

    private static final String CREATE_TABLE_SETTING_PARAMETER_LIST = "CREATE TABLE "
            + TABLE_SETTING_PARAMETER_LIST + "("
            + KEY_PMID + " VARCHAR,"
            + KEY_PARAMETERS_NAME + " VARCHAR,"
            + KEY_MODBUS_ADDRESS + " VARCHAR,"
            + KEY_MOB_BT_ADDRESS + " VARCHAR,"
            + KEY_FACTOR + " VARCHAR,"
            + KEY_PVALUE + " VARCHAR,"
            + KEY_MATERIAL_CODE + " VARCHAR,"
            + KEY_UNIT + " VARCHAR,"
            + KEY_OFFSET + " VARCHAR)";

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
        db.execSQL(CREATE_TABLE_INSTALLATION_OFFLINE_LIST);
        db.execSQL(CREATE_TABLE_OFFLINE_SUBMITTED_LIST);
        db.execSQL(CREATE_TABLE_SETTING_PARAMETER_LIST);
        db.execSQL(CREATE_TABLE_AUDITSITE_LIST);
        db.execSQL(CREATE_TABLE_AUDIT_PUMP);
        db.execSQL(CREATE_TABLE_REJECTION_LIST);
        db.execSQL(CREATE_TABLE_SURVEY_LIST);
        db.execSQL(CREATE_TABLE_STATE_SEARCH);
        db.execSQL(CREATE_TABLE_INSTALLATION_PUMP);
        db.execSQL(CREATE_SIM_CARD_REPLACEMENT);
        db.execSQL(CREATE_TABLE_SURVEY_DATA);
        db.execSQL(CREATE_TABLE_INSTALLATION_IMAGES);
        db.execSQL(CREATE_TABLE_UNLOADING_IMAGES);
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_OFFLINE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE_SUBMITTED_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING_PARAMETER_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REJECTION_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_PUMP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIM_REPLACMENT_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_PUMP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_SEARCH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITSITE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIT_PUMP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTALLATION_IMAGE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOADING_IMAGE_DATA);
            // create newworkorder tables
            onCreate(db);
        }
    }

    public void deleteSIMData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SIM_REPLACMENT_DATA, null, null);
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
            long i = db.insert(TABLE_LOGIN, null, values);
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

    public void insertStateDistrictData(String state, String state_text, String district, String district_text) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_STATE, state);
            values.put(KEY_STATE_TEXT, state_text);
            values.put(KEY_DISTRICT, district);
            values.put(KEY_DISTRICT_TEXT, district_text);

            // Insert Row
            long i = db.insert(TABLE_STATE_DISTRICT, null, values);

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


    //Insert Setting perameter data

    @SuppressLint("Range")
    public List<SettingParameterResponse> getSettingPerameterList(String userid) {

        SettingParameterResponse mSettingParameterResponse = new SettingParameterResponse();
        ArrayList<SettingParameterResponse> mSettingParameterResponseList = new ArrayList<>();
        mSettingParameterResponseList.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_SETTING_PARAMETER_LIST + " WHERE " + KEY_MATERIAL_CODE + " = '" + userid + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        mSettingParameterResponse = new SettingParameterResponse();

                        mSettingParameterResponse.setPmId(cursor.getString(cursor.getColumnIndex(KEY_PMID)));
                        mSettingParameterResponse.setParametersName(cursor.getString(cursor.getColumnIndex(KEY_PARAMETERS_NAME)));
                        mSettingParameterResponse.setModbusaddress(cursor.getString(cursor.getColumnIndex(KEY_MODBUS_ADDRESS)));
                        mSettingParameterResponse.setMobBTAddress(cursor.getString(cursor.getColumnIndex(KEY_MOB_BT_ADDRESS)));

                        try {
                            mSettingParameterResponse.setFactor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_FACTOR))));
                            mSettingParameterResponse.setPValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PVALUE))));
                            mSettingParameterResponse.setOffset(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OFFSET))));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                      /*  mSettingParameterResponse.setFactor(cursor.getInt(cursor.getColumnIndex(KEY_FACTOR)));
                        mSettingParameterResponse.setPValue(cursor.getInt(cursor.getColumnIndex(KEY_PVALUE)));
                        mSettingParameterResponse.setOffset(cursor.getInt(cursor.getColumnIndex(KEY_OFFSET)));
*/
                        mSettingParameterResponse.setMaterialCode(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_CODE)));
                        mSettingParameterResponse.setUnit(cursor.getString(cursor.getColumnIndex(KEY_UNIT)));


                        mSettingParameterResponseList.add(mSettingParameterResponse);

                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            } else {

            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return mSettingParameterResponseList;
    }


    public void insertSettingPerameterList(Context mContext, SettingParameterResponse mSettingParameterResponse, String mMaterialCode) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        Cursor cursor = null;

        int mmVkCheck = 0;


        // KEY_PMID = pmId, KEY_PARAMETERS_NAME = parametersName,  KEY_MODBUS_ADDRESS = modbusaddress, KEY_MOB_BT_ADDRESS= mobBTAddress, KEY_FACTOR= factor, KEY_PVALUE= pValue, KEY_MATERIAL_CODE=materialCode, KEY_UNIT= unit, KEY_OFFSET= offset;

        try {

            // if(checkFirstInsert == 0)
            {
                // String selectQuery = "SELECT  *  FROM " + TABLE_SETTING_PARAMETER_LIST + " WHERE " + KEY_MATERIAL_CODE + " = '" + mMaterialCode + "'";
                String selectQuery = "SELECT  *  FROM " + TABLE_SETTING_PARAMETER_LIST + " WHERE " + KEY_MATERIAL_CODE + " = '" + mMaterialCode + "'and pmId=" + mSettingParameterResponse.getPmId();

                cursor = db1.rawQuery(selectQuery, null);

                mmVkCheck = cursor.getCount();
            }


            values = new ContentValues();

            System.out.println("IN_iii==>>pp==" + mSettingParameterResponse.getPmId());
            values.put(KEY_PMID, mSettingParameterResponse.getPmId());
            values.put(KEY_PARAMETERS_NAME, mSettingParameterResponse.getParametersName());
            values.put(KEY_MODBUS_ADDRESS, mSettingParameterResponse.getModbusaddress());
            values.put(KEY_MOB_BT_ADDRESS, mSettingParameterResponse.getMobBTAddress());
            values.put(KEY_FACTOR, mSettingParameterResponse.getFactor());
            values.put(KEY_PVALUE, mSettingParameterResponse.getPValue());
            values.put(KEY_MATERIAL_CODE, mMaterialCode);
            values.put(KEY_UNIT, mSettingParameterResponse.getUnit());
            values.put(KEY_OFFSET, mSettingParameterResponse.getOffset());

            if (mmVkCheck > 0) {
                checkFirstInsert = 0;

                // String  where = KEY_MATERIAL_CODE + "='" + mMaterialCode + "'";
                String where = KEY_MATERIAL_CODE + "='" + mMaterialCode + "' and pmId=" + mSettingParameterResponse.getPmId();

                long i = db.update(TABLE_SETTING_PARAMETER_LIST, values, where, null);

                System.out.println("UP_iii==>>" + i);
                // Toast.makeText(mContext, "value Update==>>"+ i, Toast.LENGTH_SHORT).show();
            } else {
                checkFirstInsert = 1;
                long iii = db.insert(TABLE_SETTING_PARAMETER_LIST, null, values);
                System.out.println("IN_iii==>>" + iii);
            }
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
            long i = db.insert(TABLE_INSTALLATION_LIST, null, values);
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
            if(isInsert) {
                i = db.insert(TABLE_OFFLINE_SUBMITTED_LIST, null, values);
            }else{
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
            String selectQuery = "SELECT * FROM " + TABLE_OFFLINE_SUBMITTED_LIST ;
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
       if(CustomUtility.doesTableExist(db,TABLE_LOGIN)) {
            db.delete(TABLE_LOGIN, null, null);
        }
    }

    public void deleteStateSearchHelpData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_STATE_SEARCH)) {
            db.delete(TABLE_STATE_SEARCH, null, null);
        }
    }

    public void deleteStateDistrictData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_STATE_DISTRICT)) {
        db.delete(TABLE_STATE_DISTRICT, null, null);
    }
    }

    public void deleteLoginSelecData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_LOGIN_SELECTION)) {
            db.delete(TABLE_LOGIN_SELECTION, null, null);
        }
    }

    public void deleteDashboardData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_DASHBOARD)) {
            db.delete(TABLE_DASHBOARD, null, null);
        }
    }

    public void deleteRegistrationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_REGISTRATION)) {
            db.delete(TABLE_REGISTRATION, null, null);
        }
    }

    public void deleteInstallationListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_INSTALLATION_LIST)) {
            db.delete(TABLE_INSTALLATION_LIST, null, null);
        }
    }

    public void deleteInstallationOfflineListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_INSTALLATION_OFFLINE_LIST)) {
            db.delete(TABLE_INSTALLATION_OFFLINE_LIST, null, null);
        }
    }

    public void deleteOfflineSubmittedData(String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_OFFLINE_BILL_NO + "='" + billNo + "'";
        if(CustomUtility.doesTableExist(db,TABLE_OFFLINE_SUBMITTED_LIST)) {
            int value = db.delete(TABLE_OFFLINE_SUBMITTED_LIST, where, null);
            Log.i("DeleteValue", value + "");
        }

        getInstallationOfflineSubmittedListData();
    }

    public void deleteInstallationListData1(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_INSTALLATION_LIST)) {
            db.delete(TABLE_INSTALLATION_LIST, where, null);
        }
    }

    public void deleteAuditSiteListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_AUDITSITE_LIST)) {
            db.delete(TABLE_AUDITSITE_LIST, null, null);
        }
    }

    public void deleteAuditSiteListData1(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_AUDITSITE_LIST)) {
            db.delete(TABLE_AUDITSITE_LIST, where, null);
        }
    }

    public void deleteRejectListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_REJECTION_LIST)) {
            db.delete(TABLE_REJECTION_LIST, null, null);
        }
    }

    public void deleteSurveyListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_SURVEY_LIST)) {
            db.delete(TABLE_SURVEY_LIST, null, null);
        }
    }

    public void deleteInstallationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_INSTALLATION_PUMP_DATA)) {
            db.delete(TABLE_INSTALLATION_PUMP_DATA, null, null);
        }
    }

    public void deleteAuditData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_AUDIT_PUMP_DATA)) {
            db.delete(TABLE_AUDIT_PUMP_DATA, null, null);
        }
    }

    public void deleteSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_SURVEY_PUMP_DATA)) {
            db.delete(TABLE_SURVEY_PUMP_DATA, null, null);
        }
    }

    public void deleteInstallationData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_INSTALLATION_PUMP_DATA)) {
            db.delete(TABLE_INSTALLATION_PUMP_DATA, where, null);
        }
    }

    public void deleteDAMAGEMISSData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_DAMAGE_MISS_COMPLAIN)) {
            db.delete(TABLE_DAMAGE_MISS_COMPLAIN, where, null);
        }
    }

    public void deleteAuditData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_AUDIT_PUMP_DATA)) {
            db.delete(TABLE_AUDIT_PUMP_DATA, where, null);
        }
    }

    public void deleteSurveyData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_BILL_NO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_SURVEY_PUMP_DATA)) {
            db.delete(TABLE_SURVEY_PUMP_DATA, where, null);
        }
    }

    public void deleteRegistrationData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        where = KEY_ENQ_DOC + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_REGISTRATION)) {
            db.delete(TABLE_REGISTRATION, where, null);
        }
    }
    public void deleteInstallationImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_INSTALLATION_IMAGE_DATA)) {
            db.delete(TABLE_INSTALLATION_IMAGE_DATA, null, null);
        }
    }

    public void deleteUnloadingImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_UNLOADING_IMAGE_DATA)) {
            db.delete(TABLE_UNLOADING_IMAGE_DATA, null, null);
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
            Log.e("CURSORCOUNT", "&&&&123" + cursor.getCount() + " " + selectQuery);
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
        if (result != null && !result.isEmpty()) {
            return true;
        } else {
            return false;
        }
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
        if (icount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void insertInstallationImage( String name,String path, boolean isSelected, String billNo ) {
      SQLiteDatabase  database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INSTALLATION_NAME, name);
        contentValues.put(KEY_INSTALLATION_PATH, path);
        contentValues.put(KEY_INSTALLATION_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_INSTALLATION_BILL_NO, billNo);
        database.insert(TABLE_INSTALLATION_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void updateRecordAlternate( String name, String path, boolean isSelected, String billNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INSTALLATION_NAME, name);
        values.put(KEY_INSTALLATION_PATH, path);
        values.put(KEY_INSTALLATION_IMAGE_SELECTED, isSelected);
        values.put(KEY_INSTALLATION_BILL_NO, billNo);
        // update Row
        db.update(TABLE_INSTALLATION_IMAGE_DATA,values,"installationImageName = '"+name+"'",null);
        db.close();
    }

    public ArrayList<ImageModel> getAllInstallationImages() {
        ArrayList<ImageModel> installationImages = new ArrayList<ImageModel>();
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_INSTALLATION_IMAGE_DATA)) {
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
                    installationImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return installationImages;
    }


    public void insertUnloadingImage( String name ,String path,boolean isSelected,  String billNo ) {
        SQLiteDatabase  database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UNLOADING_NAME, name);
        contentValues.put(KEY_UNLOADING_PATH, path);
        contentValues.put(KEY_UNLOADING_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_UNLOADING_BILL_NO, billNo);
        database.insert(TABLE_UNLOADING_IMAGE_DATA, null, contentValues);
        database.close();
    }

    public void updateUnloadingAlternate(String name ,String path,boolean isSelected,  String billNo ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UNLOADING_NAME, name);
        values.put(KEY_UNLOADING_PATH, path);
        values.put(KEY_UNLOADING_IMAGE_SELECTED, isSelected);
        values.put(KEY_UNLOADING_BILL_NO, billNo);
        // update Row
        db.update(TABLE_UNLOADING_IMAGE_DATA,values,"unloadingImageName = '"+name+"'",null);
        db.close();
    }

    public ArrayList<ImageModel> getAllUnloadingImages() {
        ArrayList<ImageModel> UnloadingImages = new ArrayList<ImageModel>();
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_UNLOADING_IMAGE_DATA)) {
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

}
