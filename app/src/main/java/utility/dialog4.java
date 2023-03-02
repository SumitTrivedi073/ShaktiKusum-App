package utility;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shaktipumplimited.shaktikusum.DeviceStatusActivity;
import com.shaktipumplimited.shaktikusum.R;
import webservice.WebURL;

public class dialog4 extends Dialog {

    private DeviceStatusActivity activity;
    private Button search, cancel;
    private EditText text;
    private dialog4 thisDialog;

    public dialog4(DeviceStatusActivity context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.activity = context;
        this.thisDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_search1);

        thisDialog.setCancelable(false);
        thisDialog.setCanceledOnTouchOutside(false);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        initalize();
    }

    private void initalize() {
        // TODO Auto-generated method stub
        text = (EditText) findViewById(R.id.text);
        text.setText(WebURL.mDEvice_Number_CHECK);
        search = (Button) findViewById(R.id.search);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                thisDialog.cancel();
                activity.finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String textString = text.getText().toString();
                activity.searchWord(textString);
            }
        });
    }

}
