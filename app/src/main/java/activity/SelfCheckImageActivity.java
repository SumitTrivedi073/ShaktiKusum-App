package activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static utility.CustomUtility.getBitmapFromBase64;
import static utility.FileUtils.getPath;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shaktipumplimited.shaktikusum.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.ImageSelectionAdapter;
import adapter.SelfCheckDetailsAdapter;
import adapter.SelfCheckImageListAdapter;
import bean.ImageModel;
import bean.SelfCheckImageBean;
import bean.SelfCheckListBean;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class SelfCheckImageActivity extends AppCompatActivity implements SelfCheckImageListAdapter.SendImageListener, ImageSelectionAdapter.ImageSelectionListener, View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    RecyclerView recyclerview,
            recyclerview2;
    Intent intent;
    int selectedIndex;
    boolean isUpdate = false;
    SelfCheckListBean.InstallationDatum responseList;
    List<SelfCheckImageBean> imageList;
    SelfCheckImageListAdapter adapter;
    List<ImageModel> imageArrayList;
    List<String> itemNameList;
    ImageSelectionAdapter customAdapter;
    AlertDialog alertDialog;
    Toolbar toolbar;
    EditText remark_txt;
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_self_check_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inIt();
        listener();
    }

    private void inIt() {
        intent = getIntent();
        responseList = (SelfCheckListBean.InstallationDatum) intent.getSerializableExtra("arrlist");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.vendor_check);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview2 = findViewById(R.id.recyclerview2);
        remark_txt = findViewById(R.id.remark_txt);
        save = findViewById(R.id.save);
        if (CustomUtility.isInternetOn(getApplicationContext())) {
            ImageListAPI();
        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), getApplicationContext());
        }

    }

    private void setAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.attachment1));
        itemNameList.add(getResources().getString(R.string.attachment2));
        itemNameList.add(getResources().getString(R.string.attachment3));
        itemNameList.add(getResources().getString(R.string.attachment4));
        itemNameList.add(getResources().getString(R.string.attachment5));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo("");
            imageModel.setLatitude("");
            imageModel.setLongitude("");
            imageModel.setPoistion(i + 1);
            imageArrayList.add(imageModel);
        }

        customAdapter = new ImageSelectionAdapter(SelfCheckImageActivity.this,imageArrayList,false);
        recyclerview2.setHasFixedSize(true);
        recyclerview2.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);
    }


    private void ImageListAPI() {
        CustomUtility.showProgressDialogue(SelfCheckImageActivity.this);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest mStringRequest = new JsonObjectRequest(Request.Method.GET, WebURL.selfCheckImageListAPI + "?Bill=97113311", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response====>", String.valueOf(response));
                if (!response.toString().isEmpty()) {

                    imageList = new ArrayList<>();
                    try {
                        JSONArray jsonArray = response.getJSONArray("response");

                        if (jsonArray.length()>0) {

                            Log.e("size>0","size");

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SelfCheckImageBean selfCheck = new SelfCheckImageBean();

                                selfCheck.setImage1(getBitmapFromBase64(jsonObject.getString("image_1")));
                                imageList.add(selfCheck);
                            }

                            SelfCheckImageActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    adapter = new SelfCheckImageListAdapter(getApplicationContext(),imageList,responseList);
                                    recyclerview.setAdapter(adapter);
                                    adapter.SendIMG(SelfCheckImageActivity.this);
                                    recyclerview2.setVisibility(View.VISIBLE);
                                    setAdapter();
                                    remark_txt.setVisibility(View.VISIBLE);
                                }
                            });
                            CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
                        } else {
                            CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                   /**/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error====>", "Error :" + error.toString());
                CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
            }
        });
        mRequestQueue.add(mStringRequest);
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                5,  /// maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        save.setOnClickListener(this);
    }


    @Override
    public void sendImageListener(List<SelfCheckImageBean> imageList, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.image_dialog_layout, null);

        ImageView imageView = dialogView.findViewById(R.id.dialog_icon);
        imageView.setImageBitmap(imageList.get(position).getImage1());

        TextView textView = dialogView.findViewById(R.id.textview);

        AlertDialog dialog = new AlertDialog.Builder(this,R.style.FullScreenDialog)
                .setView(dialogView)
                .create();
        dialog.show();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        selectorPickImage(imageModelList);
    }
    private void selectorPickImage(ImageModel imageModelList) {
        if (checkPermission()) {
            if (imageModelList.isImageSelected()) {
                isUpdate = true;
                selectImage("1");
            } else {
                isUpdate = false;
                selectImage("0");
            }
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadMediaImages = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);


        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadMediaImages == PackageManager.PERMISSION_GRANTED;
        } else {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
        }

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_MEDIA_AUDIO,
                            android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) SelfCheckImageActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(SelfCheckImageActivity.this, R.style.MyDialogTheme);

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
            gallery.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            camera.setText(getResources().getString(R.string.camera));

        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    galleryIntent();
                } else {
                    Intent i_display_image = new Intent(SelfCheckImageActivity.this, PhotoViewerActivity.class);
                    i_display_image.putExtra(Constant.image_path, imageArrayList.get(selectedIndex).getImagePath());
                    startActivity(i_display_image);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    cameraIntent();
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
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
                    String path = getPath(SelfCheckImageActivity.this, mImageCaptureUri); // From Gallery
                    if (path == null) {
                        path = mImageCaptureUri.getPath(); // From File Manager
                    }
                    Log.e("Activity", "PathHolder22= " + path);
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file = "";
                    }
                    if (TextUtils.isEmpty(file)) {
                        Toast.makeText(SelfCheckImageActivity.this, getResources().getString(R.string.file_not_valid), Toast.LENGTH_LONG).show();
                    } else {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                        File file1 = CustomUtility.saveFile(bitmap, getResources().getString(R.string.vendorcheck), "Images");

                        UpdateArrayList(file1.getPath(), "0","", "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void cameraIntent() {
            camraLauncher.launch(new Intent(SelfCheckImageActivity.this, CameraActivity2.class)
                    .putExtra(Constant.cust_name, responseList.getName()));
    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            if (!bundle.get(Constant.latitude).toString().isEmpty() && !bundle.get(Constant.longitude).toString().isEmpty()) {
                                UpdateArrayList(bundle.get("data").toString(), "0", bundle.get(Constant.latitude).toString(), bundle.get(Constant.longitude).toString());
                            } else {
                                CustomUtility.ShowToast(getResources().getString(R.string.add_img_again), SelfCheckImageActivity.this);
                            }

                        }

                    }
                }
            });


    private void UpdateArrayList(String path, String value, String latitude, String longitude) {
        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setBillNo(responseList.getBeneficiary());

        imageModel.setPoistion(imageArrayList.get(selectedIndex).getPoistion());
        imageModel.setLatitude(latitude);
        imageModel.setLongitude(longitude);
        imageArrayList.set(selectedIndex, imageModel);

        customAdapter = new ImageSelectionAdapter(SelfCheckImageActivity.this,imageArrayList,false );
        recyclerview2.setHasFixedSize(true);
        recyclerview2.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);
        customAdapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                validation();
                break;
        }
    }

    private void validation() {
        if(remark_txt.getText().toString().isEmpty()){
            CustomUtility.showToast(SelfCheckImageActivity.this,getResources().getString(R.string.enter_remark));
        }else{
            saveData();
        }
    }

    private void saveData() {
        JSONArray ja_invc_data = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        try {

            if (imageArrayList.size() > 0) {
                for (int i = 0; i < imageArrayList.size(); i++) {
                    if (imageArrayList.get(i).isImageSelected()) {
                        try {
                            jsonObj.put("PHOTO" + imageArrayList.get(i).getPoistion(),CustomUtility.getBase64FromBitmap(SelfCheckImageActivity.this,imageArrayList.get(i).getImagePath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            jsonObj.put("PROJECT_NO", CustomUtility.getSharedPreferences(this, "projectid"));
            jsonObj.put("userid", CustomUtility.getSharedPreferences(this, "userid"));
            jsonObj.put("REGISNO", responseList.getRegisno());
            jsonObj.put("BENEFICIARY", responseList.getBeneficiary());
            jsonObj.put("project_login_no", CustomUtility.getSharedPreferences(this, "loginid"));
            jsonObj.put("SAVE_REMARK", remark_txt.getText().toString());

            ja_invc_data.put(jsonObj);
            Log.e("ja_invc_data=====>", ja_invc_data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CustomUtility.isInternetOn(SelfCheckImageActivity.this)) {

            new saveImgDataAPI(ja_invc_data).execute();

        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.check_internet_connection), SelfCheckImageActivity.this);
        }

    }

    private class saveImgDataAPI extends AsyncTask<String, String, String> {
        JSONArray jsonArray;

        public saveImgDataAPI(JSONArray jaInvcData) {
            jsonArray = jaInvcData;
        }

        @Override
        protected void onPreExecute() {
            CustomUtility.showProgressDialogue(SelfCheckImageActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("Image_save", String.valueOf(jsonArray)));
            Log.e("save_data==>",String.valueOf(jsonArray));
            try {
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.saveSelfCheckImg, param1_invc);

            } catch (Exception e) {
                e.printStackTrace();
                CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            String docno_sap = null;
            String invc_done = null;
            try {
                if (!result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {
                            CustomUtility.ShowToast(getResources().getString(R.string.dataSubmittedSuccessfully), SelfCheckImageActivity.this);
                            CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
                            onBackPressed();
                        } else if (invc_done.equalsIgnoreCase("N")) {
                            CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), SelfCheckImageActivity.this);
                            CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
                        }
                    }
                } else {
                    CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), SelfCheckImageActivity.this);
                    CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
                CustomUtility.hideProgressDialog(SelfCheckImageActivity.this);
            }
        }
    }
}