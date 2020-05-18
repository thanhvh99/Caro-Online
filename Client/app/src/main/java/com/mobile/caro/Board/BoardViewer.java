package com.mobile.caro.Board;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.BitmapManager;

public class BoardViewer extends SurfaceView {

    private enum Mode {
        TOUCH, MOVE, SCALE, WAIT
    }

    private float blockSize;
    private float markSize;
    private float markPadding;

    private AbstractPlayActivity activity;
    private SparseArray<PointF> tracker = new SparseArray<>();
    private Point lastMove = new Point(-1, -1);
    private Point confirmMove = new Point(-1, -1);
    private PointF surfaceSize = new PointF();
    private PointF offset = new PointF(0, 0);
    private Board board;
    private Mode mode = Mode.TOUCH;

    private Paint linePaint;
    private Paint lastPaint;
    private Paint confirmPaint;

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    onTouchAction(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    onMoveAction(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    onReleaseAction(event);
                    break;
            }
            return true;
        }
    };

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceSize.set(getWidth(), getHeight());
            blockSize = surfaceSize.x / 8f;

            linePaint = new Paint();
            linePaint.setColor(Color.BLACK);
            linePaint.setStrokeWidth(2);

            lastPaint = new Paint();
            lastPaint.setColor(Color.argb(255, 146, 39, 143));
            lastPaint.setStrokeWidth(4);

            confirmPaint = new Paint();
            confirmPaint.setColor(Color.argb(255, 0, 161, 75));
            confirmPaint.setStrokeWidth(4);

            scale(1);
            setOnTouchListener(onTouchListener);
            resetOffset();
            draw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    public BoardViewer(Context context) {
        super(context);
        getHolder().addCallback(callback);
    }

    public BoardViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(callback);
    }

    //////////////////////
    //      Private
    //////////////////////
    private void onTouchAction(MotionEvent event) {
        if (tracker.size() > 1) {
            return;
        }
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        tracker.put(id, new PointF(event.getX(index), event.getY(index)));
        if (tracker.size() == 2) {
            mode = Mode.SCALE;
        }
    }

    private void onMoveAction(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        float x = event.getX(index);
        float y = event.getY(index);

        switch (mode) {
            case TOUCH:
                if (distance(tracker.get(id), new PointF(x, y)) > blockSize / 2f) {
                    mode = Mode.MOVE;
                    tracker.get(id).set(x, y);
                }
                break;
            case MOVE:
                PointF last = tracker.get(id);
                offset.x += last.x - x;
                offset.y += last.y - y;
                limitOffset();
                last.set(x, y);
                break;
            case SCALE:
                float lastDistance = distance(tracker.valueAt(0), tracker.valueAt(1));
                tracker.get(id).set(x, y);
                float currentDistance = distance(tracker.valueAt(0), tracker.valueAt(1));
                float scale = currentDistance / lastDistance;
                scale(scale);
                offset.x = (offset.x + surfaceSize.x / 2f) * scale - surfaceSize.x / 2f;
                offset.y = (offset.y + surfaceSize.y / 2f) * scale - surfaceSize.y / 2f;
                limitOffset();
                break;
        }
        draw();
    }

    private void onReleaseAction(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        float x = event.getX(index);
        float y = event.getY(index);
        tracker.delete(id);
        switch (mode) {
            case SCALE:
                mode = Mode.WAIT;
                break;
            case MOVE:
            case WAIT:
                mode = Mode.TOUCH;
                break;
            case TOUCH:
                if (activity != null) {
                    activity.onTileSelected((int) ((x + offset.x) / blockSize), (int) ((y + offset.y) / blockSize));
                }
                break;
        }
    }

    private void scale(float value) {
        blockSize = Math.max(surfaceSize.x / board.getSize(), blockSize * value);
        markPadding = blockSize / 8;
        markSize = blockSize - markPadding * 2;
    }

    private float distance(PointF p1, PointF p2) {
        return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    private void resetOffset() {
        offset.x = (blockSize * board.getSize() - surfaceSize.x) / 2;
        offset.y = (blockSize * board.getSize() - surfaceSize.y) / 2;
    }

    private void limitOffset() {
        if (blockSize * board.getSize() < surfaceSize.x) {
            offset.x = (blockSize * board.getSize() - surfaceSize.x) / 2;
        } else {
            offset.x = Math.max(0, offset.x);
            offset.x = Math.min(board.getSize() * blockSize - surfaceSize.x, offset.x);
        }
        if (blockSize * board.getSize() < surfaceSize.y) {
            offset.y = (blockSize * board.getSize() - surfaceSize.y) / 2;
        } else {
            offset.y = Math.max(0, offset.y);
            offset.y = Math.min(board.getSize() * blockSize - surfaceSize.y, offset.y);
        }
    }

    /////////////////////
    //      Public
    /////////////////////
    public void draw() {
        if (board == null) {
            return;
        }
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);
            int[][] matrix = board.getMatrix();
            int size = board.getSize();
            RectF rect = new RectF();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (matrix[i][j] == Board.VALUE_BLANK) {
                        continue;
                    }
                    float x = j * blockSize + markPadding - offset.x;
                    float y = i * blockSize + markPadding - offset.y;
                    rect.set(x, y, x + markSize, y + markSize);
                    canvas.drawBitmap(matrix[i][j] == Board.VALUE_O ? BitmapManager.getO() : BitmapManager.getX(), null, rect, null);
                }
            }
            for (int i = 0; i <= size; i++) {
                canvas.drawLine(i * blockSize - offset.x, -offset.y, i * blockSize - offset.x, size * blockSize - offset.y, linePaint);
                canvas.drawLine(-offset.x, i * blockSize - offset.y, size * blockSize - offset.x, i * blockSize - offset.y, linePaint);
            }
            if (!lastMove.equals(-1, -1)) {
                float x = lastMove.x * blockSize - offset.x;
                float y = lastMove.y * blockSize - offset.y;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        canvas.drawLine(x + i * blockSize, y + i * blockSize, x + j * blockSize, y + (1 - j) * blockSize, lastPaint);
                    }
                }
            }
            if (!confirmMove.equals(-1, -1)) {
                float x = confirmMove.x * blockSize - offset.x;
                float y = confirmMove.y * blockSize - offset.y;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        canvas.drawLine(x + i * blockSize, y + i * blockSize, x + j * blockSize, y + (1 - j) * blockSize, confirmPaint);
                    }
                }
            }
        } catch (Exception e)  {
            e.printStackTrace();
        } finally {
            if (canvas!= null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    public void setBoard(Board board) {
        this.board = board;
        resetOffset();
    }

    public void setLastMove(int x, int y) {
        lastMove.set(x, y);
    }

    public void setConfirmMove(int x, int y) {
        confirmMove.set(x, y);
    }

    public void removeConfirmMove() {
        confirmMove.set(-1, -1);
    }

    public Point getConfirmMove() {
        return confirmMove;
    }

    public void setActivity(AbstractPlayActivity activity) {
        this.activity = activity;
    }

}
