package bean;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SelfCheckImageBean implements Serializable {
        private Bitmap image;

        public Bitmap getImage1() {
            return image;
        }

        public void setImage1(Bitmap image1) {
            this.image = image1;
        }
}
