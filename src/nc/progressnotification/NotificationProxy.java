package nc.progressnotification;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.util.TiConvert;

import ti.modules.titanium.android.PendingIntentProxy;
import android.app.Activity;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

@Kroll.proxy(creatableInModule=NcProgressNotificationModule.class)
public class NotificationProxy extends KrollProxy {

	private static final String PROPERTY_ID = "id";
	private static final String PROPERTY_TITLE = "title";
	private static final String PROPERTY_TEXT = "text";
	private static final String PROPERTY_ICON = "icon";
	private static final String PROPERTY_NUMBER = "number";
	private static final String PROPERTY_PENDING_INTENT = "pendingIntent";
	
	private NotificationManager mNotifyManager;
	private NotificationCompat.Builder mBuilder;
	private int mNotifyId = -1;

	public NotificationProxy() {
		super();
		
		TiApplication app = TiApplication.getInstance();
		mNotifyManager = (NotificationManager) app.getSystemService(Activity.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(app.getApplicationContext());
		makeOngoingProgress(0, true); // make progress bar indeterminate when 1st notify
	}
	
	public NotificationProxy(TiContext tiContext) {
		this();
	}
	
	@Override
	public void handleCreationDict(KrollDict dict) {
		super.handleCreationDict(dict);
		
		if (dict.containsKey(PROPERTY_ID)) {
			setId(TiConvert.toInt(dict, PROPERTY_ID));
		} else {
			throw new IllegalArgumentException("property 'id' is required");
		}
		if (dict.containsKey(PROPERTY_TITLE)) {
			setTitle(TiConvert.toString(dict, PROPERTY_TITLE));
		}
		if (dict.containsKey(PROPERTY_TEXT)) {
			setText(TiConvert.toString(dict, PROPERTY_TEXT));
		}
		if (dict.containsKey(PROPERTY_ICON)) {
			setIcon(TiConvert.toInt(dict, PROPERTY_ICON));
		}
		if (dict.containsKey(PROPERTY_PENDING_INTENT)) {
			setPendingIntent((PendingIntentProxy) dict.get(PROPERTY_PENDING_INTENT));
		}
		if (dict.containsKey(PROPERTY_NUMBER)) {
			setNumber(TiConvert.toInt(dict, PROPERTY_NUMBER));
		}
	}
	
	@Kroll.method @Kroll.setProperty
	public void setId(int id) {
		mNotifyId = id;
	}
	
	@Kroll.method @Kroll.setProperty
	public void setTitle(String title) {
		mBuilder.setContentTitle(title);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setText(String text) {
		mBuilder.setContentText(text);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setIcon(int icon) {
		mBuilder.setSmallIcon(icon);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setPendingIntent(PendingIntentProxy pendingIntentProxy) {
		mBuilder.setContentIntent(pendingIntentProxy.getPendingIntent());
	}
	
	@Kroll.method @Kroll.setProperty
	public void setNumber(int number) {
		mBuilder.setNumber(number);
	}
	
	//
	// progress = 0 to 100
	//
	@Kroll.method @Kroll.setProperty
	public void setProgress(int progress) {
		makeOngoingProgress(progress, false);
	}
	
	@Kroll.method
	public void notify(@Kroll.argument(optional=true) int progress) {
		// since `progress` is optional, default value is -1
		if (progress == NcProgressNotificationModule.PROGRESS_ERROR) { // complete with error
			makeCancellableProgress(false);
		}
		else if (progress == NcProgressNotificationModule.PROGRESS_SUCCESS) { // complete successfully
			makeCancellableProgress(true);
		}
		else {
			setProgress(progress);
		}
		mNotifyManager.notify(mNotifyId, mBuilder.build());
	}
	
	@Kroll.method
	public void cancel() {
		if (mNotifyId != -1) {
			mNotifyManager.cancel(mNotifyId);
			mNotifyId = -1;
		}
	}

	private void makeCancellableProgress(boolean hideProgress) {
		mBuilder.setProgress((hideProgress ? 0 : 100), 0, false);
		mBuilder.setOngoing(false);
		mBuilder.setAutoCancel(true);
	}
	
	private void makeOngoingProgress(int progress, boolean indeterminate) {
		mBuilder.setProgress(100, progress, indeterminate);
		mBuilder.setOngoing(true);
		mBuilder.setAutoCancel(false);
	}

}
