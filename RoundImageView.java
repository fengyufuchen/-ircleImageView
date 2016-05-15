package com.sachin.zhihu.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.sachin.imitatezhihu.imitatezhihu.R;

import static com.sachin.imitatezhihu.imitatezhihu.R.styleable.RoundImageView_borderRadius;

/**
 * Created by lenovo on 2016/5/14.
 */
public class RoundImageView extends ImageView {
    private final String TAG = "RounImageView";
    //图片类型
    private int type;
    public  static final int TYPE_CIRCLE = 0;
    public  static final int TYPE_ROUND = 1;
    //圆角大小的默认值
    private static final int BORDER_RADIUS_DEFAULT = 10;
    private int mBorderRadius;//圆角的大小
    private Paint mBitmapPaint;//绘图的paint
    //圆角半径
    private int mRadius;

    private Matrix mMatrix;
    //渲染图像，使用图像为绘制图像着色
    private BitmapShader mBitmapShader;


    private int mWidth;
    private RectF mBoundRect;

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    public RoundImageView(Context context) {
        this(context,null);
        System.out.println(" RoundImageView(Context context)");

    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        System.out.println("RoundImageView(Context context, AttributeSet attrs)");
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
//第二个参数是将BODER_RADIUS_DEFAULT值转化为px，
        mBorderRadius = a.getDimensionPixelSize(RoundImageView_borderRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BORDER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        type = a.getInt(R.styleable.RoundImageView_round_type, TYPE_CIRCLE);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure method--type---" + type);


        //如果是圆形，则强制改变view的宽高一致，以小值为准
        if (type == TYPE_CIRCLE) {

            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            System.out.println("设置view的宽高：" + mWidth);
            setMeasuredDimension(mWidth, mWidth);
        }


    }


    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());//保存系统中本来需要保存的数据

        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));//恢复系统本来保存的数据
            this.type = (int) bundle.get(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);

        } else {
            super.onRestoreInstanceState(state);
        }

    }

    private void setUpShader() {

        Drawable drawable = getDrawable();


        if (drawable == null) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);

        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;

        if (type == TYPE_CIRCLE) {

            int bitMin = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = mWidth * 1.0f / bitMin;//根据view的大小mWidth来设置图片的缩放比

        } else if (type == TYPE_ROUND) {
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }

        mMatrix.setScale(scale, scale);

        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        if (getDrawable() == null) {
            return;
        }
        setUpShader();
        if (type == TYPE_CIRCLE) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mBoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
        }


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (type == TYPE_ROUND) {
            mBoundRect = new RectF(0, 0, w, h);

        }

    }

    private Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int drawHeight = drawable.getIntrinsicHeight();
        int drawWidht = drawable.getIntrinsicWidth();//本质的宽高

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(drawWidht, drawHeight, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawWidht, drawHeight);
        drawable.draw(canvas);
        return bitmap;
    }


    public void setBorderRadius(int borderRadius) {
        int pxValue = dp2px(borderRadius);
        if(this.mBorderRadius!=pxValue){
            this.mBorderRadius=pxValue;
            this.invalidate();
        }


    }
public   void setType(int type){
    if(this.type!=type){
        this.type=type;
        if(this.type!=TYPE_CIRCLE&&this.type!=TYPE_ROUND){
            this.type=TYPE_CIRCLE;

        }
        requestLayout();
    }


}
    private int dp2px(float dpvalue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpvalue, getResources().getDisplayMetrics());

    }

    public int getType(){
        return type;
    }
}
