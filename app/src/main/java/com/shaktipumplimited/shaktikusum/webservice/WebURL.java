package com.shaktipumplimited.shaktikusum.webservice;


import static com.shaktipumplimited.shaktikusum.debugapp.GlobalValue.NewSolarVFD.BASEURL;

public class WebURL {
    public  static int SERVER_CONNECTIVITY_OK = 0;
    public  static String APP_VERSION_CODE = "0";
    public  static int CHECK_FINAL_ALL_OK = 0;
    public  static int CHECK_FOR_WORK_WITH_BT_OR_IN = 0;
    public  static String mSettingCheckValue = "0";
    public  static String mDEvice_Number_CHECK = "";

    public  static String BT_DEVICE_NAME = "";
    public  static String BT_DEVICE_MAC_ADDRESS = "";
    public  static String barCodeResult = "";
    public  static String DEBUG_CHECK_IN_KUSUM = "";

    public static String GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusum";
    public static final String MOTOR_PERSMETER_LIST = "MoterParamList";
    
    public static final String HOST_NAME_SETTING1 = "https://solar10.shaktisolarrms.com/RMSAppTest/";

    public static final String IMAGE_DIRECTORY_NAME = "shaktikusumapp";
    public static String CUSTOMERID_ID= "";
    public static int BT_DEBUG_CHECK= 0;



    public static  String ProjectNo_Con,RegNo_Con, BenificiaryNo_Con;

    public static final String GET_SURVEY_API = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/get_survey.htm";
    public static final String VERSION_PAGE = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/app_version.htm";
    public static final String LOGIN_PAGE = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/login.htm";
    public static final String LOGIN_SELEC_PAGE = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/login_selection.htm";
    public static final String DASHBOARD_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/dashboard.htm";
    public static final String REGISTRATION_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_registration.htm";
    public static final String INSTALLATION_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_installation.htm";
    public static final String INSTALLATION_DATA_UNLOAD = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/unload_image_save.htm";
    public static final String DAMAGE_MISS_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/create_complaint.htm";
    public static final String SAVE_SURVEY_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_survey.htm";
    public static final String INSTALLATION = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/installation.htm";
    public static final String INSTALLATION_UNLOAD = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/unloading_list.htm";
    public static final String INSTALLATION_UNLOAD1 = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/unloading_list.htm";
    public static final String SURVEY = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/get_survey.htm";
    public static final String SURVEY_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_survey.htm";
    public static final String DD_SUB_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_app_data.htm";
    public static final String BENF_DETAILS = BASEURL+"/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zmapp_solar_pro/get_data.htm";
    public static final String REJECT_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/reject_data.htm";
    public static final String REJECT_INSTALLATION = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_reject_installation.htm" ;
    public static final String SYNC_OFFLINE_DATA_TO_SAP    = BASEURL+"/sap/bc/bsp/sap/zmapp_solar/sync_offline_data.htm";
    public static final String STATE_DATA = BASEURL+"/sap/bc/bsp/sap/zmapp_solar/state_data.htm";
    public static final String RMSDATA = "https://solar10.shaktisolarrms.com/Home/ValidDeviceData";
    public static final String INST_CMP = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/installation_cmp.htm" ;
    public static final String AUDIT_SITES = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_site_audit.htm" ;
    public static final String SERIAL_NUMBER = BASEURL+"/sap/bc/bsp/sap/zmapp_solar/get_serial_no.htm" ;
    public static final String DEVICE_DETAILS = "https://solar10.shaktisolarrms.com/Home/SAPOnlineDeviceDetails";

    public static final String INSTALLATION_DATA_OFFLINE = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/installation_offline_done.htm";
    public static final String INSTALLATION_OFFLINE_DATA_SUBMIT = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/image_save_for_offline_done.htm";


    public static final String PendingFeedback = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/pending_feedback_list.htm";

    public static final String SendOTP = "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701";

    public static final String SendOTPToServer =  BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/save_feedback.htm?feedback=";


    //https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar_pro/pending_feedback_list.htm?project_no=1022&userid=0000700810&project_login_no=01
}
