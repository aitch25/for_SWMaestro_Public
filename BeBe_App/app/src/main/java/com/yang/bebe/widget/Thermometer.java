package com.yang.bebe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import com.yang.bebe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017-01-23.
 */

public class Thermometer extends View {
    TextView tv;


    public String myJSON;
    private static final String ARG_STR1 = "MIC";
    private static final String ARG_STR2 = "Key";
    // 아래에 JSON Object에서 추출해내고자 하는 이름 정보를 선언해줌
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_BPM = "bpm";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_MIC = "mic";
    private static final String TAG_HUMIDITY = "humidity";
    String id;
    String bpm;
    String tem;
    String hum;
    String mic;

    //thermometer circles paints
    private Paint mInnerCirclePaint;
    private Paint mOuterCirclePaint;
    private Paint mFirstOuterCirclePaint;

    //thermometer arc paint
    private Paint mFirstOuterArcPaint;


    //thermometer lines paints
    private Paint mInnerLinePaint;
    private Paint mOuterLinePaint;
    private Paint mFirstOuterLinePaint;


    //thermometer radii
    private int mOuterRadius;
    private int mInnerRadius;
    private int mFirstOuterRadius;


    //thermometer colors
    private int mThermometerColor = Color.rgb(201, 52, 107); //보라색

    //circles and lines  변수
    private float mLastCellWidth;
    private int mStageHeight;
    private float mCellWidth;
    private float mStartCenterY; //center of first cell
    private float mEndCenterY; //center of last cell
    private float mStageCenterX;
    private float mXOffset;
    private float mYOffset;
    private float mTemHeight;
    // I   1st Cell     I  2nd Cell       I  3rd Cell  I
    private static final int NUMBER_OF_CELLS = 3;

    //animation variables
    private float mIncrementalTempValue;
    private boolean mIsAnimating;
    private Animator mAnimator;
    Bitmap bitmap = null;

    public Thermometer(Context context) {
        this(context, null);
    }

    public Thermometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
       // cnxt = (MainActivity)context;
    }

    public Thermometer(Context context, AttributeSet attrs, int defStyle) { //AttributeSet->사용자 정의 클래스


        super(context, attrs, defStyle);

        if (attrs != null) {

            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Thermometer, defStyle, 0);
            // attrs.xml 에 선언해둔 attribute를 이용하여 이를 각각의 View에 설정해주는 작업을 합니다.
            mThermometerColor = a.getColor(R.styleable.Thermometer_therm_color, mThermometerColor);

            a.recycle();//할당되어 있던 메모리를 풀(pool)에 즉시 돌려줘서 garbage collection이 될 때까지 기다릴 필요가 없게 되니까.
        }
     //   bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.feeding);
     /*   Bitmap img = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        canvas.drawBitmap(img,320,10,null);
        img.recycle();*/
        init();
    }

    private void init() {
      //  getDbData("http://34.235.17.160/viewdb1.php");
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //색상 차가 뚜렷한 경계 부근에 중간 색을 삽입하여 도형이나 글꼴이 주변 배경과 부드럽게 잘어울리도록 하는 기법
        mInnerCirclePaint.setColor(mThermometerColor);
        mInnerCirclePaint.setStyle(Paint.Style.FILL);
        mInnerCirclePaint.setStrokeWidth(17f);


        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setColor(Color.WHITE);
        mOuterCirclePaint.setStyle(Paint.Style.FILL);
        mOuterCirclePaint.setStrokeWidth(32f);


        mFirstOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstOuterCirclePaint.setColor(Color.rgb(175, 161, 167));
        mFirstOuterCirclePaint.setStyle(Paint.Style.FILL);
        mFirstOuterCirclePaint.setStrokeWidth(60f);


        mFirstOuterArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstOuterArcPaint.setColor(Color.rgb(175, 161, 167));
        mFirstOuterArcPaint.setStyle(Paint.Style.STROKE);
        mFirstOuterArcPaint.setStrokeWidth(30f);


        mInnerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerLinePaint.setColor(mThermometerColor);
        mInnerLinePaint.setStyle(Paint.Style.FILL);
        mInnerLinePaint.setStrokeWidth(17f);

        mOuterLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterLinePaint.setColor(Color.WHITE);
        mOuterLinePaint.setStyle(Paint.Style.FILL);


        mFirstOuterLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstOuterLinePaint.setColor(Color.rgb(175, 161, 167));
        mFirstOuterLinePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStageCenterX = getWidth() / 3;

        mStageHeight = getHeight();

        mCellWidth = mStageHeight / NUMBER_OF_CELLS;

        //center of first cell
        mStartCenterY = mCellWidth / 2;


        //move to 3rd cell
        mLastCellWidth = (float) (2.5 * mCellWidth);

        //center of last(3rd) cell
        mEndCenterY = mLastCellWidth - (mCellWidth / 2);


        // mOuterRadius is 1/4 of mCellWidth
        mOuterRadius = (int) (0.25 * mCellWidth);

        mInnerRadius = (int) (0.656 * mOuterRadius);

        mFirstOuterRadius = (int) (1.344 * mOuterRadius);

        mFirstOuterLinePaint.setStrokeWidth(mFirstOuterRadius);

        mOuterLinePaint.setStrokeWidth(mFirstOuterRadius / 2);

        mFirstOuterArcPaint.setStrokeWidth(mFirstOuterRadius / 4);

        mXOffset = mFirstOuterRadius / 4;
        mXOffset = mXOffset / 2;

        //get the d/f btn firstOuterLine and innerAnimatedline
        mYOffset = (mStartCenterY + (float) 0.875 * mOuterRadius) - (mStartCenterY + mInnerRadius);
        mYOffset = mYOffset / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        drawFirstOuterCircle(canvas);

        drawOuterCircle(canvas);

        drawInnerCircle(canvas);

        drawFirstOuterLine(canvas);

        drawOuterLine(canvas);

        animateInnerLine(canvas);

        drawFirstOuterCornerArc(canvas);
        tem = "35.5";
        tv = new TextView(getContext());
        if(tem != null){
            if((35.5<= Float.valueOf(tem))&&((37.5 >= Float.valueOf(tem)))) {
                tv.setText(tem + "'C");
                tv.setTextColor(Color.parseColor("#c9346b"));
            }
           else{
                tv.setText(tem + "'C");
                tv.setTextColor(Color.parseColor("#1cdd53"));
            }
        }

        tv.setTextSize(77);
        tv.setX((float) (getWidth()/2.32));
        tv.setY((float) (getHeight()/2.85));
        tv.setGravity(Gravity.RIGHT| Gravity.BOTTOM); //텍스트 뷰 자체의 Gravity를 가로,세로 중앙으로 설정한다
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin =10;
        tv.setLayoutParams(lp);
        ((RelativeLayout) this.getParent()).addView(tv);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int paddingY = getPaddingBottom() + getPaddingTop();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        height += paddingY;

        setMeasuredDimension(width, height);
    }


    private void drawInnerCircle(Canvas canvas) {
        drawCircle(canvas, mInnerRadius, mInnerCirclePaint);
    }

    private void drawOuterCircle(Canvas canvas) {
        drawCircle(canvas, mOuterRadius, mOuterCirclePaint);
    }


    private void drawFirstOuterCircle(Canvas canvas) {
        drawCircle(canvas, mFirstOuterRadius, mFirstOuterCirclePaint);
    }


    private void drawCircle(Canvas canvas, float radius, Paint paint) {
        canvas.drawCircle(mStageCenterX, mEndCenterY, radius, paint);
    }

    private void drawOuterLine(Canvas canvas) {

        float startY = mEndCenterY - (float) (0.875 * mOuterRadius);
        float stopY = mStartCenterY + (float) (0.875 * mOuterRadius);

        drawLine(canvas, startY, stopY, mOuterLinePaint);
    }


    private void drawFirstOuterLine(Canvas canvas) {

        float startY = mEndCenterY - (float) (0.875 * mFirstOuterRadius);
        float stopY = mStartCenterY + (float) (0.875 * mOuterRadius);

        drawLine(canvas, startY, stopY, mFirstOuterLinePaint);
    }


    private void drawLine(Canvas canvas, float startY, float stopY, Paint paint) {
        canvas.drawLine(mStageCenterX, startY, mStageCenterX, stopY, paint);
    }


    private void animateInnerLine(Canvas canvas) {

        if (mAnimator == null)
            measureTemperature();

        if (!mIsAnimating) {

            mIncrementalTempValue = mEndCenterY + (float) (0.875 * mInnerRadius);
       //     Log.i("Icre", String.valueOf(mIncrementalTempValue));
            mIsAnimating = true;

        } else {
//(Integer.valueOf(bpm) / 2)
            if(tem != null) {
                mTemHeight = (float) 1060 - (771 * Float.valueOf(tem) / 50);
                mIncrementalTempValue = mEndCenterY + (float) (0.875 * mInnerRadius) - mIncrementalTempValue;
                Log.i("Icre2", String.valueOf(mIncrementalTempValue));
            }
            else
            {
                Toast.makeText(getContext(), "서버연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                stopMeasurement(); ////////////////change
            }

        }

        if (mIncrementalTempValue > mTemHeight) {
            float startY = mEndCenterY + (float) (0.875 * mInnerRadius);


            Log.i("Icre3", String.valueOf(mTemHeight));
            drawLine(canvas, startY, mIncrementalTempValue , mInnerCirclePaint);
            //drawLine(X좌표 시작, Y좌표 시작, X좌표 끝, Y좌표 끝, 페인트)
        } else {

            float startY = mEndCenterY + (float) (0.875 * mInnerRadius);
            float stopY = mStartCenterY + mInnerRadius;
            drawLine(canvas, startY, mTemHeight, mInnerCirclePaint);
            //drawLine(X좌표 시작, Y좌표 시작, X좌표 끝, Y좌표 끝, 페인트)
            mIsAnimating = false;
            stopMeasurement();

        }

    }


    private void drawFirstOuterCornerArc(Canvas canvas) {

        float y = mStartCenterY - (float) (0.875 * mFirstOuterRadius);

        RectF rectF = new RectF(mStageCenterX - mFirstOuterRadius / 2 + mXOffset, y + mFirstOuterRadius, mStageCenterX + mFirstOuterRadius / 2 - mXOffset, y + (2 * mFirstOuterRadius) + mYOffset);

        canvas.drawArc(rectF, -180, 180, false, mFirstOuterArcPaint);

    }

    //simulate temperature measurement for now
    private void measureTemperature() {
        mAnimator = new Animator();
        mAnimator.start();
    }


    private class Animator implements Runnable {
        private Scroller mScroller;
        private final static int ANIM_START_DELAY = 500;
        private final static int ANIM_DURATION = 7000;
        private boolean mRestartAnimation = false;

        public Animator() {
            mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
        }

        public void run() {
            if (mAnimator != this)
                return;

            if (mRestartAnimation) {
                int startY = (int) (mStartCenterY - (float) (0.875 * mInnerRadius));
                int dy = (int) (mEndCenterY + mInnerRadius);
                mScroller.startScroll(0, startY, 0, dy, ANIM_DURATION);
                mRestartAnimation = false;
            }

            boolean isScrolling = mScroller.computeScrollOffset();  // 스크롤러에 의해서 스크롤이 이루어질 수 있으며, 그 중간값 X,Y를 얻을 수 있으면
            mIncrementalTempValue = mScroller.getCurrY();

            if (isScrolling) {
                invalidate();
                post(this);
            } else {
                stop();
            }


        }

        public void start() {
            mRestartAnimation = true;
            postDelayed(this, ANIM_START_DELAY);
        }

        public void stop() {
            removeCallbacks(this);
            mAnimator = null;
        }

    }


    private void stopMeasurement() {
        if (mAnimator != null)
            mAnimator.stop();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        measureTemperature();

    }

    @Override
    protected void onDetachedFromWindow() {
        stopMeasurement();

        super.onDetachedFromWindow();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        switch (visibility) {
            case View.VISIBLE:

                measureTemperature();

                break;

            default:

                stopMeasurement();

                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.d("Screen","(x,y)=>"+"("+event.getX()+","+event.getY()+")");
            return true;
        }
        return false;
    }


    /**
     * AsyncTask<Integer, Integer, Boolean>은 아래와 같은 형식이다
     * AsyncTask<전달받은값의종류, Update값의종류, 결과값의종류>
     * ex) AsyncTask<Void, Integer, Void>
     */
    private void getDbData(String string) {
        class ParsingTask extends AsyncTask<String, Void, String> {

            /**
             * 백그라운드 작업
             *
             * @return
             */
            @Override
            protected String doInBackground(String... params) {

                // 전달받은 데이터가 없다면, 정보조회를 진행할 필요가 없음
                // 따라서 정보조회를 진행할 데이터가 넘어왔는지 검증해야 함
                if (params.length == 0) {
                    return null;
                }
                String uri = params[0];
                HttpURLConnection con = null;
                BufferedReader bufferedReader = null;
                // HttpURLConnection과 BufferedReader Object는 try/catch block 외부에서
                // 선언되어져야 함. 이렇게 선언되어져야 finally block을 통해 Resource 해제가 가능함
                // 에러가 발생하는 경우에도 Resource가 해제됨
                try {
                    URL url = new URL(uri);
                    con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;

                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }

            /**
             * 백그라운드 작업이 완료된 후 호출되는 메소드
             *
             * @param result
             */
            protected void onPostExecute(String result) {
                myJSON = result;
                try {
                    Log.i("RUN",result);
                    getSensor();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("result", result);

            }
        }
        ParsingTask g = new ParsingTask();
        g.execute(string);
    }
    private void getSensor() throws JSONException {
        //     Log.i("RUN",result));
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray sensorArray = jsonObj.getJSONArray(TAG_RESULTS);
            // JSon 형태로 받은 데이터를 보관할 String을 선언
            // JSON Format의 정보를 받아서 우리 모바일 앱에서 필요한 정보만 꺼내옴
            // JSON Format parsing은 쉬운 작업임
            // 생성자가 JSON String을 받아들여서 우리가 처리하기 쉽게 Object hierarchy에 넣어 줌
            for (int i = 0; i < sensorArray.length(); i++) {
                JSONObject senobj = sensorArray.getJSONObject(i);
                // id는 long 형태로 전달됨. (date가 될것임)
                // 따라서, 읽기 쉬운 형태로 변환시켜 줘야 함
                id = senobj.getString(TAG_ID);
                hum = senobj.getString(TAG_HUMIDITY);
                mic = senobj.getString(TAG_MIC);
                tem = senobj.getString(TAG_TEMPERATURE);
                bpm = senobj.getString(TAG_BPM);
                Log.i("id22", id);
                Log.i("hum", hum);
                Log.i("mic", mic);
                Log.i("tem", tem);
                Log.i("bpm", bpm);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}