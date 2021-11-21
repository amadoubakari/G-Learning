package com.flys.fragments.behavior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.Utils;
import com.flys.architecture.custom.CoreState;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.dao.db.NotificationDao;
import com.flys.dao.db.NotificationDaoImpl;
import com.flys.generictools.dao.daoException.DaoException;
import com.flys.notification.adapter.AdsSimpleNotificationAdapter;
import com.flys.notification.dialog.DialogStyle;
import com.flys.notification.dialog.NotificationDetailsDialogFragment;
import com.flys.notification.domain.Notification;
import com.flys.tools.dialog.MaterialNotificationDialog;
import com.flys.tools.domain.NotificationData;
import com.flys.tools.utils.FileUtils;
import com.flys.utils.Constants;
import com.google.firebase.storage.FirebaseStorage;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EFragment(R.layout.fragment_notif_layout)
@OptionsMenu(R.menu.menu_home)
public class NotificationFragment extends AbstractFragment implements MaterialNotificationDialog.NotificationButtonOnclickListeneer, AdsSimpleNotificationAdapter.NotificationOnclickListener {

    @ViewById(R.id.recycler)
    protected RecyclerView recyclerView;

    @ViewById(R.id.notification_not_found_msg_id)
    protected LinearLayout llNotificationsEmptyMsg;

    @OptionsMenuItem(R.id.search)
    protected MenuItem menuItem;

    @Bean(NotificationDaoImpl.class)
    protected NotificationDao notificationDao;

    //Sauvegarde et restauration des données avec firebase storage
    protected FirebaseStorage storage;

    //Notification details
    private NotificationDetailsDialogFragment configDialogFragment;

    protected SearchView searchView;
    private static List<Notification> notifications;
    private AdsSimpleNotificationAdapter notificationAdapter;


    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.NOTIFICATION_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {
        ((AppCompatActivity) mainActivity).getSupportActionBar().show();
        mainActivity.activateMainButtonMenu(R.id.bottom_menu_me);
        storage = FirebaseStorage.getInstance();
        //init();
    }

    @Override
    protected void initView(CoreState previousState) {

    }

    @Override
    protected void updateOnSubmit(CoreState previousState) {
        mainActivity.clearNotification();
        mainActivity.activateMainButtonMenu(R.id.bottom_menu_me);

        init();

        if (!NotificationManagerCompat.from(DApplicationContext.getContext()).areNotificationsEnabled()) {
            MaterialNotificationDialog dialog = new MaterialNotificationDialog(activity, new NotificationData(getString(R.string.app_name), "Veuillez activer les notifications\npour recevoir des nouveaux apprentissages", "OK", "NON", getActivity().getDrawable(R.drawable.logo), R.style.customMaterialAlertEditDialog), this);
            dialog.show(getActivity().getSupportFragmentManager(), "material_notification_alert_dialog");
        }

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

    @OptionsItem(R.id.search)
    protected void doSearch() {
        searchView = (SearchView) menuItem.getActionView();
        com.flys.architecture.core.Utils.changeSearchTextColor(activity, searchView, R.font.google_sans);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //whether we have one caracter at least
                if (searchView.getQuery().length() > 0) {
                    notificationAdapter.setFilter(filter(notifications, newText));
                } else {
                    notificationAdapter.setFilter(filter(notifications, ""));
                }
                return true;
            }
        });
    }

    /**
     * @param notifications
     * @param query
     * @return
     */
    private List<Notification> filter(List<Notification> notifications, String query) {
        return notifications.stream().filter(notification -> notification.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                notification.getSubTitle().toLowerCase().contains(query.toLowerCase()) ||
                notification.getContent().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

    }

    @Override
    public void onShowMoreClickListener(int position) {
        configDialogFragment = NotificationDetailsDialogFragment.newInstance(activity, notifications.get(position), new DialogStyle(activity.getColor(R.color.app_text_color), activity.getColor(R.color.app_text_second_color), R.font.google_sans));
        configDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        configDialogFragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_name" + position);
    }

    @Override
    public void onMenuClickListener(View view, int position) {
        showMenu(activity, view, R.menu.notification_popup_menu, position);
    }

    @Override
    public void onShareClickListener(int position) {
        com.flys.tools.utils.Utils.shareText(activity, getString(R.string.app_name), HtmlCompat.fromHtml(notifications.get(position).getContent().concat("</br>").concat(getString(R.string.app_google_play_store_url)), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(), getString(R.string.app_name));
    }

    @Override
    public void okButtonAction(DialogInterface dialogInterface, int i) {
        Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
        startActivityForResult(settingsIntent, 32);
    }

    @Override
    public void noButtonAction(DialogInterface dialogInterface, int i) {

    }

    /**
     * @param context
     * @param anchor
     * @param custom_menu
     * @param position
     * @return
     */
    @SuppressLint("RestrictedApi")
    public boolean showMenu(Context context, View anchor, int custom_menu, int position) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(custom_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popmenu_share:
                    com.flys.tools.utils.Utils.shareText(context, getString(R.string.app_name), HtmlCompat.fromHtml(notifications.get(position).getContent().concat("</br>").concat(getString(R.string.app_google_play_store_url)), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(), getString(R.string.app_name));
                    break;
                case R.id.popmenu_delete:
                    try {
                        notificationDao.delete(notifications.get(position));
                        notifications.remove(position);
                        notificationAdapter.notifyDataSetChanged();
                        Utils.showErrorMessage(activity, activity.findViewById(R.id.main_content), activity.getColor(R.color.blue_500), getString(R.string.delete_msg));
                    } catch (DaoException e) {
                        Log.e(getClass().getSimpleName(), "Deleting notification from database Processing Exception", e);
                    }
                    break;
            }
            return false;
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) popup.getMenu(), anchor);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
        return true;
    }

    /**
     * Update images to the recyclerview from the external storage
     */
    private void updateNotificationsImages(List<Notification> notifications) {
        notifications.stream()
                .filter(notification -> !Utils.fileExist(Constants.DIR_NAME, notification.getImageName(), activity) ||  !Utils.fileExist(Constants.DIR_NAME, notification.getSourceIcon(), activity))
                .distinct()
                .forEach(notification -> {
                    Log.e(getClass().getSimpleName(),"=============  Notification Fragment : "+notification);
                    final long ONE_MEGABYTE = 1024L * 1024;
                    storage.getReference().child("notifications").child(notification.getImageName()).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                        //Sauvegarde de l'image dans le local storage
                        FileUtils.saveToInternalStorage(bytes, Constants.DIR_NAME, notification.getImageName(), activity);
                        //Refresh adapter to take in count the changes
                        notificationAdapter.refreshAdapter();
                    }).addOnFailureListener(exception -> {
                        // Handle any errors
                    });
                    storage.getReference().child("notifications").child(notification.getSourceIcon()).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                        //Sauvegarde de l'image dans le local storage
                        FileUtils.saveToInternalStorage(bytes, Constants.DIR_NAME, notification.getSourceIcon(), activity);
                        //Refresh adapter to take in count the changes
                        notificationAdapter.refreshAdapter();
                    }).addOnFailureListener(exception -> {
                        // Handle any errors
                    });
                });
    }

    /**
     * Load view data
     */
    private void init() {
        notifications = new ArrayList<>();
        notificationAdapter = new AdsSimpleNotificationAdapter(activity, notifications, new DialogStyle(activity.getColor(R.color.app_text_color), activity.getColor(R.color.app_text_second_color), R.font.google_sans), false /*Constants.isNetworkConnected*/,activity.getString(R.string.fragment_notification_item_ads_native),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        beginRunningTasks(1);
        executeInBackground(mainActivity.loadNotificationsFromDatabase().delay(100, TimeUnit.MILLISECONDS), notifications1 -> {
            if (notifications1.isEmpty()) {
                llNotificationsEmptyMsg.setVisibility(View.VISIBLE);
            } else {
                llNotificationsEmptyMsg.setVisibility(View.GONE);
                notificationAdapter.addAll(notifications1);
                updateNotificationsImages(notifications1);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mainActivity.scrollUp();
                } else {
                    // Scrolling down
                    mainActivity.scrollDown();
                }
            }
        });
    }
}

