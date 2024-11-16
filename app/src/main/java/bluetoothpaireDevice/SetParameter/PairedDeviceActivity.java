package bluetoothpaireDevice.SetParameter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import bean.ParameterSettingListModel;
import bluetoothpaireDevice.Settingadapter.BTPairedDeviceAdapter;
import com.shaktipumplimited.shaktikusum.R;


import activity.BaseActivity;
import debugapp.GlobalValue.Constant;
import utility.CustomUtility;

public class PairedDeviceActivity extends BaseActivity {


    private Context mContext;
    private RecyclerView rclSettingListViewID;
    private LinearLayoutManager lLayout;
    private RecyclerView.Adapter recyclerViewAdapter;

    private TextView txtPairedDeviceListID;

    private RelativeLayout rlvBackViewID;

    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

    String ControllerSerialNumber,debugDataExtract,isPeramterSet,set_matno,billNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_device);

        mContext = this;
        initView();

    }



    private void initView() {


        rlvBackViewID = findViewById(R.id.rlvBackViewID);

        rclSettingListViewID = findViewById(R.id.rclSettingListViewID);
        txtPairedDeviceListID = findViewById(R.id.txtPairedDeviceListID);


        if (getIntent().getExtras() != null) {
            ControllerSerialNumber = getIntent().getStringExtra(Constant.ControllerSerialNumber);
            debugDataExtract = getIntent().getStringExtra(Constant.debugDataExtract);
            isPeramterSet = getIntent().getStringExtra(Constant.isPeramterSet);

            set_matno = getIntent().getStringExtra(Constant.pendingSettingData);
            billNo = getIntent().getStringExtra(Constant.billNo);
        }

        try {
            bAdapter = BluetoothAdapter.getDefaultAdapter();

            if (bAdapter == null) {
                Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
                finish();
            } else if (!bAdapter.isEnabled()) {
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(PairedDeviceActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(turnBTon, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtPairedDeviceListID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDeviceList();
            }
        });


        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pairedDeviceList();


    }

    private void pairedDeviceList() {
        CustomUtility.showProgressDialogue(PairedDeviceActivity.this);
        if (bAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        } else {

            Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
            List mDeviceNameList = new ArrayList();
            List mDeviceMACAddressList = new ArrayList();
            if(pairedDevices.size()>0){
                for(BluetoothDevice device: pairedDevices){
                    String devicename = device.getName();
                    String macAddress = device.getAddress();
                    mDeviceNameList.add(devicename);
                     mDeviceMACAddressList.add(macAddress);
                }
                if (recyclerViewAdapter != null)
                    recyclerViewAdapter = null;

                recyclerViewAdapter = new BTPairedDeviceAdapter(mContext, mDeviceNameList,mDeviceMACAddressList,ControllerSerialNumber,debugDataExtract,isPeramterSet,
                        set_matno,billNo);
                rclSettingListViewID.setHasFixedSize(true);
                rclSettingListViewID.setAdapter(recyclerViewAdapter);
                CustomUtility.hideProgressDialog(PairedDeviceActivity.this);
            }
            else {
                CustomUtility.hideProgressDialog(PairedDeviceActivity.this);
                Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
