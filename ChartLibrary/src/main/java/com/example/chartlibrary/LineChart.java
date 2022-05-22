package com.example.chartlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import com.example.chartlibrary.Chart.*;

public class LineChart extends View {

    public ArrayList<Line> lines = new ArrayList<>();
    public ArrayList<Paint> defaultLinePaints = new ArrayList<>();

    private Chart base;

    private Vector2 min;
    private Vector2 max;

    private String minXDescription = "";
    private String maxXDescription = "";
    private String minYDescription = "";
    private String maxYDescription = "";

    public LineChart(Context context) {
        super(context);
        base = new Chart(context);
        init();
    }

    public LineChart(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        base = new Chart(context);
        init();
    }

    public void init() {
        base.canvasSizePadding = new Vector2(2, 2 + 24);

        for (int i = 0; i < 5; i++) {
            Paint paint = new Paint();
            switch (i) {
                case 0:
                    paint.setColor(getResources().getColor(android.R.color.holo_blue_dark, base.context.getTheme()));
                    break;
                case 1:
                    paint.setColor(getResources().getColor(android.R.color.holo_green_dark, base.context.getTheme()));
                    break;
                case 2:
                    paint.setColor(getResources().getColor(android.R.color.holo_orange_dark, base.context.getTheme()));
                    break;
                case 3:
                    paint.setColor(getResources().getColor(android.R.color.holo_purple, base.context.getTheme()));
                    break;
                case 4:
                    paint.setColor(getResources().getColor(android.R.color.holo_red_dark, base.context.getTheme()));
                    break;
            }
            paint.setStrokeWidth(base.dp2px * 2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setTextSize(base.dp2px * 16);
            defaultLinePaints.add(paint);
        }

        //if(isInEditMode()){
        for (int i = 0; i < 3; i++) {
            Line line = new Line();

            line.points.add(new LinePoint(new Vector2(-0.5, new Random().nextDouble())).setXDescription("05.22"));
            line.points.add(new LinePoint(new Vector2(-0.25, new Random().nextDouble()), R.drawable.ic_baseline_android_24));
            line.points.add(new LinePoint(new Vector2(0, new Random().nextDouble()), R.drawable.ic_baseline_light_mode_24));
            line.points.add(new LinePoint(new Vector2(0.25, new Random().nextDouble()), R.drawable.ic_baseline_bedtime_24));
            line.points.add(new LinePoint(new Vector2(0.5, new Random().nextDouble())).setXDescription("06.22"));

            Vector2 random = new Vector2(new Random().nextDouble(), new Random().nextDouble());
            for (LinePoint point : line.points) {
                point.position = point.position.add(random);
            }

            lines.add(line);
        }
        //}
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        base.onDraw(canvas, this);

        calculateCanvasSize();

        int i = 0;
        for (Line line : lines){
            line.draw(i);
            i++;
        }
        drawAxis();
    }

    private void calculateCanvasSize(){
        min = new Vector2(Double.MAX_VALUE, Double.MAX_VALUE);
        max = new Vector2(-Double.MAX_VALUE, -Double.MAX_VALUE);

        for (Line line : lines){
            for (LinePoint linePoint : line.points){
                if(linePoint.position.x < min.x){
                    minXDescription = linePoint.xDescription;
                    min.x = linePoint.position.x;
                }
                if(linePoint.position.x > max.x){
                    maxXDescription = linePoint.xDescription;
                    max.x = linePoint.position.x;
                }
                if(linePoint.position.y < min.y){
                    minYDescription = linePoint.yDescription;
                    min.y = linePoint.position.y;
                }
                if(linePoint.position.y > max.y){
                    maxYDescription = linePoint.yDescription;
                    max.y = linePoint.position.y;
                }
            }
        }
        base.canvasOrigin = min;
        base.canvasSize = max.subtract(min);
    }

    private void drawAxis(){
        Vector2 xStart = new Vector2(min.x, 0);
        Vector2 xEnd = new Vector2(max.x, 0);

        Vector2 yStart = new Vector2(0, min.y);
        Vector2 yEnd = new Vector2(0, max.y);

        if(min.x > 0) {
            yStart.x = min.x;
            yEnd.x = min.x;
        }
        if(max.x < 0) {
            yStart.x = max.x;
            yEnd.x = max.x;
        }
        if(min.y > 0) {
            xStart.y = min.y;
            xEnd.y = min.y;
        }
        if(max.y < 0) {
            xStart.y = max.y;
            xEnd.y = max.y;
        }

        base.drawLine(xStart, xEnd, base.onBackgroundPaint);
        base.drawLine(yStart, yEnd, base.onBackgroundPaint);

        if(max.y < 0){
            base.drawText(minXDescription != "" ? minXDescription : new DecimalFormat("0.00").format(min.x), Alignment.BottomLeft, xStart, new Point(4, 4), base.onBackgroundPaint);
            base.drawText(maxXDescription != "" ? maxXDescription : new DecimalFormat("0.00").format(max.x), Alignment.BottomRight, xEnd, new Point(-4, 4), base.onBackgroundPaint);
        }
        else {
            base.drawText(minXDescription != "" ? minXDescription : new DecimalFormat("0.00").format(min.x), Alignment.TopLeft, xStart, new Point(4, -4), base.onBackgroundPaint);
            base.drawText(maxXDescription != "" ? maxXDescription : new DecimalFormat("0.00").format(max.x), Alignment.TopRight, xEnd, new Point(-4, -4), base.onBackgroundPaint);
        }
        if(max.x < 0) {
            base.drawText(minYDescription != "" ? minYDescription : new DecimalFormat("0.00").format(min.y), Alignment.BottomRight, yStart, new Point(-4, 4), base.onBackgroundPaint);
            base.drawText(maxYDescription != "" ? maxYDescription : new DecimalFormat("0.00").format(max.y), Alignment.TopRight, yEnd, new Point(-4, -4), base.onBackgroundPaint);
        }
        else {
            base.drawText(minYDescription != "" ? minYDescription : new DecimalFormat("0.00").format(min.y), Alignment.BottomLeft, yStart, new Point(4, 4), base.onBackgroundPaint);
            base.drawText(maxYDescription != "" ? maxYDescription : new DecimalFormat("0.00").format(max.y), Alignment.TopLeft, yEnd, new Point(4, -4), base.onBackgroundPaint);
        }
    }

    public class Line {
        ArrayList<LinePoint> points = new ArrayList<>();
        public Paint paint;

        public void draw(int index) {
            if(paint == null)
                paint = defaultLinePaints.get(index % 5);
            for (int i = 1; i < points.size() - 1; i++){
                points.get(i - 1).drawSegment(points.get(i), paint);
                points.get(i).drawPoint(points.get(i - 1), points.get(i + 1), paint);
            }
            points.get(points.size() - 1).drawSegment(points.get(points.size() - 2), paint);
        }
    }

    public enum PointIconAlignment {
        Auto,
        Center,
        Above,
        Below
    }

    public class LinePoint {
        public Vector2 position;
        public int icon = -1;
        public PointIconAlignment alignment = PointIconAlignment.Auto;

        public String xDescription = "";
        public String yDescription = "";


        public LinePoint(Vector2 position){
            this.position = position;
        }

        public LinePoint(Vector2 position, int icon){
            this.position = position;
            this.icon = icon;
            this.alignment = alignment;
        }

        public LinePoint(Vector2 position, int icon, PointIconAlignment alignment){
            this.position = position;
            this.icon = icon;
            this.alignment = alignment;
        }

        public LinePoint setXDescription(String description){
            this.xDescription = description;
            return this;
        }

        public LinePoint setYDescription(String description){
            this.yDescription = description;
            return this;
        }

        public void drawPoint(LinePoint before, LinePoint after, Paint paint) {
            if(icon == -1)
                return;
            Point offset = new Point(0, 0);
            Alignment pointAlignment = Alignment.CenterCenter;
            switch (alignment) {
                case Auto:
                    double m_before = (position.y - before.position.y) / (position.x - before.position.x);
                    double m_after = (after.position.y - position.y) / (after.position.x - position.x);
                    if (m_after > m_before) {
                        // Knick nach oben
                        offset = new Point(0, -4);
                        pointAlignment = Alignment.TopCenter;
                    } else if (m_before > m_after) {
                        // Knick nach unten
                        offset = new Point(0, 4);
                        pointAlignment = Alignment.BottomCenter;
                    } else {
                        // kein Knick
                        offset = new Point(0, -4);
                        pointAlignment = Alignment.TopCenter;
                    }
                    break;
                case Center:
                    break;
                case Above:
                    offset = new Point(0, 4);
                    pointAlignment = Alignment.BottomCenter;
                    break;
                case Below:
                    offset = new Point(0, -4);
                    pointAlignment = Alignment.TopCenter;
                    break;
            }
            base.drawIcon(icon, base.dp2px * 8, pointAlignment, position, offset, paint);
        }

        public void drawSegment(LinePoint other, Paint paint) {
            base.drawLine(position, other.position, paint);
        }
    }
}