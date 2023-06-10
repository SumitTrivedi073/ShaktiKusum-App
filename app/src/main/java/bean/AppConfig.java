package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfig {
    @SerializedName("minKusumAppVersion")
    @Expose
    private String minKusumAppVersion;
    @SerializedName("kusumAppUrl")
    @Expose
    private String kusumAppUrl;

    public String getMinKusumAppVersion() {
        return minKusumAppVersion;
    }

    public void setMinKusumAppVersion(String minKusumAppVersion) {
        this.minKusumAppVersion = minKusumAppVersion;
    }

    public String getKusumAppUrl() {
        return kusumAppUrl;
    }

    public void setKusumAppUrl(String kusumAppUrl) {
        this.kusumAppUrl = kusumAppUrl;
    }
}
