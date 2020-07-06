package com.steve.paysuccessanim.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

/**
 * author True Lies
 * 星星动画View
 * 三阶公式 B(T) = p0((1-t)(1-t)(1-t))+3P1t((1-t)(1-t))+3p2(t*t)(1-t)+p3(t*t*t)
 * 二阶公式 B(T) = (1-t)*(1-t)*P0+2t(1-t)P1+t*t*P2
 * 一阶公式 B(T) = P0+(P1-P0)*t=(1-t)P0+t*P1
 * 全屏View
 *
 */

public class FallStraightStarAnimView extends FrameLayout {

    private Context mContext = null;
    private int mMaxWidth = 0;
    private int mMaxHeight = 0;
    private Interpolator mLinearInterpolator = new LinearInterpolator();//线性
    private Interpolator mAccelerateInterpolator = new AccelerateInterpolator();//加速
    private Interpolator mDecelerateInterpolator = new DecelerateInterpolator();//减速
    private Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] mInterpolatorArray  = null;
    private Handler mHandler = new Handler();


    public FallStraightStarAnimView(@NonNull Context context) {
        this(context,null);
        this.mContext = context;
    }

    public FallStraightStarAnimView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        this.mContext = context;
    }

    public FallStraightStarAnimView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initMeasure();
        initView();
        startAnim();
    }


    /**
     * 初始化测量
     */
    private void initMeasure(){
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mMaxWidth = displayMetrics.widthPixels;
        mMaxHeight = displayMetrics.heightPixels;
    }

    /**
     * 初始化
     */
    private void initView(){
        this.mInterpolatorArray = new Interpolator[]{mLinearInterpolator,mAccelerateInterpolator};
//        this.mInterpolatorArray = new Interpolator[]{mLinearInterpolator,mAccelerateInterpolator,mDecelerateInterpolator,mAccelerateDecelerateInterpolator};
    }


    /**
     * 设置为和屏幕一样大
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMaxWidth,mMaxHeight);
    }

    /**
     * 开始动画
     */
    public void startAnim(){
        mHandler.post(starStartRunnable);
        mHandler.post(starStartRunnable);
    }

    /**
     * 开始动画
     */
    public void stopAnim(){
        mHandler.removeCallbacksAndMessages(null);
    }
    /**
     * 开始动画任务
     */
    private Runnable starStartRunnable = new Runnable() {
        @Override
        public void run() {
            AnomalyView anomalyView = new AnomalyView(mContext);

            fallAnim(anomalyView);
            mHandler.postDelayed(starStartRunnable,2);
        }
    };

    private void fallAnim(final View view){

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = new Random().nextInt(mMaxWidth-50);// 碎片最大50
        addView(view,layoutParams);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"translationY",-50f,mMaxHeight);
        objectAnimator.setInterpolator(mLinearInterpolator);
        objectAnimator.setTarget(view);
        objectAnimator.setDuration(1500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(view);
            }
        });
        objectAnimator.start();
    }


    /**
     * 不规则View
     */
    private class AnomalyView extends View {

        private Context mContext = null;
        private int mMaxWidth = 50;//最大宽度
        private int mMaxHeight = 50;//最大高度
        private int mPadding = 10;//内边距
        private Paint mPaint = null;
        private PointF[] mRandomPoint = null;//随机点  4个  顺序为  左上右下
        private String[] mLumpColorArray = null;

        public AnomalyView(Context context) {
            this(context,null);
            this.mContext = context;
        }

        public AnomalyView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs,0);
            this.mContext = context;
        }

        public AnomalyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.mContext = context;
            init();
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mMaxWidth,mMaxHeight);
        }

        /**
         * 初始化
         */
        private void init(){

            this.mLumpColorArray = new String[]{"#ef56c3","#fffd6d","#e7efff","#8c9ae4","#98e9f4","#7697ff"};

            this.mRandomPoint = new PointF[4];
            this.mRandomPoint[0] = new PointF(new Random().nextInt(mPadding),new Random().nextInt(mMaxHeight-mPadding)); //左
            this.mRandomPoint[1] = new PointF(new Random().nextInt(mMaxWidth-mPadding)+mPadding,new Random().nextInt(mPadding)); //上
            this.mRandomPoint[2] = new PointF(new Random().nextInt(mPadding)+(mMaxWidth-mPadding),new Random().nextInt(mMaxHeight-mPadding)+mPadding); //右
            this.mRandomPoint[3] = new PointF(new Random().nextInt(mMaxWidth)-mPadding,new Random().nextInt(mPadding)+(mMaxHeight-mPadding));//下

            this.mPaint = new Paint();
            this.mPaint.setDither(true);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(Color.parseColor(this.mLumpColorArray[new Random().nextInt(this.mLumpColorArray.length)]));
            this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        /**
         * 绘制
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Path path = new Path();
            path.moveTo(this.mRandomPoint[0].x,this.mRandomPoint[0].y);
            path.lineTo(this.mRandomPoint[1].x,this.mRandomPoint[1].y);
            path.lineTo(this.mRandomPoint[2].x,this.mRandomPoint[2].y);
            path.lineTo(this.mRandomPoint[3].x,this.mRandomPoint[3].y);
            path.close();
            canvas.drawPath(path,mPaint);
        }
    }

}
