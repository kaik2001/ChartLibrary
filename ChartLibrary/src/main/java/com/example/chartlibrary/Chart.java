package com.example.chartlibrary;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class Chart extends View {
    protected Paint backgroundPaint = new Paint();
    protected Paint onBackgroundPaint = new Paint();

    protected Context context;

    protected Canvas canvas;

    protected float dp2px = 1.0f;

    private Vector2 contentStart;
    private Vector2 contentSize;

    protected Vector2 canvasSize = new Vector2(1.45434, 2.5462);
    protected Vector2 canvasSizePadding = new Vector2(2, 2);
    protected Vector2 canvasOrigin = canvasSize.multiply(-0.5);

    public Chart(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Chart(Context context, AttributeSet attributeSet) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        dp2px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1.0f,
                getResources().getDisplayMetrics());

        backgroundPaint.setColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background, context.getTheme()));
        onBackgroundPaint.setColor(getResources().getColor(com.google.android.material.R.color.design_default_color_on_background, context.getTheme()));

        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
            case Configuration.UI_MODE_NIGHT_YES:
                    backgroundPaint.setColor(getResources().getColor(com.google.android.material.R.color.design_dark_default_color_background, context.getTheme()));
                    onBackgroundPaint.setColor(getResources().getColor(android.R.color.darker_gray, context.getTheme()));
                break;
        }
        backgroundPaint.setStrokeWidth(dp2px * 2);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setTextSize(dp2px * 16);

        onBackgroundPaint.setStrokeWidth(dp2px * 2);
        onBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        onBackgroundPaint.setTextSize(dp2px * 16);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        if(isInEditMode()){
            canvas.drawColor(backgroundPaint.getColor());
        }

        int paddingLeft = getPaddingLeft() + (int)(dp2px * canvasSizePadding.x);
        int paddingTop = getPaddingTop() + (int)(dp2px * canvasSizePadding.y);
        int paddingRight = getPaddingRight() + (int)(dp2px * canvasSizePadding.x);
        int paddingBottom = getPaddingBottom() + (int)(dp2px * canvasSizePadding.y);

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        contentStart = new Vector2(paddingLeft, paddingTop);
        contentSize = new Vector2(contentWidth, contentHeight);

/*
        drawText("10.05.2035", Alignment.TopLeft, new Vector2(0, 1).multiply(canvasSize).add(canvasOrigin), new Point(2, -2), onBackgroundPaint);
        drawText("10.05.2035", Alignment.TopCenter, new Vector2(0.5, 1).multiply(canvasSize).add(canvasOrigin), new Point(0, -2), onBackgroundPaint);
        drawText("10.05.2035", Alignment.TopRight, new Vector2(1, 1).multiply(canvasSize).add(canvasOrigin), new Point(-2, -2), onBackgroundPaint);
        drawText("10.05.2035", Alignment.CenterLeft, new Vector2(0, 0.5).multiply(canvasSize).add(canvasOrigin), new Point(2, 0), onBackgroundPaint);
        drawText("10.05.2035", Alignment.CenterCenter, new Vector2(0.5, 0.5).multiply(canvasSize).add(canvasOrigin), new Point(0, 0), onBackgroundPaint);
        drawText("10.05.2035", Alignment.CenterRight, new Vector2(1, 0.5).multiply(canvasSize).add(canvasOrigin), new Point(-2, 0), onBackgroundPaint);
        drawText("10.05.2035", Alignment.BottomLeft, new Vector2(0, 0).multiply(canvasSize).add(canvasOrigin), new Point(2, 2), onBackgroundPaint);
        drawText("10.05.2035", Alignment.BottomCenter, new Vector2(0.5, 0).multiply(canvasSize).add(canvasOrigin), new Point(0, 2), onBackgroundPaint);
        drawText("10.05.2035", Alignment.BottomRight, new Vector2(1, 0).multiply(canvasSize).add(canvasOrigin), new Point(-2, 2), onBackgroundPaint);
 */
    }

    protected enum Alignment {
        TopLeft,
        CenterLeft,
        BottomLeft,
        TopCenter,
        CenterCenter,
        BottomCenter,
        TopRight,
        CenterRight,
        BottomRight
    }

    protected void drawText(String text, Alignment alignment, Vector2 position, Point constantOffset, Paint paint) {
        if (canvas == null)
            return;
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        Vector2 textSize = new Vector2(bounds.width() * canvasSize.x / contentSize.x, bounds.height() * canvasSize.y / contentSize.y);
        Point textPosition = new Point();
        switch (alignment) {
            case TopLeft:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(0, -1))));
                break;
            case CenterLeft:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(0, -0.5))));
                break;
            case BottomLeft:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(0, 0))));
                break;
            case TopCenter:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(-0.5, -1))));
                break;
            case CenterCenter:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(-0.5, -0.5))));
                break;
            case BottomCenter:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(-0.5, 0))));
                break;
            case TopRight:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(-1, -1))));
                break;
            case CenterRight:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(-1, -0.5))));
                break;
            case BottomRight:
                textPosition = transformPoint(position.add(textSize.multiply(new Vector2(-1, 0))));
                break;
        }
        textPosition.x += constantOffset.x * dp2px;
        textPosition.y -= constantOffset.y * dp2px;
        canvas.drawText(text, textPosition.x, textPosition.y, paint);
    }

    protected void drawIcon(int icon, float dp, Alignment alignment, Vector2 position, Point constantOffset, Paint paint){
        if (canvas == null)
            return;

        Drawable drawable = getResources().getDrawable(icon, getContext().getTheme());
        drawable.setColorFilter(paint.getColor(), PorterDuff.Mode.SRC_ATOP);
        Rect bounds = new Rect();
        Point pos = transformPoint(position);
        pos.x += constantOffset.x * dp2px;
        pos.y -= constantOffset.y * dp2px;
        int dim = (int)(dp2px * dp);
        switch (alignment) {
            case TopLeft:
                bounds = new Rect(pos.x, pos.y, pos.x + dim, pos.y + dim);
                break;
            case CenterLeft:
                bounds = new Rect(pos.x, pos.y - dim / 2, pos.x + dim, pos.y + dim / 2);
                break;
            case BottomLeft:
                bounds = new Rect(pos.x, pos.y - dim, pos.x + dim, pos.y);
                break;
            case TopCenter:
                bounds = new Rect(pos.x - dim / 2, pos.y, pos.x + dim / 2, pos.y + dim);
                break;
            case CenterCenter:
                bounds = new Rect(pos.x - dim / 2, pos.y - dim / 2, pos.x + dim / 2, pos.y + dim / 2);
                break;
            case BottomCenter:
                bounds = new Rect(pos.x - dim / 2, pos.y - dim, pos.x + dim / 2, pos.y);
                break;
            case TopRight:
                bounds = new Rect(pos.x - dim, pos.y, pos.x, pos.y + dim);
                break;
            case CenterRight:
                bounds = new Rect(pos.x - dim, pos.y - dim / 2, pos.x, pos.y + dim / 2);
                break;
            case BottomRight:
                bounds = new Rect(pos.x - dim, pos.y - dim, pos.x, pos.y);
                break;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
    }

    protected void drawLine(Vector2 start, Vector2 end, Paint paint){
        if (canvas == null)
            return;
        Point from = transformPoint(start);
        Point to = transformPoint(end);
        canvas.drawLine(from.x, from.y, to.x, to.y, paint);
    }

    protected Point transformPoint(Vector2 point){
        return contentStart.add(contentSize.multiply(new Vector2(0, 1).add(point.subtract(canvasOrigin).divide(canvasSize).conjugate()))).toPoint();
    }

    public class Vector2 {
        double x, y;

        public Vector2(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point toPoint() {
            return new Point((int) x, (int) y);
        }

        public Vector2 add(Vector2 v) {
            return new Vector2(x + v.x, y + v.y);
        }

        public Vector2 subtract(Vector2 v) {
            return new Vector2(x - v.x, y - v.y);
        }

        public Vector2 multiply(double d) {
            return new Vector2(x * d, y * d);
        }

        public Vector2 divide(double d) {
            return new Vector2(x / d, y / d);
        }

        public Vector2 multiply(Vector2 v) {
            return new Vector2(x * v.x, y * v.y);
        }

        public Vector2 divide(Vector2 v) {
            return new Vector2(x / v.x, y / v.y);
        }

        public double dot(Vector2 v) {
            return x * v.x + y * v.y;
        }

        public double length() {
            return Math.sqrt(dot(this));
        }

        public Vector2 conjugate(){
            return new Vector2(x, -y);
        }
    }
}