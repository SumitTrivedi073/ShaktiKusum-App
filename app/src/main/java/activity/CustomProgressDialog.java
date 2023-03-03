package activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shaktipumplimited.shaktikusum.R;


/**
 * Created by p90447 on 2015-11-18.
 */
public class CustomProgressDialog extends Dialog {
    private TextView progressMessage;
    public CustomProgressDialog(Context context, String message) {
        super(context, R.style.CustomAlertDialogStyle);
        //	this.mMessage = message;
        // TODO Auto-generated constructor stub
    }

    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomAlertDialogStyle);
        //    this.mMessage = context.getResources().getString(R.stringp-.Loading);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_progress_dialog);

        setCancelable(false);

        progressMessage = findViewById(R.id.progressMessage);
        //	progressMessage.setText(mMessage);
    }
}
