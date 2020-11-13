package com.flys.fragments.behavior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.architecture.custom.Session;
import com.flys.dao.db.NotificationDao;
import com.flys.dao.db.NotificationDaoImpl;
import com.flys.generictools.dao.daoException.DaoException;
import com.flys.notification.adapter.NotificationAdapter;
import com.flys.notification.dialog.DialogStyle;
import com.flys.notification.dialog.NotificationDetailsDialogFragment;
import com.flys.notification.domain.Notification;
import com.flys.tools.dialog.MaterialNotificationDialog;
import com.flys.tools.domain.NotificationData;
import com.flys.tools.utils.FileUtils;
import com.flys.tools.utils.Utils;
import com.flys.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@EFragment(R.layout.fragment_notif_layout)
@OptionsMenu(R.menu.menu_home)
public class NotificationFragment extends AbstractFragment implements MaterialNotificationDialog.NotificationButtonOnclickListeneer, NotificationAdapter.NotificationOnclickListener {

    @ViewById(R.id.notification_main_layout)
    protected ConstraintLayout mainLayout;
    @ViewById(R.id.recycler)
    protected RecyclerView recyclerView;
    @ViewById(R.id.notifications_empty_id)
    protected TextView notificationsEmptyMsg;
    @OptionsMenuItem(R.id.search)
    protected MenuItem menuItem;

    //Sauvegarde et restauration des données avec firebase storage
    protected FirebaseStorage storage;
    //Notification details
    private NotificationDetailsDialogFragment configDialogFragment;

    @Bean(NotificationDaoImpl.class)
    protected NotificationDao notificationDao;

    protected SearchView searchView;
    private List<Notification> notifications;
    private NotificationAdapter notificationAdapter;

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
        notifications = new ArrayList<>();
        try {
            if (notificationDao.getAll() != null) {
                notifications = notificationDao.getAll().stream()
                        .distinct()
                        .sorted(Comparator.comparing(Notification::getDate).reversed())
                        .collect(Collectors.toList());
            }
        } catch (DaoException e) {
            Log.e(getClass().getSimpleName(), "Notification list getting from database Processing Exception", e);
        }
        //mise des informations dans la session
        session.setNotifications(notifications);
        //Are notifications disabled on the device
        notificationAdapter = new NotificationAdapter(activity, notifications, new DialogStyle(activity.getColor(R.color.red_700), Constants.FONTS_OPEN_SANS_REGULAR_TTF),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(notificationAdapter);
    }

    @Override
    protected void initView(CoreState previousState) {

    }

    @Override
    protected void updateOnSubmit(CoreState previousState) {
        mainActivity.clearNotification();
        mainActivity.activateMainButtonMenu(R.id.bottom_menu_me);

        //Ya t'il de nouvelle notification
        if (!Session.getNotifications().isEmpty()) {
            notifications = Session.getNotifications();
        }
        //Is notifications empty?
        if (notifications.isEmpty()) {
            mainLayout.setBackgroundColor(activity.getColor(R.color.grey_200));
            notificationsEmptyMsg.setVisibility(View.VISIBLE);
        } else {
            notificationsEmptyMsg.setVisibility(View.GONE);
            if (!NotificationManagerCompat.from(DApplicationContext.getContext()).areNotificationsEnabled()) {
                MaterialNotificationDialog dialog = new MaterialNotificationDialog(activity, new NotificationData(getString(R.string.app_name), "Veuillez activer les notifications\npour recevoir des nouveaux apprentissages", "OK", "NON", getActivity().getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_Light_Dialog_Alert), this);
                dialog.show(getActivity().getSupportFragmentManager(), "material_notification_alert_dialog");
            }
            notifications.stream()
                    .filter(notification -> !com.flys.architecture.core.Utils.fileExist("glearning", notification.getImageName(), activity))
                    .distinct()
                    .forEachOrdered(notification -> {
                        final long ONE_MEGABYTE = 1024L* 1024;
                        storage.getReference().child(notification.getImageName()).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                            //Sauvegarde de l'image dans le local storage
                            FileUtils.saveToInternalStorage(bytes, "glearning", notification.getImageName(), activity);
                            //Refresh adapter to take in count the changes
                            notificationAdapter.refreshAdapter();
                            recyclerView.setVisibility(View.VISIBLE);
                        }).addOnFailureListener(exception -> {
                            // Handle any errors
                        });
                    });
        }

    }

    @Override
    protected void updateOnRestore(CoreState previousState) {
        Toast.makeText(activity, "updateOnRestore", Toast.LENGTH_SHORT).show();
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
        // on récupère le client choisi
        searchView = (SearchView) menuItem.getActionView();
        changeSearchTextColor(searchView);
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
     * @param view
     */
    private void changeSearchTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(ContextCompat.getColor(activity, R.color.red_A700));
                ((TextView) view).setTextSize(14);
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchTextColor(viewGroup.getChildAt(i));
                }
            }
        }
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
    public void onButtonClickListener(int position) {
        configDialogFragment = NotificationDetailsDialogFragment.newInstance(activity, notifications.get(position), new DialogStyle(activity.getColor(R.color.red_A700), Constants.FONTS_OPEN_SANS_REGULAR_TTF));
        configDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        configDialogFragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_name" + position);
    }

    @Override
    public void onMenuClickListener(View view, int position) {
        showMenu(activity, view, R.menu.notification_popup_menu, position);
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
                    Utils.shareText(context, "Dubun Guiziga", HtmlCompat.fromHtml(notifications.get(position).getContent().concat("</br>").concat("https://play.google.com/store/apps/details?id=com.flys.glearning"), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(), "ƁIMUTOHO MIPAL");
                    break;
                case R.id.popmenu_delete:
                    try {
                        notificationDao.delete(notifications.get(position));
                        notifications.remove(position);
                        notificationAdapter.notifyDataSetChanged();
                        com.flys.architecture.core.Utils.showErrorMessage(activity, activity.findViewById(R.id.main_content), activity.getColor(R.color.red_700), "Supprimée !");
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

}
