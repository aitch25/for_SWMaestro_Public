package com.yang.bebe;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.bebe.BPM.BabyConditionFragment1;
import com.yang.bebe.BPM.BabyConditionFragment2;
import com.yang.bebe.widget.Thermometer;

public class TemperatureFragment1 extends Fragment {

    Thermometer myView;
    public TemperatureFragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TemperatureFragment1 newInstance(int position) {
        TemperatureFragment1 f = new TemperatureFragment1();
        return f;
    }
    private ViewPager pager;
    //   private PagerSlidingTabStrip tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDbData("http://172.25.227.12/viewdb1.php");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baby_tem, container, false);
        //    Bundle receive_data = getArguments(); //액티비티에서 넘어온 값을 받기 위해서 Bundle설정.//
        myView = (Thermometer)rootView.findViewById(R.id.cv);
        //   Log.i("ID,,",id);
        // View Pager를 선언합니다.

        return rootView;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)  //각 페이지의 정보에 따라 설정.//
            {
                case 0:
                    return "Temperature";
                case 1:
                    return "quantity";
                case 2:
                    return "History";

                default:
                    return null;
            }
        }
        public int getItemPosition(Object object) { return BabyConditionFragment.PagerAdapter.POSITION_NONE; }

        /**
         * View Pager의 Fragment 들은 각각 Index를 가진다.
         * Android OS로 부터 요청된 Pager의 Index를 보내주면,
         * 해당되는 Fragment를 리턴시킨다.
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            //Fragment fragment = null;

            switch(position)
            {
                case 0:
                    // Log.i("pos", String.valueOf(position));
                    BabyConditionFragment1 fragment_1 = new BabyConditionFragment1();
               /*     //Fragment에게 값을 전달하기 위해서 Bundle사용.//
                    //Intent일 때는 액티비티간에 데이터 전달이였다. 마찬가지로 프래그먼트도 (key,value)로 구성 후 bundle을 이용한다.//
                    Bundle bundle1= new Bundle(); //Fragment에게 값을 전달하기 위해서 Bundle사용.//\
                    bundle1.putString("Heartbeat", bpm);
                    bundle1.putString("Key", id);
                    Log.v("bundle1", String.valueOf(bundle1));
                    fragment_1.setArguments(bundle1);
*/
                    return fragment_1;

                case 1:
                    //   Log.i("pos", String.valueOf(position));
                    BabyConditionFragment2 fragment_2 = new BabyConditionFragment2();
                  /*    Bundle bundle2= new Bundle(); //Fragment에게 값을 전달하기 위해서 Bundle사용.//\
                    bundle2.putString("temperature", tem);
                    bundle2.putString("Key", id);
                    Log.v("bundle2", String.valueOf(bundle2));
                    FragmentManager fm2 = getFragmentManager();
                    FragmentTransaction fmt2 = fm2.beginTransaction();

                    fragment_2.setArguments(bundle2);
*/
                    return fragment_2;


                default: return null;
            }
        }

        /**
         * View Pager에 몇개의 Fragment가 들어가는지 설정
         * @return
         */
        @Override
        public int getCount() {
            return 2;
        }//나타낼 페이지의 수. 탭뷰의 개수만큼 반환.//
    }


}
