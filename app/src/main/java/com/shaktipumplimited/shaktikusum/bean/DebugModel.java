
package com.shaktipumplimited.shaktikusum.bean;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class DebugModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private Long mResponse;
    @SerializedName("status")
    private Boolean mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getResponse() {
        return mResponse;
    }

    public void setResponse(Long response) {
        mResponse = response;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
