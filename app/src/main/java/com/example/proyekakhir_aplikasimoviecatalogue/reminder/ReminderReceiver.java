package com.example.proyekakhir_aplikasimoviecatalogue.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.proyekakhir_aplikasimoviecatalogue.BuildConfig;
import com.example.proyekakhir_aplikasimoviecatalogue.Data;
import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.activity.MainActivity;
import com.example.proyekakhir_aplikasimoviecatalogue.activity.SettingsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String TYPE_RELEASE_REMINDER = "type_release_reminder";
    public static final String TYPE_DAILY_REMINDER = "type_daily_reminder";
    public static final String GROUP_RELEASE_REMINDER = "group_release_reminder";
    public static final String GROUP_DAILY_REMINDER = "group_daily_reminder";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_TITLE = "extra_title";

    private final int ID_RELEASE_REMINDER = 100;
    private final int ID_DAILY_REMINDER = 101;

    Calendar calendar;
    Intent intent;

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        int notifId;
        int countNotif = 0;
        String groupKey, title;
        assert type != null;

        if (type.equals(TYPE_RELEASE_REMINDER)) {
            notifId = ID_RELEASE_REMINDER;
            groupKey = GROUP_RELEASE_REMINDER;
            getMovieRelease(context, message, groupKey, notifId);
        } else {
            notifId = ID_DAILY_REMINDER;
            title = intent.getStringExtra(EXTRA_TITLE);
            groupKey = GROUP_DAILY_REMINDER;
            showReminderNotification(context, title, message, groupKey, notifId, countNotif, null);
        }
    }

    private void getMovieRelease(final Context context, final String message, final String groupKey, final int notifId) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Data> listItems = new ArrayList<>();
        final String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.TMDB_API_KEY + "&primary_release_date.gte=" + currentDate + "&primary_release_date.lte=" + currentDate;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                int countNotif = 0;
                String messageNotif;
                String title;
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        Data data = new Data();

                        data.setId(movie.getInt("id"));
                        data.setMovieName(movie.getString("title"));
                        data.setDescription(movie.getString("overview"));
                        data.setPhoto(movie.getString("poster_path"));
                        data.setRelease(movie.getString("release_date"));

                        if (movie.getString("release_date").equals(currentDate)) {
                            title = data.getMovieName();
                            messageNotif = title + " " + message;
                            listItems.add(data);
                            showReminderNotification(context, title, messageNotif, groupKey, notifId, countNotif, listItems);
                            countNotif++;
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());

            }
        });
    }

    public void setReleaseReminder(Context context, String type, String time, String message) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;
        setIntent(context, type, null, message);
        setReminderTime(time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, 0);
        setAlarmManager(context, pendingIntent);
    }

    public void setDailyReminder(Context context, String type, String time, String title, String message) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;
        setIntent(context, type, title, message);
        setReminderTime(time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        setAlarmManager(context, pendingIntent);
    }

    private void setAlarmManager(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void setReminderTime(String time) {
        String[] timeArray = time.split(":");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
    }

    private void setIntent(Context context, String type, String title, String message) {
        intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_TITLE, title);
    }

    private boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    public void cancelReminder(Context context, String type) {
        Intent intent = new Intent(context, SettingsActivity.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void showReminderNotification(Context context, String title, String message, String groupKey, int notifId, int countNotif, List<Data> listData) {

        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Reminder channel";
        Intent intent;

        if (groupKey.equals(GROUP_RELEASE_REMINDER)) {
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setGroup(groupKey)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            String NotificationID = String.valueOf(notifId) + countNotif;
            int NotificationId = Integer.parseInt(NotificationID);

            notificationManagerCompat.notify(NotificationId, notification);
        }
    }
}
