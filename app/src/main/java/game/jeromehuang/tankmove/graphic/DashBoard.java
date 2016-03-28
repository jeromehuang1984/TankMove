package game.jeromehuang.tankmove.graphic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import game.jeromehuang.tankmove.data.MotionData;
import game.jeromehuang.tankmove.localconst.Config;
import game.jeromehuang.tankmove.util.MathUtil;
import game.jeromehuang.tankmove.util.ScreenHelper;

/**
 * Dashboard for tank
 */
public class DashBoard {

	private Paint mPaint;
	private int maxWidth;
	private int maxHeight;
	private int mBarWidth = 20;
	private int mBarHeight = 220;
	private int mGap = 100;
	private int mBottomGap = 20;
	private float rightWheelSpeed = 0;
	private float leftWheelSpeed = 0;
	private int maxSpeedValue = 0;
	private int opRadius = 60;

	public float x;
	public float y;

	public DashBoard(Paint paint) {
		maxSpeedValue = mBarHeight / 2;
		mPaint = paint;
		maxWidth = ScreenHelper.getInstance().getDisplaymetrics().widthPixels;
		maxHeight = ScreenHelper.getInstance().getDisplaymetrics().heightPixels;
		x = 0;
		y = maxHeight - mBarHeight - mBottomGap;
	}

    private static DashBoard _instance = null;

    public static DashBoard getInstance(Paint paint) {
        if (_instance == null) {
            _instance = new DashBoard(paint);
        }
        return _instance;
    }

	public void changeSpeeds(float leftSpeed, float rightSpeed) {
		changeLeftSpeed(leftSpeed);
		changeRightSpeed(rightSpeed);
	}

	private float preLeftSpeed = 0;
	private float preRightSpeed = 0;
	public void changeLeftSpeed(float leftSpeed) {
		leftWheelSpeed = MathUtil.cutValueInSection(preLeftSpeed + leftSpeed, -maxSpeedValue, maxSpeedValue);
	}

	public void changeRightSpeed(float rightSpeed) {
		rightWheelSpeed = MathUtil.cutValueInSection(preRightSpeed + rightSpeed, -maxSpeedValue, maxSpeedValue);
	}

	public void setPreSpeeds() {
		preLeftSpeed = leftWheelSpeed;
		preRightSpeed = rightWheelSpeed;
	}

	public void setPreSpeed(MotionData motionData) {
		if (motionData.startX < maxHeight/2) {
			preLeftSpeed = leftWheelSpeed;
		} else {
			preRightSpeed = rightWheelSpeed;
		}
	}

	public void resetSpeedIfNeed(MotionData motionData) {
		if (MathUtil.circleContains(opRadius, maxHeight - opRadius, opRadius, motionData)) {
			preLeftSpeed = leftWheelSpeed = 0;
		}
		if (MathUtil.circleContains(maxWidth - opRadius, maxHeight - opRadius, opRadius, motionData)) {
			preRightSpeed = rightWheelSpeed = 0;
		}
	}

    public float getRightWheelSpeed() {
        return rightWheelSpeed * .1f;
    }

    public float getLeftWheelSpeed() {
        return leftWheelSpeed * .1f;
    }

	public void draw(Canvas canvas) {
//		mPaint.setColor(Config.TANK_STROKE_COLOR);
//		mPaint.setStyle(Paint.Style.STROKE);
//		canvas.save();
////		canvas.drawCircle(50, 50, 50, mPaint);
////		canvas.drawCircle(maxWidth - 50, y - 50, 50, mPaint);
//		canvas.translate(x, y);
//		for (int i = 0; i <= mBarHeight;) {
//			//draw left bar
//			canvas.drawLine(mGap, i, mGap + mBarWidth, i, mPaint);
//			//draw right bar
//			canvas.drawLine(maxWidth - mGap, i, maxWidth - mGap - mBarWidth, i, mPaint);
//			i += 5;
//		}
//		canvas.restore();

		canvas.save();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.LTGRAY);
		//draw left circle
		canvas.drawCircle(opRadius, maxHeight - opRadius, opRadius, mPaint);
		//draw right circle
		canvas.drawCircle(maxWidth - opRadius, maxHeight - opRadius, opRadius, mPaint);

		float startY = 0;
		float endY = 0;
		//draw left bar
		if (leftWheelSpeed > 0) {
			mPaint.setColor(Config.DASHBOARD_COLOR_UP);
			startY = y + maxSpeedValue - leftWheelSpeed;
			endY = y + maxSpeedValue;
		} else if (leftWheelSpeed < 0.01) {
			mPaint.setColor(Config.DASHBOARD_COLOR_DOWN);
			startY = y + maxSpeedValue;
			endY = y + maxSpeedValue - leftWheelSpeed;
		}
		drawSpeedIndicator(canvas, mGap, startY, endY);

		//draw right bar
		if (rightWheelSpeed > 0) {
			mPaint.setColor(Config.DASHBOARD_COLOR_UP);
			startY = y + maxSpeedValue - rightWheelSpeed;
			endY = y + maxSpeedValue;
		} else if (rightWheelSpeed < 0.01) {
			mPaint.setColor(Config.DASHBOARD_COLOR_DOWN);
			startY = y + maxSpeedValue;
			endY = y + maxSpeedValue - rightWheelSpeed;
		}
		drawSpeedIndicator(canvas, maxWidth - mGap - mBarWidth, startY, endY);
		canvas.restore();
	}

	private void drawSpeedIndicator(Canvas canvas, float sx, float startY, float endY) {
		if (startY < .01) {
			return;
		}
		for (float i = startY; i < endY;) {
			canvas.drawLine(sx, i, sx + mBarWidth, i, mPaint);
			i += 5;
			if (i > endY) {
				i = endY;
				canvas.drawLine(sx, i, sx + mBarWidth, i, mPaint);
				break;
			}
		}
	}

	public int getHeight() {
		return mBarHeight;
	}
}
