package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class SimCardBean {


    public String user_id,
                   user_type,
                   enq_docno,
                   device_no,
                   sim_rep_date,
                   cust_name,
                   cust_mobile,
                   cust_address,
                   sim_lat,
                   sim_lng,
                   sim_new_no,
                   sim_old_no,
                   sim_new_photo,
                   drive_photo,
                   sim_old_photo;


    public SimCardBean() {


    }



    public SimCardBean(String user_id_text,
                       String user_type_text,
                       String enq_docno_text,
                       String device_no_text,
                       String sim_rep_date_text,
                       String cust_name_text,
                       String cust_mobile_text,
                       String cust_address_text,
                       String sim_lat_text,
                       String sim_lng_text,
                       String sim_new_no_text,
                       String sim_old_no_text,
                       String sim_new_photo_text,
                       String drive_photo_text,
                       String sim_old_photo_text


    ) {

        user_id =  user_id_text;
        user_type =  user_type_text;
        enq_docno =  enq_docno_text;
        device_no =  device_no_text;
        sim_rep_date  =  sim_rep_date_text;
        cust_name =  cust_name_text;
        cust_mobile =  cust_mobile_text;
        cust_address =  cust_address_text;
        sim_lat =  sim_lat_text;
        sim_lng =  sim_lng_text;
        sim_new_no =  sim_new_no_text;
        sim_old_no =  sim_old_no_text;
        sim_new_photo =  sim_new_photo_text;
        drive_photo =  drive_photo_text;
        sim_old_photo =  sim_old_photo_text;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getEnq_docno() {
        return enq_docno;
    }

    public void setEnq_docno(String enq_docno) {
        this.enq_docno = enq_docno;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getSim_rep_date() {
        return sim_rep_date;
    }

    public void setSim_rep_date(String sim_rep_date) {
        this.sim_rep_date = sim_rep_date;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_mobile() {
        return cust_mobile;
    }

    public void setCust_mobile(String cust_mobile) {
        this.cust_mobile = cust_mobile;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public String getSim_lat() {
        return sim_lat;
    }

    public void setSim_lat(String sim_lat) {
        this.sim_lat = sim_lat;
    }

    public String getSim_lng() {
        return sim_lng;
    }

    public void setSim_lng(String sim_lng) {
        this.sim_lng = sim_lng;
    }

    public String getSim_new_no() {
        return sim_new_no;
    }

    public void setSim_new_no(String sim_new_no) {
        this.sim_new_no = sim_new_no;
    }

    public String getSim_old_no() {
        return sim_old_no;
    }

    public void setSim_old_no(String sim_old_no) {
        this.sim_old_no = sim_old_no;
    }

    public String getSim_new_photo() {
        return sim_new_photo;
    }

    public void setSim_new_photo(String sim_new_photo) {
        this.sim_new_photo = sim_new_photo;
    }

    public String getSim_old_photo() {
        return sim_old_photo;
    }

    public void setSim_old_photo(String sim_old_photo) {
        this.sim_old_photo = sim_old_photo;
    }

    public String getDrive_photo() {
        return drive_photo;
    }

    public void setDrive_photo(String drive_photo) {
        this.drive_photo = drive_photo;
    }
}
