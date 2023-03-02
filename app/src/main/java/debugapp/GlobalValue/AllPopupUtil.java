package debugapp.GlobalValue;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Set;

import com.shaktipumplimited.shaktikusum.R;

public class AllPopupUtil {



    public BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

    private static List<String> mFileNameList;
    private static List<String> mFilePathList;





   public static void ExitFromRealMonitoring(final Context context){

       // custom dialog
       final Dialog dialog = new Dialog(context);
       dialog.setContentView(R.layout.bt_popup_layout_view);
       //dialog.setTitle("");
       dialog.setCancelable(false);
       // set the custom dialog components - text, image and button
       TextView txtBTHeadingTopID = (TextView) dialog.findViewById(R.id.txtBTHeadingTopID);

       TextView txtBTDisableBTNID = (TextView) dialog.findViewById(R.id.txtBTDisableBTNID);
       TextView txtBTEnableBTNID = (TextView) dialog.findViewById(R.id.txtBTEnableBTNID);


       // if button is clicked, close the custom dialog
       txtBTDisableBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               dialog.dismiss();
           }
       });

       txtBTEnableBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               dialog.dismiss();


           }
       });

       dialog.show();

   }


   public static void btPopupCreateShow(final Context context){

       // custom dialog


       final Dialog dialog = new Dialog(context);
       dialog.setContentView(R.layout.bt_popup_layout_view);
       //dialog.setTitle("");
       dialog.setCancelable(false);
       // set the custom dialog components - text, image and button
       TextView txtBTHeadingTopID = (TextView) dialog.findViewById(R.id.txtBTHeadingTopID);

       TextView txtBTDisableBTNID = (TextView) dialog.findViewById(R.id.txtBTDisableBTNID);
       TextView txtBTEnableBTNID = (TextView) dialog.findViewById(R.id.txtBTEnableBTNID);


       // if button is clicked, close the custom dialog
       txtBTDisableBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               dialog.dismiss();
           }
       });

       txtBTEnableBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               context.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
               dialog.dismiss();


           }
       });

       dialog.show();

   }


    public static boolean pairedDeviceListGloable(Context mContext) {

        final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bAdapter==null){
            Toast.makeText(mContext,"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();

            if(pairedDevices.size()>0){

               return true;
            }
            else
            {
                Toast.makeText(mContext, "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
                return false;

            }
        }
    }


    public static void BT_OR_Internet_SelectionFun(final Context context){

        // custom dialog
        final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bt_or_internet_select_option);
        //dialog.setTitle("");
        dialog.setCancelable(true);

        final TextView txtInternetPopUpID, txtBluetoothPopUpID, txtOkayPopUpID;
        // set the custom dialog components - text, image and button
        TextView txtBTHeadingTopID = (TextView) dialog.findViewById(R.id.txtBTHeadingTopID);

         txtBluetoothPopUpID = (TextView) dialog.findViewById(R.id.txtBluetoothPopUpID);
         txtInternetPopUpID = (TextView) dialog.findViewById(R.id.txtInternetPopUpID);
         txtOkayPopUpID = (TextView) dialog.findViewById(R.id.txtOkayPopUpID);

        // ImageView image = (ImageView) dialog.findViewById(R.id.image);
        // image.setImageResource(R.drawable.ic_launcher);

        // Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog

        if(Constant.CHECK_FOR_WORK_WITH_BT_OR_IN == 0)
        {
            txtBluetoothPopUpID.setBackgroundResource(R.drawable.left_round_corner_white);
            txtInternetPopUpID.setBackgroundResource(R.drawable.right_round_corner);
        }
        else
        {
            txtBluetoothPopUpID.setBackgroundResource(R.drawable.left_round_corner);
            txtInternetPopUpID.setBackgroundResource(R.drawable.right_round_corner_white);
        }

        txtInternetPopUpID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtBluetoothPopUpID.setBackgroundResource(R.drawable.left_round_corner_white);
                txtInternetPopUpID.setBackgroundResource(R.drawable.right_round_corner);
                Constant.CHECK_FOR_WORK_WITH_BT_OR_IN = 0;/////////this is for internet
               // dialog.dismiss();

            }
        });

        txtBluetoothPopUpID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtBluetoothPopUpID.setBackgroundResource(R.drawable.left_round_corner);
                txtInternetPopUpID.setBackgroundResource(R.drawable.right_round_corner_white);

                Constant.CHECK_FOR_WORK_WITH_BT_OR_IN = 1;/////////this is for bluetooth
              //  context.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));

                //dialog.dismiss();



            }
        });
        txtOkayPopUpID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.CHECK_FOR_WORK_WITH_BT_OR_IN == 1) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                }

                dialog.dismiss();



            }
        });

        dialog.show();

    }




}
