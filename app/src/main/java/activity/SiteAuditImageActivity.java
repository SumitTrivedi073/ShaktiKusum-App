package activity;

import static utility.FileUtils.getPath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimited.shaktikusum.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import database.DatabaseHelper;
import utility.CustomUtility;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class SiteAuditImageActivity extends BaseActivity implements ImageSelectionAdapter.ImageSelectionListener {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private static final int PICK_FROM_FILE = 102;
    boolean isBackPressed = false,isUpdate = false;
    int selectedIndex;
    AlertDialog alertDialog;
    Context mContext;
    RecyclerView recyclerview;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    ImageSelectionAdapter siteAuditAdapter;
    Toolbar mToolbar;
    double AUD_latitude_double,
            AUD_longitude_double;
    String type="AUD/";

    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";

    String imageStoragePath, enq_docno, cust_nm, photo1_text, photo2_text, photo3_text, photo4_text;
    TextView photo1, photo2, photo3, photo4;
    boolean photo1_flag = false,
            photo2_flag = false,
            photo3_flag = false,
            photo4_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditsitereport_image);
        mContext = this;
        Init();
    }

    private void Init() {
        recyclerview = findViewById(R.id.recyclerview);
        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.siteAuditImages));

        CustomUtility.setSharedPreference(mContext, "AUDSYNC" + enq_docno, "");

        Bundle bundle = getIntent().getExtras();
        enq_docno= bundle.getString("billno");
        cust_nm= bundle.getString("custnm");

         SetAdapter();
        listner();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(view -> {
            if (Save()) {
                onBackPressed();
            }
        });
    }

    private boolean Save() {

        if (!imageArrayList.get(0).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_foundation_photo), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(1).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_Structure_Assembly_photo), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(2).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_LA_and_Earthing_Photo), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(3).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_MISC_Photo), Toast.LENGTH_SHORT).show();
        }  else {
            CustomUtility.setSharedPreference(mContext, "AUDSYNC" + enq_docno, "1");
            isBackPressed = true;
        }
        return isBackPressed;
    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.foundation_photo));
        itemNameList.add(getResources().getString(R.string.Structure_Assembly_photo));
        itemNameList.add(getResources().getString(R.string.LA_and_Earthing_Photo));
        itemNameList.add(getResources().getString(R.string.MISC_Photo));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setBillNo("");
            imageModel.setImageSelected(false);
            imageArrayList.add(imageModel);
        }

        DatabaseHelper db = new DatabaseHelper(this);

        //Create Table
        imageList = db.getAllAuditSiteImages();

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo()!=null &&
                            imageList.get(i).getBillNo().trim().equals(enq_docno)) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageModel.setImageSelected(true);
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
        }
        siteAuditAdapter = new ImageSelectionAdapter(SiteAuditImageActivity.this, imageArrayList, false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(siteAuditAdapter);
        siteAuditAdapter.ImageSelection(this);
    }

    public void openCamera() {

        camraLauncher.launch(new Intent(SiteAuditImageActivity.this, CameraActivity2.class)
                .putExtra("cust_name", cust_nm));
    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        UpdateArrayList(bundle.get("data").toString());
                    }
                }
            });

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setBillNo(enq_docno);
        imageModel.setImageSelected(true);
        imageArrayList.set(selectedIndex, imageModel);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if( isUpdate){
            db.updateSiteAuditRecord(imageArrayList.get(selectedIndex).getName(), path,true, enq_docno);
        }else {
            db.insertSiteAuditImage(imageArrayList.get(selectedIndex).getName(), path,true, enq_docno);
        }
        siteAuditAdapter.notifyDataSetChanged();

    }

    public void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {

            case PICK_FROM_FILE:
                try {
                    Uri mImageCaptureUri = data.getData();
                    String path = getPath(SiteAuditImageActivity.this, mImageCaptureUri); // From Gallery
                    if (path == null) {
                        path = mImageCaptureUri.getPath(); // From File Manager
                    }
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file = "";
                    }
                    if (TextUtils.isEmpty(file)) {
                        Toast.makeText(SiteAuditImageActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , mImageCaptureUri);
                        File file1 = CustomUtility.saveFile(bitmap,cust_nm.trim(),"Images");
                        UpdateArrayList(file1.getPath());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    public String getImagePath(Uri uri) {

        String s = null;

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        // TODO perform some logging or show user feedback
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor1 = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        Cursor cursor2 =  mContext.getContentResolver().query(uri, projection, null, null, null);


        if (cursor1 == null && cursor2 == null) {
            return null;
        } else {

            int column_index;
            if (cursor1 != null) {
                column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor1.moveToFirst();

                if (cursor1.moveToFirst()) {
                    s = cursor1.getString(column_index);
                }
                cursor1.close();
            }
            int column_index1;
            if (cursor2 != null) {
                column_index1 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor2.moveToFirst();

                if (cursor2.moveToFirst()) {
                    s = cursor2.getString(column_index1);
                }
                cursor2.close();
            }

            return s;
        }
    }

    public void setFlag(String key) {

        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;
        photo4_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                photo1_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO2:
                photo2_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO3:
                photo3_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO4:
                photo4_flag = true;
                break;

        }

    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            isUpdate = false;
            selectImage("0");
        }

    }

    private void selectImage(String value) {


        LayoutInflater inflater = (LayoutInflater) SiteAuditImageActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(SiteAuditImageActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView camera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            camera.setText(getResources().getString(R.string.camera));

        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }
        gallery.setVisibility(View.GONE);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    openGallery();
                } else {
                    Intent i_display_image = new Intent(SiteAuditImageActivity.this, PhotoViewerActivity.class);
                    i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                    startActivity(i_display_image);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    openCamera();
                } else {
                    selectImage("0");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}