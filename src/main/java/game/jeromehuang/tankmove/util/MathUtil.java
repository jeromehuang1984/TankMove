package game.jeromehuang.tankmove.util;

import android.graphics.Rect;

import game.jeromehuang.tankmove.data.MotionData;

/**
 * Created by jerome.huang on 3/22/16.
 */
public class MathUtil {
	static public float angleToRadian(float angle) {
		return (float) ((angle/180) * Math.PI);
	}

	static public float cutValueInSection(float originalValue, float sectionSmall, float sectionBig) {
		if (originalValue < sectionSmall) {
			return sectionSmall;
		} else if (originalValue > sectionBig) {
			return sectionBig;
		}
		return originalValue;
	}

	static public boolean rectContains(Rect rect, MotionData motionData) {
		if (rect.contains((int)motionData.startX, (int)motionData.startY, (int)motionData.currentX, (int)motionData.currentY)) {
			return true;
		}
		return false;
	}

	static public boolean circleContains(float cx, float cy, float cRadius, MotionData motionData) {
		float d1 = getDistance(cx, cy, motionData.startX, motionData.startY);
		float d2 = getDistance(cx, cy, motionData.currentX, motionData.currentY);
		if (d1 <= cRadius && d2 <= cRadius) {
			return true;
		}
		return false;
	}
	static public float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
	}

}
