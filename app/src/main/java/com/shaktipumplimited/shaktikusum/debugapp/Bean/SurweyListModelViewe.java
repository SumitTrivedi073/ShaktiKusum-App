
package com.shaktipumplimited.shaktikusum.debugapp.Bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SurweyListModelViewe {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<SurweyListResponse> mResponse;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<SurweyListResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<SurweyListResponse> response) {
        mResponse = response;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
