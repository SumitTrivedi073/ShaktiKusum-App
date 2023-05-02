package activity;

import static activity.Config.TIME_STAMP_FORMAT_DATE;
import static activity.Config.TIME_STAMP_FORMAT_TIME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import utility.CustomUtility;

public class CameraActivity2 extends AppCompatActivity{
    private static final String GALLERY_DIRECTORY_NAME_COMMON = "SurfaceCamera";
    private TextureView textureView;
    Bitmap bitmap;
    File file;
    TextView display ;
    SimpleDateFormat getDate,getTime;
    private final static int RESULT_CODE =100;
    FusedLocationProviderClient location;

    //Check state orientation of output image
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    String latitudetxt,longitudetxt,addresstxt,state,country,postalcode,customer_name;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    LinearLayout layoutPreview;

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        textureView = findViewById(R.id.textureView);
        //From Java 1.4 , you can use keyword 'assert' to check expression true or false
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        layoutPreview = findViewById(R.id.layoutPreview);
        Button btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(view -> takePicture());

        display = findViewById(R.id.display);
        Bundle bundle = getIntent().getExtras();
        customer_name = bundle.getString("cust_name");

        location = LocationServices.getFusedLocationProviderClient(this);
        //getDateandTime();
        getlastLocation();


    }

    private void askpermission() {
        ActivityCompat.requestPermissions(CameraActivity2.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.CAMERA,    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_CODE);
    }

    private void takePicture() {
        if(cameraDevice == null)
            return;
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null)
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

            //Capture image with custom size
            int width = 640;
            int height = 480;
            if(jpegSizes != null && jpegSizes.length > 0)
            {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Check orientation base on device
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        //save(bytes);

                        bitmap = saveImageWithTimeStamp(bytes);
                        file = saveFile(bitmap,customer_name.trim());

                    } finally {
                        {
                            if(image != null)
                                image.close();
                        }
                    }
                }

            };

            reader.setOnImageAvailableListener(readerListener,mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    if (file != null) {
                        Intent intent = new Intent();
                        intent.putExtra("data", file);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        CustomUtility.ShowToast("Please Click Image Again , Something went Wrong!", getApplicationContext());
                    }
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.capture(captureBuilder.build(),captureListener,mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            },mBackgroundHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    public static File saveFile(Bitmap bitmap, String name) {
        File file = new File(getMediaFilePath(name));
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String getMediaFilePath(String name) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME_COMMON);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/"+ name ); //it is my root directory

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

    private static Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) 90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void createCameraPreview() {
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              if(cameraDevice != null)
                                  layoutPreview.bringToFront();
                              cameraCaptureSessions = cameraCaptureSession;
                              updatePreview();
                          }
                      });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity2.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(cameraDevice == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void openCamera() {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            //Check realtime permission if run higher API 23
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId,stateCallback,null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] !=  PackageManager.PERMISSION_GRANTED && grantResults[2] !=  PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if(textureView.isAvailable())
            openCamera();
        else
            textureView.setSurfaceTextureListener(textureListener);
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread= null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void onBackPressed() {

        if (file != null) {
            Intent intent = new Intent();
            intent.putExtra("data", file);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.Click_Again), Toast.LENGTH_SHORT).show();
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
    }}