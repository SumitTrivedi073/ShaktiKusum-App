package activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumplimited.shaktikusum.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DeviceMappingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView write_btn, read_btn, UpdateDeviceBtn, countDownTimerTxt, checkDeviceStatusBtn;
    ImageView writeImg, read_img;

    CountDownTimer timer;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_mapping);
        inIt();
        listner();
    }

    private void inIt() {
        mToolbar =  findViewById(R.id.toolbar);

        write_btn = findViewById(R.id.write_btn);
        writeImg = findViewById(R.id.writeImg);
        read_btn = findViewById(R.id.read_btn);
        read_img = findViewById(R.id.read_img);
        UpdateDeviceBtn = findViewById(R.id.UpdateDeviceBtn);
        countDownTimerTxt = findViewById(R.id.countDownTimerTxt);
        checkDeviceStatusBtn = findViewById(R.id.checkDeviceStatusBtn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingInstallationVerification));


        changeButtonVisibility("0");
    }

    private void listner() {
        write_btn.setOnClickListener(this);
        read_btn.setOnClickListener(this);
        UpdateDeviceBtn.setOnClickListener(this);
        countDownTimerTxt.setOnClickListener(this);
        checkDeviceStatusBtn.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void startCountDownTimer() {
        timer = new CountDownTimer(/*900000*/30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                countDownTimerTxt.setText("Please Wait \n"+ f.format(min) + " Min. " + f.format(sec)+" Sec. \nDevice Installing latest vesion ");
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                countDownTimerTxt.setVisibility(View.GONE);
                changeButtonVisibility("4");

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_btn:
                changeButtonVisibility("1");
                break;
            case R.id.read_btn:
                changeButtonVisibility("2");
                break;
            case R.id.UpdateDeviceBtn:
                changeButtonVisibility("3");

                break;
            case R.id.checkDeviceStatusBtn:
                break;
        }
    }

    private void changeButtonVisibility(String buttonId) {

        switch (buttonId) {

            case "0":
                write_btn.setEnabled(true);
                write_btn.setAlpha(1f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                UpdateDeviceBtn.setEnabled(false);
                UpdateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(false);
                checkDeviceStatusBtn.setAlpha(0.5f);
                break;
            case "1":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(true);
                read_btn.setAlpha(1f);
                UpdateDeviceBtn.setEnabled(false);
                UpdateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(false);
                checkDeviceStatusBtn.setAlpha(0.5f);
                writeImg.setImageResource(R.drawable.right_mark_icn_green);
                break;

            case "2":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                UpdateDeviceBtn.setEnabled(true);
                UpdateDeviceBtn.setAlpha(1f);
                checkDeviceStatusBtn.setEnabled(false);
                checkDeviceStatusBtn.setAlpha(0.5f);
                read_img.setImageResource(R.drawable.right_mark_icn_green);

                break;

            case "3":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                UpdateDeviceBtn.setEnabled(false);
                UpdateDeviceBtn.setAlpha(0.5f);
                startCountDownTimer();
                break;

            case "4":
                write_btn.setEnabled(false);
                write_btn.setAlpha(0.5f);
                read_btn.setEnabled(false);
                read_btn.setAlpha(0.5f);
                UpdateDeviceBtn.setEnabled(false);
                UpdateDeviceBtn.setAlpha(0.5f);
                checkDeviceStatusBtn.setEnabled(true);
                checkDeviceStatusBtn.setAlpha(1f);

                break;
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountDownTImer();
    }

    private void stopCountDownTImer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}