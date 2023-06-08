package activity;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import java.io.File;

import database.DatabaseHelper;


public class ShowDocument2 extends BaseActivity {

    Context context;
    String string_image = "";
    String key = "";
    String data = "",data1 = "";
    String docno = "";
    byte[] encodeByte;
    Bitmap bitmap;
    DatabaseHelper db;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
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

        //Toolbar code
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);

        Log.e("DOCNO","&&&&"+docno);
        Log.e("KEY","&&&&"+key);
        Log.e("DATA","&&&&"+data);


        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                string_title = "Water Resource Photo";
                File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_1.jpg");
                if (file.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    imageView.setImageBitmap(myBitmap);


                }
                break;

            case DatabaseHelper.KEY_PHOTO2:
                string_title = "Document1";
                File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_2.jpg");
                if (file1.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());

                    imageView.setImageBitmap(myBitmap);


                }
                break;

            case DatabaseHelper.KEY_PHOTO3:
                string_title = "Document2";
                File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_3.jpg");
                if (file2.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());

                    imageView.setImageBitmap(myBitmap);


                }
                break;

        }

        getSupportActionBar().setTitle(string_title);




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