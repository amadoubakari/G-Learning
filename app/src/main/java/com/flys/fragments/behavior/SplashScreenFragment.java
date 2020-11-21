package com.flys.fragments.behavior;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.custom.CoreState;

@EFragment(R.layout.fragment_splash_screen)
@OptionsMenu(R.menu.menu_vide)
public class SplashScreenFragment extends AbstractFragment {
    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.SPLASHSCREEN_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {
       // ((AppCompatActivity) mainActivity).getSupportActionBar().hide();
    }

    @Override
    protected void initView(CoreState previousState) {
        //Necessary time in milisecond to launch the application
        int WAITING_TIME = 1500;
        new Handler().postDelayed(() -> {
            mainActivity.navigateToView(mainActivity.FISH_FRAGMENT, ISession.Action.NONE);
        }, WAITING_TIME);
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

    //Nous cachons le bottom navigation view

    @Override
    protected boolean hideNavigationBottomView() {
        return true;
    }
}
