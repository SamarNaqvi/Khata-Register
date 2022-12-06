package com.example.khataregister.service;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;


//boundServices have two types local-within process of same app(Binded with Ibinder) and remote-within process of different appf(binded with messenger and AIDL)

////////////////////////////////////////
public class counterService extends Service {
    static private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=100;

    public static final int GET_COUNT=0;

    private class RandomNumberRequestHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GET_COUNT:
                    Message messageSendRandomNumber = Message.obtain(null, GET_COUNT);
                    messageSendRandomNumber.arg1 = getRandomNumber();
                    try {
                        msg.replyTo.send(messageSendRandomNumber);
                    } catch (RemoteException e) {
                        Log.i(TAG, "" + e.getMessage());
                        e.printStackTrace();
                    }
            }
            super.handleMessage(msg);
        }
    }

    private Messenger randomNumberMessenger = new Messenger(new RandomNumberRequestHandler());

//    class MyServiceBinder extends Binder {
//        public MyService getService(){
//            return MyService.this;
//        }
//    }
//    private IBinder mBinder = new MyServiceBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("In onBind");
        return randomNumberMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        System.out.println("Service Destroyed");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("Service Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("In onStartCommand, thread id: "+Thread.currentThread().getId());
        //Log.i(getString(R.string.service_demo_tag),"In onStartCommend, thread id: "+Thread.currentThread().getId());
        mIsRandomGeneratorOn =true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    private void startRandomNumberGenerator() {
        while(mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = mRandomNumber+1;
                    System.out.println("Thread id: " + Thread.currentThread().getId() + ", Random Number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                System.out.println("Thread stopped");
                e.printStackTrace();
            }
        }
    }

    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn=false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("In onUnbind");
        return super.onUnbind(intent);

    }

    static public int getRandomNumber(){
        return mRandomNumber;
    }
}

