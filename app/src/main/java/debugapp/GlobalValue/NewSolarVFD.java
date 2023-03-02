package debugapp.GlobalValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shakti on 07-Apr-18.
 */
public class NewSolarVFD {
//*********************   development url ****************************************






   // https://solar10.shaktisolarrms.com/RMSAppTest/PumpSetCode?deviceNo=87-0001-0-18-12-20-0


  //  public static  String COMPELETE_ACCESS_TOKEN_NAME =   "";   // https://www your domain .com/
    public static  int  CHECK_DATA_UNOLAD =   0;   // https://www your domain .com/



    public static final String BASE_URL_UPLOAD= "http://111.118.249.190:8090/RMSApp/";
    public static final String BASE_URL_VK= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_debugger/";
    public static final String BASE_URL_VK1= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar_pro/";
    //public static final String BASE_URL_VK1= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar_pro/new_serial_no_update.htm";
    public static final String BASE_URL_OPTION_VK= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zrms_validation/";
   // public static final String BASE_URL_VK= "http://spdevsrvr1.shaktipumps.com:8000/sap/bc/bsp/sap/zmap_debugger/";
    //public static final String BASE_URL_VK= "http://spdevsrvr1.shaktipumps.com:8000/sap/bc/bsp/sap/zmap_debugger/";



    public static final String LOGIN_VK_PAGE = "login.htm";
    public static final String SAVE_VK_PAGE = BASE_URL_VK +"save.htm";
    public static final String UPDATE_IBASE_VK_PAGE = BASE_URL_VK1 +"new_serial_no_update.htm";
    public static final String SIM_STATUS_VK_PAGE = "sim_actiavtion_status.htm";
    public static final String RMS_VALIDAION_OPTION_API = "rms_validation.htm";


    //*************** organisation login ********************* //

    public static final String INSERT_DEBUG_DATA_API = "DeviceDebug";
    public static final String GET_DEVICE_SIM_NUMBER_API = "PumpSetCode";
    //public static final String BASE_URL_VK_CHECK_STATUS = "https://solar10.shaktisolarrms.com/Home/SAPDeviceData?";
    //public static final String BASE_URL_VK_CHECK_STATUS = "http://111.118.249.180:8888/api/GetDevicedetail?";
    public static final String BASE_URL_VK_CHECK_STATUS = "http://103.73.189.118:8888/api/GetDevicedetail?";


    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
