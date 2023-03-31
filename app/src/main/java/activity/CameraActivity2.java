package activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
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

public class CameraActivity2 extends AppCompatActivity implements SurfaceHolder.Callback, android.hardware.Camera.PictureCallback {
    private static final String TIME_STAMP_FORMAT_DATE = "dd.MM.yyyy";
    private static final String TIME_STAMP_FORMAT_TIME = "h:mm a";
    private static final String GALLERY_DIRECTORY_NAME_COMMON = "SurfaceCamera";
    private SurfaceHolder surfaceHolder;
    private android.hardware.Camera camera;
    private boolean safeToTakePicture = false;

    public static final int REQUEST_CODE = 100;
    private final static int RESULT_CODE = 100;
    private SurfaceView surfaceView;
    LinearLayout layoutpreview;
    TextView display ;
    FusedLocationProviderClient location;
    String latitudetxt,longitudetxt,addresstxt,state,country,postalcode,customer_name;
    SimpleDateFormat getDate,getTime;
    Bitmap bitmap;
    File save;

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

        location = LocationServices.getFusedLocationProviderClient(this);
        getlastLocation();
        setupSurfaceHolder();

    }

    @Override
    public void onBackPressed() {

        if(save != null) {
            Intent intent = new Intent();
            intent.putExtra("data",save);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Click Again", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void getlastLocation() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            location.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if(location != null)
                        {
                            Geocoder geocoder = new Geocoder(CameraActivity2.this,Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                                latitudetxt = String.valueOf(addresses.get(0).getLatitude());
                                longitudetxt = String.valueOf(addresses.get(0).getLongitude());
                                addresstxt = addresses.get(0).getAddressLine(0).substring(0,35);
                                state = addresses.get(0).getAdminArea();
                                postalcode = addresses.get(0).getPostalCode();
                                country = addresses.get(0).getCountryName();
                                getDate = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
                                getTime = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());


                                display.setText(" Latitude : " + latitudetxt + "\n" + " Longitude : " + longitudetxt+ "\n" + " Address : " + addresstxt +","
                                        + state+ " " + postalcode+ "," +country +"\n"+"Date: " + getDate.format(new Date()) + "\n" + "Time: " + getTime.format(new Date())
                                +"\n" + "Customer: " + customer_name);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        } else
        {
            askpermission();
        }
    }

    private void askpermission() {
        ActivityCompat.requestPermissions(CameraActivity2.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION }, RESULT_CODE);
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
            startBtn.setOnClickListener(view -> captureImage());
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
            safeToTakePicture = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    protected void onPause()
    {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
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

    public Bitmap saveImageWithTimeStamp( byte data[]) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        bmp = rotateBitmap(bmp);
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());
        String date = sdf.format(new Date());
        String time = sdf1.format(new Date());

        float scale = this.getResources().getDisplayMetrics().density;
        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(110);
        int color = ContextCompat.getColor(CameraActivity2.this, R.color.colorPrimaryDark);
        paint.setColor(color);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawBitmap(bmp, 0f, 0f, null);
       // paint.setTextSize((int) (12 * scale));

        // draw text to the Canvas center

        float height = paint.measureText("yY");
        float width = paint.measureText("Date: "+date+"\n"+"Time: "+time);
        float startXPosition = (bmp.getWidth() - width);
        float startYPosition = (bmp.getHeight() - height);



        String text = "Latitude: "+latitudetxt;
        String text1 = "Longitude: "+longitudetxt;
        String text2 = "Date: "+date;
        String text3 = "Time: "+time;

        String text4 = "Customer Name: "+customer_name;

        canvas.drawText(text , startXPosition - 1250, startYPosition - 600, paint);
        canvas.drawText(text1, startXPosition - 1250, startYPosition - 450, paint);
        canvas.drawText(text2+" "+text3 , startXPosition - 1250, startYPosition - 300, paint);
        canvas.drawText(text4, startXPosition - 1250, startYPosition - 150, paint);

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

    public static String getMediaFilePath(String type,String name) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME_COMMON);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/"+ type ); //it is my root directory

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a media file name
        return dir.getPath() + File.separator + "IMG_"+  String.valueOf(Calendar.getInstance().getTimeInMillis()) +".jpg";
    }
}