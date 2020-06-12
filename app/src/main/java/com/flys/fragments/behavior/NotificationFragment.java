package com.flys.fragments.behavior;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.common_tools.dialog.MaterialNotificationDialog;
import com.flys.common_tools.domain.NotificationData;
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
            notifications=notificationDao.getAll();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        //notifications = new ArrayList<>();
        //notifications.addAll(Session.getNotifications());
       /* for (int i = 0; i < 10; i++) {
            notifications.add(new Notification("title", "Subtitle", "«Gwat kum magaka ar sipi jiviɗ kumo, jam lesl, anja ki gaka a mihiri mizli ɓa a purukum ta. Da kanah na, naɓa Cine kum misi a muŋ anta sa wurkukum ta"));
        }*/
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
        }else {
            //Are notifications disabled on the device
            notificationAdapter = new NotificationAdapter(activity, notifications, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(notificationAdapter);
            if (!NotificationManagerCompat.from(DApplicationContext.getContext()).areNotificationsEnabled()) {
                MaterialNotificationDialog dialog = new MaterialNotificationDialog(activity, new NotificationData(getString(R.string.app_name), "Veuillez activer les notifications\npour recevoir des nouveaux apprentissages", "OK", "NON", getActivity().getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_Light_Dialog_Alert), this);
                View view = dialog.getView();
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
                }
                return true;
            }


        });
        ImageView closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "zzzzzzzzzzzz", Toast.LENGTH_LONG).show();
            }
        });
     /*   searchView.setOnCloseListener(() -> {
            notificationAdapter.setFilter(filter(notifications, ""));
            return true;
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) notificationAdapter.notifyDataSetChanged();
        });*/
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
    private List<Notification> filter(List<Notification> notifications, final String query) {
        List<Notification> list = new ArrayList<>();
        if (!query.isEmpty()) {
            list = notifications.stream()
                    .filter(notification -> notification.getTitle().contains(query.toLowerCase())
                            || notification.getSubTitle().contains(query.toLowerCase())
                            || notification.getContent().contains(query.toLowerCase()))
                    .collect(Collectors.toList());

        }
        return list;
    }

    @Override
    public void onClickListener(int position) {
        Toast.makeText(activity, notifications.get(position).getContent(), Toast.LENGTH_SHORT).show();
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
}
