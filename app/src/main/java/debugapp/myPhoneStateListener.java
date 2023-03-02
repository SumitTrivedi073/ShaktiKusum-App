package debugapp;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import debugapp.GlobalValue.Constant;

public class myPhoneStateListener extends PhoneStateListener {
    public int signalStrengthValue;

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        if (signalStrength.isGsm()) {
            if (signalStrength.getGsmSignalStrength() != 99)
                signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
            else
                signalStrengthValue = signalStrength.getGsmSignalStrength();
        } else {
            signalStrengthValue = signalStrength.getCdmaDbm();
        }
        Constant.Check_Signal_str = signalStrengthValue;
        System.out.println("Constant.Check_Signal_str==>>"+Constant.Check_Signal_str);      //  txtSignalStr.setText("Signal Strength : " + signalStrengthValue);
    }
}
