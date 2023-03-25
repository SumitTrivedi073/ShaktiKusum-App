package bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Created by Administrator on 12/30/2016.
 */
public class RejectListBean implements Parcelable {


    public String billno, benno, regno, custnm, photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12, remark1, remark2, remark3, remark4,
            remark5, remark6, remark7, remark8, remark9, remark10, remark11, remark12;


    public RejectListBean() {


    }

    public RejectListBean(String billno, String benno, String regno, String custnm, String photo1, String photo2, String photo3, String photo4, String photo5, String photo6,
                          String photo7, String photo8, String photo9, String photo10, String photo11, String photo12, String remark1, String remark2, String remark3,
                          String remark4, String remark5, String remark6, String remark7, String remark8, String remark9, String remark10, String remark11, String remark12) {
        this.billno = billno;
        this.benno = benno;
        this.regno = regno;
        this.custnm = custnm;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.photo4 = photo4;
        this.photo5 = photo5;
        this.photo6 = photo6;
        this.photo7 = photo7;
        this.photo8 = photo8;
        this.photo9 = photo9;
        this.photo10 = photo10;
        this.photo11 = photo11;
        this.photo12 = photo12;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.remark4 = remark4;
        this.remark5 = remark5;
        this.remark6 = remark6;
        this.remark7 = remark7;
        this.remark8 = remark8;
        this.remark9 = remark9;
        this.remark10 = remark10;
        this.remark11 = remark11;
        this.remark12 = remark12;
    }

    protected RejectListBean(Parcel in) {
        billno = in.readString();
        benno = in.readString();
        regno = in.readString();
        custnm = in.readString();
        photo1 = in.readString();
        photo2 = in.readString();
        photo3 = in.readString();
        photo4 = in.readString();
        photo5 = in.readString();
        photo6 = in.readString();
        photo7 = in.readString();
        photo8 = in.readString();
        photo9 = in.readString();
        photo10 = in.readString();
        photo11 = in.readString();
        photo12 = in.readString();
        remark1 = in.readString();
        remark2 = in.readString();
        remark3 = in.readString();
        remark4 = in.readString();
        remark5 = in.readString();
        remark6 = in.readString();
        remark7 = in.readString();
        remark8 = in.readString();
        remark9 = in.readString();
        remark10 = in.readString();
        remark11 = in.readString();
        remark12 = in.readString();
    }

    public static final Creator<RejectListBean> CREATOR = new Creator<RejectListBean>() {
        @Override
        public RejectListBean createFromParcel(Parcel in) {
            return new RejectListBean(in);
        }

        @Override
        public RejectListBean[] newArray(int size) {
            return new RejectListBean[size];
        }
    };

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBenno() {
        return benno;
    }

    public void setBenno(String benno) {
        this.benno = benno;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getCustnm() {
        return custnm;
    }

    public void setCustnm(String custnm) {
        this.custnm = custnm;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getPhoto6() {
        return photo6;
    }

    public void setPhoto6(String photo6) {
        this.photo6 = photo6;
    }

    public String getPhoto7() {
        return photo7;
    }

    public void setPhoto7(String photo7) {
        this.photo7 = photo7;
    }

    public String getPhoto8() {
        return photo8;
    }

    public void setPhoto8(String photo8) {
        this.photo8 = photo8;
    }

    public String getPhoto9() {
        return photo9;
    }

    public void setPhoto9(String photo9) {
        this.photo9 = photo9;
    }

    public String getPhoto10() {
        return photo10;
    }

    public void setPhoto10(String photo10) {
        this.photo10 = photo10;
    }

    public String getPhoto11() {
        return photo11;
    }

    public void setPhoto11(String photo11) {
        this.photo11 = photo11;
    }

    public String getPhoto12() {
        return photo12;
    }

    public void setPhoto12(String photo12) {
        this.photo12 = photo12;
    }


    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    public String getRemark5() {
        return remark5;
    }

    public void setRemark5(String remark5) {
        this.remark5 = remark5;
    }

    public String getRemark6() {
        return remark6;
    }

    public void setRemark6(String remark6) {
        this.remark6 = remark6;
    }

    public String getRemark7() {
        return remark7;
    }

    public void setRemark7(String remark7) {
        this.remark7 = remark7;
    }

    public String getRemark8() {
        return remark8;
    }

    public void setRemark8(String remark8) {
        this.remark8 = remark8;
    }

    public String getRemark9() {
        return remark9;
    }

    public void setRemark9(String remark9) {
        this.remark9 = remark9;
    }

    public String getRemark10() {
        return remark10;
    }

    public void setRemark10(String remark10) {
        this.remark10 = remark10;
    }

    public String getRemark11() {
        return remark11;
    }

    public void setRemark11(String remark11) {
        this.remark11 = remark11;
    }

    public String getRemark12() {
        return remark12;
    }

    public void setRemark12(String remark12) {
        this.remark12 = remark12;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(billno);
        parcel.writeString(benno);
        parcel.writeString(regno);
        parcel.writeString(custnm);
        parcel.writeString(photo1);
        parcel.writeString(photo2);
        parcel.writeString(photo3);
        parcel.writeString(photo4);
        parcel.writeString(photo5);
        parcel.writeString(photo6);
        parcel.writeString(photo7);
        parcel.writeString(photo8);
        parcel.writeString(photo9);
        parcel.writeString(photo10);
        parcel.writeString(photo11);
        parcel.writeString(photo12);
        parcel.writeString(remark1);
        parcel.writeString(remark2);
        parcel.writeString(remark3);
        parcel.writeString(remark4);
        parcel.writeString(remark5);
        parcel.writeString(remark6);
        parcel.writeString(remark7);
        parcel.writeString(remark8);
        parcel.writeString(remark9);
        parcel.writeString(remark10);
        parcel.writeString(remark11);
        parcel.writeString(remark12);
    }
}
