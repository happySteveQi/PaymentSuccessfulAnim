package com.steve.paysuccessanim.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.steve.paysuccessanim.R;

public class CTickView extends View {
    private int mWidth;
    private int mHeight;

    private float mFactor; // Factor: 0-1
    private float mScaleAX = 0.2659f;
    private float mScaleAY = 0.4588f;
    private float mScaleBX = 0.4541f;
    private float mScaleBY = 0.6306f;
    private float mScaleCX = 0.7553f;
    private float mScaleCY = 0.3388f;

    private int mColor;
    private int mColorCircle;
    private float mStrokeWidth;
    private Path mPath;
    private Path mPathTick;
    private Path mPathTick2;
    private Paint mPaintTick;
    private Paint mPaintCircle;
    private PathMeasure mTickPathMeasure;
    private ValueAnimator mStrokeAnimation;

    public CTickView(Context context) {
        this(context, null);
    }

    public CTickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CTickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CTickView);
        mColor = typedArray.getColor(R.styleable.CTickView_ctv_color, Color.parseColor("#ffffff"));
        mColorCircle = typedArray.getColor(R.styleable.CTickView_ctv_colorCircle, Color.parseColor("#47b018"));
        mStrokeWidth = typedArray.getDimension(R.styleable.CTickView_ctv_strokeWidth, 5);
        typedArray.recycle();
        init();
    }

    public void init() {
        mPath = new Path();
        mPathTick = new Path();
        mPathTick2 = new Path();

        mTickPathMeasure = new PathMeasure();

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(mColorCircle);

        mPaintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTick.setColor(mColor);
        mPaintTick.setStyle(Paint.Style.STROKE);
        mPaintTick.setStrokeCap(Paint.Cap.ROUND);
        mPaintTick.setStrokeJoin(Paint.Join.ROUND);//设置线段连接处的样式为圆弧
        mPaintTick.setStrokeWidth(mStrokeWidth);

        mStrokeAnimation = ValueAnimator.ofFloat(0f, 1f);
        mStrokeAnimation.setDuration(250);
        mStrokeAnimation.setInterpolator(new LinearInterpolator());
        mStrokeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFactor = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPathTick.moveTo(mWidth * mScaleAX, mHeight * mScaleAY);
        mPathTick.lineTo(mWidth * mScaleBX, mHeight * mScaleBY);
        mPathTick.lineTo(mWidth * mScaleCX, mHeight * mScaleCY);
        mTickPathMeasure.setPath(mPathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        mTickPathMeasure.getSegment(0, mFactor * mTickPathMeasure.getLength(), mPath, true);
        mPath.rLineTo(0, 0);
        canvas.drawCircle(mWidth / 2f, mWidth / 2f, mWidth / 2f, mPaintCircle);
        canvas.drawPath(mPath, mPaintTick);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight =MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * Start animation
     */
    public void start() {
        stop();
        mPath = new Path();
        if (mStrokeAnimation != null) {
            mStrokeAnimation.start();
        }
    }

    /**
     * Stop animation
     */
    public void stop() {
        if (mStrokeAnimation != null) {
            mStrokeAnimation.end();
        }
    }
}