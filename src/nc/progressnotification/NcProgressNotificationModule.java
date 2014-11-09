package nc.progressnotification;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;

@Kroll.module(name="NcProgressNotification", id="nc.progressnotification")
public class NcProgressNotificationModule extends KrollModule {

	private static final String TAG = "NcProgressNotificationModule";

	@Kroll.constant public static final int PROGRESS_SUCCESS = 100;
	@Kroll.constant public static final int PROGRESS_ERROR = -2;
	
	public NcProgressNotificationModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(TAG, "inside onAppCreate");
	}

}