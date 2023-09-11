package debugapp.localDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.List;

import bean.BTResonseData;
import debugapp.Bean.SimDetailsInfoResponse;

/**
 * Created by Sayem on 12/5/2017.
 */
public class DatabaseHelperTeacher extends SQLiteOpenHelper {


    ///*********************************end ****************//////////
    public static String DATABASE_NAME_RMS = "debug_database";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelperTeacher(Context context) {
        super(context, DATABASE_NAME_RMS, null, DATABASE_VERSION);
        // Log.d("table", CREATE_TABLE_TEACHERS);
    }

    private static final String LOGIN_KEY_ID = "id";

    /////////////////////////////device Status Nikola
    private static final String DEVICE_DEVICE_NO = "DEVICE_NO";
    private static final String DEVICE_SIGNL_STREN = "SIGNL_STREN";
    private static final String DEVICE_SIM = "SIM";
    private static final String DEVICE_NET_REG = "NET_REG";
    private static final String DEVICE_SER_CONNECT = "SER_CONNECT";
    private static final String DEVICE_CAB_CONNECT = "CAB_CONNECT";
    private static final String DEVICE_LATITUDE = "LATITUDE";
    private static final String DEVICE_LANGITUDE = "LANGITUDE";
    private static final String DEVICE_MOBILE = "MOBILE";
    private static final String DEVICE_IMEI = "IMEI";
    private static final String DEVICE_DONGAL_ID = "DONGAL_ID";
    private static final String DEVICE_MUserId = "MUserId";
    private static final String DEVICE_RMS_STATUS = "RMS_STATUS";
    private static final String DEVICE_INS_NAME = "INST_NAME";
    private static final String DEVICE_INS_MOBILE = "INST_MOBILE";

    private static final String DEVICE_RMS_SERVER_DOWN = "RMS_SERVER_DOWN";
    private static final String DEVICE_RMS_DEBUG_EXTRN = "RMS_DEBUG_EXTRN";
    private static final String DEVICE_RMS_ORG_D_F = "RMS_ORG_D_F";

    private static final String DEVICE_RMS_LAST_ONLINE_DATE = "RMS_LAST_ONLINE_DATE";
    private static final String DEVICE_RMS_CURRENT_ONLINE_STATUS = "RMS_CURRENT_ONLINE_STATUS";

    private static final String DEVICE_Nikola_TEST1 = "TestOne";
    private static final String DEVICE_Nikola_TEST2 = "TestTwo";
    private static final String DEVICE_Nikola_TEST3 = "TestThree";

    private static final String DEVICE_DEVICE_INFO_NAME = "deviceinfotable";////////////////table name
    private static final String DEVICE_SAVE_SIM_INFO_NAME = "devicesiminfotable";////////////////table name

    private static final String DEVICE_NO_SIM_CON = "DEVICE_NO_SIM_CON";
    private static final String DEVICE_NO_SIM_MOB = "DEVICE_NO_SIM_MOB";
    private static final String DEVICE_NO_SIM_BENIFICRY = "DEVICE_NO_SIM_BENIFICRY";
    private static final String DEVICE_NO_SIM_BILL = "DEVICE_NO_SIM_BILL";
    private static final String DEVICE_NO_SIM_USERID = "DEVICE_NO_SIM_USERID";


    private static final String CREATE_TABLE_DEVICE_INFO_SIM = "CREATE TABLE "
            + DEVICE_SAVE_SIM_INFO_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_NO_SIM_CON + " VARCHAR, " +
            DEVICE_NO_SIM_MOB + " VARCHAR, " +
            DEVICE_NO_SIM_BENIFICRY + " VARCHAR, " +
            DEVICE_NO_SIM_BILL + " VARCHAR, " +
            DEVICE_NO_SIM_USERID + " VARCHAR " +
            "); ";

    public long insertSimInfoData(String mDEVICE_NO_SIM_CON, String mDEVICE_NO_SIM_MOB, String mDEVICE_NO_SIM_BENIFICRY, String mDEVICE_NO_SIM_BILL, String mDEVICE_NO_SIM_USERID, boolean mCheckFirst) {
        int ccccc = 0;
        long insert = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //db.execSQL("SELECT * from " + DEVICE_SAVE_SIM_INFO_NAME + " where " + DEVICE_NO_SIM_MOB + "='" + mDEVICE_NO_SIM_MOB+"'");
        /*    if(DEVICE_NO_SIM_MOB.equalsIgnoreCase("Not Available"))
            {

            }*/
            Cursor cursor = db.rawQuery("SELECT * from " + DEVICE_SAVE_SIM_INFO_NAME + " where " + DEVICE_NO_SIM_MOB + "='" + mDEVICE_NO_SIM_MOB+"' and " + DEVICE_NO_SIM_BILL + "='" + mDEVICE_NO_SIM_BILL+"'", null);
            ccccc = cursor.getCount();



        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(DEVICE_NO_SIM_CON, mDEVICE_NO_SIM_CON);
        values.put(DEVICE_NO_SIM_MOB, mDEVICE_NO_SIM_MOB);
        values.put(DEVICE_NO_SIM_BENIFICRY, mDEVICE_NO_SIM_BENIFICRY);
        values.put(DEVICE_NO_SIM_BILL, mDEVICE_NO_SIM_BILL);
        values.put(DEVICE_NO_SIM_USERID, mDEVICE_NO_SIM_USERID);


        if(ccccc > 1)
        {
             insert = db.update(DEVICE_SAVE_SIM_INFO_NAME, values, DEVICE_NO_SIM_MOB + "='" + mDEVICE_NO_SIM_MOB+"'", null);
            System.out.println("insert_Data_update ==>"+insert);
        }
        else
        {
             insert = db.insert(DEVICE_SAVE_SIM_INFO_NAME, null, values);

            System.out.println("insert_Data ==>"+insert);
        }


        return insert;

    }

    public long deleteSimInfoData(String mBillNo)
    {
        SQLiteDatabase db = this.getReadableDatabase();

      //  Cursor cursor = db.rawQuery("DELETE FROM " + DEVICE_SAVE_SIM_INFO_NAME, null);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("DELETE FROM " + DEVICE_SAVE_SIM_INFO_NAME + " where " + DEVICE_NO_SIM_BILL + "='" + mBillNo+"'", null);
        //   Cursor cursor = db.rawQuery(selectQuery, null);
        int ccccc = cursor.getCount();
        return ccccc;
    }


    @SuppressLint("Range")
    public List<SimDetailsInfoResponse> getSimInfoDATABT(String mBillNo) {
        List<SimDetailsInfoResponse> mBTResonseDataList = new ArrayList<>();
        SimDetailsInfoResponse mBTResonseData ;
        //customer_gps = new Customer_GPS_Search();
        String mAllUserInfo = null;
        // int id=-1; //0
        String id = ""; //0

        boolean mCheckFirst;

        String mDEVICE_NO_SIM_CON,  mDEVICE_NO_SIM_MOB,  mDEVICE_NO_SIM_BENIFICRY,  mDEVICE_NO_SIM_BILL,  mDEVICE_NO_SIM_USERID;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_PARAMETER_TABLE_NAME +" where "+DEVICE_PARA_DEVICE_TYPE+"=?", new String[]{ DeviceType });
            Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_SAVE_SIM_INFO_NAME +" where " + DEVICE_NO_SIM_BILL + "='" + mBillNo+"'", null);
            int ccccc = cursor.getCount();

            System.out.println("ccccc==>>"+ccccc);

            int PU = 0;
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    //  for(PU = 0 ; PU < ccccc ; PU++)
                    while (!cursor.isAfterLast()) {
                        // mKLPTotEnergyResponse = new KLPTotEnergyResponse();
                        mBTResonseData = new SimDetailsInfoResponse();
                        id = cursor.getString(cursor.getColumnIndex(LOGIN_KEY_ID));
                        mDEVICE_NO_SIM_CON = cursor.getString(cursor.getColumnIndex(DEVICE_NO_SIM_CON));
                        mDEVICE_NO_SIM_MOB = cursor.getString(cursor.getColumnIndex(DEVICE_NO_SIM_MOB));
                        mDEVICE_NO_SIM_BENIFICRY = cursor.getString(cursor.getColumnIndex(DEVICE_NO_SIM_BENIFICRY));
                        mDEVICE_NO_SIM_BILL = cursor.getString(cursor.getColumnIndex(DEVICE_NO_SIM_BILL));
                        mDEVICE_NO_SIM_USERID = cursor.getString(cursor.getColumnIndex(DEVICE_NO_SIM_USERID));

                      //  mBTResonseData.setID(id);
                        mBTResonseData.setDEVICENOSIMCON(mDEVICE_NO_SIM_CON);
                        mBTResonseData.setDEVICENOSIMMOB(mDEVICE_NO_SIM_MOB);
                        mBTResonseData.setDEVICENOSIMBENIFICRY(mDEVICE_NO_SIM_BENIFICRY);
                        mBTResonseData.setDEVICENOSIMBILL(mDEVICE_NO_SIM_BILL);
                        mBTResonseData.setDEVICENOSIMUSERID(mDEVICE_NO_SIM_USERID);

                        mBTResonseDataList.add(mBTResonseData);
                        cursor.moveToNext();
                    }
                }

            }////////////addd chaech
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null && db.inTransaction()) {
                db.close();
            }
            // Close database
        }
        //mAllUserInfo = id+"SAK000IVS"+mUserID+"SAK000IVS"+mParentID+"SAK000IVS"+mUserName+"SAK000IVS"+mUserPhone+"SAK000IVS"+mClientID+"SAK000IVS"+mISLogin+"SAK000IVS"+mLoginStatus+"SAK000IVS"+mLoginActive;
        return mBTResonseDataList;
    }


    private static final String CREATE_TABLE_DEVICE_INFO = "CREATE TABLE "
            + DEVICE_DEVICE_INFO_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_DEVICE_NO + " VARCHAR, " +
            DEVICE_SIGNL_STREN + " VARCHAR, " +
            DEVICE_SIM + " VARCHAR, " +
            DEVICE_NET_REG + " VARCHAR, " +
            DEVICE_SER_CONNECT + " VARCHAR, " +
            DEVICE_CAB_CONNECT + " VARCHAR, " +
            DEVICE_LATITUDE + " VARCHAR, " +
            DEVICE_LANGITUDE + " VARCHAR, " +
            DEVICE_MOBILE + " VARCHAR, " +
            DEVICE_IMEI + " VARCHAR, " +
            DEVICE_DONGAL_ID + " VARCHAR, " +
            DEVICE_MUserId + " VARCHAR ," +
            DEVICE_RMS_STATUS + " VARCHAR " +
            DEVICE_INS_NAME + " VARCHAR " +
            DEVICE_INS_MOBILE + " VARCHAR " +
            DEVICE_RMS_SERVER_DOWN + " VARCHAR " +
            DEVICE_RMS_DEBUG_EXTRN + " VARCHAR " +
           // DEVICE_RMS_ORG_D_F + " VARCHAR " +
            DEVICE_RMS_CURRENT_ONLINE_STATUS + " VARCHAR " +
            DEVICE_RMS_LAST_ONLINE_DATE + " VARCHAR " +
            "); ";




    ////////////////////////////////insert Device status data

    public long insertDeviceDebugInforData(String mDEVICE_DEVICE_NO, String mDEVICE_SIGNL_STREN, String mDEVICE_SIM, String mDEVICE_NET_REG, String mDEVICE_SER_CONNECT,
                                       String mDEVICE_CAB_CONNECT,
                                       String mDEVICE_LATITUDE, String mDEVICE_LANGITUDE, String mDEVICE_MOBILE,String mDEVICE_IMEI,
                                       String mDEVICE_DONGAL_ID, String mDEVICE_MUserId,String RMS_STATUS,
                                           String RMS_CURRENT_ONLINE_STATUS, String RMS_LAST_ONLINE_DATE,
                                           String mDEVICE_INS_NAME, String mDEVICE_INS_MOBILE,
                                           String RMS_DEBUG_EXTRN,String RMS_SERVER_DOWN, String RMS_ORG_D_F, boolean mCheckFirst) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(DEVICE_DEVICE_NO, mDEVICE_DEVICE_NO);
        values.put(DEVICE_SIGNL_STREN, mDEVICE_SIGNL_STREN);
        values.put(DEVICE_SIM, mDEVICE_SIM);

        values.put(DEVICE_NET_REG, mDEVICE_NET_REG);
        values.put(DEVICE_SER_CONNECT, mDEVICE_SER_CONNECT);
        values.put(DEVICE_CAB_CONNECT, mDEVICE_CAB_CONNECT);

        values.put(DEVICE_LATITUDE,mDEVICE_LATITUDE);
        values.put(DEVICE_LANGITUDE, mDEVICE_LANGITUDE);
        values.put(DEVICE_MOBILE, mDEVICE_MOBILE);

        values.put(DEVICE_IMEI, mDEVICE_IMEI);
        values.put(DEVICE_DONGAL_ID, mDEVICE_DONGAL_ID);
        values.put(DEVICE_MUserId, mDEVICE_MUserId);
        values.put(DEVICE_RMS_STATUS, RMS_STATUS);



        //insert row in table
        long insert = db.insert(DEVICE_DEVICE_INFO_NAME, null, values);

        System.out.println("insert==mm"+insert);
        return insert;

    }

    @SuppressLint("Range")
    public List<BTResonseData> getDeviceInfoDATABT() {
        List<BTResonseData> mBTResonseDataList = new ArrayList<>();
        BTResonseData mBTResonseData ;
        //customer_gps = new Customer_GPS_Search();
        String mAllUserInfo = null;
        // int id=-1; //0
        String id = ""; //0

        String mDEVICE_DEVICE_NO,  mDEVICE_SIGNL_STREN,  mDEVICE_SIM,  mDEVICE_NET_REG,  mDEVICE_SER_CONNECT,
                 mDEVICE_CAB_CONNECT,
                 mDEVICE_LATITUDE,  mDEVICE_LANGITUDE,  mDEVICE_MOBILE, mDEVICE_IMEI,
                 mDEVICE_DONGAL_ID,  mDEVICE_MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mDEVICE_INS_NAME, mDEVICE_INS_MOBILE, RMS_SERVER_DOWN, RMS_DEBUG_EXTRN, RMS_ORG_D_F;  boolean mCheckFirst;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_PARAMETER_TABLE_NAME +" where "+DEVICE_PARA_DEVICE_TYPE+"=?", new String[]{ DeviceType });
            Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_DEVICE_INFO_NAME, null);
            int ccccc = cursor.getCount();
            int PU = 0;
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                   while (!cursor.isAfterLast()) {
                        mBTResonseData = new BTResonseData();
                        id = cursor.getString(cursor.getColumnIndex(LOGIN_KEY_ID));
                        mDEVICE_DEVICE_NO = cursor.getString(cursor.getColumnIndex(DEVICE_DEVICE_NO));
                        mDEVICE_SIGNL_STREN = cursor.getString(cursor.getColumnIndex(DEVICE_SIGNL_STREN));
                        mDEVICE_SIM = cursor.getString(cursor.getColumnIndex(DEVICE_SIM));
                        mDEVICE_NET_REG = cursor.getString(cursor.getColumnIndex(DEVICE_NET_REG));
                        mDEVICE_SER_CONNECT = cursor.getString(cursor.getColumnIndex(DEVICE_SER_CONNECT));
                        mDEVICE_CAB_CONNECT = cursor.getString(cursor.getColumnIndex(DEVICE_CAB_CONNECT));
                        mDEVICE_LATITUDE = cursor.getString(cursor.getColumnIndex(DEVICE_LATITUDE));
                        mDEVICE_LANGITUDE = cursor.getString(cursor.getColumnIndex(DEVICE_LANGITUDE));
                        mDEVICE_MOBILE = cursor.getString(cursor.getColumnIndex(DEVICE_MOBILE));
                        mDEVICE_IMEI = cursor.getString(cursor.getColumnIndex(DEVICE_IMEI));
                        mDEVICE_DONGAL_ID = cursor.getString(cursor.getColumnIndex(DEVICE_DONGAL_ID));
                        mDEVICE_MUserId = cursor.getString(cursor.getColumnIndex(DEVICE_MUserId));
                        RMS_STATUS = cursor.getString(cursor.getColumnIndex(DEVICE_RMS_STATUS));

                        mBTResonseData.setID(id);
                        mBTResonseData.setDEVICENO(mDEVICE_DEVICE_NO);
                        mBTResonseData.setSIGNLSTREN(mDEVICE_SIGNL_STREN);
                        mBTResonseData.setSIM(mDEVICE_SIM);
                        mBTResonseData.setNETREG(mDEVICE_NET_REG);
                        mBTResonseData.setSERCONNECT(mDEVICE_SER_CONNECT);
                        mBTResonseData.setCABCONNECT(mDEVICE_CAB_CONNECT);
                        mBTResonseData.setLATITUDE(mDEVICE_LATITUDE);
                        mBTResonseData.setLANGITUDE(mDEVICE_LANGITUDE);
                        mBTResonseData.setMOBILE(mDEVICE_MOBILE);
                        mBTResonseData.setIMEI(mDEVICE_IMEI);
                        mBTResonseData.setDONGALID(mDEVICE_DONGAL_ID);
                        mBTResonseData.setKUNNR(mDEVICE_MUserId);
                        mBTResonseData.setRMS_STATUS(RMS_STATUS);

                        mBTResonseDataList.add(mBTResonseData);
                        cursor.moveToNext();
                    }
                }

            }////////////addd chaech
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null && db.inTransaction()) {
                db.close();
            }
            // Close database
        }
        //mAllUserInfo = id+"SAK000IVS"+mUserID+"SAK000IVS"+mParentID+"SAK000IVS"+mUserName+"SAK000IVS"+mUserPhone+"SAK000IVS"+mClientID+"SAK000IVS"+mISLogin+"SAK000IVS"+mLoginStatus+"SAK000IVS"+mLoginActive;
        return mBTResonseDataList;
    }

    @SuppressLint("Range")
    public List<BTResonseData> getDeviceInfoDATABTFindDebug(String mDeviceNo) {
        List<BTResonseData> mBTResonseDataList = new ArrayList<>();
        BTResonseData mBTResonseData ;
        String id = "";

        String mDEVICE_DEVICE_NO,  mDEVICE_SIGNL_STREN,  mDEVICE_SIM,  mDEVICE_NET_REG,  mDEVICE_SER_CONNECT,
                mDEVICE_CAB_CONNECT,
                mDEVICE_LATITUDE,  mDEVICE_LANGITUDE,  mDEVICE_MOBILE, mDEVICE_IMEI,
                mDEVICE_DONGAL_ID,  mDEVICE_MUserId, RMS_STATUS, RMS_CURRENT_ONLINE_STATUS, RMS_LAST_ONLINE_DATE, mDEVICE_INS_NAME, mDEVICE_INS_MOBILE, RMS_SERVER_DOWN, RMS_DEBUG_EXTRN, RMS_ORG_D_F;  boolean mCheckFirst;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
             Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_DEVICE_INFO_NAME +" WHERE DEVICE_NO ='" + mDeviceNo + "'", null);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        mBTResonseData = new BTResonseData();
                        id = cursor.getString(cursor.getColumnIndex(LOGIN_KEY_ID));
                        mDEVICE_DEVICE_NO = cursor.getString(cursor.getColumnIndex(DEVICE_DEVICE_NO));
                        mDEVICE_SIGNL_STREN = cursor.getString(cursor.getColumnIndex(DEVICE_SIGNL_STREN));
                        mDEVICE_SIM = cursor.getString(cursor.getColumnIndex(DEVICE_SIM));
                        mDEVICE_NET_REG = cursor.getString(cursor.getColumnIndex(DEVICE_NET_REG));
                        mDEVICE_SER_CONNECT = cursor.getString(cursor.getColumnIndex(DEVICE_SER_CONNECT));
                        mDEVICE_CAB_CONNECT = cursor.getString(cursor.getColumnIndex(DEVICE_CAB_CONNECT));
                        mDEVICE_LATITUDE = cursor.getString(cursor.getColumnIndex(DEVICE_LATITUDE));
                        mDEVICE_LANGITUDE = cursor.getString(cursor.getColumnIndex(DEVICE_LANGITUDE));
                        mDEVICE_MOBILE = cursor.getString(cursor.getColumnIndex(DEVICE_MOBILE));
                        mDEVICE_IMEI = cursor.getString(cursor.getColumnIndex(DEVICE_IMEI));
                        mDEVICE_DONGAL_ID = cursor.getString(cursor.getColumnIndex(DEVICE_DONGAL_ID));
                        mDEVICE_MUserId = cursor.getString(cursor.getColumnIndex(DEVICE_MUserId));
                        RMS_STATUS = cursor.getString(cursor.getColumnIndex(DEVICE_RMS_STATUS));

                        mBTResonseData.setID(id);
                        mBTResonseData.setDEVICENO(mDEVICE_DEVICE_NO);
                        mBTResonseData.setSIGNLSTREN(mDEVICE_SIGNL_STREN);
                        mBTResonseData.setSIM(mDEVICE_SIM);
                        mBTResonseData.setNETREG(mDEVICE_NET_REG);
                        mBTResonseData.setSERCONNECT(mDEVICE_SER_CONNECT);
                        mBTResonseData.setCABCONNECT(mDEVICE_CAB_CONNECT);
                        mBTResonseData.setLATITUDE(mDEVICE_LATITUDE);
                        mBTResonseData.setLANGITUDE(mDEVICE_LANGITUDE);
                        mBTResonseData.setMOBILE(mDEVICE_MOBILE);
                        mBTResonseData.setIMEI(mDEVICE_IMEI);
                        mBTResonseData.setDONGALID(mDEVICE_DONGAL_ID);
                        mBTResonseData.setKUNNR(mDEVICE_MUserId);
                        mBTResonseData.setRMS_STATUS(RMS_STATUS);

                        mBTResonseDataList.add(mBTResonseData);
                        cursor.moveToNext();
                    }
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null && db.inTransaction()) {
                db.close();
            }
            // Close database
        }
        return mBTResonseDataList;
    }


    public long deleteAllDataFromTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("DELETE FROM " + DEVICE_DEVICE_INFO_NAME, null);
     //   Cursor cursor = db.rawQuery(selectQuery, null);
        int ccccc = cursor.getCount();
        return ccccc;
    }

    public long deleteAllDataFromTableSim()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("DELETE FROM " + DEVICE_SAVE_SIM_INFO_NAME, null);
     //   Cursor cursor = db.rawQuery(selectQuery, null);
        int ccccc = cursor.getCount();
        return ccccc;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

      db.execSQL(CREATE_TABLE_DEVICE_INFO);
      db.execSQL(CREATE_TABLE_DEVICE_INFO_SIM);
      db.execSQL(CREATE_TABLE_DEVICE_INFO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_DEVICE_INFO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_DEVICE_INFO_SIM + "'");

    }
}
