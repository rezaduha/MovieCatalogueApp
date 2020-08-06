package com.example.proyekakhir_aplikasimoviecatalogue.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.reminder.ReminderReceiver;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREF_RELEASE_REMINDER = "pref_release_reminder";
    public static final String PREF_DAILY_REMINDER = "pref_daily_reminder";
    public static final String SWITCH_DAILY_REMINDER = "switch_daily_reminder";
    public static final String SWITCH_RELEASE_REMINDER = "switch_release_reminder";

    private CompoundButton switchReleaseReminder, switchDailyReminder;

    private ReminderReceiver reminderReceiver;

    private SharedPreferences prefReleaseReminder, prefDailyReminder;
    private SharedPreferences.Editor editorReleaseReminder, editorDailyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchReleaseReminder = findViewById(R.id.swc_release_reminder);
        switchDailyReminder = findViewById(R.id.swc_daily_reminder);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.settings);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setPreferences();
        reminderReceiver = new ReminderReceiver();

        switchReleaseReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editorReleaseReminder = prefReleaseReminder.edit();
                if (isChecked) {
                    editorReleaseReminder.putBoolean(SWITCH_RELEASE_REMINDER, true);
                    editorReleaseReminder.apply();

                    String time = "08:00";
                    String message = getString(R.string.release_notification_message);
                    reminderReceiver.setReleaseReminder(SettingsActivity.this, ReminderReceiver.TYPE_RELEASE_REMINDER, time, message);
                } else {
                    editorReleaseReminder.putBoolean(SWITCH_RELEASE_REMINDER, false);
                    editorReleaseReminder.apply();
                    reminderReceiver.cancelReminder(SettingsActivity.this, ReminderReceiver.TYPE_RELEASE_REMINDER);
                }
            }
        });

        switchDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editorDailyReminder = prefDailyReminder.edit();
                if (isChecked) {
                    editorDailyReminder.putBoolean(SWITCH_DAILY_REMINDER, true);
                    editorDailyReminder.apply();

                    String time = "07:00";
                    String title = getString(R.string.app_name);
                    String message = getString(R.string.app_name) + " " + getString(R.string.daily_notification_message);
                    reminderReceiver.setDailyReminder(SettingsActivity.this, ReminderReceiver.TYPE_DAILY_REMINDER, time, title, message);
                } else {
                    editorDailyReminder.putBoolean(SWITCH_DAILY_REMINDER, false);
                    editorDailyReminder.apply();

                    reminderReceiver.cancelReminder(SettingsActivity.this, ReminderReceiver.TYPE_DAILY_REMINDER);
                }
            }
        });
    }

    private void setPreferences() {
        prefReleaseReminder = getSharedPreferences(PREF_RELEASE_REMINDER, MODE_PRIVATE);
        boolean checkReleaseReminderSwitch = prefReleaseReminder.getBoolean(SWITCH_RELEASE_REMINDER, false);
        switchReleaseReminder.setChecked(checkReleaseReminderSwitch);

        prefDailyReminder = getSharedPreferences(PREF_DAILY_REMINDER, MODE_PRIVATE);
        boolean checkSwitchDailyReminder = prefDailyReminder.getBoolean(SWITCH_DAILY_REMINDER, false);
        switchDailyReminder.setChecked(checkSwitchDailyReminder);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
