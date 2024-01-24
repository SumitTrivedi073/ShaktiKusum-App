package webservice;


public class WebURL {
    public static int SERVER_CONNECTIVITY_OK = 0;
    public static int CHECK_FINAL_ALL_OK = 0;
    public static int CHECK_FOR_WORK_WITH_BT_OR_IN = 0;
    public static String mSettingCheckValue = "0";
    public static String mDEvice_Number_CHECK = "";

    public static String BT_DEVICE_NAME = "";
    public static String BT_DEVICE_MAC_ADDRESS = "";
    public static String barCodeResult = "";

    public static String GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusum";
    public static final String MOTOR_PERSMETER_LIST = "MoterParamList";

    public static  int  CHECK_DATA_UNOLAD =   0;   // https://www your domain .com/

    public static String CUSTOMERID_ID = "";
    public static int BT_DEBUG_CHECK = 0;
    public static final String INSERT_DEBUG_DATA_API = "DeviceDebug";
    public static final String GET_DEVICE_SIM_NUMBER_API = "PumpSetCode";
    public static String ProjectNo_Con, RegNo_Con, BenificiaryNo_Con;

    /************* production server **************/
    public static final String RMSAPPURL = "https://shaktirms.com/";
   public static final String BASEURL = "https://spprdsrvr1.shaktipumps.com:8423";
   public static final String BASE_URL_VK= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_debugger/";
 public static final String BASE_URL_OPTION_VK= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zrms_validation/";

    /************* development server **************/
  //  public static final String BASEURL = "https://spquasrvr1.shaktipumps.com:8423";
 //   public static final String BASE_URL_VK= "https://spquasrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_debugger/";
   // public static final String BASE_URL_VK1= "https://spquasrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar_pro/";

  // public static final String BASE_URL_OPTION_VK= "https://spquasrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zrms_validation/";




    /************* APIS **************/
    public static final String GET_SURVEY_API = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/get_survey.htm";

    public static final String LOGIN_PAGE = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/login.htm";
    public static final String LOGIN_SELEC_PAGE = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/login_selection.htm";
    public static final String DASHBOARD_DATA = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/dashboard.htm";
    public static final String REGISTRATION_DATA = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_registration.htm";
    public static final String INSTALLATION_DATA = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_installation.htm";
    public static final String INSTALLATION_DATA_UNLOAD = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/unload_image_save1.htm";
    public static final String DAMAGE_MISS_DATA = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/create_complaint.htm";
    public static final String SAVE_SURVEY_DATA = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_survey.htm";
    public static final String INSTALLATION = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/installation.htm";

    public static final String INSTALLATION_UNLOAD1 = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/unloading_list.htm";

    public static final String SYNC_OFFLINE_DATA_TO_SAP = BASEURL + "/sap/bc/bsp/sap/zmapp_solar/sync_offline_data.htm";
    public static final String STATE_DATA = BASEURL + "/sap/bc/bsp/sap/zmapp_solar/state_data.htm";

    public static final String INST_CMP = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/installation_cmp.htm";
    public static final String AUDIT_SITES = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_site_audit.htm";
    public static final String SERIAL_NUMBER = BASEURL + "/sap/bc/bsp/sap/zmapp_solar/get_serial_no.htm";
    public static final String INSTALLATION_DATA_OFFLINE = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/installation_offline_done.htm";
    public static final String INSTALLATION_OFFLINE_DATA_SUBMIT = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/image_save_for_offline_done.htm";

    public static final String PendingFeedback = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/pending_feedback_list.htm";

    public static final String SendOTP = "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701";

    public static final String SendOTPToServer = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_feedback.htm?feedaback=";

    public static final String sendOTPForUnLoading = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/unload_otp_save.htm?otp=";

    public static final String KusumCSurvey = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_survey.htm";

    public static final String unloading_list_verification_pend = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/unloading_list_verification_pend.htm";

    public static final String RoutePlanAPI = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/route_plan_list.htm";

    public static final String saveRoutePlanAPI = BASEURL +"/sap/bc/bsp/sap/zmapp_solar_pro/route_plan_save.htm?route=";

    public static final String jointInspectionAPI = BASEURL +"/sap/bc/bsp/sap/zmapp_solar_pro/joint_inspection.htm";

    public static final String rejectionInstalltionAPI = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/get_reject_installation.htm";

    public static final String DocumentSubmissionListAPI = BASEURL +"/sap/bc/bsp/sap/zmapp_solar_pro/document_submission.htm";

    public static final String saveJointInspectionAPI = BASEURL +"/sap/bc/bsp/sap/zmapp_solar_pro/joint_inspection_save.htm";

    public static final String saveRejectImageAPI = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/save_rejected_installation.htm";

    public static final String saveDeptDocSubmissionAPI = BASEURL +"/sap/bc/bsp/sap/zmapp_solar_pro/document_sub_save.htm";

    public static final String UPDATE_IBASE_VK_PAGE = BASEURL +"/sap/bc/bsp/sap/zmapp_solar_pro/new_serial_no_update.htm";
    public static final String SIM_STATUS_VK_PAGE = BASE_URL_VK +"sim_actiavtion_status.htm";

    public static final String saveDebugData = BASE_URL_VK +"save.htm";

    public static final String DemoRoadURL = BASEURL+"/sap/bc/bsp/sap/zmapp_solar_pro/demo_road_show.htm";
    public static String updateLatLngToRms = "https://quality.shaktirms.com/NewShakti/RMSApp/LatLongUpdate";


    public static final String ComplaintBeforeInstallationAPI = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/complaint_before_inst.htm";

    public static final String SavecomplianbeforeURL = BASEURL +   "/sap/bc/bsp/sap/zmapp_solar_pro/save_complaint_before_inst.htm";
    public static final String ComplainRequestListURL = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/complaint_status_list.htm?userid=";


    public static final String SyncOfflineDeviceData = BASEURL + "/sap/bc/bsp/sap/zmapp_solar_pro/shifting_remark_save.htm";

    public static final String DEVICE_DETAILS = "Home/SAPOnlineDeviceDetails";

    public static String deviceMappingAPIS ="Home/DeviceSettingParam";
}
