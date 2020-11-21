package com.flys.fragments.behavior;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.custom.CoreState;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.tools.utils.CommonToolsConstants;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.listener.StateUpdatedListener;
import com.kyossi.firebase.tools.FirebaseAction;
import com.kyossi.firebase.tools.FirebaseCommonTools;
import com.kyossi.firebase.tools.UpdateProcess;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_fish_layout)
@OptionsMenu(R.menu.menu_home)
public class FishFragment extends AbstractFragment implements StateUpdatedListener<InstallState> {

    private static boolean askedForUpdate = false;
    // Creates instance of the update app manager.
    private AppUpdateManager appUpdateManager;

    @ViewById(R.id.image)
    protected ImageView icon;

    @Click(R.id.next)
    protected void nextChapitre() {
        mainActivity.navigateToView(mainActivity.DOG_FRAGMENT, ISession.Action.SUBMIT);
    }

    @Click(R.id.previous)
    protected void previousChapitre() {

    }

    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.FISH_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {
        ((AppCompatActivity) mainActivity).getSupportActionBar().show();
        appUpdateManager = AppUpdateManagerFactory.create(activity);
        mainActivity.activateMainButtonMenu(R.id.bottom_menu_book);


    }

    @Override
    protected void initView(CoreState previousState) {
        if (appUpdateManager == null) {
            appUpdateManager = AppUpdateManagerFactory.create(activity);
        }
    }

    @Override
    protected void updateOnSubmit(CoreState previousState) {
        if (appUpdateManager == null) {
            appUpdateManager = AppUpdateManagerFactory.create(activity);
        }
    }

    @Override
    protected void updateOnRestore(CoreState previousState) {
        if(appUpdateManager==null){
            appUpdateManager = AppUpdateManagerFactory.create(activity);
        }
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
    public void onFragmentResume() {
        super.onFragmentResume();

        if(appUpdateManager==null){
            appUpdateManager = AppUpdateManagerFactory.create(DApplicationContext.getContext());
        }
        UpdateProcess updateProcess = new UpdateProcess() {
            @Override
            public void beginUpdateProcess() {
                beginRunningTasks(1);
            }

            @Override
            public void endUpdateProcess() {
                cancelRunningTasks();
                mainActivity.popupSnackbarForCompleteUpdate(appUpdateManager);
                askedForUpdate = true;
            }
        };
        //network available and never ask update before?
        if (CommonToolsConstants.isNetworkConnected && !askedForUpdate) {
            FirebaseCommonTools.checkForUpdates(() -> {
                FirebaseCommonTools.checkUpdatesAvailable(activity, appUpdateManager, updateProcess, this);
            });
        }

        //Check downloaded updates
        FirebaseCommonTools.checkIfUpdatesDownloaded(appUpdateManager, () -> {
            mainActivity.popupSnackbarForCompleteUpdate(appUpdateManager);
        });
    }

    /**
     * Callback triggered whenever the state has changed.
     *
     * @param state
     */
    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            mainActivity.popupSnackbarForCompleteUpdate(appUpdateManager);
        }
    }
}
