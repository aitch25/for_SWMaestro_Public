package com.yang.bebe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yang.bebe.widget.waterwave.WaterWave;
import com.yang.bebe.widget.waterwave.FeedingView;

import static com.yang.bebe.R.styleable.WaterWave;

/**
 * Created by naman on 28/08/15.
 */
public class FeedingBottleView extends LinearLayout {

    //linear layout consisting of three views-topview,machineview and bottomview

    private int mTopviewHeight;
    private int mMiddleviewHeight;
    private int mBottomviewHeight;
    private int mMachineColor;

    public FeedingBottleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FeedingBottleView);
        mMachineColor=attributes.getColor(R.styleable.FeedingBottleView_machineColor,Color.BLACK);
        attributes.recycle();

        setOrientation(VERTICAL);

       /* Bitmap  bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.feeding);
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(tempBitmap);

        canvas.drawBitmap(bitmap2, 0, 0, null);

        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(0xFFFF0000);
        canvas.drawText("hhhhhhhhhhhhhhhhhhi", 300, 300, paint);

     //   image.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
*/

        //add the views
     //   addView(new TopView(context, attrs));
        addView(new FeedingBottle(context, attrs));
       // addView(new BottomView(context, attrs));


    }

    //machineview(middle view) extends FrameLayout and has two views-WaterWave and Overlay.
    //Overlay consists of a transparent hole from which water waves are visible

    public class FeedingBottle extends FrameLayout {

        public FeedingBottle(Context context, AttributeSet attrs) {
            super(context, attrs);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mMiddleviewHeight);
            setLayoutParams(params);

            addView(new WaterWave(context, attrs));

            addView(new FeedingView(context, attrs));
           // addView(new OverlayView(context, attrs));
        }


    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
