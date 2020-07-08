package com.flys.fragments.behavior;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;
import com.flys.tools.dialog.MaterialNotificationDialog;
import com.flys.tools.domain.NotificationData;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_settings_layout)
@OptionsMenu(R.menu.menu_home)
public class SettingsFragment extends AbstractFragment {
    private static final int SCAD_SETTINGS_NOTIFICATION_REQUEST_CODE = 32;
    @ViewById(R.id.notification_switch)
    protected Switch enableNotification;

    @Click(R.id.notification_switch)
    public void settings() {
        if (enableNotification.isChecked()) {
            MaterialNotificationDialog notificationDialog = new MaterialNotificationDialog(activity, new NotificationData("Dubun Guiziga", "Activez les notifications pour recevoir des nouveautés", "OUI", "NON", activity.getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_Light_Dialog_Alert), new MaterialNotificationDialog.NotificationButtonOnclickListeneer() {
                @Override
                public void okButtonAction(DialogInterface dialogInterface, int i) {
                    Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                    startActivityForResult(settingsIntent, SCAD_SETTINGS_NOTIFICATION_REQUEST_CODE);
                }

                @Override
                public void noButtonAction(DialogInterface dialogInterface, int i) {
                    enableNotification.setChecked(false);
                    dialogInterface.dismiss();
                }
            });
            notificationDialog.show(getActivity().getSupportFragmentManager(), "settings_notification_dialog_tag");
        }

    }

    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.SETTINGS_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {
        enableNotification.setChecked(NotificationManagerCompat.from(activity).areNotificationsEnabled());
    }

    @Override
    protected void initView(CoreState previousState) {

    }

    @Override
    protected void updateOnSubmit(CoreState previousState) {

    }

    @Override
    protected void updateOnRestore(CoreState previousState) {

    }

    @Override
    protected void notifyEndOfUpdates() {

    }

    @Override
    protected void notifyEndOfTasks(boolean runningTasksHaveBeenCanceled) {

    }

    @Override
    protected boolean hideNavigationBottomView() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SCAD_SETTINGS_NOTIFICATION_REQUEST_CODE) {
           //Checked
            if(NotificationManagerCompat.from(activity).areNotificationsEnabled()){
                enableNotification.setChecked(true);
            }else {
                enableNotification.setChecked(false);
            }
        }
    }
}
