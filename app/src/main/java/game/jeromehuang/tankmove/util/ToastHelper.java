package game.jeromehuang.tankmove.util;

import android.view.Gravity;
import android.widget.Toast;

import game.jeromehuang.tankmove.TankMoveApp;

public class ToastHelper {

	public static void show(String msg) {
		Toast toast = Toast.makeText(TankMoveApp.getAppContext(), msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	public static void show(int msg) {
		Toast toast = Toast.makeText(TankMoveApp.getAppContext(), msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
}
