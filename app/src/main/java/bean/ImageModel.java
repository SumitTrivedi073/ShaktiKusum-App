package bean;

public class ImageModel {

    String name,ImagePath,billNo;
    boolean isImageSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public boolean isImageSelected() {
        return isImageSelected;
    }

    public void setImageSelected(boolean imageSelected) {
        isImageSelected = imageSelected;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}

