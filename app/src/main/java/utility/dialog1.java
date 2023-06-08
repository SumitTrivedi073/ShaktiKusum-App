package utility;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import activity.DeptDocSubActivity;
import com.shaktipumplimited.shaktikusum.R;

public class dialog1 extends Dialog {

    private final DeptDocSubActivity activity;
    private Button search, cancel;
    private EditText text;
    private final dialog1 thisDialog;

    public dialog1(DeptDocSubActivity context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.activity = context;
        this.thisDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_search);

        thisDialog.setCancelable(false);
        thisDialog.setCanceledOnTouchOutside(false);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        initalize();
    }

    private void initalize() {
        // TODO Auto-generated method stub
        text = findViewById(R.id.text);
        search = findViewById(R.id.search);
        cancel = findViewById(R.id.cancel);
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
