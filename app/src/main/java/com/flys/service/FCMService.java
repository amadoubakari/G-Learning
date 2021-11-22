package com.flys.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.InboxStyle;


import com.flys.R;
import com.flys.activity.MainActivity_;
import com.flys.dao.db.NotificationDao;
import com.flys.dao.db.NotificationDaoImpl;
import com.flys.dao.service.Dao;
import com.flys.dao.service.IDao;
import com.flys.generictools.dao.daoException.DaoException;
import com.flys.notification.domain.Notification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EService
public class FCMService extends FirebaseMessagingService {

    @Bean(NotificationDaoImpl.class)
    protected NotificationDao notificationDao;
    // couche [DAO]
    @Bean(Dao.class)
    protected IDao dao;

    private static final String TAG = "FCMService";

    private static final String GROUP_KEY_WORK_EMAIL = "com.flys.dico.FIREBASE_NOTIFICATIONS";

    private static int SUMMARY_ID = 0;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Notification notification = new Notification();
        //List<Notification> notifications = new ArrayList<>();
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            notification.setTitle(remoteMessage.getData().get("title"));
            notification.setSubTitle(remoteMessage.getData().get("subTitle"));
            notification.setContent(remoteMessage.getData().get("content"));
            notification.setImageName(remoteMessage.getData().get("image"));
            notification.setSource(remoteMessage.getData().get("source"));
            notification.setSourceIcon(remoteMessage.getData().get("sourceIcon"));

            //Saving notification in the database
            try {
                notification.setDate(new Date());
                Log.e(getClass().getSimpleName(),"=============  Notification: "+notification);
                notificationDao.save(notification);
                dao.loadNotificationsFromDatabase("seen", false).debounce(500, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(notifications -> {
                            if (notifications != null && !notifications.isEmpty()) {
                                sendNotification(this, notifications);
                            }
                        });

            } catch (DaoException e) {
                e.printStackTrace();
            }

            //sendNotification(notification);
            /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        //if (remoteMessage.getNotification() != null) {
        //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        //sendNotification(remoteMessage.getNotification().getBody());
        // }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
       /* // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(DApplicationContext.getContext()).beginWith(work).enqueue();
        // [END dispatch_job]*/
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * @param context:       from where notifications will be send
     * @param notifications: list of unread notifications
     */
    private void sendNotification(Context context, List<Notification> notifications) {
        ShortcutBadger.applyCount(context, notifications.size());
        Notification notification = notifications.get(0);
        Intent intent = new Intent(context, MainActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("notification", notification);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, SUMMARY_ID, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        //
        InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        notifications.forEach(notification1 -> inboxStyle.addLine(notification1.getContent()));
        String message = getString(R.string.fcm_notification_service_one_message);
        if (notifications.size() > 1) {
            message = getString(R.string.fcm_notification_service_no_messages);
        }
        inboxStyle.setBigContentTitle(notifications.size() + message);
        //inboxStyle.setSummaryText("janedoe@example.com");
        //
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.books)
                        .setContentTitle(notifications.size() + getString(R.string.fcm_notification_service_no_messages))
                        .setContentText(notification.getContent())
                        .setStyle(inboxStyle)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        .setGroupSummary(true)
                        .setNumber(notifications.size());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(SUMMARY_ID /* ID of notification */, notificationBuilder.build());
    }


}
