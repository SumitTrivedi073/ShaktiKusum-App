package activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import database.DatabaseHelper;


public class ShowDocument1 extends BaseActivity {

    Context context;
    String string_image = "";
    String key = "";
    String data = "",data1 = "";
    String docno = "";
    byte[] encodeByte;
    Bitmap bitmap;
    DatabaseHelper db;
    ImageView imageView;
    String string_title = "";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document);

        context = this;

        db = new DatabaseHelper(context);
        Bundle bundle = getIntent().getExtras();

        docno = bundle.getString("docno");
        key = bundle.getString("key");

            data = bundle.getString("data");


        Log.e("DOCNO","&&&&"+docno);
        Log.e("KEY","&&&&"+key);
        Log.e("DATA","&&&&"+data);


        switch (key) {

            case DatabaseHelper.KEY_SIM_OLD_PHOTO:
                string_title = "Sim Old Photo";
                break;

            case DatabaseHelper.KEY_SIM_NEW_PHOTO:
                string_title = "Sim New Photo";
                break;

            case DatabaseHelper.KEY_DRIVE_PHOTO:
                string_title = "Drive Photo";
                break;

        }

        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(string_title);


        imageView = (ImageView) findViewById(R.id.imageView);


        string_image = data;

        Log.e("IMAGE","&&&&"+string_image);

        if (string_image != null && !string_image.isEmpty()) {
            encodeByte = Base64.decode(string_image, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(bitmap);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}