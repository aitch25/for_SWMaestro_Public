package com.yang.bebe;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.yang.bebe.DB.Bpm;
import com.yang.bebe.DB.BpmDBHelper;
import com.yang.bebe.DB.BpmListAdapter;

import java.util.ArrayList;

public class TemperatureFragment2 extends Fragment {

    //Tab_List 전역변수
    public static Button refresh_btn;
    public static ListView list_diary;
    public static BpmListAdapter listAdapter;
    public static int flag = 0;
    private static ArrayList<Bpm> data;
    public static BpmDBHelper dbHelper;
    public static SQLiteDatabase db;


    public TemperatureFragment2() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TemperatureFragment2 newInstance(int position) {
        TemperatureFragment2 f = new TemperatureFragment2();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //디비 핼퍼 객체 생성
        dbHelper = new BpmDBHelper(getActivity());
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baby_fragment2, container, false);

        flag = 1;
        refresh_btn = (Button) rootView.findViewById(R.id.refresh_btn_list);
        list_diary = (ListView) rootView.findViewById(R.id.list_diary);
        data = showDB();
        listAdapter = new BpmListAdapter(getContext(), R.layout.list_layout, data);
        list_diary.setAdapter(listAdapter);
        list_diary.setDivider(new ColorDrawable(Color.GREEN));
        list_diary.setDividerHeight(1);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = showDB();
                listAdapter = new BpmListAdapter(getContext(), R.layout.list_layout, data);
                list_diary.setAdapter(listAdapter);
            }
        });

        list_diary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        list_diary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage(data.get(position).getDate() + "을(를) 삭제하시겠습니까?");
                alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int code = data.get(position).getCode();
                        deleteDB(code);
                        showDB();
                        listAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return false;
            }
        });
        if (flag == 1) {
            return rootView;
        } else {
            return null;
        }
    }

    public static void deleteDB(int code) {
        db = dbHelper.getReadableDatabase();
        String sql = "delete from bpm where code=" + code;
        db.execSQL(sql);
        db.close();
    }

    public static ArrayList<Bpm> showDB() {
        data.clear();
        db = dbHelper.getReadableDatabase();
        String sql = "select * from bpm order by code desc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bpm bpm = new Bpm();
            bpm.setCode(cursor.getInt(0));
            bpm.setDate(cursor.getString(1));
            bpm.setContents(cursor.getString(2));
      //      Log.i("aaaaa",cursor.getString(2));
           // if(bpm != null)
              data.add(bpm);
        }
        cursor.close();
        db.close();
        return data;
    }



}