package game.jeromehuang.tankmove;

import android.app.Application;
import android.content.Context;

/**
 * Created by jerome.huang on 3/22/16.
 */
public class TankMoveApp extends Application {
	static Context context;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
	}

	public static Context getAppContext() {
		return TankMoveApp.context;
	}
}
