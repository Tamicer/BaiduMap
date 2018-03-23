package com.tamic.map.mapdemo.map.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.pinganfang.haofangtuo.R;

/**
 * 地图Marker自定View
 */
public class MapMarker extends View {

    /**
     * 默认padding值
     */
    private static final int DEFAULT_PADDING = 20;
    /**
     * 默认下三角大小
     */
    private static final int DEFAULT_SHARP_CORNER_SIZE = 30;
    /**
     * 默认文字颜色
     */
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    /**
     * 默认文字大小
     */
    private static final int DEFAULT_TEXT_SIZE = 60;
    private static final int DEFAULT_COUNT_BACKGROUND_COLOR = 0x550000FF;
    private static final int DEFAULT_COLOR_BORDER_COLOR = Color.RED;
    private static final int DEFAULT_MARKER_BACKGROUND_COLOR = 0xAAFCB840;
    private static final int DEFAULT_MARKER_BORDER_COLOR = Color.RED;

    private String mCount;
    private String mMarkerName;
    private float mDensity;
    /**
     * 圆圈、marker文字大小
     */
    private int mCountTextSize = DEFAULT_TEXT_SIZE;
    private int mMarkerTextSize = DEFAULT_TEXT_SIZE;
    /**
     * 圆圈、marker背景色
     */
    private int mCountBackgroundColor = DEFAULT_COUNT_BACKGROUND_COLOR;
    private int mMarkerBackgroundColor = DEFAULT_MARKER_BACKGROUND_COLOR;
    /**
     * 圆圈、marker文字颜色
     */
    private int mCountTextColor = DEFAULT_TEXT_COLOR;
    private int mMarkerTextColor = DEFAULT_TEXT_COLOR;
    /**
     * 圆圈、marker内边距值
     */
    private float mCountPadding = DEFAULT_PADDING;
    private float mMarkerPadding = DEFAULT_PADDING;
    /**
     * 圆圈、marker边框颜色
     */
    private int mCountBorderColor = DEFAULT_COLOR_BORDER_COLOR;
    private int mMarkerBorderColor = DEFAULT_MARKER_BORDER_COLOR;
    /**
     * 圆圈、marker边框宽度
     */
    private float mCountBorderWidth;
    private float mMarkerBorderWidth;
    private float mSpacing = DEFAULT_PADDING;
    private boolean isMarkerVisible = true;
    private boolean isCircleVisible = true;
    private float mCountRadius;
    private boolean hasSharpCorner = false;
    private float mShareCornerSize = DEFAULT_SHARP_CORNER_SIZE;
    private DrawFilter mDefaultDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private final Paint mCountPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 对称轴X坐标
     */
    private float mCenterX;
    /**
     * marker文字宽高
     */
    private float mMarkerFontWidth, mMarkerFontHeight;
    /**
     * marker矩形宽高
     */
    private float mMarkerWidth, mMarkerHeight;
    /**
     * 圆圈内文字宽高
     */
    private float mCountFontWidth, mCountFontHeight;
    private boolean isCanvasSaved = false;

    public MapMarker(Context context) {
        super(context);
        init(context, null);
    }

    public MapMarker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MapMarker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MapMarker, 0, 0);
            mCountPadding = a.getDimensionPixelSize(R.styleable.MapMarker_countPadding, DEFAULT_PADDING);
            mMarkerPadding = a.getDimensionPixelSize(R.styleable.MapMarker_markerPadding, DEFAULT_PADDING);
            mCountBackgroundColor = a.getColor(R.styleable.MapMarker_countBackground, DEFAULT_COUNT_BACKGROUND_COLOR);
            mMarkerBackgroundColor = a.getColor(R.styleable.MapMarker_markerBackground, DEFAULT_MARKER_BACKGROUND_COLOR);
            mCountTextSize = a.getDimensionPixelSize(R.styleable.MapMarker_countTextSize, DEFAULT_TEXT_SIZE);
            mMarkerTextSize = a.getDimensionPixelSize(R.styleable.MapMarker_markerTextSize, DEFAULT_TEXT_SIZE);
            mCountTextColor = a.getColor(R.styleable.MapMarker_countTextColor, DEFAULT_TEXT_COLOR);
            mMarkerTextColor = a.getColor(R.styleable.MapMarker_markerTextColor, DEFAULT_TEXT_COLOR);
            mSpacing = a.getDimensionPixelSize(R.styleable.MapMarker_intervalSpacing, DEFAULT_PADDING);
            mCountBorderColor = a.getColor(R.styleable.MapMarker_countBorderColor, 0);
            mMarkerBorderColor = a.getColor(R.styleable.MapMarker_markerBorderColor, 0);
            mCountBorderWidth = a.getDimensionPixelSize(R.styleable.MapMarker_countBorderWidth, 0);
            mMarkerBorderWidth = a.getDimensionPixelSize(R.styleable.MapMarker_markerBorderWidth, 0);
            a.recycle();
        }
        mDensity = context.getResources().getDisplayMetrics().density;

        mCountPaint.setAntiAlias(true);
        mMarkerPaint.setAntiAlias(true);

    }

    /**
     * 设置上部分圆圈内数字
     */
    public void setCount(String count) {
        this.mCount = count;
    }

    /**
     * 设置标记内文字
     */
    public void setMarkerName(String name) {
        this.mMarkerName = name;
    }

    /**
     * 设置Marker背景色
     */
    public void setMarkerBackgroundColor(int color) {
        this.mMarkerBackgroundColor = color;
    }

    /**
     * 设置圆圈的背景色
     *
     * @param color
     */
    public void setCountBackgroundColor(int color) {
        this.mCountBackgroundColor = color;
    }

    /**
     * 设置圆圈内边距
     */
    public void setCountPadding(float padding) {
        this.mCountPadding = padding;
    }

    /**
     * 设置Marker内边距
     *
     * @param padding
     */
    public void setMarkerPadding(float padding) {
        this.mMarkerPadding = padding;
    }

    /**
     * 设置圆圈内文字大小
     */
    public void setCountTextSize(int size) {
        this.mCountTextSize = size;
    }

    /**
     * 设置Marker内文字大小
     */
    public void setMarkerTextSize(int size) {
        this.mMarkerTextSize = size;
    }

    /**
     * 设置圆圈内文字的颜色
     */
    public void setCountTextColor(int color) {
        this.mCountTextColor = color;
    }

    /**
     * 设置Marker内文字的颜色
     */
    public void setMarkerTextColor(int color) {
        this.mMarkerTextColor = color;
    }

    /**
     * 设置数字圆圈的半径
     */
    public void setCountRadius(float radius) {
        this.mCountRadius = radius;
    }

    /**
     * 设置marker name是否显示
     */
    public void setMarkerNameVisible(boolean isVisible) {
        this.isMarkerVisible = isVisible;
    }

    /**
     * 设置marker name是否显示
     *
     * @param isVisible
     */
    public void setCircleVisible(boolean isVisible) {
        this.isCircleVisible = isVisible;
    }

    /**
     * 设置圆圈边框颜色
     *
     * @param color
     */
    public void setCountBorderColor(int color) {
        this.mCountBorderColor = color;
    }

    /**
     * 设置marker边框颜色
     *
     * @param color
     */
    public void setMarkerBorderColor(int color) {
        this.mMarkerBorderColor = color;
    }

    /**
     * 设置圆圈边框宽度
     *
     * @param width
     */
    public void setCountBorderWidth(float width) {
        this.mCountBorderWidth = width;
    }

    /**
     * 设置marker边框宽度
     *
     * @param width
     */
    public void setMarkerBorderWidth(float width) {
        this.mMarkerBorderWidth = width;
    }

    /**
     * 设置是否有下三角
     *
     * @param has
     */
    public void setSharpCorner(boolean has) {
        this.hasSharpCorner = has;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mCount) && isCircleVisible) {
            canvas.setDrawFilter(mDefaultDrawFilter);

            // 圆心x的位置
            float dx = mCenterX;
            // 圆心y的位置
            float dy = mCountRadius;
            // 画圆边框
            if (mCountBorderColor != 0 && mCountBorderWidth != 0) {
                mCountPaint.setColor(mCountBorderColor);
                mCountPaint.setStyle(Paint.Style.STROKE);
                mCountPaint.setStrokeWidth(mCountBorderWidth);
                canvas.drawCircle(dx, dy, mCountRadius - 1, mCountPaint);
            }

            // 设置圆的颜色
            mCountPaint.setColor(mCountBackgroundColor);
            mCountPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(dx, dy, mCountRadius - mCountBorderWidth, mCountPaint);

            // --------- draw text in circle -----------
            mCountPaint.setColor(mCountTextColor);
            canvas.drawText(mCount, dx - mCountFontWidth / 2, dy - mCountFontHeight / 2 + getFontLeading(mCountPaint), mCountPaint);
        }
        if (!TextUtils.isEmpty(mMarkerName) && isMarkerVisible) {
            // --------- draw marker -----------
            if (mMarkerBorderColor != 0 && mMarkerBorderWidth != 0) {
                mMarkerPaint.setColor(mMarkerBorderColor);
                mMarkerPaint.setStyle(Paint.Style.STROKE);
                mMarkerPaint.setStrokeWidth(mMarkerBorderWidth);
            }

            float left = mCenterX - mMarkerWidth / 2;
            float top = mCountRadius * 2 + mSpacing;
            float right = left + mMarkerWidth;
            float bottom = top + mMarkerHeight;

            if (hasSharpCorner) {
                if (mMarkerBorderWidth != 0) {
                    drawRectWithSharpCorner(canvas, mMarkerPaint, left, top, mMarkerWidth, mMarkerHeight, mShareCornerSize);
                }
                mMarkerPaint.setColor(mMarkerBackgroundColor);
                mMarkerPaint.setStyle(Paint.Style.FILL);
                drawRectWithSharpCorner(canvas, mMarkerPaint, left + mMarkerBorderWidth - 1, top + mMarkerBorderWidth - 1, mMarkerWidth - mMarkerBorderWidth + 1, mMarkerHeight - mMarkerBorderWidth + 1, mShareCornerSize);
            } else {
                // 画矩形边框
                if (mMarkerBorderColor != 0 && mMarkerBorderWidth != 0) {
                    canvas.drawRect(left, top, right, bottom, mMarkerPaint);
                }
                // 设置marker背景色
                mMarkerPaint.setColor(mMarkerBackgroundColor);
                mMarkerPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(left + mMarkerBorderWidth - 1, top + mMarkerBorderWidth - 1, right - mMarkerBorderWidth + 1, bottom - mMarkerBorderWidth + 1, mMarkerPaint);
            }

            // --------- draw text in marker -----------
            mMarkerPaint.setColor(mMarkerTextColor);
            canvas.drawText(mMarkerName, mMarkerPadding + left, top + mMarkerPadding + getFontLeading(mMarkerPaint), mMarkerPaint);
        }
    }

    private void drawRectWithSharpCorner(Canvas canvas, Paint paint, float left, float top, float width, float height, float cornerSize) {
        float right = width;
        float bottom = top + height;

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(right - width / 2 + cornerSize / 2, bottom);
        path.lineTo(right - width / 2, bottom + cornerSize / 2);
        path.lineTo(right - width / 2 - cornerSize / 2, bottom);
        path.lineTo(left, bottom);
        path.lineTo(left, top);
        canvas.drawPath(path, paint);
    }

    /**
     * 获取字体高度
     */
    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * 获取字体Baseline高度
     */
    private float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    /**
     * 计算字体宽度
     *
     * @param paint    画笔
     * @param textSize 字体大小
     * @param text     文字
     */
    private float calculateFontWidth(Paint paint, int textSize, String text) {
        float width = 0;
        if (!TextUtils.isEmpty(text)) {
            paint.setTextSize(textSize);
            width = paint.measureText(text);
        }
        return width;
    }

    /**
     * 计算文字高度
     *
     * @param paint    画笔
     * @param textSize 字体大小
     * @param text     文字
     */
    private float calculateFontHeight(Paint paint, int textSize, String text) {
        float height = 0;
        if (!TextUtils.isEmpty(text)) {
            paint.setTextSize(textSize);
            height = getFontHeight(paint);
        }
        return height;
    }

    /**
     * 测量宽度
     *
     */
    private float measureWidth() {
        // 计算marker文字宽及矩形宽
        mMarkerFontWidth = calculateFontWidth(mMarkerPaint, mMarkerTextSize, mMarkerName);
        mMarkerFontHeight = calculateFontHeight(mMarkerPaint, mMarkerTextSize, mMarkerName);
        mMarkerHeight = mMarkerFontHeight + mMarkerPadding * 2;
        mMarkerWidth = mMarkerFontWidth + mMarkerPadding * 2;

        // 计算圆圈内文字宽高
        mCountFontWidth = calculateFontWidth(mCountPaint, mCountTextSize, mCount);
        mCountFontHeight = calculateFontHeight(mCountPaint, mCountTextSize, mCount);

        // 计算圆圈半径
        if (mCountRadius == 0) {
            mCountRadius = Math.max(mCountFontWidth, mCountFontHeight) / 2;
        }
        mCountRadius += mCountPadding;
        return Math.max(mCountRadius * 2, mMarkerWidth);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int widthMeasured = 0;
        int width = (int) measureWidth();  //根据控件的内容计算控件宽度值
        // 对称轴位置
        mCenterX = width / 2;

        switch (specMode) {
            case MeasureSpec.EXACTLY: //已经有精确值了，直接用widthSize
                widthMeasured = specSize;
                break;
            case MeasureSpec.AT_MOST: //根据提供的最大值，计算合适的size
                widthMeasured = Math.min(specSize, width);
                break;
            case MeasureSpec.UNSPECIFIED:  //不知道具体的size
                widthMeasured = width;  //用自己测量的size作为测量值
                break;
        }

        return widthMeasured;
    }

    /**
     * 测量高度
     */
    private int measureHeight() {
        return (int) (mCountRadius * 2 + mSpacing + mMarkerHeight);
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int heightMeasured = 0;
        int height = measureHeight();  //根据控件的内容计算控件宽度值
        switch (specMode) {
            case MeasureSpec.EXACTLY: //已经有精确值了，直接用heightSize
                heightMeasured = specSize;
                break;
            case MeasureSpec.AT_MOST: //根据提供的最大值，计算合适的size
                heightMeasured = Math.min(specSize, height);
                break;
            case MeasureSpec.UNSPECIFIED:  //不知道具体的size
                heightMeasured = height;  //用自己测量的size作为测量值
                break;
        }

        return heightMeasured;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

}
