package bean;

public class DeviceMappingModel {

   private  String id,read,write,update,billNo;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getRead() {
      return read;
   }

   public void setRead(String read) {
      this.read = read;
   }

   public String getWrite() {
      return write;
   }

   public void setWrite(String write) {
      this.write = write;
   }

   public String getUpdate() {
      return update;
   }

   public void setUpdate(String update) {
      this.update = update;
   }

   public String getBillNo() {
      return billNo;
   }

   public void setBillNo(String billNo) {
      this.billNo = billNo;
   }
   @Override
   public String toString() {
      return "DeviceMappingModel{" +
              "id='" + id + '\'' +
              ", read='" + read + '\'' +
              ", write='" + write + '\'' +
              ", update='" + update + '\'' +
              ", billNo='" + billNo + '\'' +
              '}';
   }

}
