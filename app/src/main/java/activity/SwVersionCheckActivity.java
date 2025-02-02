package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shaktipumplimited.shaktikusum.R;

import debugapp.GlobalValue.Constant;
import utility.CustomUtility;

public class SwVersionCheckActivity extends AppCompatActivity {
    TextView okbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw_version_check);

        okbtn = findViewById(R.id.okbtn);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.APPURL))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.APPURL))));
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}