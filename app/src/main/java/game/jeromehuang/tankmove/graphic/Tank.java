package game.jeromehuang.tankmove.graphic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import game.jeromehuang.tankmove.localconst.Config;
import game.jeromehuang.tankmove.util.MathUtil;

/**
 * Created by jerome.huang on 3/22/16.
 */
public class Tank {
	public float x;
	public float y;
	public float height;
	public float width;

	private Paint mPaint;
	private int rotation = 110;

	private int r = 25;
	private int d = 15;
	private int l = 35;
	private float wheelLen = 110;
	private float wheelWidth = 15;
	private float bodyLen = 90;
	private float bodyWidth = 74;


	public Tank(Paint paint) {
		mPaint = paint;
	}

	public void draw(Canvas canvas) {
//		canvas.translate(x, y);
//		Path path = new Path();
//		int r = 50;
//		path.moveTo(r, 0);
//		k++;
//		for (int i = 0; i < k; i++) {
//			float px = (float) (r * Math.cos((double) MathUtil.angleToRadian(i * 4)));
//			float py = (float) (r * Math.sin((double) MathUtil.angleToRadian(i * 4)));
//			path.lineTo(px, py);
//		}
//		canvas.drawPath(path, mPaint);
//		canvas.save();
//		canvas.restore();
//		if (k >= 90) {
//			k = 1;
//		}

		canvas.save();
		canvas.translate(x, y);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Config.TANK_STROKE_COLOR);
        float x = wheelLen / 2;
        float leftWheelSpeed = 10;//DashBoard.getInstance(null).getLeftWheelSpeed();
        float rightWheelSpeed = 5;//DashBoard.getInstance(null).getLeftWheelSpeed();

        float x1 = (wheelLen - leftWheelSpeed) / 2;
        float x2 = (wheelLen - rightWheelSpeed) / 2;
        float y1 = wheelWidth / 2;
        float y2 = y1 + bodyWidth + wheelWidth;
        float y3 = ((y2-y1) * x + x2 * y1 - x1 * y2)/(x2-x1);
        if (Math.abs(leftWheelSpeed - rightWheelSpeed) > 0.01) {
            canvas.drawCircle(x, y3, 5, mPaint); //draw rotate center
        }
        float smallerSpeed = leftWheelSpeed < rightWheelSpeed ? leftWheelSpeed : rightWheelSpeed;
        float rotateRadius = Math.abs(y3 - y1);
        float degrees = MathUtil.radianToAngle(smallerSpeed / (rotateRadius * 2 * (float) Math.PI));
        Matrix transform = new Matrix();
        transform.setRotate(degrees, x, y3);
        canvas.setMatrix(transform);
		//draw wheel 1
//		canvas.drawRect(-wheelLen/2, -bodyWidth/2 - wheelWidth, wheelLen/2, bodyWidth/2 + wheelWidth, mPaint);
		canvas.drawRect(-wheelLen/2, -bodyWidth/2 - wheelWidth, wheelLen/2, -bodyWidth/2, mPaint);
		//draw wheel 2
		canvas.drawRect(-wheelLen/2, bodyWidth/2, wheelLen/2, bodyWidth/2 + wheelWidth, mPaint);
		//draw body
		canvas.drawRect(-bodyLen/2, -bodyWidth/2, bodyLen/2, bodyWidth/2, mPaint);
		//draw wheel lines
		for (float i = wheelWidth; i < wheelLen; i = i + 1) {
			canvas.drawLine(-wheelLen/2 + i, -bodyWidth/2 - wheelWidth, -wheelLen/2 + i, -bodyWidth/2, mPaint);
			canvas.drawLine(-wheelLen/2 + i, bodyWidth/2, -wheelLen/2 + i, bodyWidth/2 + wheelWidth, mPaint);
			i += wheelWidth;
		}

		//draw tank header
		Path path = new Path();
		float startRadian = (float) Math.asin((float)d / (2 * r));
		float endRadian = (float) Math.PI * 2 - startRadian;
		float startX = r * (float) Math.cos(startRadian);
		float startY = r * (float) Math.sin(startRadian);
		float endX = startX;
		float endY = -startY;
		path.moveTo(startX, startY);
		for (int i = 0; i < 60; i++) {
			float radian = MathUtil.angleToRadian(i * 6);
			if (radian < startRadian) {
				continue;
			}
			if (radian > endRadian) {
				path.lineTo(endX, endY);
				break;
			}
			float px = (float) (r * Math.cos(radian));
			float py = (float) (r * Math.sin(radian));
			path.lineTo(px, py);
		}
		path.lineTo(endX + l, endY);
		path.lineTo(endX + l, endY + d);
		path.lineTo(startX, startY);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path, mPaint);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Config.TANK_HEADER_FILL_COLOR);
		canvas.drawPath(path, mPaint);
		mPaint.setColor(Config.TANK_STROKE_COLOR);
		canvas.restore();
	}
}
