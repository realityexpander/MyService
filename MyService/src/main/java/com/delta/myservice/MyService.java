package com.delta.myservice;


import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;


/**
 * Created by Vic on 13/9/10.
 */
public class MyService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private HandlerThread myHandlerThread;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Heya")
                            .setContentText("Well hello there!");

            // Add the notification
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());

            Log.e("0xcafebabe the msg", Integer.toString(msg.arg1));
            stopSelf();
        }
    }

    public class myBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.

        /* todo:
         * Create a HandlerThread with 2 parameters
         *
         * - Get the Looper from the thread
         * - Use the Looper from the previous step to get a new ServiceHandler
         * - You will need the ServiceHandler from the previous step in other functions!
         * */

        myHandlerThread = new HandlerThread("Test", Thread.MIN_PRIORITY);
        myHandlerThread.start();
        mServiceLooper = myHandlerThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        /*
        * todo:
        *
        * - Obtain the Message object from the service handler
        * - Assign the startID to the Message's first argument
        * - send the message
        * */
        Message theMessage = mServiceHandler.obtainMessage();
        theMessage.arg1 = startId;
        mServiceHandler.sendMessage(theMessage);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}


//package com.delta.myservice;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.widget.Toast;
//
///**
// * Created by Vic on 13/9/10.
// */
//public class MyService extends Service {
//    private Looper mServiceLooper;
//    private ServiceHandler mServiceHandler;
//
//    // Handler that receives messages from the thread
//    private final class ServiceHandler extends Handler {
//        public ServiceHandler(Looper looper) {
//            super(looper);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            // Normally we would do some work here, like download a file.
//            // For our sample, we just sleep for 5 seconds.
//            long endTime = System.currentTimeMillis() + 5*1000;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                    }
//                }
//            }
//            // Stop the service using the startId, so that we don't stop
//            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        // Start up the thread running the service.  Note that we create a
//        // separate thread because the service normally runs in the process's
//        // main thread, which we don't want to block.  We also make it
//        // background priority so CPU-intensive work will not disrupt our UI.
//
//        /* todo:
//         * Create a HandlerThread with 2 parameters
//         *
//         * - Get the Looper from the thread
//         * - Use the Looper from the previous step to get a new ServiceHandler
//         * - You will need the ServiceHandler from the previous step in other functions!
//         * */
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//
//        /*
//        * todo:
//        *
//        * - Obtain the Message object from the service handler
//        * - Assign the startID to the Message's first argument
//        * */
//
//        // If we get killed, after returning from here, restart
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // We don't provide binding, so return null
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
//    }
//}
