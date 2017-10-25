package com.yang.bebe.BPM;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.yang.bebe.DB.Bpm;
import com.yang.bebe.DB.BpmDBHelper;
import com.yang.bebe.DB.TimeUtils;
import com.yang.bebe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BabyConditionFragment1 extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
    TextView sensortext;
    Button info_1_button;
    private static ArrayList<Bpm> data;
    public static BpmDBHelper dbHelper;
    public static SQLiteDatabase db;
    public static SQLiteDatabase db2;
    public static BpmDBHelper dbHelper2;
    int pStatus = 0;
    private Handler handler = new Handler();
  //  protected boolean mbActive;
 //   protected static final int TIMER_RUNTIME = 10000;
   private static final String TAG = BabyConditionFragment2.class.getSimpleName();
    private final static int WEEK_COUNT = 7;
    private final static int DISTRIBUTION_COUNT = 10;

    @Bind(R.id.chart1)
    CombinedChart mChart;
    private int mAccentColor;
    private int mPrimaryColor;
    private int mPrimaryColorDark;
    private Setgraph mUpdateChartTask;
    private CombinedData mCombinedData;


    public BabyConditionFragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BabyConditionFragment1 newInstance(String mParam1, String mParam2) {
        BabyConditionFragment1 f = new BabyConditionFragment1();
        Bundle b = new Bundle();
        b.putString(ARG_STR1,mParam1);
        b.putString(ARG_STR2,mParam2);
        Log.v("Test2","Param1 : "+mParam1 + " Param2 :"+mParam2);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDbData("http://192.168.1.101/viewdb1.php");
        //디비 핼퍼 객체 생성
        dbHelper = new BpmDBHelper(getActivity());
        data = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baby_fragment1, container, false);

        mChart = (CombinedChart) rootView.findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        TypedValue accentValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, accentValue, true);
        mAccentColor = accentValue.data;
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, accentValue, true);
        mPrimaryColor = accentValue.data;
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, accentValue, true);
        mPrimaryColorDark = accentValue.data;
        mUpdateChartTask = new Setgraph();
        mUpdateChartTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) rootView.findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(150); // Secondary Progress
        mProgress.setMax(150); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        sensortext = (TextView) rootView.findViewById(R.id.textView1);
        info_1_button = (Button)rootView.findViewById(R.id.button1);

                info_1_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                //progress ++

                pStatus = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if( bpm != null) {
                            while (pStatus < Integer.valueOf(bpm)) {
                                pStatus += 1;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        mProgress.setProgress(pStatus);
                                    }
                                });
                                try {
                                    // Sleep for 200 milliseconds.
                                    // Just to display the progress slowly
                                    Thread.sleep(8); //thread will take approx 1.5 seconds to finish
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }).start();
                sensortext.setText("0" + bpm);

                getDbData("http://192.168.1.101/viewdb1.php");
                InsertDB();

                mCombinedData.setData(getBpmData());
                //   mCombinedData.setData(getWeekThoughtData());
                mCombinedData.setData(getAverageBpm());
            }
        });


        return rootView;
    }

    public void InsertDB() {
        db = dbHelper.getWritableDatabase();
        String sql = "insert into bpm ('date', 'contents') values(?,?)";
        SQLiteStatement st = db.compileStatement(sql);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yy년 MM월 dd일 HH시 mm분 ss초");
        String writeTime = sdf.format(date);
        st.bindString(1, writeTime);
      //  Log.i("aaaa", writeTime);
        if( bpm != null)////////
          st.bindString(2, bpm);
//        Log.i("aaaa", bpm);
            st.execute();
         //   db.close();
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
                Log.i("id", id);
                Log.i("hum", hum);
                Log.i("mic", mic);
                Log.i("tem", tem);
                Log.i("bpm", bpm);

                //fragment_3.newInstance(mic,id);
                //  fragment_3.setArguments(bundle3);
                //     Log.i("'recieve", String.valueOf(bundle3));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////////////graph
    private class Setgraph extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setTouchEnabled(true);
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);
            mChart.setDescription("");
            mChart.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            mChart.setDrawGridBackground(false);
            mChart.setDrawBarShadow(false);
            mChart.setDrawOrder(
                    new CombinedChart.DrawOrder[]{
                            CombinedChart.DrawOrder.BAR,
                            CombinedChart.DrawOrder.LINE
                    });
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setValueFormatter(new IntValueFormatter());
            leftAxis.setDrawGridLines(false);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setEnabled(false);

        }
        @Override
        protected Void doInBackground(Void... params) {
            mCombinedData = new CombinedData(TimeUtils.getWeekDateString());
            mCombinedData.setData(getBpmData());
         //   mCombinedData.setData(getWeekThoughtData());
            mCombinedData.setData(getAverageBpm());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mChart.setData(mCombinedData);
            mChart.invalidate();
        }
    }
    private BarData getBpmData() {
        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<>();
        ////////////db에서 값 불러오기
        int[] minValues = new int[]{0, 0, 0, 0, 0, 0, 0};
        int[] D = new int[7];

        List<double[]> Senvalues = new ArrayList<double[]>();

        db = dbHelper.getReadableDatabase();
        String sql = "select contents from bpm order by code desc limit 7   ";
        Cursor c = db.rawQuery(sql, null); // 배열지정
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {//row 값 받아오기
            if(D[i]>= 0)
                if( c != null && c.moveToFirst() ) {
                if(c.getString(0) !=null)
                    if(c.getString(0)==null)
                        D[i] = 0;
                     else D[i] =Integer.valueOf(c.getString(0)); // 값 받아오는 부분  ///////////////////////10.08 에러나는 부분
                    //    Log.i("dasa", String.valueOf(D[i]));
                }
                c.moveToNext();//다음 row로 이동

        }

        Senvalues.add(new double[]
                {D[0], D[1], D[2], D[3], D[4], D[5], D[6]}); //sensor 값들 저장 (bpm)
    //    db.close();
//////////////////////////////////
        /////////////////// 데이터 입력

        entries.add(new BarEntry((float) D[0], 0));
        entries.add(new BarEntry((float) D[1], 1));
        entries.add(new BarEntry((float) D[2], 2));
        entries.add(new BarEntry((float) D[3], 3));
        entries.add(new BarEntry((float) D[4], 4));
        entries.add(new BarEntry((float) D[5], 5));
        entries.add(new BarEntry((float) D[6], 6));

        BarDataSet set = new BarDataSet(entries, getString(R.string.time));

        set.setColor(mPrimaryColorDark);
        set.setValueFormatter(new IntValueFormatter());
        d.addDataSet(set);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return d;
    }

    private class IntValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }
    private LineData getAverageBpm() {
        LineData d = new LineData();
        double[] minValues = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[] D2 = new double[7];
        float avg=0;
        List<double[]> Senvalues = new ArrayList<double[]>();

        ArrayList<Entry> entries = new ArrayList<>();
        db2 = dbHelper.getReadableDatabase();
        String sql = "select contents from bpm order by code desc limit 7";
        Cursor c = db2.rawQuery(sql, null); // 배열지정
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {//row 값 받아오기
            D2[i] = Integer.valueOf(c.getInt(0)); // 값 받아오는 부분
        //   Log.i("d2asa", String.valueOf(D2[i]));
            c.moveToNext();//다음 row로 이동
        }
        for(int i=0; i<c.getCount();i++)
        {
            avg += D2[i];
        }
        avg = avg/c.getCount();

        entries.add(new BarEntry(avg,0));
        entries.add(new BarEntry(avg,1));
        entries.add(new BarEntry(avg,2));
        entries.add(new BarEntry(avg,3));
        entries.add(new BarEntry(avg,4));
        entries.add(new BarEntry(avg,5));


        LineDataSet set = new LineDataSet(entries, getString(R.string.average));
        set.setColor(mAccentColor);
        set.setLineWidth(2.5f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setCircleColor(R.color.grape_primary_dark);
        set.setCircleSize(5f);
        set.setFillColor(mPrimaryColor);
        set.setDrawValues(true);
        set.setValueFormatter(new IntValueFormatter());
        d.addDataSet(set);
        return d;
    }

}
