package activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import bean.SelfCheckImageBean;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;

public class PhotoViewerActivity extends BaseActivity {

    ImageView showImg;
    Toolbar mToolbar;
    Integer flag;
    Bitmap myBitmap;
    SelfCheckImageBean imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Init();
        listner();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void Init() {
        showImg = findViewById(R.id.showImg);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.photoGallery));

        if (getIntent().getExtras() != null) {
            flag = getIntent().getIntExtra("flag", 0);

            if (flag == 1) {
                myBitmap = (Bitmap) getIntent().getParcelableExtra(Constant.ImageData);
            } else {
                myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
            }
            showImg.setImageBitmap(myBitmap);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}