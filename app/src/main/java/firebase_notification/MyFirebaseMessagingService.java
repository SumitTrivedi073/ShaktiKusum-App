package firebase_notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shaktipumplimited.shaktikusum.R;

import activity.Login;
import activity.MainActivity;
import database.DatabaseHelper;
import utility.CustomUtility;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


	public static final String MyNoti = "CHANNEL_ID";

	PendingIntent pendingIntent;
	Intent intent;
	// Override onNewToken to get new token
	@Override
	public void onNewToken(@NonNull String token)
	{
		Log.e( "Refreshedtoken====>" , token);
	}
	// Override onMessageReceived() method to extract the
	// title and
	// body from the message passed in FCM
	@Override
	public void
	onMessageReceived(RemoteMessage remoteMessage)
	{
		// First case when notifications are received via
		// data event
		// Here, 'title' and 'message' are the assumed names
		// of JSON
		// attributes. Since here we do not have any data
		// payload, This section is commented out. It is
		// here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

		// Second case when notification payload is
		// received.
		Log.e("remoteMessage",remoteMessage.getNotification().getTitle());
		Log.e("remoteMessage",remoteMessage.getNotification().getBody());
		if (remoteMessage.getNotification() != null) {
			// Since the notification is received directly
			// from FCM, the title and the body can be
			// fetched directly as below.
			showNotification(
					remoteMessage.getNotification().getTitle(),
					remoteMessage.getNotification().getBody());
		}
	}


	// Method to display the notifications
	public void showNotification(String title, String message) {
		DatabaseHelper databaseHelper = new DatabaseHelper(this);

		if (databaseHelper.getLogin() && CustomUtility.getSharedPreferences(this, "CHECK_OTP_VERIFED").equals("Y")) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);

		} else {
				Intent intent = new Intent(this, Login.class);
				startActivity(intent);
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			pendingIntent = PendingIntent.getActivity(this,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

		}else {
			pendingIntent = PendingIntent.getActivity(this,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		}


		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(getApplicationContext(), MyNoti).setSmallIcon(
						R.mipmap.ic_notification).setContentTitle(title).setContentText(
						message).setStyle(new NotificationCompat.BigTextStyle()
						.bigText(message)).setAutoCancel(true).setContentIntent(pendingIntent);
		NotificationManager manager =
				(NotificationManager) getApplicationContext().getSystemService(
						NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(MyNoti, "Default channel",
					NotificationManager.IMPORTANCE_DEFAULT);
			manager.createNotificationChannel(channel);
		}
		manager.notify(0, builder.build());
	}
}