package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shaktipumplimited.shaktikusum.R;

import bean.ComplaintInstModel;
import debugapp.GlobalValue.Constant;

public class ComplaintFormActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton damageRadio,missedRadio,pumpRadio,motorRadio,controllerRadio;

    TextView submitBtn;

    EditText farmerNameExt, contactNumberExt, applicationNumberExt, addressExt, pumpSrNoExt, motorSrNoExt, controllerSrNoExt;
    ComplaintInstModel.Response complaintInstModel;

    private boolean isDamage = false,isMissed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_form);

        Init();
        retrieveValue();
        listner();
    }

    private void listner() {
        damageRadio.setOnClickListener(this);
        missedRadio.setOnClickListener(this);

    }

    private void Init() {
        farmerNameExt = findViewById(R.id.farmerNameExt);
        contactNumberExt = findViewById(R.id.contactNumberExt);
        applicationNumberExt = findViewById(R.id.applicationNumberExt);
        addressExt = findViewById(R.id.addressExt);
        pumpSrNoExt = findViewById(R.id.pumpSrNoExt);
        motorSrNoExt = findViewById(R.id.motorSrNoExt);
        controllerSrNoExt = findViewById(R.id.controllerSrNoExt);
        damageRadio = findViewById(R.id.damageRadio);
        missedRadio = findViewById(R.id.missedRadio);
        pumpRadio = findViewById(R.id.pumpRadio);
        motorRadio = findViewById(R.id.motorRadio);
        controllerRadio = findViewById(R.id.controllerRadio);
        submitBtn = findViewById(R.id.submitBtn);
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            complaintInstModel = (ComplaintInstModel.Response) getIntent().getSerializableExtra(Constant.InstallationCompData);

            assert complaintInstModel != null;
            farmerNameExt.setText(complaintInstModel.getName());
            contactNumberExt.setText(complaintInstModel.getContactNo());
            applicationNumberExt.setText(complaintInstModel.getBeneficiary());
            addressExt.setText(complaintInstModel.getAddress());
            pumpSrNoExt.setText(complaintInstModel.getPumpSernr());
            motorSrNoExt.setText(complaintInstModel.getMotorSernr());
            controllerSrNoExt.setText(complaintInstModel.getControllerSernr());
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.damageRadio:
                if (!isDamage){
                    damageRadio.setChecked(true);
                    isDamage = true;
                }else {
                    damageRadio.setChecked(false);
                    isDamage = false;
                }

                break;

            case R.id.missedRadio:
                if (!isMissed){
                    missedRadio.setChecked(true);
                    isMissed = true;
                }else {
                    missedRadio.setChecked(false);
                    isMissed = false;
                }
                break;
        }
    }
}