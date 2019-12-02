package com.intentservice;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Process;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ParallelDownloadService extends Service {
    private ServiceHandler serviceHandler;


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        /**
         * Handle the request from onStartCommand
         */
        @Override
        public void handleMessage(Message msg) {
            String getUrl;
            int getId;

            Bundle b = msg.getData();
            if (b != null) {
                getUrl = (String) b.get("btnImage2");
                getId = (int) b.get("ID");
                getBitmapFromURL(getUrl, getId);
            }
            stopSelf(msg.arg1);
        }

    }

    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        Looper serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();


        Bundle b = intent.getExtras();
        if (b != null) {

            Message msg = serviceHandler.obtainMessage();
            Message m = new Message();
            m.setData(b);
            msg.arg1 = startId;
            serviceHandler.sendMessage(m);

        }
        return START_STICKY;
    }

    /**
     * getting the image from url and decoding it to bitmap and sending to MainActivity by broadcast receiver
     */
    public void getBitmapFromURL(String src, int id) {

        try {
            URL url = new URL(src);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Intent intent = new Intent("GET_IMAGE");
            intent.putExtra("BitmapImage", myBitmap);
            intent.putExtra("Id", id);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("GET_IMAGE");
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
