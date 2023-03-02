
package com.shaktipumplimited.SettingModel;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class SettingParameterResponse {

    @SerializedName("factor")
    private int mFactor;
    @SerializedName("modbusaddress")
    private String mModbusaddress;

    @SerializedName("mobBTAddress")
    private String mMobBTAddress;
    @SerializedName("moterid")
    private String mMoterid;

    @SerializedName("materialCode")
    private String mMaterialCode;
    @SerializedName("pValue")
    private int mPValue;

    @SerializedName("offset")
    private int mOffset;

    @SerializedName("parametersName")
    private String mParametersName;

    @SerializedName("unit")
    private String mUnit;

    @SerializedName("pmId")
    private String mPmId;

    public int getFactor() {
        return mFactor;
    }

    public void setFactor(int factor) {
        mFactor = factor;
    }

    public String getModbusaddress() {
        return mModbusaddress;
    }

    public void setModbusaddress(String modbusaddress) {
        mModbusaddress = modbusaddress;
    }

    public String getMobBTAddress() {
        return mMobBTAddress;
    }

    public void setMobBTAddress(String mobBTAddress) {
        mMobBTAddress = mobBTAddress;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }

    public String getMoterid() {
        return mMoterid;
    }

    public void setMoterid(String moterid) {
        mMoterid = moterid;
    }


    public String getMaterialCode() {
        return mMaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        mMaterialCode = materialCode;
    }

    public int getPValue() {
        return mPValue;
    }

    public void setPValue(int pValue) {
        mPValue = pValue;
    }

    public String getParametersName() {
        return mParametersName;
    }

    public void setParametersName(String parametersName) {
        mParametersName = parametersName;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    public String getPmId() {
        return mPmId;
    }

    public void setPmId(String pmId) {
        mPmId = pmId;
    }

}
