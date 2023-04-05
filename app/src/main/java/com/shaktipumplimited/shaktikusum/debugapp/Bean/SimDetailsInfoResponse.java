
package com.shaktipumplimited.shaktikusum.debugapp.Bean;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SimDetailsInfoResponse {

    @SerializedName("DEVICE_NO_SIM_BENIFICRY")
    private String mDEVICENOSIMBENIFICRY;
    @SerializedName("DEVICE_NO_SIM_BILL")
    private String mDEVICENOSIMBILL;
    @SerializedName("DEVICE_NO_SIM_CON")
    private String mDEVICENOSIMCON;
    @SerializedName("DEVICE_NO_SIM_MOB")
    private String mDEVICENOSIMMOB;
    @SerializedName("DEVICE_NO_SIM_USERID")
    private String mDEVICENOSIMUSERID;

    public String getDEVICENOSIMBENIFICRY() {
        return mDEVICENOSIMBENIFICRY;
    }

    public void setDEVICENOSIMBENIFICRY(String dEVICENOSIMBENIFICRY) {
        mDEVICENOSIMBENIFICRY = dEVICENOSIMBENIFICRY;
    }

    public String getDEVICENOSIMBILL() {
        return mDEVICENOSIMBILL;
    }

    public void setDEVICENOSIMBILL(String dEVICENOSIMBILL) {
        mDEVICENOSIMBILL = dEVICENOSIMBILL;
    }

    public String getDEVICENOSIMCON() {
        return mDEVICENOSIMCON;
    }

    public void setDEVICENOSIMCON(String dEVICENOSIMCON) {
        mDEVICENOSIMCON = dEVICENOSIMCON;
    }

    public String getDEVICENOSIMMOB() {
        return mDEVICENOSIMMOB;
    }

    public void setDEVICENOSIMMOB(String dEVICENOSIMMOB) {
        mDEVICENOSIMMOB = dEVICENOSIMMOB;
    }

    public String getDEVICENOSIMUSERID() {
        return mDEVICENOSIMUSERID;
    }

    public void setDEVICENOSIMUSERID(String dEVICENOSIMUSERID) {
        mDEVICENOSIMUSERID = dEVICENOSIMUSERID;
    }

}
