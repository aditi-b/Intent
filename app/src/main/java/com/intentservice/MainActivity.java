package com.intentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnImageDownload, btnParallelDownload;                                         // button to download the images one by one
    String firstUrl = "https://cdn.dtcn.com/dt/dt-logo-small.png";
    String secondUrl = "https://compresspng.com/images/compresspng/icon.png";
    String thirdUrl = "https://www.clker.com/cliparts/b/e/U/I/k/O/add2-png-md.png";
    String arr[] = {firstUrl, secondUrl, thirdUrl};                 // array of image urls
    ImageView imgView, imgView2, imgView3;
    ImageReceiver imageReceiver;                                    // broadcast receiver
    int imageId = 1;                                                // id to identify the image view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnImageDownload = findViewById(R.id.button1);
        btnParallelDownload = findViewById(R.id.button2);

        imgView = findViewById(R.id.imgView);
        imgView2 = findViewById(R.id.imgView2);
        imgView3 = findViewById(R.id.imgView3);

        btnImageDownload.setOnClickListener(this);
        btnParallelDownload.setOnClickListener(this);

        imageReceiver = new ImageReceiver();
        // registering the broadcast receiver
        registerReceiver(imageReceiver, new IntentFilter("GET_IMAGE"));

    }

    /**
     * to send the url and id to service class
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Intent startIntent = new Intent(MainActivity.this, DownloadIntentService.class);
                for (String anArr : arr) {
                    startIntent.putExtra("btnImage1", anArr);
                    startIntent.putExtra("ID", imageId);
                    imageId++;
                    startService(startIntent);
                }
                break;

            case R.id.button2:
                Intent parallelIntent = new Intent(MainActivity.this, ParallelDownloadService.class);
                for (String anArr : arr) {
                    parallelIntent.putExtra("btnImage2", anArr);
                    parallelIntent.putExtra("ID", imageId);
                    imageId++;
                    startService(parallelIntent);
                }
                break;
        }

    }

    // Broadcast receiver class
    class ImageReceiver extends BroadcastReceiver {
        @Override

        /*
          onReceive method to receive the broadcast
         */
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("GET_IMAGE")) {
                Bitmap bitmap = intent.getParcelableExtra("BitmapImage");    // getting the image
                int id = intent.getIntExtra("Id", 0);            // getting the id
                switch (id) {
                    case 1:
                        imgView.setImageBitmap(bitmap);                           // setting the image to the image view
                        break;

                    case 2:
                        imgView2.setImageBitmap(bitmap);
                        break;

                    case 3:
                        imgView3.setImageBitmap(bitmap);
                        break;

                }

            }
        }

    }

    /**
     * unregistering the broadcast receiver
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(imageReceiver);
    }
}




