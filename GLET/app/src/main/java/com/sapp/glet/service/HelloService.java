package com.sapp.glet.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sapp.glet.MainActivity;
import com.sapp.glet.R;
import com.sapp.glet.connection.Client;
import com.sapp.glet.connection.MessageListener;

import java.io.IOException;

/**
 * Created by Simon on 15.03.2017.
 */

public class HelloService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public int timeout = 0;
        public Context notifyContext;

        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.


            Client client = new Client("m.m-core.eu", 24400);
            boolean connected = false;

            while (true)
            {
                timeout++;
                try {
                    if ((!connected) || timeout > 6)
                    {
                        connected = client.Connect();
                        timeout = 0;
                    }

                    if (connected)
                    {
                        client.addListener(new MessageListener() {
                            @Override
                            public void recieveMessage(String message, byte[] bytes) {
                                timeout = 0;

                                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(notifyContext)
                                                .setSmallIcon(R.drawable.ic_menu_camera).setContentInfo("Right Inf").setSubText("Sub Text").setVibrate(new long[] {0, 100, 200, 300, 300}).setSound(alarmSound)
                                                .setContentTitle("My notification")
                                                .setContentText("Length: " + bytes.length);


// Creates an explicit intent for an Activity in your app
                                Intent resultIntent = new Intent(notifyContext, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(notifyContext);
// Adds the back stack for the Intent (but not the Intent itself)
                                stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent =
                                        stackBuilder.getPendingIntent(
                                                0,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );
                                mBuilder.setContentIntent(resultPendingIntent);
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                                mNotificationManager.notify(0, mBuilder.build());
                            }
                        });

                        client.send(new byte[] {0, 2, 0, 1, 2, 3});
                    }


                } catch (IOException e) {

                }


                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt();
                }

            }



            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job

            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mServiceHandler.notifyContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "service removed", Toast.LENGTH_SHORT).show();

        super.onTaskRemoved(rootIntent);
        //sendBroadcast(new Intent("IWillStartAuto"));
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();

        super.onDestroy();
        //sendBroadcast(new Intent("IWillStartAuto"));
    }
}