package com.kasarulor.ui.log;

import android.util.Log;

import com.kasarulor.ui.BuildConfig;

public class LogX {
    public   static  final  boolean  SHOWLOG= BuildConfig.LOG;

    public   static   void   e(String tag,String message){
        if(SHOWLOG)
            Log.e(tag,message);
    }
    public   static   void   d(String tag,String message){
        if(SHOWLOG)
            Log.d(tag,message);
    }
    public   static   void   i(String tag,String message){
        if(SHOWLOG)
            Log.i(tag,message);
    }
    public   static   void   v(String tag,String message){
        if(SHOWLOG)
            Log.v(tag,message);
    }
    public   static   void   w(String tag,String message){
        if(SHOWLOG)
            Log.w(tag,message);
    }
}
