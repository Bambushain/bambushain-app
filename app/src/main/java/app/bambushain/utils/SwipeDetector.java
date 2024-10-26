package app.bambushain.utils;

import android.view.MotionEvent;
import android.view.View;

import lombok.Setter;
import lombok.val;

public class SwipeDetector implements View.OnTouchListener {
    float x1, x2, y1, y2;
    @Setter
    private OnSwipeDetectListener onSwipeLeftListener;
    @Setter
    private OnSwipeDetectListener onSwipeRightListener;
    @Setter
    private OnSwipeDetectListener onSwipeUpListener;
    @Setter
    private OnSwipeDetectListener onSwipeDownListener;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        val threshold = 400;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                SwipeDirection direction;

                float xDiff = x2 - x1;
                float yDiff = y2 - y1;
                if (Math.abs(xDiff) > Math.abs(yDiff) && (xDiff > threshold || xDiff < -threshold)) {
                    if (x1 < x2) {
                        direction = SwipeDirection.RIGHT;
                    } else {
                        direction = SwipeDirection.LEFT;
                    }
                } else if (yDiff > threshold || yDiff < -threshold) {
                    if (y1 > y2) {
                        direction = SwipeDirection.UP;
                    } else {
                        direction = SwipeDirection.DOWN;
                    }
                } else {
                    direction = null;
                }

                if (direction == SwipeDirection.RIGHT && onSwipeRightListener != null) {
                    onSwipeRightListener.onSwipe();
                } else if (direction == SwipeDirection.LEFT && onSwipeLeftListener != null) {
                    onSwipeLeftListener.onSwipe();
                } else if (direction == SwipeDirection.UP && onSwipeUpListener != null) {
                    onSwipeUpListener.onSwipe();
                } else if (direction == SwipeDirection.DOWN && onSwipeDownListener != null) {
                    onSwipeDownListener.onSwipe();
                } else {
                    view.performClick();
                }

                break;
            default:
                view.performClick();
                break;
        }

        return false;
    }

    enum SwipeDirection {
        UP, RIGHT, DOWN, LEFT
    }


    public interface OnSwipeDetectListener {
        void onSwipe();
    }
}
