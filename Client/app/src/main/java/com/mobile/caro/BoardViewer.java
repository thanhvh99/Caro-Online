package com.mobile.caro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class BoardViewer extends SurfaceView implements SurfaceHolder.Callback {

    private int blockSize = 50;
    private int markSize;
    private int markPadding;

    private AbstractPlayActivity activity;
    private SparseArray<PointF> tracker = new SparseArray<>();
    private Point lastMove = new Point(-1, -1);
    private Point surfaceSize = new Point();
    private Point offset = new Point(0, 0);
    private float scale = 1;
    private Board board;

    public BoardViewer(Context context) {
        super(context);
    }

    public BoardViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceSize.set(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
        calculateSize();
        setupListener();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //////////////////////
    //      Private
    //////////////////////
    private void setupListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });
    }

    private void addOffset(int x, int y) {
        offset.x += x;
        offset.x = Math.max(0, offset.x);
        offset.x = Math.min(board.getSize() * blockSize - surfaceSize.x, offset.x);

        offset.y += y;
        offset.y = Math.max(0, offset.y);
        offset.y = Math.min(board.getSize() * blockSize - surfaceSize.y, offset.y);

        draw();
    }

    private void resetOffset() {
        offset.x = (blockSize * board.getSize() - surfaceSize.x) / 2;
        offset.y = (blockSize * board.getSize() - surfaceSize.y) / 2;
    }

    private void draw() {
        if (board == null) {
            return;
        }
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);
            int[][] matrix = board.getMatrix();
            int size = board.getSize();
            Rect rect = new Rect();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (matrix[i][j] == Board.VALUE_NULL) {
                        continue;
                    }
                    int x = j * blockSize + markPadding - offset.x;
                    int y = i * blockSize + markPadding - offset.y;
                    rect.set(x, y, x + markSize, y + markSize);
                    canvas.drawBitmap(matrix[i][j] == Board.VALUE_O ? BitmapManager.getO() : BitmapManager.getX(), null, rect, null);
                }
            }
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            for (int i = 1; i < size; i++) {
                canvas.drawLine(i * blockSize - offset.x, -offset.y, i * blockSize - offset.x, size * blockSize - offset.y, paint);
                canvas.drawLine(-offset.x, i * blockSize - offset.y, size * blockSize - offset.x, i * blockSize - offset.y, paint);
            }
        } catch (Exception e)  {
            e.printStackTrace();
        } finally {
            if (canvas!= null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void calculateSize() {
        markPadding = blockSize / 4;
        markSize = blockSize - markPadding * 2;
    }

    /////////////////////
    //      Public
    /////////////////////
    public void setBoard(Board board) {
        this.board = board;
        resetOffset();
        draw();
    }

    public void setLastMove(int x, int y) {
        lastMove.x = x;
        lastMove.y = y;
        draw();
    }

    public void setActivity(AbstractPlayActivity activity) {
        this.activity = activity;
    }

}
