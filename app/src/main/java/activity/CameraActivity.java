package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumplimited.shaktikusum.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends Activity {

    private static final int REQUEST_CODE_CHANGE_SETTING = 1;
    private static final int TIME_STAMP_UPDATE_INTERVAL = 1000;
    private Camera camera;
    private Context context;
    private int cameraFacing;
    private CameraPreview preview;
    private ImageView image;
    private String lat, lng, cust_name, enq_docno, type, name, billNo;
    private TextView txtData;
    double inst_latitude_double, inst_longitude_double;
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdf1;
    private Handler handler = new Handler();

    private Runnable runnableSetDateText = new Runnable() {
        @Override
        public void run() {
            txtData.setText("Latitude: " + lat + "\n" + "Longitude: " + lng + "\n" + "Date: " + sdf.format(new Date()) + " " + "Time: " + sdf1.format(new Date()) + "\n" + "Customer Name: " + cust_name);
            handler.postDelayed(runnableSetDateText, TIME_STAMP_UPDATE_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                lat = null;
                lng = null;
                cust_name = null;
                enq_docno = null;
                type = null;
                name = null;
            } else {
                lat = extras.getString("lat");
                lng = extras.getString("lng");
                if(extras.getBoolean("isOffline")){
                    billNo = extras.getString("bill_no");
                }else {
                    cust_name = extras.getString("cust_name");
                }
                enq_docno = extras.getString("inst_id");
                type = extras.getString("type");
                name = extras.getString("name");
            }
        } else {
            lat = (String) savedInstanceState.getSerializable("lat");
            lng = (String) savedInstanceState.getSerializable("lng");
            if((Boolean)savedInstanceState.getSerializable("isOffline")){
                billNo = (String) savedInstanceState.getSerializable("bill_no");
            }else {
                cust_name = (String) savedInstanceState.getSerializable("cust_name");
            }
            enq_docno = (String) savedInstanceState.getSerializable("inst_id");
            type = (String) savedInstanceState.getSerializable("type");
            name = (String) savedInstanceState.getSerializable("name");
        }

        txtData = (TextView) findViewById(R.id.txtDate);
        image = (ImageView) findViewById(R.id.sw);
        sdf = new SimpleDateFormat(Config.TIME_STAMP_FORMAT_DATE, Locale.getDefault());
        sdf1 = new SimpleDateFormat(Config.TIME_STAMP_FORMAT_TIME, Locale.getDefault());
        if(null!=billNo){
            txtData.setText("Latitude: " + lat + "\n" + "Longitude: " + lng + "\n" + "Date: " + sdf.format(new Date()) + " " + "Time: " + sdf1.format(new Date()) + "\n" + "Bill No: " + billNo);
        }else{
            txtData.setText("Latitude: " + lat + "\n" + "Longitude: " + lng + "\n" + "Date: " + sdf.format(new Date()) + " " + "Time: " + sdf1.format(new Date()) + "\n" + "Customer Name: " + cust_name);
        }
//        handler.postDelayed(runnableSetDateText, TIME_STAMP_UPDATE_INTERVAL);
        startPreview(null);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });

        if (TextUtils.isEmpty(lat) && TextUtils.isEmpty(lng)) {
            getGpsLocation();
            lat = String.valueOf(inst_latitude_double);
            lng = String.valueOf(inst_longitude_double);
        }
    }

    private void startPreview(String pictureSizeStr) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        camera = getCameraInstance();
        if (camera == null) {
            Log.e("jisunLog", "Failed camera open");
        } else {
            FrameLayout layoutPreview = (FrameLayout) findViewById(R.id.layoutPreview);
            if (preview != null) {
                layoutPreview.removeView(preview);
                preview = null;
            }
            preview = new CameraPreview(this, camera);
            preview.setKeepScreenOn(true);
            ImageManager.adjustCameraParameters(this, camera, pictureSizeStr);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) layoutPreview.getLayoutParams();
            p.height = displayMetrics.widthPixels / 1 * 2;
            layoutPreview.setLayoutParams(p);
            layoutPreview.addView(preview);
            findViewById(R.id.txtDate).bringToFront();
        }
    }

    public static int setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public void switchCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        //swap the id of the camera to be used
        if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            camera = Camera.open(cameraFacing);
            camera.setDisplayOrientation(setCameraDisplayOrientation(this, cameraFacing, camera));
            Camera.Parameters params = camera.getParameters();
            params.setRotation(setCameraDisplayOrientation(this, cameraFacing, camera));
            camera.setPreviewDisplay(preview.getHolder());
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Camera getCameraInstance() {
        Camera camera = null;
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                camera = Camera.open(cameraFacing);
                camera.setDisplayOrientation(setCameraDisplayOrientation(this, cameraFacing, camera));
                Camera.Parameters params = camera.getParameters();
                params.setRotation(setCameraDisplayOrientation(this, cameraFacing, camera));
                camera.startPreview();
            } catch (RuntimeException ex) {
                Toast.makeText(this, "camera_not_found ] " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.d(Config.TAG, "camera_not_found ] " + ex.getMessage().toString());
            }
        }
        return camera;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHANGE_SETTING) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Config.PREF_KEY_PICTURE_SIZE);
                ((FrameLayout) findViewById(R.id.layoutPreview)).removeView(preview);
                preview = null;
                startPreview(str);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickCapture(View v) {

        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                new AsyncTask<byte[], Void, File>() {
                    public float textSize;
                    private CustomProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new CustomProgressDialog(CameraActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        textSize = txtData.getTextSize();
                    }

                    @Override
                    protected File doInBackground(byte[]... params) {
                        byte[] data = params[0];
                        Bitmap bitmap = ImageManager.saveImageWithTimeStamp(CameraActivity.this, data, 0, data.length, textSize, lat, lng, cust_name);
                        File file = ImageManager.saveFile(bitmap, type, name, enq_docno);
                        return file;
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        progressDialog.dismiss();
                        Intent intent = new Intent();
                        setResult(100, intent);
                        finish();//finishing activity
                        super.onPostExecute(file);
                    }
                }.execute(data);
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnableSetDateText);
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
    }
}