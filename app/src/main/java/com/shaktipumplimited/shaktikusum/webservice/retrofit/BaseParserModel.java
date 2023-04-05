package com.shaktipumplimited.shaktikusum.webservice.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 10/29/2016.
 */
public class BaseParserModel<T> {


    @SerializedName("status")
    String status = "";
    @SerializedName("code")
    public String code = "";
    @SerializedName("success message")
    public String message = "";
    @SerializedName("apiname")
    public String apiname;

//    public void setListChallanModelList(List<ChallanDetailModel> listChallanModelList) {
//        this.listChallanModelList = listChallanModelList;
//    }
}
