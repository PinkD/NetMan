package moe.pinkd.netman.component.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import moe.pinkd.netman.R;
import moe.pinkd.netman.component.service.RestoreIptablesRulesService;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: WTF--->  " + intent.toString());
        postNotification(context);
    }

    private void postNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.string.app_name, createNotification(context));
    }

    private Notification createNotification(Context context) {
        Notification.Builder build = new Notification.Builder(context)
                .setContentTitle(context.getText(R.string.boot_notification_title))
                .setContentText(context.getText(R.string.boot_notification_content))
                .setSmallIcon(R.drawable.ic_wifi)
                .setContentIntent(PendingIntent.getService(context, R.string.app_name, new Intent(context, RestoreIptablesRulesService.class).setAction(RestoreIptablesRulesService.ACTION_RESTORE_IPTABLES_RULES), PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = build.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            return notification;
        } else {
            notification = build.getNotification();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            return notification;
        }
    }
}
