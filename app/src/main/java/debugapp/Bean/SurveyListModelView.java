
package debugapp.Bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SurveyListModelView {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<SurveyListResponse> mResponse;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<SurveyListResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<SurveyListResponse> response) {
        mResponse = response;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
