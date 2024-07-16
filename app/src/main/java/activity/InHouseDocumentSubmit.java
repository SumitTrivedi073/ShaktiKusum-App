package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.zxing.integration.android.IntentIntegrator;
import com.shaktipumplimited.shaktikusum.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import adapter.BarCodeSelectionAdapter;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;

public class InHouseDocumentSubmit extends AppCompatActivity implements BarCodeSelectionAdapter.BarCodeSelectionListener{

    AlertDialog alertDialog;
    int barcodeSelectIndex, scannerCode;
    GmsBarcodeScannerOptions options;
    GmsBarcodeScanner scanner;
    LinearLayout barcodeLayout;
    RecyclerView barcodeListView;
    Button btnSave;
    TextView inst_module_ser_no;
    BarCodeSelectionAdapter barCodeSelectionAdapter;
    List<String> barcodenameList = new ArrayList<>();
    private Toolbar mToolbar;
    String serialNoLength , noOfModules = "";
    boolean isSubmit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_in_house_document_submit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


       // serialNoLength = Integer.parseInt(CustomUtility.getSharedPreferences(InHouseDocumentSubmit.this, Constant.quantity));
        Log.e("quantity", String.valueOf(serialNoLength));

        if( CustomUtility.getSharedPreferences(InHouseDocumentSubmit.this, Constant.quantity).isEmpty()){
            ShowQuantityPopup();
        }

        Init();
        listner();
    }

    private void Init() {

        barcodeListView = findViewById(R.id.barcodeListView);
        barcodeLayout = findViewById(R.id.barcodeLayout);
        inst_module_ser_no = findViewById(R.id.module_serial_no);
        btnSave = findViewById(R.id.btnSave);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.inHouse));

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void listner() {
       btnSave.setOnClickListener(v -> {

           Set<String> set = new HashSet<>();
           if (!inst_module_ser_no.getText().toString().trim().equals("0")) {
               for (int i = 0; i < barcodenameList.size(); i++) {

                   if (barcodenameList.get(i).isEmpty()) {
                       CustomUtility.ShowToast(getResources().getString(R.string.enter_allModuleSrno), getApplicationContext());
                        isSubmit = false;
                   } else {
                       if (set.contains(barcodenameList.get(i).toUpperCase())) {
                           isSubmit = false;
                           CustomUtility.ShowToast(barcodenameList.get(i) + getResources().getString(R.string.moduleMultipleTime), InHouseDocumentSubmit.this);
                           break;
                       } else {
                           set.add(barcodenameList.get(i).toUpperCase());
                       }
                   }
               }
               noOfModules = "";
               for (String ele : set) {
                   if (noOfModules.isEmpty()) {
                       noOfModules = ele;
                   } else {
                       noOfModules = noOfModules + "," + ele;
                   }
               }

               if ((set.size() == Integer.parseInt(inst_module_ser_no.getText().toString()))) {
                    isSubmit = true;
               }

               Log.e("noOfModules===>",noOfModules.toString());

           }
       });
       
    }
    
    private void setAdapter() {

        barcodenameList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(serialNoLength) ; i++) {
            barcodenameList.add("");
        }
        Log.e("barcodenameList======>", String.valueOf(barcodenameList.size()));

        barCodeSelectionAdapter = new BarCodeSelectionAdapter(InHouseDocumentSubmit.this, barcodenameList);
        barcodeListView.setHasFixedSize(true);
        barcodeListView.setAdapter(barCodeSelectionAdapter);
        barCodeSelectionAdapter.BarCodeSelection(this);
    }

    @SuppressLint("MissingInflatedId")
    private void ShowQuantityPopup() {
        LayoutInflater inflater = (LayoutInflater) InHouseDocumentSubmit.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.inhousequantiypopup,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(InHouseDocumentSubmit.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        Button submit , cancel;
        EditText totalQuantity;
        submit =  layout.findViewById(R.id.submit);
        cancel =  layout.findViewById(R.id.cancel);
        totalQuantity =  layout.findViewById(R.id.qtyTxt);

        submit.setOnClickListener(v -> {
            if(totalQuantity.getText().toString().isEmpty()){
                CustomUtility.showToast(InHouseDocumentSubmit.this,getResources().getString(R.string.enter_quantity));
            } else{
                Log.e("totalQuantity==>",  totalQuantity.getText().toString());
                serialNoLength = totalQuantity.getText().toString();
                CustomUtility.setSharedPreference(InHouseDocumentSubmit.this, serialNoLength, Constant.quantity);
                inst_module_ser_no.setText(totalQuantity.getText().toString());
                barcodeLayout.setVisibility(View.VISIBLE);
                setAdapter();
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();
        });

    }

    @Override
    public void BarCodeSelectionListener(int position) {
        barcodeSelectIndex = position;
        ScanCode(4000);
    }
    private void ScanCode(int scannerCode) {

        this.scannerCode = scannerCode;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            options = new GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_ALL_FORMATS)
                    .build();
            scanner = GmsBarcodeScanning.getClient(this);
            scanner.startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                // Task completed successfully
                               String rawValue = barcode.getRawValue();
                                setScanValue(rawValue, scannerCode);

                            })
                    .addOnCanceledListener(
                            () -> {
                                // Task canceled
                                Toast.makeText(getApplicationContext(), "Scanning Cancelled Please try again", Toast.LENGTH_SHORT).show();
                            })
                    .addOnFailureListener(
                            e -> {
                                startScanOldVersion();
                                // Task failed with an exception
                                //  Toast.makeText(getApplicationContext(), "Scanning Failed Please try again", Toast.LENGTH_SHORT).show();
                            });
        }else {
            startScanOldVersion();
        }

    }

    private void setScanValue(String rawValue, int scannerCode) {

        if (!barcodenameList.contains(rawValue)) {
            barcodenameList.set(barcodeSelectIndex, rawValue);
            barCodeSelectionAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Already Scanned", Toast.LENGTH_SHORT).show();
        }
    }

    private void startScanOldVersion() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QRCode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);// Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }



}