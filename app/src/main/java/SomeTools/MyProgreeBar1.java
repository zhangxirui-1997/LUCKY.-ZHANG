package SomeTools;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.luckzhang.R;

public class MyProgreeBar1 extends RelativeLayout {
    private Context mContext;
    private View mView;
    private double aDouble=0;
    private int widthnow;

    public MyProgreeBar1(Context context) {
        this(context, null);
    }

    public MyProgreeBar1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgreeBar1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.myprogressbar1, this, true);
    }

    public void letuspaint(double aDouble){
        this.aDouble=aDouble;

        ImageView imageView=mView.findViewById(R.id.dsafknasdl);
        //imageView.layout(aDouble/100*(widthnow-10)+5,0,8,8);

        Double dd= (aDouble/100)*(widthnow-30);
        TranslateAnimation animation = new TranslateAnimation(0,Float.parseFloat(dd.toString()),0,0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width     = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode= MeasureSpec.getMode(heightMeasureSpec);
        int height    = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            int groupWidth = getMaxWidth();
            int groupHeight= getTotalHeight();

            setMeasuredDimension(groupWidth, groupHeight);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(getMaxWidth(), height);
        }else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(width, getTotalHeight());
        }
        widthnow= MeasureSpec.getSize(widthMeasureSpec);
        letuspaint(aDouble);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(aDouble/30*(widthnow-10)+5,0,5,paint);*/

    }

    //测量他的宽
    private int getMaxWidth(){
        int count = getChildCount();
        int maxWidth = 0;
        for (int i = 0 ; i < count ; i ++){
            int currentWidth = getChildAt(i).getMeasuredWidth();
            if (maxWidth < currentWidth){
                maxWidth = currentWidth;
            }
        }
        return maxWidth;
    }
    //测量他的高
    private int getTotalHeight(){
        int count = getChildCount();
        int totalHeight = 0;
        for (int i = 0 ; i < count ; i++){
            totalHeight += getChildAt(i).getMeasuredHeight();
        }
        return totalHeight;
    }

}
