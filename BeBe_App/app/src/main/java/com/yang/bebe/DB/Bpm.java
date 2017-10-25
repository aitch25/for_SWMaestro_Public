package com.yang.bebe.DB;

/**
 * Created by Administrator on 2017-01-17.
 */

public class Bpm {
    private int code;
    private String date;
    private String contents;

    public void setDate(String date) {
        this.date = date;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }


    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

}
