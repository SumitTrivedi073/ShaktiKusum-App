package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import com.shaktipumplimited.shaktikusum.R;


/**
 * Created by p90447 on 2015-11-18.
 */
public class CustomProgressDialog extends ProgressDialog {
    private TextView progressMessage;
    private final String mMessage;


    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomAlertDialogStyle);
          this.mMessage = context.getResources().getString(R.string.Loading);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_progress_dialog);

        progressMessage = findViewById(R.id.progressMessage);
        progressMessage.setText(mMessage);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
