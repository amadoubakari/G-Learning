package com.flys.fragments.behavior;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.common_tools.dialog.MaterialNotificationDialog;
import com.flys.common_tools.domain.NotificationData;
import com.flys.common_tools.utils.Utils;
import com.flys.dao.db.NotificationDao;
import com.flys.dao.db.NotificationDaoImpl;
import com.flys.generictools.dao.daoException.DaoException;
import com.flys.notification.adapter.NotificationAdapter;
import com.flys.notification.domain.Notification;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
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
        try {
            notifications = notificationDao.getAll();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        //Is notifications empty?
        if (notifications.isEmpty()) {
            mainLayout.setBackgroundColor(activity.getColor(R.color.grey_200));
            notificationsEmptyMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView(CoreState previousState) {

        //Is notifications empty?
        if (notifications.isEmpty()) {
            mainLayout.setBackgroundColor(activity.getColor(R.color.grey_200));
            notificationsEmptyMsg.setVisibility(View.VISIBLE);
        } else {
            //Are notifications disabled on the device
            notificationAdapter = new NotificationAdapter(activity, notifications, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(notificationAdapter);
            if (!NotificationManagerCompat.from(DApplicationContext.getContext()).areNotificationsEnabled()) {
                MaterialNotificationDialog dialog = new MaterialNotificationDialog(activity, new NotificationData(getString(R.string.app_name), "Veuillez activer les notifications\npour recevoir des nouveaux apprentissages", "OK", "NON", getActivity().getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_Light_Dialog_Alert), this);
                dialog.show(getActivity().getSupportFragmentManager(), "material_notification_alert_dialog");
            }
        }
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
        List<Notification> list = new ArrayList<>();
        for (Notification notification : notifications) {
            if ((
                    notification.getTitle().contains(query.toLowerCase()) ||
                            notification.getSubTitle().contains(query.toLowerCase()) ||
                            notification.getContent().contains(query.toLowerCase()))) {
                list.add(notification);
            }
        }
        return list;
    }

    @Override
    public void onClickListener(int position) {
        Toast.makeText(activity, notifications.get(position).getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuListener(View view, int position) {
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
     *
     * @param context
     * @param anchor
     * @param custom_menu
     * @param position
     * @return
     */
    public boolean showMenu(Context context, View anchor, int custom_menu, int position) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(custom_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popmenu_share:
                    Utils.shareText(context, "Dubun Guiziga", HtmlCompat.fromHtml(notifications.get(position).getContent(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(), "ƁIMUTOHO MIPAL");
                    break;
                case R.id.popmenu_delete:
                    try {
                        notificationDao.delete(notifications.get(position));
                        notifications.remove(position);
                        notificationAdapter.notifyDataSetChanged();
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return false;
        });
        popup.show();
        return true;
    }
}
