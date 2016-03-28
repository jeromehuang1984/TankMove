package game.jeromehuang.tankmove.data;

/**
 * Created by jerome.huang on 3/23/16.
 */
public class MotionData {
	public int index;
	public float startX;
	public float startY;
	public float currentX;
	public float currentY;
    public MotionType type;

    public enum MotionType {
        LEFT, RIGHT, INVALID
    }

	public MotionData(float initialX, float initialY, int pointerIndex) {
		index = pointerIndex;
		startX = initialX;
		startY = initialY;
	}

	public void setCurrentPos(float x, float y) {
		currentX = x;
		currentY = y;
	}

	public float getDeltaY() {
		return currentY - startY;
	}
}
