package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import adapter.BarCodeSelectionAdapter;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class JointInspection extends AppCompatActivity implements BarCodeSelectionAdapter.BarCodeSelectionListener{

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
    String[] modules;
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

        if( CustomUtility.getSharedPreferences(JointInspection.this, Constant.quantity).isEmpty()){
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
        getSupportActionBar().setTitle(getResources().getString(R.string.jointInsp));

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
                            CustomUtility.ShowToast(barcodenameList.get(i) + getResources().getString(R.string.moduleMultipleTime), JointInspection.this);
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
            if(isSubmit){
                modules = noOfModules.split(",");
                new JointInspection.saveData().execute();
            }

        });

    }
    private class saveData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(JointInspection.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Sending Data to server..please wait !");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;
            JSONArray ja_invc_data = new JSONArray();
            try {
                for(int i=0;i<modules.length;i++){
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("vbeln", modules[i] );
                    jsonObj.put("userid", CustomUtility.getSharedPreferences(getApplicationContext(), "userid"));
                    jsonObj.put("project_no", CustomUtility.getSharedPreferences(getApplicationContext(), "projectid"));
                    ja_invc_data.put(jsonObj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Param====>", ja_invc_data.toString());
            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("installation", String.valueOf(ja_invc_data)));
            Log.e("url====>", WebURL.jointInsp + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.jointInsp, param1_invc);
                Log.e("OUTPUT1", "&&&&" + obj2);
                System.out.println("OUTPUT1==>>" + obj2);
                progressDialog.dismiss();
                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");
                    JSONArray ja = new JSONArray(obj1);
                    Log.e("OUTPUT2", "&&&&" + ja);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");
                        if (invc_done.equalsIgnoreCase("Y")) {
                            progressDialog.dismiss();
                            showingMessage(getResources().getString(R.string.dataSubmittedSuccessfully));
                            finish();
                        } else if (invc_done.equalsIgnoreCase("N")) {
                            showingMessage(getResources().getString(R.string.dataNotSubmitted));
                            progressDialog.dismiss();
                        }
                    }
                } else {
                    CustomUtility.showToast(JointInspection.this, getResources().getString(R.string.somethingWentWrong));
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }
    }

    private void showingMessage(String message) {
        runOnUiThread(new Runnable() {
            public void run() {

                CustomUtility.showToast(JointInspection.this, message);

            }
        });
    }

    private void setAdapter() {

        barcodenameList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(serialNoLength) ; i++) {
            barcodenameList.add("");
        }
        Log.e("barcodenameList======>", String.valueOf(barcodenameList.size()));

        barCodeSelectionAdapter = new BarCodeSelectionAdapter(JointInspection.this, barcodenameList);
        barcodeListView.setHasFixedSize(true);
        barcodeListView.setAdapter(barCodeSelectionAdapter);
        barCodeSelectionAdapter.BarCodeSelection(this);
    }

    @SuppressLint("MissingInflatedId")
    private void ShowQuantityPopup() {
        LayoutInflater inflater = (LayoutInflater) JointInspection.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.inhousequantiypopup,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(JointInspection.this, R.style.MyDialogTheme);

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
                CustomUtility.showToast(JointInspection.this,getResources().getString(R.string.enter_quantity));
            } else{
                Log.e("totalQuantity==>",  totalQuantity.getText().toString());
                serialNoLength = totalQuantity.getText().toString();
                CustomUtility.setSharedPreference(JointInspection.this, serialNoLength, Constant.quantity);
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