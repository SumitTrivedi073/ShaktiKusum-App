package activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

import database.DatabaseHelper;

import static android.os.Environment.getExternalStoragePublicDirectory;

import com.shaktipumplimited.shaktikusum.R;

public class ShowDocument extends AppCompatActivity {

    Context context;

    String key = "";
    String data = "";
    String docno = "";

    DatabaseHelper db;
    ImageView imageView;
    String string_title = "";
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";
    public static final String GALLERY_DIRECTORY_NAME_UNLOD = "ShaktiKusumUnload";


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

        Log.e("DOCno", "&&&&" + docno);
        Log.e("KEY", "&&&&" + key);
        Log.e("data", "&&&&" + data);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageView = (ImageView) findViewById(R.id.imageView);

        if (docno != null) {

            switch (key) {

                case DatabaseHelper.KEY_PHOTO1:
                    string_title = "Photo 1";
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_1.jpg");
                    if (file.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);


                    }

                    break;
                case DatabaseHelper.KEY_PHOTO2:
                    string_title = "Photo 2";
                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_2.jpg");
                    if (file1.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }

                    break;
                case DatabaseHelper.KEY_PHOTO3:
                    string_title = "Photo 3";
                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_3.jpg");
                    if (file2.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }

                    break;
                case DatabaseHelper.KEY_PHOTO4:
                    string_title = "Photo 4";
                    File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_4.jpg");
                    if (file3.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file3.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;
                case DatabaseHelper.KEY_PHOTO5:
                    string_title = "Photo 5";

                    File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_5.jpg");
                    if (file4.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file4.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }

                    break;
                case DatabaseHelper.KEY_PHOTO6:
                    string_title = "Photo 6";
                    File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_6.jpg");
                    if (file5.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file5.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;
                case DatabaseHelper.KEY_PHOTO7:
                    string_title = "Photo 7";
                    File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_7.jpg");
                    if (file6.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file6.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;
                case DatabaseHelper.KEY_PHOTO8:
                    string_title = "Photo 8";
                    File file11 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_8.jpg");
                    if (file11.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file11.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }

                    break;

                case DatabaseHelper.KEY_PHOTO9:
                    string_title = "Photo 9";
                    File file7 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_9.jpg");
                    if (file7.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file7.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

                case DatabaseHelper.KEY_PHOTO10:
                    string_title = "Photo 10";
                    File file8 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_10.jpg");
                    if (file8.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file8.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

                case DatabaseHelper.KEY_PHOTO11:
                    string_title = "Photo 11";
                    File file9 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_11.jpg");
                    if (file9.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file9.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

                case DatabaseHelper.KEY_PHOTO12:
                    string_title = "Photo 12";
                    File file10 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_12.jpg");
                    if (file10.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file10.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

                    case DatabaseHelper.KEY_PHOTO13:
                    string_title = "Photo 13";
                    File file13 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME_UNLOD  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_13.jpg");
                    if (file13.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file13.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

                case DatabaseHelper.KEY_PHOTO14:
                    string_title = "Photo 14";
                    File file14 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME_UNLOD  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_14.jpg");
                    if (file14.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file14.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

                case DatabaseHelper.KEY_PHOTO15:
                    string_title = "Photo 15";
                    File file15 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME_UNLOD  + "/SKAPP/" + data + "/" + docno, "/IMG_PHOTO_15.jpg");
                    if (file15.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file15.getAbsolutePath());

                        imageView.setImageBitmap(myBitmap);
                    }
                    break;

            }
        } else {
            if (DatabaseHelper.KEY_PHOTO1.equals(key)) {
                string_title = "DD Collection Image";
                File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SKAPP/" + data, "/IMG_PHOTO_1.jpg");
                if (file.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    imageView.setImageBitmap(myBitmap);

                }
            }
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
