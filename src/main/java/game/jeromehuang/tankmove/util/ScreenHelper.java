package game.jeromehuang.tankmove.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by jerome.huang on 3/23/16.
 */
public class ScreenHelper {
	DisplayMetrics displaymetrics = new DisplayMetrics();
	private static ScreenHelper _instance = null;

	public static ScreenHelper getInstance() {
		if (_instance == null) {
			_instance = new ScreenHelper();
		}
		return _instance;
	}

	public ScreenHelper() {}

	public void init(Activity context) {
		context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	}

	public DisplayMetrics getDisplaymetrics() {
		return displaymetrics;
	}
}
