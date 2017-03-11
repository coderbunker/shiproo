package com.coderbunker.hyperledger.communication;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.coderbunker.hyperledger.MainActivity;
import com.coderbunker.hyperledger.R;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetworkService extends Service {

    public static final int NOTIFICATION_ID = 1000;

    public static String ACTION_START = "ACTION_START";
    public static String ACTION_PROCESS = "ACTION_PROCESS";
    public static String ACTION_END = "ACTION_END";

    public static String EXTRA_COMMAND = "EXTRA_COMMAND";

    public static void startService(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    public static void runCommand(Context context, Command command) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_PROCESS);
        intent.putExtra(EXTRA_COMMAND, command);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_END);
        context.stopService(intent);
    }

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 6, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(ACTION_START)) {
            // start service and prevent the death by system
            processStartNotification();
        } else if (action.equals(ACTION_PROCESS)) {
            // extract parcelable content and run the command
            processCommand(intent);
        } else if (action.equals(ACTION_END)) {
            // end service
            executor.shutdown();
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void processCommand(Intent intent) {
        final Command command = intent.getParcelableExtra(EXTRA_COMMAND);
        executor.submit(command, new Runnable() {
            @Override
            public void run() {
                command.execute();
            }
        });
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Scheduled Notification")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText("This notification has been triggered by Notification Service")
                .setSmallIcon(R.mipmap.ic_launcher);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
