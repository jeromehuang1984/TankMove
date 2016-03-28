package game.jeromehuang.tankmove.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import game.jeromehuang.tankmove.data.MotionData;
import game.jeromehuang.tankmove.graphic.DashBoard;
import game.jeromehuang.tankmove.graphic.Tank;
import game.jeromehuang.tankmove.localconst.Config;
import game.jeromehuang.tankmove.util.SharedPreferencesHelper;

/**
 * Created by jerome.huang on 3/22/16.
 */
public class GameTankMove extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	/**
	 * 用于绘制的线程
	 */
	private Thread t;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning = true;
	private boolean isPaused = false;

	private Paint mPaint;
	private Paint mScorePaint;
	private Paint mTimePaint;
	private SharedPreferencesHelper sp;

	private Tank mTank;
	private DashBoard mDashboard;
	private int screenWidth;
	private int screenHeight;

	private enum GameStatus {
		WAITTING, RUNNING, STOP;
	}

	private enum InteractType {
		START, MOVE, STOP
	}
	/**
	 * 记录游戏的状态
	 */
	private GameStatus mStatus = GameStatus.WAITTING;
	private InteractType mInteractType = InteractType.STOP;

	@Override
	public void run() {
		while (isRunning) {
			drawGame();
		}
	}

	public GameTankMove(Context context) {
		this(context, null);
	}

	public GameTankMove(Context context, AttributeSet attrs) {
		super(context, attrs);
		sp = SharedPreferencesHelper.getInstance(getContext());
		mHolder = getHolder();
		mHolder.addCallback(this);

		setZOrderOnTop(true);// 设置画布 背景透明
		mHolder.setFormat(PixelFormat.TRANSLUCENT);

		// 设置可获得焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		// 设置常亮
		this.setKeepScreenOn(true);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);

		mPaint.setColor(Config.TANK_STROKE_COLOR);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);
		mPaint.setStyle(Paint.Style.STROKE);

		mTank = new Tank(mPaint);
		mTank.x = mTank.y = 320;

		mDashboard = DashBoard.getInstance(mPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenWidth = w;
		screenHeight = h;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// 开启线程
		isRunning = true;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// 通知关闭线程
		isRunning = false;
	}

	private ArrayList<MotionData> mMotionLst = new ArrayList<MotionData>();
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int pointerCount = event.getPointerCount();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mInteractType = InteractType.START;
				MotionData motion = new MotionData(event.getX(), event.getY(), 0);
				mMotionLst.add(motion);
				break;
			case MotionEvent.ACTION_MOVE:
				mInteractType = InteractType.MOVE;
				MotionData motionData0 = null;
				MotionData motionData1 = null;
				if (mMotionLst.size() > 0) {
					motionData0 = mMotionLst.get(0);
					motionData0.setCurrentPos(event.getX(), event.getY());
				}
				if (mMotionLst.size() > 1 && pointerCount > 1) {
					motionData1 = mMotionLst.get(1);
					motionData1.setCurrentPos(event.getX(1), event.getY(1));
				}
				break;
			case MotionEvent.ACTION_UP:
//				Log.d("jerome", "ACTION_UP " + pointerCount);
				mInteractType = InteractType.STOP;
				if (mMotionLst.size() > 0) {
					for (int i = 0; i < mMotionLst.size(); i++) {
						mDashboard.resetSpeedIfNeed(mMotionLst.get(i));
					}
				}
				mMotionLst.clear();
				mDashboard.setPreSpeeds();
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (mMotionLst.size() > 1) {
					MotionData motionData = mMotionLst.remove(1);
					mDashboard.setPreSpeed(motionData);
					mDashboard.resetSpeedIfNeed(motionData);
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (pointerCount > 1) {
					MotionData motionOther = new MotionData(event.getX(1), event.getY(1), 1);
					mMotionLst.add(motionOther);
				}
				break;
		}
		if (mInteractType != InteractType.MOVE) {
			return true;
		}
		if (mMotionLst.size() == 1) {
			MotionData motion = mMotionLst.get(0);
			if (motion.startX > screenWidth/2) {//move right bar
				mDashboard.changeRightSpeed(-motion.getDeltaY());
			} else {//move left bar
				mDashboard.changeLeftSpeed(-motion.getDeltaY());
			}
		} else if (mMotionLst.size() > 1) {
			MotionData leftMotion = mMotionLst.get(0);
			MotionData rightMotion = mMotionLst.get(1);
			MotionData tmp;
			if (leftMotion.startX > rightMotion.startX) {
				tmp = leftMotion;
				leftMotion = rightMotion;
				rightMotion = tmp;
			}
			mDashboard.changeSpeeds(-leftMotion.getDeltaY(), -rightMotion.getDeltaY());
		}
		return true;
	}

	private void drawGame() {
		try {
			Rect rect = null;
			mCanvas = mHolder.lockCanvas(rect);
			if (mCanvas != null) {
				mCanvas.drawColor(Color.WHITE);
				mTank.draw(mCanvas);
				mDashboard.draw(mCanvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mCanvas != null)
				mHolder.unlockCanvasAndPost(mCanvas);
		}
	}
}
