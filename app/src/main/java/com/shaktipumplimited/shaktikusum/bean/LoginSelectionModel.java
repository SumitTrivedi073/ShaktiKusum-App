package com.shaktipumplimited.shaktikusum.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginSelectionModel {
    @SerializedName("login_type")
    @Expose
    private List<LoginType> loginType;

    public List<LoginType> getLoginType() {
        return loginType;
    }

    public void setLoginType(List<LoginType> loginType) {
        this.loginType = loginType;
    }

    public class LoginType {

        @SerializedName("project_no")
        @Expose
        private String projectNo;
        @SerializedName("project_nm")
        @Expose
        private String projectNm;
        @SerializedName("project_login_no")
        @Expose
        private String projectLoginNo;
        @SerializedName("project_login_nm")
        @Expose
        private String projectLoginNm;

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public String getProjectNm() {
            return projectNm;
        }

        public void setProjectNm(String projectNm) {
            this.projectNm = projectNm;
        }

        public String getProjectLoginNo() {
            return projectLoginNo;
        }

        public void setProjectLoginNo(String projectLoginNo) {
            this.projectLoginNo = projectLoginNo;
        }

        public String getProjectLoginNm() {
            return projectLoginNm;
        }

        public void setProjectLoginNm(String projectLoginNm) {
            this.projectLoginNm = projectLoginNm;
        }

    }
}
