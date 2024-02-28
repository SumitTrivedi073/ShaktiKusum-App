package activity;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.shaktipumplimited.shaktikusum.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import utility.CustomUtility;

public class CameraActivity2 extends BaseActivity implements SurfaceHolder.Callback, Camera.PictureCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener{
    private static final String TIME_STAMP_FORMAT_DATE = "dd.MM.yyyy";
    private static final String TIME_STAMP_FORMAT_TIME = "h:mm a";
    private static final String GALLERY_DIRECTORY_NAME_COMMON = "Shakti Kusum App";

    private static final String TAG = "LocationService";
    private SurfaceHolder surfaceHolder;
    private android.hardware.Camera camera;
    public static final int REQUEST_CODE = 100;
    private final static int RESULT_CODE = 100;
    private SurfaceView surfaceView;
    LinearLayout layoutpreview;
    TextView display;
    String latitudetxt, longitudetxt, addresstxt, state, country, postalcode, customer_name, canvasText;
    SimpleDateFormat getDate, getTime;
    Bitmap bitmap;
    File save;
    public int TIME_INTERVAL =  1000;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        surfaceView = findViewById(R.id.surfaceView);

        layoutpreview = findViewById(R.id.layoutPreview);
        display = findViewById(R.id.display);
        Bundle bundle = getIntent().getExtras();
        customer_name = bundle.getString("cust_name");

        locationRequest = new LocationRequest();
        locationRequest.setInterval(TIME_INTERVAL);
        locationRequest.setFastestInterval(TIME_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        buildApiCLient();
        setupSurfaceHolder();

    }

    private void buildApiCLient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {

        if (save != null) {
            Intent intent = new Intent();
            intent.putExtra("data", save);
            intent.putExtra("latitude", latitudetxt);
            intent.putExtra("longitude", longitudetxt);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Click Again", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
            }
            setupSurfaceHolder();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setViewVisibility(int id, int visibility) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void setupSurfaceHolder() {
        setViewVisibility(R.id.startBtn, View.VISIBLE);
        setViewVisibility(R.id.surfaceView, View.VISIBLE);
        setViewVisibility(R.id.layoutPreview,View.VISIBLE);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        setBtnClick();
    }

    private void setBtnClick() {
        Button startBtn = findViewById(R.id.startBtn);
        if (startBtn != null) {
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!display.getText().toString().isEmpty()){
                    captureImage();

                    startBtn.setEnabled(false);

                    //enable button after 1000 millisecond
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        startBtn.setEnabled(true);
                    }, 5000);
                }else {
                        CustomUtility.showToast(CameraActivity2.this,getResources().getString(R.string.fetching_location));
                    }
                }
            });
        }
    }

    public void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startCamera();
    }

    private void startCamera() {

            camera = android.hardware.Camera.open(0);
            camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            setCamFocusMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setCamFocusMode(){

        if(null == camera) {
            return;
        }

        /* Set Auto focus */
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }
    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, android.hardware.Camera camera) {

        bitmap = saveImageWithTimeStamp(bytes);
        save = saveFile(bitmap,customer_name.trim(),customer_name.trim());
        onBackPressed();
    }

    public Bitmap saveImageWithTimeStamp(byte[] data) {

        BitmapFactory.Options options = new BitmapFactory.Options();


        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        bmp = rotateBitmap(bmp);
        Canvas canvas = new Canvas(bmp);
        TextPaint mTextPaint=new TextPaint();
        int color = ContextCompat.getColor(CameraActivity2.this, R.color.black);
        mTextPaint.setColor(color);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        mTextPaint.setTextSize(70);
        StaticLayout mTextLayout = new StaticLayout(display.getText().toString().trim(), mTextPaint, canvas.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

        canvas.save();
        canvas.translate(0f,bmp.getHeight() - mTextLayout.getHeight() - 0.0f);
        mTextLayout.draw(canvas);
        canvas.restore();


        return bmp;
    }

    public static File saveFile(Bitmap bitmap, String type, String name) {
        File file = new File(getMediaFilePath(type,name));
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        return file;
    }


    private static Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) 90);
       return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public static String getMediaFilePath(String type, String name) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME_COMMON);

        File dir = new File(root.getAbsolutePath() + "/Images/" + type); //it is my root directory

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a media file name
        return dir.getPath() + File.separator + "IMG_"+ Calendar.getInstance().getTimeInMillis() +".jpg";
    }


    @SuppressLint("SetTextI18n")
    private void getLastLocation(Location location) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Geocoder geocoder = new Geocoder(CameraActivity2.this, Locale.getDefault());
            try {
                if (CustomUtility.isInternetOn(CameraActivity2.this)) {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (!addresses.isEmpty()) {
                        latitudetxt = String.valueOf(location.getLatitude());
                        longitudetxt = String.valueOf(location.getLongitude());
                        if (addresses.get(0).getAddressLine(0) != null && !addresses.get(0).getAddressLine(0).isEmpty()) {
                            addresstxt = addresses.get(0).getAddressLine(0);
                        }
                        state = addresses.get(0).getAdminArea();
                        postalcode = addresses.get(0).getPostalCode();
                        country = addresses.get(0).getCountryName();
                        getDate = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
                        getTime = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());


                        display.setText("Latitude : " + latitudetxt + "\n" + "Longitude : " + longitudetxt + "\n" + "Address : " + addresstxt + ","
                                + state + " " + postalcode + "," + country + "\n" + "Date: " + getDate.format(new Date()) + "\n" + "Time: " + getTime.format(new Date())
                                + "\n" + "Customer: " + customer_name);

                    }
                } else {

                    latitudetxt = String.valueOf(location.getLatitude());
                    longitudetxt = String.valueOf(location.getLongitude());
                    getDate = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
                    getTime = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());


                    display.setText(" Latitude : " + latitudetxt + "\n" +
                            "Longitude : " + longitudetxt + "\n" + "Date: " + getDate.format(new Date()) + "\n" + "Time: " + getTime.format(new Date())
                            + "\n" + "Customer: " + customer_name);


                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            askpermission();
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onStop(){
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    private void askpermission() {
        ActivityCompat.requestPermissions(CameraActivity2.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, RESULT_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        boolean granted =
                CustomUtility.checkLocationPermission(this);
         if (granted) {
                 startLocationUpdates();
    }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.i("currentLocation====>", "lat " + location.getLatitude());
        Log.i("currentLocation===>", "lng " + location.getLongitude());
        getLastLocation(location);
    }

    protected synchronized void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.disconnect();
            mGoogleApiClient= null;
        }
        releaseCamera();
    }
}