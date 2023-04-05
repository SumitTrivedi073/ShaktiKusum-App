package com.shaktipumplimited.shaktikusum.debugapp.GlobalValue;

import com.shaktipumplimited.shaktikusum.BuildConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shakti on 07-Apr-18.
 */
public class NewSolarVFD {

    public static  int  CHECK_DATA_UNOLAD =   0;   // https://www your domain .com/

    public static final String BASEURL = BuildConfig.baseUrl;
    public static final String BASE_URL_VK = BuildConfig.baseUrlVK;
    public static final String BASE_URL_VK1= BuildConfig.baseUrlVK1;
    public static final String BASE_URL_OPTION_VK= BuildConfig.baseUrlOptionVK;

    public static final String BASE_URL_VK_CHECK_STATUS = "http://103.73.189.118:8888/api/GetDevicedetail?";


    public static final String SAVE_VK_PAGE = BASE_URL_VK1 +"save_installation.htm";
    public static final String UPDATE_IBASE_VK_PAGE = BASE_URL_VK1 +"new_serial_no_update.htm";
    public static final String SIM_STATUS_VK_PAGE = "sim_actiavtion_status.htm";
    public static final String RMS_VALIDAION_OPTION_API = "rms_validation.htm";


    public static final String INSERT_DEBUG_DATA_API = "DeviceDebug";
    public static final String GET_DEVICE_SIM_NUMBER_API = "PumpSetCode";



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
