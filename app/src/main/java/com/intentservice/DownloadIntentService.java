package com.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    /**
     * Handles the request. Extract the values from intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String getUrl;
        int getId;
        Bundle b = intent.getExtras();
        if(b!=null)
        {
            getUrl =  (String) b.get("btnImage1");
            getId = (int) b.get("ID");

            getBitmapFromURL(getUrl, getId);
        }
//        stopSelf();
    }

    /**
     * Download the images and put it back to the intent to send it to the MainActivity
     */
        public void getBitmapFromURL (String src, int id){

            try
            {
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
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


